
package com.orderonline.api.fpx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>modifyOrderRequest complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="modifyOrderRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="buyerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cargoCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeCompanyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneePostCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeTelephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="declareInvoice" type="{entity.orderonline.api.fpx}declareInvoice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="destinationCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="initialCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insurType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insurValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pieces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperCompanyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperPostCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperTelephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stateOrProvince" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="street" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackingNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modifyOrderRequest", propOrder = {
    "buyerId",
    "cargoCode",
    "city",
    "consigneeCompanyName",
    "consigneeEmail",
    "consigneeFax",
    "consigneeName",
    "consigneePostCode",
    "consigneeTelephone",
    "customerWeight",
    "declareInvoice",
    "destinationCountryCode",
    "initialCountryCode",
    "insurType",
    "insurValue",
    "orderNo",
    "orderNote",
    "paymentCode",
    "pieces",
    "productCode",
    "returnSign",
    "shipperAddress",
    "shipperCompanyName",
    "shipperFax",
    "shipperName",
    "shipperPostCode",
    "shipperTelephone",
    "stateOrProvince",
    "street",
    "trackingNumber",
    "transactionId"
})
public class ModifyOrderRequest {

    protected String buyerId;
    protected String cargoCode;
    protected String city;
    protected String consigneeCompanyName;
    protected String consigneeEmail;
    protected String consigneeFax;
    protected String consigneeName;
    protected String consigneePostCode;
    protected String consigneeTelephone;
    protected String customerWeight;
    @XmlElement(nillable = true)
    protected List<DeclareInvoice> declareInvoice;
    protected String destinationCountryCode;
    protected String initialCountryCode;
    protected String insurType;
    protected String insurValue;
    protected String orderNo;
    protected String orderNote;
    protected String paymentCode;
    protected String pieces;
    protected String productCode;
    protected String returnSign;
    protected String shipperAddress;
    protected String shipperCompanyName;
    protected String shipperFax;
    protected String shipperName;
    protected String shipperPostCode;
    protected String shipperTelephone;
    protected String stateOrProvince;
    protected String street;
    protected String trackingNumber;
    protected String transactionId;

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
     * 获取city属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置city属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * 获取consigneeCompanyName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeCompanyName() {
        return consigneeCompanyName;
    }

    /**
     * 设置consigneeCompanyName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeCompanyName(String value) {
        this.consigneeCompanyName = value;
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
     * Gets the value of the declareInvoice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the declareInvoice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeclareInvoice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeclareInvoice }
     * 
     * 
     */
    public List<DeclareInvoice> getDeclareInvoice() {
        if (declareInvoice == null) {
            declareInvoice = new ArrayList<DeclareInvoice>();
        }
        return this.declareInvoice;
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
     * 获取shipperCompanyName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperCompanyName() {
        return shipperCompanyName;
    }

    /**
     * 设置shipperCompanyName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperCompanyName(String value) {
        this.shipperCompanyName = value;
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
     * 获取stateOrProvince属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     * 设置stateOrProvince属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateOrProvince(String value) {
        this.stateOrProvince = value;
    }

    /**
     * 获取street属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreet() {
        return street;
    }

    /**
     * 设置street属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreet(String value) {
        this.street = value;
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

}
