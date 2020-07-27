package com.winteree.api.entity;

import lombok.Data;

/**
 * <p>Title: IpInfoDTO</p>
 * <p>Description: IP信息数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-07-27 21:20
 */
@Data
public class IpInfoDTO {
    String ipAddress;
    String countryShort;
    String countryLong;
    String region;
    String city;
    String isp;
    Float latitude;
    Float longitude;
    String domain;
    String zipcode;
    String netspeed;
    String timezone;
    String iddcode;
    String areacode;
    String weatherstationcode;
    String weatherstationname;
    String mcc;
    String mnc;
    String mobilebrand;
    Float elevation;
    String usagetype;
    String status;
    Boolean delay = false;
}
