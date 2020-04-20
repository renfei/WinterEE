package com.winteree.core.service.impl;

import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.UserRoleDOMapper;
import com.winteree.core.dao.entity.UserRoleDO;
import com.winteree.core.dao.entity.UserRoleDOExample;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.RoleService;
import net.renfei.sdk.utils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: RoleServiceImpl</p>
 * <p>Description: 角色服务</p>
 *
 * @author RenFei
 * @date : 2020-04-20 13:48
 */
@Service
public class RoleServiceImpl extends BaseService implements RoleService {
    private final UserRoleDOMapper userRoleDOMapper;
    protected RoleServiceImpl(AccountService accountService,
                              WintereeCoreConfig wintereeCoreConfig,
                              UserRoleDOMapper userRoleDOMapper) {
        super(accountService, wintereeCoreConfig);
        this.userRoleDOMapper = userRoleDOMapper;
    }

    /**
     * 根据用户UUID获取角色列表UUID
     *
     * @param userUuid 用户UUID
     * @return 角色列表UUID
     */
    @Override
    public List<String> selectRoleUuidByUserUuid(String userUuid) {
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
}
