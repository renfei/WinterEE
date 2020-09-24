package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.*;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.AccountDOMapper;
import com.winteree.core.dao.MessageContextDOMapper;
import com.winteree.core.dao.MessageDOMapper;
import com.winteree.core.dao.entity.*;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.MessageService;
import com.winteree.core.service.RoleService;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.ListUtils;
import net.renfei.sdk.utils.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>Title: MessageServiceImpl</p>
 * <p>Description: 消息服务</p>
 *
 * @author RenFei
 * @date : 2020-09-23 21:35
 */
@Service
public class MessageServiceImpl extends BaseService implements MessageService {
    private final RoleService roleService;
    private final AccountService accountService;
    private final MessageDOMapper messageDOMapper;
    private final AccountDOMapper accountDOMapper;
    private final MessageContextDOMapper messageContextDOMapper;

    protected MessageServiceImpl(RoleService roleService,
                                 AccountService accountService,
                                 MessageDOMapper messageDOMapper,
                                 AccountDOMapper accountDOMapper,
                                 WintereeCoreConfig wintereeCoreConfig,
                                 MessageContextDOMapper messageContextDOMapper) {
        super(wintereeCoreConfig);
        this.messageDOMapper = messageDOMapper;
        this.accountService = accountService;
        this.roleService = roleService;
        this.accountDOMapper = accountDOMapper;
        this.messageContextDOMapper = messageContextDOMapper;
    }

    /**
     * 获取我的站内消息列表
     *
     * @param pages 页码
     * @param rows  每页容量
     * @return ListData<MessageVO>
     */
    @Override
    public ListData<MessageVO> getMyStationMessage(String pages, String rows) {
        AccountDTO accountDTO = getSignedUser(accountService);
        MessageDOExample example = new MessageDOExample();
        example.setOrderByClause("sent_date DESC");
        example.createCriteria()
                .andReceiveUuidEqualTo(accountDTO.getUuid())
                .andMsgTypeEqualTo(MsgType.STATION.value());
        Page<MessageDO> page = PageHelper.startPage(NumberUtils.parseInt(pages, 1),
                NumberUtils.parseInt(rows, 10));
        messageDOMapper.selectByExample(example);
        ListData<MessageVO> messageListData = new ListData<>();
        messageListData.setData(convert(page.getResult()));
        messageListData.setTotal(page.getTotal());
        return messageListData;
    }

    /**
     * 读取我的消息内容
     *
     * @param msgUuid 消息编号
     * @return MessageContextVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageContextVO getMyMessage(String msgUuid) {
        AccountDTO accountDTO = getSignedUser(accountService);
        MessageDOExample messageExample = new MessageDOExample();
        messageExample.createCriteria()
                .andReceiveUuidEqualTo(accountDTO.getUuid())
                .andUuidEqualTo(msgUuid);
        MessageDO message = ListUtils.getOne(messageDOMapper.selectByExample(messageExample));
        if (message == null) {
            return null;
        }
        MessageContextDOExample messageContextExample = new MessageContextDOExample();
        messageContextExample.createCriteria().andUuidEqualTo(message.getContextUuid());
        MessageContextDOWithBLOBs messageContext =
                ListUtils.getOne(messageContextDOMapper.selectByExampleWithBLOBs(messageContextExample));
        if (messageContext == null) {
            return null;
        }
        if (!message.getIsRead()) {
            message.setIsRead(true);
            messageDOMapper.updateByExampleSelective(message, messageExample);
        }
        return Builder.of(MessageContextVO::new)
                .with(MessageContextVO::setContent, messageContext.getContent())
                .with(MessageContextVO::setExtendedLinks, messageContext.getExtendedLinks())
                .with(MessageContextVO::setUuid, message.getUuid())
                .with(MessageContextVO::setTitle, message.getTitle())
                .with(MessageContextVO::setSentDate, message.getSentDate())
                .with(MessageContextVO::setSender, convert(message).getSender())
                .with(MessageContextVO::setIsRead, message.getIsRead())
                .build();
    }

    /**
     * 给指定的用户发送消息
     *
     * @param receiveUuid    消息收件人UUID
     * @param messageContext 消息内容
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendP2PMessage(String receiveUuid, MessageContextVO messageContext) throws FailureException {
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            if (!"system".equals(messageContext.getSender())) {
                messageContext.setSender(accountDTO.getUuid());
            }
        } else {
            messageContext.setSender(accountDTO.getUuid());
        }
        com.winteree.api.entity.AccountDTO receiveAccount = accountService.getAccountById(receiveUuid);
        if (receiveAccount == null) {
            throw new FailureException("收件人不存在");
        }
        if (!wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            if (!receiveAccount.getTenantUuid().equals(accountDTO.getTenantUuid())) {
                // 禁止普通用户跨租户发送消息
                throw new FailureException("收件人不存在");
            }
        }
        // 先插入一个消息，但没有执行发送
        String contextUuid = addMessageContext(messageContext.getContent(), messageContext.getExtendedLinks());
        // 执行发送动作
        execSendStationMessage(messageContext, contextUuid, messageContext.getSender(), receiveUuid);
    }

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMessageBroadcasting(MessageScope messageScope,
                                        MessageType messageType,
                                        MessageContextVO messageContext,
                                        String valueUuid) throws FailureException, ForbiddenException {
        AccountDTO accountDTO = getSignedUser(accountService);
        DataScopeEnum dataScopeEnum = roleService.getDataScope();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            if (!"system".equals(messageContext.getSender())) {
                messageContext.setSender(accountDTO.getUuid());
            }
        } else {
            messageContext.setSender(accountDTO.getUuid());
        }
        // 先插入一个消息，但没有执行发送
        String contextUuid = addMessageContext(messageContext.getContent(), messageContext.getExtendedLinks());
        AccountDOExample accountDOExample = new AccountDOExample();
        AccountDOExample.Criteria criteria = accountDOExample.createCriteria();
        switch (messageType) {
            case ALL:
                throw new FailureException("暂时只支持发送站内消息");
            case STATION:
                // 站内消息
                switch (messageScope) {
                    case ALL:
                        // 给所有用户发送
                        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                            break;
                        } else {
                            throw new ForbiddenException("只有超级管理员才能做平台级别广播");
                        }
                    case TENANT:
                        // 给指定的租户全体发送
                        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                            criteria.andTenantUuidEqualTo(valueUuid);
                        } else if (DataScopeEnum.ALL.equals(dataScopeEnum)) {
                            criteria.andTenantUuidEqualTo(accountDTO.getTenantUuid());
                        } else {
                            throw new ForbiddenException("只有租户管理员才能做租户级别广播");
                        }
                        break;
                    case COMPANY:
                        // 给指定的公司用户全体发送
                        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                            criteria.andOfficeUuidEqualTo(valueUuid);
                        } else if (DataScopeEnum.ALL.equals(dataScopeEnum) ||
                                DataScopeEnum.COMPANY.equals(dataScopeEnum)) {
                            criteria.andTenantUuidEqualTo(accountDTO.getTenantUuid())
                                    .andOfficeUuidEqualTo(accountDTO.getOfficeUuid());
                        } else {
                            throw new ForbiddenException("只有公司管理员才能做公司级别广播");
                        }
                        break;
                    case DEPARTMENT:
                        // 给指定的部门全体发送
                        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                            criteria.andDepartmentUuidEqualTo(valueUuid);
                        } else if (DataScopeEnum.ALL.equals(dataScopeEnum) ||
                                DataScopeEnum.COMPANY.equals(dataScopeEnum) ||
                                DataScopeEnum.COMPANY_AND_DEPARTMENT.equals(dataScopeEnum)) {
                            criteria.andTenantUuidEqualTo(accountDTO.getTenantUuid())
                                    .andOfficeUuidEqualTo(accountDTO.getOfficeUuid())
                                    .andDepartmentUuidEqualTo(accountDTO.getDepartmentUuid());
                        } else {
                            throw new ForbiddenException("只有部门管理员才能做部门级别广播");
                        }
                        break;
                    default:
                        throw new ForbiddenException("权限不足");
                }
                // 执行查询用户列表的动作
                List<AccountDO> accountDOS = accountDOMapper.selectByExample(accountDOExample);
                for (AccountDO account : accountDOS
                ) {
                    // 执行发送动作
                    execSendStationMessage(messageContext, contextUuid, messageContext.getSender(), account.getUuid());
                }
                break;
            case APP:
                // app 推送消息
                throw new FailureException("暂时只支持发送站内消息");
            case EMAIL:
                // Email 消息
                throw new FailureException("暂时只支持发送站内消息");
            default:
                throw new FailureException("未知的消息类型");
        }
    }

    /**
     * 执行消息发送的动作
     *
     * @param messageContext 消息内容
     * @param contextUuid    消息正文UUID
     * @param sendUuid       发送者UUID
     * @param receiveUuid    接收者UUID
     */
    private void execSendStationMessage(MessageContextVO messageContext, String contextUuid, String sendUuid, String receiveUuid) {
        MessageDO messageDO = Builder.of(MessageDO::new)
                .with(MessageDO::setUuid, UUID.randomUUID().toString().toUpperCase())
                .with(MessageDO::setContextUuid, contextUuid)
                .with(MessageDO::setMsgType, MessageType.STATION.value())
                .with(MessageDO::setSendUuid, sendUuid)
                .with(MessageDO::setReceiveUuid, receiveUuid)
                .with(MessageDO::setTitle, messageContext.getTitle())
                .with(MessageDO::setIsRead, false)
                .with(MessageDO::setSentDate, new Date())
                .build();
        messageDOMapper.insertSelective(messageDO);
    }

    /**
     * 添加消息正文
     *
     * @param content       消息正文
     * @param extendedLinks 扩展链接
     * @return 消息正文UUID
     */
    public String addMessageContext(String content, String extendedLinks) {
        AccountDTO accountDTO = getSignedUser(accountService);
        String uuid = UUID.randomUUID().toString().toUpperCase();
        MessageContextDOWithBLOBs messageContextDO = Builder.of(MessageContextDOWithBLOBs::new)
                .with(MessageContextDOWithBLOBs::setUuid, uuid)
                .with(MessageContextDOWithBLOBs::setContent, content)
                .with(MessageContextDOWithBLOBs::setExtendedLinks, extendedLinks)
                .with(MessageContextDOWithBLOBs::setCreateBy, accountDTO.getUuid())
                .build();
        messageContextDOMapper.insertSelective(messageContextDO);
        return uuid;
    }

    private List<MessageVO> convert(List<MessageDO> messageDOList) {
        List<MessageVO> messageVOList = new ArrayList<>();
        for (MessageDO messageDO : messageDOList
        ) {
            messageVOList.add(convert(messageDO));
        }
        return messageVOList;
    }

    private MessageVO convert(MessageDO messageDO) {
        String sender = "";
        if ("system".equals(messageDO.getSendUuid())) {
            sender = "系统(System)";
        } else {
            com.winteree.api.entity.AccountDTO accountDTO = accountService.getAccountById(messageDO.getSendUuid());
            sender = accountDTO.getUserName();
        }
        return Builder.of(MessageVO::new)
                .with(MessageVO::setUuid, messageDO.getUuid())
                .with(MessageVO::setTitle, messageDO.getTitle())
                .with(MessageVO::setSentDate, messageDO.getSentDate())
                .with(MessageVO::setSender, sender)
                .with(MessageVO::setIsRead, messageDO.getIsRead())
                .build();
    }
}
