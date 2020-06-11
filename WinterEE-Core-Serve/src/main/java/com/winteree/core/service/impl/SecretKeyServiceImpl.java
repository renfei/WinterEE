package com.winteree.core.service.impl;

import com.winteree.api.entity.ReportPublicKeyVO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.SecretKeyDOMapper;
import com.winteree.core.dao.entity.SecretKeyDOExample;
import com.winteree.core.dao.entity.SecretKeyDOWithBLOBs;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.SecretKeyService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
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
    private final SecretKeyDOMapper secretKeyDOMapper;

    protected SecretKeyServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                   SecretKeyDOMapper secretKeyDOMapper) {
        super(wintereeCoreConfig);
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
    public APIResult setSecretKey(ReportPublicKeyVO reportPublicKeyVO) {
        SecretKeyDOExample secretKeyDOExample = new SecretKeyDOExample();
        secretKeyDOExample.createCriteria()
                .andUuidEqualTo(reportPublicKeyVO.getSecretKeyId());
        SecretKeyDOWithBLOBs secretKeyDO = ListUtils.getOne(secretKeyDOMapper.selectByExampleWithBLOBs(secretKeyDOExample));
        if (BeanUtils.isEmpty(secretKeyDO)) {
            return APIResult.builder()
                    .code(StateCode.BadRequest)
                    .message("secretKeyId不正确")
                    .build();
        }
        String clentKey = null;
        try {
            clentKey = RSAUtils.decrypt(reportPublicKeyVO.getPublicKey(), secretKeyDO.getPrivateKey());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return APIResult.builder()
                    .code(StateCode.BadRequest)
                    .message("publicKey解密失败")
                    .build();
        }
        String aes = StringUtils.getRandomString(16);
        String aesEnc;
        try {
            aesEnc = RSAUtils.encrypt(aes, clentKey.replaceAll("\n", ""));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return APIResult.builder()
                    .code(StateCode.Error)
                    .message("服务器内部错误，使用RSA客户端公钥加密失败")
                    .build();
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
        return APIResult.builder()
                .code(StateCode.OK)
                .message("成功！")
                .data(map)
                .build();
    }
}
