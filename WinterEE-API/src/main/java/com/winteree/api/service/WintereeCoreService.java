package com.winteree.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.winteree.api.entity.*;
import com.winteree.api.exception.FailureException;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * WinterEE-Core-Serve提供的服务接口
 *
 * @author RenFei
 */
public interface WintereeCoreService {
    /**
     * 默认降级方法，实现类中可以重写降级方法
     *
     * @return
     */
    default APIResult defaultFallbackMethod() {
        System.err.println("\n核心服务不可达。\nCore Services Not Available.\n");
        return APIResult.builder()
                .code(StateCode.Unavailable)
                .message("\n核心服务不可达。\nCore Services Not Available.\n")
                .build();
    }

    /**
     * i18n国际化语言翻译服务
     *
     * @param language       需要的语言
     * @param message        i18n 代码
     * @param defaultMessage 默认内容
     * @return 当没有找到所需的语言时将返回默认内容
     */
    @GetMapping("/i18n/{language}/{message}/{defaultMessage}")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    String getMessage(@PathVariable("language") String language, @PathVariable("message") String message,
                      @PathVariable("defaultMessage") String defaultMessage);

    //<editor-fold desc="秘钥类的接口" defaultstate="collapsed">

    /**
     * 申请一个非对称秘钥的公钥
     *
     * @return
     */
    @GetMapping("/secretkey")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<String> secretKey();

    /**
     * 上报一个非对称秘钥公钥
     *
     * @return
     */
    @PostMapping("/secretkey")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult setSecretKey(@RequestBody ReportPublicKeyVO reportPublicKeyVO);
    //</editor-fold>

    //<editor-fold desc="账户类的接口" defaultstate="collapsed">

    /**
     * 根据用户名获取账号对象
     *
     * @param username 用户名
     * @return AccountDTO
     */
    @GetMapping("/inside/account/username")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    AccountDTO findAccountByUsername(@RequestParam("username") String username);

    /**
     * 根据UUID获取账号对象
     *
     * @param uuid UUID
     * @return AccountDTO
     */
    @GetMapping("/inside/account/uuid")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    AccountDTO findAccountByUuid(@RequestParam("uuid") String uuid);

    /**
     * 根据邮箱地址获取账号对象
     *
     * @param email 电子邮件
     * @return AccountDTO
     */
    @GetMapping("/inside/account/email")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    AccountDTO findAccountByEmail(@RequestParam("email") String email);

    /**
     * 根据手机号获取账号对象
     *
     * @param phone 手机号
     * @return AccountDTO
     */
    @GetMapping("/inside/account/phone")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    AccountDTO findAccountByPhoneNumber(@RequestParam("phone") String phone);

    /**
     * 创建一个Totp的秘钥
     *
     * @param username 用户名
     * @return AccountDTO
     */
    @GetMapping("/account/totp")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<String> createTotp(@RequestParam("username") String username);

    /**
     * 检查一个用户名是否存在以及类型
     *
     * @param account  用户输入的用户名、邮箱、手机号
     * @param language 返回的消息语言，默认 zh-CN
     * @return
     */
    @GetMapping("/account/check")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<String> checkAccount(@RequestParam("account") String account, @RequestParam("language") String language);

    /**
     * 获取当前登陆账号的信息
     *
     * @return
     */
    @GetMapping("/account/myinfo")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<AccountDTO> getMyInfo();

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param language    语言
     * @param keyid       秘钥ID
     * @return 受影响行数
     * @throws FailureException 失败异常信息
     */
    @PutMapping("/account/mypassword")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult changePassword(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("language") String language,
                             @RequestParam("keyid") String keyid);

    /**
     * 重置任意账户密码
     *
     * @param passwordResetDAT 传输对象
     * @return 受影响行数
     * @throws FailureException 失败异常信息
     */
    @PutMapping("/account/resetpassword")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult passwordReset(@RequestBody PasswordResetDAT passwordResetDAT);

    /**
     * 根据查询条件获取账户列表
     *
     * @param accountSearchCriteriaVO 查询条件
     * @return
     */
    @GetMapping("/account")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<AccountDTO>> getAccountList(@RequestBody AccountSearchCriteriaVO accountSearchCriteriaVO);

    /**
     * 添加用户
     * 密码是在添加用户后，使用密码重置功能进行重置的
     *
     * @param accountDTO 用户信息传输对象
     * @return
     */
    @PostMapping("/account")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addAccount(@RequestBody AccountDTO accountDTO);

    /**
     * 更新账户信息
     *
     * @param accountDTO 用户信息传输对象
     * @return
     */
    @PutMapping("/account")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateAccount(@RequestBody AccountDTO accountDTO);

    /**
     * 发送验证码（内部接口）
     *
     * @param userName       手机或邮箱
     * @param tenantUuid     租户ID
     * @param validationType 验证码类型
     * @return
     */
    @PostMapping("/inside/account/verificationCode")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult sendVerificationCode(@RequestParam("userName") String userName,
                                   @RequestParam("tenantUuid") String tenantUuid,
                                   @RequestParam("validationType") String validationType);

    /**
     * 获取验证码并标记验证码为已使用状态
     *
     * @param userName       手机号/邮箱地址
     * @param validationType 验证码类别
     * @return VerificationCodeDTO
     */
    @GetMapping("/inside/account/verificationCode")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<VerificationCodeDTO> getVerificationCode(@RequestParam("userName") String userName,
                                                       @RequestParam("validationType") String validationType);
    //</editor-fold>

    //<editor-fold desc="菜单类的接口" defaultstate="collapsed">

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @GetMapping("/menu/tree")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<MenuVO>> getMenuTree(@RequestParam(name = "language", required = false) String language);

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @GetMapping("/menu/treeAndAuthority")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<MenuVO>> getMenuAndAuthorityTree(@RequestParam(name = "language", required = false) String language);

    /**
     * 后台菜单管理中的查询菜单列表，注意这个不是登陆以后获取菜单列表
     *
     * @return
     */
    @GetMapping("/setting/menu/tree")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<MenuVO>> getSettingMenuTree();

    @GetMapping("/setting/menu/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<MenuVO>> getSettingMenuList();

    @GetMapping("/setting/menu")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<MenuVO> getSettingMenu(@RequestParam("uuid") String uuid);

    /**
     * @param uuid
     * @return
     */
    @DeleteMapping("/setting/menu")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult deleteSettingMenuByUuid(@RequestParam("uuid") String uuid);

    /**
     * @param menuVO
     * @return
     */
    @PutMapping("/setting/menu")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateSettingMenu(@RequestBody MenuVO menuVO);

    /**
     * @param menuVO
     * @return
     */
    @PostMapping("/setting/menu")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addSettingMenu(@RequestBody MenuVO menuVO);
    //</editor-fold>

    //<editor-fold desc="日志类的接口" defaultstate="collapsed">

    /**
     * 日志记录（服务内部）
     *
     * @param logDTO 日志实体
     * @return
     */
    @PostMapping("/inside/log")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult log(@RequestBody LogDTO logDTO);

    /**
     * 获取系统日志
     *
     * @param page      页数
     * @param rows      每页行数
     * @param logType   每页行数
     * @param subType   每页行数
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    @GetMapping("/setting/logs")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<LogDTO>> getLogList(@RequestParam("page") int page,
                                       @RequestParam("rows") int rows,
                                       @RequestParam(name = "logType", required = false) String logType,
                                       @RequestParam(name = "subType", required = false) String subType,
                                       @RequestParam(name = "startDate", required = false) String startDate,
                                       @RequestParam(name = "endDate", required = false) String endDate);

    /**
     * 获取日志类型
     *
     * @return
     */
    @GetMapping("/setting/logs/type")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<Map<String, String>>> getAllLogType(@RequestParam(name = "lang", required = false) String lang);

    /**
     * 获取日志子类型
     *
     * @return
     */
    @GetMapping("/setting/logs/subtype")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<Map<String, String>>> getAllLogSubType(@RequestParam(name = "lang", required = false) String lang);
    //</editor-fold>

    //<editor-fold desc="租户类的接口" defaultstate="collapsed">

    /**
     * 获取所有租户列表，需要自己管理权限
     *
     * @return
     */
    @GetMapping("/setting/tenantlist")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<TenantDTO>> getTenantList();

    /**
     * 获取所有租户列表
     *
     * @param page 页数
     * @param rows 每页行数
     * @return
     */
    @GetMapping("/setting/tenant")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<TenantDTO>> getAllTenant(@RequestParam("page") int page, @RequestParam("rows") int rows);

    /**
     * 新增租户
     *
     * @param tenantDTO 租户实体
     * @return
     */
    @PostMapping("/setting/tenant")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addTenant(@RequestBody TenantDTO tenantDTO);

    /**
     * 修改租户
     *
     * @param tenantDTO
     * @return
     */
    @PutMapping("/setting/tenant")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateTenant(@RequestBody TenantDTO tenantDTO);

    /**
     * 根据租户ID获取基础信息（开放服务，无需身份校验）
     *
     * @param tenantUUID
     * @return
     */
    @GetMapping("/tenant/info")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<TenantInfoDTO> getTenantInfo(@RequestParam("tenantUUID") String tenantUUID);

    /**
     * 修改租户基础信息
     *
     * @param tenantInfoDTO 租户基础信息
     * @return
     */
    @PostMapping("/tenant/info")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateTenantInfo(@RequestBody TenantInfoDTO tenantInfoDTO);
    //</editor-fold>

    //<editor-fold desc="OAtuh类的接口" defaultstate="collapsed">

    /**
     * 获取所有OAtuh客户端列表
     *
     * @param page 页数
     * @param rows 容量
     * @return
     */
    @GetMapping("/setting/oauthclient")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<OAuthClientDTO>> getOAuthClientAllList(@RequestParam("page") int page, @RequestParam("rows") int rows);

    /**
     * 添加OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    @PostMapping("/setting/oauthclient")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addOAuthClient(@RequestBody OAuthClientDTO oAuthClientDTO);

    /**
     * 修改OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    @PutMapping("/setting/oauthclient")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateOAuthClient(@RequestBody OAuthClientDTO oAuthClientDTO);

    /**
     * 删除OAtuh客户端
     *
     * @param clientId clientId
     * @return
     */
    @DeleteMapping("/setting/oauthclient")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult deleteOAuthClient(@RequestParam("clientId") String clientId);
    //</editor-fold>

    //<editor-fold desc="组织机构类的接口" defaultstate="collapsed">

    /**
     * 获取整个组织架构的树
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @GetMapping("/organization")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult getAllOrganizationTree(@RequestParam("tenantUuid") String tenantUuid);

    /**
     * 获取公司列表
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @GetMapping("/organization/company")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult getCompanyList(@RequestParam("tenantUuid") String tenantUuid);

    /**
     * 获取公司列表（简单列表非树状）
     *
     * @param tenantUuid
     * @return
     */
    @GetMapping("/organization/company/simpleList")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult getCompanySimpleList(@RequestParam("tenantUuid") String tenantUuid);

    /**
     * 获取公司列表
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @GetMapping("/organization/myCompany")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult getMyCompanyList(@RequestParam("tenantUuid") String tenantUuid);

    /**
     * 添加新增公司
     *
     * @param organizationVO
     * @return
     */
    @PostMapping("/organization/company")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addCompany(@RequestBody OrganizationVO organizationVO);

    /**
     * 更新公司信息
     *
     * @param organizationVO
     * @return
     */
    @PutMapping("/organization/company")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateCompany(@RequestBody OrganizationVO organizationVO);

    /**
     * 获取部门列表（树状）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    @GetMapping("/organization/department")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult getDepartmentList(@RequestParam(name = "tenantUuid", required = false) String tenantUuid,
                                @RequestParam(name = "companyUuid", required = false) String companyUuid);

    /**
     * 获取部门列表（简单列表非树状）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    @GetMapping("/organization/department/simpleList")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult getDepartmentSimpleList(@RequestParam(name = "tenantUuid", required = false) String tenantUuid,
                                      @RequestParam(name = "companyUuid", required = false) String companyUuid);

    /**
     * 添加部门
     *
     * @param organizationVO 部门对象
     * @return
     */
    @PostMapping("/organization/department")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addDepartment(@RequestBody OrganizationVO organizationVO);

    /**
     * 添加部门
     *
     * @param organizationVO 部门对象
     * @return
     */
    @PutMapping("/organization/department")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateDepartment(@RequestBody OrganizationVO organizationVO);
    //</editor-fold>

    //<editor-fold desc="角色类的接口" defaultstate="collapsed">

    /**
     * 获取角色列表
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @GetMapping("/role/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult getRoleList(@RequestParam("tenantUuid") String tenantUuid);

    /**
     * 添加角色
     *
     * @param roleDTO 角色传输类
     * @return
     */
    @PostMapping("/role/data")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addRole(@RequestBody RoleDTO roleDTO);

    /**
     * 更新角色
     *
     * @param roleDTO 角色传输类
     * @return
     */
    @PutMapping("/role/data")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateRole(@RequestBody RoleDTO roleDTO);

    /**
     * 删除角色
     *
     * @param uuid 角色ID
     * @return
     */
    @DeleteMapping("/role/data")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult deleteRole(@RequestParam("uuid") String uuid);
    //</editor-fold>

    //<editor-fold desc="CMS类的接口" defaultstate="collapsed">

    /**
     * 获取CMS站点列表（后台管理）
     *
     * @param tenantUuid 租户ID
     * @param page       页码
     * @param rows       行数
     * @return 站点列表
     */
    @GetMapping("/cms/site/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<CmsSiteDTO>> getCmsSiteList(@RequestParam(name = "tenantUuid") String tenantUuid,
                                                   @RequestParam(name = "page") int page,
                                                   @RequestParam(name = "rows") int rows);

    /**
     * 根据UUID获取CMS系统站点（后台管理）
     *
     * @param uuid UUID
     * @return 站点传输对象
     */
    @GetMapping("/cms/site")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<CmsSiteDTO> getCmsSiteByUuid(@RequestParam("uuid") String uuid);

    /**
     * 根据域名获取站点信息
     *
     * @param domain 域名
     * @return
     */
    @GetMapping("/cms/site/domain")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<CmsSiteDTO> getCmsSiteByDomain(@RequestParam("domain") String domain);

    /**
     * 添加站点（后台管理）
     *
     * @param cmsSiteDTO 站点信息传输对象
     * @return
     */
    @PostMapping("/cms/site")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addCmsSite(@RequestBody CmsSiteDTO cmsSiteDTO);

    /**
     * 更新站点（后台管理）
     *
     * @param cmsSiteDTO 站点信息传输对象
     * @return
     */
    @PutMapping("/cms/site")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateCmsSite(@RequestBody CmsSiteDTO cmsSiteDTO);

    /**
     * 删除站点所有内容（后台管理）
     *
     * @param uuid 站点ID
     * @return
     */
    @DeleteMapping("/cms/site")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult deleteCmsSite(@RequestParam("uuid") String uuid);

    /**
     * 获取CMS系统分类列表（后台管理）
     *
     * @param siteUuid 站点ID
     * @return 文章分类列表
     */
    @GetMapping("/cms/category/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<CmsCategoryDTO>> getCmsCategoryList(@RequestParam("siteUuid") String siteUuid);

    /**
     * 根据UUID获取CMS系统站点下的分类
     *
     * @param uuid 分类UUID
     * @return 分类对象
     */
    @GetMapping("/cms/category")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<CmsCategoryDTO> getCmsCategoryByUuid(@RequestParam("uuid") String uuid);

    /**
     * 添加文章分类（后台管理）
     *
     * @param cmsCategoryDTO 文章分类传输对象
     * @return
     */
    @PostMapping("/cms/category")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addCmsCategory(@RequestBody CmsCategoryDTO cmsCategoryDTO);

    /**
     * 更新文章分类（后台管理）
     *
     * @param cmsCategoryDTO 文章分类传输对象
     * @return
     */
    @PutMapping("/cms/category")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateCmsCategory(@RequestBody CmsCategoryDTO cmsCategoryDTO);

    /**
     * 删除文章分类（后台管理）
     *
     * @param cmsCategoryUuid 文章分类UUID
     * @return
     */
    @DeleteMapping("/cms/category")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult deleteCmsCategory(@RequestParam("cmsCategoryUuid") String cmsCategoryUuid);

    /**
     * 根据查询条件获取文章列表（后台管理）
     *
     * @param cmsPostSearchCriteriaVO 查询条件
     * @return 文章列表
     */
    @GetMapping("/cms/posts/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<CmsPostsDTO>> getCmsPostList(@RequestBody CmsPostSearchCriteriaVO cmsPostSearchCriteriaVO);

    /**
     * 根据栏目ID获取文章列表（前台）
     *
     * @param categoryUuid 栏目UUID
     * @param pages        页码
     * @param rows         每页行数
     * @return 文章列表
     */
    @GetMapping("/cms/posts/bycategory/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<CmsPostsDTO>> getCmsPostListByCategory(@RequestParam("categoryUuid") String categoryUuid,
                                                              @RequestParam("pages") int pages,
                                                              @RequestParam("rows") int rows);

    /**
     * 获取相关文章
     *
     * @param uuid   文章UUID
     * @param number 获取的数量
     * @return 相关文章列表
     */
    @GetMapping("/cms/posts/related/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<CmsPostsDTO>> getRelatedPostList(@RequestParam("uuid") String uuid,
                                                        @RequestParam("number") int number);

    @GetMapping("/cms/posts/hot/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<CmsPostsDTO>> getHotPostList(@RequestParam("siteUuid") String siteUuid,
                                                    @RequestParam("number") int number);

    @GetMapping("/cms/posts/hot/year")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<CmsPostsDTO>> getHotPostListByYear(@RequestParam("siteUuid") String siteUuid,
                                                          @RequestParam("number") int number);

    @GetMapping("/cms/posts/hot/quarter")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<CmsPostsDTO>> getHotPostListByQuarter(@RequestParam("siteUuid") String siteUuid,
                                                             @RequestParam("number") int number);

    /**
     * 根据文章ID获取文章详情并更新浏览量
     *
     * @param uuid 文章UUID
     * @return
     */
    @GetMapping("/cms/PostByUuid")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<CmsPostsDTO> getCmsPostByUuid(@RequestParam("uuid") String uuid);

    /**
     * 根据文章ID获取文章详情并更新浏览量
     *
     * @param uuid       文章UUID
     * @param updateView 是否更新浏览量
     * @return
     */
    @GetMapping("/cms/posts/{uuid}")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<CmsPostsDTO> getCmsPostByUuid(@PathVariable("uuid") String uuid,
                                            @RequestParam("updateView") Boolean updateView);

    /**
     * 根据文章ID获取文章详情
     *
     * @param id         文章主键ID
     * @param updateView 是否更新浏览量
     * @return
     */
    @GetMapping("/cms/PostByLongId")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<CmsPostsDTO> getCmsPostByLongId(@RequestParam("id") Long id,
                                              @RequestParam("updateView") Boolean updateView);

    /**
     * 点赞
     *
     * @param uuid 文章UUID
     * @return
     */
    @GetMapping("/cms/posts/thumbsUp")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult thumbsUpPost(@RequestParam("uuid") String uuid);

    /**
     * 点踩
     *
     * @param uuid 文章UUID
     * @return
     */
    @GetMapping("/cms/posts/thumbsDown")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult thumbsDownPost(@RequestParam("uuid") String uuid);


    /**
     * 添加文章（后台管理）
     *
     * @param cmsPostsDTO 文章传输对象
     * @return
     */
    @PostMapping("/cms/posts")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addCmsPost(@RequestBody CmsPostsDTO cmsPostsDTO);

    /**
     * 修改文章（后台管理）
     *
     * @param cmsPostsDTO 文章传输对象
     * @return
     */
    @PutMapping("/cms/posts")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateCmsPost(@RequestBody CmsPostsDTO cmsPostsDTO);

    /**
     * 删除文章（后台管理）
     *
     * @param uuid 文章UUID
     * @return
     */
    @DeleteMapping("/cms/posts")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult deleteCmsPost(@RequestParam("uuid") String uuid);

    /**
     * 获取标签列表（后台管理）
     *
     * @param siteUuid 站点UUID
     * @param pages    页码
     * @param rows     行数
     * @return 标签列表
     */
    @GetMapping("/cms/tag/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<CmsTagDTO>> getTagList(@RequestParam(name = "siteUuid") String siteUuid,
                                              @RequestParam(name = "pages") int pages,
                                              @RequestParam(name = "rows") int rows);

    /**
     * 获取所有标签列表以及文章数量（前台）
     *
     * @param siteUuid 站点ID
     * @return
     */
    @GetMapping("/cms/tag/listAndCount")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<CmsTagDTO>> getAllTagAndCount(String siteUuid);

    /**
     * 根据UUID获取标签对象
     *
     * @param siteUuid 站点UUID
     * @param uuid     标签对象UUID
     * @return 标签对象
     */
    @GetMapping("/cms/tag")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<CmsTagDTO> getTagByUuid(@RequestParam(name = "siteUuid") String siteUuid,
                                      @RequestParam(name = "uuid") String uuid);

    /**
     * 添加标签
     *
     * @param cmsTagDTO 标签传输对象
     * @return
     */
    @PostMapping("/cms/tag")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addCmsTag(@RequestBody CmsTagDTO cmsTagDTO);

    /**
     * 更新标签
     *
     * @param cmsTagDTO 标签传输对象
     * @return
     */
    @PutMapping("/cms/tag")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateCmsTag(@RequestBody CmsTagDTO cmsTagDTO);

    /**
     * 删除标签
     *
     * @param uuid 标签UUID
     * @return
     */
    @DeleteMapping("/cms/tag")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult deleteCmsTag(@RequestParam("uuid") String uuid);

    /**
     * 获取CMS菜单树
     *
     * @param siteUuid 站点UUID
     * @param menuType 菜单类型
     * @return
     */
    @GetMapping("/cms/menu/tree")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<CmsMenuVO>> getCmsMenuBySiteUuidAndType(@RequestParam("siteUuid") String siteUuid,
                                                           @RequestParam("menuType") int menuType);

    /**
     * 根据CMS系统的UUID获取菜单对象
     *
     * @param uuid 菜单UUID
     * @return
     */
    @GetMapping("/cms/menu")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<CmsMenuVO> getCmsMenuByUuid(@RequestParam("uuid") String uuid);

    /**
     * 添加菜单（CMS系统）
     *
     * @param cmsMenuVO
     * @return
     */
    @PostMapping("/cms/menu")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult addCmsMenu(@RequestBody CmsMenuVO cmsMenuVO);

    /**
     * 修改菜单（CMS系统）
     *
     * @param cmsMenuVO
     * @return
     */
    @PutMapping("/cms/menu")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult updateCmsMenu(@RequestBody CmsMenuVO cmsMenuVO);

    /**
     * 删除菜单（CMS系统）
     *
     * @param uuid
     * @return
     */
    @DeleteMapping("/cms/menu")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult deleteCmsMenu(@RequestParam("uuid") String uuid);
    //</editor-fold>

    @PostMapping("/uploadPublicFile")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<String> uploadPublicFile(MultipartFile file);

    @PostMapping("/uploadPrivateFile")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<String> uploadPrivateFile(MultipartFile file);

    //<editor-fold desc="定时任务类的接口" defaultstate="collapsed">

    @GetMapping("/task/list")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<ListData<TaskJobDTO>> getTaskList(@RequestParam("pages") int pages, @RequestParam("rows") int rows);

    @PostMapping("/task/job")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult saveTask(@RequestBody TaskJobDTO taskJobDTO);

    @PostMapping("/task/job/trigger")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult triggerJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup);

    @PutMapping("/task/job/pause")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult pauseJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup);

    @PutMapping("/task/job/resume")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult resumeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup);

    @DeleteMapping("/task/job")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult removeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup);

    @PutMapping("/task/job")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult modifyJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroup") String jobGroup, @RequestParam("time") String time);
    //</editor-fold>

    //<editor-fold desc="行政区划类的接口" defaultstate="collapsed">

    /**
     * 根据行政代码查询行政区划数据
     *
     * @param code 行政代码
     * @return RegionDTO
     */
    @GetMapping("/region")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<RegionDTO> getRegionByCode(@RequestParam("code") String code);

    /**
     * 获取子级行政区划列表
     *
     * @param code 本级行政代码，为空时查询顶级行政区划
     * @return List<RegionDTO>
     */
    @GetMapping("/region/child")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<List<RegionDTO>> getChildRegion(@RequestParam("code") String code);
    //</editor-fold>

    @GetMapping("util/ipinfo/{ip}")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult<IpInfoDTO> queryIpInfo(@PathVariable("ip") String ip);
}
