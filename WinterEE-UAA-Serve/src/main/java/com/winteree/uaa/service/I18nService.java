package com.winteree.uaa.service;

/**
 * <p>Title: I18nServiceImpl</p>
 * <p>Description: 国际化语言服务</p>
 *
 * @author RenFei
 * @date : 2020-04-18 21:30
 */
public interface I18nService {
    String getMessage(String language, String message, String defaultMessage);
}
