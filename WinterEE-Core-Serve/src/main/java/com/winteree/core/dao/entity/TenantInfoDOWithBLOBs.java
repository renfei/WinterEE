package com.winteree.core.dao.entity;

public class TenantInfoDOWithBLOBs extends TenantInfoDO {
    private String administrators;

    private String contact;

    private String address;

    public String getAdministrators() {
        return administrators;
    }

    public void setAdministrators(String administrators) {
        this.administrators = administrators == null ? null : administrators.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}