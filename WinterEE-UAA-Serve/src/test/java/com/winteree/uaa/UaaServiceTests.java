package com.winteree.uaa;

import com.winteree.api.entity.LogSubTypeEnum;
import com.winteree.uaa.dao.entity.AccountDO;
import com.winteree.uaa.service.*;
import net.renfei.sdk.utils.DateUtils;
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

import java.util.ArrayList;

/**
 * <p>Title: UaaControllerTests</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-07-02 15:50
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test UaaControllerTests")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class UaaServiceTests {
    private final AccountService accountService;
    private final I18nService i18nService;
    private final LogService logService;
    private final MenuService menuService;
    private final RoleService roleService;
    private final SecretKeyService secretKeyService;

    @Autowired
    public UaaServiceTests(AccountService accountService,
                           I18nService i18nService,
                           LogService logService,
                           MenuService menuService,
                           RoleService roleService,
                           SecretKeyService secretKeyService) {
        this.accountService = accountService;
        this.i18nService = i18nService;
        this.logService = logService;
        this.menuService = menuService;
        this.roleService = roleService;
        this.secretKeyService = secretKeyService;
    }

    @Test
    public void findAccountByUsernameTest() {
        Assertions.assertNotNull(accountService.findAccountByUsername("admin"));
    }

    @Test
    public void findAccountByEmailTest() {
        Assertions.assertNotNull(accountService.findAccountByEmail("i@renfei.net"));
    }

    @Test
    public void findAccountByPhoneNumberTest() {
        Assertions.assertNotNull(accountService.findAccountByPhoneNumber("13001000000"));
    }

    @Test
    @Rollback
    @Transactional
    public void updateByPrimaryKeySelectiveTest() {
        AccountDO accountDO = new AccountDO();
        accountDO.setId(1L);
        accountDO.setErrorCount(5);
        accountDO.setLockTime(DateUtils.nextMinutes(10));
        Assertions.assertEquals(accountService.updateByPrimaryKeySelective(accountDO), 1);
    }

    @Test
    public void getGrantedAuthorityTest() {
        AccountDO accountDO = new AccountDO();
        accountDO.setUuid("9369919A-F95E-44CF-AB0A-6BCD1D933403");
        Assertions.assertNotNull(accountService.getGrantedAuthority(accountDO));
        accountDO.setUuid("6853F941-EF46-4B0D-AC2C-5A6B8D5C9626");
        Assertions.assertNotNull(accountService.getGrantedAuthority(accountDO));
    }

    @Test
    public void getMessageTest() {
        Assertions.assertNotNull(i18nService.getMessage("zh-cn", "uaa.invalidusernameorpassword", "无效的用户名或密码"));
    }

    @Test
    @Rollback
    @Transactional
    public void logTest() {
        logService.log("9369919A-F95E-44CF-AB0A-6BCD1D933403", LogSubTypeEnum.SUCCESS);
        logService.log("9369919A-F95E-44CF-AB0A-6BCD1D933403", LogSubTypeEnum.SUCCESS, "Test");
    }

    @Test
    public void getAllMenuPermissionTest() {
        Assertions.assertNotNull(menuService.getAllMenuPermission());
    }

    @Test
    public void getMenuUuidByRoleUuidTest() {
        Assertions.assertNotNull(menuService.getMenuUuidByRoleUuid(new ArrayList<String>() {{
            add("997FBB41-0EE2-44ED-B94F-EFDDA316EAA2");
        }}));
    }

    @Test
    public void getMenuPermissionByMenuUuidTest() {
        Assertions.assertNotNull(menuService.getMenuPermissionByMenuUuid(new ArrayList<String>() {{
            add("31380c03-eee8-4e85-ad3e-a919d86b96aa");
            add("eff2d51d-107a-4085-9925-ade657a33880");
            add("807f746f-b7b0-4aff-9674-f71e39a5c92f");
            add("3f06a551-7039-4dcb-b74e-fcd7bef6a129");
        }}));
    }

    @Test
    public void selectRoleUuidByUserUuidTest(){
        Assertions.assertNotNull(roleService.selectRoleUuidByUserUuid("6853F941-EF46-4B0D-AC2C-5A6B8D5C9626"));
    }

    @Test
    public void getSecretKeyStringByIdTest(){
        Assertions.assertNotNull(secretKeyService.getSecretKeyStringById("35657532-5856-40ae-8d7c-25e9e243a2f8"));
    }
}
