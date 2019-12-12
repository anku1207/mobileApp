package com.uav.rof.vo;

import java.io.Serializable;
import java.util.Date;

public class ShipmentUnitVO  implements Serializable{

     private String awbNo;
     private Integer pcs;
     private String pincode;
     private Double shipmentWeight;
     private String docInvoiceNumber;
     private Integer shipmentValue;
     private String consigneeName;
     private CityVO city;
     private CustomerVO customer;
     private LastmileVO lastMile;
     private ServiceTypeVO servicetype;
     private ShipmentModeVO shipmentMode;
     private ShipmentTypeVO shipmentType;
     private PaymentTypeVO paymentType;
     private Integer fileId;
     private Long bookingDate;

     private String fwdNumber;
     private LastmileVO forwarder;




     private  Integer anonymousInteger;


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

    public String getDocInvoiceNumber() {
        return docInvoiceNumber;
    }

    public void setDocInvoiceNumber(String docInvoiceNumber) {
        this.docInvoiceNumber = docInvoiceNumber;
    }

    public Integer getShipmentValue() {
        return shipmentValue;
    }

    public void setShipmentValue(Integer shipmentValue) {
        this.shipmentValue = shipmentValue;
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

    public CustomerVO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerVO customer) {
        this.customer = customer;
    }

    public LastmileVO getLastMile() {
        return lastMile;
    }

    public void setLastMile(LastmileVO lastMile) {
        this.lastMile = lastMile;
    }

    public ServiceTypeVO getServicetype() {
        return servicetype;
    }

    public void setServicetype(ServiceTypeVO servicetype) {
        this.servicetype = servicetype;
    }

    public ShipmentModeVO getShipmentMode() {
        return shipmentMode;
    }

    public void setShipmentMode(ShipmentModeVO shipmentMode) {
        this.shipmentMode = shipmentMode;
    }



    public PaymentTypeVO getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeVO paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }


    public Integer getAnonymousInteger(int id) {
        return anonymousInteger;
    }

    public void setAnonymousInteger(Integer anonymousInteger) {
        this.anonymousInteger = anonymousInteger;
    }


    public ShipmentTypeVO getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(ShipmentTypeVO shipmentType) {
        this.shipmentType = shipmentType;
    }


    public Long getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Long bookingDate) {
        this.bookingDate = bookingDate;
    }


    public String getFwdNumber() {
        return fwdNumber;
    }

    public void setFwdNumber(String fwdNumber) {
        this.fwdNumber = fwdNumber;
    }

    public LastmileVO getForwarder() {
        return forwarder;
    }

    public void setForwarder(LastmileVO forwarder) {
        this.forwarder = forwarder;
    }
}
