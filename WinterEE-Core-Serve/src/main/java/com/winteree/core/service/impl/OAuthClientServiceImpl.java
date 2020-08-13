package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.ListData;
import com.winteree.api.entity.OAuthClientDTO;
import com.winteree.api.exception.FailureException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.OAuthClientDOMapper;
import com.winteree.core.dao.entity.OAuthClientDO;
import com.winteree.core.dao.entity.OAuthClientDOExample;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.I18nMessageService;
import com.winteree.core.service.OAuthClientService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.ListUtils;
import net.renfei.sdk.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: OAuthClientServiceImpl</p>
 * <p>Description: OAuth客户端服务</p>
 *
 * @author RenFei
 * @date : 2020-04-27 14:46
 */
@Slf4j
@Service
public class OAuthClientServiceImpl extends BaseService implements OAuthClientService {
    private final OAuthClientDOMapper oAuthClientDOMapper;
    private final I18nMessageService i18nMessageService;

    protected OAuthClientServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                     OAuthClientDOMapper oAuthClientDOMapper,
                                     I18nMessageService i18nMessageService) {
        super(wintereeCoreConfig);
        this.oAuthClientDOMapper = oAuthClientDOMapper;
        this.i18nMessageService = i18nMessageService;
    }

    /**
     * 获取所有OAtuh客户端列表
     *
     * @param page 页数
     * @param rows 容量
     * @return
     */
    @Override
    public ListData<OAuthClientDTO> getOAuthClientAllList(int page, int rows) throws FailureException {
        OAuthClientDOExample oAuthClientDOExample = new OAuthClientDOExample();
        oAuthClientDOExample.createCriteria();
        Page page1 = PageHelper.startPage(page, rows);
        try {
            List<OAuthClientDO> oAuthClientDOS = oAuthClientDOMapper.selectByExampleWithBLOBs(oAuthClientDOExample);
            ListData<OAuthClientDTO> oAuthClientDTOListData = new ListData<>();
            List<OAuthClientDTO> oAuthClientDTOS = new ArrayList<>();
            oAuthClientDTOListData.setTotal(0L);
            oAuthClientDTOListData.setData(oAuthClientDTOS);
            if (BeanUtils.isEmpty(oAuthClientDOS)) {
                return oAuthClientDTOListData;
            }
            for (OAuthClientDO oaDO : oAuthClientDOS
            ) {
                oAuthClientDTOS.add(convert(oaDO));
            }
            oAuthClientDTOListData.setTotal(page1.getTotal());
            oAuthClientDTOListData.setData(oAuthClientDTOS);
            return oAuthClientDTOListData;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("Failure");
        }
    }

    /**
     * 根据 clientId 获取OAuth客户端信息
     *
     * @param clientId 客户端ID
     * @return
     */
    @Override
    public OAuthClientDTO getOAuthClientByClientId(String clientId) {
        OAuthClientDOExample oAuthClientDOExample = new OAuthClientDOExample();
        oAuthClientDOExample.createCriteria().andClientIdEqualTo(clientId);
        OAuthClientDO oAuthClientDO = ListUtils.getOne(oAuthClientDOMapper.selectByExampleWithBLOBs(oAuthClientDOExample));
        if (oAuthClientDO == null) {
            return null;
        }
        return convert(oAuthClientDO);
    }

    /**
     * 添加OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    @Override
    public String addOAuthClient(OAuthClientDTO oAuthClientDTO) throws FailureException {
        if (BeanUtils.isEmpty(oAuthClientDTO.getClientId())) {
            throw new FailureException("ClientId " + i18nMessageService.getMessage(oAuthClientDTO.getLang(), "core.cannotbeempty", "不能为空"));
        }
        OAuthClientDOExample oAuthClientDOExample = new OAuthClientDOExample();
        oAuthClientDOExample.createCriteria().andClientIdEqualTo(oAuthClientDTO.getClientId());
        List<OAuthClientDO> oAuthClientDOS = oAuthClientDOMapper.selectByExample(oAuthClientDOExample);
        if (oAuthClientDOS != null && oAuthClientDOS.size() > 0) {
            throw new FailureException("ClientId " + i18nMessageService.getMessage(oAuthClientDTO.getLang(), "core.occupied", "已被占用"));
        }
        String clientSecret = StringUtils.getRandomString(32);
        oAuthClientDTO.setCreateTime(new Date());
        OAuthClientDO oAuthClientDO = convert(oAuthClientDTO);
        oAuthClientDO.setClientSecret(clientSecret);
        try {
            oAuthClientDOMapper.insertSelective(oAuthClientDO);
            return clientSecret;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("Failure");
        }
    }

    /**
     * 修改OAtuh客户端
     *
     * @param oAuthClientDTO OAtuh客户端
     * @return
     */
    @Override
    public int updateOAuthClient(OAuthClientDTO oAuthClientDTO) throws FailureException {
        if (BeanUtils.isEmpty(oAuthClientDTO.getClientId())) {
            throw new FailureException("ClientId " + i18nMessageService.getMessage(oAuthClientDTO.getLang(), "core.cannotbeempty", "不能为空"));
        }
        oAuthClientDTO.setCreateTime(null);
        OAuthClientDO oAuthClientDO = convert(oAuthClientDTO);
        OAuthClientDOExample oAuthClientDOExample = new OAuthClientDOExample();
        oAuthClientDOExample.createCriteria().andClientIdEqualTo(oAuthClientDTO.getClientId());
        try {
            int updated = oAuthClientDOMapper.updateByExampleSelective(oAuthClientDO, oAuthClientDOExample);
            if (updated == 0) {
                throw new FailureException("Failure");
            }
            return 1;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("Failure");
        }
    }

    /**
     * 删除OAtuh客户端
     *
     * @param clientId clientId
     * @return
     */
    @Override
    public int deleteOAuthClient(String clientId) throws FailureException {
        OAuthClientDOExample oAuthClientDOExample = new OAuthClientDOExample();
        oAuthClientDOExample.createCriteria().andClientIdEqualTo(clientId);
        try {
            int deleted = oAuthClientDOMapper.deleteByExample(oAuthClientDOExample);
            if (deleted == 0) {
                throw new FailureException("Failure");
            }
            return deleted;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("Failure");
        }
    }

    private OAuthClientDTO convert(OAuthClientDO oAuthClientDO) {
        return Builder.of(OAuthClientDTO::new)
                .with(OAuthClientDTO::setClientId, oAuthClientDO.getClientId())
                .with(OAuthClientDTO::setResourceIds, oAuthClientDO.getResourceIds())
                .with(OAuthClientDTO::setScope, oAuthClientDO.getScope())
                .with(OAuthClientDTO::setAuthorities, oAuthClientDO.getAuthorities())
                .with(OAuthClientDTO::setAuthorizedGrantTypes, oAuthClientDO.getAuthorizedGrantTypes())
                .with(OAuthClientDTO::setWebServerRedirectUri, oAuthClientDO.getWebServerRedirectUri())
                .with(OAuthClientDTO::setAccessTokenValidity, oAuthClientDO.getAccessTokenValidity())
                .with(OAuthClientDTO::setRefreshTokenValidity, oAuthClientDO.getRefreshTokenValidity())
                .with(OAuthClientDTO::setCreateTime, oAuthClientDO.getCreateTime())
                .with(OAuthClientDTO::setArchived, oAuthClientDO.getArchived())
                .with(OAuthClientDTO::setTrusted, oAuthClientDO.getTrusted())
                .with(OAuthClientDTO::setAutoapprove, oAuthClientDO.getAutoapprove())
                .with(OAuthClientDTO::setAdditionalInformation, oAuthClientDO.getAdditionalInformation())
                .with(OAuthClientDTO::setTenantUuid, oAuthClientDO.getTenantUuid())
                .build();
    }

    private OAuthClientDO convert(OAuthClientDTO oAuthClientDTO) {
        return Builder.of(OAuthClientDO::new)
                .with(OAuthClientDO::setClientId, oAuthClientDTO.getClientId())
                .with(OAuthClientDO::setResourceIds, oAuthClientDTO.getResourceIds())
                .with(OAuthClientDO::setScope, oAuthClientDTO.getScope())
                .with(OAuthClientDO::setAuthorities, oAuthClientDTO.getAuthorities())
                .with(OAuthClientDO::setAuthorizedGrantTypes, oAuthClientDTO.getAuthorizedGrantTypes())
                .with(OAuthClientDO::setWebServerRedirectUri, oAuthClientDTO.getWebServerRedirectUri())
                .with(OAuthClientDO::setAccessTokenValidity, oAuthClientDTO.getAccessTokenValidity())
                .with(OAuthClientDO::setRefreshTokenValidity, oAuthClientDTO.getRefreshTokenValidity())
                .with(OAuthClientDO::setCreateTime, oAuthClientDTO.getCreateTime())
                .with(OAuthClientDO::setArchived, oAuthClientDTO.getArchived())
                .with(OAuthClientDO::setTrusted, oAuthClientDTO.getTrusted())
                .with(OAuthClientDO::setAutoapprove, oAuthClientDTO.getAutoapprove())
                .with(OAuthClientDO::setAdditionalInformation, oAuthClientDTO.getAdditionalInformation())
                .with(OAuthClientDO::setTenantUuid, oAuthClientDTO.getTenantUuid())
                .build();
    }
}
