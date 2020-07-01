package com.winteree.core.dao.entity;

import java.util.ArrayList;
import java.util.List;

public class CmsTagPostsDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CmsTagPostsDOExample() {
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

        public Criteria andTagUuidIsNull() {
            addCriterion("tag_uuid is null");
            return (Criteria) this;
        }

        public Criteria andTagUuidIsNotNull() {
            addCriterion("tag_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andTagUuidEqualTo(String value) {
            addCriterion("tag_uuid =", value, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidNotEqualTo(String value) {
            addCriterion("tag_uuid <>", value, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidGreaterThan(String value) {
            addCriterion("tag_uuid >", value, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidGreaterThanOrEqualTo(String value) {
            addCriterion("tag_uuid >=", value, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidLessThan(String value) {
            addCriterion("tag_uuid <", value, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidLessThanOrEqualTo(String value) {
            addCriterion("tag_uuid <=", value, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidLike(String value) {
            addCriterion("tag_uuid like", value, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidNotLike(String value) {
            addCriterion("tag_uuid not like", value, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidIn(List<String> values) {
            addCriterion("tag_uuid in", values, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidNotIn(List<String> values) {
            addCriterion("tag_uuid not in", values, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidBetween(String value1, String value2) {
            addCriterion("tag_uuid between", value1, value2, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andTagUuidNotBetween(String value1, String value2) {
            addCriterion("tag_uuid not between", value1, value2, "tagUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidIsNull() {
            addCriterion("post_uuid is null");
            return (Criteria) this;
        }

        public Criteria andPostUuidIsNotNull() {
            addCriterion("post_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andPostUuidEqualTo(String value) {
            addCriterion("post_uuid =", value, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidNotEqualTo(String value) {
            addCriterion("post_uuid <>", value, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidGreaterThan(String value) {
            addCriterion("post_uuid >", value, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidGreaterThanOrEqualTo(String value) {
            addCriterion("post_uuid >=", value, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidLessThan(String value) {
            addCriterion("post_uuid <", value, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidLessThanOrEqualTo(String value) {
            addCriterion("post_uuid <=", value, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidLike(String value) {
            addCriterion("post_uuid like", value, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidNotLike(String value) {
            addCriterion("post_uuid not like", value, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidIn(List<String> values) {
            addCriterion("post_uuid in", values, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidNotIn(List<String> values) {
            addCriterion("post_uuid not in", values, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidBetween(String value1, String value2) {
            addCriterion("post_uuid between", value1, value2, "postUuid");
            return (Criteria) this;
        }

        public Criteria andPostUuidNotBetween(String value1, String value2) {
            addCriterion("post_uuid not between", value1, value2, "postUuid");
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