package com.winteree.core.service;

import com.winteree.api.entity.*;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;

import java.util.List;

/**
 * <p>Title: CmsService</p>
 * <p>Description: 内容管理服务</p>
 *
 * @author RenFei
 * @date : 2020-06-24 20:27
 */
public interface CmsService {
    /**
     * 获取CMS站点列表（后台管理）
     *
     * @param tenantUuid 租户ID
     * @param page       页码
     * @param rows       行数
     * @return 站点列表
     */
    ListData<CmsSiteDTO> getCmsSiteList(String tenantUuid, int page, int rows);

    /**
     * 根据UUID获取CMS系统站点（后台管理）
     *
     * @param uuid UUID
     * @return 站点传输对象
     * @throws ForbiddenException 权限不足异常
     */
    CmsSiteDTO getCmsSiteByUuid(String uuid) throws ForbiddenException;

    /**
     * 根据域名获取站点信息
     *
     * @param domain 域名
     * @return
     */
    CmsSiteDTO getCmsSiteByDomain(String domain);

    /**
     * 添加站点（后台管理）
     *
     * @param cmsSiteDTO 站点信息传输对象
     * @return 插入条数
     * @throws FailureException 失败异常信息
     */
    int addCmsSite(CmsSiteDTO cmsSiteDTO) throws FailureException;

    /**
     * 更新站点（后台管理）
     *
     * @param cmsSiteDTO 站点信息传输对象
     * @return 修改条数
     * @throws ForbiddenException 失败异常信息
     * @throws FailureException   失败异常信息
     */
    int updateCmsSite(CmsSiteDTO cmsSiteDTO) throws ForbiddenException, FailureException;

    /**
     * 删除站点所有内容（后台管理）
     *
     * @param uuid 站点ID
     * @return 删除条数
     * @throws ForbiddenException 失败异常信息
     * @throws FailureException   失败异常信息
     */
    int deleteCmsSite(String uuid) throws ForbiddenException, FailureException;

    /**
     * 获取CMS系统分类列表（后台管理）
     *
     * @param siteUuid 站点ID
     * @return 文章分类列表
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    List<CmsCategoryDTO> getCmsCategoryList(String siteUuid) throws ForbiddenException, FailureException;

    /**
     * 根据UUID获取CMS系统站点下的分类
     *
     * @param uuid 分类UUID
     * @return 分类对象
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    CmsCategoryDTO getCmsCategoryByUuid(String uuid) throws ForbiddenException, FailureException;

    /**
     * 添加文章分类（后台管理）
     *
     * @param cmsCategoryDTO 文章分类传输对象
     * @return 插入行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    int addCmsCategory(CmsCategoryDTO cmsCategoryDTO) throws ForbiddenException, FailureException;

    /**
     * 更新文章分类（后台管理）
     *
     * @param cmsCategoryDTO 文章分类传输对象
     * @return 修改行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    int updateCmsCategory(CmsCategoryDTO cmsCategoryDTO) throws ForbiddenException, FailureException;

    /**
     * 删除文章分类（后台管理）
     *
     * @param cmsCategoryUuid 文章分类UUID
     * @return 删除行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    int deleteCmsCategory(String cmsCategoryUuid) throws ForbiddenException, FailureException;

    /**
     * 根据查询条件获取文章列表（后台管理）
     *
     * @param cmsPostSearchCriteriaVO 查询条件
     * @return 文章列表
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    ListData<CmsPostsDTO> getCmsPostList(CmsPostSearchCriteriaVO cmsPostSearchCriteriaVO) throws ForbiddenException, FailureException;

    /**
     * 添加文章（后台管理）
     *
     * @param cmsPostsDTO 文章传输对象
     * @return 插入行数
     */
    int addCmsPost(CmsPostsDTO cmsPostsDTO) throws ForbiddenException, FailureException;

    /**
     * 修改文章（后台管理）
     *
     * @param cmsPostsDTO 文章传输对象
     * @return 插入行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    int updateCmsPost(CmsPostsDTO cmsPostsDTO) throws ForbiddenException, FailureException;

    /**
     * 删除文章（后台管理）
     *
     * @param uuid 文章UUID
     * @return 删除行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    int deleteCmsPost(String uuid) throws ForbiddenException, FailureException;

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
    ListData<CmsTagDTO> getTagList(String siteUuid, int pages, int rows) throws ForbiddenException, FailureException;

    /**
     * 根据文章UUID获取标签列表
     *
     * @param postUuid 文章UUID
     * @return
     */
    List<CmsTagDTO> getTagListByPostUuid(String postUuid);

    /**
     * 根据UUID获取标签对象
     *
     * @param siteUuid 站点UUID
     * @param uuid     标签对象UUID
     * @return 标签对象
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    CmsTagDTO getTagByUuid(String siteUuid, String uuid) throws ForbiddenException, FailureException;

    /**
     * 添加标签
     *
     * @param cmsTagDTO 标签传输对象
     * @return 插入行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    int addCmsTag(CmsTagDTO cmsTagDTO) throws ForbiddenException, FailureException;

    /**
     * 更新标签
     *
     * @param cmsTagDTO 标签传输对象
     * @return 修改行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    int updateCmsTag(CmsTagDTO cmsTagDTO) throws ForbiddenException, FailureException;

    /**
     * 删除标签
     *
     * @param uuid 标签UUID
     * @return 删除行数
     * @throws ForbiddenException 权限不足异常
     * @throws FailureException   失败异常
     */
    int deleteCmsTag(String uuid) throws ForbiddenException, FailureException;

    /**
     * 获取CMS菜单树
     *
     * @param siteUuid    站点UUID
     * @param cmsMenuEnum 菜单类型
     * @return CMS菜单树
     */
    List<CmsMenuVO> getCmsMenuBySiteUuidAndType(String siteUuid, CmsMenuEnum cmsMenuEnum);

    /**
     * 根据CMS系统的UUID获取菜单对象
     *
     * @param uuid 菜单UUID
     * @return
     */
    CmsMenuVO getCmsMenuByUuid(String uuid);

    /**
     * 添加菜单（CMS系统）
     *
     * @param cmsMenuVO
     * @return
     * @throws ForbiddenException
     * @throws FailureException
     */
    int addCmsMenu(CmsMenuVO cmsMenuVO) throws ForbiddenException, FailureException;

    /**
     * 修改菜单（CMS系统）
     *
     * @param cmsMenuVO
     * @return
     * @throws ForbiddenException
     * @throws FailureException
     */
    int updateCmsMenu(CmsMenuVO cmsMenuVO) throws ForbiddenException, FailureException;

    /**
     * 删除菜单（CMS系统）
     *
     * @param uuid
     * @return
     * @throws ForbiddenException
     */
    int deleteCmsMenu(String uuid) throws ForbiddenException;
}
