package com.winteree.api.service;

import com.winteree.api.entity.*;
import com.winteree.api.exception.FailureException;
import net.renfei.sdk.entity.APIResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * WinterEE-Core-Serve提供的服务接口
 *
 * @author RenFei
 */
public interface WintereeCoreService {
    /**
     * i18n国际化语言翻译服务
     *
     * @param language       需要的语言
     * @param message        i18n 代码
     * @param defaultMessage 默认内容
     * @return 当没有找到所需的语言时将返回默认内容
     */
    @GetMapping("/i18n/{language}/{message}/{defaultMessage}")
    String getMessage(@PathVariable("language") String language, @PathVariable("message") String message, @PathVariable("defaultMessage") String defaultMessage);

    //<editor-fold desc="秘钥类的接口" defaultstate="collapsed">

    /**
     * 申请一个非对称秘钥的公钥
     *
     * @return
     */
    @GetMapping("/secretkey")
    APIResult<String> secretKey();

    /**
     * 上报一个非对称秘钥公钥
     *
     * @return
     */
    @PostMapping("/secretkey")
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
    AccountDTO findAccountByUsername(String username);

    /**
     * 根据邮箱地址获取账号对象
     *
     * @param email 电子邮件
     * @return AccountDTO
     */
    @GetMapping("/inside/account/email")
    AccountDTO findAccountByEmail(String email);

    /**
     * 根据手机号获取账号对象
     *
     * @param phone 手机号
     * @return AccountDTO
     */
    @GetMapping("/inside/account/phone")
    AccountDTO findAccountByPhoneNumber(String phone);

    /**
     * 创建一个Totp的秘钥
     *
     * @param username 用户名
     * @return AccountDTO
     */
    @GetMapping("/account/totp")
    APIResult<String> createTotp(String username);

    /**
     * 检查一个用户名是否存在以及类型
     *
     * @param account  用户输入的用户名、邮箱、手机号
     * @param language 返回的消息语言，默认 zh-CN
     * @return
     */
    @GetMapping("/account/check")
    APIResult<String> checkAccount(@RequestParam("account") String account, @RequestParam("language") String language);

    /**
     * 获取当前登陆账号的信息
     *
     * @return
     */
    @GetMapping("/account/myinfo")
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
    APIResult changePassword(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("language") String language,
                             @RequestParam("keyid") String keyid);

    /**
     * 重置任意账户密码
     *
     * @param accountUuid 账户ID
     * @param newPassword 新密码
     * @param language    语言
     * @param keyid       秘钥ID
     * @return 受影响行数
     * @throws FailureException 失败异常信息
     */
    @PutMapping("/account/resetpassword")
    APIResult passwordReset(@RequestBody String accountUuid,
                            @RequestBody String newPassword,
                            @RequestBody String language,
                            @RequestBody String keyid);

    /**
     * 根据查询条件获取账户列表
     *
     * @param accountSearchCriteriaVO 查询条件
     * @return
     */
    @GetMapping("/account")
    APIResult<ListData<AccountDTO>> getAccountList(AccountSearchCriteriaVO accountSearchCriteriaVO);

    /**
     * 添加用户
     * 密码是在添加用户后，使用密码重置功能进行重置的
     *
     * @param accountDTO 用户信息传输对象
     * @return
     */
    @PostMapping("/account")
    APIResult addAccount(AccountDTO accountDTO);

    /**
     * 更新账户信息
     *
     * @param accountDTO 用户信息传输对象
     * @return
     */
    @PutMapping("/account")
    APIResult updateAccount(AccountDTO accountDTO);
    //</editor-fold>

    //<editor-fold desc="菜单类的接口" defaultstate="collapsed">

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @GetMapping("/menu/tree")
    APIResult<List<MenuVO>> getMenuTree(@RequestParam(name = "language", required = false) String language);

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @GetMapping("/menu/treeAndAuthority")
    APIResult<List<MenuVO>> getMenuAndAuthorityTree(@RequestParam(name = "language", required = false) String language);

    /**
     * 后台菜单管理中的查询菜单列表，注意这个不是登陆以后获取菜单列表
     *
     * @return
     */
    @GetMapping("/setting/menu/tree")
    APIResult<List<MenuVO>> getSettingMenuTree();

    @GetMapping("/setting/menu/list")
    APIResult<List<MenuVO>> getSettingMenuList();

    @GetMapping("/setting/menu")
    APIResult<MenuVO> getSettingMenu(String uuid);

    /**
     * @param uuid
     * @return
     */
    @DeleteMapping("/setting/menu")
    APIResult deleteSettingMenuByUuid(String uuid);

    /**
     * @param menuVO
     * @return
     */
    @PutMapping("/setting/menu")
    APIResult updateSettingMenu(MenuVO menuVO);

    /**
     * @param menuVO
     * @return
     */
    @PostMapping("/setting/menu")
    APIResult addSettingMenu(MenuVO menuVO);
    //</editor-fold>

    //<editor-fold desc="日志类的接口" defaultstate="collapsed">

    /**
     * 日志记录（服务内部）
     *
     * @param logDTO 日志实体
     * @return
     */
    @PostMapping("/inside/log")
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
    APIResult<List<Map<String, String>>> getAllLogType(@RequestParam(name = "lang", required = false) String lang);

    /**
     * 获取日志子类型
     *
     * @return
     */
    @GetMapping("/setting/logs/subtype")
    APIResult<List<Map<String, String>>> getAllLogSubType(@RequestParam(name = "lang", required = false) String lang);
    //</editor-fold>

    //<editor-fold desc="租户类的接口" defaultstate="collapsed">

    /**
     * 获取所有租户列表，需要自己管理权限
     *
     * @return
     */
    @GetMapping("/setting/tenantlist")
    APIResult<ListData<TenantDTO>> getTenantList();

    /**
     * 获取所有租户列表
     *
     * @param page 页数
     * @param rows 每页行数
     * @return
     */
    @GetMapping("/setting/tenant")
    APIResult<ListData<TenantDTO>> getAllTenant(@RequestParam("page") int page, @RequestParam("rows") int rows);

    /**
     * 新增租户
     *
     * @param tenantDTO 租户实体
     * @return
     */
    @PostMapping("/setting/tenant")
    APIResult addTenant(TenantDTO tenantDTO);

    /**
     * 修改租户
     *
     * @param tenantDTO
     * @return
     */
    @PutMapping("/setting/tenant")
    APIResult updateTenant(TenantDTO tenantDTO);

    /**
     * 根据租户ID获取基础信息（开放服务，无需身份校验）
     *
     * @param tenantUUID
     * @return
     */
    @GetMapping("/tenant/info")
    APIResult<TenantInfoDTO> getTenantInfo(String tenantUUID);

    /**
     * 修改租户基础信息
     *
     * @param tenantInfoDTO 租户基础信息
     * @return
     */
    @PostMapping("/tenant/info")
    APIResult updateTenantInfo(TenantInfoDTO tenantInfoDTO);
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
    APIResult<ListData<OAuthClientDTO>> getOAuthClientAllList(@RequestParam("page") int page, @RequestParam("rows") int rows);

    /**
     * 添加OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    @PostMapping("/setting/oauthclient")
    APIResult addOAuthClient(OAuthClientDTO oAuthClientDTO);

    /**
     * 修改OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    @PutMapping("/setting/oauthclient")
    APIResult updateOAuthClient(OAuthClientDTO oAuthClientDTO);

    /**
     * 删除OAtuh客户端
     *
     * @param clientId clientId
     * @return
     */
    @DeleteMapping("/setting/oauthclient")
    APIResult deleteOAuthClient(String clientId);
    //</editor-fold>

    //<editor-fold desc="组织机构类的接口" defaultstate="collapsed">

    /**
     * 获取整个组织架构的树
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @GetMapping("/organization")
    APIResult getAllOrganizationTree(String tenantUuid);

    /**
     * 获取公司列表
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @GetMapping("/organization/company")
    APIResult getCompanyList(String tenantUuid);

    /**
     * 获取公司列表（简单列表非树状）
     *
     * @param tenantUuid
     * @return
     */
    @GetMapping("/organization/company/simpleList")
    APIResult getCompanySimpleList(String tenantUuid);

    /**
     * 获取公司列表
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @GetMapping("/organization/myCompany")
    APIResult getMyCompanyList(String tenantUuid);

    /**
     * 添加新增公司
     *
     * @param organizationVO
     * @return
     */
    @PostMapping("/organization/company")
    APIResult addCompany(OrganizationVO organizationVO);

    /**
     * 更新公司信息
     *
     * @param organizationVO
     * @return
     */
    @PutMapping("/organization/company")
    APIResult updateCompany(OrganizationVO organizationVO);

    /**
     * 获取部门列表（树状）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    @GetMapping("/organization/department")
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
    APIResult getDepartmentSimpleList(@RequestParam(name = "tenantUuid", required = false) String tenantUuid,
                                      @RequestParam(name = "companyUuid", required = false) String companyUuid);

    /**
     * 添加部门
     *
     * @param organizationVO 部门对象
     * @return
     */
    @PostMapping("/organization/department")
    APIResult addDepartment(OrganizationVO organizationVO);

    /**
     * 添加部门
     *
     * @param organizationVO 部门对象
     * @return
     */
    @PutMapping("/organization/department")
    APIResult updateDepartment(OrganizationVO organizationVO);
    //</editor-fold>

    //<editor-fold desc="角色类的接口" defaultstate="collapsed">

    /**
     * 获取角色列表
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @GetMapping("/role/list")
    APIResult getRoleList(String tenantUuid);

    /**
     * 添加角色
     *
     * @param roleDTO 角色传输类
     * @return
     */
    @PostMapping("/role/data")
    APIResult addRole(RoleDTO roleDTO);

    /**
     * 更新角色
     *
     * @param roleDTO 角色传输类
     * @return
     */
    @PutMapping("/role/data")
    APIResult updateRole(RoleDTO roleDTO);

    /**
     * 删除角色
     *
     * @param uuid 角色ID
     * @return
     */
    @DeleteMapping("/role/data")
    APIResult deleteRole(String uuid);
    //</editor-fold>
}
