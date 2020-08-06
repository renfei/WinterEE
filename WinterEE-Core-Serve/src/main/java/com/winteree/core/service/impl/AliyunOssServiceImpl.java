package com.winteree.core.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.AliyunOssService;
import com.winteree.core.service.BaseService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * <p>Title: AliyunOssServiceImpl</p>
 * <p>Description: 阿里云OSS储存桶服务</p>
 *
 * @author RenFei
 * @date : 2020-08-06 21:51
 */
@Service
public class AliyunOssServiceImpl extends BaseService implements AliyunOssService {
    private final OSS ossClient = new OSSClientBuilder()
            .build(wintereeCoreConfig.getAliyunOssEndpoint(),
                    wintereeCoreConfig.getAliyun().getAccessKeyId(),
                    wintereeCoreConfig.getAliyun().getSecret());

    protected AliyunOssServiceImpl(WintereeCoreConfig wintereeCoreConfig) {
        super(wintereeCoreConfig);
    }

    /**
     * 上传对象到私有桶
     *
     * @param inputStream 流
     * @param objectName  对象路径和名称
     */
    @Override
    public void uploadPrivateFile(InputStream inputStream, String objectName) {
        this.putObject(wintereeCoreConfig.getAliyunOssPrivateBuckename(), objectName, inputStream);
    }

    /**
     * 上传对象到公共桶
     *
     * @param inputStream 流
     * @param objectName  对象路径和名称
     */
    @Override
    public void uploadPubilcFile(InputStream inputStream, String objectName) {
        this.putObject(wintereeCoreConfig.getAliyunOssPublicBuckename(), objectName, inputStream);
    }

    /**
     * 上传对象到储存桶
     *
     * @param bucketName  桶名称
     * @param objectName  对象路径和名称
     * @param inputStream 流
     */
    @Override
    public void putObject(String bucketName, String objectName, InputStream inputStream) {
        ossClient.putObject(bucketName, objectName, inputStream);
    }

    /**
     * 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
     *
     * @param downLoadHost 下载链接，需要以 http:// 或 https:// 开头
     * @param bucketName   桶名称
     * @param objectName   对象路径和名称
     * @param expiration   过期时间
     * @return
     */
    @Override
    public String generatePresignedUrl(String downLoadHost, String bucketName, String objectName, Date expiration) {
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
        return downLoadHost + url.getPath() + "?" + url.getQuery();
    }
}
