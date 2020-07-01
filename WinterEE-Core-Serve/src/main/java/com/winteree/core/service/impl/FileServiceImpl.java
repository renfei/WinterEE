package com.winteree.core.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.winteree.api.entity.DataScopeEnum;
import com.winteree.api.entity.FileDTO;
import com.winteree.api.exception.FailureException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.FilesDOMapper;
import com.winteree.core.dao.entity.FilesDO;
import com.winteree.core.dao.entity.FilesDOExample;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.FileService;
import com.winteree.core.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.DateUtils;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * <p>Title: FileServiceImpl</p>
 * <p>Description: 文件服务实现类</p>
 *
 * @author RenFei
 * @date : 2020-06-30 20:22
 */
@Slf4j
@Service
public class FileServiceImpl extends BaseService implements FileService {
    private final AccountService accountService;
    private final RoleService roleService;
    private final FilesDOMapper filesDOMapper;

    protected FileServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                              AccountService accountService,
                              RoleService roleService,
                              FilesDOMapper filesDOMapper) {
        super(wintereeCoreConfig);
        this.accountService = accountService;
        this.roleService = roleService;
        this.filesDOMapper = filesDOMapper;
    }

    /**
     * 上传公开访问的公共读文件，访问这里的文件不需要鉴权
     *
     * @param file 文件
     * @return 文件访问地址
     * @throws FailureException 失败异常
     * @throws IOException      IO异常
     */
    @Override
    public String uploadPublicFile(MultipartFile file) throws FailureException, IOException {
        AccountDTO accountDTO = getSignedUser(accountService);
        if (file.isEmpty()) {
            throw new FailureException("File is Empty");
        }
        // 文件名
        String fileName = file.getOriginalFilename();
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 新文件名
        fileName = UUID.randomUUID().toString().replace("-", "") + suffixName;
        String path = wintereeCoreConfig.getStoragePath();
        if (!path.endsWith("/")) {
            path += "/";
        }
        switch (wintereeCoreConfig.getStorageType()) {
            case "local":
                // 上传到本地
                File localFile = new File(path + fileName);
                File dirPath = new File(path);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                file.transferTo(localFile);
                break;
            case "oss":
                // 上传到 OSS
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                this.aliyunOssUploadPubilcFile(file.getInputStream(), path + fileName);
                break;
            default:
                log.error("StorageType Config is Error");
                throw new FailureException("StorageType Config is Error");
        }
        // 存库记录
        FilesDO filesDO = new FilesDO();
        filesDO.setUuid(UUID.randomUUID().toString().toUpperCase());
        filesDO.setTenantUuid(accountDTO.getTenantUuid());
        filesDO.setOfficeUuid(accountDTO.getOfficeUuid());
        filesDO.setDepartmentUuid(accountDTO.getDepartmentUuid());
        filesDO.setFileName(fileName);
        filesDO.setOriginalFileName(file.getOriginalFilename());
        filesDO.setStorageType(wintereeCoreConfig.getStorageType());
        filesDO.setStoragePath(path);
        filesDO.setCreateBy(accountDTO.getUuid());
        filesDO.setCreateTime(new Date());
        filesDO.setBuckeName(wintereeCoreConfig.getAliyunOssPublicBuckename());
        filesDOMapper.insertSelective(filesDO);
        return wintereeCoreConfig.getStoragePublicUrl() + fileName;
    }

    /**
     * 上传私有文件，访问文件需要鉴权
     *
     * @param file
     * @return
     * @throws FailureException
     * @throws IOException
     */
    @Override
    public String uploadPrivateFile(MultipartFile file) throws FailureException, IOException {
        AccountDTO accountDTO = getSignedUser(accountService);
        if (file.isEmpty()) {
            throw new FailureException("File is Empty");
        }
        // 文件名
        String fileName = file.getOriginalFilename();
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 新文件名
        fileName = UUID.randomUUID().toString().replace("-", "") + suffixName;
        String path = wintereeCoreConfig.getStoragePath();
        if (!path.endsWith("/")) {
            path += "/";
        }
        switch (wintereeCoreConfig.getStorageType()) {
            case "local":
                // 上传到本地
                File localFile = new File(path + fileName);
                File dirPath = new File(path);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                file.transferTo(localFile);
                break;
            case "oss":
                // 上传到 OSS
                if (path.startsWith("/")) {
                    path = path.substring(1, path.length() - 1);
                }
                this.aliyunOssUploadPrivateFile(file.getInputStream(), path + fileName);
                break;
            default:
                log.error("StorageType Config is Error");
                throw new FailureException("StorageType Config is Error");
        }
        // 存库记录
        FilesDO filesDO = new FilesDO();
        filesDO.setUuid(UUID.randomUUID().toString().toUpperCase());
        filesDO.setTenantUuid(accountDTO.getTenantUuid());
        filesDO.setOfficeUuid(accountDTO.getOfficeUuid());
        filesDO.setDepartmentUuid(accountDTO.getDepartmentUuid());
        filesDO.setFileName(fileName);
        filesDO.setOriginalFileName(file.getOriginalFilename());
        filesDO.setStorageType(wintereeCoreConfig.getStorageType());
        filesDO.setStoragePath(path);
        filesDO.setCreateBy(accountDTO.getUuid());
        filesDO.setCreateTime(new Date());
        filesDO.setBuckeName(wintereeCoreConfig.getAliyunOssPrivateBuckename());
        filesDOMapper.insertSelective(filesDO);
        return wintereeCoreConfig.getStoragePublicUrl() + path + fileName;
    }

    /**
     * 根据文件ID获取文件
     *
     * @param uuid
     * @return
     */
    @Override
    public FilesDO getFilesByUuid(String uuid) {
        if (BeanUtils.isEmpty(uuid)) {
            return null;
        }
        FilesDOExample example = new FilesDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        return ListUtils.getOne(filesDOMapper.selectByExample(example));
    }

    /**
     * 获取文件（不做校验，需要外部调用者自己判断权限）
     *
     * @param uuid
     * @return
     */
    @Override
    public FileDTO getFileNoVerification(String uuid) {
        FilesDO filesDO = this.getFilesByUuid(uuid);
        if (filesDO == null) {
            return null;
        }
        // 鉴权
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid()) ||
                accountDTO.getUuid().equals(filesDO.getCreateBy())) {
            // 超管或上传人自己不做限制
        } else {
            //否则只能管理自己归属的租户
            if (!accountDTO.getTenantUuid().equals(filesDO.getTenantUuid())) {
                return null;
            }
        }
        switch (filesDO.getStorageType()) {
            case "local":
                File file = new File(filesDO.getStoragePath() + filesDO.getFileName());
                FileDTO fileDTOLocal = new FileDTO();
                fileDTOLocal.setFile(file);
                return fileDTOLocal;
            case "oss":
                FileDTO fileDTOOss = new FileDTO();
                fileDTOOss.setFileUrl(this.getAliyunOssPresignedUrl(
                        filesDO.getStoragePath() + filesDO.getFileName(),
                        DateUtils.nextMinutes(10)));
                return fileDTOOss;
            default:
                return null;
        }
    }

    /**
     * 获取文件（只做简单的权限判断）
     *
     * @param uuid
     * @return
     */
    @Override
    public FileDTO getFileOnVerification(String uuid) {
        FilesDO filesDO = this.getFilesByUuid(uuid);
        if (filesDO == null) {
            return null;
        }
        // 鉴权
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid()) ||
                accountDTO.getUuid().equals(filesDO.getCreateBy())) {
            // 超管或上传人自己不做限制
        } else {
            //否则只能管理自己归属的租户
            if (!accountDTO.getTenantUuid().equals(filesDO.getTenantUuid())) {
                return null;
            }
            // 验证数据权限范围，是全部还是本公司
            DataScopeEnum dataScopeEnum = roleService.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    break;
                case COMPANY:
                    // 只能查看公司下面的
                    if (!accountDTO.getOfficeUuid().equals(filesDO.getOfficeUuid())) {
                        return null;
                    }
                    break;
                case DEPARTMENT:
                    // 只能查询他自己部门下的
                    if (!accountDTO.getDepartmentUuid().equals(filesDO.getDepartmentUuid())) {
                        return null;
                    }
                    break;
                default:
                    return null;
            }
        }
        switch (filesDO.getStorageType()) {
            case "local":
                File file = new File(filesDO.getStoragePath() + filesDO.getFileName());
                FileDTO fileDTOLocal = new FileDTO();
                fileDTOLocal.setFile(file);
                return fileDTOLocal;
            case "oss":
                FileDTO fileDTOOss = new FileDTO();
                fileDTOOss.setFileUrl(this.getAliyunOssPresignedUrl(
                        filesDO.getStoragePath() + filesDO.getFileName(),
                        DateUtils.nextMinutes(10)));
                return fileDTOOss;
            default:
                return null;
        }
    }

    /**
     * 获取OSS签名URL进行临时授权
     *
     * @param objectName 对象地址
     * @param expiration 授权过期时间
     * @return
     */
    private String getAliyunOssPresignedUrl(String objectName, Date expiration) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder()
                .build(wintereeCoreConfig.getAliyunOssEndpoint(),
                        wintereeCoreConfig.getAliyun().getAccessKeyId(),
                        wintereeCoreConfig.getAliyun().getSecret());
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(wintereeCoreConfig.getAliyunOssPrivateBuckename(), objectName, expiration);
        // 关闭OSSClient。
        ossClient.shutdown();
        return wintereeCoreConfig.getStorageOssPrivateUrl() + url.getPath() + "?" + url.getQuery();
    }

    /**
     * 上传到OSS私有桶
     *
     * @param inputStream
     * @param objectName
     */
    private void aliyunOssUploadPrivateFile(InputStream inputStream, String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder()
                .build(wintereeCoreConfig.getAliyunOssEndpoint(),
                        wintereeCoreConfig.getAliyun().getAccessKeyId(),
                        wintereeCoreConfig.getAliyun().getSecret());
        ossClient.putObject(wintereeCoreConfig.getAliyunOssPrivateBuckename(), objectName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 上传到OSS公共读桶
     *
     * @param inputStream
     * @param objectName
     */
    private void aliyunOssUploadPubilcFile(InputStream inputStream, String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder()
                .build(wintereeCoreConfig.getAliyunOssEndpoint(),
                        wintereeCoreConfig.getAliyun().getAccessKeyId(),
                        wintereeCoreConfig.getAliyun().getSecret());
        ossClient.putObject(wintereeCoreConfig.getAliyunOssPublicBuckename(), objectName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
