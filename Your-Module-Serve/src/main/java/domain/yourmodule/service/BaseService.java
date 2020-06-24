package domain.yourmodule.service;

import com.winteree.api.entity.AccountDTO;
import domain.yourmodule.client.WintereeCoreServiceClient;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: BaseService</p>
 * <p>Description: 基础服务类</p>
 *
 * @author RenFei
 * @date : 2020-06-24 20:05
 */
@Slf4j
public abstract class BaseService {
    private final WintereeCoreServiceClient wintereeCoreServiceClient;

    protected BaseService(WintereeCoreServiceClient wintereeCoreServiceClient) {
        this.wintereeCoreServiceClient = wintereeCoreServiceClient;
    }

    /**
     * 获取当前登录的账户
     *
     * @return 当前登录的账户
     */
    protected AccountDTO getSignedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountId = (String) authentication.getPrincipal();
        List<GrantedAuthority> grantedAuthorityList = (List<GrantedAuthority>) authentication.getAuthorities();
        try {
            AccountDTO accountDTO = wintereeCoreServiceClient.findAccountByUuid(accountId);
            if (accountDTO != null) {
                List<String> authorities = null;
                if (!BeanUtils.isEmpty(grantedAuthorityList)) {
                    authorities = new ArrayList<>();
                    for (GrantedAuthority g : grantedAuthorityList
                    ) {
                        authorities.add(g.getAuthority());
                    }
                }
                return Builder.of(AccountDTO::new)
                        .with(AccountDTO::setUuid, accountDTO.getUuid())
                        .with(AccountDTO::setCreateTime, accountDTO.getCreateTime())
                        .with(AccountDTO::setUserName, accountDTO.getUserName())
                        .with(AccountDTO::setEmail, accountDTO.getEmail())
                        .with(AccountDTO::setPhone, accountDTO.getPhone())
                        .with(AccountDTO::setUserStatus, accountDTO.getUserStatus())
                        .with(AccountDTO::setLockTime, accountDTO.getLockTime())
                        .with(AccountDTO::setUuid, accountDTO.getUuid())
                        .with(AccountDTO::setAuthorities, authorities)
                        .build();
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }
}
