package com.winteree.core.service;

import com.winteree.api.entity.AccountDTO;
import com.winteree.api.entity.AccountSearchCriteriaVO;
import com.winteree.api.entity.ListData;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;

/**
 * <p>Title: AccountService</p>
 * <p>Description: 账户服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 12:53
 */
public interface AccountService {
    /**
     * 账户查询
     *
     * @param accountSearchCriteriaVO 查询条件
     * @return 账户列表
     */
    ListData<AccountDTO> getAccountList(AccountSearchCriteriaVO accountSearchCriteriaVO);

    /**
     * 添加用户
     * 密码是在添加用户后，使用密码重置功能进行重置的
     *
     * @param addAccountDTO 用户信息传输对象
     * @return 插入行数
     * @throws ForbiddenException 权限不足异常
     */
    int addAccount(AccountDTO addAccountDTO) throws ForbiddenException;

    /**
     * 更新账户信息
     *
     * @param updateAccountDTO 用户信息传输对象
     * @return 更新行数
     * @throws ForbiddenException 权限不足异常
     */
    int updateAccount(AccountDTO updateAccountDTO) throws ForbiddenException;

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
    int changePassword(String oldPassword, String newPassword, String language, String keyid) throws FailureException;

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
    int passwordReset(String accountUuid, String newPassword, String language, String keyid) throws FailureException, ForbiddenException;

    AccountDTO getAccountById(String uuid);

    AccountDTO getAccountIdByUserName(String username);

    AccountDTO getAccountIdByEmail(String email);

    AccountDTO getAccountIdByPhone(String phone);

    AccountDTO getAccountInfo();
}
