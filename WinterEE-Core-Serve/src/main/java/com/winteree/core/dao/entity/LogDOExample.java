package com.winteree.core.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LogDOExample() {
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

        public Criteria andLogTypeIsNull() {
            addCriterion("log_type is null");
            return (Criteria) this;
        }

        public Criteria andLogTypeIsNotNull() {
            addCriterion("log_type is not null");
            return (Criteria) this;
        }

        public Criteria andLogTypeEqualTo(String value) {
            addCriterion("log_type =", value, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeNotEqualTo(String value) {
            addCriterion("log_type <>", value, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeGreaterThan(String value) {
            addCriterion("log_type >", value, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeGreaterThanOrEqualTo(String value) {
            addCriterion("log_type >=", value, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeLessThan(String value) {
            addCriterion("log_type <", value, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeLessThanOrEqualTo(String value) {
            addCriterion("log_type <=", value, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeLike(String value) {
            addCriterion("log_type like", value, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeNotLike(String value) {
            addCriterion("log_type not like", value, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeIn(List<String> values) {
            addCriterion("log_type in", values, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeNotIn(List<String> values) {
            addCriterion("log_type not in", values, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeBetween(String value1, String value2) {
            addCriterion("log_type between", value1, value2, "logType");
            return (Criteria) this;
        }

        public Criteria andLogTypeNotBetween(String value1, String value2) {
            addCriterion("log_type not between", value1, value2, "logType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeIsNull() {
            addCriterion("log_sub_type is null");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeIsNotNull() {
            addCriterion("log_sub_type is not null");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeEqualTo(String value) {
            addCriterion("log_sub_type =", value, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeNotEqualTo(String value) {
            addCriterion("log_sub_type <>", value, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeGreaterThan(String value) {
            addCriterion("log_sub_type >", value, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeGreaterThanOrEqualTo(String value) {
            addCriterion("log_sub_type >=", value, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeLessThan(String value) {
            addCriterion("log_sub_type <", value, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeLessThanOrEqualTo(String value) {
            addCriterion("log_sub_type <=", value, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeLike(String value) {
            addCriterion("log_sub_type like", value, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeNotLike(String value) {
            addCriterion("log_sub_type not like", value, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeIn(List<String> values) {
            addCriterion("log_sub_type in", values, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeNotIn(List<String> values) {
            addCriterion("log_sub_type not in", values, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeBetween(String value1, String value2) {
            addCriterion("log_sub_type between", value1, value2, "logSubType");
            return (Criteria) this;
        }

        public Criteria andLogSubTypeNotBetween(String value1, String value2) {
            addCriterion("log_sub_type not between", value1, value2, "logSubType");
            return (Criteria) this;
        }

        public Criteria andTenantUuidIsNull() {
            addCriterion("tenant_uuid is null");
            return (Criteria) this;
        }

        public Criteria andTenantUuidIsNotNull() {
            addCriterion("tenant_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andTenantUuidEqualTo(String value) {
            addCriterion("tenant_uuid =", value, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidNotEqualTo(String value) {
            addCriterion("tenant_uuid <>", value, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidGreaterThan(String value) {
            addCriterion("tenant_uuid >", value, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidGreaterThanOrEqualTo(String value) {
            addCriterion("tenant_uuid >=", value, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidLessThan(String value) {
            addCriterion("tenant_uuid <", value, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidLessThanOrEqualTo(String value) {
            addCriterion("tenant_uuid <=", value, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidLike(String value) {
            addCriterion("tenant_uuid like", value, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidNotLike(String value) {
            addCriterion("tenant_uuid not like", value, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidIn(List<String> values) {
            addCriterion("tenant_uuid in", values, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidNotIn(List<String> values) {
            addCriterion("tenant_uuid not in", values, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidBetween(String value1, String value2) {
            addCriterion("tenant_uuid between", value1, value2, "tenantUuid");
            return (Criteria) this;
        }

        public Criteria andTenantUuidNotBetween(String value1, String value2) {
            addCriterion("tenant_uuid not between", value1, value2, "tenantUuid");
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

        public Criteria andClientUuidIsNull() {
            addCriterion("client_uuid is null");
            return (Criteria) this;
        }

        public Criteria andClientUuidIsNotNull() {
            addCriterion("client_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andClientUuidEqualTo(String value) {
            addCriterion("client_uuid =", value, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidNotEqualTo(String value) {
            addCriterion("client_uuid <>", value, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidGreaterThan(String value) {
            addCriterion("client_uuid >", value, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidGreaterThanOrEqualTo(String value) {
            addCriterion("client_uuid >=", value, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidLessThan(String value) {
            addCriterion("client_uuid <", value, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidLessThanOrEqualTo(String value) {
            addCriterion("client_uuid <=", value, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidLike(String value) {
            addCriterion("client_uuid like", value, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidNotLike(String value) {
            addCriterion("client_uuid not like", value, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidIn(List<String> values) {
            addCriterion("client_uuid in", values, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidNotIn(List<String> values) {
            addCriterion("client_uuid not in", values, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidBetween(String value1, String value2) {
            addCriterion("client_uuid between", value1, value2, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientUuidNotBetween(String value1, String value2) {
            addCriterion("client_uuid not between", value1, value2, "clientUuid");
            return (Criteria) this;
        }

        public Criteria andClientIpIsNull() {
            addCriterion("client_ip is null");
            return (Criteria) this;
        }

        public Criteria andClientIpIsNotNull() {
            addCriterion("client_ip is not null");
            return (Criteria) this;
        }

        public Criteria andClientIpEqualTo(String value) {
            addCriterion("client_ip =", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotEqualTo(String value) {
            addCriterion("client_ip <>", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpGreaterThan(String value) {
            addCriterion("client_ip >", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpGreaterThanOrEqualTo(String value) {
            addCriterion("client_ip >=", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLessThan(String value) {
            addCriterion("client_ip <", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLessThanOrEqualTo(String value) {
            addCriterion("client_ip <=", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLike(String value) {
            addCriterion("client_ip like", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotLike(String value) {
            addCriterion("client_ip not like", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpIn(List<String> values) {
            addCriterion("client_ip in", values, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotIn(List<String> values) {
            addCriterion("client_ip not in", values, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpBetween(String value1, String value2) {
            addCriterion("client_ip between", value1, value2, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotBetween(String value1, String value2) {
            addCriterion("client_ip not between", value1, value2, "clientIp");
            return (Criteria) this;
        }

        public Criteria andRequestMethodIsNull() {
            addCriterion("request_method is null");
            return (Criteria) this;
        }

        public Criteria andRequestMethodIsNotNull() {
            addCriterion("request_method is not null");
            return (Criteria) this;
        }

        public Criteria andRequestMethodEqualTo(String value) {
            addCriterion("request_method =", value, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodNotEqualTo(String value) {
            addCriterion("request_method <>", value, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodGreaterThan(String value) {
            addCriterion("request_method >", value, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodGreaterThanOrEqualTo(String value) {
            addCriterion("request_method >=", value, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodLessThan(String value) {
            addCriterion("request_method <", value, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodLessThanOrEqualTo(String value) {
            addCriterion("request_method <=", value, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodLike(String value) {
            addCriterion("request_method like", value, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodNotLike(String value) {
            addCriterion("request_method not like", value, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodIn(List<String> values) {
            addCriterion("request_method in", values, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodNotIn(List<String> values) {
            addCriterion("request_method not in", values, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodBetween(String value1, String value2) {
            addCriterion("request_method between", value1, value2, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andRequestMethodNotBetween(String value1, String value2) {
            addCriterion("request_method not between", value1, value2, "requestMethod");
            return (Criteria) this;
        }

        public Criteria andStatusCodeIsNull() {
            addCriterion("status_code is null");
            return (Criteria) this;
        }

        public Criteria andStatusCodeIsNotNull() {
            addCriterion("status_code is not null");
            return (Criteria) this;
        }

        public Criteria andStatusCodeEqualTo(String value) {
            addCriterion("status_code =", value, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeNotEqualTo(String value) {
            addCriterion("status_code <>", value, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeGreaterThan(String value) {
            addCriterion("status_code >", value, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeGreaterThanOrEqualTo(String value) {
            addCriterion("status_code >=", value, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeLessThan(String value) {
            addCriterion("status_code <", value, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeLessThanOrEqualTo(String value) {
            addCriterion("status_code <=", value, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeLike(String value) {
            addCriterion("status_code like", value, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeNotLike(String value) {
            addCriterion("status_code not like", value, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeIn(List<String> values) {
            addCriterion("status_code in", values, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeNotIn(List<String> values) {
            addCriterion("status_code not in", values, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeBetween(String value1, String value2) {
            addCriterion("status_code between", value1, value2, "statusCode");
            return (Criteria) this;
        }

        public Criteria andStatusCodeNotBetween(String value1, String value2) {
            addCriterion("status_code not between", value1, value2, "statusCode");
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