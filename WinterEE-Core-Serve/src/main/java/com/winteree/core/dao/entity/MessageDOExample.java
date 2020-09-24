package com.winteree.core.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MessageDOExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUuidIsNull() {
            addCriterion("uuid is null");
            return (Criteria) this;
        }

        public Criteria andUuidIsNotNull() {
            addCriterion("uuid is not null");
            return (Criteria) this;
        }

        public Criteria andUuidEqualTo(String value) {
            addCriterion("uuid =", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotEqualTo(String value) {
            addCriterion("uuid <>", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThan(String value) {
            addCriterion("uuid >", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThanOrEqualTo(String value) {
            addCriterion("uuid >=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThan(String value) {
            addCriterion("uuid <", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThanOrEqualTo(String value) {
            addCriterion("uuid <=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLike(String value) {
            addCriterion("uuid like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotLike(String value) {
            addCriterion("uuid not like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidIn(List<String> values) {
            addCriterion("uuid in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotIn(List<String> values) {
            addCriterion("uuid not in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidBetween(String value1, String value2) {
            addCriterion("uuid between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotBetween(String value1, String value2) {
            addCriterion("uuid not between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidIsNull() {
            addCriterion("context_uuid is null");
            return (Criteria) this;
        }

        public Criteria andContextUuidIsNotNull() {
            addCriterion("context_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andContextUuidEqualTo(String value) {
            addCriterion("context_uuid =", value, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidNotEqualTo(String value) {
            addCriterion("context_uuid <>", value, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidGreaterThan(String value) {
            addCriterion("context_uuid >", value, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidGreaterThanOrEqualTo(String value) {
            addCriterion("context_uuid >=", value, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidLessThan(String value) {
            addCriterion("context_uuid <", value, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidLessThanOrEqualTo(String value) {
            addCriterion("context_uuid <=", value, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidLike(String value) {
            addCriterion("context_uuid like", value, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidNotLike(String value) {
            addCriterion("context_uuid not like", value, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidIn(List<String> values) {
            addCriterion("context_uuid in", values, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidNotIn(List<String> values) {
            addCriterion("context_uuid not in", values, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidBetween(String value1, String value2) {
            addCriterion("context_uuid between", value1, value2, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andContextUuidNotBetween(String value1, String value2) {
            addCriterion("context_uuid not between", value1, value2, "contextUuid");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNull() {
            addCriterion("msg_type is null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNotNull() {
            addCriterion("msg_type is not null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeEqualTo(Integer value) {
            addCriterion("msg_type =", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotEqualTo(Integer value) {
            addCriterion("msg_type <>", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThan(Integer value) {
            addCriterion("msg_type >", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("msg_type >=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThan(Integer value) {
            addCriterion("msg_type <", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThanOrEqualTo(Integer value) {
            addCriterion("msg_type <=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIn(List<Integer> values) {
            addCriterion("msg_type in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotIn(List<Integer> values) {
            addCriterion("msg_type not in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeBetween(Integer value1, Integer value2) {
            addCriterion("msg_type between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("msg_type not between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andSendUuidIsNull() {
            addCriterion("send_uuid is null");
            return (Criteria) this;
        }

        public Criteria andSendUuidIsNotNull() {
            addCriterion("send_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andSendUuidEqualTo(String value) {
            addCriterion("send_uuid =", value, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidNotEqualTo(String value) {
            addCriterion("send_uuid <>", value, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidGreaterThan(String value) {
            addCriterion("send_uuid >", value, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidGreaterThanOrEqualTo(String value) {
            addCriterion("send_uuid >=", value, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidLessThan(String value) {
            addCriterion("send_uuid <", value, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidLessThanOrEqualTo(String value) {
            addCriterion("send_uuid <=", value, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidLike(String value) {
            addCriterion("send_uuid like", value, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidNotLike(String value) {
            addCriterion("send_uuid not like", value, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidIn(List<String> values) {
            addCriterion("send_uuid in", values, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidNotIn(List<String> values) {
            addCriterion("send_uuid not in", values, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidBetween(String value1, String value2) {
            addCriterion("send_uuid between", value1, value2, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andSendUuidNotBetween(String value1, String value2) {
            addCriterion("send_uuid not between", value1, value2, "sendUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidIsNull() {
            addCriterion("receive_uuid is null");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidIsNotNull() {
            addCriterion("receive_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidEqualTo(String value) {
            addCriterion("receive_uuid =", value, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidNotEqualTo(String value) {
            addCriterion("receive_uuid <>", value, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidGreaterThan(String value) {
            addCriterion("receive_uuid >", value, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidGreaterThanOrEqualTo(String value) {
            addCriterion("receive_uuid >=", value, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidLessThan(String value) {
            addCriterion("receive_uuid <", value, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidLessThanOrEqualTo(String value) {
            addCriterion("receive_uuid <=", value, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidLike(String value) {
            addCriterion("receive_uuid like", value, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidNotLike(String value) {
            addCriterion("receive_uuid not like", value, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidIn(List<String> values) {
            addCriterion("receive_uuid in", values, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidNotIn(List<String> values) {
            addCriterion("receive_uuid not in", values, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidBetween(String value1, String value2) {
            addCriterion("receive_uuid between", value1, value2, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andReceiveUuidNotBetween(String value1, String value2) {
            addCriterion("receive_uuid not between", value1, value2, "receiveUuid");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andIsReadIsNull() {
            addCriterion("is_read is null");
            return (Criteria) this;
        }

        public Criteria andIsReadIsNotNull() {
            addCriterion("is_read is not null");
            return (Criteria) this;
        }

        public Criteria andIsReadEqualTo(Boolean value) {
            addCriterion("is_read =", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadNotEqualTo(Boolean value) {
            addCriterion("is_read <>", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadGreaterThan(Boolean value) {
            addCriterion("is_read >", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_read >=", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadLessThan(Boolean value) {
            addCriterion("is_read <", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadLessThanOrEqualTo(Boolean value) {
            addCriterion("is_read <=", value, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadIn(List<Boolean> values) {
            addCriterion("is_read in", values, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadNotIn(List<Boolean> values) {
            addCriterion("is_read not in", values, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadBetween(Boolean value1, Boolean value2) {
            addCriterion("is_read between", value1, value2, "isRead");
            return (Criteria) this;
        }

        public Criteria andIsReadNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_read not between", value1, value2, "isRead");
            return (Criteria) this;
        }

        public Criteria andSentDateIsNull() {
            addCriterion("sent_date is null");
            return (Criteria) this;
        }

        public Criteria andSentDateIsNotNull() {
            addCriterion("sent_date is not null");
            return (Criteria) this;
        }

        public Criteria andSentDateEqualTo(Date value) {
            addCriterion("sent_date =", value, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateNotEqualTo(Date value) {
            addCriterion("sent_date <>", value, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateGreaterThan(Date value) {
            addCriterion("sent_date >", value, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateGreaterThanOrEqualTo(Date value) {
            addCriterion("sent_date >=", value, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateLessThan(Date value) {
            addCriterion("sent_date <", value, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateLessThanOrEqualTo(Date value) {
            addCriterion("sent_date <=", value, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateIn(List<Date> values) {
            addCriterion("sent_date in", values, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateNotIn(List<Date> values) {
            addCriterion("sent_date not in", values, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateBetween(Date value1, Date value2) {
            addCriterion("sent_date between", value1, value2, "sentDate");
            return (Criteria) this;
        }

        public Criteria andSentDateNotBetween(Date value1, Date value2) {
            addCriterion("sent_date not between", value1, value2, "sentDate");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}