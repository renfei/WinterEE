package com.winteree.core.service;

import com.winteree.api.entity.LicenseDTO;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;

import java.io.IOException;

/**
 * <p>Title: LicenseService</p>
 * <p>Description: 授权服务</p>
 *
 * @author RenFei
 * @date : 2020-08-05 21:37
 */
public interface LicenseService {
    LicenseDTO getLicense();

    String getMachineCode();

    void saveLicense(String license) throws ForbiddenException, FailureException, IOException;
}
