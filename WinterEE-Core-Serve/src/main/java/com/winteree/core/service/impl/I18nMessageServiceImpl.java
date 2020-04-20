package com.winteree.core.service.impl;

import com.winteree.api.utils.I18nMessageUtil;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.I18nMessageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 多国语言服务
 *
 * @author RenFei
 */
@Service
public class I18nMessageServiceImpl extends BaseService implements I18nMessageService {

    protected I18nMessageServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                     AccountService accountService) {
        super(accountService, wintereeCoreConfig);
    }

    @Override
    public String getMessage(String language, String message, String defaultMessage) {
        if (StringUtils.isEmpty(language)) {
            return defaultMessage;
        }
        switch (language.toLowerCase()) {
            case "zh-cn":
                language = "zh_cn";
                break;
            case "en-us":
                language = "en_us";
                break;
            case "ja-jp":
                language = "ja_jp";
                break;
            default:
                break;
        }
        try {
            return I18nMessageUtil.getMessage(language, message, defaultMessage);
        } catch (IOException e) {
            return defaultMessage;
        }
    }
}
