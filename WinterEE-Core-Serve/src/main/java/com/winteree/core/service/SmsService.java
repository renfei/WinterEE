package com.winteree.core.service;

import com.winteree.api.entity.Sms;

/**
 * <p>Title: SmsService</p>
 * <p>Description: 短信服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 17:46
 */
public interface SmsService {
    String sendSms(Sms sms);
}
