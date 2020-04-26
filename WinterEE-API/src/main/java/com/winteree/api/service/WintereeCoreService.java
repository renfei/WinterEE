package com.winteree.api.service;

import com.winteree.api.entity.AccountDTO;
import com.winteree.api.entity.LogDTO;
import com.winteree.api.entity.MenuVO;
import com.winteree.api.entity.ReportPublicKeyVO;
import net.renfei.sdk.entity.APIResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * WinterEE-Core-Serve提供的服务接口
 *
 * @author RenFei
 */
public interface WintereeCoreService {
    /**
     * i18n国际化语言翻译服务
     *
     * @param language       需要的语言
     * @param message        i18n 代码
     * @param defaultMessage 默认内容
     * @return 当没有找到所需的语言时将返回默认内容
     */
    @GetMapping("/i18n/{language}/{message}/{defaultMessage}")
    String getMessage(@PathVariable("language") String language, @PathVariable("message") String message, @PathVariable("defaultMessage") String defaultMessage);

    //<editor-fold desc="秘钥类的接口" defaultstate="collapsed">

    /**
     * 申请一个非对称秘钥的公钥
     *
     * @return
     */
    @GetMapping("/secretkey")
    APIResult<String> secretKey();

    /**
     * 上报一个非对称秘钥公钥
     *
     * @return
     */
    @PostMapping("/secretkey")
    APIResult setSecretKey(@RequestBody ReportPublicKeyVO reportPublicKeyVO);
    //</editor-fold>

    //<editor-fold desc="账户类的接口" defaultstate="collapsed">

    /**
     * 根据用户名获取账号对象
     *
     * @param username 用户名
     * @return AccountDTO
     */
    @GetMapping("/inside/account/username")
    AccountDTO findAccountByUsername(String username);

    /**
     * 根据邮箱地址获取账号对象
     *
     * @param email 电子邮件
     * @return AccountDTO
     */
    @GetMapping("/inside/account/email")
    AccountDTO findAccountByEmail(String email);

    /**
     * 根据手机号获取账号对象
     *
     * @param phone 手机号
     * @return AccountDTO
     */
    @GetMapping("/inside/account/phone")
    AccountDTO findAccountByPhoneNumber(String phone);

    /**
     * 创建一个Totp的秘钥
     *
     * @param username 用户名
     * @return AccountDTO
     */
    @GetMapping("/account/totp")
    APIResult<String> createTotp(String username);

    /**
     * 检查一个用户名是否存在以及类型
     *
     * @param account  用户输入的用户名、邮箱、手机号
     * @param language 返回的消息语言，默认 zh-CN
     * @return
     */
    @GetMapping("/account/check")
    APIResult<String> checkAccount(@RequestParam("account") String account, @RequestParam("language") String language);
    //</editor-fold>

    //<editor-fold desc="菜单类的接口" defaultstate="collapsed">

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @GetMapping("/menu/tree")
    APIResult<List<MenuVO>> getMenuTree(@RequestParam(name = "language", required = false) String language);

    /**
     * 后台菜单管理中的查询菜单列表，注意这个不是登陆以后获取菜单列表
     *
     * @return
     */
    @GetMapping("/setting/menu/tree")
    APIResult<List<MenuVO>> getSettingMenuTree();

    @GetMapping("/setting/menu/list")
    APIResult<List<MenuVO>> getSettingMenuList();

    @GetMapping("/setting/menu")
    APIResult<MenuVO> getSettingMenu(String uuid);

    /**
     * @param uuid
     * @return
     */
    @DeleteMapping("/setting/menu")
    APIResult deleteSettingMenuByUuid(String uuid);

    /**
     * @param menuVO
     * @return
     */
    @PutMapping("/setting/menu")
    APIResult updateSettingMenu(MenuVO menuVO);

    /**
     * @param menuVO
     * @return
     */
    @PostMapping("/setting/menu")
    APIResult addSettingMenu(MenuVO menuVO);
    //</editor-fold>

    //<editor-fold desc="日志类的接口" defaultstate="collapsed">

    /**
     * 日志记录（服务内部）
     *
     * @param logDTO 日志实体
     * @return
     */
    @PostMapping("/inside/log")
    APIResult log(@RequestBody LogDTO logDTO);

    /**
     * 获取系统日志
     *
     * @param page      页数
     * @param rows      每页行数
     * @param logType   每页行数
     * @param subType   每页行数
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    @GetMapping("/setting/logs")
    APIResult<List<LogDTO>> getLogList(@RequestParam("page") int page,
                                       @RequestParam("rows") int rows,
                                       @RequestParam(name = "logType", required = false) String logType,
                                       @RequestParam(name = "subType", required = false) String subType,
                                       @RequestParam(name = "startDate", required = false) String startDate,
                                       @RequestParam(name = "endDate", required = false) String endDate);

    /**
     * 获取日志类型
     *
     * @return
     */
    @GetMapping("/setting/logs/type")
    APIResult<List<Map<String, String>>> getAllLogType(@RequestParam(name = "lang", required = false) String lang);

    /**
     * 获取日志子类型
     *
     * @return
     */
    @GetMapping("/setting/logs/subtype")
    APIResult<List<Map<String, String>>> getAllLogSubType(@RequestParam(name = "lang", required = false) String lang);
    //</editor-fold>

    //<editor-fold desc="租户类的接口" defaultstate="collapsed">
    //</editor-fold>
}
