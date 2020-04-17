package com.winteree.core.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.winteree.api.entity.LogDTO;
import com.winteree.api.entity.LogSubTypeEnum;
import com.winteree.api.entity.LogTypeEnum;
import com.winteree.api.entity.Sms;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.LogService;
import com.winteree.core.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.Builder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * <p>Title: AliyunSmsServiceImpl</p>
 * <p>Description: 阿里云短信服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 17:48
 */
@Slf4j
@Service
public class AliyunSmsServiceImpl extends BaseService implements SmsService {
    private final WintereeCoreConfig wintereeCoreConfig;
    private final LogService logService;

    public AliyunSmsServiceImpl(WintereeCoreConfig wintereeCoreConfig, LogService logService) {
        this.wintereeCoreConfig = wintereeCoreConfig;
        this.logService = logService;
    }

    @Override
    public String sendSms(Sms sms) {
        if (wintereeCoreConfig.getEnableSMS()) {
            DefaultProfile profile = DefaultProfile.getProfile(wintereeCoreConfig.getAliyun().getRegionId(), wintereeCoreConfig.getAliyun().getAccessKeyId(), wintereeCoreConfig.getAliyun().getSecret());
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", wintereeCoreConfig.getAliyun().getRegionId());
            request.putQueryParameter("PhoneNumbers", sms.getPhoneNumbers());
            request.putQueryParameter("SignName", sms.getSignName());
            request.putQueryParameter("TemplateCode", sms.getTemplateCode());
            request.putQueryParameter("TemplateParam", sms.getTemplateParam());
            CommonResponse response = null;
            try {
                response = client.getCommonResponse(request);
                LogDTO logDTO = Builder.of(LogDTO::new)
                        .with(LogDTO::setId, UUID.randomUUID().toString())
                        .with(LogDTO::setDateTime, new Date())
                        .with(LogDTO::setLogType, LogTypeEnum.SYSTEM)
                        .with(LogDTO::setLogSubType, LogSubTypeEnum.SMS)
                        .with(LogDTO::setTenantId, sms.getTenantId())
                        .with(LogDTO::setLogValue, String.format("Send SMS ->%s Success, Content:%s",
                                sms.getPhoneNumbers(), sms.getTemplateParam()))
                        .with(LogDTO::setId, UUID.randomUUID().toString())
                        .build();
                logService.log(logDTO);
                return response.getData();
            } catch (ClientException e) {
                log.error(e.getMessage());
                LogDTO logDTO = Builder.of(LogDTO::new)
                        .with(LogDTO::setId, UUID.randomUUID().toString())
                        .with(LogDTO::setDateTime, new Date())
                        .with(LogDTO::setLogType, LogTypeEnum.SYSTEM)
                        .with(LogDTO::setLogSubType, LogSubTypeEnum.ERROR)
                        .with(LogDTO::setTenantId, sms.getTenantId())
                        .with(LogDTO::setLogValue, e.getMessage())
                        .with(LogDTO::setId, UUID.randomUUID().toString())
                        .build();
                logService.log(logDTO);
                return null;
            }
        } else {
            log.error("Send SMS ->{} Fail, SMS System Not Enable. Content:{}",
                    sms.getPhoneNumbers(), sms.getTemplateParam());
            LogDTO logDTO = Builder.of(LogDTO::new)
                    .with(LogDTO::setId, UUID.randomUUID().toString())
                    .with(LogDTO::setDateTime, new Date())
                    .with(LogDTO::setLogType, LogTypeEnum.SYSTEM)
                    .with(LogDTO::setLogSubType, LogSubTypeEnum.ERROR)
                    .with(LogDTO::setTenantId, sms.getTenantId())
                    .with(LogDTO::setLogValue, String.format("Send SMS ->%s Fail, SMS System Not Enable. Content:%s",
                            sms.getPhoneNumbers(), sms.getTemplateParam()))
                    .with(LogDTO::setId, UUID.randomUUID().toString())
                    .build();
            logService.log(logDTO);
            return null;
        }
    }
}
