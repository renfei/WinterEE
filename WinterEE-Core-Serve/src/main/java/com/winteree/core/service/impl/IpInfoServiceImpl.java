package com.winteree.core.service.impl;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.winteree.api.entity.IpInfoDTO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.IpInfoService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;

/**
 * <p>Title: IpInfoServiceImpl</p>
 * <p>Description: IP信息服务</p>
 *
 * @author RenFei
 * @date : 2020-07-27 20:48
 */
@Slf4j
@Service
public class IpInfoServiceImpl extends BaseService implements IpInfoService {
    private static final String OK = "OK";
    private static final String EMPTY_IP_ADDRESS = "EMPTY_IP_ADDRESS";
    private static final String INVALID_IP_ADDRESS = "INVALID_IP_ADDRESS";
    private static final String MISSING_FILE = "MISSING_FILE";
    private static final String IPV6_NOT_SUPPORTED = "IPV6_NOT_SUPPORTED";
    private IP2Location ip2LocationV4;
    private IP2Location ip2LocationV6;

    /**
     * 由于 Github 阻止大文件提交
     * IP 的数据库文件过大，无法提交，你可以到下面的网址下载，装载在自己的程序中
     * https://cdn.renfei.net/WinterEE/Data/IPV4_DATA_2020_07.BIN
     * https://cdn.renfei.net/WinterEE/Data/IPV6_DATA_2020_07.BIN
     *
     * @param wintereeCoreConfig
     * @throws IOException
     */
    protected IpInfoServiceImpl(WintereeCoreConfig wintereeCoreConfig) throws IOException {
        super(wintereeCoreConfig);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] ipv4Resources = resolver.getResources(wintereeCoreConfig.getIpv4DataPath());
        if (ipv4Resources.length > 0) {
            Resource resourceV4 = ipv4Resources[0];
            this.ip2LocationV4 = new IP2Location();
            this.ip2LocationV4.IPDatabasePath = resourceV4.getFile().getPath();
        }
        Resource[] ipv6Resources = resolver.getResources(wintereeCoreConfig.getIpv6DataPath());
        if (ipv6Resources.length > 0) {
            Resource resourceV6 = ipv6Resources[0];
            this.ip2LocationV6 = new IP2Location();
            this.ip2LocationV6.IPDatabasePath = resourceV6.getFile().getPath();
        }
    }

    @Override
    public APIResult<IpInfoDTO> query(String ip) {
        if (BeanUtils.isEmpty(ip)) {
            return APIResult.builder().code(StateCode.Failure).message("IP address cannot be null.").build();
        }
        IPResult rec;
        try {
            BigInteger bIp = StringUtils.stringToBigInt(ip);
            if (BigInteger.valueOf(4294967295L).compareTo(bIp) == 1) {
                // IPv4
                if (this.ip2LocationV4 != null){
                    return APIResult.builder().code(StateCode.Failure).message("IPv4 is not supported").build();
                }
                rec = this.ip2LocationV4.IPQuery(ip);
            } else if (this.ip2LocationV6 != null) {
                // IPv6
                rec = this.ip2LocationV6.IPQuery(ip);
            } else {
                return APIResult.builder().code(StateCode.Failure).message("IPv6 is not supported").build();
            }
            if (OK.equals(rec.getStatus())) {
                return APIResult.builder().code(StateCode.OK).message("OK").data(convert(rec)).build();
            } else if (EMPTY_IP_ADDRESS.equals(rec.getStatus())) {
                return APIResult.builder().code(StateCode.Failure).message("IP address cannot be blank.").build();
            } else if (INVALID_IP_ADDRESS.equals(rec.getStatus())) {
                return APIResult.builder().code(StateCode.Failure).message("Invalid IP address.").build();
            } else if (MISSING_FILE.equals(rec.getStatus())) {
                return APIResult.builder().code(StateCode.Failure).message("Invalid database path.").build();
            } else if (IPV6_NOT_SUPPORTED.equals(rec.getStatus())) {
                return APIResult.builder().code(StateCode.Failure).message("This BIN does not contain IPv6 data.").build();
            } else {
                return APIResult.builder().code(StateCode.Failure).message("Unknown error." + rec.getStatus()).build();
            }
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
            return APIResult.builder().code(StateCode.Failure).message("IOException.").build();
        }
    }

    private IpInfoDTO convert(IPResult ipResult) {
        return Builder.of(IpInfoDTO::new)
                .with(IpInfoDTO::setIpAddress, ipResult.getIp_address())
                .with(IpInfoDTO::setCountryShort, ipResult.getCountry_short())
                .with(IpInfoDTO::setCountryLong, ipResult.getCountry_long())
                .with(IpInfoDTO::setRegion, ipResult.getRegion())
                .with(IpInfoDTO::setCity, ipResult.getCity())
                .with(IpInfoDTO::setIsp, ipResult.getISP())
                .with(IpInfoDTO::setLatitude, ipResult.getLatitude())
                .with(IpInfoDTO::setLongitude, ipResult.getLongitude())
                .with(IpInfoDTO::setDomain, ipResult.getDomain())
                .with(IpInfoDTO::setZipcode, ipResult.getZipCode())
                .with(IpInfoDTO::setNetspeed, ipResult.getNetSpeed())
                .with(IpInfoDTO::setTimezone, ipResult.getTimeZone())
                .with(IpInfoDTO::setIddcode, ipResult.getIDDCode())
                .with(IpInfoDTO::setAreacode, ipResult.getAreaCode())
                .with(IpInfoDTO::setWeatherstationcode, ipResult.getWeatherStationCode())
                .with(IpInfoDTO::setWeatherstationname, ipResult.getWeatherStationName())
                .with(IpInfoDTO::setMcc, ipResult.getMCC())
                .with(IpInfoDTO::setMnc, ipResult.getMNC())
                .with(IpInfoDTO::setMobilebrand, ipResult.getMobileBrand())
                .with(IpInfoDTO::setElevation, ipResult.getElevation())
                .with(IpInfoDTO::setUsagetype, ipResult.getUsageType())
                .with(IpInfoDTO::setStatus, ipResult.getStatus())
                .build();
    }
}
