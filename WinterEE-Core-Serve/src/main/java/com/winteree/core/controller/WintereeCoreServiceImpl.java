package com.winteree.core.controller;

import com.winteree.api.entity.*;
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

import java.util.List;
import java.util.Map;

/**
 * WinterEE-Core-Serve 提供的服务实现
 *
 * @author RenFei
 */
@RestController
public class WintereeCoreServiceImpl implements WintereeCoreService {

    private final I18nMessageService i18nMessageService;
    private final WintereeCoreConfig wintereeCoreConfig;
    private final AccountService accountService;
    private final SecretKeyService secretKeyService;
    private final LogService logService;
    private final MenuService menuService;

    public WintereeCoreServiceImpl(I18nMessageService i18nMessageService,
                                   WintereeCoreConfig wintereeCoreConfig,
                                   AccountService accountService,
                                   SecretKeyService secretKeyService,
                                   LogService logService,
                                   MenuService menuService) {
        this.i18nMessageService = i18nMessageService;
        this.wintereeCoreConfig = wintereeCoreConfig;
        this.accountService = accountService;
        this.secretKeyService = secretKeyService;
        this.logService = logService;
        this.menuService = menuService;
    }

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

    @Override
    @ApiIgnore
    public APIResult log(LogDTO logDTO) {
        return logService.log(logDTO);
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
        return secretKeyService.setSecretKey(reportPublicKeyVO);
    }

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
        return menuService.getMenuListBySignedUser(language);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:view')")
    @OperationLog(description = "获取系统菜单列表", type = LogSubTypeEnum.SELECT)
    @ApiOperation(value = "获取后台设置菜单接口", notes = "获取后台设置菜单接口，后台管理使用的", tags = "菜单接口", response = APIResult.class)
    public APIResult<List<MenuVO>> getSettingMenuTree() {
        return menuService.getAllMenuTree();
    }

    @Override
    public APIResult<List<MenuVO>> getSettingMenuList() {
        return menuService.getAllMenuList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:view')")
    @OperationLog(description = "获取系统菜单详情", type = LogSubTypeEnum.SELECT)
    @ApiOperation(value = "获取后台设置菜单详情接口", notes = "获取后台设置菜单详情接口，后台管理使用的", tags = "菜单接口", response = APIResult.class)
    public APIResult<MenuVO> getSettingMenu(String uuid) {
        return menuService.getMenuByUuid(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:delete')")
    @OperationLog(description = "删除系统菜单", type = LogSubTypeEnum.DELETE)
    @ApiOperation(value = "删除系统菜单", notes = "物理删除系统菜单，不可恢复！！", tags = "菜单接口", response = APIResult.class)
    public APIResult deleteSettingMenuByUuid(String uuid) {
        return menuService.deleteMenuByUuid(uuid);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:update')")
    @OperationLog(description = "修改系统菜单", type = LogSubTypeEnum.UPDATE)
    @ApiOperation(value = "修改系统菜单", notes = "修改更新系统菜单", tags = "菜单接口", response = APIResult.class)
    public APIResult updateSettingMenu(@RequestBody MenuVO menuVO) {
        return menuService.updateMenu(menuVO);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:add')")
    @OperationLog(description = "添加系统菜单", type = LogSubTypeEnum.INSERT)
    @ApiOperation(value = "添加系统菜单", notes = "添加系统菜单", tags = "菜单接口", response = APIResult.class)
    public APIResult addSettingMenu(@RequestBody MenuVO menuVO) {
        return menuService.addMenu(menuVO);
    }
}
