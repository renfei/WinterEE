package com.winteree.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.winteree.api.entity.LicenseDTO;
import com.winteree.api.entity.LicenseState;
import com.winteree.api.entity.LicenseType;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.HardwareService;
import com.winteree.core.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.http.HttpRequest;
import net.renfei.sdk.http.HttpResult;
import net.renfei.sdk.utils.*;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * <p>Title: LicenseServiceImpl</p>
 * <p>Description: 授权服务</p>
 *
 * @author RenFei
 * @date : 2020-08-05 21:37
 */
@Slf4j
@Service
public class LicenseServiceImpl extends BaseService implements LicenseService {
    private final AccountService accountService;
    private final HardwareService hardwareService;
    private final static String SECRET_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAocNDieXLt3QDLibBJcyUYO" +
            "pmLW5JZfAlZRFLgPozi9X/f2zxKqRjhhgzFqOP9F9VFcfx65y1k/VUNRRwmVVzPW7k3kQOWmmyQeVvFwK6aIhl+72YHoMSnOdZ9" +
            "EBDL27PlRjqXSHoQq8Ldvagmuad+/Nhqdr2rvy4UH93PdwSdDquwHAl/9Tr6TW0nM/wx1YYyxp7MXiSkuVsTfmnazNWYxNSvAWS" +
            "F1iXUUoBaFTQJTF8YKCc6lBv0ElRCuemXIucpU6HnjE3zLt3g9+blZEBQDNegAyLDe0kP48/hCYrWigA7vOWmUI4Nyd0js0UOY1" +
            "t2+EkLBsZUsGTImOlpqL3XQIDAQAB";

    protected LicenseServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                 AccountService accountService, HardwareService hardwareService) {
        super(wintereeCoreConfig);
        this.accountService = accountService;
        this.hardwareService = hardwareService;
    }

    @Override
    public LicenseDTO getLicense() {
        String machineCode = hardwareService.getSerialNumber();
        LicenseDTO license = Builder.of(LicenseDTO::new)
                .with(LicenseDTO::setMacCode, machineCode)
                .with(LicenseDTO::setExpired, DateUtils.parseDate(LicenseDTO.PERMANENT_DATE))
                .with(LicenseDTO::setState, LicenseState.NORMAL)
                .with(LicenseDTO::setReason, "有效")
                .with(LicenseDTO::setSn, UUID.randomUUID().toString().replace("-", "").toUpperCase())
                .with(LicenseDTO::setType, LicenseType.COMMUNITY)
                .with(LicenseDTO::setName, "WinterEE Community Edition")
                .build();
        try {
            String content = this.getLicenseContent();
            if (BeanUtils.isEmpty(content)) {
                return license;
            }
            String licenseJson = RSAUtils.encrypt(content, SECRET_KEY);
            LicenseDTO licenseDTO = JSON.parseObject(licenseJson, LicenseDTO.class);
            String date = DateUtils.formatDate(licenseDTO.getExpired(), "yyyy-MM-dd HH:mm:ss");
            if (!LicenseDTO.PERMANENT_DATE.equals(date)) {
                if (new Date().after(licenseDTO.getExpired())) {
                    licenseDTO.setState(LicenseState.OVERDUE);
                    licenseDTO.setReason("许可证已过期。License expired.");
                    return licenseDTO;
                }
            }
            if (!machineCode.equals(licenseDTO.getMacCode())) {
                licenseDTO.setState(LicenseState.INVALID);
                licenseDTO.setReason("许可证授权的机器与当前机器不符。The machine authorized by the license does not match the current machine.");
                return licenseDTO;
            }
            File file = new File(this.getResourceBasePath() + "/RevocationList.txt");
            if (file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader bfreader = new BufferedReader(reader);
                String line;
                while ((line = bfreader.readLine()) != null) {
                    if (line.equals(licenseDTO.getSn())) {
                        licenseDTO.setState(LicenseState.REVOCATION);
                        licenseDTO.setReason("许可证已经被吊销。The license has been revoked.");
                        return licenseDTO;
                    }
                }
            }
            return licenseDTO;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return license;
        }
    }

    @Override
    public String getMachineCode() {
        return hardwareService.getSerialNumber();
    }

    public void saveLicense(String license) throws ForbiddenException, FailureException, IOException {
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            String machineCode = hardwareService.getSerialNumber();
            LicenseDTO licenseDTO = null;
            try {
                String licenseJson = RSAUtils.encrypt(license, SECRET_KEY);
                licenseDTO = JSON.parseObject(licenseJson, LicenseDTO.class);
            } catch (Exception e) {
                throw new FailureException("许可证不合法，请查正后重试。");
            }
            String date = DateUtils.formatDate(licenseDTO.getExpired(), "yyyy-MM-dd HH:mm:ss");
            if (!LicenseDTO.PERMANENT_DATE.equals(date)) {
                if (new Date().after(licenseDTO.getExpired())) {
                    throw new FailureException("许可证已过期。License expired.");
                }
            }
            if (!machineCode.equals(licenseDTO.getMacCode())) {
                throw new FailureException("许可证授权的机器与当前机器不符。The machine authorized by the license does not match the current machine.");
            }
            File file = new File(this.getResourceBasePath() + "/license.lic");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file,false);
            fos.write(license.getBytes());
            fos.close();
        } else {
            throw new ForbiddenException("权限不足，只有超管可以更改License文件。");
        }
    }

    @Scheduled(cron = "0 0 0/12 * * ?")
    public void getRevocationList() {
        try {
            HttpRequest requestSSL = HttpRequest.create().url("https://license.renfei.net/RevocationList.txt").useSSL();
            HttpResult resultSSL = HttpUtils.get(requestSSL);
            String text = resultSSL.getResponseText();
            if (!BeanUtils.isEmpty(text)) {
                File file = new File(this.getResourceBasePath() + "/RevocationList.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter printWriter = new PrintWriter(new FileWriter(file, false), true);
                printWriter.println(text);
                printWriter.close();
            }
        } catch (Exception e) {
        }
    }

    private String getLicenseContent() throws IOException {
        File file = new File(this.getResourceBasePath() + "/license.lic");
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String data;
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
            br.close();
            isr.close();
            fis.close();
            return sb.toString();
        } else {
            return null;
        }
    }

    private String getResourceBasePath() {
        // 获取跟目录
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            // nothing to do
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }

        String pathStr = path.getAbsolutePath();
        log.info("Path:" + pathStr);
        ApplicationHome home = new ApplicationHome(this.getClass());
        log.info("ApplicationHome:" + JSON.toJSONString(home));
        return pathStr;
    }
//    private String getResourceBasePath() {
//        // 获取跟目录
//        ApplicationHome home = new ApplicationHome(this.getClass());
//        System.out.println("ApplicationHome:" + home.getSource().getParent());
//        return home.getSource().getParent();
//    }
}
