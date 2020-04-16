package com.winteree.core.service;

import com.winteree.api.entity.ReportPublicKeyVO;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.RSAUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>Title: SecretKeyServiceTest</p>
 * <p>Description: 秘钥服务测试</p>
 *
 * @author RenFei
 * @date : 2020-04-16 20:40
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test SecretKeyService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class SecretKeyServiceTest {
    private final SecretKeyService secretKeyService;

    @Autowired
    public SecretKeyServiceTest(SecretKeyService secretKeyService) {
        this.secretKeyService = secretKeyService;
    }

    @Test
    @Rollback
    @Transactional
    public void secretKeyTest() throws Exception {
        Map<Integer, String> serverKeyMap = secretKeyService.secretKey();
        Assertions.assertNotNull(serverKeyMap);
        Map<Integer, String> clientKeyMap = RSAUtils.genKeyPair(1024);
        String serverPubKey = serverKeyMap.get(0);
        String clientPubKey = clientKeyMap.get(0);
        String clientPriKey = clientKeyMap.get(1);
        ReportPublicKeyVO reportPublicKeyVO = new ReportPublicKeyVO();
        reportPublicKeyVO.setSecretKeyId("tests");
        reportPublicKeyVO.setPublicKey("tests");
        Assertions.assertEquals(secretKeyService.setSecretKey(reportPublicKeyVO).getCode(), StateCode.BadRequest.getCode());
        reportPublicKeyVO.setSecretKeyId(serverKeyMap.get(1));
        Assertions.assertEquals(secretKeyService.setSecretKey(reportPublicKeyVO).getCode(), StateCode.BadRequest.getCode());
        reportPublicKeyVO.setPublicKey(RSAUtils.encrypt("test", serverPubKey));
        Assertions.assertEquals(secretKeyService.setSecretKey(reportPublicKeyVO).getCode(), StateCode.Error.getCode());
        reportPublicKeyVO.setPublicKey(RSAUtils.encrypt(clientPubKey, serverPubKey));
        APIResult apiResult = secretKeyService.setSecretKey(reportPublicKeyVO);
        Assertions.assertEquals(apiResult.getCode(), StateCode.OK.getCode());
        Map<String, String> apiResultMap = (Map<String, String>) apiResult.getData();
        Assertions.assertNotNull(RSAUtils.decrypt(apiResultMap.get("aeskey"), clientPriKey));
    }
}
