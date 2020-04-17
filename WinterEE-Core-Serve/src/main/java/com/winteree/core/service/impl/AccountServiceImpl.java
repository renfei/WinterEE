package com.winteree.core.service.impl;

import com.winteree.api.entity.AccountDTO;
import com.winteree.core.dao.AccountDOMapper;
import com.winteree.core.dao.entity.AccountDO;
import com.winteree.core.dao.entity.AccountDOExample;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户数据服务
 *
 * @author RenFei
 */
@Service
public class AccountServiceImpl extends BaseService implements AccountService {
    private final AccountDOMapper accountDOMapper;

    public AccountServiceImpl(AccountDOMapper accountDOMapper) {
        this.accountDOMapper = accountDOMapper;
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

    private AccountDTO getAccountDTO(List<AccountDO> accountDOS) {
        AccountDO accountDO = ListUtils.getOne(accountDOS);
        if (!BeanUtils.isEmpty(accountDO)) {
            AccountDTO accountDTO = new AccountDTO();
            org.springframework.beans.BeanUtils.copyProperties(accountDO, accountDTO);
            return accountDTO;
        }
        return null;
    }
}
