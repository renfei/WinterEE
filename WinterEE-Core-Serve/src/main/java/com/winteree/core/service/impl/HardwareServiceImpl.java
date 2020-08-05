package com.winteree.core.service.impl;

import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.HardwareService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * <p>Title: HardwareServiceImpl</p>
 * <p>Description: 硬件服务</p>
 *
 * @author RenFei
 * @date : 2020-08-05 22:14
 */
@Service
public class HardwareServiceImpl extends BaseService implements HardwareService {
    private final SystemInfo systemInfo = new SystemInfo();

    protected HardwareServiceImpl(WintereeCoreConfig wintereeCoreConfig) {
        super(wintereeCoreConfig);
    }

    @Override
    public String getSerialNumber() {
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();
        Baseboard baseboard = computerSystem.getBaseboard();
        return baseboard.getSerialNumber();
    }
}
