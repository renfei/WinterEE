package com.philisense.report.controller;

import com.philisense.report.service.YourService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>Title: YourController</p>
 * <p>Description: 你的控制层</p>
 *
 * @author RenFei
 * @date : 2020-06-24 20:03
 */
@Controller
public class YourController {
    private final YourService yourService;

    public YourController(YourService yourService) {
        this.yourService = yourService;
    }

    /**
     * 演示方法
     * 这里为你展示调用 WinterEE核心服务获取当前登陆的用户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/")
    public String demoMethod() {
        return yourService.demoMethod();
    }
}
