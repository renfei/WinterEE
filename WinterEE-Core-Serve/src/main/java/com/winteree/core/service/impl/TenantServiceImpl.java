package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.*;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.TenantDOMapper;
import com.winteree.core.dao.TenantInfoDOMapper;
import com.winteree.core.dao.entity.*;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.GeospatialService;
import com.winteree.core.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>Title: TenantServiceImpl</p>
 * <p>Description: 租户服务</p>
 *
 * @author RenFei
 * @date : 2020-04-26 21:22
 */
@Slf4j
@Service
public class TenantServiceImpl extends BaseService implements TenantService {
    /**
     * 租户管理的权限标识
     */
    private static final String TENANT_MANAGE_AUTHORITIES = "platf:tenant:manage";
    private final AccountService accountService;
    private final GeospatialService geospatialService;
    private final TenantDOMapper tenantDOMapper;
    private final TenantInfoDOMapper tenantInfoDOMapper;

    protected TenantServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                AccountService accountService,
                                GeospatialService geospatialService,
                                TenantDOMapper tenantDOMapper,
                                TenantInfoDOMapper tenantInfoDOMapper) {
        super(wintereeCoreConfig);
        this.accountService = accountService;
        this.geospatialService = geospatialService;
        this.tenantDOMapper = tenantDOMapper;
        this.tenantInfoDOMapper = tenantInfoDOMapper;
    }

    /**
     * 获取所有租户列表，需要自己管理权限
     *
     * @return
     */
    @Override
    public APIResult<ListData<TenantDTO>> getTenantList() {
        AccountDTO accountDTO = getSignedUser(accountService);
        AtomicBoolean manage = new AtomicBoolean(false);
        accountDTO.getAuthorities().forEach(i -> {
            if (TENANT_MANAGE_AUTHORITIES.equals(i)) {
                manage.set(true);
            }
        });
        if (manage.get()) {
            TenantDOExample tenantDOExample = new TenantDOExample();
            tenantDOExample.createCriteria();
            Page page = PageHelper.startPage(1, Integer.MAX_VALUE);
            List<TenantDO> tenantDOList = tenantDOMapper.selectByExample(tenantDOExample);
            List<TenantDTO> tenantDTOS = new ArrayList<>();
            ListData<TenantDTO> tenantDTOListData = new ListData<>();
            if (BeanUtils.isEmpty(tenantDOList)) {
                tenantDTOListData.setTotal(0L);
                tenantDTOListData.setData(tenantDTOS);
                return APIResult.builder()
                        .code(StateCode.OK)
                        .message("OK")
                        .data(tenantDTOListData)
                        .build();
            } else {
                tenantDTOListData.setTotal(page.getTotal());
                for (TenantDO tenantDO : tenantDOList
                ) {
                    tenantDTOS.add(convert(tenantDO));
                }
                tenantDTOListData.setData(tenantDTOS);
            }
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(tenantDTOListData)
                    .build();
        } else {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message("")
                    .build();
        }
    }

    /**
     * 获取所有租户列表
     *
     * @param page 页数
     * @param rows 每页行数
     * @return
     */
    @Override
    public APIResult<ListData<TenantDTO>> getAllTenant(int page, int rows) {
        TenantDOExample tenantDOExample = new TenantDOExample();
        tenantDOExample.createCriteria();
        Page page1 = PageHelper.startPage(page, rows);
        List<TenantDO> tenantDOList = tenantDOMapper.selectByExample(tenantDOExample);
        ListData<TenantDTO> tenantDTOListData = new ListData<>();
        List<TenantDTO> tenantDTOS = new ArrayList<>();
        if (BeanUtils.isEmpty(tenantDOList)) {
            tenantDTOListData.setTotal(0L);
        } else {
            tenantDTOListData.setTotal(page1.getTotal());
            for (TenantDO tenantDO : tenantDOList
            ) {
                tenantDTOS.add(convert(tenantDO));
            }
        }
        tenantDTOListData.setData(tenantDTOS);
        return APIResult.builder()
                .code(StateCode.OK)
                .data(tenantDTOListData)
                .build();
    }

    @Override
    public APIResult addTenant(TenantDTO tenantDTO) {
        if (RunModeEnum.DEMO.getMode().equals(wintereeCoreConfig.getRunMode())) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message("演示模式，禁止修改数据，只允许查看")
                    .build();
        }
        TenantDO tenantDO = convert(tenantDTO);
        tenantDO.setId(null);
        tenantDO.setCreateTime(new Date());
        tenantDO.setUpdateTime(null);
        tenantDO.setUuid(UUID.randomUUID().toString());
        try {
            tenantDOMapper.insertSelective(tenantDO);
            return APIResult.builder()
                    .code(StateCode.OK)
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("Failure")
                    .build();
        }
    }

    @Override
    public APIResult updateTenant(TenantDTO tenantDTO) {
        if (RunModeEnum.DEMO.getMode().equals(wintereeCoreConfig.getRunMode())) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message("演示模式，禁止修改数据，只允许查看")
                    .build();
        }
        TenantDO tenantDO = convert(tenantDTO);
        tenantDO.setUpdateTime(new Date());
        TenantDOExample tenantDOExample = new TenantDOExample();
        tenantDOExample.createCriteria()
                .andUuidEqualTo(tenantDO.getUuid());
        try {
            tenantDOMapper.updateByExampleSelective(tenantDO, tenantDOExample);
            return APIResult.builder()
                    .code(StateCode.OK)
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("Failure")
                    .build();
        }
    }

    @Override
    public APIResult<TenantDO> getTenantDOByUUID(String uuid) {
        TenantDOExample tenantDOExample = new TenantDOExample();
        tenantDOExample.createCriteria()
                .andUuidEqualTo(uuid);
        TenantDO tenantDO = ListUtils.getOne(tenantDOMapper.selectByExample(tenantDOExample));
        if (tenantDO == null) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("Failure")
                    .build();
        }
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(tenantDO)
                .build();
    }

    /**
     * 获取租户信息（开放服务，无需身份校验）
     *
     * @param tenantUUID 租户UUID
     * @return
     */
    @Override
    public APIResult<TenantInfoDTO> getTenantInfo(String tenantUUID) {
        APIResult<TenantDO> apiResult = this.getTenantDOByUUID(tenantUUID);
        TenantDO tenantDO = null;
        if (StateCode.OK.getCode().equals(apiResult.getCode())) {
            TenantInfoDTO tenantInfoDTO = new TenantInfoDTO();
            tenantDO = apiResult.getData();
            tenantInfoDTO.setTenantUuid(tenantUUID);
            tenantInfoDTO.setName(tenantDO.getName());
            TenantInfoDOExample tenantInfoDOExample = new TenantInfoDOExample();
            tenantInfoDOExample.createCriteria()
                    .andTenantUuidEqualTo(tenantUUID);
            TenantInfoDOWithBLOBs tenantInfoDOWithBLOBs = ListUtils.getOne(tenantInfoDOMapper.selectByExampleWithBLOBs(tenantInfoDOExample));
            if (tenantInfoDOWithBLOBs != null) {
                tenantInfoDTO.setAddress(tenantInfoDOWithBLOBs.getAddress());
                tenantInfoDTO.setAdministrators(tenantInfoDOWithBLOBs.getAdministrators());
                tenantInfoDTO.setContact(tenantInfoDOWithBLOBs.getContact());
            }
            GeospatialDO geospatialDO = geospatialService.getGeospatialByFkIdAndType(tenantUUID, GeospatialEnum.TENANT);
            if (geospatialDO != null) {
                tenantInfoDTO.setLongitude(geospatialDO.getLongitude());
                tenantInfoDTO.setLatitude(geospatialDO.getLatitude());
            }
            return APIResult.builder()
                    .data(tenantInfoDTO)
                    .code(StateCode.OK)
                    .message("OK")
                    .build();
        }
        return APIResult.builder()
                .code(StateCode.Failure)
                .message("Failure")
                .build();
    }

    /**
     * 更新租户信息
     *
     * @param tenantInfoDTO 租户信息
     * @return
     */
    @Override
    public APIResult updateTenantInfo(TenantInfoDTO tenantInfoDTO) {
        //先查询租户是否存在
        APIResult<TenantDO> apiResult = this.getTenantDOByUUID(tenantInfoDTO.getTenantUuid());
        if (StateCode.OK.getCode().equals(apiResult.getCode())) {
            //查询租户信息是否存在
            TenantInfoDOExample tenantInfoDOExample = new TenantInfoDOExample();
            tenantInfoDOExample.createCriteria()
                    .andTenantUuidEqualTo(tenantInfoDTO.getTenantUuid());
            TenantInfoDOWithBLOBs tenantInfoDOWithBLOBs = ListUtils.getOne(tenantInfoDOMapper.selectByExampleWithBLOBs(tenantInfoDOExample));
            if (tenantInfoDOWithBLOBs == null) {
                tenantInfoDOWithBLOBs = new TenantInfoDOWithBLOBs();
                tenantInfoDOWithBLOBs.setUuid(UUID.randomUUID().toString());
                tenantInfoDOWithBLOBs.setTenantUuid(tenantInfoDTO.getTenantUuid());
                tenantInfoDOWithBLOBs.setAddress(tenantInfoDTO.getAddress());
                tenantInfoDOWithBLOBs.setAdministrators(tenantInfoDTO.getAdministrators());
                tenantInfoDOWithBLOBs.setContact(tenantInfoDTO.getContact());
                tenantInfoDOMapper.insertSelective(tenantInfoDOWithBLOBs);
            } else {
                tenantInfoDOWithBLOBs.setAddress(tenantInfoDTO.getAddress());
                tenantInfoDOWithBLOBs.setAdministrators(tenantInfoDTO.getAdministrators());
                tenantInfoDOWithBLOBs.setContact(tenantInfoDTO.getContact());
                tenantInfoDOMapper.updateByPrimaryKeySelective(tenantInfoDOWithBLOBs);
            }
            //更新地理空间
            GeospatialDO geospatialDO = new GeospatialDO();
            geospatialDO.setFkId(tenantInfoDTO.getTenantUuid());
            geospatialDO.setFkType(GeospatialEnum.TENANT.getType());
            geospatialDO.setLongitude(tenantInfoDTO.getLongitude());
            geospatialDO.setLatitude(tenantInfoDTO.getLatitude());
            geospatialService.updateGeospatial(geospatialDO);
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .build();
        }
        return APIResult.builder()
                .code(StateCode.Failure)
                .message("Failure")
                .build();
    }

    private TenantDO convert(TenantDTO tenantDTO) {
        return Builder.of(TenantDO::new)
                .with(TenantDO::setId, tenantDTO.getId())
                .with(TenantDO::setCreateTime, tenantDTO.getCreateTime())
                .with(TenantDO::setExpiryDate, tenantDTO.getExpiryDate())
                .with(TenantDO::setName, tenantDTO.getName())
                .with(TenantDO::setStatus, tenantDTO.getStatus())
                .with(TenantDO::setUpdateTime, tenantDTO.getUpdateTime())
                .with(TenantDO::setUuid, tenantDTO.getUuid())
                .build();
    }

    private TenantDTO convert(TenantDO tenantDO) {
        return Builder.of(TenantDTO::new)
                .with(TenantDTO::setId, tenantDO.getId())
                .with(TenantDTO::setCreateTime, tenantDO.getCreateTime())
                .with(TenantDTO::setExpiryDate, tenantDO.getExpiryDate())
                .with(TenantDTO::setName, tenantDO.getName())
                .with(TenantDTO::setStatus, tenantDO.getStatus())
                .with(TenantDTO::setUpdateTime, tenantDO.getUpdateTime())
                .with(TenantDTO::setUuid, tenantDO.getUuid())
                .build();
    }
}
