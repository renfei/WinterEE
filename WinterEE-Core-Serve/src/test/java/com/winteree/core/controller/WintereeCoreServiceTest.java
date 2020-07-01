package com.winteree.core.controller;

import com.winteree.api.entity.*;
import com.winteree.api.service.WintereeCoreService;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.RSAUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * <p>Title: WintereeCoreServiceTest</p>
 * <p>Description: 核心服务测试</p>
 *
 * @author RenFei
 * @date : 2020-04-16 21:15
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test WintereeCoreService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class WintereeCoreServiceTest {
    private final WintereeCoreService wintereeCoreService;

    @Autowired
    public WintereeCoreServiceTest(WintereeCoreService wintereeCoreServiceImpl) {
        this.wintereeCoreService = wintereeCoreServiceImpl;
    }

    @BeforeAll
    static void init() {
        String[] authorities = new String[]{
                "signed",
        };
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken("9369919A-F95E-44CF-AB0A-6BCD1D933403", "N/A", AuthorityUtils.createAuthorityList(authorities));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource());
        //将authenticationToken填充到安全上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    public void getMessageTest() {
        String message = "test.testmessage";
        Assertions.assertEquals(wintereeCoreService.getMessage("zh-cn", message, "默认测试消息"), "测试消息");
        Assertions.assertEquals(wintereeCoreService.getMessage("en-us", message, "默认测试消息"), "Test message");
        Assertions.assertEquals(wintereeCoreService.getMessage("ja-jp", message, "默认测试消息"), "テストメッセージ");
        Assertions.assertEquals(wintereeCoreService.getMessage("tester", message, "默认测试消息"), "默认测试消息");
        Assertions.assertEquals(wintereeCoreService.getMessage("", message, "默认测试消息"), "默认测试消息");
        Assertions.assertEquals(wintereeCoreService.getMessage(null, message, "默认测试消息"), "默认测试消息");
    }

    //<editor-fold desc="秘钥类的接口" defaultstate="collapsed">
    @Test
    @Rollback
    @Transactional
    public void logTest() {
        Assertions.assertEquals(wintereeCoreService.log(null).getCode(), 100);
        LogDTO logDTO = new LogDTO();
        Assertions.assertEquals(wintereeCoreService.log(logDTO).getCode(), 100);
        logDTO.setUuid(UUID.randomUUID().toString());
        logDTO.setCreateTime(new Date());
        logDTO.setLogType(LogTypeEnum.ACCESS);
        Assertions.assertEquals(wintereeCoreService.log(logDTO).getCode(), 200);
        logDTO.setUuid(UUID.randomUUID().toString());
        logDTO.setCreateTime(new Date());
        logDTO.setLogType(LogTypeEnum.ACCESS);
        logDTO.setLogSubType(LogSubTypeEnum.DEBUG);
        Assertions.assertEquals(wintereeCoreService.log(logDTO).getCode(), 200);
    }

    @Test
    @Rollback
    @Transactional
    public void secretKeyTest() throws Exception {
        APIResult apiResult = wintereeCoreService.secretKey();
        Assertions.assertNotNull(apiResult);
        Map<Integer, String> clientKeyMap = RSAUtils.genKeyPair(1024);
        String serverPubKey = (String) apiResult.getData();
        String clientPubKey = clientKeyMap.get(0);
        String clientPriKey = clientKeyMap.get(1);
        ReportPublicKeyVO reportPublicKeyVO = new ReportPublicKeyVO();
        reportPublicKeyVO.setSecretKeyId("tests");
        reportPublicKeyVO.setPublicKey("tests");
        Assertions.assertEquals(wintereeCoreService.setSecretKey(reportPublicKeyVO).getCode(), StateCode.BadRequest.getCode());
        reportPublicKeyVO.setSecretKeyId(apiResult.getMessage());
        Assertions.assertEquals(wintereeCoreService.setSecretKey(reportPublicKeyVO).getCode(), StateCode.BadRequest.getCode());
        reportPublicKeyVO.setPublicKey(RSAUtils.encrypt("test", serverPubKey));
        Assertions.assertEquals(wintereeCoreService.setSecretKey(reportPublicKeyVO).getCode(), StateCode.Error.getCode());
        reportPublicKeyVO.setPublicKey(RSAUtils.encrypt(clientPubKey, serverPubKey));
        APIResult setSecretKeyResult = wintereeCoreService.setSecretKey(reportPublicKeyVO);
        Assertions.assertEquals(setSecretKeyResult.getCode(), StateCode.OK.getCode());
        Map<String, String> apiResultMap = (Map<String, String>) setSecretKeyResult.getData();
        Assertions.assertNotNull(RSAUtils.decrypt(apiResultMap.get("aeskey"), clientPriKey));
    }
    //</editor-fold>

    //<editor-fold desc="账户类的接口" defaultstate="collapsed">
    @Test
    public void getAccountIdByUserNameTest() {
        Assertions.assertNull(wintereeCoreService.findAccountByUsername(null));
        Assertions.assertNull(wintereeCoreService.findAccountByUsername(""));
        Assertions.assertNull(wintereeCoreService.findAccountByUsername("tests"));
        Assertions.assertNotNull(wintereeCoreService.findAccountByUsername("admin"));
    }

    @Test
    public void getAccountIdByUuidTest() {
        Assertions.assertNull(wintereeCoreService.findAccountByUuid(null));
        Assertions.assertNull(wintereeCoreService.findAccountByUuid(""));
        Assertions.assertNull(wintereeCoreService.findAccountByUuid("tests"));
        Assertions.assertNotNull(wintereeCoreService.findAccountByUuid("9369919A-F95E-44CF-AB0A-6BCD1D933403"));
    }

    @Test
    public void getAccountIdByEmailTest() {
        Assertions.assertNull(wintereeCoreService.findAccountByEmail(null));
        Assertions.assertNull(wintereeCoreService.findAccountByEmail(""));
        Assertions.assertNull(wintereeCoreService.findAccountByEmail("tests"));
        Assertions.assertNotNull(wintereeCoreService.findAccountByEmail("i@renfei.net"));
        Assertions.assertNotNull(wintereeCoreService.findAccountByEmail("I@RENFEI.NET"));
    }

    @Test
    public void getAccountIdByPhoneTest() {
        Assertions.assertNull(wintereeCoreService.findAccountByPhoneNumber(null));
        Assertions.assertNull(wintereeCoreService.findAccountByPhoneNumber(""));
        Assertions.assertNull(wintereeCoreService.findAccountByPhoneNumber("tests"));
        Assertions.assertNotNull(wintereeCoreService.findAccountByPhoneNumber("13001000000"));
    }

    @Test
    public void createTotpTest() {
        Assertions.assertEquals(wintereeCoreService.createTotp("admin").getCode(), StateCode.OK.getCode());
        Assertions.assertEquals(wintereeCoreService.createTotp("").getCode(), StateCode.OK.getCode());
        Assertions.assertEquals(wintereeCoreService.createTotp(null).getCode(), StateCode.OK.getCode());
    }

    @Test
    public void checkAccountTest() {
        Assertions.assertEquals(wintereeCoreService.checkAccount(null, null).getCode(), StateCode.Failure.getCode());
        Assertions.assertEquals(wintereeCoreService.checkAccount(null, "en-US").getCode(), StateCode.Failure.getCode());
        Assertions.assertEquals(wintereeCoreService.checkAccount("i@renfei.net", "zh-CN").getMessage(), "Email");
        Assertions.assertEquals(wintereeCoreService.checkAccount("13001000000", "zh-CN").getMessage(), "Phone");
        Assertions.assertEquals(wintereeCoreService.checkAccount("admin", "zh-CN").getMessage(), "UserName");
        Assertions.assertEquals(wintereeCoreService.checkAccount("tester", "zh-CN").getCode(), StateCode.Failure.getCode());
    }

    @Test
    public void getMyInfoTest(){
        Assertions.assertEquals(wintereeCoreService.getMyInfo().getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void changePasswordTest(){
        Assertions.assertEquals(wintereeCoreService.changePassword("yYtZkSpPI2zMEETWGV3ZoQ==","a8Ywm0Qx5SAGxiahG5H6tQ==","","64a01c36-f863-494d-80ab-85439c512536").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void passwordResetTest(){
        PasswordResetDAT passwordResetDAT = new PasswordResetDAT();
        passwordResetDAT.setAccountUuid("");
        passwordResetDAT.setKeyid("64a01c36-f863-494d-80ab-85439c512536");
        passwordResetDAT.setLanguage("9369919A-F95E-44CF-AB0A-6BCD1D933403");
        passwordResetDAT.setNewPassword("a8Ywm0Qx5SAGxiahG5H6tQ");
        Assertions.assertEquals(wintereeCoreService.passwordReset(passwordResetDAT).getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getAccountListTest(){
        AccountSearchCriteriaVO accountSearchCriteriaVO = new AccountSearchCriteriaVO();
        accountSearchCriteriaVO.setTenantuuid("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189");
        accountSearchCriteriaVO.setAccountUuid("9369919A-F95E-44CF-AB0A-6BCD1D933403");
        accountSearchCriteriaVO.setEmail("i@renfei.net");
        accountSearchCriteriaVO.setPages(1);
        accountSearchCriteriaVO.setRows(10);
        accountSearchCriteriaVO.setPhone("13001000000");
        accountSearchCriteriaVO.setUserName("admin");
        Assertions.assertEquals(wintereeCoreService.getAccountList(accountSearchCriteriaVO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addAccountTest(){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail("ads@ssdf.com");
        accountDTO.setPhone("13001000000");
        accountDTO.setUserName("Tester");
        accountDTO.setTenantUuid("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189");
        Assertions.assertEquals(wintereeCoreService.addAccount(accountDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateAccountTest(){
        AccountDTO accountDTO = wintereeCoreService.findAccountByUuid("9369919A-F95E-44CF-AB0A-6BCD1D933403");
        accountDTO.setUserName("tetete");
        accountDTO.setEmail("dfs@wre.com");
        accountDTO.setPhone("13101000000");
        Assertions.assertEquals(wintereeCoreService.updateAccount(accountDTO).getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>

    //<editor-fold desc="菜单类的接口" defaultstate="collapsed">
    @Test
    public void getMenuTreeTest(){
        Assertions.assertEquals(wintereeCoreService.getMenuTree("").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getMenuAndAuthorityTreeTest(){
        Assertions.assertEquals(wintereeCoreService.getMenuAndAuthorityTree("").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getSettingMenuTreeTest(){
        Assertions.assertEquals(wintereeCoreService.getSettingMenuTree().getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getSettingMenuListTest(){
        Assertions.assertEquals(wintereeCoreService.getSettingMenuList().getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getSettingMenuTest(){
        Assertions.assertEquals(wintereeCoreService.getSettingMenu("99406120-183c-4029-a992-1c707aba5ba2").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteSettingMenuByUuidTest(){
        Assertions.assertEquals(wintereeCoreService.deleteSettingMenuByUuid("99406120-183c-4029-a992-1c707aba5ba2").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateSettingMenuTest(){
        MenuVO menuVO = wintereeCoreService.getSettingMenu("99406120-183c-4029-a992-1c707aba5ba2").getData();
        menuVO.setIcon("aaa");
        menuVO.setText("Test");
        menuVO.setHref("Test");
        menuVO.setI18n("Test");
        menuVO.setIcondown("Test");
        menuVO.setIsMenu(false);
        menuVO.setParentUuid("root");
        menuVO.setRemarks("Test");
        menuVO.setPermission("Test");
        menuVO.setSort(1L);
        menuVO.setTarget("Test");
        menuVO.setModel(false);
        Assertions.assertEquals(wintereeCoreService.updateSettingMenu(menuVO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addSettingMenuTest(){
        MenuVO menuVO = new MenuVO();
        menuVO.setIcon("aaa");
        menuVO.setText("Test");
        menuVO.setHref("Test");
        menuVO.setI18n("Test");
        menuVO.setIcondown("Test");
        menuVO.setIsMenu(false);
        menuVO.setParentUuid("root");
        menuVO.setRemarks("Test");
        menuVO.setPermission("Test");
        menuVO.setSort(1L);
        menuVO.setTarget("Test");
        menuVO.setModel(false);
        Assertions.assertEquals(wintereeCoreService.addSettingMenu(menuVO).getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>
}
