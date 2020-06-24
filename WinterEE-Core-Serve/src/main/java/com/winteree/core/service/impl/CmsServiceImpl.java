package com.winteree.core.service.impl;

import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.CmsService;
import org.springframework.stereotype.Service;

/**
 * <p>Title: CmsServiceImpl</p>
 * <p>Description: 内容管理服务</p>
 *
 * @author RenFei
 * @date : 2020-06-24 20:28
 */
@Service
public class CmsServiceImpl extends BaseService implements CmsService {
    protected CmsServiceImpl(WintereeCoreConfig wintereeCoreConfig) {
        super(wintereeCoreConfig);
    }
}
