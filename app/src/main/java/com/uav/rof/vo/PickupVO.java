package com.uav.rof.vo;

public class PickupVO {



    private String awbNo;
    private CustomerVO customer;
    private FlieVO fileId;
    private String pincode;

    public String getAwbNo() {
        return awbNo;
    }

    public void setAwbNo(String awbNo) {
        this.awbNo = awbNo;
    }

    public CustomerVO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerVO customer) {
        this.customer = customer;
    }

    public FlieVO getFileId() {
        return fileId;
    }

    public void setFileId(FlieVO fileId) {
        this.fileId = fileId;
    }


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
