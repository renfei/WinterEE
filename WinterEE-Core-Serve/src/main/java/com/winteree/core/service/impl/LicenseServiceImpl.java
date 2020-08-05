package com.winteree.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.winteree.api.entity.LicenseDTO;
import com.winteree.api.entity.LicenseType;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.CoreSettingService;
import com.winteree.core.service.LicenseService;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.RSAUtils;
import org.springframework.stereotype.Service;

/**
 * <p>Title: LicenseServiceImpl</p>
 * <p>Description: 授权服务</p>
 *
 * @author RenFei
 * @date : 2020-08-05 21:37
 */
@Service
public class LicenseServiceImpl extends BaseService implements LicenseService {
    private final CoreSettingService coreSettingService;
    private final static String SECRET_KEY = "";

    protected LicenseServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                 CoreSettingService coreSettingService) {
        super(wintereeCoreConfig);
        this.coreSettingService = coreSettingService;
    }

    @Override
    public LicenseDTO getLicense() {
        String license = coreSettingService.getLicense();
        if (BeanUtils.isEmpty(license)) {
            return Builder.of(LicenseDTO::new)
                    .with(LicenseDTO::setLicenseType, LicenseType.COMMUNITY)
                    .with(LicenseDTO::setLicenseSerialNumber, "")
                    .with(LicenseDTO::setSystemName, "WinterEE")
                    .build();
        }
        try {
            String licenseJson = RSAUtils.encrypt(license, SECRET_KEY);
            return JSON.parseObject(licenseJson, LicenseDTO.class);
        } catch (Exception exception) {
            return Builder.of(LicenseDTO::new)
                    .with(LicenseDTO::setLicenseType, LicenseType.COMMUNITY)
                    .with(LicenseDTO::setLicenseSerialNumber, "")
                    .with(LicenseDTO::setSystemName, "WinterEE")
                    .build();
        }
    }
}
