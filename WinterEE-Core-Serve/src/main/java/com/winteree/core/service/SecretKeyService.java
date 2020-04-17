package com.winteree.core.service;

import com.winteree.api.entity.ReportPublicKeyVO;
import net.renfei.sdk.entity.APIResult;

import java.util.Map;

/**
 * <p>Title: SecretKeyService</p>
 * <p>Description: 秘钥服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 13:04
 */
public interface SecretKeyService {
    Map<Integer, String> secretKey();
    APIResult setSecretKey(ReportPublicKeyVO reportPublicKeyVO);
}
