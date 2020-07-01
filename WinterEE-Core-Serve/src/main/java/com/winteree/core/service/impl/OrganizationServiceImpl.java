package com.winteree.core.service.impl;

import com.winteree.api.entity.DataScopeEnum;
import com.winteree.api.entity.GeospatialEnum;
import com.winteree.api.entity.OrgEnum;
import com.winteree.api.entity.OrganizationVO;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.OrganizationDOMapper;
import com.winteree.core.dao.entity.GeospatialDO;
import com.winteree.core.dao.entity.OrganizationDO;
import com.winteree.core.dao.entity.OrganizationDOExample;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.*;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>Title: OrganizationServiceImpl</p>
 * <p>Description: 组织机构服务</p>
 *
 * @author RenFei
 * @date : 2020-06-04 19:35
 */
@Slf4j
@Service
public class OrganizationServiceImpl extends BaseService implements OrganizationService {
    private final AccountService accountService;
    private final TenantService tenantService;
    private final GeospatialService geospatialService;
    private final RoleService roleService;
    private final OrganizationDOMapper organizationDOMapper;

    protected OrganizationServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                      AccountService accountService,
                                      TenantService tenantService,
                                      GeospatialService geospatialService,
                                      RoleService roleService,
                                      OrganizationDOMapper organizationDOMapper) {
        super(wintereeCoreConfig);
        this.accountService = accountService;
        this.tenantService = tenantService;
        this.geospatialService = geospatialService;
        this.roleService = roleService;
        this.organizationDOMapper = organizationDOMapper;
    }

    public OrganizationDO getOrganizationByUuid(String uuid) {
        OrganizationDOExample example = new OrganizationDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        return ListUtils.getOne(organizationDOMapper.selectByExample(example));
    }

    /**
     * 获取整个组织架构的树
     *
     * @param tenantUuid 租户编号
     * @return
     */
    @Override
    public List<OrganizationVO> getAllOrganizationTree(String tenantUuid, String organizationUuid) {
        AccountDTO accountDTO = getSignedUser(accountService);
        List<OrganizationVO> organizationVOS = new ArrayList<>();
        OrganizationDOExample organizationDOExample = new OrganizationDOExample();
        OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            //只有平台超管才能夸租户管理，并且获取所有公司
            criteria.andTenantUuidEqualTo(tenantUuid);
            if (BeanUtils.isEmpty(organizationUuid)) {
                criteria.andParentUuidEqualTo(tenantUuid);
            } else {
                criteria.andParentUuidEqualTo(tenantUuid);
            }
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVO.setIsTenant(false);
                organizationVOS.add(organizationVO);
                if (organizationDO.getOrgType() == OrgEnum.COMPANY.value()) {
                    fillGeospatial(organizationVO);
                }
            }
        } else {
            //否则只能管理自己归属的租户
            tenantUuid = accountDTO.getTenantUuid();
            criteria.andTenantUuidEqualTo(tenantUuid);
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    criteria.andParentUuidEqualTo(tenantUuid);
                    break;
                case COMPANY:
                    // 只能查看公司下面的
                    criteria.andParentUuidEqualTo(accountDTO.getOfficeUuid());
                    break;
                case DEPARTMENT:
                    // 只能查询他自己部门下的
                    criteria.andParentUuidEqualTo(accountDTO.getDepartmentUuid());
                    break;
                case COMPANY_AND_DEPARTMENT:
                    return new ArrayList<>();
                default:
                    return new ArrayList<>();
            }
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVO.setIsTenant(false);
                organizationVOS.add(organizationVO);
                if (organizationDO.getOrgType() == OrgEnum.COMPANY.value()) {
                    fillGeospatial(organizationVO);
                }
            }
        }
        // 递归查询子机构
        getChildren(organizationVOS, null);
        List<OrganizationVO> organizationListAndTenant = new ArrayList<>();
        OrganizationVO organizationTenant = new OrganizationVO();
        organizationTenant.setIsTenant(true);
        organizationTenant.setUuid(tenantUuid);
        organizationTenant.setName(tenantService.getTenantDOByUUID(tenantUuid).getName());
        organizationTenant.setChildren(organizationVOS);
        organizationListAndTenant.add(organizationTenant);
        return organizationListAndTenant;
    }

    //<editor-fold desc="公司类管理" defaultstate="collapsed">

    /**
     * 根据UUID获取公司实体
     *
     * @param uuid ID
     * @return 公司实体
     */
    @Override
    public OrganizationDO getCompanyByUuid(String uuid) {
        OrganizationDOExample example = new OrganizationDOExample();
        example.createCriteria()
                .andOrgTypeEqualTo(OrgEnum.COMPANY.value())
                .andUuidEqualTo(uuid);
        return ListUtils.getOne(organizationDOMapper.selectByExample(example));
    }

    /**
     * 获取公司列表
     *
     * @param tenantUuid 租户UUID
     */
    @Override
    public List<OrganizationVO> getCompanyList(String tenantUuid) {
        AccountDTO accountDTO = getSignedUser(accountService);
        List<OrganizationVO> organizationVOS = new ArrayList<>();
        OrganizationDOExample organizationDOExample = new OrganizationDOExample();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            //只有平台超管才能夸租户管理，并且获取所有公司
            organizationDOExample.createCriteria()
                    .andTenantUuidEqualTo(tenantUuid)
                    .andParentUuidEqualTo(tenantUuid)
                    .andOrgTypeEqualTo(OrgEnum.COMPANY.value());
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVO.setIsTenant(false);
                organizationVOS.add(organizationVO);
                fillGeospatial(organizationVO);
            }
        } else {
            //否则只能管理自己归属的租户
            tenantUuid = accountDTO.getTenantUuid();
            OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
            criteria
                    .andTenantUuidEqualTo(tenantUuid)
                    .andParentUuidEqualTo(tenantUuid)
                    .andOrgTypeEqualTo(OrgEnum.COMPANY.value());
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            if (dataScopeEnum.equals(DataScopeEnum.ALL)) {
                // 加载租户下全部公司
            } else {
                // 只加载自己公司的
                criteria.andUuidEqualTo(accountDTO.getOfficeUuid());
            }
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVO.setIsTenant(false);
                organizationVOS.add(organizationVO);
                fillGeospatial(organizationVO);
            }
        }
        getChildren(organizationVOS, OrgEnum.COMPANY);
        List<OrganizationVO> organizationListAndTenant = new ArrayList<>();
        OrganizationVO organizationTenant = new OrganizationVO();
        organizationTenant.setIsTenant(true);
        organizationTenant.setUuid(tenantUuid);
        organizationTenant.setName(tenantService.getTenantDOByUUID(tenantUuid).getName());
        organizationTenant.setChildren(organizationVOS);
        organizationListAndTenant.add(organizationTenant);
        return organizationListAndTenant;
    }

    /**
     * 获取公司列表（简单列表非树状）
     *
     * @param tenantUuid
     * @return
     */
    @Override
    public List<OrganizationVO> getCompanySimpleList(String tenantUuid) {
        AccountDTO accountDTO = getSignedUser(accountService);
        List<OrganizationVO> organizationVOS = new ArrayList<>();
        OrganizationDOExample organizationDOExample = new OrganizationDOExample();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            //只有平台超管才能夸租户管理，并且获取所有公司
            organizationDOExample.createCriteria()
                    .andTenantUuidEqualTo(tenantUuid)
                    .andOrgTypeEqualTo(OrgEnum.COMPANY.value());
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVO.setIsTenant(false);
                organizationVOS.add(organizationVO);
            }
        } else {
            //否则只能管理自己归属的租户
            tenantUuid = accountDTO.getTenantUuid();
            OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
            criteria
                    .andTenantUuidEqualTo(tenantUuid)
                    .andOrgTypeEqualTo(OrgEnum.COMPANY.value());
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            if (dataScopeEnum.equals(DataScopeEnum.ALL)) {
                // 加载租户下全部公司
                List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
                for (OrganizationDO organizationDO : organizationDOS
                ) {
                    OrganizationVO organizationVO = convert(organizationDO);
                    organizationVO.setIsTenant(false);
                    organizationVOS.add(organizationVO);
                }
            } else {
                // 只加载自己公司以及递归子公司的
                criteria.andUuidEqualTo(accountDTO.getOfficeUuid());
                OrganizationDO organizationDO = ListUtils.getOne(organizationDOMapper.selectByExample(organizationDOExample));
                if (organizationDO != null) {
                    OrganizationVO organizationVO = convert(organizationDO);
                    organizationVO.setIsTenant(false);
                    organizationVOS.add(organizationVO);
                    getSubsidiary(tenantUuid, organizationDO.getUuid(), organizationVOS);
                }
            }
        }
        return organizationVOS;
    }

    /**
     * 获取公司列表
     *
     * @param tenantUuid 租户UUID
     */
    @Override
    public List<OrganizationVO> getMyCompanyList(String tenantUuid) {
        AccountDTO accountDTO = getSignedUser(accountService);
        List<OrganizationVO> organizationVOS = new ArrayList<>();
        OrganizationDOExample organizationDOExample = new OrganizationDOExample();
        OrganizationVO organizationAll = new OrganizationVO();
        organizationAll.setName("全部 - All");
        organizationAll.setUuid("");
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            //只有平台超管才能夸租户管理，并且获取所有公司
            organizationDOExample.createCriteria()
                    .andTenantUuidEqualTo(tenantUuid)
                    .andParentUuidEqualTo(tenantUuid)
                    .andOrgTypeEqualTo(OrgEnum.COMPANY.value());
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVO.setIsTenant(false);
                organizationVOS.add(organizationVO);
            }
            organizationVOS.add(0, organizationAll);
        } else {
            //否则只能管理自己归属的租户
            tenantUuid = accountDTO.getTenantUuid();
            OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
            criteria
                    .andTenantUuidEqualTo(tenantUuid)
                    .andParentUuidEqualTo(tenantUuid)
                    .andOrgTypeEqualTo(OrgEnum.COMPANY.value());
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            if (dataScopeEnum.equals(DataScopeEnum.ALL)) {
                // 加载租户下全部公司
            } else {
                // 只加载自己公司的
                criteria.andUuidEqualTo(accountDTO.getOfficeUuid());
            }
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVO.setIsTenant(false);
                organizationVOS.add(organizationVO);
            }
            if (dataScopeEnum.equals(DataScopeEnum.ALL)) {
                organizationVOS.add(0, organizationAll);
            }
        }
        return organizationVOS;
    }

    /**
     * 添加公司
     *
     * @param organizationVO
     */
    @Override
    public int addCompany(OrganizationVO organizationVO) {
        OrganizationDO organizationDO = convert(organizationVO);
        organizationDO.setUuid(UUID.randomUUID().toString());
        organizationDO.setOrgType(OrgEnum.COMPANY.value());
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 只有超管提交的数据才完全信任
            organizationDO.setTenantUuid(organizationVO.getTenantUuid());
        } else {
            // 否则需要检查租户ID
            organizationDO.setTenantUuid(accountDTO.getTenantUuid());
            // 验证数据权限范围
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    // 在租户下任意
                    break;
                default:
                    // 权限不足
                    return 0;
            }
        }
        organizationDO.setCreateBy(accountDTO.getUuid());
        organizationDO.setCreateTime(new Date());
        organizationDO.setUpdateBy(accountDTO.getUuid());
        organizationDO.setUpdateTime(new Date());
        organizationDO.setDelFlag("0");
        int number = organizationDOMapper.insertSelective(organizationDO);
        updateCompanyGeospatial(organizationVO, organizationDO, number);
        return number;
    }

    /**
     * 更新公司信息
     *
     * @param organizationVO
     * @return
     */
    @Override
    public int updateCompany(OrganizationVO organizationVO) {
        // 先从库里查询，不要相信前端提交的信息
        OrganizationDOExample organizationDOExample = new OrganizationDOExample();
        OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 只有超管提交的数据才完全信任
            criteria.andTenantUuidEqualTo(organizationVO.getTenantUuid());
        } else {
            // 否则需要检查租户ID
            criteria.andTenantUuidEqualTo(accountDTO.getTenantUuid());
            // 验证数据权限范围，是全部还是只能编辑自己的公司
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    // 在租户下任意修改
                    break;
                case COMPANY:
                    // 只能在自己公司
                    if (!organizationVO.getUuid().equals(accountDTO.getOfficeUuid())) {
                        return 0;
                    }
                    break;
                default:
                    // 权限不足
                    return 0;
            }
        }
        criteria.andUuidEqualTo(organizationVO.getUuid())
                .andOrgTypeEqualTo(OrgEnum.COMPANY.value());
        OrganizationDO organizationDO = ListUtils.getOne(organizationDOMapper.selectByExample(organizationDOExample));
        if (organizationDO != null) {
            //只能修改这几项，其他的都从库里取原数据
            organizationDO.setParentUuid(organizationVO.getParentUuid());
            organizationDO.setName(organizationVO.getName());
            organizationDO.setAddress(organizationVO.getAddress());
            organizationDO.setZipCode(organizationVO.getZipCode());
            organizationDO.setMaster(organizationVO.getMaster());
            organizationDO.setPhone(organizationVO.getPhone());
            organizationDO.setFax(organizationVO.getFax());
            organizationDO.setEmail(organizationVO.getEmail());
            organizationDO.setPrimaryPerson(organizationVO.getPrimaryPerson());
            organizationDO.setDeputyPerson(organizationVO.getDeputyPerson());
            organizationDO.setUpdateBy(accountDTO.getUuid());
            organizationDO.setRemarks(organizationVO.getRemarks());
            organizationDO.setUpdateTime(new Date());
            // 暂不提供删除功能 organizationDO.setDelFlag(organizationVO.getDelFlag());
            organizationDOExample = new OrganizationDOExample();
            organizationDOExample.createCriteria().andUuidEqualTo(organizationDO.getUuid());
            int number = organizationDOMapper.updateByExampleSelective(organizationDO, organizationDOExample);
            updateCompanyGeospatial(organizationVO, organizationDO, number);
            return number;
        } else {
            return 0;
        }
    }

    /**
     * 更新公司的地理空间数据
     *
     * @param organizationVO
     * @param organizationDO
     * @param number
     * @return
     */
    private int updateCompanyGeospatial(OrganizationVO organizationVO, OrganizationDO organizationDO, int number) {
        if (organizationVO.getLatitude().equals(BigDecimal.valueOf(0)) && organizationVO.getLongitude().equals(BigDecimal.valueOf(0))) {
        } else {
            GeospatialDO geospatialDO = new GeospatialDO();
            geospatialDO.setLongitude(organizationVO.getLongitude());
            geospatialDO.setLatitude(organizationVO.getLatitude());
            geospatialDO.setFkType(GeospatialEnum.COMPANY.value());
            geospatialDO.setFkId(organizationDO.getUuid());
            geospatialService.updateGeospatial(geospatialDO);
        }
        return number;
    }

    /**
     * 递归查询子公司
     *
     * @param organizationVOS
     */
    private void getChildren(List<OrganizationVO> organizationVOS, OrgEnum orgEnum) {
        for (OrganizationVO organizationVO : organizationVOS
        ) {
            OrganizationDOExample organizationDOExample = new OrganizationDOExample();
            OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
            criteria
                    .andTenantUuidEqualTo(organizationVO.getTenantUuid())
                    .andParentUuidEqualTo(organizationVO.getUuid());
            if (orgEnum != null) {
                criteria.andOrgTypeEqualTo(orgEnum.value());
            }
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            if (!BeanUtils.isEmpty(organizationDOS)) {
                List<OrganizationVO> organizationChildrenList = new ArrayList<>();
                for (OrganizationDO organizationDO : organizationDOS
                ) {
                    OrganizationVO organizationChildren = convert(organizationDO);
                    organizationChildren.setIsTenant(false);
                    organizationChildrenList.add(organizationChildren);
                    fillGeospatial(organizationChildren);
                }
                getChildren(organizationChildrenList, orgEnum);
                organizationVO.setChildren(organizationChildrenList);
            }
            fillGeospatial(organizationVO);
        }
    }

    /**
     * 递归查询子部门(列表，非树状)
     *
     * @param organizationVOS 部门列表
     */
    private void getChildrenList(List<OrganizationVO> organizationVOS, List<OrganizationVO> organizationS, OrgEnum orgEnum) {
        for (OrganizationVO org : organizationVOS
        ) {
            OrganizationDOExample organizationDOExample = new OrganizationDOExample();
            OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
            criteria
                    .andTenantUuidEqualTo(org.getTenantUuid())
                    .andParentUuidEqualTo(org.getUuid());
            if (orgEnum != null) {
                criteria.andOrgTypeEqualTo(orgEnum.value());
            }
            List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
            if (!BeanUtils.isEmpty(organizationDOS)) {
                for (OrganizationDO organizationDO : organizationDOS
                ) {
                    OrganizationVO organizationVO = convert(organizationDO);
                    organizationVO.setIsTenant(false);
                    organizationS.add(organizationVO);

                }
                getChildrenList(organizationVOS, organizationS, orgEnum);
            }
        }
        organizationVOS.addAll(organizationS);
    }

    private void fillGeospatial(OrganizationVO organizationVO) {
        GeospatialDO geospatialDO = geospatialService.getGeospatialByFkIdAndType(organizationVO.getUuid(), GeospatialEnum.COMPANY);
        if (geospatialDO != null) {
            organizationVO.setLongitude(geospatialDO.getLongitude());
            organizationVO.setLatitude(geospatialDO.getLatitude());
        }
    }

    /**
     * 递归查询子公司
     *
     * @param tenantUuid      租户ID
     * @param officeUuid      公司ID
     * @param organizationVOS 公司列表
     */
    private void getSubsidiary(String tenantUuid, String officeUuid, List<OrganizationVO> organizationVOS) {
        getSubsidiary(tenantUuid, officeUuid, organizationVOS, null);
    }

    /**
     * 递归查询子公司
     *
     * @param tenantUuid      租户ID
     * @param officeUuid      公司ID
     * @param organizationVOS 公司列表
     */
    private void getSubsidiary(String tenantUuid, String officeUuid, List<OrganizationVO> organizationVOS, OrgEnum orgEnum) {
        OrganizationDOExample organizationDOExample = new OrganizationDOExample();
        OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
        criteria.andTenantUuidEqualTo(tenantUuid).andParentUuidEqualTo(officeUuid);
        if (orgEnum != null) {
            criteria.andOrgTypeEqualTo(orgEnum.value());
        }
        List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
        if (!BeanUtils.isEmpty(organizationDOS)) {
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVO.setIsTenant(false);
                organizationVOS.add(organizationVO);
                getSubsidiary(tenantUuid, organizationDO.getUuid(), organizationVOS);
            }
        }
    }

    /**
     * 判断当前用户是否归属此公司（包含子公司）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    private boolean isBelongThisCompany(String tenantUuid, String companyUuid) {
        List<OrganizationVO> organizationVOS = this.getCompanySimpleList(tenantUuid);
        if (BeanUtils.isEmpty(organizationVOS)) {
            return false;
        } else {
            for (OrganizationVO org : organizationVOS
            ) {
                if (org.getUuid().equals(companyUuid)) {
                    return true;
                }
            }
            return false;
        }
    }

    //</editor-fold>

    //<editor-fold desc="部门类管理" defaultstate="collapsed">

    /**
     * 根据UUID获取部门
     *
     * @param uuid 部门UUID
     * @return
     */
    @Override
    public OrganizationDO getDepartmentByUuid(String uuid) {
        OrganizationDOExample example = new OrganizationDOExample();
        example.createCriteria()
                .andOrgTypeEqualTo(OrgEnum.DEPARTMENT.value())
                .andUuidEqualTo(uuid);
        return ListUtils.getOne(organizationDOMapper.selectByExample(example));
    }

    /**
     * 获取部门列表（树状）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return 公司下的部门树
     */
    @Override
    public List<OrganizationVO> getDepartmentList(String tenantUuid, String companyUuid) {
        OrganizationDO companyDO = this.getCompanyByUuid(companyUuid);
        List<OrganizationVO> organizationVOS = new ArrayList<>();
        if (companyDO != null) {
            AccountDTO accountDTO = getSignedUser(accountService);
            OrganizationDOExample organizationDOExample = new OrganizationDOExample();
            boolean recursion = false;
            if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                recursion = true;
                //只有平台超管才能夸租户管理，并且获取所有公司
                organizationDOExample.createCriteria()
                        .andTenantUuidEqualTo(tenantUuid)
                        .andParentUuidEqualTo(companyUuid)
                        .andOrgTypeEqualTo(OrgEnum.DEPARTMENT.value());
                List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
                for (OrganizationDO organizationDO : organizationDOS
                ) {
                    OrganizationVO organizationVO = convert(organizationDO);
                    organizationVOS.add(organizationVO);
                }
            } else {
                //否则只能管理自己归属的租户
                tenantUuid = accountDTO.getTenantUuid();
                OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
                criteria
                        .andTenantUuidEqualTo(tenantUuid)
                        .andOrgTypeEqualTo(OrgEnum.DEPARTMENT.value());
                // 验证数据权限范围，是全部还是本公司
                DataScopeEnum dataScopeEnum = roleService.getDataScope();
                switch (dataScopeEnum) {
                    case ALL:
                        recursion = true;
                        criteria.andParentUuidEqualTo(companyUuid);
                        //加载全部
                        break;
                    case COMPANY:
                        recursion = true;
                        // 只能查看所属公司下的
                        criteria.andParentUuidEqualTo(accountDTO.getOfficeUuid());
                        break;
                    default:
                        // 只能加载所属部门
                        criteria.andUuidEqualTo(accountDTO.getDepartmentUuid());
                        break;
                }
                List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
                for (OrganizationDO organizationDO : organizationDOS
                ) {
                    OrganizationVO organizationVO = convert(organizationDO);
                    organizationVO.setIsTenant(false);
                    organizationVOS.add(organizationVO);
                }
            }
            if (recursion) {
                getChildren(organizationVOS, null);
            }
            List<OrganizationVO> organizations = new ArrayList<>();
            OrganizationVO companyVO = convert(companyDO);
            companyVO.setChildren(organizationVOS);
            organizations.add(companyVO);
            return organizations;
        } else {
            return organizationVOS;
        }
    }

    @Override
    public List<OrganizationVO> getDepartmentSimpleList(String tenantUuid, String companyUuid) {
        OrganizationDO companyDO = this.getCompanyByUuid(companyUuid);
        List<OrganizationVO> organizationVOS = new ArrayList<>();
        if (companyDO != null) {
            AccountDTO accountDTO = getSignedUser(accountService);
            OrganizationDOExample organizationDOExample = new OrganizationDOExample();
            boolean recursion = false;
            if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                recursion = true;
                //只有平台超管才能夸租户管理，并且获取所有公司
                organizationDOExample.createCriteria()
                        .andTenantUuidEqualTo(tenantUuid)
                        .andParentUuidEqualTo(companyUuid)
                        .andOrgTypeEqualTo(OrgEnum.DEPARTMENT.value());
                List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
                for (OrganizationDO organizationDO : organizationDOS
                ) {
                    OrganizationVO organizationVO = convert(organizationDO);
                    organizationVOS.add(organizationVO);
                }
            } else {
                //否则只能管理自己归属的租户
                tenantUuid = accountDTO.getTenantUuid();
                OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
                criteria
                        .andTenantUuidEqualTo(tenantUuid)
                        .andOrgTypeEqualTo(OrgEnum.DEPARTMENT.value());
                // 验证数据权限范围，是全部还是本公司
                DataScopeEnum dataScopeEnum = roleService.getDataScope();
                switch (dataScopeEnum) {
                    case ALL:
                        recursion = true;
                        criteria.andParentUuidEqualTo(companyUuid);
                        //加载全部
                        break;
                    case COMPANY:
                        recursion = true;
                        // 只能查看所属公司下的
                        criteria.andParentUuidEqualTo(accountDTO.getOfficeUuid());
                        break;
                    default:
                        // 只能加载所属部门
                        criteria.andUuidEqualTo(accountDTO.getDepartmentUuid());
                        break;
                }
                List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
                for (OrganizationDO organizationDO : organizationDOS
                ) {
                    OrganizationVO organizationVO = convert(organizationDO);
                    organizationVO.setIsTenant(false);
                    organizationVOS.add(organizationVO);
                }
            }
            if (recursion) {
                getSubsidiary(tenantUuid, companyUuid, organizationVOS, OrgEnum.DEPARTMENT);
            }
            return organizationVOS;
        } else {
            return organizationVOS;
        }
    }

    /**
     * 添加部门
     *
     * @param organizationVO
     */
    @Override
    public int addDepartment(OrganizationVO organizationVO) throws ForbiddenException {
        OrganizationDO organizationDO = convert(organizationVO);
        organizationDO.setUuid(UUID.randomUUID().toString());
        organizationDO.setOrgType(OrgEnum.DEPARTMENT.value());
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 只有超管提交的数据才完全信任
        } else {
            // 否则需要检查租户ID
            organizationDO.setTenantUuid(accountDTO.getTenantUuid());
            // 判断数据范围
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    // 在租户下任意添加
                    break;
                case COMPANY:
                    // 只能在自己公司下面添加部门
                    if (!this.isBelongThisDepartment(accountDTO.getTenantUuid(), accountDTO.getOfficeUuid(), organizationVO.getParentUuid())) {
                        // 权限不足
                        throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                    }
                    break;
                default:
                    // 权限不足
                    throw new ForbiddenException(StateCode.Forbidden.getDescribe());
            }
        }
        organizationDO.setCreateBy(accountDTO.getUuid());
        organizationDO.setCreateTime(new Date());
        organizationDO.setUpdateBy(accountDTO.getUuid());
        organizationDO.setUpdateTime(new Date());
        organizationDO.setDelFlag("0");
        return organizationDOMapper.insertSelective(organizationDO);
    }

    /**
     * 修改部门信息
     *
     * @param organizationVO
     * @return
     */
    @Override
    public int updateDepartment(OrganizationVO organizationVO) throws ForbiddenException, FailureException {
        OrganizationDO oldOrganizationDO = this.getOrganizationByUuid(organizationVO.getUuid());
        if (oldOrganizationDO != null && OrgEnum.DEPARTMENT.value() == oldOrganizationDO.getOrgType()) {
            AccountDTO accountDTO = getSignedUser(accountService);
            if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                // 只有超管提交的数据才完全信任
            } else {
                // 否则需要检查租户ID
                if (oldOrganizationDO.getTenantUuid().equals(accountDTO.getTenantUuid())) {
                    // 判断数据范围
                    DataScopeEnum dataScopeEnum = roleService.getDataScope();
                    switch (dataScopeEnum) {
                        case ALL:
                            // 在租户下任意修改
                            break;
                        case COMPANY:
                            // 只能在自己公司下面添加部门
                            if (!this.isBelongThisDepartment(accountDTO.getTenantUuid(), accountDTO.getOfficeUuid(), oldOrganizationDO.getUuid()) ||
                                    !this.isBelongThisDepartment(accountDTO.getTenantUuid(), accountDTO.getOfficeUuid(), oldOrganizationDO.getParentUuid())) {
                                // 权限不足
                                throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                            }
                            break;
                        default:
                            // 权限不足
                            throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                    }
                } else {
                    // 不允许跨租户编辑
                    throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                }
            }
            // 修改
            oldOrganizationDO.setParentUuid(organizationVO.getParentUuid());
            oldOrganizationDO.setName(organizationVO.getName());
            oldOrganizationDO.setAddress(organizationVO.getAddress());
            oldOrganizationDO.setZipCode(organizationVO.getZipCode());
            oldOrganizationDO.setMaster(organizationVO.getMaster());
            oldOrganizationDO.setPhone(organizationVO.getPhone());
            oldOrganizationDO.setFax(organizationVO.getFax());
            oldOrganizationDO.setEmail(organizationVO.getEmail());
            oldOrganizationDO.setPrimaryPerson(organizationVO.getPrimaryPerson());
            oldOrganizationDO.setDeputyPerson(organizationVO.getDeputyPerson());
            oldOrganizationDO.setUpdateBy(accountDTO.getUuid());
            oldOrganizationDO.setUpdateTime(new Date());
            oldOrganizationDO.setRemarks(organizationVO.getRemarks());
            oldOrganizationDO.setDelFlag(organizationVO.getDelFlag());
            OrganizationDOExample example = new OrganizationDOExample();
            example.createCriteria().andUuidEqualTo(oldOrganizationDO.getUuid());
            return organizationDOMapper.updateByExampleSelective(oldOrganizationDO, example);
        } else {
            // 不是部门类型的
            throw new FailureException("操作失败，非部门类型");
        }
    }

    /**
     * 删除部门
     *
     * @param uuid
     * @return
     */
    @Override
    public int deleteDepartment(String uuid) throws ForbiddenException, FailureException {
        OrganizationDO oldOrganizationDO = this.getOrganizationByUuid(uuid);
        if (oldOrganizationDO != null && OrgEnum.DEPARTMENT.value() == oldOrganizationDO.getOrgType()) {
            AccountDTO accountDTO = getSignedUser(accountService);
            if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                // 超管提交的随便删
            } else {
                // 否则需要检查租户ID
                if (oldOrganizationDO.getTenantUuid().equals(accountDTO.getTenantUuid())) {
                    // 判断数据范围
                    DataScopeEnum dataScopeEnum = roleService.getDataScope();
                    switch (dataScopeEnum) {
                        case ALL:
                            // 在租户下任意删
                            break;
                        case COMPANY:
                            // 只能在自己公司下删除
                            if (!this.isBelongThisDepartment(accountDTO.getTenantUuid(), accountDTO.getOfficeUuid(), oldOrganizationDO.getUuid()) ||
                                    !this.isBelongThisDepartment(accountDTO.getTenantUuid(), accountDTO.getOfficeUuid(), oldOrganizationDO.getParentUuid())) {
                                // 权限不足
                                throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                            }
                            break;
                        default:
                            // 权限不足
                            throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                    }
                } else {
                    // 不允许跨租户编辑
                    throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                }
            }
            // 执行删除逻辑
            this.deleteDepartment(oldOrganizationDO);
            return 1;
        } else {
            throw new FailureException(StateCode.Failure.getDescribe());
        }
    }

    /**
     * 获取公司下的部门列表（包含子公司）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    @Override
    public List<OrganizationVO> getDepartmentListUnderCompanyUuid(String tenantUuid, String companyUuid) {
        AccountDTO accountDTO = getSignedUser(accountService);
        List<OrganizationVO> organizationVOS = new ArrayList<>();
        OrganizationDOExample organizationDOExample = new OrganizationDOExample();
        OrganizationDOExample.Criteria criteria = organizationDOExample.createCriteria();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 只有超管提交的数据才完全信任
        } else {
            tenantUuid = accountDTO.getTenantUuid();
            companyUuid = accountDTO.getOfficeUuid();
        }
        criteria.andTenantUuidEqualTo(tenantUuid);
        DataScopeEnum dataScopeEnum = roleService.getDataScope();
        switch (dataScopeEnum) {
            case ALL:
                // 可以获取公司下所有部门
                criteria.andParentUuidEqualTo(companyUuid);
                // 添加自己顶级的
                OrganizationDOExample organizationPrivateExample = new OrganizationDOExample();
                organizationPrivateExample.createCriteria()
                        .andTenantUuidEqualTo(tenantUuid).andUuidEqualTo(companyUuid);
                OrganizationDO organizationPrivate = ListUtils.getOne(organizationDOMapper.selectByExample(organizationPrivateExample));
                if (organizationPrivate != null) {
                    organizationVOS.add(convert(organizationPrivate));
                }
                break;
            case COMPANY:
                // 可以获取自己公司下所有部门
                criteria.andParentUuidEqualTo(accountDTO.getOfficeUuid());
                // 添加自己顶级的
                OrganizationDOExample organizationPrivateExampleCompany = new OrganizationDOExample();
                organizationPrivateExampleCompany.createCriteria()
                        .andTenantUuidEqualTo(tenantUuid).andUuidEqualTo(accountDTO.getOfficeUuid());
                OrganizationDO organizationPrivateCompany = ListUtils.getOne(organizationDOMapper.selectByExample(organizationPrivateExampleCompany));
                if (organizationPrivateCompany != null) {
                    organizationVOS.add(convert(organizationPrivateCompany));
                }
                break;
            default:
                // 只能获取自己部门和以下的
                criteria.andParentUuidEqualTo(accountDTO.getDepartmentUuid());
                // 添加自己顶级的
                OrganizationDOExample organizationPrivateExampleDefault = new OrganizationDOExample();
                organizationPrivateExampleDefault.createCriteria()
                        .andTenantUuidEqualTo(tenantUuid).andUuidEqualTo(accountDTO.getDepartmentUuid());
                OrganizationDO organizationPrivateDefault = ListUtils.getOne(organizationDOMapper.selectByExample(organizationPrivateExampleDefault));
                if (organizationPrivateDefault != null) {
                    organizationVOS.add(convert(organizationPrivateDefault));
                }
                break;
        }
        List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
        if (!BeanUtils.isEmpty(organizationDOS)) {
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO = convert(organizationDO);
                organizationVOS.add(organizationVO);
                // 递归查询子级
                getDepartmentListUnderCompanyUuid(tenantUuid, organizationVO, organizationVOS);
            }
        }
        return organizationVOS;
    }

    /**
     * 递归查询子级部门
     *
     * @param tenantUuid
     * @param organizationVO
     * @param organizationVOS
     */
    private void getDepartmentListUnderCompanyUuid(String tenantUuid, OrganizationVO organizationVO, List<OrganizationVO> organizationVOS) {
        OrganizationDOExample organizationDOExample = new OrganizationDOExample();
        organizationDOExample.createCriteria()
                .andTenantUuidEqualTo(tenantUuid)
                .andParentUuidEqualTo(organizationVO.getUuid());
        List<OrganizationDO> organizationDOS = organizationDOMapper.selectByExample(organizationDOExample);
        if (!BeanUtils.isEmpty(organizationDOS)) {
            for (OrganizationDO organizationDO : organizationDOS
            ) {
                OrganizationVO organizationVO2 = convert(organizationDO);
                organizationVOS.add(organizationVO2);
                // 递归查询子级
                getDepartmentListUnderCompanyUuid(tenantUuid, organizationVO2, organizationVOS);
            }
        }
    }

    /**
     * 判断部门是否归属此公司
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    private boolean isBelongThisDepartment(String tenantUuid, String companyUuid, String departmentUuid) {
        // 根据公司ID获取下面的部门列表
        List<OrganizationVO> organizationVOS = this.getDepartmentListUnderCompanyUuid(tenantUuid, companyUuid);
        if (BeanUtils.isEmpty(organizationVOS)) {
            return false;
        } else {
            for (OrganizationVO org : organizationVOS
            ) {
                if (org.getUuid().equals(departmentUuid)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 部门删除逻辑
     *
     * @param oldOrganizationDO
     */
    private void deleteDepartment(OrganizationDO oldOrganizationDO) {
        // TODO 删除部门逻辑比较复杂，牵扯较多，暂时不支持删除逻辑
    }
    //</editor-fold>

    private OrganizationVO convert(OrganizationDO organizationDO) {
        OrganizationVO organizationVO = new OrganizationVO();
        organizationVO.setId(organizationDO.getId());
        organizationVO.setUuid(organizationDO.getUuid());
        organizationVO.setTenantUuid(organizationDO.getTenantUuid());
        organizationVO.setParentUuid(organizationDO.getParentUuid());
        organizationVO.setOrgType(organizationDO.getOrgType());
        organizationVO.setName(organizationDO.getName());
        organizationVO.setAddress(organizationDO.getAddress());
        organizationVO.setZipCode(organizationDO.getZipCode());
        organizationVO.setMaster(organizationDO.getMaster());
        organizationVO.setPhone(organizationDO.getPhone());
        organizationVO.setFax(organizationDO.getFax());
        organizationVO.setEmail(organizationDO.getEmail());
        organizationVO.setPrimaryPerson(organizationDO.getPrimaryPerson());
        organizationVO.setDeputyPerson(organizationDO.getDeputyPerson());
        organizationVO.setCreateBy(organizationDO.getCreateBy());
        organizationVO.setCreateTime(organizationDO.getCreateTime());
        organizationVO.setUpdateBy(organizationDO.getUpdateBy());
        organizationVO.setUpdateTime(organizationDO.getUpdateTime());
        organizationVO.setRemarks(organizationDO.getRemarks());
        organizationVO.setDelFlag(organizationDO.getDelFlag());
        return organizationVO;
    }

    private OrganizationDO convert(OrganizationVO organizationVO) {
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setId(organizationVO.getId());
        organizationDO.setUuid(organizationVO.getUuid());
        organizationDO.setTenantUuid(organizationVO.getTenantUuid());
        organizationDO.setParentUuid(organizationVO.getParentUuid());
        organizationDO.setOrgType(organizationVO.getOrgType());
        organizationDO.setName(organizationVO.getName());
        organizationDO.setAddress(organizationVO.getAddress());
        organizationDO.setZipCode(organizationVO.getZipCode());
        organizationDO.setMaster(organizationVO.getMaster());
        organizationDO.setPhone(organizationVO.getPhone());
        organizationDO.setFax(organizationVO.getFax());
        organizationDO.setEmail(organizationVO.getEmail());
        organizationDO.setPrimaryPerson(organizationVO.getPrimaryPerson());
        organizationDO.setDeputyPerson(organizationVO.getDeputyPerson());
        organizationDO.setCreateBy(organizationVO.getCreateBy());
        organizationDO.setCreateTime(organizationVO.getCreateTime());
        organizationDO.setUpdateBy(organizationVO.getUpdateBy());
        organizationDO.setUpdateTime(organizationVO.getUpdateTime());
        organizationDO.setRemarks(organizationVO.getRemarks());
        organizationDO.setDelFlag(organizationVO.getDelFlag());
        return organizationDO;
    }
}
