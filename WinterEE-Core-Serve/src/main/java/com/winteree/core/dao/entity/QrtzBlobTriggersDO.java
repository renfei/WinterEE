package com.winteree.core.dao.entity;

public class QrtzBlobTriggersDO extends QrtzBlobTriggersDOKey {
    private byte[] blobData;

    public byte[] getBlobData() {
        return blobData;
    }

    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
}