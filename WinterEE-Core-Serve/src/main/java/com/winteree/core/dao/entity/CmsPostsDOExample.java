package com.winteree.core.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CmsPostsDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CmsPostsDOExample() {
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

        public Criteria andSiteUuidIsNull() {
            addCriterion("site_uuid is null");
            return (Criteria) this;
        }

        public Criteria andSiteUuidIsNotNull() {
            addCriterion("site_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andSiteUuidEqualTo(String value) {
            addCriterion("site_uuid =", value, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidNotEqualTo(String value) {
            addCriterion("site_uuid <>", value, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidGreaterThan(String value) {
            addCriterion("site_uuid >", value, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidGreaterThanOrEqualTo(String value) {
            addCriterion("site_uuid >=", value, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidLessThan(String value) {
            addCriterion("site_uuid <", value, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidLessThanOrEqualTo(String value) {
            addCriterion("site_uuid <=", value, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidLike(String value) {
            addCriterion("site_uuid like", value, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidNotLike(String value) {
            addCriterion("site_uuid not like", value, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidIn(List<String> values) {
            addCriterion("site_uuid in", values, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidNotIn(List<String> values) {
            addCriterion("site_uuid not in", values, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidBetween(String value1, String value2) {
            addCriterion("site_uuid between", value1, value2, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andSiteUuidNotBetween(String value1, String value2) {
            addCriterion("site_uuid not between", value1, value2, "siteUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidIsNull() {
            addCriterion("category_uuid is null");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidIsNotNull() {
            addCriterion("category_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidEqualTo(String value) {
            addCriterion("category_uuid =", value, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidNotEqualTo(String value) {
            addCriterion("category_uuid <>", value, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidGreaterThan(String value) {
            addCriterion("category_uuid >", value, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidGreaterThanOrEqualTo(String value) {
            addCriterion("category_uuid >=", value, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidLessThan(String value) {
            addCriterion("category_uuid <", value, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidLessThanOrEqualTo(String value) {
            addCriterion("category_uuid <=", value, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidLike(String value) {
            addCriterion("category_uuid like", value, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidNotLike(String value) {
            addCriterion("category_uuid not like", value, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidIn(List<String> values) {
            addCriterion("category_uuid in", values, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidNotIn(List<String> values) {
            addCriterion("category_uuid not in", values, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidBetween(String value1, String value2) {
            addCriterion("category_uuid between", value1, value2, "categoryUuid");
            return (Criteria) this;
        }

        public Criteria andCategoryUuidNotBetween(String value1, String value2) {
            addCriterion("category_uuid not between", value1, value2, "categoryUuid");
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

        public Criteria andIsOriginalIsNull() {
            addCriterion("is_original is null");
            return (Criteria) this;
        }

        public Criteria andIsOriginalIsNotNull() {
            addCriterion("is_original is not null");
            return (Criteria) this;
        }

        public Criteria andIsOriginalEqualTo(Boolean value) {
            addCriterion("is_original =", value, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalNotEqualTo(Boolean value) {
            addCriterion("is_original <>", value, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalGreaterThan(Boolean value) {
            addCriterion("is_original >", value, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_original >=", value, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalLessThan(Boolean value) {
            addCriterion("is_original <", value, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalLessThanOrEqualTo(Boolean value) {
            addCriterion("is_original <=", value, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalIn(List<Boolean> values) {
            addCriterion("is_original in", values, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalNotIn(List<Boolean> values) {
            addCriterion("is_original not in", values, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalBetween(Boolean value1, Boolean value2) {
            addCriterion("is_original between", value1, value2, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andIsOriginalNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_original not between", value1, value2, "isOriginal");
            return (Criteria) this;
        }

        public Criteria andViewsIsNull() {
            addCriterion("views is null");
            return (Criteria) this;
        }

        public Criteria andViewsIsNotNull() {
            addCriterion("views is not null");
            return (Criteria) this;
        }

        public Criteria andViewsEqualTo(Long value) {
            addCriterion("views =", value, "views");
            return (Criteria) this;
        }

        public Criteria andViewsNotEqualTo(Long value) {
            addCriterion("views <>", value, "views");
            return (Criteria) this;
        }

        public Criteria andViewsGreaterThan(Long value) {
            addCriterion("views >", value, "views");
            return (Criteria) this;
        }

        public Criteria andViewsGreaterThanOrEqualTo(Long value) {
            addCriterion("views >=", value, "views");
            return (Criteria) this;
        }

        public Criteria andViewsLessThan(Long value) {
            addCriterion("views <", value, "views");
            return (Criteria) this;
        }

        public Criteria andViewsLessThanOrEqualTo(Long value) {
            addCriterion("views <=", value, "views");
            return (Criteria) this;
        }

        public Criteria andViewsIn(List<Long> values) {
            addCriterion("views in", values, "views");
            return (Criteria) this;
        }

        public Criteria andViewsNotIn(List<Long> values) {
            addCriterion("views not in", values, "views");
            return (Criteria) this;
        }

        public Criteria andViewsBetween(Long value1, Long value2) {
            addCriterion("views between", value1, value2, "views");
            return (Criteria) this;
        }

        public Criteria andViewsNotBetween(Long value1, Long value2) {
            addCriterion("views not between", value1, value2, "views");
            return (Criteria) this;
        }

        public Criteria andThumbsUpIsNull() {
            addCriterion("thumbs_up is null");
            return (Criteria) this;
        }

        public Criteria andThumbsUpIsNotNull() {
            addCriterion("thumbs_up is not null");
            return (Criteria) this;
        }

        public Criteria andThumbsUpEqualTo(Long value) {
            addCriterion("thumbs_up =", value, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpNotEqualTo(Long value) {
            addCriterion("thumbs_up <>", value, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpGreaterThan(Long value) {
            addCriterion("thumbs_up >", value, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpGreaterThanOrEqualTo(Long value) {
            addCriterion("thumbs_up >=", value, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpLessThan(Long value) {
            addCriterion("thumbs_up <", value, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpLessThanOrEqualTo(Long value) {
            addCriterion("thumbs_up <=", value, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpIn(List<Long> values) {
            addCriterion("thumbs_up in", values, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpNotIn(List<Long> values) {
            addCriterion("thumbs_up not in", values, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpBetween(Long value1, Long value2) {
            addCriterion("thumbs_up between", value1, value2, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsUpNotBetween(Long value1, Long value2) {
            addCriterion("thumbs_up not between", value1, value2, "thumbsUp");
            return (Criteria) this;
        }

        public Criteria andThumbsDownIsNull() {
            addCriterion("thumbs_down is null");
            return (Criteria) this;
        }

        public Criteria andThumbsDownIsNotNull() {
            addCriterion("thumbs_down is not null");
            return (Criteria) this;
        }

        public Criteria andThumbsDownEqualTo(Long value) {
            addCriterion("thumbs_down =", value, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownNotEqualTo(Long value) {
            addCriterion("thumbs_down <>", value, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownGreaterThan(Long value) {
            addCriterion("thumbs_down >", value, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownGreaterThanOrEqualTo(Long value) {
            addCriterion("thumbs_down >=", value, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownLessThan(Long value) {
            addCriterion("thumbs_down <", value, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownLessThanOrEqualTo(Long value) {
            addCriterion("thumbs_down <=", value, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownIn(List<Long> values) {
            addCriterion("thumbs_down in", values, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownNotIn(List<Long> values) {
            addCriterion("thumbs_down not in", values, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownBetween(Long value1, Long value2) {
            addCriterion("thumbs_down between", value1, value2, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andThumbsDownNotBetween(Long value1, Long value2) {
            addCriterion("thumbs_down not between", value1, value2, "thumbsDown");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIsNull() {
            addCriterion("release_time is null");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIsNotNull() {
            addCriterion("release_time is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeEqualTo(Date value) {
            addCriterion("release_time =", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotEqualTo(Date value) {
            addCriterion("release_time <>", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeGreaterThan(Date value) {
            addCriterion("release_time >", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("release_time >=", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeLessThan(Date value) {
            addCriterion("release_time <", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeLessThanOrEqualTo(Date value) {
            addCriterion("release_time <=", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIn(List<Date> values) {
            addCriterion("release_time in", values, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotIn(List<Date> values) {
            addCriterion("release_time not in", values, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeBetween(Date value1, Date value2) {
            addCriterion("release_time between", value1, value2, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotBetween(Date value1, Date value2) {
            addCriterion("release_time not between", value1, value2, "releaseTime");
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

        public Criteria andCreateByIsNull() {
            addCriterion("create_by is null");
            return (Criteria) this;
        }

        public Criteria andCreateByIsNotNull() {
            addCriterion("create_by is not null");
            return (Criteria) this;
        }

        public Criteria andCreateByEqualTo(String value) {
            addCriterion("create_by =", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotEqualTo(String value) {
            addCriterion("create_by <>", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThan(String value) {
            addCriterion("create_by >", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByGreaterThanOrEqualTo(String value) {
            addCriterion("create_by >=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThan(String value) {
            addCriterion("create_by <", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLessThanOrEqualTo(String value) {
            addCriterion("create_by <=", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByLike(String value) {
            addCriterion("create_by like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotLike(String value) {
            addCriterion("create_by not like", value, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByIn(List<String> values) {
            addCriterion("create_by in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotIn(List<String> values) {
            addCriterion("create_by not in", values, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByBetween(String value1, String value2) {
            addCriterion("create_by between", value1, value2, "createBy");
            return (Criteria) this;
        }

        public Criteria andCreateByNotBetween(String value1, String value2) {
            addCriterion("create_by not between", value1, value2, "createBy");
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

        public Criteria andUpdateByIsNull() {
            addCriterion("update_by is null");
            return (Criteria) this;
        }

        public Criteria andUpdateByIsNotNull() {
            addCriterion("update_by is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateByEqualTo(String value) {
            addCriterion("update_by =", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotEqualTo(String value) {
            addCriterion("update_by <>", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThan(String value) {
            addCriterion("update_by >", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByGreaterThanOrEqualTo(String value) {
            addCriterion("update_by >=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThan(String value) {
            addCriterion("update_by <", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLessThanOrEqualTo(String value) {
            addCriterion("update_by <=", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByLike(String value) {
            addCriterion("update_by like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotLike(String value) {
            addCriterion("update_by not like", value, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByIn(List<String> values) {
            addCriterion("update_by in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotIn(List<String> values) {
            addCriterion("update_by not in", values, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByBetween(String value1, String value2) {
            addCriterion("update_by between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andUpdateByNotBetween(String value1, String value2) {
            addCriterion("update_by not between", value1, value2, "updateBy");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeleteEqualTo(Boolean value) {
            addCriterion("is_delete =", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotEqualTo(Boolean value) {
            addCriterion("is_delete <>", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThan(Boolean value) {
            addCriterion("is_delete >", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_delete >=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThan(Boolean value) {
            addCriterion("is_delete <", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteLessThanOrEqualTo(Boolean value) {
            addCriterion("is_delete <=", value, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteIn(List<Boolean> values) {
            addCriterion("is_delete in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotIn(List<Boolean> values) {
            addCriterion("is_delete not in", values, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteBetween(Boolean value1, Boolean value2) {
            addCriterion("is_delete between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsDeleteNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_delete not between", value1, value2, "isDelete");
            return (Criteria) this;
        }

        public Criteria andIsCommentIsNull() {
            addCriterion("is_comment is null");
            return (Criteria) this;
        }

        public Criteria andIsCommentIsNotNull() {
            addCriterion("is_comment is not null");
            return (Criteria) this;
        }

        public Criteria andIsCommentEqualTo(Boolean value) {
            addCriterion("is_comment =", value, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentNotEqualTo(Boolean value) {
            addCriterion("is_comment <>", value, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentGreaterThan(Boolean value) {
            addCriterion("is_comment >", value, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_comment >=", value, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentLessThan(Boolean value) {
            addCriterion("is_comment <", value, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentLessThanOrEqualTo(Boolean value) {
            addCriterion("is_comment <=", value, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentIn(List<Boolean> values) {
            addCriterion("is_comment in", values, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentNotIn(List<Boolean> values) {
            addCriterion("is_comment not in", values, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentBetween(Boolean value1, Boolean value2) {
            addCriterion("is_comment between", value1, value2, "isComment");
            return (Criteria) this;
        }

        public Criteria andIsCommentNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_comment not between", value1, value2, "isComment");
            return (Criteria) this;
        }

        public Criteria andAvgViewsIsNull() {
            addCriterion("avg_views is null");
            return (Criteria) this;
        }

        public Criteria andAvgViewsIsNotNull() {
            addCriterion("avg_views is not null");
            return (Criteria) this;
        }

        public Criteria andAvgViewsEqualTo(Double value) {
            addCriterion("avg_views =", value, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsNotEqualTo(Double value) {
            addCriterion("avg_views <>", value, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsGreaterThan(Double value) {
            addCriterion("avg_views >", value, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsGreaterThanOrEqualTo(Double value) {
            addCriterion("avg_views >=", value, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsLessThan(Double value) {
            addCriterion("avg_views <", value, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsLessThanOrEqualTo(Double value) {
            addCriterion("avg_views <=", value, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsIn(List<Double> values) {
            addCriterion("avg_views in", values, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsNotIn(List<Double> values) {
            addCriterion("avg_views not in", values, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsBetween(Double value1, Double value2) {
            addCriterion("avg_views between", value1, value2, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgViewsNotBetween(Double value1, Double value2) {
            addCriterion("avg_views not between", value1, value2, "avgViews");
            return (Criteria) this;
        }

        public Criteria andAvgCommentIsNull() {
            addCriterion("avg_comment is null");
            return (Criteria) this;
        }

        public Criteria andAvgCommentIsNotNull() {
            addCriterion("avg_comment is not null");
            return (Criteria) this;
        }

        public Criteria andAvgCommentEqualTo(Double value) {
            addCriterion("avg_comment =", value, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentNotEqualTo(Double value) {
            addCriterion("avg_comment <>", value, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentGreaterThan(Double value) {
            addCriterion("avg_comment >", value, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentGreaterThanOrEqualTo(Double value) {
            addCriterion("avg_comment >=", value, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentLessThan(Double value) {
            addCriterion("avg_comment <", value, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentLessThanOrEqualTo(Double value) {
            addCriterion("avg_comment <=", value, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentIn(List<Double> values) {
            addCriterion("avg_comment in", values, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentNotIn(List<Double> values) {
            addCriterion("avg_comment not in", values, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentBetween(Double value1, Double value2) {
            addCriterion("avg_comment between", value1, value2, "avgComment");
            return (Criteria) this;
        }

        public Criteria andAvgCommentNotBetween(Double value1, Double value2) {
            addCriterion("avg_comment not between", value1, value2, "avgComment");
            return (Criteria) this;
        }

        public Criteria andPageRankIsNull() {
            addCriterion("page_rank is null");
            return (Criteria) this;
        }

        public Criteria andPageRankIsNotNull() {
            addCriterion("page_rank is not null");
            return (Criteria) this;
        }

        public Criteria andPageRankEqualTo(Double value) {
            addCriterion("page_rank =", value, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankNotEqualTo(Double value) {
            addCriterion("page_rank <>", value, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankGreaterThan(Double value) {
            addCriterion("page_rank >", value, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankGreaterThanOrEqualTo(Double value) {
            addCriterion("page_rank >=", value, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankLessThan(Double value) {
            addCriterion("page_rank <", value, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankLessThanOrEqualTo(Double value) {
            addCriterion("page_rank <=", value, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankIn(List<Double> values) {
            addCriterion("page_rank in", values, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankNotIn(List<Double> values) {
            addCriterion("page_rank not in", values, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankBetween(Double value1, Double value2) {
            addCriterion("page_rank between", value1, value2, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankNotBetween(Double value1, Double value2) {
            addCriterion("page_rank not between", value1, value2, "pageRank");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeIsNull() {
            addCriterion("page_rank_update_time is null");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeIsNotNull() {
            addCriterion("page_rank_update_time is not null");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeEqualTo(Date value) {
            addCriterion("page_rank_update_time =", value, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeNotEqualTo(Date value) {
            addCriterion("page_rank_update_time <>", value, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeGreaterThan(Date value) {
            addCriterion("page_rank_update_time >", value, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("page_rank_update_time >=", value, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeLessThan(Date value) {
            addCriterion("page_rank_update_time <", value, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("page_rank_update_time <=", value, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeIn(List<Date> values) {
            addCriterion("page_rank_update_time in", values, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeNotIn(List<Date> values) {
            addCriterion("page_rank_update_time not in", values, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("page_rank_update_time between", value1, value2, "pageRankUpdateTime");
            return (Criteria) this;
        }

        public Criteria andPageRankUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("page_rank_update_time not between", value1, value2, "pageRankUpdateTime");
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