package com.winteree.core.service.impl;

import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.CoreSettingDOMapper;
import com.winteree.core.dao.entity.CoreSettingDO;
import com.winteree.core.dao.entity.CoreSettingDOExample;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.CoreSettingService;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>Title: CoreSettingServiceImpl</p>
 * <p>Description: 核心设置服务</p>
 *
 * @author RenFei
 * @date : 2020-08-05 22:31
 */
@Service
public class CoreSettingServiceImpl extends BaseService implements CoreSettingService {
    private final CoreSettingDOMapper coreSettingDOMapper;
    private final static String LICENSE_KEY = "LICENSE";

    protected CoreSettingServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                     CoreSettingDOMapper coreSettingDOMapper) {
        super(wintereeCoreConfig);
        this.coreSettingDOMapper = coreSettingDOMapper;
    }

    @Override
    public String getLicense() {
        return ListUtils.getOne(this.getValuesByKey(LICENSE_KEY));
    }

    @Override
    public int setLicense(String license) {
        if (getLicense() == null) {
            return addValues(LICENSE_KEY, new ArrayList<String>() {{
                add(license);
            }});
        } else {
            return updateByKey(LICENSE_KEY, license);
        }
    }

    @Override
    public int addValues(String key, List<String> values) {
        if (BeanUtils.isEmpty(values)) {
            return 0;
        }
        int num = 0;
        for (String value : values
        ) {
            CoreSettingDO coreSettingDO = new CoreSettingDO();
            coreSettingDO.setCreateTime(new Date());
            coreSettingDO.setMyKey(key);
            coreSettingDO.setUuid(UUID.randomUUID().toString().toUpperCase());
            coreSettingDO.setMyValue(value);
            coreSettingDOMapper.insertSelective(coreSettingDO);
            num++;
        }
        return num;
    }

    @Override
    public int updateByKey(String key, String value) {
        CoreSettingDOExample example = new CoreSettingDOExample();
        example.createCriteria().andMyKeyEqualTo(key);
        CoreSettingDO coreSettingDO = new CoreSettingDO();
        coreSettingDO.setMyValue(value);
        coreSettingDO.setUpdateTime(new Date());
        return coreSettingDOMapper.updateByExampleSelective(coreSettingDO, example);
    }

    @Override
    public int deleteByKey(String key) {
        CoreSettingDOExample example = new CoreSettingDOExample();
        example.createCriteria().andMyKeyEqualTo(key);
        return coreSettingDOMapper.deleteByExample(example);
    }

    @Override
    public List<String> getValuesByKey(String key) {
        CoreSettingDOExample example = new CoreSettingDOExample();
        example.createCriteria().andMyKeyEqualTo(key);
        List<CoreSettingDO> coreSettingDOS = coreSettingDOMapper.selectByExample(example);
        if (BeanUtils.isEmpty(coreSettingDOS)) {
            return null;
        }
        List<String> values = new ArrayList<>();
        coreSettingDOS.forEach(coreSettingDO -> values.add(coreSettingDO.getMyValue()));
        return values;
    }
}
