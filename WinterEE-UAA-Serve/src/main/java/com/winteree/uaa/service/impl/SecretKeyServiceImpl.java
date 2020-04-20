package com.winteree.uaa.service.impl;

import com.winteree.uaa.dao.SecretKeyDOMapper;
import com.winteree.uaa.dao.entity.SecretKeyDOExample;
import com.winteree.uaa.dao.entity.SecretKeyDOWithBLOBs;
import com.winteree.uaa.service.SecretKeyService;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

/**
 * 秘钥服务
 *
 * @author RenFei
 */
@Service
public class SecretKeyServiceImpl implements SecretKeyService {
    private final SecretKeyDOMapper secretKeyDOMapper;

    public SecretKeyServiceImpl(SecretKeyDOMapper secretKeyDOMapper) {
        this.secretKeyDOMapper = secretKeyDOMapper;
    }

    @Override
    public String getSecretKeyStringById(String id) {
        SecretKeyDOExample secretKeyDOExample = new SecretKeyDOExample();
        secretKeyDOExample.createCriteria()
                .andUuidEqualTo(id);
        SecretKeyDOWithBLOBs secretKeyDOWithBLOBs = ListUtils.getOne(secretKeyDOMapper.selectByExampleWithBLOBs(secretKeyDOExample));
        if (BeanUtils.isEmpty(secretKeyDOWithBLOBs)) {
            return null;
        } else {
            return secretKeyDOWithBLOBs.getPrivateKey();
        }
    }
}
