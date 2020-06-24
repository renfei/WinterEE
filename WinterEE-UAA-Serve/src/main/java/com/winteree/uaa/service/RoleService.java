package com.winteree.uaa.service;

import java.util.List;

/**
 * <p>Title: RoleService</p>
 * <p>Description: 角色服务</p>
 *
 * @author RenFei
 * @date : 2020-04-20 13:02
 */
public interface RoleService {
    /**
     * 根据用户UUID获取角色列表UUID
     *
     * @param userUuid 用户UUID
     * @return 角色列表UUID
     */
    List<String> selectRoleUuidByUserUuid(String userUuid);
}
