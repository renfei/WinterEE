package com.winteree.core.service;

import com.winteree.api.entity.ListData;
import com.winteree.api.entity.TenantDTO;
import net.renfei.sdk.entity.APIResult;

/**
 * <p>Title: TenantService</p>
 * <p>Description: 租户服务</p>
 *
 * @author RenFei
 * @date : 2020-04-26 21:22
 */
public interface TenantService {
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
}
