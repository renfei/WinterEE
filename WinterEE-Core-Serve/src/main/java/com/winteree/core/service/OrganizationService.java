package com.winteree.core.service;

import com.winteree.api.entity.OrganizationVO;

import java.util.List;

/**
 * <p>Title: OrganizationService</p>
 * <p>Description: 组织机构服务</p>
 *
 * @author RenFei
 * @date : 2020-06-04 19:35
 */
public interface OrganizationService {
    /**
     * 获取公司列表
     *
     * @param tenantUuid 租户UUID
     */
    List<OrganizationVO> getCompanyList(String tenantUuid);

    /**
     * 获取我的公司列表
     *
     * @param tenantUuid 租户UUID
     */
    List<OrganizationVO> getMyCompanyList(String tenantUuid);

    /**
     * 添加公司
     *
     * @param organizationVO
     */
    int addCompany(OrganizationVO organizationVO);

    /**
     * 更新公司信息
     *
     * @param organizationVO
     * @return
     */
    int updateCompany(OrganizationVO organizationVO);
}
