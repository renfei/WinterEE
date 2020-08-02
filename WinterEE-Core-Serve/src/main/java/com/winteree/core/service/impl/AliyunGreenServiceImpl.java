package com.winteree.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.service.AliyunGreenService;
import com.winteree.core.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>Title: AliyunGreenServiceImpl</p>
 * <p>Description: 阿里云绿盾服务</p>
 *
 * @author RenFei
 * @date : 2020-07-28 23:17
 */
@Slf4j
@Service
public class AliyunGreenServiceImpl extends BaseService implements AliyunGreenService {
    private final IAcsClient client;

    protected AliyunGreenServiceImpl(WintereeCoreConfig wintereeCoreConfig) throws ClientException {
        super(wintereeCoreConfig);
        IClientProfile profile = DefaultProfile
                .getProfile(wintereeCoreConfig.getAliyun().getRegionId(),
                        wintereeCoreConfig.getAliyun().getAccessKeyId(),
                        wintereeCoreConfig.getAliyun().getSecret());
        DefaultProfile
                .addEndpoint(wintereeCoreConfig.getAliyunGreen().getEndpointName(),
                        wintereeCoreConfig.getAliyunGreen().getRegionId(), "Green",
                        wintereeCoreConfig.getAliyunGreen().getDomain());
        this.client = new DefaultAcsClient(profile);
    }

    /**
     * 阿里云绿网-文本扫描
     *
     * @param text 待检测的文本，长度不超过10000个字符
     * @return boolean
     */
    @Override
    public boolean textScan(String text) {
        TextScanRequest textScanRequest = new TextScanRequest();
        // 指定API返回格式。
        textScanRequest.setAcceptFormat(FormatType.JSON);
        textScanRequest.setHttpContentType(FormatType.JSON);
        // 指定请求方法。
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST);
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(wintereeCoreConfig.getAliyunGreen().getRegionId());
        List<Map<String, Object>> tasks = new ArrayList<>();
        Map<String, Object> task1 = new LinkedHashMap<>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * 待检测的文本，长度不超过10000个字符。
         */
        task1.put("content", text);
        tasks.add(task1);
        JSONObject data = new JSONObject();

        /**
         * 检测场景。文本垃圾检测请传递 antispam。
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        log.debug(JSON.toJSONString(data, true));
        try {
            textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
            // 请务必设置超时时间。
            textScanRequest.setConnectTimeout(3000);
            textScanRequest.setReadTimeout(6000);
            HttpResponse httpResponse = this.client.doAction(textScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                log.debug(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if (200 == ((JSONObject) taskResult).getInteger("code")) {
                            JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject) sceneResult).getString("scene");
                                String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                                //根据scene和suggetion做相关处理。
                                //suggestion == pass表示未命中垃圾。suggestion == block表示命中了垃圾，可以通过label字段查看命中的垃圾分类。
                                log.debug("args = [" + scene + "]");
                                log.debug("args = [" + suggestion + "]");
                                return "pass".equals(suggestion.toLowerCase());
                            }
                        } else {
                            log.error("task process fail:" + ((JSONObject) taskResult).getInteger("code"));
                        }
                    }
                } else {
                    log.error("detect not success. code:" + scrResponse.getInteger("code"));
                }
            } else {
                log.error("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            log.error(e.getMessage(), e);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
