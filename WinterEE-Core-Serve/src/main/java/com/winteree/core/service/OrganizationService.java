package com.winteree.core.service;

import com.winteree.api.entity.OrganizationVO;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
import com.winteree.core.dao.entity.OrganizationDO;

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
     * 获取整个组织架构的树
     *
     * @param tenantUuid 租户编号
     * @return
     */
    List<OrganizationVO> getAllOrganizationTree(String tenantUuid, String organizationUuid);

    /**
     * 根据UUID获取公司实体
     *
     * @param uuid ID
     * @return 公司实体
     */
    OrganizationDO getCompanyByUuid(String uuid);

    /**
     * 获取公司列表
     *
     * @param tenantUuid 租户UUID
     */
    List<OrganizationVO> getCompanyList(String tenantUuid);

    /**
     * 获取公司列表（简单列表非树状）
     *
     * @param tenantUuid
     * @return
     */
    List<OrganizationVO> getCompanySimpleList(String tenantUuid);

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

    /**
     * 获取部门列表（树状）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return 公司下的部门树
     */
    List<OrganizationVO> getDepartmentList(String tenantUuid, String companyUuid);

    /**
     * 获取部门列表（简单列表非树状）
     * @param tenantUuid 租户ID
     * @param companyUuid 公司ID
     * @return 公司下的部门列表
     */
    List<OrganizationVO> getDepartmentSimpleList(String tenantUuid, String companyUuid);

    /**
     * 添加部门
     *
     * @param organizationVO
     * @return
     */
    int addDepartment(OrganizationVO organizationVO) throws ForbiddenException;

    /**
     * 修改部门信息
     *
     * @param organizationVO
     * @return
     */
    int updateDepartment(OrganizationVO organizationVO) throws ForbiddenException, FailureException;

    /**
     * 删除部门
     *
     * @param uuid
     * @return
     */
    int deleteDepartment(String uuid) throws ForbiddenException, FailureException;

    /**
     * 获取公司下的部门列表（包含子公司）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    List<OrganizationVO> getDepartmentListUnderCompanyUuid(String tenantUuid, String companyUuid);
}
