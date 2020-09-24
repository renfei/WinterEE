package com.winteree.api.entity;

import lombok.Data;

/**
 * <p>Title: MessageBroadcastingVO</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-09-24 16:51
 */
@Data
public class MessageBroadcastingVO {
    private MessageScope messageScope;
    private MessageType messageType;
    private MessageContextVO messageContext;
}
