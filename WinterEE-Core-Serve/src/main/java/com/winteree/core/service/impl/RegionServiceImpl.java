package com.winteree.core.service.impl;

import com.winteree.api.entity.RegionDTO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.RegionDOMapper;
import com.winteree.core.dao.entity.RegionDO;
import com.winteree.core.dao.entity.RegionDOExample;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.RegionService;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Title: RegionServiceImpl</p>
 * <p>Description: 行政区划服务</p>
 *
 * @author RenFei
 * @date : 2020-07-21 22:03
 */
@Service
public class RegionServiceImpl extends BaseService implements RegionService {
    private final RegionDOMapper regionDOMapper;

    protected RegionServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                RegionDOMapper regionDOMapper) {
        super(wintereeCoreConfig);
        this.regionDOMapper = regionDOMapper;
    }

    /**
     * 根据行政代码查询行政区划数据
     *
     * @param code 行政代码
     * @return RegionDTO
     */
    @Override
    public RegionDTO getRegionByCode(String code) {
        RegionDOExample example = new RegionDOExample();
        example.createCriteria().andRegionCodeEqualTo(code);
        RegionDO regionDO = ListUtils.getOne(regionDOMapper.selectByExample(example));
        if (regionDO == null) {
            return null;
        }
        return convert(regionDO);
    }

    /**
     * 获取子级行政区划列表
     *
     * @param code 本级行政代码，为空时查询顶级行政区划
     * @return List<RegionDTO>
     */
    @Override
    public List<RegionDTO> getChildRegion(String code) {
        RegionDOExample example = new RegionDOExample();
        if (code == null || code.length() == 0) {
            // 获取顶级行政区划代码
            example.createCriteria().andRegionCodeLike("__0000");
        } else {
            if (code.length() != 6) {
                // 行政代码必须是6位的
                return null;
            }
            // 获取子级行政代码
            if (code.endsWith("0000")) {
                String startCode = code.substring(0, 2);
                example.createCriteria().andRegionCodeLike(startCode + "__00");
            } else if (code.endsWith("00")) {
                String startCode = code.substring(0, 4);
                example.createCriteria().andRegionCodeLike(startCode + "__");
            } else {
                return null;
            }
        }
        List<RegionDO> regionDOS = regionDOMapper.selectByExample(example);
        if (BeanUtils.isEmpty(regionDOS)) {
            return null;
        }
        List<RegionDTO> regionDTOS = new ArrayList<>();
        regionDOS.forEach(regionDO -> regionDTOS.add(convert(regionDO)));
        Iterator<RegionDTO> itr = regionDTOS.iterator();
        while (itr.hasNext()) {
            RegionDTO regionDTO = itr.next();
            if (regionDTO.getRegionCode().equals(code)) {
                itr.remove();
                break;
            }
        }
        return regionDTOS;
    }

    private RegionDTO convert(RegionDO regionDO) {
        return Builder.of(RegionDTO::new)
                .with(RegionDTO::setRegionCode, regionDO.getRegionCode())
                .with(RegionDTO::setRegionName, regionDO.getRegionName())
                .build();
    }

    private RegionDO convert(RegionDTO regionDTO) {
        return Builder.of(RegionDO::new)
                .with(RegionDO::setRegionCode, regionDTO.getRegionCode())
                .with(RegionDO::setRegionName, regionDTO.getRegionName())
                .build();
    }
}
