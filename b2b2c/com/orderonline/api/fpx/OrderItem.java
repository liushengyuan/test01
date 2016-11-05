
package com.orderonline.api.fpx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>orderItem complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="orderItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="buyerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cargoCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="channelHawbCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chargeWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="checkinDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="checkoutDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="confirmDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeAddress3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeCompany" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneePostCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeTelephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="createDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="declareInvoiceItem" type="{entity.orderonline.api.fpx}declareInvoiceItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="destinationCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="grossWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="holdSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="initialCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insurStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insurType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insurValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modifyDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="odaCheckSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="odaSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pieces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="printDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperCompany" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperPostCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperTelephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackingNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="volumeWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderItem", propOrder = {
    "buyerId",
    "cargoCode",
    "channelHawbCode",
    "chargeWeight",
    "checkinDate",
    "checkoutDate",
    "confirmDate",
    "consigneeAddress1",
    "consigneeAddress2",
    "consigneeAddress3",
    "consigneeCompany",
    "consigneeEmail",
    "consigneeFax",
    "consigneeName",
    "consigneePostCode",
    "consigneeTelephone",
    "createDate",
    "customerWeight",
    "declareInvoiceItem",
    "destinationCountryCode",
    "grossWeight",
    "holdSign",
    "initialCountryCode",
    "insurStatus",
    "insurType",
    "insurValue",
    "modifyDate",
    "odaCheckSign",
    "odaSign",
    "orderNo",
    "orderNote",
    "paymentCode",
    "pieces",
    "postDate",
    "printDate",
    "productCode",
    "returnSign",
    "shipperAddress",
    "shipperCompany",
    "shipperFax",
    "shipperName",
    "shipperPostCode",
    "shipperTelephone",
    "status",
    "trackingNumber",
    "transactionId",
    "volumeWeight"
})
public class OrderItem {

    protected String buyerId;
    protected String cargoCode;
    protected String channelHawbCode;
    protected String chargeWeight;
    protected String checkinDate;
    protected String checkoutDate;
    protected String confirmDate;
    protected String consigneeAddress1;
    protected String consigneeAddress2;
    protected String consigneeAddress3;
    protected String consigneeCompany;
    protected String consigneeEmail;
    protected String consigneeFax;
    protected String consigneeName;
    protected String consigneePostCode;
    protected String consigneeTelephone;
    protected String createDate;
    protected String customerWeight;
    @XmlElement(nillable = true)
    protected List<DeclareInvoiceItem> declareInvoiceItem;
    protected String destinationCountryCode;
    protected String grossWeight;
    protected String holdSign;
    protected String initialCountryCode;
    protected String insurStatus;
    protected String insurType;
    protected String insurValue;
    protected String modifyDate;
    protected String odaCheckSign;
    protected String odaSign;
    protected String orderNo;
    protected String orderNote;
    protected String paymentCode;
    protected String pieces;
    protected String postDate;
    protected String printDate;
    protected String productCode;
    protected String returnSign;
    protected String shipperAddress;
    protected String shipperCompany;
    protected String shipperFax;
    protected String shipperName;
    protected String shipperPostCode;
    protected String shipperTelephone;
    protected String status;
    protected String trackingNumber;
    protected String transactionId;
    protected String volumeWeight;

    /**
     * 获取buyerId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyerId() {
        return buyerId;
    }

    /**
     * 设置buyerId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyerId(String value) {
        this.buyerId = value;
    }

    /**
     * 获取cargoCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCargoCode() {
        return cargoCode;
    }

    /**
     * 设置cargoCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCargoCode(String value) {
        this.cargoCode = value;
    }

    /**
     * 获取channelHawbCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelHawbCode() {
        return channelHawbCode;
    }

    /**
     * 设置channelHawbCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelHawbCode(String value) {
        this.channelHawbCode = value;
    }

    /**
     * 获取chargeWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeWeight() {
        return chargeWeight;
    }

    /**
     * 设置chargeWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeWeight(String value) {
        this.chargeWeight = value;
    }

    /**
     * 获取checkinDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckinDate() {
        return checkinDate;
    }

    /**
     * 设置checkinDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckinDate(String value) {
        this.checkinDate = value;
    }

    /**
     * 获取checkoutDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckoutDate() {
        return checkoutDate;
    }

    /**
     * 设置checkoutDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckoutDate(String value) {
        this.checkoutDate = value;
    }

    /**
     * 获取confirmDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfirmDate() {
        return confirmDate;
    }

    /**
     * 设置confirmDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfirmDate(String value) {
        this.confirmDate = value;
    }

    /**
     * 获取consigneeAddress1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeAddress1() {
        return consigneeAddress1;
    }

    /**
     * 设置consigneeAddress1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeAddress1(String value) {
        this.consigneeAddress1 = value;
    }

    /**
     * 获取consigneeAddress2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeAddress2() {
        return consigneeAddress2;
    }

    /**
     * 设置consigneeAddress2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeAddress2(String value) {
        this.consigneeAddress2 = value;
    }

    /**
     * 获取consigneeAddress3属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeAddress3() {
        return consigneeAddress3;
    }

    /**
     * 设置consigneeAddress3属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeAddress3(String value) {
        this.consigneeAddress3 = value;
    }

    /**
     * 获取consigneeCompany属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeCompany() {
        return consigneeCompany;
    }

    /**
     * 设置consigneeCompany属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeCompany(String value) {
        this.consigneeCompany = value;
    }

    /**
     * 获取consigneeEmail属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeEmail() {
        return consigneeEmail;
    }

    /**
     * 设置consigneeEmail属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeEmail(String value) {
        this.consigneeEmail = value;
    }

    /**
     * 获取consigneeFax属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeFax() {
        return consigneeFax;
    }

    /**
     * 设置consigneeFax属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeFax(String value) {
        this.consigneeFax = value;
    }

    /**
     * 获取consigneeName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeName() {
        return consigneeName;
    }

    /**
     * 设置consigneeName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeName(String value) {
        this.consigneeName = value;
    }

    /**
     * 获取consigneePostCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneePostCode() {
        return consigneePostCode;
    }

    /**
     * 设置consigneePostCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneePostCode(String value) {
        this.consigneePostCode = value;
    }

    /**
     * 获取consigneeTelephone属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeTelephone() {
        return consigneeTelephone;
    }

    /**
     * 设置consigneeTelephone属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeTelephone(String value) {
        this.consigneeTelephone = value;
    }

    /**
     * 获取createDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * 设置createDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateDate(String value) {
        this.createDate = value;
    }

    /**
     * 获取customerWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerWeight() {
        return customerWeight;
    }

    /**
     * 设置customerWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerWeight(String value) {
        this.customerWeight = value;
    }

    /**
     * Gets the value of the declareInvoiceItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the declareInvoiceItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeclareInvoiceItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeclareInvoiceItem }
     * 
     * 
     */
    public List<DeclareInvoiceItem> getDeclareInvoiceItem() {
        if (declareInvoiceItem == null) {
            declareInvoiceItem = new ArrayList<DeclareInvoiceItem>();
        }
        return this.declareInvoiceItem;
    }

    /**
     * 获取destinationCountryCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationCountryCode() {
        return destinationCountryCode;
    }

    /**
     * 设置destinationCountryCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationCountryCode(String value) {
        this.destinationCountryCode = value;
    }

    /**
     * 获取grossWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrossWeight() {
        return grossWeight;
    }

    /**
     * 设置grossWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrossWeight(String value) {
        this.grossWeight = value;
    }

    /**
     * 获取holdSign属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoldSign() {
        return holdSign;
    }

    /**
     * 设置holdSign属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoldSign(String value) {
        this.holdSign = value;
    }

    /**
     * 获取initialCountryCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialCountryCode() {
        return initialCountryCode;
    }

    /**
     * 设置initialCountryCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialCountryCode(String value) {
        this.initialCountryCode = value;
    }

    /**
     * 获取insurStatus属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsurStatus() {
        return insurStatus;
    }

    /**
     * 设置insurStatus属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsurStatus(String value) {
        this.insurStatus = value;
    }

    /**
     * 获取insurType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsurType() {
        return insurType;
    }

    /**
     * 设置insurType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsurType(String value) {
        this.insurType = value;
    }

    /**
     * 获取insurValue属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsurValue() {
        return insurValue;
    }

    /**
     * 设置insurValue属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsurValue(String value) {
        this.insurValue = value;
    }

    /**
     * 获取modifyDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModifyDate() {
        return modifyDate;
    }

    /**
     * 设置modifyDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifyDate(String value) {
        this.modifyDate = value;
    }

    /**
     * 获取odaCheckSign属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdaCheckSign() {
        return odaCheckSign;
    }

    /**
     * 设置odaCheckSign属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdaCheckSign(String value) {
        this.odaCheckSign = value;
    }

    /**
     * 获取odaSign属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdaSign() {
        return odaSign;
    }

    /**
     * 设置odaSign属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdaSign(String value) {
        this.odaSign = value;
    }

    /**
     * 获取orderNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置orderNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderNo(String value) {
        this.orderNo = value;
    }

    /**
     * 获取orderNote属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderNote() {
        return orderNote;
    }

    /**
     * 设置orderNote属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderNote(String value) {
        this.orderNote = value;
    }

    /**
     * 获取paymentCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentCode() {
        return paymentCode;
    }

    /**
     * 设置paymentCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentCode(String value) {
        this.paymentCode = value;
    }

    /**
     * 获取pieces属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPieces() {
        return pieces;
    }

    /**
     * 设置pieces属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPieces(String value) {
        this.pieces = value;
    }

    /**
     * 获取postDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostDate() {
        return postDate;
    }

    /**
     * 设置postDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostDate(String value) {
        this.postDate = value;
    }

    /**
     * 获取printDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrintDate() {
        return printDate;
    }

    /**
     * 设置printDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrintDate(String value) {
        this.printDate = value;
    }

    /**
     * 获取productCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * 设置productCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCode(String value) {
        this.productCode = value;
    }

    /**
     * 获取returnSign属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnSign() {
        return returnSign;
    }

    /**
     * 设置returnSign属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnSign(String value) {
        this.returnSign = value;
    }

    /**
     * 获取shipperAddress属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperAddress() {
        return shipperAddress;
    }

    /**
     * 设置shipperAddress属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperAddress(String value) {
        this.shipperAddress = value;
    }

    /**
     * 获取shipperCompany属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperCompany() {
        return shipperCompany;
    }

    /**
     * 设置shipperCompany属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperCompany(String value) {
        this.shipperCompany = value;
    }

    /**
     * 获取shipperFax属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperFax() {
        return shipperFax;
    }

    /**
     * 设置shipperFax属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperFax(String value) {
        this.shipperFax = value;
    }

    /**
     * 获取shipperName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperName() {
        return shipperName;
    }

    /**
     * 设置shipperName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperName(String value) {
        this.shipperName = value;
    }

    /**
     * 获取shipperPostCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperPostCode() {
        return shipperPostCode;
    }

    /**
     * 设置shipperPostCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperPostCode(String value) {
        this.shipperPostCode = value;
    }

    /**
     * 获取shipperTelephone属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperTelephone() {
        return shipperTelephone;
    }

    /**
     * 设置shipperTelephone属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperTelephone(String value) {
        this.shipperTelephone = value;
    }

    /**
     * 获取status属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置status属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * 获取trackingNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackingNumber() {
        return trackingNumber;
    }

    /**
     * 设置trackingNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackingNumber(String value) {
        this.trackingNumber = value;
    }

    /**
     * 获取transactionId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * 设置transactionId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * 获取volumeWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolumeWeight() {
        return volumeWeight;
    }

    /**
     * 设置volumeWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolumeWeight(String value) {
        this.volumeWeight = value;
    }

}
