package com.winteree.core.service;

import java.util.List;

/**
 * <p>Title: CoreSettingService</p>
 * <p>Description: 核心设置服务</p>
 *
 * @author RenFei
 * @date : 2020-08-05 22:31
 */
public interface CoreSettingService {
    String getLicense();
    int setLicense(String license);
    int addValues(String key, List<String> values);
    int updateByKey(String key, String value);
    int deleteByKey(String key);
    List<String> getValuesByKey(String key);
}
