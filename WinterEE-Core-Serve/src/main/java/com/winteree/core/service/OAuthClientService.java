package com.winteree.core.service;

import com.winteree.api.entity.ListData;
import com.winteree.api.entity.OAuthClientDTO;
import com.winteree.api.exception.FailureException;

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
    ListData<OAuthClientDTO> getOAuthClientAllList(int page, int rows) throws FailureException;

    /**
     * 添加OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    String addOAuthClient(OAuthClientDTO oAuthClientDTO) throws FailureException;

    /**
     * 修改OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    int updateOAuthClient(OAuthClientDTO oAuthClientDTO) throws FailureException;

    /**
     * 删除OAtuh客户端
     *
     * @param clientId clientId
     * @return
     */
    int deleteOAuthClient(String clientId) throws FailureException;
}
