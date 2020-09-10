package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.*;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.AccountDOMapper;
import com.winteree.core.dao.OrganizationDOMapper;
import com.winteree.core.dao.RoleDOMapper;
import com.winteree.core.dao.UserRoleDOMapper;
import com.winteree.core.dao.entity.*;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.I18nMessageService;
import com.winteree.core.service.SecretKeyService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.ListUtils;
import net.renfei.sdk.utils.PasswordUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 账户数据服务
 *
 * @author RenFei
 */
@Slf4j
@Service
public class AccountServiceImpl extends BaseService implements AccountService {
    private final I18nMessageService i18NService;
    private final SecretKeyService secretKeyService;
    private final AccountDOMapper accountDOMapper;
    private final RoleDOMapper roleDOMapper;
    private final UserRoleDOMapper userRoleDOMapper;
    private final OrganizationDOMapper organizationDOMapper;

    protected AccountServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                 I18nMessageService i18NService,
                                 SecretKeyService secretKeyService,
                                 AccountDOMapper accountDOMapper,
                                 RoleDOMapper roleDOMapper,
                                 UserRoleDOMapper userRoleDOMapper,
                                 OrganizationDOMapper organizationDOMapper) {
        super(wintereeCoreConfig);
        this.i18NService = i18NService;
        this.secretKeyService = secretKeyService;
        this.accountDOMapper = accountDOMapper;
        this.roleDOMapper = roleDOMapper;
        this.userRoleDOMapper = userRoleDOMapper;
        this.organizationDOMapper = organizationDOMapper;
    }

    /**
     * 账户查询
     *
     * @param accountSearchCriteriaVO 查询条件
     * @return 账户列表
     */
    @Override
    public ListData<AccountDTO> getAccountList(AccountSearchCriteriaVO accountSearchCriteriaVO) {
        com.winteree.core.entity.AccountDTO accountDTO = getSignedUser(this);
        AccountDOExample example = new AccountDOExample();
        AccountDOExample.Criteria criteria = example.createCriteria();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            //只有平台超管才能夸租户管理，并且获取所有公司
            criteria.andTenantUuidEqualTo(accountSearchCriteriaVO.getTenantuuid());
        } else {
            //否则只能管理自己归属的租户
            accountSearchCriteriaVO.setTenantuuid(accountDTO.getTenantUuid());
            criteria.andTenantUuidEqualTo(accountSearchCriteriaVO.getTenantuuid());
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = this.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    break;
                case COMPANY:
                    // 只能查询该公司下的
                    this.checkInCompany(accountDTO, criteria);
                    break;
                case DEPARTMENT:
                    // 只能查询他自己部门下的
                    this.checkInDepartment(accountDTO, criteria);
                    break;
                case COMPANY_AND_DEPARTMENT:
                    return new ListData<>();
                default:
                    return new ListData<>();
            }
        }
        if (!BeanUtils.isEmpty(accountSearchCriteriaVO.getOrgType()) &&
                !BeanUtils.isEmpty(accountSearchCriteriaVO.getOrgUuid())) {
            switch (Objects.requireNonNull(OrgEnum.valueOf(accountSearchCriteriaVO.getOrgType()))) {
                case COMPANY:
                    criteria.andOfficeUuidEqualTo(accountSearchCriteriaVO.getOrgUuid());
                    break;
                case DEPARTMENT:
                    criteria.andDepartmentUuidEqualTo(accountSearchCriteriaVO.getOrgUuid());
                    break;
                default:
                    break;
            }
        }
        if (!BeanUtils.isEmpty(accountSearchCriteriaVO.getAccountUuid())) {
            criteria.andUuidLike("%" + accountSearchCriteriaVO.getAccountUuid() + "%");
        }
        if (!BeanUtils.isEmpty(accountSearchCriteriaVO.getUserName())) {
            criteria.andUserNameLike("%" + accountSearchCriteriaVO.getUserName() + "%");
        }
        if (!BeanUtils.isEmpty(accountSearchCriteriaVO.getPhone())) {
            criteria.andPhoneLike("%" + accountSearchCriteriaVO.getPhone() + "%");
        }
        if (!BeanUtils.isEmpty(accountSearchCriteriaVO.getEmail())) {
            criteria.andEmailLike("%" + accountSearchCriteriaVO.getEmail() + "%");
        }
        List<AccountDTO> accountDTOS = new ArrayList<>();
        ListData<AccountDTO> accountDTOListData = new ListData<>();
        Page page = null;
        if (!BeanUtils.isEmpty(accountSearchCriteriaVO.getPages()) &&
                !BeanUtils.isEmpty(accountSearchCriteriaVO.getRows())) {
            page = PageHelper.startPage(accountSearchCriteriaVO.getPages(), accountSearchCriteriaVO.getRows());
        }
        for (AccountDO accountDO : accountDOMapper.selectByExample(example)
        ) {
            AccountDTO account = new AccountDTO();
            org.springframework.beans.BeanUtils.copyProperties(accountDO, account);
            accountDTOS.add(account);
        }
        if (page != null) {
            accountDTOListData.setTotal(page.getTotal());
        }
        // 给传输类填充公司名称和部门名称信息
        fillCompanyAndDepartmentInfo(accountDTOS);
        // 填充角色ID
        fillRoles(accountDTOS);
        accountDTOListData.setData(accountDTOS);
        return accountDTOListData;
    }

    /**
     * 添加用户
     * 密码是在添加用户后，使用密码重置功能进行重置的
     *
     * @param addAccountDTO 用户信息传输对象
     * @return 插入行数
     * @throws ForbiddenException 权限不足异常
     */
    @Override
    public int addAccount(AccountDTO addAccountDTO) throws ForbiddenException {
        com.winteree.core.entity.AccountDTO accountDTO = getSignedUser(this);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            //只有平台超管才能夸租户管理
            if (BeanUtils.isEmpty(addAccountDTO.getTenantUuid())) {
                addAccountDTO.setTenantUuid(accountDTO.getTenantUuid());
            }
        } else {
            //否则只能管理自己归属的租户
            addAccountDTO.setTenantUuid(accountDTO.getTenantUuid());
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = this.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    break;
                case COMPANY:
                    // 只能该公司下的
                    addAccountDTO.setOfficeUuid(accountDTO.getOfficeUuid());
                    break;
                case DEPARTMENT:
                    // 只能他自己部门下的
                    addAccountDTO.setOfficeUuid(accountDTO.getOfficeUuid());
                    addAccountDTO.setDepartmentUuid(accountDTO.getDepartmentUuid());
                    break;
                case COMPANY_AND_DEPARTMENT:
                    throw new ForbiddenException("权限不足");
                default:
                    throw new ForbiddenException("权限不足");
            }
        }
        // 检查用户名、手机、电邮重复的情况
        AccountDOExample example = new AccountDOExample();
        if (BeanUtils.isEmpty(addAccountDTO.getUserName())) {
            throw new FailureException("用户名不能为空");
        } else {
            example.createCriteria()
                    .andTenantUuidEqualTo(addAccountDTO.getTenantUuid())
                    .andUserNameEqualTo(addAccountDTO.getUserName());
            if (!BeanUtils.isEmpty(accountDOMapper.selectByExample(example))) {
                throw new FailureException("用户名被占用");
            }
        }
        if (!BeanUtils.isEmpty(addAccountDTO.getPhone())) {
            example = new AccountDOExample();
            example.createCriteria()
                    .andTenantUuidEqualTo(addAccountDTO.getTenantUuid())
                    .andPhoneEqualTo(addAccountDTO.getPhone());
            if (!BeanUtils.isEmpty(accountDOMapper.selectByExample(example))) {
                throw new FailureException("手机号码被占用");
            }
        }
        if (!BeanUtils.isEmpty(addAccountDTO.getEmail())) {
            example = new AccountDOExample();
            example.createCriteria()
                    .andTenantUuidEqualTo(addAccountDTO.getTenantUuid())
                    .andEmailEqualTo(addAccountDTO.getEmail());
            if (!BeanUtils.isEmpty(accountDOMapper.selectByExample(example))) {
                throw new FailureException("邮件地址被占用");
            }
        }

        // 密码是在添加用户后，使用密码重置功能进行重置的
        AccountDO accountDO = Builder.of(AccountDO::new)
                .with(AccountDO::setUuid, UUID.randomUUID().toString().toUpperCase())
                .with(AccountDO::setTenantUuid, addAccountDTO.getTenantUuid())
                .with(AccountDO::setOfficeUuid, addAccountDTO.getOfficeUuid())
                .with(AccountDO::setDepartmentUuid, addAccountDTO.getDepartmentUuid())
                .with(AccountDO::setCreateTime, new Date())
                .with(AccountDO::setUserName, addAccountDTO.getUserName())
                .with(AccountDO::setEmail, addAccountDTO.getEmail())
                .with(AccountDO::setPhone, addAccountDTO.getPhone())
                .with(AccountDO::setUserStatus, 1)
                .build();
        int rows = accountDOMapper.insertSelective(accountDO);
        updateRole(addAccountDTO);
        return rows;
    }

    /**
     * 更新账户信息
     *
     * @param updateAccountDTO 用户信息传输对象
     * @return 更新行数
     * @throws ForbiddenException 权限不足异常
     */
    @Override
    public int updateAccount(AccountDTO updateAccountDTO) throws ForbiddenException {
        // 不要相信客户端提交的，要从库里查，然后修改
        AccountDTO oldAccountDTO = this.getAccountById(updateAccountDTO.getUuid());
        if (oldAccountDTO != null) {
            com.winteree.core.entity.AccountDTO accountDTO = getSignedUser(this);
            if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                //超管完全信任
            } else {
                //否则只能管理自己归属的租户
                if (accountDTO.getTenantUuid().equals(oldAccountDTO.getTenantUuid())) {
                    // 验证数据权限范围，是全部还是本公司
                    DataScopeEnum dataScopeEnum = this.getDataScope();
                    switch (dataScopeEnum) {
                        case ALL:
                            //TODO 此处还应该判断是否更新了所属公司和部门，同时去更新
                            break;
                        case COMPANY:
                            //TODO 此处还应该判断是否更新了所属部门，同时去更新
                            if (!oldAccountDTO.getOfficeUuid().equals(updateAccountDTO.getOfficeUuid())) {
                                // 只能管理该公司下的
                                throw new ForbiddenException("权限不足");
                            }
                            break;
                        case DEPARTMENT:
                            if (!oldAccountDTO.getOfficeUuid().equals(updateAccountDTO.getOfficeUuid()) &&
                                    !oldAccountDTO.getDepartmentUuid().equals(updateAccountDTO.getDepartmentUuid())) {
                                // 只能管理他自己部门下的
                                throw new ForbiddenException("权限不足");
                            }
                            break;
                        default:
                            throw new ForbiddenException("权限不足");
                    }
                } else {
                    // 不可跨租户管理
                    throw new ForbiddenException("权限不足");
                }
            }
            // 检查用户名、手机、电邮重复的情况
            AccountDOExample checkexample = new AccountDOExample();
            if (BeanUtils.isEmpty(updateAccountDTO.getUserName())) {
                throw new FailureException("用户名不能为空");
            } else {
                if (oldAccountDTO.getUserName() == null || !oldAccountDTO.getUserName().equals(updateAccountDTO.getUserName())) {
                    checkexample.createCriteria()
                            .andTenantUuidEqualTo(updateAccountDTO.getTenantUuid())
                            .andUserNameEqualTo(updateAccountDTO.getUserName());
                    if (!BeanUtils.isEmpty(accountDOMapper.selectByExample(checkexample))) {
                        throw new FailureException("用户名被占用");
                    }
                }
            }
            if (oldAccountDTO.getPhone() == null || !oldAccountDTO.getPhone().equals(updateAccountDTO.getPhone())) {
                checkexample = new AccountDOExample();
                checkexample.createCriteria()
                        .andTenantUuidEqualTo(updateAccountDTO.getTenantUuid())
                        .andPhoneEqualTo(updateAccountDTO.getPhone());
                if (!BeanUtils.isEmpty(accountDOMapper.selectByExample(checkexample))) {
                    throw new FailureException("手机号码被占用");
                }
            }
            if (oldAccountDTO.getEmail() == null || !oldAccountDTO.getEmail().equals(updateAccountDTO.getEmail())) {
                checkexample = new AccountDOExample();
                checkexample.createCriteria()
                        .andTenantUuidEqualTo(updateAccountDTO.getTenantUuid())
                        .andEmailEqualTo(updateAccountDTO.getEmail());
                if (!BeanUtils.isEmpty(accountDOMapper.selectByExample(checkexample))) {
                    throw new FailureException("邮件地址被占用");
                }
            }
            // 只允许更新这几项字段，这不是BUG！例如用户名是不允许修改的
            AccountDO accountDO = new AccountDO();
            accountDO.setOfficeUuid(updateAccountDTO.getOfficeUuid());
            accountDO.setDepartmentUuid(updateAccountDTO.getDepartmentUuid());
            accountDO.setEmail(updateAccountDTO.getEmail());
            accountDO.setPhone(updateAccountDTO.getPhone());
            accountDO.setUpdateTime(new Date());
            AccountDOExample example = new AccountDOExample();
            example.createCriteria().andUuidEqualTo(oldAccountDTO.getUuid());
            updateRole(updateAccountDTO);
            return accountDOMapper.updateByExampleSelective(accountDO, example);
        } else {
            return 0;
        }
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
    public int changePassword(String oldPassword, String newPassword, String language, String keyid) throws FailureException {
        com.winteree.core.entity.AccountDTO accountDTO = getSignedUser(this);
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            throw new FailureException(i18NService.getMessage(language, "uaa.invalidusernameorpassword", "无效的密码"));
        }
        // AES解密密码
        oldPassword = secretKeyService.decrypt(oldPassword, language, keyid);
        newPassword = secretKeyService.decrypt(newPassword, language, keyid);
        if (!PasswordUtils.verifyPassword(oldPassword, accountDTO.getPasswd())) {
            throw new FailureException(i18NService.getMessage(language, "uaa.invalidusernameorpassword", "无效的密码"));
        }
        try {
            newPassword = PasswordUtils.createHash(newPassword);
        } catch (PasswordUtils.CannotPerformOperationException e) {
            log.error(e.getMessage(), e);
            throw new FailureException("失败！内部服务器错误");
        }
        AccountDO accountDO = new AccountDO();
        accountDO.setPasswd(newPassword);
        AccountDOExample example = new AccountDOExample();
        example.createCriteria().andUuidEqualTo(accountDTO.getUuid());
        return accountDOMapper.updateByExampleSelective(accountDO, example);
    }

    /**
     * 重置任意账户密码
     *
     * @param accountUuid 账户ID
     * @param newPassword 新密码
     * @param language    语言
     * @param keyid       秘钥ID
     * @return 受影响行数
     * @throws FailureException   失败异常信息
     * @throws ForbiddenException 权限不足异常信息
     */
    @Override
    public int passwordReset(String accountUuid, String newPassword, String language, String keyid) throws FailureException, ForbiddenException {
        if (StringUtils.isEmpty(newPassword)) {
            throw new FailureException(i18NService.getMessage(language, "uaa.invalidusernameorpassword", "无效的密码"));
        }
        com.winteree.core.entity.AccountDTO signedAccountDTO = getSignedUser(this);
        AccountDTO accountDTO = this.getAccountById(accountUuid);
        if (accountDTO != null) {
            if (wintereeCoreConfig.getRootAccount().equals(signedAccountDTO.getUuid())) {
                // 超管登陆，啥都不限制
            } else {
                if (wintereeCoreConfig.getRootAccount().equals(accountUuid)) {
                    // 超管的账号
                    if (!signedAccountDTO.getUuid().equals(wintereeCoreConfig.getRootAccount())) {
                        throw new ForbiddenException("超管的账号只能超管自己重置");
                    }
                }
                if (!signedAccountDTO.getTenantUuid().equals(accountDTO.getTenantUuid())) {
                    // 不可跨租户管理用户
                    throw new ForbiddenException("权限不足");
                }
                DataScopeEnum dataScopeEnum = this.getDataScope();
                switch (dataScopeEnum) {
                    case ALL:
                        break;
                    case COMPANY:
                        // TODO 此处应该还考虑子公司
                        if (accountDTO.getOfficeUuid().equals(signedAccountDTO.getOfficeUuid())) {
                            // 只能管理自己公司的
                        } else {
                            // 无权管理其他公司的角色
                            throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                        }
                        break;
                    case DEPARTMENT:
                        // TODO 此处应该还考虑子部门
                        if (accountDTO.getDepartmentUuid().equals(signedAccountDTO.getDepartmentUuid())) {
                            // 只能管理自己公司的
                        } else {
                            // 无权管理其他公司的角色
                            throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                        }
                    default:
                        // 其他数据范围的不允许
                        throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                }
            }
            // AES解密密码
            newPassword = secretKeyService.decrypt(newPassword, language, keyid);
            try {
                newPassword = PasswordUtils.createHash(newPassword);
            } catch (PasswordUtils.CannotPerformOperationException e) {
                log.error(e.getMessage(), e);
                throw new FailureException("失败！内部服务器错误");
            }
            AccountDO accountDO = new AccountDO();
            accountDO.setPasswd(newPassword);
            AccountDOExample example = new AccountDOExample();
            example.createCriteria().andUuidEqualTo(accountUuid);
            return accountDOMapper.updateByExampleSelective(accountDO, example);
        } else {
            throw new FailureException("失败！用户未找到");
        }
    }

    @Override
    public AccountDTO getAccountById(String uuid) {
        if (!BeanUtils.isEmpty(uuid)) {
            AccountDOExample accountDoExample = new AccountDOExample();
            accountDoExample.createCriteria()
                    .andUuidEqualTo(uuid)
                    .andUserStatusGreaterThan(0);
            return getAccountDTO(accountDOMapper.selectByExample(accountDoExample));
        }
        return null;
    }

    @Override
    public AccountDTO getAccountIdByUserName(String username) {
        if (!BeanUtils.isEmpty(username)) {
            AccountDOExample accountDoExample = new AccountDOExample();
            accountDoExample.createCriteria()
                    .andUserNameEqualTo(username);
            return getAccountDTO(accountDOMapper.selectByExample(accountDoExample));
        }
        return null;
    }

    @Override
    public AccountDTO getAccountIdByEmail(String email) {
        if (!BeanUtils.isEmpty(email)) {
            AccountDOExample accountDoExample = new AccountDOExample();
            accountDoExample.createCriteria()
                    .andEmailEqualTo(email);
            return getAccountDTO(accountDOMapper.selectByExample(accountDoExample));
        }
        return null;
    }

    @Override
    public AccountDTO getAccountIdByPhone(String phone) {
        if (!BeanUtils.isEmpty(phone)) {
            AccountDOExample accountDoExample = new AccountDOExample();
            accountDoExample.createCriteria()
                    .andPhoneEqualTo(phone);
            return getAccountDTO(accountDOMapper.selectByExample(accountDoExample));
        }
        return null;
    }

    @Override
    public AccountDTO getAccountInfo() {
        com.winteree.core.entity.AccountDTO accountDTO = getSignedUser();
        return Builder.of(AccountDTO::new)
                .with(AccountDTO::setAuthorities, accountDTO.getAuthorities())
                .with(AccountDTO::setUserName, accountDTO.getUserName())
                .with(AccountDTO::setUuid, accountDTO.getUuid())
                .with(AccountDTO::setTenantUuid, accountDTO.getTenantUuid())
                .with(AccountDTO::setOfficeUuid, accountDTO.getOfficeUuid())
                .with(AccountDTO::setDepartmentUuid, accountDTO.getDepartmentUuid())
                .with(AccountDTO::setEmail, accountDTO.getEmail())
                .with(AccountDTO::setPhone, accountDTO.getPhone())
                .with(AccountDTO::setCreateTime, accountDTO.getCreateTime())
                .build();
    }

    /**
     * 给传输类填充公司名称和部门名称信息
     *
     * @param accountDTOS 账户实体列表
     */
    private void fillCompanyAndDepartmentInfo(List<AccountDTO> accountDTOS) {
        for (AccountDTO accountDTO : accountDTOS
        ) {
            if (!BeanUtils.isEmpty(accountDTO.getOfficeUuid())) {
                OrganizationDOExample example = new OrganizationDOExample();
                example.createCriteria().andUuidEqualTo(accountDTO.getOfficeUuid());
                OrganizationDO organizationDO = ListUtils.getOne(organizationDOMapper.selectByExample(example));
                if (organizationDO != null) {
                    accountDTO.setOfficeName(organizationDO.getName());
                }
            }
            if (!BeanUtils.isEmpty(accountDTO.getDepartmentUuid())) {
                OrganizationDOExample example = new OrganizationDOExample();
                example.createCriteria().andUuidEqualTo(accountDTO.getDepartmentUuid());
                OrganizationDO organizationDO = ListUtils.getOne(organizationDOMapper.selectByExample(example));
                if (organizationDO != null) {
                    accountDTO.setDepartmentName(organizationDO.getName());
                }
            }
        }
    }

    /**
     * 更新用户的角色
     *
     * @param accountDTO
     */
    private void updateRole(AccountDTO accountDTO) {
        // 先删除再添加
        UserRoleDOExample example = new UserRoleDOExample();
        example.createCriteria().andAccountUuidEqualTo(accountDTO.getUuid());
        userRoleDOMapper.deleteByExample(example);
        // 添加
        for (String roleid : accountDTO.getRoles()
        ) {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUuid(UUID.randomUUID().toString().toUpperCase());
            userRoleDO.setAccountUuid(accountDTO.getUuid());
            userRoleDO.setRoleUuid(roleid);
            userRoleDOMapper.insertSelective(userRoleDO);
        }
    }

    private void fillRoles(List<AccountDTO> accountDTOS) {
        for (AccountDTO accountDTO : accountDTOS
        ) {
            UserRoleDOExample example = new UserRoleDOExample();
            example.createCriteria().andAccountUuidEqualTo(accountDTO.getUuid());
            List<UserRoleDO> userRoleDOS = userRoleDOMapper.selectByExample(example);
            if (BeanUtils.isEmpty(userRoleDOS)) {
                accountDTO.setRoles(new ArrayList<>());
            } else {
                List<String> ids = new ArrayList<>();
                userRoleDOS.forEach(id -> ids.add(id.getRoleUuid()));
                accountDTO.setRoles(ids);
            }
        }
    }

    private DataScopeEnum getDataScope() {
        com.winteree.core.entity.AccountDTO accountDTO = getSignedUser(this);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 如果是超管，直接最大权限
            return DataScopeEnum.ALL;
        } else {
            List<String> roleIds = this.selectRoleUuidByUserUuid(accountDTO.getUuid());
            if (BeanUtils.isEmpty(roleIds)) {
                return DataScopeEnum.DEPARTMENT;
            }
            RoleDOExample roleDOExample = new RoleDOExample();
            roleDOExample.createCriteria().andUuidIn(roleIds);
            List<RoleDO> roleDOS = roleDOMapper.selectByExample(roleDOExample);
            if (BeanUtils.isEmpty(roleDOS)) {
                return DataScopeEnum.DEPARTMENT;
            }
            DataScopeEnum maxDataScopeEnum = DataScopeEnum.DEPARTMENT;
            for (RoleDO role : roleDOS
            ) {
                if (role.getDataScope() != null && role.getDataScope() > maxDataScopeEnum.value()) {
                    maxDataScopeEnum = DataScopeEnum.valueOf(role.getDataScope());
                }
            }
            return maxDataScopeEnum;
        }
    }

    private List<String> selectRoleUuidByUserUuid(String userUuid) {
        UserRoleDOExample userRoleDOExample = new UserRoleDOExample();
        userRoleDOExample.createCriteria()
                .andAccountUuidEqualTo(userUuid);
        List<UserRoleDO> userRoleDOS = userRoleDOMapper.selectByExample(userRoleDOExample);
        List<String> roleUuid = new ArrayList<>();
        if (BeanUtils.isEmpty(userRoleDOS)) {
            return roleUuid;
        }
        for (UserRoleDO userRole : userRoleDOS
        ) {
            roleUuid.add(userRole.getRoleUuid());
        }
        return roleUuid;
    }

    /**
     * 获取当前登录的账户(账户服务私有方法，共有的在BaseService里)
     *
     * @return 当前登录的账户
     */
    private com.winteree.core.entity.AccountDTO getSignedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountId = (String) authentication.getPrincipal();
        List<GrantedAuthority> grantedAuthorityList = (List<GrantedAuthority>) authentication.getAuthorities();
        com.winteree.api.entity.AccountDTO accountDTO = this.getAccountById(accountId);
        if (accountDTO != null) {
            List<String> authorities = null;
            if (!BeanUtils.isEmpty(grantedAuthorityList)) {
                authorities = new ArrayList<>();
                for (GrantedAuthority g : grantedAuthorityList
                ) {
                    authorities.add(g.getAuthority());
                }
            }
            return Builder.of(com.winteree.core.entity.AccountDTO::new)
                    .with(com.winteree.core.entity.AccountDTO::setUuid, accountDTO.getUuid())
                    .with(com.winteree.core.entity.AccountDTO::setCreateTime, accountDTO.getCreateTime())
                    .with(com.winteree.core.entity.AccountDTO::setUserName, accountDTO.getUserName())
                    .with(com.winteree.core.entity.AccountDTO::setEmail, accountDTO.getEmail())
                    .with(com.winteree.core.entity.AccountDTO::setPhone, accountDTO.getPhone())
                    .with(com.winteree.core.entity.AccountDTO::setUserStatus, accountDTO.getUserStatus())
                    .with(com.winteree.core.entity.AccountDTO::setLockTime, accountDTO.getLockTime())
                    .with(com.winteree.core.entity.AccountDTO::setUuid, accountDTO.getUuid())
                    .with(com.winteree.core.entity.AccountDTO::setTenantUuid, accountDTO.getTenantUuid())
                    .with(com.winteree.core.entity.AccountDTO::setAuthorities, authorities)
                    .build();
        } else {
            return null;
        }
    }

    private AccountDTO getAccountDTO(List<AccountDO> accountDOS) {
        AccountDO accountDO = ListUtils.getOne(accountDOS);
        if (!BeanUtils.isEmpty(accountDO)) {
            AccountDTO accountDTO = new AccountDTO();
            org.springframework.beans.BeanUtils.copyProperties(accountDO, accountDTO);
            return accountDTO;
        }
        return null;
    }

    /**
     * 检查查询条件是否是本公司下的
     *
     * @param accountDTO
     * @param criteria
     */
    private void checkInCompany(com.winteree.core.entity.AccountDTO accountDTO,
                                AccountDOExample.Criteria criteria) {
        // 先获取公司下的所有机构列表
        OrganizationDOExample example = new OrganizationDOExample();
        example.createCriteria().andUuidEqualTo(accountDTO.getOfficeUuid());
        List<OrganizationDO> myOrganization = organizationDOMapper.selectByExample(example);
        //递归查询子级机构
        getMyOrganization(myOrganization);
        List<String> ids = new ArrayList<>();
        if (!BeanUtils.isEmpty(myOrganization)) {
            myOrganization.forEach(item -> {
                ids.add(item.getUuid());
            });
        }
        criteria.andOfficeUuidIn(ids);
    }

    /**
     * 检查查询条件是否是本公司下的
     *
     * @param accountDTO
     * @param criteria
     */
    private void checkInDepartment(com.winteree.core.entity.AccountDTO accountDTO,
                                   AccountDOExample.Criteria criteria) {
        // 先获取公司下的所有机构列表
        OrganizationDOExample example = new OrganizationDOExample();
        example.createCriteria().andUuidEqualTo(accountDTO.getDepartmentUuid());
        List<OrganizationDO> myOrganization = organizationDOMapper.selectByExample(example);
        //递归查询子级机构
        getMyOrganization(myOrganization);
        List<String> ids = new ArrayList<>();
        if (!BeanUtils.isEmpty(myOrganization)) {
            myOrganization.forEach(item -> {
                ids.add(item.getUuid());
            });
        }
        criteria.andDepartmentUuidIn(ids);
    }

    private void getMyOrganization(List<OrganizationDO> myOrganization) {
        for (OrganizationDO org : myOrganization
        ) {
            OrganizationDOExample example = new OrganizationDOExample();
            example.createCriteria().andParentUuidEqualTo(org.getUuid());
            List<OrganizationDO> organization = organizationDOMapper.selectByExample(example);
            if (!BeanUtils.isEmpty(organization)) {
                myOrganization.addAll(organization);
                getMyOrganization(organization);
            }
        }
    }
}
