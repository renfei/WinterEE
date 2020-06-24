package com.winteree.core.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VerificationCodeDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public VerificationCodeDOExample() {
        oredCriteria = new ArrayList<Criteria>();
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
            criteria = new ArrayList<Criterion>();
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

        public Criteria andAccountUuidIsNull() {
            addCriterion("account_uuid is null");
            return (Criteria) this;
        }

        public Criteria andAccountUuidIsNotNull() {
            addCriterion("account_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andAccountUuidEqualTo(String value) {
            addCriterion("account_uuid =", value, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidNotEqualTo(String value) {
            addCriterion("account_uuid <>", value, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidGreaterThan(String value) {
            addCriterion("account_uuid >", value, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidGreaterThanOrEqualTo(String value) {
            addCriterion("account_uuid >=", value, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidLessThan(String value) {
            addCriterion("account_uuid <", value, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidLessThanOrEqualTo(String value) {
            addCriterion("account_uuid <=", value, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidLike(String value) {
            addCriterion("account_uuid like", value, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidNotLike(String value) {
            addCriterion("account_uuid not like", value, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidIn(List<String> values) {
            addCriterion("account_uuid in", values, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidNotIn(List<String> values) {
            addCriterion("account_uuid not in", values, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidBetween(String value1, String value2) {
            addCriterion("account_uuid between", value1, value2, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andAccountUuidNotBetween(String value1, String value2) {
            addCriterion("account_uuid not between", value1, value2, "accountUuid");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeIsNull() {
            addCriterion("verification_code is null");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeIsNotNull() {
            addCriterion("verification_code is not null");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeEqualTo(String value) {
            addCriterion("verification_code =", value, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeNotEqualTo(String value) {
            addCriterion("verification_code <>", value, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeGreaterThan(String value) {
            addCriterion("verification_code >", value, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeGreaterThanOrEqualTo(String value) {
            addCriterion("verification_code >=", value, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeLessThan(String value) {
            addCriterion("verification_code <", value, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeLessThanOrEqualTo(String value) {
            addCriterion("verification_code <=", value, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeLike(String value) {
            addCriterion("verification_code like", value, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeNotLike(String value) {
            addCriterion("verification_code not like", value, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeIn(List<String> values) {
            addCriterion("verification_code in", values, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeNotIn(List<String> values) {
            addCriterion("verification_code not in", values, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeBetween(String value1, String value2) {
            addCriterion("verification_code between", value1, value2, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andVerificationCodeNotBetween(String value1, String value2) {
            addCriterion("verification_code not between", value1, value2, "verificationCode");
            return (Criteria) this;
        }

        public Criteria andDeadDateIsNull() {
            addCriterion("dead_date is null");
            return (Criteria) this;
        }

        public Criteria andDeadDateIsNotNull() {
            addCriterion("dead_date is not null");
            return (Criteria) this;
        }

        public Criteria andDeadDateEqualTo(Date value) {
            addCriterion("dead_date =", value, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateNotEqualTo(Date value) {
            addCriterion("dead_date <>", value, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateGreaterThan(Date value) {
            addCriterion("dead_date >", value, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateGreaterThanOrEqualTo(Date value) {
            addCriterion("dead_date >=", value, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateLessThan(Date value) {
            addCriterion("dead_date <", value, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateLessThanOrEqualTo(Date value) {
            addCriterion("dead_date <=", value, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateIn(List<Date> values) {
            addCriterion("dead_date in", values, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateNotIn(List<Date> values) {
            addCriterion("dead_date not in", values, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateBetween(Date value1, Date value2) {
            addCriterion("dead_date between", value1, value2, "deadDate");
            return (Criteria) this;
        }

        public Criteria andDeadDateNotBetween(Date value1, Date value2) {
            addCriterion("dead_date not between", value1, value2, "deadDate");
            return (Criteria) this;
        }

        public Criteria andUsableIsNull() {
            addCriterion("usable is null");
            return (Criteria) this;
        }

        public Criteria andUsableIsNotNull() {
            addCriterion("usable is not null");
            return (Criteria) this;
        }

        public Criteria andUsableEqualTo(Byte value) {
            addCriterion("usable =", value, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableNotEqualTo(Byte value) {
            addCriterion("usable <>", value, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableGreaterThan(Byte value) {
            addCriterion("usable >", value, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableGreaterThanOrEqualTo(Byte value) {
            addCriterion("usable >=", value, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableLessThan(Byte value) {
            addCriterion("usable <", value, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableLessThanOrEqualTo(Byte value) {
            addCriterion("usable <=", value, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableIn(List<Byte> values) {
            addCriterion("usable in", values, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableNotIn(List<Byte> values) {
            addCriterion("usable not in", values, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableBetween(Byte value1, Byte value2) {
            addCriterion("usable between", value1, value2, "usable");
            return (Criteria) this;
        }

        public Criteria andUsableNotBetween(Byte value1, Byte value2) {
            addCriterion("usable not between", value1, value2, "usable");
            return (Criteria) this;
        }

        public Criteria andSendedIsNull() {
            addCriterion("sended is null");
            return (Criteria) this;
        }

        public Criteria andSendedIsNotNull() {
            addCriterion("sended is not null");
            return (Criteria) this;
        }

        public Criteria andSendedEqualTo(Byte value) {
            addCriterion("sended =", value, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedNotEqualTo(Byte value) {
            addCriterion("sended <>", value, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedGreaterThan(Byte value) {
            addCriterion("sended >", value, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedGreaterThanOrEqualTo(Byte value) {
            addCriterion("sended >=", value, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedLessThan(Byte value) {
            addCriterion("sended <", value, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedLessThanOrEqualTo(Byte value) {
            addCriterion("sended <=", value, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedIn(List<Byte> values) {
            addCriterion("sended in", values, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedNotIn(List<Byte> values) {
            addCriterion("sended not in", values, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedBetween(Byte value1, Byte value2) {
            addCriterion("sended between", value1, value2, "sended");
            return (Criteria) this;
        }

        public Criteria andSendedNotBetween(Byte value1, Byte value2) {
            addCriterion("sended not between", value1, value2, "sended");
            return (Criteria) this;
        }

        public Criteria andContentTextIsNull() {
            addCriterion("content_text is null");
            return (Criteria) this;
        }

        public Criteria andContentTextIsNotNull() {
            addCriterion("content_text is not null");
            return (Criteria) this;
        }

        public Criteria andContentTextEqualTo(String value) {
            addCriterion("content_text =", value, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextNotEqualTo(String value) {
            addCriterion("content_text <>", value, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextGreaterThan(String value) {
            addCriterion("content_text >", value, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextGreaterThanOrEqualTo(String value) {
            addCriterion("content_text >=", value, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextLessThan(String value) {
            addCriterion("content_text <", value, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextLessThanOrEqualTo(String value) {
            addCriterion("content_text <=", value, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextLike(String value) {
            addCriterion("content_text like", value, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextNotLike(String value) {
            addCriterion("content_text not like", value, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextIn(List<String> values) {
            addCriterion("content_text in", values, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextNotIn(List<String> values) {
            addCriterion("content_text not in", values, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextBetween(String value1, String value2) {
            addCriterion("content_text between", value1, value2, "contentText");
            return (Criteria) this;
        }

        public Criteria andContentTextNotBetween(String value1, String value2) {
            addCriterion("content_text not between", value1, value2, "contentText");
            return (Criteria) this;
        }

        public Criteria andValidationTypeIsNull() {
            addCriterion("validation_type is null");
            return (Criteria) this;
        }

        public Criteria andValidationTypeIsNotNull() {
            addCriterion("validation_type is not null");
            return (Criteria) this;
        }

        public Criteria andValidationTypeEqualTo(String value) {
            addCriterion("validation_type =", value, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeNotEqualTo(String value) {
            addCriterion("validation_type <>", value, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeGreaterThan(String value) {
            addCriterion("validation_type >", value, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeGreaterThanOrEqualTo(String value) {
            addCriterion("validation_type >=", value, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeLessThan(String value) {
            addCriterion("validation_type <", value, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeLessThanOrEqualTo(String value) {
            addCriterion("validation_type <=", value, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeLike(String value) {
            addCriterion("validation_type like", value, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeNotLike(String value) {
            addCriterion("validation_type not like", value, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeIn(List<String> values) {
            addCriterion("validation_type in", values, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeNotIn(List<String> values) {
            addCriterion("validation_type not in", values, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeBetween(String value1, String value2) {
            addCriterion("validation_type between", value1, value2, "validationType");
            return (Criteria) this;
        }

        public Criteria andValidationTypeNotBetween(String value1, String value2) {
            addCriterion("validation_type not between", value1, value2, "validationType");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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