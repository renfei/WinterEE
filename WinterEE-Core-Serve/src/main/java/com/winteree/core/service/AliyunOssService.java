package com.winteree.core.service;

import java.io.InputStream;
import java.util.Date;

/**
 * <p>Title: AliyunOssService</p>
 * <p>Description: 阿里云OSS储存桶服务</p>
 *
 * @author RenFei
 * @date : 2020-08-06 21:47
 */
public interface AliyunOssService {
    /**
     * 上传对象到私有桶
     *
     * @param inputStream 流
     * @param objectName  对象路径和名称
     */
    void uploadPrivateFile(InputStream inputStream, String objectName);

    /**
     * 上传对象到公共桶
     *
     * @param inputStream 流
     * @param objectName  对象路径和名称
     */
    void uploadPubilcFile(InputStream inputStream, String objectName);

    /**
     * 上传对象到储存桶
     *
     * @param bucketName  桶名称
     * @param objectName  对象路径和名称
     * @param inputStream 流
     */
    void putObject(String bucketName, String objectName, InputStream inputStream);

    /**
     * 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
     *
     * @param downLoadHost 下载链接，需要以 http:// 或 https:// 开头
     * @param bucketName   桶名称
     * @param objectName   对象路径和名称
     * @param expiration   过期时间
     * @return
     */
    String generatePresignedUrl(String downLoadHost, String bucketName, String objectName, Date expiration);
}
