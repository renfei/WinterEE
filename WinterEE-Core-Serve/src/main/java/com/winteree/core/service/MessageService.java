package com.winteree.core.service;

import com.winteree.api.entity.*;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;

/**
 * <p>Title: MessageService</p>
 * <p>Description: 消息服务</p>
 *
 * @author RenFei
 * @date : 2020-09-23 21:35
 */
public interface MessageService {
    /**
     * 获取我的站内消息列表
     *
     * @param pages 页码
     * @param rows  每页容量
     * @return ListData<MessageVO>
     */
    ListData<MessageVO> getMyStationMessage(String pages, String rows);

    /**
     * 读取我的消息内容
     *
     * @param msgUuid 消息编号
     * @return MessageContextVO
     */
    MessageContextVO getMyMessage(String msgUuid);

    /**
     * 给指定的用户发送消息
     *
     * @param receiveUuid    消息收件人UUID
     * @param messageContext 消息内容
     */
    void sendP2PMessage(String receiveUuid, MessageContextVO messageContext) throws FailureException;

    /**
     * 发送消息广播
     *
     * @param messageScope   消息广播范围
     * @param messageType    消息类型
     * @param messageContext 消息内容
     * @param valueUuid      消息广播范围参数
     * @throws FailureException   操作失败
     * @throws ForbiddenException 权限不足
     */
    void sendMessageBroadcasting(MessageScope messageScope,
                                 MessageType messageType,
                                 MessageContextVO messageContext,
                                 String valueUuid) throws FailureException, ForbiddenException;
}
