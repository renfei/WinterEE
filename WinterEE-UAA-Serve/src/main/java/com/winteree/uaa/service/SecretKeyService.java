package com.winteree.uaa.service;

import com.winteree.uaa.dao.SecretKeyDOMapper;
import com.winteree.uaa.dao.entity.SecretKeyDO;
import com.winteree.uaa.dao.entity.SecretKeyDOExample;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 秘钥服务
 *
 * @author RenFei
 */
@Service
public class SecretKeyService {
    @Autowired
    private SecretKeyDOMapper secretKeyDOMapper;

    public String getSecretKeyStringById(String id) {
        SecretKeyDOExample secretKeyDOExample = new SecretKeyDOExample();
        secretKeyDOExample.createCriteria()
                .andIdEqualTo(id);
        SecretKeyDO secretKeyDO = ListUtils.getOne(secretKeyDOMapper.selectByExampleWithBLOBs(secretKeyDOExample));
        if (BeanUtils.isEmpty(secretKeyDO)) {
            return null;
        } else {
            return secretKeyDO.getPrivateKey();
        }
    }
}
