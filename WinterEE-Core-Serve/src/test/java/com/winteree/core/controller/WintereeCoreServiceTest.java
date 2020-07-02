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

import java.util.ArrayList;
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
    public void getMyInfoTest() {
        Assertions.assertEquals(wintereeCoreService.getMyInfo().getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void changePasswordTest() {
        Assertions.assertEquals(wintereeCoreService.changePassword("yYtZkSpPI2zMEETWGV3ZoQ==", "a8Ywm0Qx5SAGxiahG5H6tQ==", "", "64a01c36-f863-494d-80ab-85439c512536").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void passwordResetTest() {
        PasswordResetDAT passwordResetDAT = new PasswordResetDAT();
        passwordResetDAT.setAccountUuid("");
        passwordResetDAT.setKeyid("64a01c36-f863-494d-80ab-85439c512536");
        passwordResetDAT.setLanguage("9369919A-F95E-44CF-AB0A-6BCD1D933403");
        passwordResetDAT.setNewPassword("a8Ywm0Qx5SAGxiahG5H6tQ");
        Assertions.assertEquals(wintereeCoreService.passwordReset(passwordResetDAT).getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getAccountListTest() {
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
    public void addAccountTest() {
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
    public void updateAccountTest() {
        AccountDTO accountDTO = wintereeCoreService.findAccountByUuid("9369919A-F95E-44CF-AB0A-6BCD1D933403");
        accountDTO.setUserName("tetete");
        accountDTO.setEmail("dfs@wre.com");
        accountDTO.setPhone("13101000000");
        Assertions.assertEquals(wintereeCoreService.updateAccount(accountDTO).getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>

    //<editor-fold desc="菜单类的接口" defaultstate="collapsed">
    @Test
    public void getMenuTreeTest() {
        Assertions.assertEquals(wintereeCoreService.getMenuTree("").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getMenuAndAuthorityTreeTest() {
        Assertions.assertEquals(wintereeCoreService.getMenuAndAuthorityTree("").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getSettingMenuTreeTest() {
        Assertions.assertEquals(wintereeCoreService.getSettingMenuTree().getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getSettingMenuListTest() {
        Assertions.assertEquals(wintereeCoreService.getSettingMenuList().getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getSettingMenuTest() {
        Assertions.assertEquals(wintereeCoreService.getSettingMenu("99406120-183c-4029-a992-1c707aba5ba2").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteSettingMenuByUuidTest() {
        Assertions.assertEquals(wintereeCoreService.deleteSettingMenuByUuid("99406120-183c-4029-a992-1c707aba5ba2").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateSettingMenuTest() {
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
    public void addSettingMenuTest() {
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

    //<editor-fold desc="日志类的接口" defaultstate="collapsed">
    @Test
    public void getLogListTest() {
        Assertions.assertEquals(wintereeCoreService.getLogList(1, 10, "ALL", "ALL", "2000-01-01 00:00:00", "2021-12-31 23:59:59").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getAllLogTypeTest() {
        Assertions.assertEquals(wintereeCoreService.getAllLogType("").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getAllLogSubTypeTest() {
        Assertions.assertEquals(wintereeCoreService.getAllLogSubType("").getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>

    //<editor-fold desc="租户类的接口" defaultstate="collapsed">
    @Test
    public void getTenantListTest() {
        Assertions.assertEquals(wintereeCoreService.getTenantList().getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getAllTenantTest() {
        Assertions.assertEquals(wintereeCoreService.getAllTenant(1, 10).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addTenantTest() {
        TenantDTO tenantDTO = new TenantDTO();
        Assertions.assertEquals(wintereeCoreService.addTenant(tenantDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateTenantTest() {
        TenantDTO tenantDTO = wintereeCoreService.getAllTenant(1, 10).getData().getData().get(0);
        Assertions.assertEquals(wintereeCoreService.addTenant(tenantDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getTenantInfoTest() {
        Assertions.assertEquals(wintereeCoreService.getTenantInfo("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateTenantInfoTest() {
        TenantInfoDTO tenantInfoDTO = wintereeCoreService.getTenantInfo("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189").getData();
        Assertions.assertEquals(wintereeCoreService.updateTenantInfo(tenantInfoDTO).getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>

    //<editor-fold desc="OAtuh类的接口" defaultstate="collapsed">
    @Test
    public void getOAuthClientAllListTest() {
        Assertions.assertEquals(wintereeCoreService.getOAuthClientAllList(1, 10).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addOAuthClientTest() {
        OAuthClientDTO oAuthClientDTO = new OAuthClientDTO();
        oAuthClientDTO.setClientId("Tests");
        oAuthClientDTO.setResourceIds("Tests");
        oAuthClientDTO.setScope("Tests");
        oAuthClientDTO.setAuthorities("Tests");
        oAuthClientDTO.setAuthorizedGrantTypes("Tests");
        oAuthClientDTO.setAutoapprove("Tests");
        oAuthClientDTO.setWebServerRedirectUri("Tests");
        oAuthClientDTO.setAccessTokenValidity(0);
        oAuthClientDTO.setRefreshTokenValidity(300);
        oAuthClientDTO.setAdditionalInformation("Tests");
        oAuthClientDTO.setArchived(new Byte("0"));
        oAuthClientDTO.setTrusted(new Byte("0"));
        oAuthClientDTO.setCreateTime(new Date());
        Assertions.assertEquals(wintereeCoreService.addOAuthClient(oAuthClientDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateOAuthClientTest() {
        OAuthClientDTO oAuthClientDTO = wintereeCoreService.getOAuthClientAllList(1, 10).getData().getData().get(0);
        oAuthClientDTO.setResourceIds("Tests");
        oAuthClientDTO.setScope("Tests");
        oAuthClientDTO.setAuthorities("Tests");
        oAuthClientDTO.setAuthorizedGrantTypes("Tests");
        oAuthClientDTO.setAutoapprove("Tests");
        oAuthClientDTO.setWebServerRedirectUri("Tests");
        oAuthClientDTO.setAccessTokenValidity(0);
        oAuthClientDTO.setRefreshTokenValidity(300);
        oAuthClientDTO.setAdditionalInformation("Tests");
        oAuthClientDTO.setArchived(new Byte("0"));
        oAuthClientDTO.setTrusted(new Byte("0"));
        oAuthClientDTO.setCreateTime(new Date());
        Assertions.assertEquals(wintereeCoreService.updateOAuthClient(oAuthClientDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteOAuthClientTest() {
        Assertions.assertEquals(wintereeCoreService.deleteOAuthClient("Your-Module-Serve").getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>

    //<editor-fold desc="组织机构类的接口" defaultstate="collapsed">
    @Test
    public void getAllOrganizationTreeTest() {
        Assertions.assertEquals(wintereeCoreService.getAllOrganizationTree("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getCompanyListTest() {
        Assertions.assertEquals(wintereeCoreService.getCompanyList("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getCompanySimpleListTest() {
        Assertions.assertEquals(wintereeCoreService.getCompanySimpleList("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getMyCompanyListTest() {
        Assertions.assertEquals(wintereeCoreService.getMyCompanyList("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addCompanyTest() {
        OrganizationVO organizationVO = new OrganizationVO();
        organizationVO.setUuid(UUID.randomUUID().toString().toUpperCase());
        organizationVO.setName("Test");
        organizationVO.setIsTenant(false);
        organizationVO.setCreateTime(new Date());
        organizationVO.setCreateBy("Tester");
        organizationVO.setTenantUuid("Tester");
        organizationVO.setParentUuid("root");
        organizationVO.setOrgType(1);
        organizationVO.setUpdateBy("Tester");
        organizationVO.setUpdateTime(new Date());
        organizationVO.setDelFlag("0");
        Assertions.assertEquals(wintereeCoreService.addCompany(organizationVO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateCompanyTest() {
        OrganizationVO organizationVO = new OrganizationVO();
        organizationVO.setUuid("ed9ee135-029d-42d9-8a1c-b9a43a8c55ea");
        organizationVO.setName("Test");
        organizationVO.setIsTenant(false);
        organizationVO.setCreateTime(new Date());
        organizationVO.setCreateBy("Tester");
        organizationVO.setTenantUuid("Tester");
        organizationVO.setParentUuid("root");
        organizationVO.setOrgType(1);
        organizationVO.setUpdateBy("Tester");
        organizationVO.setUpdateTime(new Date());
        organizationVO.setDelFlag("0");
        Assertions.assertEquals(wintereeCoreService.updateCompany(organizationVO).getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getDepartmentListTest() {
        Assertions.assertEquals(wintereeCoreService.getDepartmentList("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189", "ed9ee135-029d-42d9-8a1c-b9a43a8c55ea").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getDepartmentSimpleListTest() {
        Assertions.assertEquals(wintereeCoreService.getDepartmentSimpleList("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189", "ed9ee135-029d-42d9-8a1c-b9a43a8c55ea").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addDepartmentTest() {
        OrganizationVO organizationVO = new OrganizationVO();
        organizationVO.setUuid(UUID.randomUUID().toString().toUpperCase());
        organizationVO.setName("Test");
        organizationVO.setIsTenant(false);
        organizationVO.setCreateTime(new Date());
        organizationVO.setCreateBy("Tester");
        organizationVO.setTenantUuid("Tester");
        organizationVO.setParentUuid("root");
        organizationVO.setOrgType(2);
        organizationVO.setUpdateBy("Tester");
        organizationVO.setUpdateTime(new Date());
        organizationVO.setDelFlag("0");
        Assertions.assertEquals(wintereeCoreService.addDepartment(organizationVO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateDepartmentTest() {
        OrganizationVO organizationVO = new OrganizationVO();
        organizationVO.setUuid("1106dc4a-4a15-434e-b78d-282a3bcffc71");
        organizationVO.setName("Test");
        organizationVO.setIsTenant(false);
        organizationVO.setCreateTime(new Date());
        organizationVO.setCreateBy("Tester");
        organizationVO.setTenantUuid("Tester");
        organizationVO.setParentUuid("root");
        organizationVO.setOrgType(2);
        organizationVO.setUpdateBy("Tester");
        organizationVO.setUpdateTime(new Date());
        organizationVO.setDelFlag("0");
        Assertions.assertEquals(wintereeCoreService.updateDepartment(organizationVO).getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>

    //<editor-fold desc="角色类的接口" defaultstate="collapsed">
    @Test
    public void getRoleListTest() {
        Assertions.assertEquals(wintereeCoreService.getRoleList("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addRoleTest() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setUuid(UUID.randomUUID().toString().toUpperCase());
        roleDTO.setTenantUuid("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189");
        roleDTO.setName("Test");
        Assertions.assertEquals(wintereeCoreService.addRole(roleDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateRoleTest() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setUuid("997FBB41-0EE2-44ED-B94F-EFDDA316EAA2");
        roleDTO.setTenantUuid("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189");
        roleDTO.setName("Test");
        Assertions.assertEquals(wintereeCoreService.updateRole(roleDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteRoleTest() {
        Assertions.assertEquals(wintereeCoreService.deleteRole("997FBB41-0EE2-44ED-B94F-EFDDA316EAA2").getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>

    //<editor-fold desc="CMS类的接口" defaultstate="collapsed">
    @Test
    public void getCmsSiteListTest() {
        Assertions.assertEquals(wintereeCoreService.getCmsSiteList("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189", 1, 10).getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getCmsSiteByUuidTest() {
        Assertions.assertEquals(wintereeCoreService.getCmsSiteByUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getCmsSiteByDomainTest() {
        Assertions.assertEquals(wintereeCoreService.getCmsSiteByDomain("demo.com").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addCmsSiteTest() {
        CmsSiteDTO cmsSiteDTO = new CmsSiteDTO();
        cmsSiteDTO.setTenantUuid("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189");
        cmsSiteDTO.setSiteName("Test");
        cmsSiteDTO.setSiteDomain("test.com");
        Assertions.assertEquals(wintereeCoreService.addCmsSite(cmsSiteDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateCmsSiteTest() {
        CmsSiteDTO cmsSiteDTO = new CmsSiteDTO();
        cmsSiteDTO.setUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        cmsSiteDTO.setTenantUuid("BC21F895-63DA-4E94-9D9E-D4CD2DCFB189");
        cmsSiteDTO.setSiteName("Test");
        cmsSiteDTO.setSiteKeyword("test.com");
        cmsSiteDTO.setSiteDescription("test.com");
        cmsSiteDTO.setIcpNo("test.com");
        cmsSiteDTO.setGonganNo("test.com");
        Assertions.assertEquals(wintereeCoreService.updateCmsSite(cmsSiteDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteCmsSiteTest() {
        Assertions.assertEquals(wintereeCoreService.deleteCmsSite("1BD49B64-3206-4E27-A4F5-93DD9CD28157").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getCmsCategoryListTest() {
        Assertions.assertEquals(wintereeCoreService.getCmsCategoryList("1BD49B64-3206-4E27-A4F5-93DD9CD28157").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getCmsCategoryByUuidTest() {
        Assertions.assertEquals(wintereeCoreService.getCmsCategoryByUuid("CC7F7D92-3816-4881-BA94-3D29A22C22C9").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addCmsCategoryTest() {
        CmsCategoryDTO cmsCategoryDTO = new CmsCategoryDTO();
        cmsCategoryDTO.setSiteUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        cmsCategoryDTO.setZhName("测试");
        cmsCategoryDTO.setEnName("Test");
        Assertions.assertEquals(wintereeCoreService.addCmsCategory(cmsCategoryDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateCmsCategoryTest() {
        CmsCategoryDTO cmsCategoryDTO = new CmsCategoryDTO();
        cmsCategoryDTO.setUuid("CC7F7D92-3816-4881-BA94-3D29A22C22C9");
        cmsCategoryDTO.setZhName("测试");
        cmsCategoryDTO.setEnName("Test");
        Assertions.assertEquals(wintereeCoreService.updateCmsCategory(cmsCategoryDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteCmsCategoryTest() {
        Assertions.assertEquals(wintereeCoreService.deleteCmsCategory("CC7F7D92-3816-4881-BA94-3D29A22C22C9").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getCmsPostListTest() {
        CmsPostSearchCriteriaVO cmsPostSearchCriteriaVO = new CmsPostSearchCriteriaVO();
        cmsPostSearchCriteriaVO.setSiteUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        cmsPostSearchCriteriaVO.setCategoryUuid("CC7F7D92-3816-4881-BA94-3D29A22C22C9");
        cmsPostSearchCriteriaVO.setPages(1);
        cmsPostSearchCriteriaVO.setRows(10);
        cmsPostSearchCriteriaVO.setTitle("");
        Assertions.assertEquals(wintereeCoreService.getCmsPostList(cmsPostSearchCriteriaVO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addCmsPostTest() {
        CmsPostsDTO cmsPostsDTO = new CmsPostsDTO();
        cmsPostsDTO.setSiteUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        cmsPostsDTO.setCategoryUuid("CC7F7D92-3816-4881-BA94-3D29A22C22C9");
        cmsPostsDTO.setFeaturedImage("");
        cmsPostsDTO.setTitle("");
        cmsPostsDTO.setContent("");
        cmsPostsDTO.setIsOriginal(true);
        cmsPostsDTO.setSourceName("");
        cmsPostsDTO.setSourceUrl("");
        cmsPostsDTO.setTagIds(new ArrayList<String>() {{
            add("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        }});
        Assertions.assertEquals(wintereeCoreService.addCmsPost(cmsPostsDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateCmsPostTest() {
        CmsPostsDTO cmsPostsDTO = new CmsPostsDTO();
        cmsPostsDTO.setUuid("54003D18-D84B-4F4E-B311-C5DEEBB89C76");
        cmsPostsDTO.setSiteUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        cmsPostsDTO.setCategoryUuid("CC7F7D92-3816-4881-BA94-3D29A22C22C9");
        cmsPostsDTO.setFeaturedImage("");
        cmsPostsDTO.setTitle("");
        cmsPostsDTO.setContent("");
        cmsPostsDTO.setIsOriginal(true);
        cmsPostsDTO.setSourceName("");
        cmsPostsDTO.setSourceUrl("");
        cmsPostsDTO.setTagIds(new ArrayList<String>() {{
            add("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        }});
        Assertions.assertEquals(wintereeCoreService.updateCmsPost(cmsPostsDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteCmsPostTest() {
        Assertions.assertEquals(wintereeCoreService.deleteCmsPost("54003D18-D84B-4F4E-B311-C5DEEBB89C76").getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getTagListTest() {
        Assertions.assertEquals(wintereeCoreService.getTagList("1BD49B64-3206-4E27-A4F5-93DD9CD28157", 1, 10).getCode(), StateCode.OK.getCode());
    }

    @Test
    public void getTagByUuidTest() {
        Assertions.assertEquals(wintereeCoreService.getTagByUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157", "17CE6CBF-174B-4152-9949-F415CDFB33B7").getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void addCmsTagTest() {
        CmsTagDTO cmsTagDTO = new CmsTagDTO();
        cmsTagDTO.setSiteUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        cmsTagDTO.setEnName("Test");
        cmsTagDTO.setZhName("测试");
        Assertions.assertEquals(wintereeCoreService.addCmsTag(cmsTagDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void updateCmsTagTest() {
        CmsTagDTO cmsTagDTO = new CmsTagDTO();
        cmsTagDTO.setUuid("17CE6CBF-174B-4152-9949-F415CDFB33B7");
        cmsTagDTO.setSiteUuid("1BD49B64-3206-4E27-A4F5-93DD9CD28157");
        cmsTagDTO.setEnName("Test");
        cmsTagDTO.setZhName("测试");
        Assertions.assertEquals(wintereeCoreService.updateCmsTag(cmsTagDTO).getCode(), StateCode.OK.getCode());
    }

    @Test
    @Rollback
    @Transactional
    public void deleteCmsTagTest() {
        Assertions.assertEquals(wintereeCoreService.deleteCmsTag("17CE6CBF-174B-4152-9949-F415CDFB33B7").getCode(), StateCode.OK.getCode());
    }
    //</editor-fold>
}
