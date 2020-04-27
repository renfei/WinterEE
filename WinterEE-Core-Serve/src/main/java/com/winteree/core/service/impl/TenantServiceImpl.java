package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.ListData;
import com.winteree.api.entity.RunModeEnum;
import com.winteree.api.entity.TenantDTO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.TenantDOMapper;
import com.winteree.core.dao.entity.TenantDO;
import com.winteree.core.dao.entity.TenantDOExample;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private final TenantDOMapper tenantDOMapper;

    protected TenantServiceImpl(AccountService accountService,
                                WintereeCoreConfig wintereeCoreConfig,
                                TenantDOMapper tenantDOMapper) {
        super(accountService, wintereeCoreConfig);
        this.tenantDOMapper = tenantDOMapper;
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
        if(RunModeEnum.DEMO.getMode().equals(wintereeCoreConfig.getRunMode())){
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
        if(RunModeEnum.DEMO.getMode().equals(wintereeCoreConfig.getRunMode())){
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
