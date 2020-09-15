package com.winteree.api.entity;

import lombok.Data;
import net.renfei.sdk.utils.DateUtils;

/**
 * <p>Title: LicenseVO</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-09-15 13:02
 */
@Data
public class LicenseVO {
    private String licenseSerialNumber;
    private String licenseType;
    private String systemName;
    private String expiredDate;
    private String machineCode;
    private String state;
    private String reason;

    public LicenseVO(LicenseDTO licenseDTO) {
        this.licenseSerialNumber = licenseDTO.getSn();
        switch (licenseDTO.getType()) {
            case DEMO:
                this.licenseType = "演示使用版(Demo Trial Edition)";
                break;
            case COMMERCIAL:
                this.licenseType = "商业许可版(Commercial License Edition)";
                break;
            default:
                this.licenseType = "开源社区版(Open Source Community Edition)";
                break;
        }
        switch (licenseDTO.getState()) {
            case NORMAL:
                this.state = "正常(NORMAL)";
                break;
            case OVERDUE:
                this.state = "过期(OVERDUE)";
                break;
            case INVALID:
                this.state = "无效(INVALID)";
                break;
            case REVOCATION:
                this.state = "吊销(REVOCATION)";
                break;
            default:
                this.state = "";
                break;
        }
        String date = DateUtils.formatDate(licenseDTO.getExpired(), "yyyy-MM-dd HH:mm:ss");
        if (LicenseDTO.PERMANENT_DATE.equals(date)) {
            this.expiredDate = "永久授权(Perpetual License)";
        } else {
            this.expiredDate = date;
        }
        this.systemName = licenseDTO.getName();
        this.machineCode = licenseDTO.getMacCode();
        this.reason = licenseDTO.getReason();
    }
}
