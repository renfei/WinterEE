package com.winteree.core.service.impl;

import com.winteree.api.entity.ReportPublicKeyVO;
import com.winteree.api.exception.FailureException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.SecretKeyDOMapper;
import com.winteree.core.dao.entity.SecretKeyDOExample;
import com.winteree.core.dao.entity.SecretKeyDOWithBLOBs;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.I18nMessageService;
import com.winteree.core.service.SecretKeyService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 秘钥服务
 *
 * @author RenFei
 */
@Slf4j
@Service
public class SecretKeyServiceImpl extends BaseService implements SecretKeyService {
    private final I18nMessageService i18NService;
    private final SecretKeyDOMapper secretKeyDOMapper;

    protected SecretKeyServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                   I18nMessageService i18NService,
                                   SecretKeyDOMapper secretKeyDOMapper) {
        super(wintereeCoreConfig);
        this.i18NService = i18NService;
        this.secretKeyDOMapper = secretKeyDOMapper;
    }

    @Override
    public Map<Integer, String> secretKey() {
        Map<Integer, String> map = RSAUtils.genKeyPair(2048);
        if (BeanUtils.isEmpty(map)) {
            return null;
        }
        //保存
        String uuid = UUID.randomUUID().toString();
        SecretKeyDOWithBLOBs secretKeyDO = Builder.of(SecretKeyDOWithBLOBs::new)
                .with(SecretKeyDOWithBLOBs::setUuid, uuid)
                .with(SecretKeyDOWithBLOBs::setCreateTime, new Date())
                .with(SecretKeyDOWithBLOBs::setPrivateKey, map.get(1))
                .with(SecretKeyDOWithBLOBs::setPublicKey, map.get(0))
                .build();
        secretKeyDOMapper.insertSelective(secretKeyDO);
        map.put(1, uuid);
        return map;
    }

    @Override
    public Map<String, String> setSecretKey(ReportPublicKeyVO reportPublicKeyVO) throws FailureException {
        SecretKeyDOExample secretKeyDOExample = new SecretKeyDOExample();
        secretKeyDOExample.createCriteria()
                .andUuidEqualTo(reportPublicKeyVO.getSecretKeyId());
        SecretKeyDOWithBLOBs secretKeyDO = ListUtils.getOne(secretKeyDOMapper.selectByExampleWithBLOBs(secretKeyDOExample));
        if (BeanUtils.isEmpty(secretKeyDO)) {
            throw new FailureException("secretKeyId不正确");
        }
        String clentKey = null;
        try {
            clentKey = RSAUtils.decrypt(reportPublicKeyVO.getPublicKey(), secretKeyDO.getPrivateKey());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("publicKey解密失败");
        }
        String aes = StringUtils.getRandomString(16);
        String aesEnc;
        try {
            aesEnc = RSAUtils.encrypt(aes, clentKey.replaceAll("\n", ""));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("服务器内部错误，使用RSA客户端公钥加密失败");
        }
        //保存AES
        String uuid = UUID.randomUUID().toString();
        SecretKeyDOWithBLOBs secretKeyDTO = Builder.of(SecretKeyDOWithBLOBs::new)
                .with(SecretKeyDOWithBLOBs::setUuid, uuid)
                .with(SecretKeyDOWithBLOBs::setCreateTime, new Date())
                .with(SecretKeyDOWithBLOBs::setPrivateKey, aes)
                .build();
        secretKeyDOMapper.insertSelective(secretKeyDTO);
        Map<String, String> map = new HashMap<>();
        map.put("keyid", uuid);
        map.put("aeskey", aesEnc);
        return map;
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

    /**
     * 解密
     *
     * @param value    密文
     * @param language 语言
     * @param keyId    秘钥ID
     * @return 明文
     */
    @Override
    public String decrypt(String value, String language, String keyId) throws FailureException {
        String aesKey = this.getSecretKeyStringById(keyId);
        if (aesKey == null) {
            throw new FailureException(i18NService.getMessage(language, "uaa.aeskeyiddoesnotexist", "AESKeyId不存在"));
        }
        try {
            value = AESUtil.decrypt(value, aesKey);
        } catch (Exception ex) {
            throw new FailureException(i18NService.getMessage(language, "uaa.passworddecryptionfailed", "密码解密失败"));
        }
        return value;
    }
}
