package com.uav.rof.vo;

import java.io.Serializable;

public class VendorBookingVO extends BaseVO implements Serializable{

    private String awbNo;
    private Integer pcs;
    private String pincode;
    private Double shipmentWeight;
    private String consigneeName;
    private CityVO city;
    private ServiceTypeVO servicetype;
    private Long bookingDate;

    private AttorneyVO attorney;
    private String forwarderAwbNo;
    private LastmileVO forwarder;


    public String getAwbNo() {
        return awbNo;
    }

    public void setAwbNo(String awbNo) {
        this.awbNo = awbNo;
    }

    public Integer getPcs() {
        return pcs;
    }

    public void setPcs(Integer pcs) {
        this.pcs = pcs;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Double getShipmentWeight() {
        return shipmentWeight;
    }

    public void setShipmentWeight(Double shipmentWeight) {
        this.shipmentWeight = shipmentWeight;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public CityVO getCity() {
        return city;
    }

    public void setCity(CityVO city) {
        this.city = city;
    }

    public ServiceTypeVO getServicetype() {
        return servicetype;
    }

    public void setServicetype(ServiceTypeVO servicetype) {
        this.servicetype = servicetype;
    }



    public Long getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Long bookingDate) {
        this.bookingDate = bookingDate;
    }


    public AttorneyVO getAttorney() {
        return attorney;
    }

    public void setAttorney(AttorneyVO attorney) {
        this.attorney = attorney;
    }


    public String getForwarderAwbNo() {
        return forwarderAwbNo;
    }

    public void setForwarderAwbNo(String forwarderAwbNo) {
        this.forwarderAwbNo = forwarderAwbNo;
    }

    public LastmileVO getForwarder() {
        return forwarder;
    }

    public void setForwarder(LastmileVO forwarder) {
        this.forwarder = forwarder;
    }
}

