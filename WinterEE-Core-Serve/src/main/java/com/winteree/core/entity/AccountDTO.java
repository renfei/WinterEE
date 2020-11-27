package com.winteree.core.entity;

import com.winteree.core.dao.entity.AccountDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: AccountDTO</p>
 * <p>Description: 账户传输类</p>
 *
 * @author RenFei
 * @date : 2020-04-18 13:56
 */
@Data
public class AccountDTO extends AccountDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> authorities;
}
