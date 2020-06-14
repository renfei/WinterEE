package com.winteree.core.service.impl;

import com.winteree.api.entity.GeospatialEnum;
import com.winteree.api.entity.OrgEnum;
import com.winteree.api.entity.OrganizationVO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.OrganizationDOMapper;
import com.winteree.core.dao.entity.GeospatialDO;
import com.winteree.core.dao.entity.OrganizationDO;
import com.winteree.core.dao.entity.OrganizationDOExample;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.*;
import lombok.extern.slf4j.Slf4j;
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
    private final OrganizationDOMapper organizationDOMapper;

    protected OrganizationServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                      AccountService accountService,
                                      TenantService tenantService,
                                      GeospatialService geospatialService,
                                      OrganizationDOMapper organizationDOMapper) {
        super(wintereeCoreConfig);
        this.accountService = accountService;
        this.tenantService = tenantService;
        this.geospatialService = geospatialService;
        this.organizationDOMapper = organizationDOMapper;
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
            // TODO DataRangeEnum 验证数据权限范围，是全部还是本公司
            // TODO 由于先做的组织机构，后做角色权限，此处缺少数据范围权限校验
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
        }
        getChildren(organizationVOS, OrgEnum.COMPANY);
        List<OrganizationVO> organizationListAndTenant = new ArrayList<>();
        OrganizationVO organizationTenant = new OrganizationVO();
        organizationTenant.setIsTenant(true);
        organizationTenant.setUuid(tenantUuid);
        organizationTenant.setName(tenantService.getTenantDOByUUID(tenantUuid).getData().getName());
        organizationTenant.setChildren(organizationVOS);
        organizationListAndTenant.add(organizationTenant);
        return organizationListAndTenant;
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
        } else {
            // 否则需要检查租户ID
            organizationDO.setTenantUuid(accountDTO.getTenantUuid());
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
        } else {
            // 否则需要检查租户ID
            criteria.andTenantUuidEqualTo(accountDTO.getTenantUuid());
            // TODO DataRangeEnum 验证数据权限范围，是全部还是只能编辑自己的公司
            // TODO 由于先做的组织机构，后做角色权限，此处缺少数据范围权限校验
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
            organizationDOExample.createCriteria()
                    .andTenantUuidEqualTo(organizationVO.getTenantUuid())
                    .andParentUuidEqualTo(organizationVO.getUuid())
                    .andOrgTypeEqualTo(orgEnum.value());
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

    private void fillGeospatial(OrganizationVO organizationVO) {
        GeospatialDO geospatialDO = geospatialService.getGeospatialByFkIdAndType(organizationVO.getUuid(), GeospatialEnum.COMPANY);
        if (geospatialDO != null) {
            organizationVO.setLongitude(geospatialDO.getLongitude());
            organizationVO.setLatitude(geospatialDO.getLatitude());
        }
    }

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
