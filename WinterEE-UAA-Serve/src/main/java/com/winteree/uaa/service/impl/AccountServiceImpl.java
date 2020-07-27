package com.winteree.uaa.service.impl;

import com.winteree.api.entity.AccountSignUpDTO;
import com.winteree.api.entity.ValidationType;
import com.winteree.api.entity.VerificationCodeDTO;
import com.winteree.uaa.client.WintereeCoreServiceClient;
import com.winteree.uaa.config.WintereeUaaConfig;
import com.winteree.uaa.dao.AccountDOMapper;
import com.winteree.uaa.dao.OauthClientDetailsDOMapper;
import com.winteree.uaa.dao.entity.AccountDO;
import com.winteree.uaa.dao.entity.AccountDOExample;
import com.winteree.uaa.dao.entity.OauthClientDetailsDO;
import com.winteree.uaa.dao.entity.OauthClientDetailsDOExample;
import com.winteree.uaa.service.*;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 账号服务
 *
 * @authorenFei
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private static final String AUTHORITY_DELIMITERS = ",";
    private final WintereeCoreServiceClient wintereeCoreServiceClient;
    private final AccountDOMapper accountDOMapper;
    private final WintereeUaaConfig wintereeUaaConfig;
    private final MenuService menuService;
    private final RoleService roleService;
    private final OauthClientDetailsDOMapper oauthClientDetailsDOMapper;
    private final SecretKeyService secretKeyService;
    private final I18nService i18NService;

    public AccountServiceImpl(WintereeCoreServiceClient wintereeCoreServiceClient,
                              AccountDOMapper accountDOMapper,
                              WintereeUaaConfig wintereeUaaConfig,
                              MenuService menuService,
                              RoleService roleService,
                              OauthClientDetailsDOMapper oauthClientDetailsDOMapper,
                              SecretKeyService secretKeyService,
                              I18nService i18NService) {
        this.wintereeCoreServiceClient = wintereeCoreServiceClient;
        this.accountDOMapper = accountDOMapper;
        this.wintereeUaaConfig = wintereeUaaConfig;
        this.menuService = menuService;
        this.roleService = roleService;
        this.oauthClientDetailsDOMapper = oauthClientDetailsDOMapper;
        this.secretKeyService = secretKeyService;
        this.i18NService = i18NService;
    }

    /**
     * 账户注册
     *
     * @param accountSignUpDTO 账户注册数据传输对象
     * @return
     */
    @Override
    public APIResult signUp(AccountSignUpDTO accountSignUpDTO) {
        OauthClientDetailsDOExample oauthClientDetailsDOExample = new OauthClientDetailsDOExample();
        oauthClientDetailsDOExample.createCriteria().andClientIdEqualTo(accountSignUpDTO.getClientId());
        OauthClientDetailsDO oauthClientDetailsDO = ListUtils.getOne(oauthClientDetailsDOMapper.selectByExampleWithBLOBs(oauthClientDetailsDOExample));
        if (oauthClientDetailsDO == null) {
            return APIResult.builder().code(StateCode.Failure).message("Client Id Error").build();
        }
        // 解密密码
        String passwd = "";
        try {
            passwd = decryptPassword(accountSignUpDTO.getPassword(), "zh-CN", accountSignUpDTO.getKeyId());
        } catch (Exception exception) {
            return APIResult.builder().code(StateCode.Failure).message(exception.getMessage()).build();
        }
        AccountDO accountDO = new AccountDO();
        AccountDOExample example = new AccountDOExample();
        if (StringUtils.isChinaPhone(accountSignUpDTO.getUserName())) {
            // 是手机号注册，验证是否重名
            example.createCriteria()
                    .andPhoneEqualTo(accountSignUpDTO.getUserName())
                    .andTenantUuidEqualTo(oauthClientDetailsDO.getTenantUuid());
            if (BeanUtils.isEmpty(accountDOMapper.selectByExample(example))) {
                // 验证验证码
                APIResult<VerificationCodeDTO> apiResult =
                        wintereeCoreServiceClient.getVerificationCode(accountDO.getUserName(), ValidationType.SIGNUP.value());
                if (!StateCode.OK.getCode().equals(apiResult.getCode()) || apiResult.getData() == null) {
                    return APIResult.builder().code(StateCode.Failure).message("验证码错误，请重新获取新的验证码").build();
                }
                if (!apiResult.getData().getVerificationCode().equals(accountSignUpDTO.getVerCode())) {
                    return APIResult.builder().code(StateCode.Failure).message("验证码错误，请重新获取新的验证码").build();
                }
            } else {
                return APIResult.builder().code(StateCode.Failure).message("手机号已经被注册过，不能再次注册").build();
            }
            accountDO.setPhone(accountSignUpDTO.getUserName());
        } else if (StringUtils.isEmail(accountSignUpDTO.getUserName())) {
            // 是邮件注册，验证是否重名
            example.createCriteria()
                    .andEmailEqualTo(accountSignUpDTO.getUserName())
                    .andTenantUuidEqualTo(oauthClientDetailsDO.getTenantUuid());
            if (BeanUtils.isEmpty(accountDOMapper.selectByExample(example))) {
                // 验证验证码
                APIResult<VerificationCodeDTO> apiResult =
                        wintereeCoreServiceClient.getVerificationCode(accountDO.getUserName(), ValidationType.SIGNUP.value());
                if (!StateCode.OK.getCode().equals(apiResult.getCode()) || apiResult.getData() == null) {
                    return APIResult.builder().code(StateCode.Failure).message("验证码错误，请重新获取新的验证码").build();
                }
                if (!apiResult.getData().getVerificationCode().equals(accountSignUpDTO.getVerCode())) {
                    return APIResult.builder().code(StateCode.Failure).message("验证码错误，请重新获取新的验证码").build();
                }
            } else {
                return APIResult.builder().code(StateCode.Failure).message("邮箱地址已经被注册过，不能再次注册").build();
            }
            accountDO.setEmail(accountSignUpDTO.getUserName());
        } else {
            // 用户名注册，验证是否重名
            example.createCriteria()
                    .andUserNameEqualTo(accountSignUpDTO.getUserName())
                    .andTenantUuidEqualTo(oauthClientDetailsDO.getTenantUuid());
            if (BeanUtils.isEmpty(accountDOMapper.selectByExample(example))) {
                // 不重名，允许注册，用户名注册没有验证码验证步奏
            } else {
                return APIResult.builder().code(StateCode.Failure).message("用户名已经被占用").build();
            }
            accountDO.setUserName(accountSignUpDTO.getUserName());
        }
        try {
            accountDO.setPasswd(PasswordUtils.createHash(passwd));
        } catch (PasswordUtils.CannotPerformOperationException e) {
            log.error(e.getMessage(), e);
        }
        accountDO.setTenantUuid(oauthClientDetailsDO.getTenantUuid());
        accountDO.setCreateTime(new Date());
        accountDO.setUserStatus(1);
        accountDO.setErrorCount(0);
        accountDO.setLastName(accountDO.getLastName());
        accountDO.setFirstName(accountDO.getFirstName());
        accountDOMapper.insertSelective(accountDO);
        return APIResult.builder().code(StateCode.OK).message("OK").data(accountDO.getUuid()).build();
    }

    /**
     * 根据用户名获取账号对象
     *
     * @param username 用户名
     * @return AccountDTO
     */
    @Override
    public AccountDO findAccountByUsername(String username) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andUserNameEqualTo(username);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    /**
     * 根据邮箱地址获取账号对象
     *
     * @param email 电子邮件
     * @return AccountDTO
     */
    @Override
    public AccountDO findAccountByEmail(String email) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andEmailEqualTo(email);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    /**
     * 根据手机号获取账号对象
     *
     * @param phone 手机号
     * @return AccountDTO
     */
    @Override
    public AccountDO findAccountByPhoneNumber(String phone) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andPhoneEqualTo(phone);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    @Override
    public int updateByPrimaryKeySelective(AccountDO accountDO) {
        accountDO.setUpdateTime(new Date());
        return accountDOMapper.updateByPrimaryKeySelective(accountDO);
    }

    /**
     * 获取用户的权限列表
     *
     * @param accountDO 账户实体
     * @return 权限列表
     */
    @Override
    public List<GrantedAuthority> getGrantedAuthority(AccountDO accountDO) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("signed,");
        if (wintereeUaaConfig.getRootAccountId().equals(accountDO.getUuid())) {
            // 如果是平台管理员，加载全部权限列表
            for (String permission : menuService.getAllMenuPermission()
            ) {
                stringBuilder.append(permission);
                stringBuilder.append(AUTHORITY_DELIMITERS);
            }
        } else {
            // 根据用户获得角色列表
            List<String> roles = roleService.selectRoleUuidByUserUuid(accountDO.getUuid());
            // 根据角色列表获得菜单列表，从而获得权限列表
            List<String> menuIds = menuService.getMenuUuidByRoleUuid(roles);
            for (String permission : menuService.getMenuPermissionByMenuUuid(menuIds)
            ) {
                stringBuilder.append(permission);
                stringBuilder.append(AUTHORITY_DELIMITERS);
            }
        }
        String authority = stringBuilder.toString();
        if (authority.endsWith(AUTHORITY_DELIMITERS)) {
            authority = authority.substring(0, authority.length() - 1);
        }
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }

    /**
     * 解密密码
     *
     * @param password 密文密码
     * @param language 语言
     * @param keyId    AES的ID
     * @return 明文密码
     */
    private String decryptPassword(String password, String language, String keyId) throws Exception {
        String aesKey = secretKeyService.getSecretKeyStringById(keyId);
        if (aesKey == null) {
            throw new Exception(i18NService.getMessage(language, "uaa.aeskeyiddoesnotexist", "AESKeyId不存在"));
        }
        try {
            password = AESUtil.decrypt(password, aesKey);
        } catch (Exception ex) {
            throw new Exception(i18NService.getMessage(language, "uaa.passworddecryptionfailed", "密码解密失败"));
        }
        return password;
    }
}
