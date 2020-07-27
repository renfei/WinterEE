package com.winteree.core.service;

import com.winteree.api.entity.IpInfoDTO;
import net.renfei.sdk.entity.APIResult;

/**
 * <p>Title: IpInfoService</p>
 * <p>Description: IP信息服务</p>
 *
 * @author RenFei
 * @date : 2020-07-27 20:48
 */
public interface IpInfoService {
    APIResult<IpInfoDTO> query(String ip);
}
