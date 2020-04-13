package com.winteree.gateway.client;

import com.winteree.api.service.WintereeCoreService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author RenFei
 */
@FeignClient("WinterEE-Core-Serve")
public interface WintereeCoreServiceClient extends WintereeCoreService {
}
