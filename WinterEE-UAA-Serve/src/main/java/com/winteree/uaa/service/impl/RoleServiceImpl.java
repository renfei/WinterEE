package com.winteree.uaa.service.impl;

import com.winteree.uaa.dao.UserRoleDOMapper;
import com.winteree.uaa.dao.entity.UserRoleDO;
import com.winteree.uaa.dao.entity.UserRoleDOExample;
import com.winteree.uaa.service.RoleService;
import net.renfei.sdk.utils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: RoleServiceImpl</p>
 * <p>Description: 角色服务</p>
 *
 * @author RenFei
 * @date : 2020-04-20 13:04
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final UserRoleDOMapper userRoleDOMapper;

    public RoleServiceImpl(UserRoleDOMapper userRoleDOMapper) {
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
