package com.winteree.gateway.client;

import com.winteree.api.service.WintereeCoreService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>Title: WintereeCoreServiceClient</p>
 * <p>Description: 核心服务客户端接口</p>
 *
 * @author RenFei
 * @date : 2020-04-13 22:05
 */
@FeignClient("WinterEE-Core-Serve")
public interface WintereeCoreServiceClient extends WintereeCoreService {
}
