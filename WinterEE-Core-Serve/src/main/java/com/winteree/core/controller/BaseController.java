package com.winteree.core.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: BaseController</p>
 * <p>Description: 基础Controller</p>
 *
 * @author RenFei
 * @date : 2020-04-24 20:07
 */
abstract class BaseController {
    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     *
     * @param request
     * @param response
     */
    @ModelAttribute
    public void modelAttribute(HttpServletRequest request, HttpServletResponse response) {
    }
}
