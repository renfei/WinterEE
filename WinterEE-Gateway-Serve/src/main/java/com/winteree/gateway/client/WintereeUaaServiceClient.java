package com.winteree.gateway.client;

import com.winteree.api.service.WintereeUaaService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>Title: WintereeCoreServiceClient</p>
 * <p>Description: 核心服务客户端接口</p>
 *
 * @author RenFei
 * @date : 2020-07-15 12:57:05
 */
@FeignClient("WinterEE-UAA-Serve")
public interface WintereeUaaServiceClient extends WintereeUaaService {
}
