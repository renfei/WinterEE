package com.winteree.core.service;

import com.winteree.api.entity.ListData;
import com.winteree.api.entity.OAuthClientDTO;
import net.renfei.sdk.entity.APIResult;

/**
 * <p>Title: OAuthClientService</p>
 * <p>Description: OAtuh客户端服务</p>
 *
 * @author RenFei
 * @date : 2020-04-27 14:45
 */
public interface OAuthClientService {
    /**
     * 获取所有OAtuh客户端列表
     *
     * @param page 页数
     * @param rows 容量
     * @return
     */
    APIResult<ListData<OAuthClientDTO>> getOAuthClientAllList(int page, int rows);

    /**
     * 添加OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    APIResult addOAuthClient(OAuthClientDTO oAuthClientDTO);

    /**
     * 修改OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    APIResult updateOAuthClient(OAuthClientDTO oAuthClientDTO);

    /**
     * 删除OAtuh客户端
     *
     * @param clientId clientId
     * @return
     */
    APIResult deleteOAuthClient(String clientId);
}
