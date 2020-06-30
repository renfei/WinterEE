package com.winteree.core.dao.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilesDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FilesDOExample() {
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

        public Criteria andOfficeUuidIsNull() {
            addCriterion("office_uuid is null");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidIsNotNull() {
            addCriterion("office_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidEqualTo(String value) {
            addCriterion("office_uuid =", value, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidNotEqualTo(String value) {
            addCriterion("office_uuid <>", value, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidGreaterThan(String value) {
            addCriterion("office_uuid >", value, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidGreaterThanOrEqualTo(String value) {
            addCriterion("office_uuid >=", value, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidLessThan(String value) {
            addCriterion("office_uuid <", value, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidLessThanOrEqualTo(String value) {
            addCriterion("office_uuid <=", value, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidLike(String value) {
            addCriterion("office_uuid like", value, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidNotLike(String value) {
            addCriterion("office_uuid not like", value, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidIn(List<String> values) {
            addCriterion("office_uuid in", values, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidNotIn(List<String> values) {
            addCriterion("office_uuid not in", values, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidBetween(String value1, String value2) {
            addCriterion("office_uuid between", value1, value2, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andOfficeUuidNotBetween(String value1, String value2) {
            addCriterion("office_uuid not between", value1, value2, "officeUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidIsNull() {
            addCriterion("department_uuid is null");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidIsNotNull() {
            addCriterion("department_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidEqualTo(String value) {
            addCriterion("department_uuid =", value, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidNotEqualTo(String value) {
            addCriterion("department_uuid <>", value, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidGreaterThan(String value) {
            addCriterion("department_uuid >", value, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidGreaterThanOrEqualTo(String value) {
            addCriterion("department_uuid >=", value, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidLessThan(String value) {
            addCriterion("department_uuid <", value, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidLessThanOrEqualTo(String value) {
            addCriterion("department_uuid <=", value, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidLike(String value) {
            addCriterion("department_uuid like", value, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidNotLike(String value) {
            addCriterion("department_uuid not like", value, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidIn(List<String> values) {
            addCriterion("department_uuid in", values, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidNotIn(List<String> values) {
            addCriterion("department_uuid not in", values, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidBetween(String value1, String value2) {
            addCriterion("department_uuid between", value1, value2, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andDepartmentUuidNotBetween(String value1, String value2) {
            addCriterion("department_uuid not between", value1, value2, "departmentUuid");
            return (Criteria) this;
        }

        public Criteria andFileNameIsNull() {
            addCriterion("file_name is null");
            return (Criteria) this;
        }

        public Criteria andFileNameIsNotNull() {
            addCriterion("file_name is not null");
            return (Criteria) this;
        }

        public Criteria andFileNameEqualTo(String value) {
            addCriterion("file_name =", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotEqualTo(String value) {
            addCriterion("file_name <>", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameGreaterThan(String value) {
            addCriterion("file_name >", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("file_name >=", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLessThan(String value) {
            addCriterion("file_name <", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLessThanOrEqualTo(String value) {
            addCriterion("file_name <=", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameLike(String value) {
            addCriterion("file_name like", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotLike(String value) {
            addCriterion("file_name not like", value, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameIn(List<String> values) {
            addCriterion("file_name in", values, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotIn(List<String> values) {
            addCriterion("file_name not in", values, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameBetween(String value1, String value2) {
            addCriterion("file_name between", value1, value2, "fileName");
            return (Criteria) this;
        }

        public Criteria andFileNameNotBetween(String value1, String value2) {
            addCriterion("file_name not between", value1, value2, "fileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameIsNull() {
            addCriterion("original_file_name is null");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameIsNotNull() {
            addCriterion("original_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameEqualTo(String value) {
            addCriterion("original_file_name =", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameNotEqualTo(String value) {
            addCriterion("original_file_name <>", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameGreaterThan(String value) {
            addCriterion("original_file_name >", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("original_file_name >=", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameLessThan(String value) {
            addCriterion("original_file_name <", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameLessThanOrEqualTo(String value) {
            addCriterion("original_file_name <=", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameLike(String value) {
            addCriterion("original_file_name like", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameNotLike(String value) {
            addCriterion("original_file_name not like", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameIn(List<String> values) {
            addCriterion("original_file_name in", values, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameNotIn(List<String> values) {
            addCriterion("original_file_name not in", values, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameBetween(String value1, String value2) {
            addCriterion("original_file_name between", value1, value2, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameNotBetween(String value1, String value2) {
            addCriterion("original_file_name not between", value1, value2, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andStorageTypeIsNull() {
            addCriterion("storage_type is null");
            return (Criteria) this;
        }

        public Criteria andStorageTypeIsNotNull() {
            addCriterion("storage_type is not null");
            return (Criteria) this;
        }

        public Criteria andStorageTypeEqualTo(String value) {
            addCriterion("storage_type =", value, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeNotEqualTo(String value) {
            addCriterion("storage_type <>", value, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeGreaterThan(String value) {
            addCriterion("storage_type >", value, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeGreaterThanOrEqualTo(String value) {
            addCriterion("storage_type >=", value, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeLessThan(String value) {
            addCriterion("storage_type <", value, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeLessThanOrEqualTo(String value) {
            addCriterion("storage_type <=", value, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeLike(String value) {
            addCriterion("storage_type like", value, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeNotLike(String value) {
            addCriterion("storage_type not like", value, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeIn(List<String> values) {
            addCriterion("storage_type in", values, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeNotIn(List<String> values) {
            addCriterion("storage_type not in", values, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeBetween(String value1, String value2) {
            addCriterion("storage_type between", value1, value2, "storageType");
            return (Criteria) this;
        }

        public Criteria andStorageTypeNotBetween(String value1, String value2) {
            addCriterion("storage_type not between", value1, value2, "storageType");
            return (Criteria) this;
        }

        public Criteria andStoragePathIsNull() {
            addCriterion("storage_path is null");
            return (Criteria) this;
        }

        public Criteria andStoragePathIsNotNull() {
            addCriterion("storage_path is not null");
            return (Criteria) this;
        }

        public Criteria andStoragePathEqualTo(String value) {
            addCriterion("storage_path =", value, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathNotEqualTo(String value) {
            addCriterion("storage_path <>", value, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathGreaterThan(String value) {
            addCriterion("storage_path >", value, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathGreaterThanOrEqualTo(String value) {
            addCriterion("storage_path >=", value, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathLessThan(String value) {
            addCriterion("storage_path <", value, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathLessThanOrEqualTo(String value) {
            addCriterion("storage_path <=", value, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathLike(String value) {
            addCriterion("storage_path like", value, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathNotLike(String value) {
            addCriterion("storage_path not like", value, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathIn(List<String> values) {
            addCriterion("storage_path in", values, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathNotIn(List<String> values) {
            addCriterion("storage_path not in", values, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathBetween(String value1, String value2) {
            addCriterion("storage_path between", value1, value2, "storagePath");
            return (Criteria) this;
        }

        public Criteria andStoragePathNotBetween(String value1, String value2) {
            addCriterion("storage_path not between", value1, value2, "storagePath");
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

        public Criteria andBuckeNameIsNull() {
            addCriterion("bucke_name is null");
            return (Criteria) this;
        }

        public Criteria andBuckeNameIsNotNull() {
            addCriterion("bucke_name is not null");
            return (Criteria) this;
        }

        public Criteria andBuckeNameEqualTo(String value) {
            addCriterion("bucke_name =", value, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameNotEqualTo(String value) {
            addCriterion("bucke_name <>", value, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameGreaterThan(String value) {
            addCriterion("bucke_name >", value, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameGreaterThanOrEqualTo(String value) {
            addCriterion("bucke_name >=", value, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameLessThan(String value) {
            addCriterion("bucke_name <", value, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameLessThanOrEqualTo(String value) {
            addCriterion("bucke_name <=", value, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameLike(String value) {
            addCriterion("bucke_name like", value, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameNotLike(String value) {
            addCriterion("bucke_name not like", value, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameIn(List<String> values) {
            addCriterion("bucke_name in", values, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameNotIn(List<String> values) {
            addCriterion("bucke_name not in", values, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameBetween(String value1, String value2) {
            addCriterion("bucke_name between", value1, value2, "buckeName");
            return (Criteria) this;
        }

        public Criteria andBuckeNameNotBetween(String value1, String value2) {
            addCriterion("bucke_name not between", value1, value2, "buckeName");
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