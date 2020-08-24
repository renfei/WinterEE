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
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.*;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * WinterEE-Core-Serve 提供的服务实现
 *
 * @author RenFei
 */
@Slf4j
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
    private final CmsService cmsService;
    private final FileService fileService;
    private final TaskService taskService;
    private final RegionService regionService;
    private final VerificationCodeService verificationCodeService;
    private final EmailService emailService;
    private final SmsService aliYunSmsService;
    private final AliyunOssService aliyunOssService;
    private final IpInfoService ipInfoService;
    private final LicenseService licenseService;
    @Autowired
    private HttpServletRequest request;
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
                                   RoleService roleService,
                                   CmsService cmsService,
                                   FileService fileService,
                                   TaskService taskService,
                                   RegionService regionService,
                                   VerificationCodeService verificationCodeService,
                                   EmailService emailService,
                                   @Qualifier("aliyunSmsServiceImpl") SmsService aliYunSmsService,
                                   AliyunOssService aliyunOssService, IpInfoService ipInfoService,
                                   LicenseService licenseService) {
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
        this.cmsService = cmsService;
        this.fileService = fileService;
        this.taskService = taskService;
        this.regionService = regionService;
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
        this.aliYunSmsService = aliYunSmsService;
        this.aliyunOssService = aliyunOssService;
        this.ipInfoService = ipInfoService;
        this.licenseService = licenseService;
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
            logService.log(logDTO);
            return APIResult.success();
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
    public AccountDTO findAccountByUuid(String uuid) {
        return accountService.getAccountById(uuid);
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

    /**
     * 获取超管的UUID
     *
     * @return
     */
    @Override
    @ApiIgnore
    public String getRootAccountUuid() {
        return wintereeCoreConfig.getRootAccount();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    public APIResult<AccountDTO> getMyInfo() {
        return APIResult.builder().code(StateCode.OK).message("OK").data(accountService.getAccountInfo()).build();
    }

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
    @Override
    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "修改自己的密码", notes = "修改自己的密码", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "language", value = "语言，默认 zh-CN", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "keyid", value = "秘钥ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult changePassword(String oldPassword, String newPassword, String language, String keyid) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(accountService.changePassword(oldPassword, newPassword, language, keyid)).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(failureException.getMessage()).build();
        }
    }

    /**
     * 重置任意账户密码
     *
     * @param passwordResetDAT 数据传输对象
     * @return 受影响行数
     * @throws FailureException 失败异常信息
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:account:resetpasseord') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "重置任意账户密码", notes = "重置任意账户密码", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "passwordResetDAT", value = "账户ID", required = false, paramType = "query", dataType = "PasswordResetDAT")
    })
    public APIResult passwordReset(PasswordResetDAT passwordResetDAT) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK")
                    .data(accountService.passwordReset(passwordResetDAT.getAccountUuid(), passwordResetDAT.getNewPassword(), passwordResetDAT.getLanguage(), passwordResetDAT.getKeyid())).build();
        } catch (FailureException failureException) {
            return APIResult.builder().code(StateCode.Failure).message(failureException.getMessage()).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Forbidden).message(forbiddenException.getMessage()).build();
        }
    }

    /**
     * 根据查询条件获取账户列表
     *
     * @param accountSearchCriteriaVO 查询条件
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:account:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取账户列表", notes = "根据查询条件获取账户列表", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountSearchCriteriaVO", value = "查询条件", required = false, paramType = "query", dataType = "AccountSearchCriteriaVO")
    })
    public APIResult<ListData<AccountDTO>> getAccountList(AccountSearchCriteriaVO accountSearchCriteriaVO) {
        return APIResult.builder().code(StateCode.OK).message("OK").data(accountService.getAccountList(accountSearchCriteriaVO)).build();
    }

    /**
     * 添加用户
     * 密码是在添加用户后，使用密码重置功能进行重置的
     *
     * @param accountDTO 用户信息传输对象
     * @return 插入行数
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:account:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取账户列表", notes = "根据查询条件获取账户列表", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountSearchCriteriaVO", value = "查询条件", required = false, paramType = "query", dataType = "AccountSearchCriteriaVO")
    })
    public APIResult addAccount(@RequestBody AccountDTO accountDTO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(accountService.addAccount(accountDTO)).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Forbidden).message(forbiddenException.getMessage()).build();
        }
    }

    /**
     * 更新账户信息
     *
     * @param accountDTO 用户信息传输对象
     * @return 插入行数
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:account:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取账户列表", notes = "根据查询条件获取账户列表", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountSearchCriteriaVO", value = "查询条件", required = false, paramType = "query", dataType = "AccountSearchCriteriaVO")
    })
    public APIResult updateAccount(@RequestBody AccountDTO accountDTO) {
        try {
            return APIResult.builder().code(StateCode.OK).message("OK").data(accountService.updateAccount(accountDTO)).build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder().code(StateCode.Forbidden).message(forbiddenException.getMessage()).build();
        }
    }

    /**
     * 发送验证码(内部接口)
     *
     * @param userName       手机或邮箱
     * @param tenantUuid     租户ID
     * @param validationType 验证码类型
     * @return
     */
    @Override
    @ApiOperation(value = "发送验证码", notes = "发送验证码", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "手机或邮箱", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "validationType", value = "验证码类型", required = false, paramType = "query", dataType = "String")
    })
    public APIResult sendVerificationCode(String userName, String tenantUuid, String validationType) {
        ValidationType validationType1 = null;
        try {
            validationType1 = ValidationType.valueOf(validationType);
        } catch (Exception e) {
            return APIResult.builder().code(StateCode.Failure).message("ValidationType Error").build();
        }
        // 验证码有可能是自己生成的，也可能是短信接口返回的
        String code = StringUtils.getRandomNumber(6);
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        // 调用发送服务发送验证码
        if (StringUtils.isChinaPhone(userName)) {
            verificationCodeDTO.setPhone(userName);
            // 短信服务
            if ("aliyun".equals(wintereeCoreConfig.getSmsService())) {
                Sms sms = Builder.of(Sms::new)
                        .with(Sms::setPhoneNumbers, userName)
                        .with(Sms::setSignName, wintereeCoreConfig.getAliyunSms().getSignName())
                        .with(Sms::setTemplateCode, wintereeCoreConfig.getAliyunSms().getTemplateCode())
                        .with(Sms::setTemplateParam, "{\"code\":\"" + code + "\"}")
                        .with(Sms::setTenantUuid, tenantUuid)
                        .build();
                aliYunSmsService.sendSms(sms);
            } else {
                log.error("短信服务配置错误，没有匹配到启用的短信服务");
                return APIResult.builder().code(StateCode.Failure).message("请联系管理员：短信服务配置错误，没有匹配到启用的短信服务").build();
            }
            verificationCodeDTO.setVerificationCode(code);
        } else if (StringUtils.isEmail(userName)) {
            verificationCodeDTO.setEmail(userName);
            String contentText = "您的验证码为【" + code + "】，有效期10分钟。";
            verificationCodeDTO.setVerificationCode(code);
            verificationCodeDTO.setContentText(contentText);
            Email email = Builder.of(Email::new)
                    .with(Email::setTo, userName)
                    .with(Email::setSubject, "您的验证码邮件")
                    .with(Email::setText, contentText)
                    .build();
            emailService.send(email);
        } else {
            return APIResult.builder().code(StateCode.Failure).message("只有手机号和邮箱地址才能发送验证码").build();
        }
        // 保存验证码
        verificationCodeDTO.setSended(true);
        verificationCodeDTO.setValidationType(validationType1);
        // 十分钟有效期
        verificationCodeDTO.setDeadDate(DateUtils.nextMinutes(10));
        if (verificationCodeService.saveVerificationCode(verificationCodeDTO) == 1) {
            // 返回结果
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message("内部错误：保存验证码失败，请重试").build();
        }
    }

    /**
     * 获取验证码并标记验证码为已使用状态
     *
     * @param userName       手机号/邮箱地址
     * @param validationType 验证码类别
     * @return VerificationCodeDTO
     */
    @Override
    @ApiOperation(value = "获取验证码并标记验证码为已使用状态", notes = "获取验证码并标记验证码为已使用状态", tags = "账户接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "手机或邮箱", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "validationType", value = "验证码类型", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<VerificationCodeDTO> getVerificationCode(String userName, String validationType) {
        ValidationType validationType1 = null;
        try {
            validationType1 = ValidationType.valueOf(validationType);
        } catch (Exception e) {
            return APIResult.builder().code(StateCode.Failure).message("ValidationType Error").build();
        }
        VerificationCodeDTO verificationCodeDTO = verificationCodeService.getVerificationCode(userName, validationType1);
        if (verificationCodeDTO == null) {
            return APIResult.builder().code(StateCode.Failure).message("获取验证码失败").build();
        }
        return APIResult.builder().code(StateCode.OK).message("获取验证码失败").data(verificationCodeDTO).build();
    }
    //</editor-fold>

    //<editor-fold desc="菜单类的接口" defaultstate="collapsed">

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取菜单和权限列表", notes = "获取菜单和权限列表，注意不是菜单管理中的查询菜单列表", tags = "菜单接口", response = APIResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "language", value = "语言，默认 zh-CN", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<List<MenuVO>> getMenuAndAuthorityTree(String language) {
        return APIResult.builder().code(StateCode.OK).message("OK").data(menuService.getMenuAndAuthorityListBySignedUser(language)).build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:menu:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:menu:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:menu:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:menu:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:menu:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:log:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:tenant:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:tenant:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:tenant:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:tenantinfo:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:oauth:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:oauth:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:oauth:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:oauth:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
     * 获取整个组织架构的树
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:AllOrganization:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取整个组织架构的树接口", notes = "获取整个组织架构的树接口", tags = "组织机构接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult getAllOrganizationTree(String tenantUuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(organizationService.getAllOrganizationTree(tenantUuid, tenantUuid))
                .build();
    }

    /**
     * 获取公司列表接口
     *
     * @param tenantUuid 租户ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:company:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:company:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:company:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:company:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:department:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
     * 获取部门列表（简单列表非树状）
     *
     * @param tenantUuid  租户ID
     * @param companyUuid 公司ID
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:department:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取部门列表（简单列表非树状）", notes = "获取部门列表（简单列表非树状）", tags = "组织机构接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "companyUuid", value = "公司ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult getDepartmentSimpleList(String tenantUuid, String companyUuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(organizationService.getDepartmentSimpleList(tenantUuid, companyUuid))
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
    @PreAuthorize("hasAnyAuthority('platf:role:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:role:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:role:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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
    @PreAuthorize("hasAnyAuthority('platf:role:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
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

    /**
     * 获取数据权限范围（取最大值）
     *
     * @return 数据权限范围
     */
    @Override
    @ApiOperation(value = "获取数据权限范围（取最大值）", notes = "获取数据权限范围（取最大值）", tags = "角色类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountUuid", value = "账号UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<Integer> getDataScope(String accountUuid) {
        return APIResult.builder().code(StateCode.OK).message("OK").data(roleService.getDataScope(accountUuid)).build();
    }
    //</editor-fold>

    //<editor-fold desc="CMS类的接口" defaultstate="collapsed">

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmssite:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取站点列表接口", notes = "获取站点列表接口", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantUuid", value = "租户ID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "每页行数", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<CmsSiteDTO>> getCmsSiteList(String tenantUuid, int page, int rows) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("")
                .data(cmsService.getCmsSiteList(tenantUuid, page, rows))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmssite:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取站点信息接口", notes = "根据UUID获取站点信息接口", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "站点UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsSiteDTO> getCmsSiteByUuid(String uuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getCmsSiteByUuid(uuid))
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @ApiOperation(value = "根据域名获取站点信息接口", notes = "根据域名获取站点信息", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "domain", value = "站点域名", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsSiteDTO> getCmsSiteByDomain(String domain) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getCmsSiteByDomain(domain))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmssite:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "添加站点接口", notes = "添加站点接口", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsSiteDTO", value = "站点传输对象", required = false, paramType = "query", dataType = "CmsSiteDTO")
    })
    public APIResult addCmsSite(CmsSiteDTO cmsSiteDTO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.addCmsSite(cmsSiteDTO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmssite:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "修改站点接口", notes = "修改站点接口", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsSiteDTO", value = "站点传输对象", required = false, paramType = "query", dataType = "CmsSiteDTO")
    })
    public APIResult updateCmsSite(CmsSiteDTO cmsSiteDTO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.updateCmsSite(cmsSiteDTO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmssite:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "删除站点接口", notes = "删除站点下所有内容", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "站点UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult deleteCmsSite(String uuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.deleteCmsSite(uuid))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmscategory:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取CMS系统分类列表接口", notes = "获取CMS系统分类列表", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<List<CmsCategoryDTO>> getCmsCategoryList(String siteUuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getCmsCategoryList(siteUuid))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmscategory:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "根据UUID获取CMS系统站点下的分类接口", notes = "根据UUID获取CMS系统站点下的分类", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "分类UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsCategoryDTO> getCmsCategoryByUuid(String uuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getCmsCategoryByUuid(uuid))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmscategory:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "根据英文名获取CMS系统站点下的分类接口", notes = "根据英文名获取CMS系统站点下的分类接口", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ename", value = "分类英文名", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsCategoryDTO> getCmsCategoryByEname(String ename) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getCmsCategoryByEname(ename))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmscategory:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "添加文章分类接口", notes = "添加文章分类", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsCategoryDTO", value = "分类传输对象", required = false, paramType = "query", dataType = "CmsCategoryDTO")
    })
    public APIResult addCmsCategory(CmsCategoryDTO cmsCategoryDTO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.addCmsCategory(cmsCategoryDTO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmscategory:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "更新文章分类接口", notes = "更新文章分类", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsCategoryDTO", value = "分类传输对象", required = false, paramType = "query", dataType = "CmsCategoryDTO")
    })
    public APIResult updateCmsCategory(CmsCategoryDTO cmsCategoryDTO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.updateCmsCategory(cmsCategoryDTO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmscategory:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "删除文章分类接口", notes = "删除文章分类", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsCategoryUuid", value = "分类UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult deleteCmsCategory(String cmsCategoryUuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.deleteCmsCategory(cmsCategoryUuid))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmsposts:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "根据查询条件获取文章列表接口", notes = "根据查询条件获取文章列表", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsPostSearchCriteriaVO", value = "查询条件", required = false, paramType = "query", dataType = "CmsPostSearchCriteriaVO")
    })
    public APIResult<ListData<CmsPostsDTO>> getCmsPostList(CmsPostSearchCriteriaVO cmsPostSearchCriteriaVO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getCmsPostList(cmsPostSearchCriteriaVO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @ApiOperation(value = "根据栏目ID获取文章列表（前台）", notes = "根据栏目ID获取文章列表（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryUuid", value = "分类UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pages", value = "页码", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "每页行数", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<CmsPostsDTO>> getCmsPostListByCategory(String categoryUuid, int pages, int rows) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getCmsPostListByCategory(categoryUuid, pages, rows))
                .build();
    }

    @Override
    @ApiOperation(value = "获取相关文章列表（前台）", notes = "获取相关文章列表（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文章UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "number", value = "获取数量", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<CmsPostsDTO>> getRelatedPostList(String uuid, int number) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getRelatedPostList(uuid, number))
                .build();
    }

    @Override
    @ApiOperation(value = "获取最热文章列表（前台）", notes = "获取最热文章列表（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "number", value = "获取数量", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<CmsPostsDTO>> getHotPostList(String siteUuid, int number) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getHotPostList(siteUuid, number))
                .build();
    }

    @Override
    @ApiOperation(value = "获取年度最热文章列表（前台）", notes = "获取年度最热文章列表（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "number", value = "获取数量", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<CmsPostsDTO>> getHotPostListByYear(String siteUuid, int number) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getHotPostListByYear(siteUuid, number))
                .build();
    }

    @Override
    @ApiOperation(value = "获取季度最热文章列表（前台）", notes = "获取季度最热文章列表（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "number", value = "获取数量", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<CmsPostsDTO>> getHotPostListByQuarter(String siteUuid, int number) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getHotPostListByQuarter(siteUuid, number))
                .build();
    }

    @Override
    @ApiOperation(value = "根据文章ID获取文章详情（前台）", notes = "根据文章ID获取文章详情（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文章UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsPostsDTO> getCmsPostByUuid(String uuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getCmsPostByUuid(uuid))
                .build();
    }

    @Override
    @ApiOperation(value = "根据文章ID获取文章详情（前台）", notes = "根据文章ID获取文章详情（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文章UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "updateView", value = "是否更新浏览量", required = false, paramType = "query", dataType = "Boolean")
    })
    public APIResult<CmsPostsDTO> getCmsPostByUuid(String uuid, Boolean updateView) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getCmsPostByUuid(uuid, updateView))
                .build();
    }

    @Override
    @ApiOperation(value = "根据文章ID获取文章详情（前台）", notes = "根据文章ID获取文章详情（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = false, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "updateView", value = "是否更新浏览量", required = false, paramType = "query", dataType = "Boolean")
    })
    public APIResult<CmsPostsDTO> getCmsPostByLongId(Long id, Boolean updateView) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getCmsPostByLongId(id, updateView))
                .build();
    }

    @Override
    @ApiOperation(value = "文章点赞（前台）", notes = "文章点赞（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文章ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult thumbsUpPost(String uuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.thumbsUpPost(uuid))
                .build();
    }

    @Override
    @ApiOperation(value = "文章点踩（前台）", notes = "文章点踩（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文章ID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult thumbsDownPost(String uuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.thumbsDownPost(uuid))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmsposts:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "添加文章接口", notes = "添加文章", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsPostsDTO", value = "文章传输对象", required = false, paramType = "query", dataType = "CmsPostsDTO")
    })
    public APIResult addCmsPost(CmsPostsDTO cmsPostsDTO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.addCmsPost(cmsPostsDTO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmsposts:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "修改文章接口", notes = "修改文章", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsPostsDTO", value = "文章传输对象", required = false, paramType = "query", dataType = "CmsPostsDTO")
    })
    public APIResult updateCmsPost(CmsPostsDTO cmsPostsDTO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.updateCmsPost(cmsPostsDTO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmsposts:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "删除文章接口", notes = "删除文章", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文章UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult deleteCmsPost(String uuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.deleteCmsPost(uuid))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @ApiOperation(value = "文章点赞接口", notes = "文章点赞接口", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文章UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult thumbsUpCmsPost(String uuid) {
        cmsService.thumbsUpCmsPost(uuid);
        return APIResult.success();
    }

    @Override
    @ApiOperation(value = "文章点踩接口", notes = "文章点踩接口", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "文章UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult thumbsDownCmsPost(String uuid) {
        cmsService.thumbsDownCmsPost(uuid);
        return APIResult.success();
    }

    @Override
    @ApiOperation(value = "根据标签英文名获取文章列表（前台）", notes = "根据标签英文名获取文章列表（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tagEname", value = "标签英文名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pages", value = "页码", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rows", value = "行数", required = false, paramType = "query", dataType = "Integer")
    })
    public APIResult<ListData<CmsPostsDTO>> getCmsPostListByTagEname(String siteUuid, String tagEname,
                                                                     Integer pages, Integer rows) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getCmsPostListByTagEname(siteUuid, tagEname, pages, rows))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmstag:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取标签列表接口", notes = "获取标签列表", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pages", value = "页码", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "每页行数", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<CmsTagDTO>> getTagList(String siteUuid, int pages, int rows) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getTagList(siteUuid, pages, rows))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @ApiOperation(value = "获取所有标签列表以及文章数量（前台）", notes = "获取所有标签列表以及文章数量（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<List<CmsTagDTO>> getAllTagAndCount(String siteUuid) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getAllTagAndCount(siteUuid))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmstag:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "根据UUID获取标签对象接口", notes = "根据UUID获取标签对象", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "uuid", value = "标签UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsTagDTO> getTagByUuid(String siteUuid, String uuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getTagByUuid(siteUuid, uuid))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @ApiOperation(value = "根据英文名获取标签对象接口", notes = "根据英文名获取标签对象接口", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ename", value = "英文名", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsTagDTO> getTagByEname(String siteUuid, String ename) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getTagByEname(siteUuid, ename))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmstag:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "添加标签接口", notes = "添加标签", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsTagDTO", value = "标签传输对象", required = false, paramType = "query", dataType = "CmsTagDTO")
    })
    public APIResult addCmsTag(CmsTagDTO cmsTagDTO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.addCmsTag(cmsTagDTO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmstag:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "更新标签接口", notes = "更新标签", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsTagDTO", value = "标签传输对象", required = false, paramType = "query", dataType = "CmsTagDTO")
    })
    public APIResult updateCmsTag(CmsTagDTO cmsTagDTO) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.updateCmsTag(cmsTagDTO))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmstag:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "删除标签接口", notes = "删除标签", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "标签UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult deleteCmsTag(String uuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.deleteCmsTag(uuid))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    /**
     * 获取CMS菜单树
     *
     * @param siteUuid 站点UUID
     * @param menuType 菜单类型
     * @return
     */
    @Override
    @ApiOperation(value = "获取CMS菜单树（CMS系统）", notes = "获取CMS菜单树（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "menuType", value = "菜单类型", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<List<CmsMenuVO>> getCmsMenuBySiteUuidAndType(String siteUuid, int menuType) {
        CmsMenuEnum cmsMenuEnum = CmsMenuEnum.valueOf(menuType);
        if (cmsMenuEnum == null) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("menuType Error")
                    .build();
        }
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getCmsMenuBySiteUuidAndType(siteUuid, cmsMenuEnum))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    /**
     * 根据CMS系统的UUID获取菜单对象
     *
     * @param uuid 菜单UUID
     * @return
     */
    @Override
    @ApiOperation(value = "根据CMS系统的UUID获取菜单对象（CMS系统）", notes = "根据CMS系统的UUID获取菜单对象（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "菜单UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsMenuVO> getCmsMenuByUuid(String uuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getCmsMenuByUuid(uuid))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    /**
     * 添加菜单（CMS系统）
     *
     * @param cmsMenuVO
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmsmenu:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "添加菜单（CMS系统）", notes = "添加菜单（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsMenuVO", value = "菜单对象", required = false, paramType = "query", dataType = "CmsMenuVO")
    })
    public APIResult addCmsMenu(CmsMenuVO cmsMenuVO) {
        try {
            cmsService.addCmsMenu(cmsMenuVO);
            return APIResult.success();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    /**
     * 修改菜单（CMS系统）
     *
     * @param cmsMenuVO
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmsmenu:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "修改菜单（CMS系统）", notes = "修改菜单（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsMenuVO", value = "菜单对象", required = false, paramType = "query", dataType = "CmsMenuVO")
    })
    public APIResult updateCmsMenu(CmsMenuVO cmsMenuVO) {
        try {
            cmsService.updateCmsMenu(cmsMenuVO);
            return APIResult.success();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    /**
     * 删除菜单（CMS系统）
     *
     * @param uuid
     * @return
     */
    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmsmenu:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "删除菜单（CMS系统）", notes = "删除菜单（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "菜单UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult deleteCmsMenu(String uuid) {
        try {
            cmsService.deleteCmsMenu(uuid);
            return APIResult.success();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @ApiOperation(value = "获取CMS自定义页面（前台）", notes = "获取CMS自定义页面（前台）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "页面ID", required = false, paramType = "query", dataType = "Long")
    })
    public APIResult<CmsPageDTO> getCmsPageById(Long id) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(cmsService.getCmsPageById(id))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmspage:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取CMS自定义页面（CMS系统）", notes = "获取CMS自定义页面（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "uuid", value = "页面UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<CmsPageDTO> getCmsPageByUuid(String siteUuid, String uuid) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(cmsService.getCmsPageByUuid(siteUuid, uuid))
                    .build();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmspage:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "删除自定义页面（CMS系统）", notes = "删除自定义页面（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteUuid", value = "站点UUID", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "uuid", value = "页面UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult deleteCmsPage(String siteUuid, String uuid) {
        try {
            cmsService.deleteCmsPage(siteUuid, uuid);
            return APIResult.success();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmspage:update') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "更新自定义页面（CMS系统）", notes = "更新自定义页面（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsPageDTO", value = "自定义页面数据传输对象", required = false, paramType = "query", dataType = "CmsPageDTO")
    })
    public APIResult updateCmsPage(CmsPageDTO cmsPageDTO) {
        try {
            cmsService.updateCmsPage(cmsPageDTO);
            return APIResult.success();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:cmspage:add') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "添加自定义页面（CMS系统）", notes = "添加自定义页面（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsPageDTO", value = "自定义页面数据传输对象", required = false, paramType = "query", dataType = "CmsPageDTO")
    })
    public APIResult addCmsPage(CmsPageDTO cmsPageDTO) {
        try {
            cmsService.addCmsPage(cmsPageDTO);
            return APIResult.success();
        } catch (ForbiddenException forbiddenException) {
            return APIResult.builder()
                    .code(StateCode.Forbidden)
                    .message(forbiddenException.getMessage())
                    .build();
        }
    }
    //</editor-fold>

    //<editor-fold desc="评论类的接口" defaultstate="collapsed">
    @Override
    @ApiOperation(value = "评论（CMS系统）", notes = "评论（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentDTO", value = "菜单UUID", required = false, paramType = "query", dataType = "CommentDTO")
    })
    public APIResult addComment(CommentDTO commentDTO) {
        commentDTO.setAuthorIp(IpUtils.getIpAddress(request));
        return cmsService.addComment(commentDTO);
    }

    @Override
    @ApiOperation(value = "根据文章UUID获取评论树（CMS系统）", notes = "根据文章UUID获取评论树（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postUuid", value = "文章UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<List<CommentDTO>> getCommentByPostId(String postUuid) {
        return APIResult.builder().code(StateCode.OK).message("").data(cmsService.getCommentByPostId(postUuid)).build();
    }

    @Override
    @ApiOperation(value = "获取最新的评论（CMS系统）", notes = "获取最新的评论（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "获取数量", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<List<CommentDTO>> getLastComment(int size) {
        return APIResult.builder().code(StateCode.OK).message("").data(cmsService.getLastComment(size)).build();
    }

    @Override
    @ApiOperation(value = "根据文章UUID获取评论数量（CMS系统）", notes = "根据文章UUID获取评论数量（CMS系统）", tags = "CMS类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postUuid", value = "文章UUID", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<Long> getCommentNumber(String postUuid) {
        return APIResult.builder().code(StateCode.OK).message("").data(cmsService.getCommentNumber(postUuid)).build();
    }
    //</editor-fold>

    //<editor-fold desc="文件类的接口" defaultstate="collapsed">
    @Override
    @PreAuthorize("hasAnyAuthority('platf:publicfile:upload') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "公开文件上传接口", notes = "该接口上传的文件将直接公共读，不做权限校验", tags = "文件类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = false, paramType = "query", dataType = "MultipartFile")
    })
    public APIResult<String> uploadPublicFile(MultipartFile file) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("成功")
                    .data(fileService.uploadPublicFile(file))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("Failure")
                    .build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "私有文件上传接口", notes = "该接口上传的文件获取时会做简单的校验，不会公共读", tags = "文件类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = false, paramType = "query", dataType = "MultipartFile")
    })
    public APIResult<String> uploadPrivateFile(MultipartFile file) {
        try {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("")
                    .data(fileService.uploadPrivateFile(file))
                    .build();
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("Failure")
                    .build();
        }
    }

    @PreAuthorize("hasAnyAuthority('signed') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @GetMapping("/getFile")
    public void getFile(String uuid, HttpServletResponse response) throws IOException {
        FileDTO fileDTO = fileService.getFileOnVerification(uuid);
        if (fileDTO == null) {
            response.setStatus(404);
            response.getWriter().write("404 Not Found.");
        } else {
            if (fileDTO.getFile() != null) {
                //设置响应头控制浏览器以下载的形式打开文件
                response.setHeader("content-disposition",
                        "attachment;fileName=" + fileDTO.getFile().getName());
                InputStream in = new FileInputStream(fileDTO.getFile()); //获取下载文件的输入流
                int count = 0;
                byte[] by = new byte[1024];
                //通过response对象获取OutputStream流
                OutputStream out = response.getOutputStream();
                while ((count = in.read(by)) != -1) {
                    out.write(by, 0, count);//将缓冲区的数据输出到浏览器
                }
                in.close();
                out.flush();
                out.close();
            } else {
                response.sendRedirect(fileDTO.getFileUrl());
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="定时任务类的接口" defaultstate="collapsed">
    @Override
    @PreAuthorize("hasAnyAuthority('platf:task:view') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "获取定时任务列表接口", notes = "获取定时任务列表接口", tags = "定时任务类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "每页行数", required = false, paramType = "query", dataType = "int")
    })
    public APIResult<ListData<TaskJobDTO>> getTaskList(int pages, int rows) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(taskService.getTaskList(pages, rows))
                .build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:task:save') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "保存定时任务接口", notes = "保存定时任务接口，包含新增和修改", tags = "定时任务类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskJobDTO", value = "定时任务数据传输对象", required = false, paramType = "query", dataType = "TaskJobDTO")
    })
    public APIResult saveTask(TaskJobDTO taskJobDTO) {
        if (taskService.saveJob(taskJobDTO)) {
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message("Failure").build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:task:exec') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "立即触发一个定时任务接口", notes = "立即触发一个定时任务接口", tags = "定时任务类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "jobGroup", value = "任务分组", required = false, paramType = "query", dataType = "String")
    })
    public APIResult triggerJob(String jobName, String jobGroup) {
        if (taskService.triggerJob(jobName, jobGroup)) {
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message("Failure").build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:task:pause') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "暂停一个任务接口", notes = "暂停一个任务接口", tags = "定时任务类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "jobGroup", value = "任务分组", required = false, paramType = "query", dataType = "String")
    })
    public APIResult pauseJob(String jobName, String jobGroup) {
        if (taskService.pauseJob(jobName, jobGroup)) {
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message("Failure").build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:task:resume') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "恢复一个任务接口", notes = "恢复一个任务接口", tags = "定时任务类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "jobGroup", value = "任务分组", required = false, paramType = "query", dataType = "String")
    })
    public APIResult resumeJob(String jobName, String jobGroup) {
        if (taskService.resumeJob(jobName, jobGroup)) {
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message("Failure").build();
        }
    }

    @Override
    @PreAuthorize("hasAnyAuthority('platf:task:delete') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "删除一个任务接口", notes = "删除一个任务接口", tags = "定时任务类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "jobGroup", value = "任务分组", required = false, paramType = "query", dataType = "String")
    })
    public APIResult removeJob(String jobName, String jobGroup) {
        if (taskService.removeJob(jobName, jobGroup)) {
            return APIResult.success();
        } else {
            return APIResult.builder().code(StateCode.Failure).message("Failure").build();
        }
    }

    @PreAuthorize("hasAnyAuthority('platf:task:save') or (#oauth2.isClient() and #oauth2.hasScope('WinterEE-Core-Serve'))")
    @ApiOperation(value = "修改某个任务的执行时间接口", notes = "修改某个任务的执行时间接口", tags = "定时任务类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "jobGroup", value = "任务分组", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "time", value = "执行时间", required = false, paramType = "query", dataType = "String")
    })
    @Override
    public APIResult modifyJob(String jobName, String jobGroup, String time) {
        try {
            if (taskService.modifyJob(jobName, jobGroup, time)) {
                return APIResult.success();
            } else {
                return APIResult.builder().code(StateCode.Failure).message("Failure").build();
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return APIResult.builder().code(StateCode.Failure).message("Failure").build();
        }
    }
    //</editor-fold>

    //<editor-fold desc="行政区划类的接口" defaultstate="collapsed">

    @Override
    @ApiOperation(value = "获取行政区划接口", notes = "获取行政区划接口", tags = "行政区划类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "行政区划代码（6位数字）", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<RegionDTO> getRegionByCode(String code) {
        RegionDTO regionDTO = regionService.getRegionByCode(code);
        if (regionDTO == null) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("未查询到指定的行政区划数据")
                    .build();
        } else {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(regionDTO)
                    .build();
        }
    }

    /**
     * 获取子级行政区划列表
     *
     * @param code 本级行政代码，为空时查询顶级行政区划
     * @return List<RegionDTO>
     */
    @Override
    @ApiOperation(value = "获取子级行政区划接口", notes = "获取子级行政区划接口", tags = "行政区划类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "行政区划代码（6位数字）", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<List<RegionDTO>> getChildRegion(String code) {
        List<RegionDTO> regionDTOS = regionService.getChildRegion(code);
        if (regionDTOS == null) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message("未查询到指定的行政区划数据")
                    .build();
        } else {
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("OK")
                    .data(regionDTOS)
                    .build();
        }
    }

    //</editor-fold>

    @Override
    @ApiOperation(value = "查询IP信息", notes = "查询IP信息", tags = "工具类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ip", value = "IPv4地址或IPv6地址", required = false, paramType = "query", dataType = "String")
    })
    public APIResult<IpInfoDTO> queryIpInfo(String ip) {
        return ipInfoService.query(ip);
    }

    @Override
    @ApiOperation(value = "获取授权信息", notes = "获取授权信息", tags = "工具类接口", response = String.class)
    public APIResult<LicenseDTO> getLicense() {
        return APIResult.builder().code(StateCode.OK).message("OK").data(licenseService.getLicense()).build();
    }

    @Override
    @ApiOperation(value = "上传文件到阿里云OSS私有桶", notes = "上传文件到阿里云OSS私有桶", tags = "工具类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = false, paramType = "query", dataType = "MultipartFile"),
            @ApiImplicitParam(name = "objectName", value = "文件名及路径", required = false, paramType = "query", dataType = "String")
    })
    public APIResult uploadPrivateFileByAliyunOss(MultipartFile file, String objectName) {
        try {
            aliyunOssService.uploadPrivateFile(file.getInputStream(), objectName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return APIResult.builder().code(StateCode.Failure).message("内部服务器错误").build();
        }
        return APIResult.success();
    }

    @Override
    @ApiOperation(value = "上传文件到阿里云OSS公有桶", notes = "上传文件到阿里云OSS公有桶", tags = "工具类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件", required = false, paramType = "query", dataType = "MultipartFile"),
            @ApiImplicitParam(name = "objectName", value = "文件名及路径", required = false, paramType = "query", dataType = "String")
    })
    public APIResult uploadPubilcFileByAliyunOss(MultipartFile file, String objectName) {
        try {
            aliyunOssService.uploadPubilcFile(file.getInputStream(), objectName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return APIResult.builder().code(StateCode.Failure).message("内部服务器错误").build();
        }
        return APIResult.success();
    }

    @Override
    @ApiOperation(value = "上传文件到阿里云OSS", notes = "上传文件到阿里云OSS", tags = "工具类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucketName", value = "储存桶名称", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "file", value = "文件", required = false, paramType = "query", dataType = "MultipartFile"),
            @ApiImplicitParam(name = "objectName", value = "文件名及路径", required = false, paramType = "query", dataType = "String")
    })
    public APIResult putObjectByAliyunOss(String bucketName, String objectName, MultipartFile file) {
        try {
            aliyunOssService.putObject(bucketName, objectName, file.getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return APIResult.builder().code(StateCode.Failure).message("内部服务器错误").build();
        }
        return APIResult.success();
    }

    @Override
    @ApiOperation(value = "获取阿里云OSS授权链接", notes = "获取阿里云OSS授权链接", tags = "工具类接口", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "downLoadHost", value = "下载链接的域名和协议，例如：https://cdn.renfei.net", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "bucketName", value = "储存桶名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "objectName", value = "文件路径和文件名", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "expiration", value = "过期时间", required = false, paramType = "query", dataType = "Date")
    })
    public APIResult<String> generatePresignedUrlByAliyunOss(String downLoadHost, String bucketName, String objectName, Date expiration) {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("OK")
                .data(aliyunOssService.generatePresignedUrl(downLoadHost, bucketName, objectName, expiration))
                .build();
    }
}
