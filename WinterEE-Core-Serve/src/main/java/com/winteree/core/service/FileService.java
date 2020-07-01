package com.winteree.core.service;

import com.winteree.api.entity.FileDTO;
import com.winteree.api.exception.FailureException;
import com.winteree.core.dao.entity.FilesDO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>Title: FileService</p>
 * <p>Description: 文件服务</p>
 *
 * @author RenFei
 * @date : 2020-06-30 20:18
 */
public interface FileService {
    /**
     * 上传公开访问的公共读文件，访问这里的文件不需要鉴权
     *
     * @param file 文件
     * @return 文件访问地址
     * @throws FailureException 失败异常
     * @throws IOException      IO异常
     */
    String uploadPublicFile(MultipartFile file) throws FailureException, IOException;

    /**
     * 上传私有文件，访问文件需要鉴权
     *
     * @param file
     * @return
     * @throws FailureException
     * @throws IOException
     */
    String uploadPrivateFile(MultipartFile file) throws FailureException, IOException;
    /**
     * 根据文件ID获取文件
     *
     * @param uuid
     * @return
     */
    FilesDO getFilesByUuid(String uuid);
    /**
     * 获取文件（不做校验，需要外部调用者自己判断权限）
     *
     * @param uuid
     * @return
     */
    FileDTO getFileNoVerification(String uuid);
    /**
     * 获取文件（只做简单的权限判断）
     *
     * @param uuid
     * @return
     */
    FileDTO getFileOnVerification(String uuid);
}
