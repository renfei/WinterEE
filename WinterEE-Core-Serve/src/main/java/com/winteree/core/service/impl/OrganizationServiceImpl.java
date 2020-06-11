package com.winteree.core.service.impl;

import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>Title: OrganizationServiceImpl</p>
 * <p>Description: 组织机构服务</p>
 *
 * @author RenFei
 * @date : 2020-06-04 19:35
 */
@Slf4j
@Service
public class OrganizationServiceImpl extends BaseService implements OrganizationService {
    protected OrganizationServiceImpl(WintereeCoreConfig wintereeCoreConfig) {
        super(wintereeCoreConfig);
    }
}
