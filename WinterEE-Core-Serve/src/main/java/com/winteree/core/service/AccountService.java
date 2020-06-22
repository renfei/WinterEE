package com.winteree.core.service;

import com.winteree.api.entity.AccountDTO;
import com.winteree.api.entity.AccountSearchCriteriaVO;
import com.winteree.api.entity.ListData;
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

    AccountDTO getAccountById(String uuid);

    AccountDTO getAccountIdByUserName(String username);

    AccountDTO getAccountIdByEmail(String email);

    AccountDTO getAccountIdByPhone(String phone);

    AccountDTO getAccountInfo();
}
