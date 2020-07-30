package com.winteree.core.service;

/**
 * <p>Title: AliyunGreenService</p>
 * <p>Description: 阿里云绿盾服务</p>
 *
 * @author RenFei
 * @date : 2020-07-28 23:17
 */
public interface AliyunGreenService {
    /**
     * 阿里云绿网-文本扫描
     *
     * @param text 待检测的文本，长度不超过10000个字符
     * @return boolean
     */
    boolean textScan(String text);
}
