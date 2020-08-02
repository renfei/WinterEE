package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.*;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.*;
import com.winteree.core.dao.entity.*;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.*;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Title: CmsServiceImpl</p>
 * <p>Description: 内容管理服务</p>
 *
 * @author RenFei
 * @date : 2020-06-24 20:28
 */
@Service
public class CmsServiceImpl extends BaseService implements CmsService {
    //<editor-fold desc="依赖与构造函数" defaultstate="collapsed">
    private final AccountService accountService;
    private final RoleService roleService;
    private final TenantService tenantService;
    private final OrganizationService organizationService;
    private final CmsCategoryDOMapper cmsCategoryDOMapper;
    private final CmsCommentsDOMapper cmsCommentsDOMapper;
    private final CmsPostsDOMapper cmsPostsDOMapper;
    private final CmsSiteDOMapper cmsSiteDOMapper;
    private final CmsTagDOMapper cmsTagDOMapper;
    private final CmsTagPostsDOMapper cmsTagPostsDOMapper;
    private final CmsMenuDOMapper cmsMenuDOMapper;
    private final AliyunGreenService aliyunGreenService;

    protected CmsServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                             AccountService accountService,
                             RoleService roleService,
                             TenantService tenantService,
                             OrganizationService organizationService,
                             CmsCategoryDOMapper cmsCategoryDOMapper,
                             CmsCommentsDOMapper cmsCommentsDOMapper,
                             CmsPostsDOMapper cmsPostsDOMapper,
                             CmsSiteDOMapper cmsSiteDOMapper,
                             CmsTagDOMapper cmsTagDOMapper,
                             CmsTagPostsDOMapper cmsTagPostsDOMapper,
                             CmsMenuDOMapper cmsMenuDOMapper,
                             AliyunGreenService aliyunGreenService) {
        super(wintereeCoreConfig);
        this.accountService = accountService;
        this.roleService = roleService;
        this.tenantService = tenantService;
        this.organizationService = organizationService;
        this.cmsCategoryDOMapper = cmsCategoryDOMapper;
        this.cmsCommentsDOMapper = cmsCommentsDOMapper;
        this.cmsPostsDOMapper = cmsPostsDOMapper;
        this.cmsSiteDOMapper = cmsSiteDOMapper;
        this.cmsTagDOMapper = cmsTagDOMapper;
        this.cmsTagPostsDOMapper = cmsTagPostsDOMapper;
        this.cmsMenuDOMapper = cmsMenuDOMapper;
        this.aliyunGreenService = aliyunGreenService;
    }
    //</editor-fold>

    //<editor-fold desc="站点类的接口" defaultstate="collapsed">

    /**
     * 获取CMS站点列表（后台管理）
     *
     * @param tenantUuid 租户ID
     * @param page       页码
     * @param rows       行数
     * @return 站点列表
     */
    @Override
    public ListData<CmsSiteDTO> getCmsSiteList(String tenantUuid, int page, int rows) {
        AccountDTO accountDTO = getSignedUser(accountService);
        ListData<CmsSiteDTO> cmsSiteDTOListData = new ListData<>();
        cmsSiteDTOListData.setTotal(0L);
        CmsSiteDOExample example = new CmsSiteDOExample();
        CmsSiteDOExample.Criteria criteria = example.createCriteria();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 超管完全信任，不做限制
            if (BeanUtils.isEmpty(tenantUuid)) {
                criteria.andTenantUuidEqualTo(accountDTO.getTenantUuid());
            } else {
                criteria.andTenantUuidEqualTo(tenantUuid);
            }
        } else {
            // 不可跨租户，只能查自己租户下的
            criteria.andTenantUuidEqualTo(accountDTO.getTenantUuid());
            // 获取子级部门和公司
            List<OrganizationVO> organizationVOS = organizationService.getDepartmentListUnderCompanyUuid(
                    accountDTO.getTenantUuid(),
                    accountDTO.getOfficeUuid());
            List<String> ids = new ArrayList<>();
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    break;
                case COMPANY:
                    // 只能查看公司下面的
                    if (BeanUtils.isEmpty(organizationVOS)) {
                        return new ListData<>();
                    } else {
                        organizationVOS.forEach(organizationVO -> ids.add(organizationVO.getUuid()));
                    }
                    criteria.andOfficeUuidIn(ids);
                    break;
                case DEPARTMENT:
                    // 只能查询他自己部门下的
                    if (BeanUtils.isEmpty(organizationVOS)) {
                        return new ListData<>();
                    } else {
                        organizationVOS.forEach(organizationVO -> ids.add(organizationVO.getUuid()));
                    }
                    criteria.andDepartmentUuidIn(ids);
                    break;
                default:
                    return cmsSiteDTOListData;
            }
        }
        Page page1 = PageHelper.startPage(page, rows);
        List<CmsSiteDO> cmsSiteDOList = cmsSiteDOMapper.selectByExample(example);
        if (BeanUtils.isEmpty(cmsSiteDOList)) {
            return cmsSiteDTOListData;
        }
        List<CmsSiteDTO> cmsSiteDTOList = new ArrayList<>();
        cmsSiteDOList.forEach(cmsSiteDO -> {
            CmsSiteDTO cmsSiteDTO = convert(cmsSiteDO);
            fillSiteInfo(cmsSiteDTO);
            cmsSiteDTOList.add(cmsSiteDTO);
        });
        cmsSiteDTOListData.setData(cmsSiteDTOList);
        cmsSiteDTOListData.setTotal(page1.getTotal());
        return cmsSiteDTOListData;
    }

    /**
     * 根据UUID获取CMS系统站点（后台管理）
     *
     * @param uuid UUID
     * @return 站点传输对象
     * @throws ForbiddenException 权限不足异常
     */
    @Override
    public CmsSiteDTO getCmsSiteByUuid(String uuid) throws ForbiddenException {
        CmsSiteDOExample example = new CmsSiteDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        CmsSiteDO cmsSiteDO = ListUtils.getOne(cmsSiteDOMapper.selectByExample(example));
        if (cmsSiteDO == null) {
            return null;
        } else {
            // 检查权限
            AccountDTO accountDTO = getSignedUser(accountService);
            if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                // 超管不做限制
            } else {
                if (!accountDTO.getTenantUuid().equals(cmsSiteDO.getTenantUuid())) {
                    // 不可跨租户
                    throw new ForbiddenException("权限不足");
                }
                // 检查权限范围
                checkPermissions(accountDTO, cmsSiteDO);
            }
        }
        CmsSiteDTO cmsSiteDTO = convert(cmsSiteDO);
        fillSiteInfo(cmsSiteDTO);
        return cmsSiteDTO;
    }

    /**
     * 根据域名获取站点信息
     *
     * @param domain 域名
     * @return
     */
    @Override
    public CmsSiteDTO getCmsSiteByDomain(String domain) {
        CmsSiteDOExample example = new CmsSiteDOExample();
        example.createCriteria().andSiteDomainEqualTo(domain.toLowerCase());
        CmsSiteDO cmsSiteDO = ListUtils.getOne(cmsSiteDOMapper.selectByExample(example));
        if (cmsSiteDO == null) {
            return null;
        }
        return convert(cmsSiteDO);
    }

    /**
     * 添加站点（后台管理）
     *
     * @param cmsSiteDTO 站点信息传输对象
     * @return 插入条数
     * @throws FailureException 失败异常信息
     */
    @Override
    public int addCmsSite(CmsSiteDTO cmsSiteDTO) throws FailureException {
        if (BeanUtils.isEmpty(cmsSiteDTO.getSiteName())) {
            throw new FailureException("站点名称不可为空");
        }
        if (BeanUtils.isEmpty(cmsSiteDTO.getSiteDomain())) {
            throw new FailureException("域名不可为空");
        }
        if (!StringUtils.isDomain(cmsSiteDTO.getSiteDomain())) {
            throw new FailureException("域名格式不合法");
        }
        CmsSiteDOExample example = new CmsSiteDOExample();
        example.createCriteria().andSiteDomainEqualTo(cmsSiteDTO.getSiteDomain().toLowerCase());
        if (ListUtils.getOne(cmsSiteDOMapper.selectByExample(example)) != null) {
            throw new FailureException("域名已经被添加过，不能重复添加");
        }
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 超管完全信任
            if (BeanUtils.isEmpty(cmsSiteDTO.getTenantUuid())) {
                cmsSiteDTO.setTenantUuid(accountDTO.getTenantUuid());
            }
        } else {
            // 不可跨租户
            cmsSiteDTO.setTenantUuid(accountDTO.getTenantUuid());
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    break;
                case COMPANY:
                    // 只能添加自己公司的
                    cmsSiteDTO.setOfficeUuid(accountDTO.getOfficeUuid());
                    break;
                case DEPARTMENT:
                    // 只能添加自己部门的
                    cmsSiteDTO.setOfficeUuid(accountDTO.getOfficeUuid());
                    cmsSiteDTO.setDepartmentUuid(accountDTO.getDepartmentUuid());
                    break;
                default:
                    return 0;
            }
        }
        CmsSiteDO cmsSiteDO = convert(cmsSiteDTO);
        cmsSiteDO.setUuid(UUID.randomUUID().toString().toUpperCase());
        cmsSiteDO.setCreateBy(accountDTO.getUuid());
        cmsSiteDO.setCreateTime(new Date());
        cmsSiteDO.setUpdateBy(accountDTO.getUuid());
        cmsSiteDO.setUpdateTime(new Date());
        cmsSiteDO.setSiteEnable(true);
        return cmsSiteDOMapper.insertSelective(cmsSiteDO);
    }

    /**
     * 更新站点（后台管理）
     *
     * @param cmsSiteDTO 站点信息传输对象
     * @return 修改条数
     * @throws ForbiddenException 失败异常信息
     * @throws FailureException   失败异常信息
     */
    @Override
    public int updateCmsSite(CmsSiteDTO cmsSiteDTO) throws ForbiddenException, FailureException {
        CmsSiteDOExample example = new CmsSiteDOExample();
        example.createCriteria().andUuidEqualTo(cmsSiteDTO.getUuid());
        CmsSiteDO oldCmsSite = ListUtils.getOne(cmsSiteDOMapper.selectByExample(example));
        if (oldCmsSite == null) {
            throw new FailureException("要修改的站点不存在");
        }
        if (!BeanUtils.isEmpty(cmsSiteDTO.getSiteDomain()) &&
                !oldCmsSite.getSiteDomain().equals(cmsSiteDTO.getSiteDomain())) {
            // 修改了域名，需要重新检验格式
            if (!StringUtils.isDomain(cmsSiteDTO.getSiteDomain())) {
                throw new FailureException("域名格式不合法");
            }
            CmsSiteDOExample exampleByDomain = new CmsSiteDOExample();
            exampleByDomain.createCriteria().andSiteDomainEqualTo(cmsSiteDTO.getSiteDomain().toLowerCase());
            if (ListUtils.getOne(cmsSiteDOMapper.selectByExample(exampleByDomain)) != null) {
                throw new FailureException("域名已经被添加过，不能重复添加");
            }
        }
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 超管，完全信任
        } else {
            if (!accountDTO.getTenantUuid().equals(cmsSiteDTO.getTenantUuid())) {
                // 不可跨租户管理
                throw new ForbiddenException("权限不足");
            }
            checkPermissions(accountDTO, oldCmsSite);
        }
        CmsSiteDO newCmsSite = convert(cmsSiteDTO);
        newCmsSite.setUuid(oldCmsSite.getUuid());
        newCmsSite.setCreateBy(oldCmsSite.getCreateBy());
        newCmsSite.setCreateTime(oldCmsSite.getCreateTime());
        newCmsSite.setUpdateBy(accountDTO.getUuid());
        newCmsSite.setUpdateTime(new Date());
        return cmsSiteDOMapper.updateByExampleSelective(newCmsSite, example);
    }

    /**
     * 删除站点所有内容（后台管理）
     *
     * @param uuid 站点ID
     * @return 删除条数
     * @throws ForbiddenException 失败异常信息
     * @throws FailureException   失败异常信息
     */
    @Override
    public int deleteCmsSite(String uuid) throws ForbiddenException, FailureException {
        CmsSiteDOExample example = new CmsSiteDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        CmsSiteDO oldCmsSite = ListUtils.getOne(cmsSiteDOMapper.selectByExample(example));
        if (oldCmsSite == null) {
            throw new FailureException("要删除的站点不存在");
        }
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 超管，完全信任
        } else {
            if (!accountDTO.getTenantUuid().equals(oldCmsSite.getTenantUuid())) {
                // 不可跨租户管理
                throw new ForbiddenException("权限不足");
            }
            checkPermissions(accountDTO, oldCmsSite);
        }
        // 删除顺序：文章与标签关系表->文章表->标签表->分类表，评论不用删
        CmsPostsDOExample cmsPostsDOExample = new CmsPostsDOExample();
        cmsPostsDOExample.createCriteria().andSiteUuidEqualTo(uuid);
        List<CmsPostsDO> cmsPostsDOS = cmsPostsDOMapper.selectByExample(cmsPostsDOExample);
        if (!BeanUtils.isEmpty(cmsPostsDOS)) {
            List<String> postIds = new ArrayList<>();
            cmsPostsDOS.forEach(cmsPostsDO -> postIds.add(cmsPostsDO.getUuid()));
            CmsTagPostsDOExample cmsTagPostsDOExample = new CmsTagPostsDOExample();
            cmsTagPostsDOExample.createCriteria().andPostUuidIn(postIds);
            cmsTagPostsDOMapper.deleteByExample(cmsTagPostsDOExample);
        }
        cmsPostsDOMapper.deleteByExample(cmsPostsDOExample);
        CmsTagDOExample cmsTagDOExample = new CmsTagDOExample();
        cmsTagDOExample.createCriteria().andSiteUuidEqualTo(uuid);
        cmsTagDOMapper.deleteByExample(cmsTagDOExample);
        CmsCategoryDOExample cmsCategoryDOExample = new CmsCategoryDOExample();
        cmsCategoryDOExample.createCriteria().andSiteUuidEqualTo(uuid);
        cmsCategoryDOMapper.deleteByExample(cmsCategoryDOExample);
        return cmsSiteDOMapper.deleteByExample(example);
    }
    //</editor-fold>

    //<editor-fold desc="分类的接口" defaultstate="collapsed">

    /**
     * 获取CMS系统分类列表（后台管理）
     *
     * @param siteUuid 站点ID
     * @return 文章分类列表
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public List<CmsCategoryDTO> getCmsCategoryList(String siteUuid) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(siteUuid);
        if (cmsSiteDTO == null) {
            throw new FailureException("站点不存在");
        }
        CmsCategoryDOExample example = new CmsCategoryDOExample();
        example.createCriteria().andSiteUuidEqualTo(siteUuid);
        List<CmsCategoryDO> cmsCategoryDOS = cmsCategoryDOMapper.selectByExample(example);
        if (BeanUtils.isEmpty(cmsCategoryDOS)) {
            return new ArrayList<>();
        }
        List<CmsCategoryDTO> cmsCategoryDTOS = new ArrayList<>();
        cmsCategoryDOS.forEach(cmsCategoryDO -> cmsCategoryDTOS.add(convert(cmsCategoryDO)));
        return cmsCategoryDTOS;
    }

    /**
     * 根据UUID获取CMS系统站点下的分类
     *
     * @param uuid 分类UUID
     * @return 分类对象
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public CmsCategoryDTO getCmsCategoryByUuid(String uuid) throws ForbiddenException, FailureException {
        CmsCategoryDOExample example = new CmsCategoryDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        List<CmsCategoryDO> cmsCategoryDOS = cmsCategoryDOMapper.selectByExample(example);
        if (BeanUtils.isEmpty(cmsCategoryDOS)) {
            return null;
        } else {
            CmsCategoryDO cmsCategoryDO = ListUtils.getOne(cmsCategoryDOS);
            // 这里获取一下站点，可以鉴权是否可以管理该站点下的分类
            this.getCmsSiteByUuid(cmsCategoryDO.getSiteUuid());
            return convert(cmsCategoryDO);
        }
    }

    /**
     * 添加文章分类（后台管理）
     *
     * @param cmsCategoryDTO 文章分类传输对象
     * @return 插入行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public int addCmsCategory(CmsCategoryDTO cmsCategoryDTO) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsCategoryDTO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("站点不存在");
        }
        CmsCategoryDO cmsCategoryDO = convert(cmsCategoryDTO);
        cmsCategoryDO.setUuid(UUID.randomUUID().toString().toUpperCase());
        return cmsCategoryDOMapper.insertSelective(cmsCategoryDO);
    }

    /**
     * 更新文章分类（后台管理）
     *
     * @param cmsCategoryDTO 文章分类传输对象
     * @return 修改行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public int updateCmsCategory(CmsCategoryDTO cmsCategoryDTO) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsCategoryDTO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("站点不存在");
        }
        CmsCategoryDOExample example = new CmsCategoryDOExample();
        example.createCriteria().andUuidEqualTo(cmsCategoryDTO.getUuid());
        CmsCategoryDO oldCmsCategoryDO = ListUtils.getOne(cmsCategoryDOMapper.selectByExample(example));
        if (oldCmsCategoryDO == null) {
            throw new FailureException("要修改的文章分类不存在");
        }
        oldCmsCategoryDO.setEnName(cmsCategoryDTO.getEnName());
        oldCmsCategoryDO.setZhName(cmsCategoryDTO.getZhName());
        return cmsCategoryDOMapper.updateByExampleSelective(oldCmsCategoryDO, example);
    }

    /**
     * 删除文章分类（后台管理）
     *
     * @param cmsCategoryUuid 文章分类UUID
     * @return 删除行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public int deleteCmsCategory(String cmsCategoryUuid) throws ForbiddenException, FailureException {
        CmsCategoryDOExample example = new CmsCategoryDOExample();
        example.createCriteria().andUuidEqualTo(cmsCategoryUuid);
        CmsCategoryDO cmsCategoryDO = ListUtils.getOne(cmsCategoryDOMapper.selectByExample(example));
        if (cmsCategoryDO == null) {
            throw new FailureException("要删除的文章分类不存在");
        }
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsCategoryDO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("站点不存在");
        }
        // 检查分类下是否还有文章存在
        CmsPostsDOExample cmsPostsDOExample = new CmsPostsDOExample();
        cmsPostsDOExample.createCriteria().andCategoryUuidEqualTo(cmsCategoryDO.getUuid());
        List<CmsPostsDO> cmsPostsDOS = cmsPostsDOMapper.selectByExample(cmsPostsDOExample);
        if (cmsPostsDOS != null && cmsPostsDOS.size() > 0) {
            throw new FailureException("要删除的文章分类下还存在文章，无法删除");
        }
        return cmsCategoryDOMapper.deleteByExample(example);
    }
    //</editor-fold>

    //<editor-fold desc="文章类的接口" defaultstate="collapsed">

    /**
     * 根据查询条件获取文章列表（后台管理）
     *
     * @param cmsPostSearchCriteriaVO 查询条件
     * @return 文章列表
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public ListData<CmsPostsDTO> getCmsPostList(CmsPostSearchCriteriaVO cmsPostSearchCriteriaVO) throws ForbiddenException, FailureException {
        CmsPostsDOExample example = new CmsPostsDOExample();
        example.setOrderByClause("release_time DESC");
        CmsPostsDOExample.Criteria criteria = example.createCriteria();
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsPostSearchCriteriaVO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("文章所属站点不存在");
        }
        criteria.andSiteUuidEqualTo(cmsPostSearchCriteriaVO.getSiteUuid());
        if (!BeanUtils.isEmpty(cmsPostSearchCriteriaVO.getCategoryUuid())) {
            criteria.andCategoryUuidEqualTo(cmsPostSearchCriteriaVO.getCategoryUuid());
        }
        if (!BeanUtils.isEmpty(cmsPostSearchCriteriaVO.getTitle())) {
            criteria.andTitleLike("%" + cmsPostSearchCriteriaVO.getTitle() + "%");
        }
        Page page = PageHelper.startPage(cmsPostSearchCriteriaVO.getPages(), cmsPostSearchCriteriaVO.getRows());
        ListData<CmsPostsDTO> cmsPostsDTOListData = new ListData<>();
        cmsPostsDTOListData.setTotal(0L);
        List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(example);
        if (BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
            return cmsPostsDTOListData;
        }
        List<CmsPostsDTO> cmsPostsDTOS = new ArrayList<>();
        cmsPostsDOWithBLOBs.forEach(cmsPostsWithBLOBs -> {
            CmsPostsDTO cmsPostsDTO = convert(cmsPostsWithBLOBs);
            List<CmsTagDTO> cmsTagDTOS = this.getTagListByPostUuid(cmsPostsWithBLOBs.getUuid());
            if (!BeanUtils.isEmpty(cmsTagDTOS)) {
                List<String> ids = new ArrayList<>();
                cmsTagDTOS.forEach(tag -> ids.add(tag.getUuid()));
                cmsPostsDTO.setTagIds(ids);
            }
            cmsPostsDTOS.add(cmsPostsDTO);
        });
        cmsPostsDTOListData.setData(cmsPostsDTOS);
        cmsPostsDTOListData.setTotal(page.getTotal());
        return cmsPostsDTOListData;
    }

    /**
     * 根据栏目ID获取文章列表（前台）
     *
     * @param categoryUuid 栏目UUID
     * @param pages        页码
     * @param rows         每页行数
     * @return 文章列表
     */
    @Override
    public ListData<CmsPostsDTO> getCmsPostListByCategory(String categoryUuid, int pages, int rows) {
        CmsPostsDOExample example = new CmsPostsDOExample();
        example.setOrderByClause("release_time DESC");
        // 此处条件比后台要增加 发布时间判断、软删除判断
        example.createCriteria()
                .andCategoryUuidEqualTo(categoryUuid)
                .andReleaseTimeLessThan(new Date())
                .andIsDeleteEqualTo(false);
        Page page = PageHelper.startPage(pages, rows);
        ListData<CmsPostsDTO> cmsPostsDTOListData = new ListData<>();
        cmsPostsDTOListData.setTotal(0L);
        List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(example);
        if (BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
            return cmsPostsDTOListData;
        }
        List<CmsPostsDTO> cmsPostsDTOS = new ArrayList<>();
        cmsPostsDOWithBLOBs.forEach(cmsPostsWithBLOBs -> {
            CmsPostsDTO cmsPostsDTO = convert(cmsPostsWithBLOBs);
            List<CmsTagDTO> cmsTagDTOS = this.getTagListByPostUuid(cmsPostsWithBLOBs.getUuid());
            if (!BeanUtils.isEmpty(cmsTagDTOS)) {
                List<String> ids = new ArrayList<>();
                cmsTagDTOS.forEach(tag -> ids.add(tag.getUuid()));
                cmsPostsDTO.setTagIds(ids);
            }
            cmsPostsDTOS.add(cmsPostsDTO);
        });
        cmsPostsDTOListData.setData(cmsPostsDTOS);
        cmsPostsDTOListData.setTotal(page.getTotal());
        return cmsPostsDTOListData;
    }

    /**
     * 获取相关文章
     *
     * @param uuid   文章UUID
     * @param number 获取的数量
     * @return 相关文章列表
     */
    @Override
    public ListData<CmsPostsDTO> getRelatedPostList(String uuid, int number) {
        ListData<CmsPostsDTO> cmsPostsDTOListData = new ListData<>();
        List<CmsPostsDTO> cmsPostsDTOS = new ArrayList<>();
        // 1、先拿到文章的标签组
        List<CmsTagDTO> cmsTagDTOS = this.getTagListByPostUuid(uuid);
        if (!BeanUtils.isEmpty(cmsTagDTOS)) {
            // 2、根据标签组拿到所有文章id
            List<String> tagIds = new ArrayList<>();
            cmsTagDTOS.forEach(cmsTagDTO -> tagIds.add(cmsTagDTO.getUuid()));
            CmsTagPostsDOExample cmsTagPostsDOExample = new CmsTagPostsDOExample();
            cmsTagPostsDOExample.createCriteria()
                    .andTagUuidIn(tagIds);
            List<CmsTagPostsDO> cmsTagPostsDOS = cmsTagPostsDOMapper.selectByExample(cmsTagPostsDOExample);
            if (!BeanUtils.isEmpty(cmsTagPostsDOS)) {
                // 3、根据文章ID获得所有文章
                List<String> postIds = new ArrayList<>();
                cmsTagPostsDOS.forEach(cmsTagPostsDO -> postIds.add(cmsTagPostsDO.getPostUuid()));
                CmsPostsDOExample cmsPostsDOExample = new CmsPostsDOExample();
                cmsPostsDOExample.setDistinct(true);
                cmsPostsDOExample.setOrderByClause("page_rank DESC,release_time DESC");
                cmsPostsDOExample.createCriteria()
                        .andReleaseTimeLessThan(new Date())
                        .andIsDeleteEqualTo(false)
                        .andUuidIn(postIds);
                Page page = PageHelper.startPage(1, number);
                List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(cmsPostsDOExample);
                if (!BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
                    cmsPostsDOWithBLOBs.forEach(cmsPostsDO -> cmsPostsDTOS.add(convert(cmsPostsDO)));
                    cmsPostsDTOListData.setData(cmsPostsDTOS);
                    cmsPostsDTOListData.setTotal(page.getTotal());
                    return cmsPostsDTOListData;
                }
            }
        }
        // 兜底，如果上面没有return出去，就使用下面的这个
        CmsPostsDOExample cmsPostsDOExample = new CmsPostsDOExample();
        cmsPostsDOExample.setDistinct(true);
        cmsPostsDOExample.setOrderByClause("page_rank DESC,release_time DESC");
        cmsPostsDOExample.createCriteria()
                .andReleaseTimeLessThan(new Date())
                .andIsDeleteEqualTo(false)
                .andUuidNotEqualTo(uuid);
        Page page = PageHelper.startPage(1, number);
        List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(cmsPostsDOExample);
        if (!BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
            cmsPostsDOWithBLOBs.forEach(cmsPostsDO -> cmsPostsDTOS.add(convert(cmsPostsDO)));
            cmsPostsDTOListData.setData(cmsPostsDTOS);
            cmsPostsDTOListData.setTotal(page.getTotal());
        }
        return cmsPostsDTOListData;
    }

    /**
     * 获取最热文章
     *
     * @param siteUuid 站点UUID
     * @param number   获取数量
     * @return
     */
    @Override
    public ListData<CmsPostsDTO> getHotPostList(String siteUuid, int number) {
        ListData<CmsPostsDTO> cmsPostsDTOS = new ListData<>();
        CmsPostsDOExample cmsPostsDOExample = new CmsPostsDOExample();
        cmsPostsDOExample.setDistinct(true);
        cmsPostsDOExample.setOrderByClause("avg_views DESC,release_time DESC");
        cmsPostsDOExample.createCriteria()
                .andSiteUuidEqualTo(siteUuid)
                .andReleaseTimeLessThan(new Date())
                .andIsDeleteEqualTo(false);
        Page page = PageHelper.startPage(1, number);
        List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(cmsPostsDOExample);
        cmsPostsDTOS.setTotal(page.getTotal());
        if (!BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
            List<CmsPostsDTO> cmsPostsDTO = new ArrayList<>();
            cmsPostsDOWithBLOBs.forEach(cmsPostsDO -> cmsPostsDTO.add(convert(cmsPostsDO)));
            cmsPostsDTOS.setData(cmsPostsDTO);
        }
        return cmsPostsDTOS;
    }

    /**
     * 获取年度最热文章
     *
     * @param siteUuid 站点UUID
     * @param number   获取数量
     * @return
     */
    @Override
    public ListData<CmsPostsDTO> getHotPostListByYear(String siteUuid, int number) {
        Date dateStart = DateUtils.parseDate(DateUtils.getYear() + "-01-01 00:00:00");
        Date dateEnd = DateUtils.parseDate(DateUtils.getYear() + "-12-31 23:59:59");
        ListData<CmsPostsDTO> cmsPostsDTOS = new ListData<>();
        CmsPostsDOExample cmsPostsDOExample = new CmsPostsDOExample();
        cmsPostsDOExample.setDistinct(true);
        cmsPostsDOExample.setOrderByClause("avg_views DESC,release_time DESC");
        cmsPostsDOExample.createCriteria()
                .andSiteUuidEqualTo(siteUuid)
                .andReleaseTimeLessThan(new Date())
                .andReleaseTimeBetween(dateStart, dateEnd)
                .andIsDeleteEqualTo(false);
        Page page = PageHelper.startPage(1, number);
        List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(cmsPostsDOExample);
        cmsPostsDTOS.setTotal(page.getTotal());
        if (!BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
            List<CmsPostsDTO> cmsPostsDTO = new ArrayList<>();
            cmsPostsDOWithBLOBs.forEach(cmsPostsDO -> cmsPostsDTO.add(convert(cmsPostsDO)));
            cmsPostsDTOS.setData(cmsPostsDTO);
        }
        return cmsPostsDTOS;
    }

    /**
     * 获取季度最热文章
     *
     * @param siteUuid 站点UUID
     * @param number   获取数量
     * @return
     */
    @Override
    public ListData<CmsPostsDTO> getHotPostListByQuarter(String siteUuid, int number) {
        Date dateStart, dateEnd;
        String month = DateUtils.getMonth();
        switch (month) {
            case "01":
            case "02":
            case "03":
                dateStart = DateUtils.parseDate(DateUtils.getYear() + "-01-01 00:00:00");
                dateEnd = DateUtils.parseDate(DateUtils.getYear() + "-03-31 23:59:59");
                break;
            case "04":
            case "05":
            case "06":
                dateStart = DateUtils.parseDate(DateUtils.getYear() + "-04-01 00:00:00");
                dateEnd = DateUtils.parseDate(DateUtils.getYear() + "-06-30 23:59:59");
                break;
            case "07":
            case "08":
            case "09":
                dateStart = DateUtils.parseDate(DateUtils.getYear() + "-07-01 00:00:00");
                dateEnd = DateUtils.parseDate(DateUtils.getYear() + "-09-30 23:59:59");
                break;
            case "10":
            case "11":
            case "12":
                dateStart = DateUtils.parseDate(DateUtils.getYear() + "-10-01 00:00:00");
                dateEnd = DateUtils.parseDate(DateUtils.getYear() + "-12-31 23:59:59");
                break;
            default:
                return null;
        }
        ListData<CmsPostsDTO> cmsPostsDTOS = new ListData<>();
        CmsPostsDOExample cmsPostsDOExample = new CmsPostsDOExample();
        cmsPostsDOExample.setDistinct(true);
        cmsPostsDOExample.setOrderByClause("avg_views DESC,release_time DESC");
        cmsPostsDOExample.createCriteria()
                .andSiteUuidEqualTo(siteUuid)
                .andReleaseTimeLessThan(new Date())
                .andReleaseTimeBetween(dateStart, dateEnd)
                .andIsDeleteEqualTo(false);
        Page page = PageHelper.startPage(1, number);
        List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(cmsPostsDOExample);
        cmsPostsDTOS.setTotal(page.getTotal());
        if (!BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
            List<CmsPostsDTO> cmsPostsDTO = new ArrayList<>();
            cmsPostsDOWithBLOBs.forEach(cmsPostsDO -> cmsPostsDTO.add(convert(cmsPostsDO)));
            cmsPostsDTOS.setData(cmsPostsDTO);
        }
        return cmsPostsDTOS;
    }

    /**
     * 根据文章ID获取文章详情
     *
     * @param uuid 文章UUID
     * @return
     */
    @Override
    public CmsPostsDTO getCmsPostByUuid(String uuid) {
        return getCmsPostByUuid(uuid, false);
    }

    /**
     * 根据文章ID获取文章详情
     *
     * @param uuid        文章UUID
     * @param updateViews 是否更新浏览量
     * @return
     */
    @Override
    public CmsPostsDTO getCmsPostByUuid(String uuid, boolean updateViews) {
        CmsPostsDOExample example = new CmsPostsDOExample();
        // 此处条件比后台要增加 发布时间判断、软删除判断
        example.createCriteria()
                .andUuidEqualTo(uuid)
                .andReleaseTimeLessThan(new Date())
                .andIsDeleteEqualTo(false);
        List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(example);
        if (BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
            return null;
        }
        CmsPostsDOWithBLOBs cmsPostsDO = ListUtils.getOne(cmsPostsDOWithBLOBs);
        // 更新文章浏览量
        if (updateViews) {
            updateViews(cmsPostsDO);
        }
        return convert(cmsPostsDO);
    }

    /**
     * 根据文章ID获取文章详情
     *
     * @param id          文章主键ID
     * @param updateViews 是否更新浏览量
     * @return
     */
    @Override
    public CmsPostsDTO getCmsPostByLongId(Long id, boolean updateViews) {
        CmsPostsDOExample example = new CmsPostsDOExample();
        // 此处条件比后台要增加 发布时间判断、软删除判断
        example.createCriteria()
                .andIdEqualTo(id)
                .andReleaseTimeLessThan(new Date())
                .andIsDeleteEqualTo(false);
        List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(example);
        if (BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
            return null;
        }
        CmsPostsDOWithBLOBs cmsPostsDO = ListUtils.getOne(cmsPostsDOWithBLOBs);
        // 更新文章浏览量
        if (updateViews) {
            updateViews(cmsPostsDO);
        }
        return convert(cmsPostsDO);
    }

    /**
     * 点赞
     *
     * @param uuid 文章UUID
     * @return
     */
    @Override
    public int thumbsUpPost(String uuid) {
        CmsPostsDTO cmsPostsDTO = this.getCmsPostByUuid(uuid);
        if (cmsPostsDTO == null) {
            return 0;
        }
        CmsPostsDOExample example = new CmsPostsDOExample();
        example.createCriteria().andUuidNotEqualTo(uuid);
        CmsPostsDOWithBLOBs cmsPostsDO = convert(cmsPostsDTO);
        cmsPostsDO.setThumbsUp(cmsPostsDO.getThumbsUp() + 1);
        return cmsPostsDOMapper.updateByExampleWithBLOBs(cmsPostsDO, example);
    }

    /**
     * 点踩
     *
     * @param uuid 文章UUID
     * @return
     */
    @Override
    public int thumbsDownPost(String uuid) {
        CmsPostsDTO cmsPostsDTO = this.getCmsPostByUuid(uuid);
        if (cmsPostsDTO == null) {
            return 0;
        }
        CmsPostsDOExample example = new CmsPostsDOExample();
        example.createCriteria().andUuidNotEqualTo(uuid);
        CmsPostsDOWithBLOBs cmsPostsDO = convert(cmsPostsDTO);
        cmsPostsDO.setThumbsDown(cmsPostsDO.getThumbsDown() + 1);
        return cmsPostsDOMapper.updateByExampleWithBLOBs(cmsPostsDO, example);
    }

    /**
     * 添加文章（后台管理）
     *
     * @param cmsPostsDTO 文章传输对象
     * @return 插入行数
     */
    @Override
    public int addCmsPost(CmsPostsDTO cmsPostsDTO) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsPostsDTO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("文章所属站点不存在");
        }
        if (this.getCmsCategoryByUuid(cmsPostsDTO.getCategoryUuid()) == null) {
            throw new FailureException("文章所属分类不存在");
        }
        AccountDTO accountDTO = getSignedUser(accountService);
        CmsPostsDOWithBLOBs cmsPostsDOWithBLOBs = convert(cmsPostsDTO);
        if (cmsPostsDTO.getReleaseTime() == null) {
            cmsPostsDOWithBLOBs.setReleaseTime(new Date());
        }
        cmsPostsDOWithBLOBs.setUuid(UUID.randomUUID().toString().toUpperCase());
        cmsPostsDOWithBLOBs.setAvgComment(0D);
        cmsPostsDOWithBLOBs.setAvgViews(0D);
        cmsPostsDOWithBLOBs.setPageRank(0D);
        cmsPostsDOWithBLOBs.setPageRankUpdateTime(null);
        cmsPostsDOWithBLOBs.setThumbsDown(0L);
        cmsPostsDOWithBLOBs.setThumbsUp(0L);
        cmsPostsDOWithBLOBs.setViews(0L);
        cmsPostsDOWithBLOBs.setCreateBy(accountDTO.getUuid());
        cmsPostsDOWithBLOBs.setCreateTime(new Date());
        cmsPostsDOWithBLOBs.setUpdateBy(accountDTO.getUuid());
        cmsPostsDOWithBLOBs.setUpdateTime(new Date());
        cmsPostsDOWithBLOBs.setIsDelete(false);
        int rows = cmsPostsDOMapper.insertSelective(cmsPostsDOWithBLOBs);
        if (!BeanUtils.isEmpty(cmsPostsDTO.getTagIds())) {
            // 新增标签和文章关系表数据
            cmsPostsDTO.getTagIds().forEach(id ->
                    this.addCmsTagPosts(cmsPostsDOWithBLOBs.getSiteUuid(),
                            Builder.of(CmsTagPostsDO::new)
                                    .with(CmsTagPostsDO::setPostUuid, cmsPostsDOWithBLOBs.getUuid())
                                    .with(CmsTagPostsDO::setTagUuid, id)
                                    .build())
            );
        }
        return rows;
    }

    /**
     * 修改文章（后台管理）
     *
     * @param cmsPostsDTO 文章传输对象
     * @return 插入行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public int updateCmsPost(CmsPostsDTO cmsPostsDTO) throws ForbiddenException, FailureException {
        AccountDTO accountDTO = getSignedUser(accountService);
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsPostsDTO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("文章所属站点不存在");
        }
        CmsPostsDOExample example = new CmsPostsDOExample();
        example.createCriteria().andSiteUuidEqualTo(cmsPostsDTO.getSiteUuid()).andUuidEqualTo(cmsPostsDTO.getUuid());
        CmsPostsDOWithBLOBs oldCmsSiteDO = ListUtils.getOne(cmsPostsDOMapper.selectByExampleWithBLOBs(example));
        if (oldCmsSiteDO == null) {
            throw new FailureException("要更新的文章不存在");
        }
        if (!oldCmsSiteDO.getCategoryUuid().equals(cmsPostsDTO.getCategoryUuid())) {
            // 分类发生了变更，检查分类
            if (this.getCmsCategoryByUuid(cmsPostsDTO.getCategoryUuid()) == null) {
                throw new FailureException("文章所属分类不存在");
            }
        }
        oldCmsSiteDO.setCategoryUuid(cmsPostsDTO.getCategoryUuid());
        oldCmsSiteDO.setFeaturedImage(cmsPostsDTO.getFeaturedImage());
        oldCmsSiteDO.setTitle(cmsPostsDTO.getTitle());
        oldCmsSiteDO.setContent(cmsPostsDTO.getContent());
        oldCmsSiteDO.setIsOriginal(cmsPostsDTO.getIsOriginal());
        oldCmsSiteDO.setSourceUrl(cmsPostsDTO.getSourceUrl());
        oldCmsSiteDO.setSourceName(cmsPostsDTO.getSourceName());
        oldCmsSiteDO.setDescribes(cmsPostsDTO.getDescribes());
        oldCmsSiteDO.setIsDelete(cmsPostsDTO.getIsDelete());
        oldCmsSiteDO.setIsComment(cmsPostsDTO.getIsComment());
        oldCmsSiteDO.setUpdateBy(accountDTO.getUuid());
        oldCmsSiteDO.setUpdateTime(new Date());
        int rows = cmsPostsDOMapper.updateByExampleWithBLOBs(oldCmsSiteDO, example);
        if (rows > 0) {
            // 先删除标签和文章关系表数据，再添加
            CmsTagPostsDOExample cmsTagPostsDOExample = new CmsTagPostsDOExample();
            cmsTagPostsDOExample.createCriteria().andPostUuidEqualTo(oldCmsSiteDO.getUuid());
            cmsTagPostsDOMapper.deleteByExample(cmsTagPostsDOExample);
            if (!BeanUtils.isEmpty(cmsPostsDTO.getTagIds())) {
                // 新增标签和文章关系表数据
                cmsPostsDTO.getTagIds().forEach(id ->
                        this.addCmsTagPosts(oldCmsSiteDO.getSiteUuid(),
                                Builder.of(CmsTagPostsDO::new)
                                        .with(CmsTagPostsDO::setPostUuid, oldCmsSiteDO.getUuid())
                                        .with(CmsTagPostsDO::setTagUuid, id)
                                        .build())
                );
            }
            return rows;
        } else {
            return 0;
        }
    }

    /**
     * 删除文章（后台管理）
     *
     * @param uuid 文章UUID
     * @return 删除行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public int deleteCmsPost(String uuid) throws ForbiddenException, FailureException {
        CmsPostsDOExample example = new CmsPostsDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        CmsPostsDOWithBLOBs oldCmsSiteDO = ListUtils.getOne(cmsPostsDOMapper.selectByExampleWithBLOBs(example));
        if (oldCmsSiteDO == null) {
            throw new FailureException("要删除的文章不存在");
        }
        // 查一下站点，用于鉴权
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(oldCmsSiteDO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("文章所属站点不存在");
        }
        CmsTagPostsDOExample cmsTagPostsDOExample = new CmsTagPostsDOExample();
        cmsTagPostsDOExample.createCriteria().andPostUuidEqualTo(oldCmsSiteDO.getUuid());
        cmsTagPostsDOMapper.deleteByExample(cmsTagPostsDOExample);
        return cmsPostsDOMapper.deleteByExample(example);
    }
    //</editor-fold>

    //<editor-fold desc="标签类的接口" defaultstate="collapsed">

    /**
     * 获取标签列表（后台管理）
     *
     * @param siteUuid 站点UUID
     * @param pages    页码
     * @param rows     行数
     * @return 标签列表
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public ListData<CmsTagDTO> getTagList(String siteUuid, int pages, int rows) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(siteUuid);
        if (cmsSiteDTO == null) {
            throw new FailureException("站点不存在");
        }
        CmsTagDOExample example = new CmsTagDOExample();
        example.createCriteria().andSiteUuidEqualTo(siteUuid);
        Page page = PageHelper.startPage(pages, rows);
        List<CmsTagDO> cmsTagDOS = cmsTagDOMapper.selectByExampleWithBLOBs(example);
        if (BeanUtils.isEmpty(cmsTagDOS)) {
            return new ListData<>();
        }
        ListData<CmsTagDTO> cmsTagDTOListData = new ListData<>();
        List<CmsTagDTO> cmsTagDTOS = new ArrayList<>();
        cmsTagDOS.forEach(cmsTagDO -> cmsTagDTOS.add(convert(cmsTagDO)));
        cmsTagDTOListData.setTotal(page.getTotal());
        cmsTagDTOListData.setData(cmsTagDTOS);
        return cmsTagDTOListData;
    }

    /**
     * 获取所有标签列表以及文章数量（前台）
     *
     * @param siteUuid 站点ID
     * @return
     */
    @Override
    public List<CmsTagDTO> getAllTagAndCount(String siteUuid) {
        CmsTagDOExample example = new CmsTagDOExample();
        example.createCriteria().andSiteUuidEqualTo(siteUuid);
        List<CmsTagDO> cmsTagDOS = cmsTagDOMapper.selectByExampleWithBLOBs(example);
        if (BeanUtils.isEmpty(cmsTagDOS)) {
            return null;
        }
        List<CmsTagDTO> cmsTagDTOS = new ArrayList<>();
        List<CmsTagDTO> finalCmsTagDTOS = cmsTagDTOS;
        cmsTagDOS.forEach(cmsTagDO -> finalCmsTagDTOS.add(convert(cmsTagDO)));
        for (CmsTagDTO dto : cmsTagDTOS
        ) {
            CmsTagPostsDOExample cmsTagPostsDOExample = new CmsTagPostsDOExample();
            cmsTagPostsDOExample.createCriteria().andTagUuidEqualTo(dto.getUuid());
            Page page = PageHelper.startPage(1, 1);
            cmsTagPostsDOMapper.selectByExample(cmsTagPostsDOExample);
            dto.setCount(page.getTotal());
        }
        cmsTagDTOS = finalCmsTagDTOS.stream().sorted(Comparator.comparing(CmsTagDTO::getCount).reversed()).collect(Collectors.toList());
        return cmsTagDTOS;
    }

    /**
     * 根据文章UUID获取标签列表
     *
     * @param postUuid 文章UUID
     * @return
     */
    @Override
    public List<CmsTagDTO> getTagListByPostUuid(String postUuid) {
        CmsTagPostsDOExample cmsTagPostsDOExample = new CmsTagPostsDOExample();
        cmsTagPostsDOExample.createCriteria().andPostUuidEqualTo(postUuid);
        List<CmsTagPostsDO> cmsTagPostsDOS = cmsTagPostsDOMapper.selectByExample(cmsTagPostsDOExample);
        List<String> ids = new ArrayList<>();
        if (BeanUtils.isEmpty(cmsTagPostsDOS)) {
            return null;
        }
        cmsTagPostsDOS.forEach(cms -> ids.add(cms.getTagUuid()));
        CmsTagDOExample cmsTagDOExample = new CmsTagDOExample();
        cmsTagDOExample.createCriteria().andUuidIn(ids);
        List<CmsTagDO> cmsTagDOS = cmsTagDOMapper.selectByExampleWithBLOBs(cmsTagDOExample);
        if (BeanUtils.isEmpty(cmsTagDOS)) {
            return null;
        }
        List<CmsTagDTO> cmsTagDTOS = new ArrayList<>();
        cmsTagDOS.forEach(cmsTagDO -> cmsTagDTOS.add(convert(cmsTagDO)));
        return cmsTagDTOS;
    }

    /**
     * 根据UUID获取标签对象
     *
     * @param siteUuid 站点UUID
     * @param uuid     标签对象UUID
     * @return 标签对象
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public CmsTagDTO getTagByUuid(String siteUuid, String uuid) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(siteUuid);
        if (cmsSiteDTO == null) {
            throw new FailureException("站点不存在");
        }
        CmsTagDOExample example = new CmsTagDOExample();
        example.createCriteria().andSiteUuidEqualTo(siteUuid).andUuidEqualTo(uuid);
        CmsTagDO cmsTagDO = ListUtils.getOne(cmsTagDOMapper.selectByExampleWithBLOBs(example));
        if (cmsTagDO == null) {
            return null;
        }
        return convert(cmsTagDO);
    }

    /**
     * 添加标签
     *
     * @param cmsTagDTO 标签传输对象
     * @return 插入行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public int addCmsTag(CmsTagDTO cmsTagDTO) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsTagDTO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("站点不存在");
        }
        if (BeanUtils.isEmpty(cmsTagDTO.getEnName())) {
            throw new FailureException("标签英文名不能为空");
        }
        if (BeanUtils.isEmpty(cmsTagDTO.getZhName())) {
            throw new FailureException("标签中文名不能为空");
        }
        CmsTagDOExample example = new CmsTagDOExample();
        example.createCriteria().andEnNameEqualTo(cmsTagDTO.getEnName());
        List<CmsTagDO> cmsTagDOS = cmsTagDOMapper.selectByExample(example);
        if (cmsTagDOS != null && cmsTagDOS.size() > 0) {
            throw new FailureException("标签英文名已经存在不能重复添加");
        }
        CmsTagDO cmsTagDO = convert(cmsTagDTO);
        cmsTagDO.setUuid(UUID.randomUUID().toString().toUpperCase());
        return cmsTagDOMapper.insertSelective(cmsTagDO);
    }

    /**
     * 更新标签
     *
     * @param cmsTagDTO 标签传输对象
     * @return 修改行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public int updateCmsTag(CmsTagDTO cmsTagDTO) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsTagDTO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("站点不存在");
        }
        CmsTagDOExample example = new CmsTagDOExample();
        example.createCriteria().andSiteUuidEqualTo(cmsTagDTO.getSiteUuid()).andUuidEqualTo(cmsTagDTO.getUuid());
        CmsTagDO cmsTagDO = ListUtils.getOne(cmsTagDOMapper.selectByExample(example));
        if (cmsTagDO == null) {
            throw new FailureException("要修改的标签不存在");
        }
        if (!cmsTagDO.getEnName().equals(cmsTagDTO.getEnName())) {
            // 英文名发生了变更
            example = new CmsTagDOExample();
            example.createCriteria().andEnNameEqualTo(cmsTagDTO.getEnName());
            List<CmsTagDO> cmsTagDOS = cmsTagDOMapper.selectByExample(example);
            if (cmsTagDOS != null || cmsTagDOS.size() > 0) {
                throw new FailureException("标签英文名已经存在不能重复");
            }
        }
        cmsTagDO.setEnName(cmsTagDTO.getEnName());
        cmsTagDO.setZhName(cmsTagDTO.getZhName());
        cmsTagDO.setDescribe(cmsTagDTO.getDescribe());
        example = new CmsTagDOExample();
        example.createCriteria().andSiteUuidEqualTo(cmsTagDTO.getSiteUuid()).andUuidEqualTo(cmsTagDTO.getUuid());
        return cmsTagDOMapper.updateByExampleWithBLOBs(cmsTagDO, example);
    }

    /**
     * 删除标签
     *
     * @param uuid 标签UUID
     * @return 删除行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    @Override
    public int deleteCmsTag(String uuid) throws ForbiddenException, FailureException {
        CmsTagDOExample example = new CmsTagDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        CmsTagDO cmsTagDO = ListUtils.getOne(cmsTagDOMapper.selectByExample(example));
        if (cmsTagDO == null) {
            throw new FailureException("要删除的标签不存在");
        }
        // 获取一下站点用于鉴权
        this.getCmsSiteByUuid(cmsTagDO.getSiteUuid());
        // 删除关系表
        CmsTagPostsDOExample cmsTagPostsDOExample = new CmsTagPostsDOExample();
        cmsTagPostsDOExample.createCriteria().andTagUuidEqualTo(uuid);
        cmsTagPostsDOMapper.deleteByExample(cmsTagPostsDOExample);
        // 删除标签
        return cmsTagDOMapper.deleteByExample(example);
    }
    //</editor-fold>

    //<editor-fold desc="菜单类的接口" defaultstate="collapsed">

    /**
     * 获取CMS菜单树
     *
     * @param siteUuid    站点UUID
     * @param cmsMenuEnum 菜单类型
     * @return CMS菜单树
     */
    @Override
    public List<CmsMenuVO> getCmsMenuBySiteUuidAndType(String siteUuid, CmsMenuEnum cmsMenuEnum) {
        // 因为CMS是开放的，而且菜单没有什么敏感信息，所以获取菜单的操作就不做站点权限校验了
        CmsMenuDOExample example = new CmsMenuDOExample();
        example.setOrderByClause("order_number DESC");
        example.createCriteria()
                .andSiteUuidEqualTo(siteUuid)
                .andMenuTypeEqualTo(cmsMenuEnum.value())
                .andPuuidEqualTo(siteUuid);
        List<CmsMenuDO> cmsMenuDOS = cmsMenuDOMapper.selectByExample(example);
        if (BeanUtils.isEmpty(cmsMenuDOS)) {
            return null;
        }
        List<CmsMenuVO> cmsMenuVOS = new ArrayList<>();
        cmsMenuDOS.forEach(cmsMenuDO -> cmsMenuVOS.add(convert(cmsMenuDO)));
        setCmsMenuChildren(cmsMenuVOS);
        return cmsMenuVOS;
    }

    /**
     * 根据CMS系统的UUID获取菜单对象
     *
     * @param uuid 菜单UUID
     * @return
     */
    @Override
    public CmsMenuVO getCmsMenuByUuid(String uuid) {
        CmsMenuDOExample example = new CmsMenuDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        return convert(ListUtils.getOne(cmsMenuDOMapper.selectByExample(example)));
    }

    /**
     * 添加菜单（CMS系统）
     *
     * @param cmsMenuVO
     * @return
     * @throws ForbiddenException
     * @throws FailureException
     */
    @Override
    public int addCmsMenu(CmsMenuVO cmsMenuVO) throws ForbiddenException, FailureException {
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsMenuVO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("所属站点不存在");
        }
        if (!cmsMenuVO.getSiteUuid().equals(cmsMenuVO.getPuuid())) {
            if (BeanUtils.isEmpty(this.getCmsMenuByUuid(cmsMenuVO.getPuuid()))) {
                throw new FailureException("指定的父级菜单不存在");
            }
        }
        CmsMenuDO cmsMenuDO = convert(cmsMenuVO);
        cmsMenuDO.setCreateBy(getSignedUser(accountService).getUuid());
        cmsMenuDO.setCreateTime(new Date());
        cmsMenuDO.setUpdateBy(getSignedUser(accountService).getUuid());
        cmsMenuDO.setUpdateTime(new Date());
        return cmsMenuDOMapper.insertSelective(cmsMenuDO);
    }

    /**
     * 修改菜单（CMS系统）
     *
     * @param cmsMenuVO
     * @return
     * @throws ForbiddenException
     * @throws FailureException
     */
    @Override
    public int updateCmsMenu(CmsMenuVO cmsMenuVO) throws ForbiddenException, FailureException {
        CmsMenuDOExample example = new CmsMenuDOExample();
        example.createCriteria().andUuidEqualTo(cmsMenuVO.getUuid());
        CmsMenuDO oldCmsMenu = ListUtils.getOne(cmsMenuDOMapper.selectByExample(example));
        if (BeanUtils.isEmpty(oldCmsMenu)) {
            throw new FailureException("要修改的菜单不存在");
        }
        // 这里获取一下站点是为了校验他是否有这个站点的权限
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsMenuVO.getSiteUuid());
        if (cmsSiteDTO == null) {
            throw new FailureException("所属站点不存在");
        }
        if (!cmsMenuVO.getSiteUuid().equals(cmsMenuVO.getPuuid())) {
            if (BeanUtils.isEmpty(this.getCmsMenuByUuid(cmsMenuVO.getPuuid()))) {
                throw new FailureException("指定的父级菜单不存在");
            }
        }
        oldCmsMenu.setIsNewWin(cmsMenuVO.getIsNewWin());
        oldCmsMenu.setPuuid(cmsMenuVO.getPuuid());
        oldCmsMenu.setMenuText(cmsMenuVO.getMenuText());
        oldCmsMenu.setMenuIcon(cmsMenuVO.getMenuIcon());
        oldCmsMenu.setMenuLink(cmsMenuVO.getMenuLink());
        oldCmsMenu.setMenuType(cmsMenuVO.getMenuType());
        oldCmsMenu.setOrderNumber(cmsMenuVO.getOrderNumber());
        oldCmsMenu.setUpdateBy(getSignedUser(accountService).getUuid());
        oldCmsMenu.setUpdateTime(new Date());
        return cmsMenuDOMapper.updateByExampleSelective(oldCmsMenu, example);
    }

    /**
     * 删除菜单（CMS系统）
     *
     * @param uuid
     * @return
     * @throws ForbiddenException
     */
    @Override
    public int deleteCmsMenu(String uuid) throws ForbiddenException {
        CmsMenuDOExample example = new CmsMenuDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        CmsMenuDO oldCmsMenu = ListUtils.getOne(cmsMenuDOMapper.selectByExample(example));
        if (BeanUtils.isEmpty(oldCmsMenu)) {
            throw new FailureException("要删除的菜单不存在");
        }
        // 这里获取一下站点是为了校验他是否有这个站点的权限
        this.getCmsSiteByUuid(oldCmsMenu.getSiteUuid());
        return cmsMenuDOMapper.deleteByExample(example);
    }
    //</editor-fold>

    //<editor-fold desc="评论类的接口" defaultstate="collapsed">

    /**
     * 添加评论
     *
     * @param commentDTO 评论数据传输对象
     * @return
     */
    @Override
    public APIResult addComment(CommentDTO commentDTO) {
        // 先获取评论目标
        CmsPostsDTO cmsPostsDTO = this.getCmsPostByUuid(commentDTO.getPostUuid());
        if (cmsPostsDTO == null) {
            // 评论目标不存在
            return APIResult.builder().code(StateCode.Failure).message("评论目标不存在").build();
        }
        // 检查全局评论开关
        CmsSiteDTO cmsSiteDTO = this.getCmsSiteByUuid(cmsPostsDTO.getSiteUuid());
        if (cmsSiteDTO == null || !cmsSiteDTO.getIsComment()) {
            // 全局评论禁止，或者网站不存在
            return APIResult.builder().code(StateCode.Failure).message("禁止评论").build();
        }
        // 检查目标评论开关
        if (!cmsPostsDTO.getIsComment()) {
            // 目标禁止评论
            return APIResult.builder().code(StateCode.Failure).message("禁止评论").build();
        }
        commentDTO.setUuid(UUID.randomUUID().toString().toUpperCase());
        CmsCommentsDOWithBLOBs cmsCommentsDOWithBLOBs = convert(commentDTO);
        cmsCommentsDOWithBLOBs.setAddtime(new Date());
        cmsCommentsDOWithBLOBs.setIsDelete(true);
        cmsCommentsDOWithBLOBs.setIsOwner(false);
        cmsCommentsDOMapper.insertSelective(cmsCommentsDOWithBLOBs);
        // 检查评论内容
        textScan(commentDTO);
        return APIResult.success();
    }

    /**
     * 根据文章UUID获取评论树
     *
     * @param postUuid 文章UUID
     * @return
     */
    @Override
    public List<CommentDTO> getCommentByPostId(String postUuid) {
        CmsCommentsDOExample example = new CmsCommentsDOExample();
        example.createCriteria()
                .andPostUuidEqualTo(postUuid)
                .andIsDeleteEqualTo(false)
                .andParentIdIsNull();
        List<CmsCommentsDOWithBLOBs> cmsCommentsDOWithBLOBs = cmsCommentsDOMapper.selectByExampleWithBLOBs(example);
        if (BeanUtils.isEmpty(cmsCommentsDOWithBLOBs)) {
            return new ArrayList<>();
        }
        List<CommentDTO> commentDTOS = new ArrayList<>();
        cmsCommentsDOWithBLOBs.forEach(cmsCommentsDO -> commentDTOS.add(convert(cmsCommentsDO)));
        getCommentByParentID(commentDTOS);
        return commentDTOS;
    }

    /**
     * 获取最新的评论
     *
     * @param size 获取数量
     * @return
     */
    @Override
    public List<CommentDTO> getLastComment(int size) {
        CmsCommentsDOExample example = new CmsCommentsDOExample();
        example.setOrderByClause("addtime DESC");
        example.createCriteria()
                .andIsDeleteEqualTo(false)
                .andParentIdIsNull();
        PageHelper.startPage(1, size);
        List<CmsCommentsDOWithBLOBs> cmsCommentsDOWithBLOBs = cmsCommentsDOMapper.selectByExampleWithBLOBs(example);
        if (BeanUtils.isEmpty(cmsCommentsDOWithBLOBs)) {
            return new ArrayList<>();
        }
        List<CommentDTO> commentDTOS = new ArrayList<>();
        cmsCommentsDOWithBLOBs.forEach(cmsCommentsDO -> commentDTOS.add(convert(cmsCommentsDO)));
        return commentDTOS;
    }

    /**
     * 根据文章UUID获取评论数量
     *
     * @param postUuid 文章UUID
     * @return
     */
    @Override
    public Long getCommentNumber(String postUuid) {
        CmsCommentsDOExample example = new CmsCommentsDOExample();
        example.createCriteria()
                .andPostUuidEqualTo(postUuid)
                .andIsDeleteEqualTo(false);
        Page page = PageHelper.startPage(1, 1);
        cmsCommentsDOMapper.selectByExampleWithBLOBs(example);
        return page.getTotal();
    }

    @Async
    public void textScan(CommentDTO commentDTO) {
        if (!"".equals(wintereeCoreConfig.getAliyunGreen().getRegionId())) {
            // 启用了
            if (aliyunGreenService.textScan(commentDTO.getContent())) {
                CmsCommentsDOWithBLOBs cmsCommentsDOWithBLOBs = new CmsCommentsDOWithBLOBs();
                cmsCommentsDOWithBLOBs.setIsDelete(false);
                CmsCommentsDOExample example = new CmsCommentsDOExample();
                example.createCriteria().andUuidEqualTo(commentDTO.getUuid());
                cmsCommentsDOMapper.updateByExampleSelective(cmsCommentsDOWithBLOBs, example);
            }
        }
    }
    //</editor-fold>

    /**
     * 跟新文章评级
     */
    @Override
    public void updatePageRank() {
        CmsPostsDOExample example = new CmsPostsDOExample();
        example.createCriteria();
        int pages = 1;
        for (; ; ) {
            PageHelper.startPage(pages, 100);
            List<CmsPostsDOWithBLOBs> cmsPostsDOWithBLOBs = cmsPostsDOMapper.selectByExampleWithBLOBs(example);
            if (BeanUtils.isEmpty(cmsPostsDOWithBLOBs)) {
                break;
            }
            pages++;
            for (CmsPostsDOWithBLOBs post : cmsPostsDOWithBLOBs
            ) {
                setPageRank(post);
                cmsPostsDOMapper.updateByPrimaryKey(post);
            }
        }

    }

    /**
     * 更新文章浏览量
     *
     * @param cmsPostsDO 文章对象
     */
    @Async
    public void updateViews(CmsPostsDOWithBLOBs cmsPostsDO) {
        CmsPostsDOExample example = new CmsPostsDOExample();
        example.createCriteria().andUuidEqualTo(cmsPostsDO.getUuid());
        cmsPostsDO.setId(null);
        cmsPostsDO.setViews(cmsPostsDO.getViews() + 1);
        cmsPostsDOMapper.updateByExampleSelective(cmsPostsDO, example);
    }

    /**
     * 递归查询子评论
     *
     * @param commentDTOS 父级评论列表
     */
    private void getCommentByParentID(List<CommentDTO> commentDTOS) {
        if (commentDTOS != null && commentDTOS.size() > 0) {
            for (CommentDTO commentDTO : commentDTOS
            ) {
                CmsCommentsDOExample example = new CmsCommentsDOExample();
                example.createCriteria()
                        .andParentIdEqualTo(commentDTO.getId());
                List<CmsCommentsDOWithBLOBs> cmsCommentsDOWithBLOBs = cmsCommentsDOMapper.selectByExampleWithBLOBs(example);
                if (BeanUtils.isEmpty(cmsCommentsDOWithBLOBs)) {
                    continue;
                }
                List<CommentDTO> child = new ArrayList<>();
                cmsCommentsDOWithBLOBs.forEach(cmsCommentsDO -> child.add(convert(cmsCommentsDO)));
                if (child.size() > 0) {
                    getCommentByParentID(child);
                    commentDTO.setChild(child);
                }
            }
        }
    }

    private void setPageRank(CmsPostsDOWithBLOBs cmsPostsDOWithBLOBs) {
        PageRankUtil pageRankUtil = new PageRankUtil();
        cmsPostsDOWithBLOBs.setPageRank(pageRankUtil.getPageRank(
                cmsPostsDOWithBLOBs.getReleaseTime(),
                cmsPostsDOWithBLOBs.getViews(),
                0L,
                DATE_WEIGHTED, VIEW_WEIGHTED, COMMENTHTED
        ));
        cmsPostsDOWithBLOBs.setAvgViews(pageRankUtil.getAvgViews(
                cmsPostsDOWithBLOBs.getReleaseTime(),
                cmsPostsDOWithBLOBs.getViews()
        ));
        cmsPostsDOWithBLOBs.setAvgComment(pageRankUtil.getAvgComments(
                cmsPostsDOWithBLOBs.getReleaseTime(),
                0L
        ));
        cmsPostsDOWithBLOBs.setPageRankUpdateTime(new Date());
    }

    /**
     * 递归查询子级菜单（CMS系统）
     *
     * @param cmsMenuVOS
     */
    private void setCmsMenuChildren(List<CmsMenuVO> cmsMenuVOS) {
        if (BeanUtils.isEmpty(cmsMenuVOS)) {
            return;
        }
        for (CmsMenuVO menu : cmsMenuVOS
        ) {
            CmsMenuDOExample example = new CmsMenuDOExample();
            example.setOrderByClause("order_number DESC");
            example.createCriteria()
                    .andSiteUuidEqualTo(menu.getSiteUuid())
                    .andMenuTypeEqualTo(menu.getMenuType())
                    .andPuuidEqualTo(menu.getUuid());
            List<CmsMenuDO> cmsMenuDOS = cmsMenuDOMapper.selectByExample(example);
            if (!BeanUtils.isEmpty(cmsMenuVOS)) {
                List<CmsMenuVO> cmsMenuChildren = new ArrayList<>();
                cmsMenuDOS.forEach(cmsMenuDO -> cmsMenuChildren.add(convert(cmsMenuDO)));
                menu.setChildren(cmsMenuChildren);
                setCmsMenuChildren(cmsMenuChildren);
            }
        }
    }

    /**
     * 新增标签和文章关系表数据
     * 因为标签和文章关系应该是程序自动维护的，所以是私有方法
     *
     * @param cmsTagPostsDO
     * @return
     */
    private int addCmsTagPosts(String siteUuid, CmsTagPostsDO cmsTagPostsDO) throws ForbiddenException, FailureException {
        if (this.getTagByUuid(siteUuid, cmsTagPostsDO.getTagUuid()) == null) {
            return 0;
        }
        cmsTagPostsDO.setUuid(UUID.randomUUID().toString().toUpperCase());
        return cmsTagPostsDOMapper.insertSelective(cmsTagPostsDO);
    }

    /**
     * 检查操作权限
     *
     * @param accountDTO
     * @param cmsSiteDO
     */
    private void checkPermissions(AccountDTO accountDTO, CmsSiteDO cmsSiteDO) {
        // 获取子公司和子部门
        List<OrganizationVO> organizationVOS = organizationService.getDepartmentListUnderCompanyUuid(
                accountDTO.getTenantUuid(), accountDTO.getOfficeUuid());
        // 验证数据权限范围，是全部还是本公司
        DataScopeEnum dataScopeEnum = roleService.getDataScope();
        switch (dataScopeEnum) {
            case ALL:
                break;
            case COMPANY:
                if (BeanUtils.isEmpty(organizationVOS)) {
                    throw new ForbiddenException("权限不足");
                }
                // 只能添加自己公司的
                organizationVOS.forEach(organizationVO -> {
                    if (organizationVO.getUuid().equals(cmsSiteDO.getOfficeUuid())) {
                        return;
                    }
                });
                throw new ForbiddenException("权限不足");
            case DEPARTMENT:
                if (BeanUtils.isEmpty(organizationVOS)) {
                    throw new ForbiddenException("权限不足");
                }
                // 只能添加自己部门的
                organizationVOS.forEach(organizationVO -> {
                    if (organizationVO.getUuid().equals(cmsSiteDO.getDepartmentUuid())) {
                        return;
                    }
                });
                throw new ForbiddenException("权限不足");
            default:
                throw new ForbiddenException("权限不足");
        }
    }

    private void fillSiteInfo(CmsSiteDTO cmsSiteDTO) {
        if (!BeanUtils.isEmpty(cmsSiteDTO.getTenantUuid())) {
            TenantDO tenantDO = tenantService.getTenantDOByUUID(cmsSiteDTO.getTenantUuid());
            if (tenantDO != null) {
                cmsSiteDTO.setTenantName(tenantDO.getName());
            }
        }
        if (!BeanUtils.isEmpty(cmsSiteDTO.getOfficeUuid())) {
            OrganizationDO organizationDO = organizationService.getCompanyByUuid(cmsSiteDTO.getOfficeUuid());
            if (organizationDO != null) {
                cmsSiteDTO.setOfficeName(organizationDO.getName());
            }
        }
        if (!BeanUtils.isEmpty(cmsSiteDTO.getDepartmentUuid())) {
            OrganizationDO organizationDO = organizationService.getDepartmentByUuid(cmsSiteDTO.getDepartmentUuid());
            if (organizationDO != null) {
                cmsSiteDTO.setDepartmentName(organizationDO.getName());
            }
        }
        if (!BeanUtils.isEmpty(cmsSiteDTO.getCreateBy())) {
            com.winteree.api.entity.AccountDTO accountDTO = accountService.getAccountById(cmsSiteDTO.getCreateBy());
            if (accountDTO != null) {
                cmsSiteDTO.setCreateByName(accountDTO.getUserName());
            }
        }
        if (!BeanUtils.isEmpty(cmsSiteDTO.getUpdateBy())) {
            com.winteree.api.entity.AccountDTO accountDTO = accountService.getAccountById(cmsSiteDTO.getUpdateBy());
            if (accountDTO != null) {
                cmsSiteDTO.setUpdateByName(accountDTO.getUserName());
            }
        }
    }

    private CmsSiteDTO convert(CmsSiteDO cmsSiteDO) {
        return Builder.of(CmsSiteDTO::new)
                .with(CmsSiteDTO::setUuid, cmsSiteDO.getUuid())
                .with(CmsSiteDTO::setTenantUuid, cmsSiteDO.getTenantUuid())
                .with(CmsSiteDTO::setOfficeUuid, cmsSiteDO.getOfficeUuid())
                .with(CmsSiteDTO::setDepartmentUuid, cmsSiteDO.getDepartmentUuid())
                .with(CmsSiteDTO::setSiteName, cmsSiteDO.getSiteName())
                .with(CmsSiteDTO::setSiteDomain, cmsSiteDO.getSiteDomain().toLowerCase())
                .with(CmsSiteDTO::setSiteKeyword, cmsSiteDO.getSiteKeyword())
                .with(CmsSiteDTO::setSiteDescription, cmsSiteDO.getSiteDescription())
                .with(CmsSiteDTO::setIcpNo, cmsSiteDO.getIcpNo())
                .with(CmsSiteDTO::setIsComment, cmsSiteDO.getIsComment())
                .with(CmsSiteDTO::setGonganNo, cmsSiteDO.getGonganNo())
                .with(CmsSiteDTO::setAnalysisCode, cmsSiteDO.getAnalysisCode())
                .with(CmsSiteDTO::setCreateTime, cmsSiteDO.getCreateTime())
                .with(CmsSiteDTO::setCreateBy, cmsSiteDO.getCreateBy())
                .with(CmsSiteDTO::setUpdateTime, cmsSiteDO.getUpdateTime())
                .with(CmsSiteDTO::setUpdateBy, cmsSiteDO.getUpdateBy())
                .with(CmsSiteDTO::setSiteEnable, cmsSiteDO.getSiteEnable())
                .build();
    }

    private CmsSiteDO convert(CmsSiteDTO cmsSiteDTO) {
        return Builder.of(CmsSiteDO::new)
                .with(CmsSiteDO::setUuid, cmsSiteDTO.getUuid())
                .with(CmsSiteDO::setTenantUuid, cmsSiteDTO.getTenantUuid())
                .with(CmsSiteDO::setOfficeUuid, cmsSiteDTO.getOfficeUuid())
                .with(CmsSiteDO::setDepartmentUuid, cmsSiteDTO.getDepartmentUuid())
                .with(CmsSiteDO::setSiteName, cmsSiteDTO.getSiteName())
                .with(CmsSiteDO::setSiteDomain, cmsSiteDTO.getSiteDomain().toLowerCase())
                .with(CmsSiteDO::setSiteKeyword, cmsSiteDTO.getSiteKeyword())
                .with(CmsSiteDO::setSiteDescription, cmsSiteDTO.getSiteDescription())
                .with(CmsSiteDO::setIcpNo, cmsSiteDTO.getIcpNo())
                .with(CmsSiteDO::setIsComment, cmsSiteDTO.getIsComment())
                .with(CmsSiteDO::setGonganNo, cmsSiteDTO.getGonganNo())
                .with(CmsSiteDO::setAnalysisCode, cmsSiteDTO.getAnalysisCode())
                .with(CmsSiteDO::setCreateTime, cmsSiteDTO.getCreateTime())
                .with(CmsSiteDO::setCreateBy, cmsSiteDTO.getCreateBy())
                .with(CmsSiteDO::setUpdateTime, cmsSiteDTO.getUpdateTime())
                .with(CmsSiteDO::setUpdateBy, cmsSiteDTO.getUpdateBy())
                .with(CmsSiteDO::setSiteEnable, cmsSiteDTO.getSiteEnable())
                .build();
    }

    private CmsCategoryDTO convert(CmsCategoryDO cmsCategoryDO) {
        return Builder.of(CmsCategoryDTO::new)
                .with(CmsCategoryDTO::setSiteUuid, cmsCategoryDO.getSiteUuid())
                .with(CmsCategoryDTO::setEnName, cmsCategoryDO.getEnName())
                .with(CmsCategoryDTO::setZhName, cmsCategoryDO.getZhName())
                .with(CmsCategoryDTO::setUuid, cmsCategoryDO.getUuid())
                .build();
    }

    private CmsCategoryDO convert(CmsCategoryDTO cmsCategoryDTO) {
        return Builder.of(CmsCategoryDO::new)
                .with(CmsCategoryDO::setSiteUuid, cmsCategoryDTO.getSiteUuid())
                .with(CmsCategoryDO::setEnName, cmsCategoryDTO.getEnName())
                .with(CmsCategoryDO::setZhName, cmsCategoryDTO.getZhName())
                .with(CmsCategoryDO::setUuid, cmsCategoryDTO.getUuid())
                .build();
    }

    private CmsPostsDTO convert(CmsPostsDOWithBLOBs cmsPostsDOWithBLOBs) {
        return Builder.of(CmsPostsDTO::new)
                .with(CmsPostsDTO::setId, cmsPostsDOWithBLOBs.getId())
                .with(CmsPostsDTO::setUuid, cmsPostsDOWithBLOBs.getUuid())
                .with(CmsPostsDTO::setSiteUuid, cmsPostsDOWithBLOBs.getSiteUuid())
                .with(CmsPostsDTO::setCategoryUuid, cmsPostsDOWithBLOBs.getCategoryUuid())
                .with(CmsPostsDTO::setIsOriginal, cmsPostsDOWithBLOBs.getIsOriginal())
                .with(CmsPostsDTO::setViews, cmsPostsDOWithBLOBs.getViews())
                .with(CmsPostsDTO::setThumbsUp, cmsPostsDOWithBLOBs.getThumbsUp())
                .with(CmsPostsDTO::setThumbsDown, cmsPostsDOWithBLOBs.getThumbsDown())
                .with(CmsPostsDTO::setReleaseTime, cmsPostsDOWithBLOBs.getReleaseTime())
                .with(CmsPostsDTO::setCreateTime, cmsPostsDOWithBLOBs.getCreateTime())
                .with(CmsPostsDTO::setCreateBy, cmsPostsDOWithBLOBs.getCreateBy())
                .with(CmsPostsDTO::setUpdateTime, cmsPostsDOWithBLOBs.getUpdateTime())
                .with(CmsPostsDTO::setUpdateBy, cmsPostsDOWithBLOBs.getUpdateBy())
                .with(CmsPostsDTO::setIsDelete, cmsPostsDOWithBLOBs.getIsDelete())
                .with(CmsPostsDTO::setIsComment, cmsPostsDOWithBLOBs.getIsComment())
                .with(CmsPostsDTO::setAvgViews, cmsPostsDOWithBLOBs.getAvgViews())
                .with(CmsPostsDTO::setAvgComment, cmsPostsDOWithBLOBs.getAvgComment())
                .with(CmsPostsDTO::setPageRank, cmsPostsDOWithBLOBs.getPageRank())
                .with(CmsPostsDTO::setPageRankUpdateTime, cmsPostsDOWithBLOBs.getPageRankUpdateTime())
                .with(CmsPostsDTO::setFeaturedImage, cmsPostsDOWithBLOBs.getFeaturedImage())
                .with(CmsPostsDTO::setTitle, cmsPostsDOWithBLOBs.getTitle())
                .with(CmsPostsDTO::setContent, cmsPostsDOWithBLOBs.getContent())
                .with(CmsPostsDTO::setSourceUrl, cmsPostsDOWithBLOBs.getSourceUrl())
                .with(CmsPostsDTO::setSourceName, cmsPostsDOWithBLOBs.getSourceName())
                .with(CmsPostsDTO::setDescribes, cmsPostsDOWithBLOBs.getDescribes())
                .with(CmsPostsDTO::setKeyword, cmsPostsDOWithBLOBs.getKeyword())
                .build();
    }

    private CmsPostsDOWithBLOBs convert(CmsPostsDTO cmsPostsDTO) {
        return Builder.of(CmsPostsDOWithBLOBs::new)
                .with(CmsPostsDOWithBLOBs::setId, cmsPostsDTO.getId())
                .with(CmsPostsDOWithBLOBs::setUuid, cmsPostsDTO.getUuid())
                .with(CmsPostsDOWithBLOBs::setSiteUuid, cmsPostsDTO.getSiteUuid())
                .with(CmsPostsDOWithBLOBs::setCategoryUuid, cmsPostsDTO.getCategoryUuid())
                .with(CmsPostsDOWithBLOBs::setIsOriginal, cmsPostsDTO.getIsOriginal())
                .with(CmsPostsDOWithBLOBs::setViews, cmsPostsDTO.getViews())
                .with(CmsPostsDOWithBLOBs::setThumbsUp, cmsPostsDTO.getThumbsUp())
                .with(CmsPostsDOWithBLOBs::setThumbsDown, cmsPostsDTO.getThumbsDown())
                .with(CmsPostsDOWithBLOBs::setReleaseTime, cmsPostsDTO.getReleaseTime())
                .with(CmsPostsDOWithBLOBs::setCreateTime, cmsPostsDTO.getCreateTime())
                .with(CmsPostsDOWithBLOBs::setCreateBy, cmsPostsDTO.getCreateBy())
                .with(CmsPostsDOWithBLOBs::setUpdateTime, cmsPostsDTO.getUpdateTime())
                .with(CmsPostsDOWithBLOBs::setUpdateBy, cmsPostsDTO.getUpdateBy())
                .with(CmsPostsDOWithBLOBs::setIsDelete, cmsPostsDTO.getIsDelete())
                .with(CmsPostsDOWithBLOBs::setIsComment, cmsPostsDTO.getIsComment())
                .with(CmsPostsDOWithBLOBs::setAvgViews, cmsPostsDTO.getAvgViews())
                .with(CmsPostsDOWithBLOBs::setAvgComment, cmsPostsDTO.getAvgComment())
                .with(CmsPostsDOWithBLOBs::setPageRank, cmsPostsDTO.getPageRank())
                .with(CmsPostsDOWithBLOBs::setPageRankUpdateTime, cmsPostsDTO.getPageRankUpdateTime())
                .with(CmsPostsDOWithBLOBs::setFeaturedImage, cmsPostsDTO.getFeaturedImage())
                .with(CmsPostsDOWithBLOBs::setTitle, cmsPostsDTO.getTitle())
                .with(CmsPostsDOWithBLOBs::setContent, cmsPostsDTO.getContent())
                .with(CmsPostsDOWithBLOBs::setSourceUrl, cmsPostsDTO.getSourceUrl())
                .with(CmsPostsDOWithBLOBs::setSourceName, cmsPostsDTO.getSourceName())
                .with(CmsPostsDOWithBLOBs::setDescribes, cmsPostsDTO.getDescribes())
                .with(CmsPostsDOWithBLOBs::setKeyword, cmsPostsDTO.getKeyword())
                .build();
    }

    private CmsTagDTO convert(CmsTagDO cmsTagDO) {
        return Builder.of(CmsTagDTO::new)
                .with(CmsTagDTO::setUuid, cmsTagDO.getUuid())
                .with(CmsTagDTO::setSiteUuid, cmsTagDO.getSiteUuid())
                .with(CmsTagDTO::setEnName, cmsTagDO.getEnName())
                .with(CmsTagDTO::setZhName, cmsTagDO.getZhName())
                .with(CmsTagDTO::setDescribe, cmsTagDO.getDescribe())
                .build();
    }

    private CmsTagDO convert(CmsTagDTO cmsTagDTO) {
        return Builder.of(CmsTagDO::new)
                .with(CmsTagDO::setUuid, cmsTagDTO.getUuid())
                .with(CmsTagDO::setSiteUuid, cmsTagDTO.getSiteUuid())
                .with(CmsTagDO::setEnName, cmsTagDTO.getEnName())
                .with(CmsTagDO::setZhName, cmsTagDTO.getZhName())
                .with(CmsTagDO::setDescribe, cmsTagDTO.getDescribe())
                .build();
    }

    private CmsMenuVO convert(CmsMenuDO cmsMenuDO) {
        return Builder.of(CmsMenuVO::new)
                .with(CmsMenuVO::setUuid, cmsMenuDO.getUuid())
                .with(CmsMenuVO::setSiteUuid, cmsMenuDO.getSiteUuid())
                .with(CmsMenuVO::setPuuid, cmsMenuDO.getPuuid())
                .with(CmsMenuVO::setOrderNumber, cmsMenuDO.getOrderNumber())
                .with(CmsMenuVO::setMenuType, cmsMenuDO.getMenuType())
                .with(CmsMenuVO::setMenuText, cmsMenuDO.getMenuText())
                .with(CmsMenuVO::setMenuLink, cmsMenuDO.getMenuLink())
                .with(CmsMenuVO::setMenuIcon, cmsMenuDO.getMenuIcon())
                .with(CmsMenuVO::setIsNewWin, cmsMenuDO.getIsNewWin())
                .build();
    }

    private CmsMenuDO convert(CmsMenuVO cmsMenuVO) {
        return Builder.of(CmsMenuDO::new)
                .with(CmsMenuDO::setUuid, cmsMenuVO.getUuid())
                .with(CmsMenuDO::setSiteUuid, cmsMenuVO.getSiteUuid())
                .with(CmsMenuDO::setPuuid, cmsMenuVO.getPuuid())
                .with(CmsMenuDO::setOrderNumber, cmsMenuVO.getOrderNumber())
                .with(CmsMenuDO::setMenuType, cmsMenuVO.getMenuType())
                .with(CmsMenuDO::setMenuText, cmsMenuVO.getMenuText())
                .with(CmsMenuDO::setMenuLink, cmsMenuVO.getMenuLink())
                .with(CmsMenuDO::setMenuIcon, cmsMenuVO.getMenuIcon())
                .with(CmsMenuDO::setIsNewWin, cmsMenuVO.getIsNewWin())
                .build();
    }

    private CmsCommentsDOWithBLOBs convert(CommentDTO commentDTO) {
        return Builder.of(CmsCommentsDOWithBLOBs::new)
                .with(CmsCommentsDOWithBLOBs::setId, commentDTO.getId())
                .with(CmsCommentsDOWithBLOBs::setAuthor, commentDTO.getAuthor())
                .with(CmsCommentsDOWithBLOBs::setAuthorAddress, commentDTO.getAuthorAddress())
                .with(CmsCommentsDOWithBLOBs::setAuthorEmail, commentDTO.getAuthorEmail())
                .with(CmsCommentsDOWithBLOBs::setAuthorIp, commentDTO.getAuthorIp())
                .with(CmsCommentsDOWithBLOBs::setAuthorUrl, commentDTO.getAuthorUrl())
                .with(CmsCommentsDOWithBLOBs::setContent, commentDTO.getContent())
                .with(CmsCommentsDOWithBLOBs::setPostUuid, commentDTO.getPostUuid())
                .with(CmsCommentsDOWithBLOBs::setParentId, commentDTO.getParentId())
                .build();
    }

    private CommentDTO convert(CmsCommentsDOWithBLOBs commentDTO) {
        return Builder.of(CommentDTO::new)
                .with(CommentDTO::setId, commentDTO.getId())
                .with(CommentDTO::setUuid, commentDTO.getUuid())
                .with(CommentDTO::setAuthor, commentDTO.getAuthor())
                .with(CommentDTO::setAuthorAddress, commentDTO.getAuthorAddress())
                .with(CommentDTO::setAuthorEmail, commentDTO.getAuthorEmail())
                .with(CommentDTO::setAuthorIp, commentDTO.getAuthorIp())
                .with(CommentDTO::setAuthorUrl, commentDTO.getAuthorUrl())
                .with(CommentDTO::setContent, commentDTO.getContent())
                .with(CommentDTO::setPostUuid, commentDTO.getPostUuid())
                .with(CommentDTO::setParentId, commentDTO.getParentId())
                .with(CommentDTO::setAddtime, commentDTO.getAddtime())
                .build();
    }

    /**
     * 文件评级工具内部类
     */
    public class PageRankUtil {
        public Double getPageRank(Date date, Long views, Long comments,
                                  Double dateWeighted, Double viewWeighted, Double commentWeighted) {
            long days = DateUtils.pastDays(date);
            if (days > -3) {
                return 10000D;
            }
            double avgViews = getAvgViews(date, views);
            double avgComments = getAvgComments(date, comments);
            return ((days * dateWeighted) + (avgViews * viewWeighted) + (avgComments * commentWeighted)) / (dateWeighted + viewWeighted + commentWeighted);
        }

        public Double getAvgViews(Date date, Long views) {
            long days = DateUtils.pastDays(date);
            return (double) ((float) views / (float) (-days));
        }

        public Double getAvgComments(Date date, Long comments) {
            long days = DateUtils.pastDays(date);
            return (double) ((float) comments / (float) (-days));
        }
    }
}
