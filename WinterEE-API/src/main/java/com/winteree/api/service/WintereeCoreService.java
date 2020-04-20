package com.winteree.api.service;

import com.winteree.api.entity.AccountDTO;
import com.winteree.api.entity.LogDTO;
import com.winteree.api.entity.MenuVO;
import com.winteree.api.entity.ReportPublicKeyVO;
import net.renfei.sdk.entity.APIResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/core/log")
    APIResult log(@RequestBody LogDTO logDTO);

    //<editor-fold desc="秘钥类的接口" defaultstate="collapsed">

    /**
     * 申请一个非对称秘钥的公钥
     *
     * @return
     */
    @GetMapping("/secretkey")
    APIResult secretKey();

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
    @GetMapping("/account/username")
    AccountDTO findAccountByUsername(String username);

    /**
     * 根据邮箱地址获取账号对象
     *
     * @param email 电子邮件
     * @return AccountDTO
     */
    @GetMapping("/account/email")
    AccountDTO findAccountByEmail(String email);

    /**
     * 根据手机号获取账号对象
     *
     * @param phone 手机号
     * @return AccountDTO
     */
    @GetMapping("/account/phone")
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

    /**
     * 获取菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @GetMapping("/menu/tree")
    APIResult<List<MenuVO>> getMenuTree(@RequestParam(name = "language", required = false) String language);
}
