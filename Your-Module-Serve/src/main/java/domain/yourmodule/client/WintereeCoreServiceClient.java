package domain.yourmodule.client;

import com.winteree.api.service.WintereeCoreService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * <p>Title: WintereeCoreServiceClient</p>
 * <p>Description: 核心服务接口</p>
 *
 * @author RenFei
 * @date : 2020-06-24 20:07
 */
@FeignClient("WinterEE-Core-Serve")
public interface WintereeCoreServiceClient extends WintereeCoreService {
}
