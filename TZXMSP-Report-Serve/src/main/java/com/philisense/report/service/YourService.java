package com.philisense.report.service;

import com.philisense.report.client.WintereeCoreServiceClient;
import com.winteree.api.entity.AccountDTO;
import org.springframework.stereotype.Service;

/**
 * <p>Title: YourService</p>
 * <p>Description: 你的服务</p>
 *
 * @author RenFei
 * @date : 2020-07-06 18:08:43
 */
@Service
public class YourService extends BaseService {
    protected YourService(WintereeCoreServiceClient wintereeCoreServiceClient) {
        super(wintereeCoreServiceClient);
    }

    /**
     * 演示方法
     * 这里为你展示调用 WinterEE核心服务获取当前登陆的用户
     *
     * @return
     */
    public String demoMethod() {
        // 这里为你展示调用 WinterEE核心服务获取当前登陆的用户
        AccountDTO accountDTO = getSignedUser();
        if (accountDTO == null) {
            return "This is [domain.yourmodule.service.YourService.demoMethod].";
        } else {
            return "Welcome " + accountDTO.getUserName() + ".This is [domain.yourmodule.service.YourService.demoMethod].";
        }
    }
}
