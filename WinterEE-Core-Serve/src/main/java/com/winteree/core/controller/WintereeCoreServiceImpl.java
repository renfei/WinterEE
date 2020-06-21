package com.winteree.core.controller;

import com.winteree.api.entity.*;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
import com.winteree.api.service.WintereeCoreService;
import com.winteree.core.aop.OperationLog;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.GoogleAuthenticator;
import net.renfei.sdk.utils.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WinterEE-Core-Serve 提供的服务实现
 *
 * @author RenFei
 */
@RestController
public class WintereeCoreServiceImpl extends BaseController implements WintereeCoreService {
    //<editor-fold desc="依赖服务" defaultstate="collapsed">
    private final I18nMessageService i18nMessageService;
    private final WintereeCoreConfig wintereeCoreConfig;
    private final AccountService accountService;
    private final SecretKeyService secretKeyService;
    private final LogService logService;
    private final MenuService menuService;
    private final TenantService tenantService;
    private final OAuthClientService oAuthClientService;
    private final OrganizationService organizationService;
    private final RoleService roleService;
    //</editor-fold>

    //<editor-fold desc="构造函数" defaultstate="collapsed">
    public WintereeCoreServiceImpl(I18nMessageService i18nMessageService,
                                   WintereeCoreConfig wintereeCoreConfig,
                                   AccountService accountService,
                                   SecretKeyService secretKeyService,
                                   LogService logService,
                                   MenuService menuService,
                                   TenantService tenantService,
                                   OAuthClientService oAuthClientService,
                                   OrganizationService organizationService,
                                   RoleService roleService) {
        this.i18nMessageService = i18nMessageService;
        this.wintereeCoreConfig = wintereeCoreConfig;
        this.accountService = accountService;
        this.secretKeyService = secretKeyService;
        this.logService = logService;
        this.menuService = menuService;
        this.tenantService = tenantService;
        this.oAuthClientService = oAuthClientService;
        this.organizationService = organizationService;
        this.roleService = roleService;
    }
    //</editor-fold>

    //<editor-fold desc="i18n国际化接口" defaultstate="collapsed">
    @Override
    @ApiOperation(value = "i18n国际化接口", notes = "将国际化标签翻译为指定的语言", tags = "国际化（i18n）", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "language", value = "指定的语言", required = false, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "message", value = "国际化标签", required = false, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "defaultMessage", value = "翻译失败时默认返回", required = false, paramType = "path", dataType = "String")
    })
    public String getMessage(String language, String message, String defaultMessage) {
        return i18nMessageService.getMessage(language, message, defaultMessage);
    }
    //</editor-fold>

    //<editor-fold desc="秘钥类的接口" defaultstate="collapsed">
    @Override
    @ApiIgnore
    public APIResult log(LogDTO logDTO) {
        try {
            if (logService.log(logDTO) > 0) {
                return APIResult.success();
            } else {
                return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
            }
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }

    @Override
    @ApiOperation(value = "获取服务器端公钥接口", notes = "服务器端会为你生成一个服务器端的RSA(2048)公钥", tags = "秘钥交换", response = APIResult.class)
    public APIResult<String> secretKey() {
        Map<Integer, String> map = secretKeyService.secretKey();
        if (BeanUtils.isEmpty(map)) {
            return APIResult.builder()
                    .code(StateCode.Error)
                    .data("")
                    .build();
        }
        return APIResult.builder()
                .code(StateCode.OK)
                .message(map.get(1))
                .data(map.get(0))
                .build();
    }

    @Override
    @ApiOperation(value = "上报客户端公钥接口", notes = "客户端生成RSA(1024)秘钥对，使用客户端RSA(2048)公钥加密客户端的RSA(1024)公钥，上报给服务器", tags = "秘钥交换", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportPublicKeyVO", value = "ReportPublicKeyVO对象", required = false, paramType = "body", dataType = "ReportPublicKeyVO")
    })
    public APIResult setSecretKey(ReportPublicKeyVO reportPublicKeyVO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(secretKeyService.setSecretKey(reportPublicKeyVO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }
    //</editor-fold>

    //<editor-fold desc="账户类的接口" defaultstate="collapsed">
    @Override
    @ApiIgnore
    public AccountDTO findAccountByUsername(String username) {
        return accountService.getAccountIdByUserName(username);
    }

    @Override
    @ApiIgnore
    public AccountDTO findAccountByEmail(String email) {
        return accountService.getAccountIdByEmail(email);
    }

    @Override
    @ApiIgnore
    public AccountDTO findAccountByPhoneNumber(String phone) {
        return accountService.getAccountIdByPhone(phone);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('signed')")
    @ApiOperation(value = "创建TOTP", notes = "为账户创建一个TOTP秘钥", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<String> createTotp(String username) {
        String secret = GoogleAuthenticator.generateSecretKey(wintereeCoreConfig.getTotpseed());
        return APIResult.builder()
                .code(StateCode.OK)
                .data(GoogleAuthenticator.genTotpString(wintereeCoreConfig.getSystemname(), username, secret))
                .build();
    }

    @Override
    @ApiOperation(value = "检查用户账户是否存在和类型", notes = "检查用户输入的用户名是否存在，以及类型", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户输入", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "language", value = "语言，默认 zh-CN", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<String> checkAccount(String account, String language) {
        language = language == null ? "zh-CN" : language;
        String type;
        AccountDTO accountDTO;
        if (BeanUtils.isEmpty(account)) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(i18nMessageService.getMessage(language, "core.cantfindyouraccount", "找不到您的账户"))
                    .data(i18nMessageService.getMessage(language, "core.cantfindyouraccount", "找不到您的账户"))
                    .build();
        }
        if (StringUtils.isEmail(account)) {
            type = "Email";
            accountDTO = accountService.getAccountIdByEmail(account);
        } else if (StringUtils.isChinaPhone(account)) {
            type = "Phone";
            accountDTO = accountService.getAccountIdByPhone(account);
        } else {
            type = "UserName";
            accountDTO = accountService.getAccountIdByUserName(account);
        }
        if (accountDTO == null) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(i18nMessageService.getMessage(language, "core.cantfindyouraccount", "找不到您的账户"))
                    .data(i18nMessageService.getMessage(language, "core.cantfindyouraccount", "找不到您的账户"))
                    .build();
        }
        return APIResult.builder()
                .code(StateCode.OK)
                .message(type)
                .data(type)
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('signed')")
    public APIResult<AccountDTO> getMyInfo() {
        return APIResult.builder().code(StateCode.OK).message("OK").data(accountService.getAccountInfo()).build();
    }
    //</editor-fold>

    //<editor-fold desc="菜单类的接口" defaultstate="collapsed">

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('signed')")
    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表，注意不是菜单管理中的查询菜单列表", tags = "菜单接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "language", value = "语言，默认 zh-CN", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<List<MenuVO>> getMenuTree(String language) {
        return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.getMenuListBySignedUser(language)).build();
    }

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('signed')")
    @ApiOperation(value = "获取菜单和权限列表", notes = "获取菜单和权限列表，注意不是菜单管理中的查询菜单列表", tags = "菜单接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "language", value = "语言，默认 zh-CN", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<List<MenuVO>> getMenuAndAuthorityTree(String language) {
        return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.getMenuAndAuthorityListBySignedUser(language)).build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:view')")
    @OperationLog(description = "获取系统菜单列表", type = LogSubTypeEnum.SELECT)
    @ApiOperation(value = "获取后台设置菜单接口", notes = "获取后台设置菜单接口，后台管理使用的", tags = "菜单接口", response = APIResult.class)
    public APIResult<List<MenuVO>> getSettingMenuTree() {
        return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.getAllMenuTree()).build();
    }

    @Override
    public APIResult<List<MenuVO>> getSettingMenuList() {
        return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.getAllMenuList()).build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:view')")
    @OperationLog(description = "获取系统菜单详情", type = LogSubTypeEnum.SELECT)
    @ApiOperation(value = "获取后台设置菜单详情接口", notes = "获取后台设置菜单详情接口，后台管理使用的", tags = "菜单接口", response = APIResult.class)
    public APIResult<MenuVO> getSettingMenu(String uuid) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.getMenuByUuid(uuid)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:delete')")
    @OperationLog(description = "删除系统菜单", type = LogSubTypeEnum.DELETE)
    @ApiOperation(value = "删除系统菜单", notes = "物理删除系统菜单，不可恢复！！", tags = "菜单接口", response = APIResult.class)
    public APIResult deleteSettingMenuByUuid(String uuid) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.deleteMenuByUuid(uuid)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:update')")
    @OperationLog(description = "修改系统菜单", type = LogSubTypeEnum.UPDATE)
    @ApiOperation(value = "修改系统菜单", notes = "修改更新系统菜单", tags = "菜单接口", response = APIResult.class)
    public APIResult updateSettingMenu(@RequestBody MenuVO menuVO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.updateMenu(menuVO)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:add')")
    @OperationLog(description = "添加系统菜单", type = LogSubTypeEnum.INSERT)
    @ApiOperation(value = "添加系统菜单", notes = "添加系统菜单", tags = "菜单接口", response = APIResult.class)
    public APIResult addSettingMenu(@RequestBody MenuVO menuVO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.addMenu(menuVO)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }
    //</editor-fold>

    //<editor-fold desc="日志类的接口" defaultstate="collapsed">
    @Override
    @PreAuthorize("hasAnyAuthority('platf:log:view')")
    @OperationLog(description = "获取系统日志", type = LogSubTypeEnum.SELECT)
    @ApiOperation(value = "获取系统日志", notes = "获取系统日志", tags = "日志接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query", dataType = "Int"),
            @ApiImplicitParam(name = "rows", value = "每页行数", required = true, paramType = "query", dataType = "Int"),
            @ApiImplicitParam(name = "logType", value = "日志类型", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "subType", value = "日志子类型", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", required = false, paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = false, paramType = "query", dataType = "Date")
    })
    public APIResult<List<LogDTO>> getLogList(int page, int rows, String logType, String subType, String startDate, String endDate) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(logService.getLogList(page, rows, logType, subType, startDate, endDate)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }

    @Override
    public APIResult<List<Map<String, String>>> getAllLogType(String lang) {
        List<Map<String, String>> logTypes = new ArrayList<>();
        Map<String, String> map = getLogTypeMap(lang);
        logTypes.add(map);
        for (LogTypeEnum logType : LogTypeEnum.values()
        ) {
            map = new HashMap<>();
            map.put("text", i18nMessageService.getMessage(lang, "core." + logType.getType(), logType.getType()));
            map.put("value", logType.getType());
            logTypes.add(map);
        }
        return APIResult.builder()
                .code(StateCode.OK)
                .data(logTypes)
                .build();
    }

    @Override
    public APIResult<List<Map<String, String>>> getAllLogSubType(String lang) {
        List<Map<String, String>> logTypes = new ArrayList<>();
        Map<String, String> map = getLogTypeMap(lang);
        logTypes.add(map);
        for (LogSubTypeEnum logType : LogSubTypeEnum.values()
        ) {
            map = new HashMap<>();
            map.put("text", i18nMessageService.getMessage(lang, "core." + logType.getType(), logType.getType()));
            map.put("value", logType.getType());
            logTypes.add(map);
        }
        return APIResult.builder()
                .code(StateCode.OK)
                .data(logTypes)
                .build();
    }

    private Map<String, String> getLogTypeMap(String lang) {
        Map<String, String> map = new HashMap<>();
        map.put("text", i18nMessageService.getMessage(lang, "core.ALL", "ALL"));
        map.put("value", "ALL");
        return map;
    }
    //</editor-fold>

    //<editor-fold desc="租户类的接口" defaultstate="collapsed">

    /**
     * 获取所有租户列表，需要自己管理权限
     *
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('signed')")
    @ApiOperation(value = "获取租户列表接口", notes = "获取所有租户的列表，用于切换租户", tags = "租户接口", response = String.class)
    public APIResult<ListData<TenantDTO>> getTenantList() {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(tenantService.getTenantList()).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Forbidden).message(StateCode.Forbidden.getDescribe()).build();
        }
    }

    /**
     * 获取所有租户列表
     *
     * @param page 页数
     * @param rows 每页行数
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:tenant:view')")
    @ApiOperation(value = "获取所有租户接口", notes = "获取所有租户的列表", tags = "租户接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "每页行数", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<TenantDTO>> getAllTenant(int page, int rows) {
        return APIResult.builder().code(StateCode.OK).message("OK").data(tenantService.getAllTenant(page, rows)).build();
    }

    /**
     * 添加租户
     *
     * @param tenantDTO 租户实体
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:tenant:add')")
    @ApiOperation(value = "添加租户接口", notes = "添加一个租户", tags = "租户接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantDTO", value = "租户对象", required = false, paramType = "query", dataType = "TenantDTO")
    })
    public APIResult addTenant(@RequestBody TenantDTO tenantDTO) {
        if (tenantService.addTenant(tenantDTO) > 0) {
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }

    /**
     * 修改租户
     *
     * @param tenantDTO 租户实体
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:tenant:update')")
    @ApiOperation(value = "修改租户接口", notes = "修改租户数据", tags = "租户接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantDTO", value = "租户对象", required = false, paramType = "query", dataType = "TenantDTO")
    })
    public APIResult updateTenant(@RequestBody TenantDTO tenantDTO) {
        if (tenantService.updateTenant(tenantDTO) > 0) {
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }

    /**
     * 根据租户ID获取基础信息（开放服务，无需身份校验）
     *
     * @param tenantUUID
     * @return
     */
    @Override
    @ApiOperation(value = "获取租户信息接口（开放服务，无需身份校验）", notes = "获取租户开放信息", tags = "租户接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUUID", value = "租户UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<TenantInfoDTO> getTenantInfo(String tenantUUID) {
        return APIResult.builder().code(StateCode.OK).message(StateCode.OK.getDescribe()).data(tenantService.getTenantInfo(tenantUUID)).build();
    }

    /**
     * 修改租户基础信息
     *
     * @param tenantInfoDTO 租户基础信息
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:tenantinfo:update')")
    @ApiOperation(value = "修改租户基础信息接口", notes = "修改租户基础信息", tags = "租户接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantInfoDTO", value = "修改租户基础信息对象", required = false, paramType = "query", dataType = "TenantInfoDTO")
    })
    public APIResult updateTenantInfo(@RequestBody TenantInfoDTO tenantInfoDTO) {
        if (tenantService.updateTenantInfo(tenantInfoDTO) > 0) {
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message(StateCode.Failure.getDescribe()).build();
        }
    }
    //</editor-fold>

    //<editor-fold desc="OAtuh类的接口" defaultstate="collapsed">
    @Override
    @PreAuthorize("hasAnyAuthority('platf:oauth:view')")
    @ApiOperation(value = "查看OAtuh客户端接口", notes = "查看OAtuh客户端接口", tags = "OAtuh客户端接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "每页容量", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<OAuthClientDTO>> getOAuthClientAllList(int page, int rows) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(oAuthClientService.getOAuthClientAllList(page, rows)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(failureException.getMessage()).build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:oauth:add')")
    @ApiOperation(value = "添加OAtuh客户端接口", notes = "添加OAtuh客户端接口", tags = "OAtuh客户端接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oAuthClientDTO", value = "OAtuh客户端对象", required = false, paramType = "query", dataType = "OAuthClientDTO")
    })
    public APIResult addOAuthClient(@RequestBody OAuthClientDTO oAuthClientDTO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(oAuthClientService.addOAuthClient(oAuthClientDTO)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(failureException.getMessage()).build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:oauth:update')")
    @ApiOperation(value = "修改OAtuh客户端接口", notes = "修改添加OAtuh客户端接口", tags = "OAtuh客户端接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oAuthClientDTO", value = "OAtuh客户端对象", required = false, paramType = "query", dataType = "OAuthClientDTO")
    })
    public APIResult updateOAuthClient(@RequestBody OAuthClientDTO oAuthClientDTO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(oAuthClientService.updateOAuthClient(oAuthClientDTO)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(failureException.getMessage()).build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:oauth:delete')")
    @ApiOperation(value = "删除OAtuh客户端接口", notes = "删除OAtuh客户端接口", tags = "OAtuh客户端接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "客户端ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult deleteOAuthClient(String clientId) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(oAuthClientService.deleteOAuthClient(clientId)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(failureException.getMessage()).build();
        }
    }
    //</editor-fold>

    //<editor-fold desc="组织机构类的接口" defaultstate="collapsed">

    /**
     * 获取公司列表接口
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:company:view')")
    @ApiOperation(value = "获取公司列表接口", notes = "获取公司列表接口", tags = "组织机构接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult getCompanyList(String tenantUuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(organizationService.getCompanyList(tenantUuid))
                .build();
    }

    /**
     * 获取公司列表（简单列表非树状）
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:company:view')")
    @ApiOperation(value = "获取公司列表（简单列表非树状）", notes = "获取公司列表（简单列表非树状）", tags = "组织机构接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult getCompanySimpleList(String tenantUuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(organizationService.getCompanySimpleList(tenantUuid))
                .build();
    }

    /**
     * 获取我的公司列表接口
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('signed')")
    @ApiOperation(value = "获取公司列表接口", notes = "获取公司列表接口", tags = "组织机构接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult getMyCompanyList(String tenantUuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(organizationService.getMyCompanyList(tenantUuid))
                .build();
    }


    /**
     * 添加新增公司
     *
     * @param organizationVO
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:company:add')")
    @ApiOperation(value = "添加公司接口", notes = "新增公司接口", tags = "组织机构接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "organizationVO", value = "公司信息对象", required = false, paramType = "query", dataType = "OrganizationVO")
    })
    public APIResult addCompany(@RequestBody OrganizationVO organizationVO) {
        int status = organizationService.addCompany(organizationVO);
        if (status > 0) {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .build();
        }
        return APIResult.builder()
                .code(StateCode.Failure)
                .message("Failure")
                .build();
    }

    /**
     * 更新公司信息
     *
     * @param organizationVO
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:company:update')")
    @ApiOperation(value = "更新公司信息接口", notes = "更新公司信息接口", tags = "组织机构接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "organizationVO", value = "公司信息对象", required = false, paramType = "query", dataType = "OrganizationVO")
    })
    public APIResult updateCompany(@RequestBody OrganizationVO organizationVO) {
        int status = organizationService.updateCompany(organizationVO);
        if (status > 0) {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .build();
        }
        return APIResult.builder()
                .code(StateCode.Failure)
                .message("Failure")
                .build();
    }

    /**
     * 获取部门列表（树状）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:department:view')")
    @ApiOperation(value = "获取部门列表（树状）", notes = "获取部门列表（树状）", tags = "组织机构接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyUuid", value = "公司ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult getDepartmentList(String tenantUuid, String companyUuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(organizationService.getDepartmentList(tenantUuid, companyUuid))
                .build();
    }

    /**
     * 添加部门
     *
     * @param organizationVO 部门对象
     * @return
     */
    @Override
    public APIResult addDepartment(@RequestBody OrganizationVO organizationVO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(organizationService.addDepartment(organizationVO)).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Failure).message(forbiddenException.getMessage()).build();
        }
    }

    /**
     * 添加部门
     *
     * @param organizationVO 部门对象
     * @return
     */
    @Override
    public APIResult updateDepartment(@RequestBody OrganizationVO organizationVO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(organizationService.updateDepartment(organizationVO)).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Failure).message(forbiddenException.getMessage()).build();
        }
    }
    //</editor-fold>

    //<editor-fold desc="角色类的接口" defaultstate="collapsed">

    /**
     * 获取角色列表
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:role:view')")
    @ApiOperation(value = "获取角色列表接口", notes = "获取角色列表接口", tags = "角色类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult getRoleList(String tenantUuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("")
                .data(roleService.getRoleList(tenantUuid))
                .build();
    }

    /**
     * 添加角色
     *
     * @param roleDTO 角色传输类
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:role:add')")
    @ApiOperation(value = "添加角色接口", notes = "添加角色接口", tags = "角色类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleDTO", value = "角色数据传输对象", required = false, paramType = "query", dataType = "RoleDTO")
    })
    public APIResult addRole(@RequestBody RoleDTO roleDTO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(roleService.addRole(roleDTO)).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Failure).message(forbiddenException.getMessage()).build();
        }
    }

    /**
     * 更新角色
     *
     * @param roleDTO 角色传输类
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:role:update')")
    @ApiOperation(value = "修改角色接口", notes = "修改角色接口", tags = "角色类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleDTO", value = "角色数据传输对象", required = false, paramType = "query", dataType = "RoleDTO")
    })
    public APIResult updateRole(@RequestBody RoleDTO roleDTO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(roleService.updateRole(roleDTO)).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Failure).message(forbiddenException.getMessage()).build();
        }
    }

    /**
     * 删除角色
     *
     * @param uuid 角色ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:role:delete')")
    @ApiOperation(value = "删除角色接口", notes = "删除角色接口", tags = "角色类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "角色ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult deleteRole(String uuid) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(roleService.deleteRole(uuid)).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Failure).message(forbiddenException.getMessage()).build();
        }
    }
    //</editor-fold>
}
