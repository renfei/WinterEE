package com.winteree.core.service;

import com.winteree.api.utils.I18nMessageUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 多国语言服务
 *
 * @author RenFei
 */
@Service
public class I18nMessageService {
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
