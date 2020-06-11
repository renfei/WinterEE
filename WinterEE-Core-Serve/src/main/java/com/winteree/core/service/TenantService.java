package com.winteree.core.service;

import com.winteree.api.entity.ListData;
import com.winteree.api.entity.TenantDTO;
import com.winteree.api.entity.TenantInfoDTO;
import com.winteree.core.dao.entity.TenantDO;
import net.renfei.sdk.entity.APIResult;

/**
 * <p>Title: TenantService</p>
 * <p>Description: 租户服务</p>
 *
 * @author RenFei
 * @date : 2020-04-26 21:22
 */
public interface TenantService {
    APIResult<ListData<TenantDTO>> getTenantList();

    /**
     * 获取所有租户列表
     *
     * @param page 页数
     * @param rows 每页行数
     * @return
     */
    APIResult<ListData<TenantDTO>> getAllTenant(int page, int rows);

    /**
     * 添加租户
     *
     * @param tenantDTO 租户实体
     * @return
     */
    APIResult addTenant(TenantDTO tenantDTO);

    /**
     * 修改租户数据
     *
     * @param tenantDTO 租户实体
     * @return
     */
    APIResult updateTenant(TenantDTO tenantDTO);

    /**
     * 根据UUID获取租户实体对象
     *
     * @param uuid
     * @return
     */
    APIResult<TenantDO> getTenantDOByUUID(String uuid);

    /**
     * 获取租户信息（开放服务，无需身份校验）
     *
     * @param tenantUUID 租户UUID
     * @return
     */
    APIResult<TenantInfoDTO> getTenantInfo(String tenantUUID);

    /**
     * 更新租户信息
     *
     * @param tenantInfoDTO 租户信息
     * @return
     */
    APIResult updateTenantInfo(TenantInfoDTO tenantInfoDTO);
}
