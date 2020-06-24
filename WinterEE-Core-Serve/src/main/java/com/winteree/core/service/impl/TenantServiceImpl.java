package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.GeospatialEnum;
import com.winteree.api.entity.ListData;
import com.winteree.api.entity.TenantDTO;
import com.winteree.api.entity.TenantInfoDTO;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
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
    public ListData<TenantDTO> getTenantList() throws ForbiddenException {
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
                return tenantDTOListData;
            } else {
                tenantDTOListData.setTotal(page.getTotal());
                for (TenantDO tenantDO : tenantDOList
                ) {
                    tenantDTOS.add(convert(tenantDO));
                }
                tenantDTOListData.setData(tenantDTOS);
            }
            return tenantDTOListData;
        } else {
            throw new ForbiddenException(StateCode.Forbidden.getDescribe());
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
    public ListData<TenantDTO> getAllTenant(int page, int rows) {
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
        return tenantDTOListData;
    }

    @Override
    public int addTenant(TenantDTO tenantDTO) throws FailureException {
        TenantDO tenantDO = convert(tenantDTO);
        tenantDO.setId(null);
        tenantDO.setCreateTime(new Date());
        tenantDO.setUpdateTime(null);
        tenantDO.setUuid(UUID.randomUUID().toString());
        try {
            return tenantDOMapper.insertSelective(tenantDO);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException(StateCode.Failure.getDescribe());
        }
    }

    @Override
    public int updateTenant(TenantDTO tenantDTO) throws FailureException {
        TenantDO tenantDO = convert(tenantDTO);
        tenantDO.setUpdateTime(new Date());
        TenantDOExample tenantDOExample = new TenantDOExample();
        tenantDOExample.createCriteria()
                .andUuidEqualTo(tenantDO.getUuid());
        try {
            return tenantDOMapper.updateByExampleSelective(tenantDO, tenantDOExample);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException(StateCode.Failure.getDescribe());
        }
    }

    @Override
    public TenantDO getTenantDOByUUID(String uuid) throws FailureException {
        TenantDOExample tenantDOExample = new TenantDOExample();
        tenantDOExample.createCriteria()
                .andUuidEqualTo(uuid);
        TenantDO tenantDO = ListUtils.getOne(tenantDOMapper.selectByExample(tenantDOExample));
        if (tenantDO == null) {
            throw new FailureException(StateCode.Failure.getDescribe());
        }
        return tenantDO;
    }

    /**
     * 获取租户信息（开放服务，无需身份校验）
     *
     * @param tenantUUID 租户UUID
     * @return
     */
    @Override
    public TenantInfoDTO getTenantInfo(String tenantUUID) throws FailureException {
        TenantDO tenantDO = this.getTenantDOByUUID(tenantUUID);
        if (tenantDO != null) {
            TenantInfoDTO tenantInfoDTO = new TenantInfoDTO();
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
            return tenantInfoDTO;
        }
        throw new FailureException(StateCode.Failure.getDescribe());
    }

    /**
     * 更新租户信息
     *
     * @param tenantInfoDTO 租户信息
     * @return
     */
    @Override
    public int updateTenantInfo(TenantInfoDTO tenantInfoDTO) throws FailureException {
        //先查询租户是否存在
        TenantDO tenantDO = this.getTenantDOByUUID(tenantInfoDTO.getTenantUuid());
        if (tenantDO != null) {
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
            return 1;
        }
        throw new FailureException(StateCode.Failure.getDescribe());
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
