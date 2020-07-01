package com.winteree.core.service;

/**
 * <p>Title: I18nMessageService</p>
 * <p>Description: 多国语言服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 12:59
 */
public interface I18nMessageService {
    String getMessage(String language, String message, String defaultMessage);
}
