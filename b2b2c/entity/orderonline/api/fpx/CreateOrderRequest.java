
package entity.orderonline.api.fpx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>createOrderRequest complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="createOrderRequest">
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
 *         &lt;element name="mctCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paymentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pieces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prealertintegrationtype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperCompanyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperPostCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shipperStateOrProvince" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "createOrderRequest", propOrder = {
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
    "mctCode",
    "orderNo",
    "orderNote",
    "paymentCode",
    "pieces",
    "prealertintegrationtype",
    "productCode",
    "returnSign",
    "shipperAddress",
    "shipperCity",
    "shipperCompanyName",
    "shipperFax",
    "shipperName",
    "shipperPostCode",
    "shipperStateOrProvince",
    "shipperTelephone",
    "stateOrProvince",
    "street",
    "trackingNumber",
    "transactionId"
})
public class CreateOrderRequest {

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
    protected String mctCode;
    protected String orderNo;
    protected String orderNote;
    protected String paymentCode;
    protected String pieces;
    protected String prealertintegrationtype;
    protected String productCode;
    protected String returnSign;
    protected String shipperAddress;
    protected String shipperCity;
    protected String shipperCompanyName;
    protected String shipperFax;
    protected String shipperName;
    protected String shipperPostCode;
    protected String shipperStateOrProvince;
    protected String shipperTelephone;
    protected String stateOrProvince;
    protected String street;
    protected String trackingNumber;
    protected String transactionId;

    /**
     * ��ȡbuyerId���Ե�ֵ��
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
     * ����buyerId���Ե�ֵ��
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
     * ��ȡcargoCode���Ե�ֵ��
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
     * ����cargoCode���Ե�ֵ��
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
     * ��ȡcity���Ե�ֵ��
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
     * ����city���Ե�ֵ��
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
     * ��ȡconsigneeCompanyName���Ե�ֵ��
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
     * ����consigneeCompanyName���Ե�ֵ��
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
     * ��ȡconsigneeEmail���Ե�ֵ��
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
     * ����consigneeEmail���Ե�ֵ��
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
     * ��ȡconsigneeFax���Ե�ֵ��
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
     * ����consigneeFax���Ե�ֵ��
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
     * ��ȡconsigneeName���Ե�ֵ��
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
     * ����consigneeName���Ե�ֵ��
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
     * ��ȡconsigneePostCode���Ե�ֵ��
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
     * ����consigneePostCode���Ե�ֵ��
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
     * ��ȡconsigneeTelephone���Ե�ֵ��
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
     * ����consigneeTelephone���Ե�ֵ��
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
     * ��ȡcustomerWeight���Ե�ֵ��
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
     * ����customerWeight���Ե�ֵ��
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
     * ��ȡdestinationCountryCode���Ե�ֵ��
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
     * ����destinationCountryCode���Ե�ֵ��
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
     * ��ȡinitialCountryCode���Ե�ֵ��
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
     * ����initialCountryCode���Ե�ֵ��
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
     * ��ȡinsurType���Ե�ֵ��
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
     * ����insurType���Ե�ֵ��
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
     * ��ȡinsurValue���Ե�ֵ��
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
     * ����insurValue���Ե�ֵ��
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
     * ��ȡmctCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMctCode() {
        return mctCode;
    }

    /**
     * ����mctCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMctCode(String value) {
        this.mctCode = value;
    }

    /**
     * ��ȡorderNo���Ե�ֵ��
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
     * ����orderNo���Ե�ֵ��
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
     * ��ȡorderNote���Ե�ֵ��
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
     * ����orderNote���Ե�ֵ��
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
     * ��ȡpaymentCode���Ե�ֵ��
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
     * ����paymentCode���Ե�ֵ��
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
     * ��ȡpieces���Ե�ֵ��
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
     * ����pieces���Ե�ֵ��
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
     * ��ȡprealertintegrationtype���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrealertintegrationtype() {
        return prealertintegrationtype;
    }

    /**
     * ����prealertintegrationtype���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrealertintegrationtype(String value) {
        this.prealertintegrationtype = value;
    }

    /**
     * ��ȡproductCode���Ե�ֵ��
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
     * ����productCode���Ե�ֵ��
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
     * ��ȡreturnSign���Ե�ֵ��
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
     * ����returnSign���Ե�ֵ��
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
     * ��ȡshipperAddress���Ե�ֵ��
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
     * ����shipperAddress���Ե�ֵ��
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
     * ��ȡshipperCity���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperCity() {
        return shipperCity;
    }

    /**
     * ����shipperCity���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperCity(String value) {
        this.shipperCity = value;
    }

    /**
     * ��ȡshipperCompanyName���Ե�ֵ��
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
     * ����shipperCompanyName���Ե�ֵ��
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
     * ��ȡshipperFax���Ե�ֵ��
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
     * ����shipperFax���Ե�ֵ��
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
     * ��ȡshipperName���Ե�ֵ��
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
     * ����shipperName���Ե�ֵ��
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
     * ��ȡshipperPostCode���Ե�ֵ��
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
     * ����shipperPostCode���Ե�ֵ��
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
     * ��ȡshipperStateOrProvince���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipperStateOrProvince() {
        return shipperStateOrProvince;
    }

    /**
     * ����shipperStateOrProvince���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipperStateOrProvince(String value) {
        this.shipperStateOrProvince = value;
    }

    /**
     * ��ȡshipperTelephone���Ե�ֵ��
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
     * ����shipperTelephone���Ե�ֵ��
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
     * ��ȡstateOrProvince���Ե�ֵ��
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
     * ����stateOrProvince���Ե�ֵ��
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
     * ��ȡstreet���Ե�ֵ��
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
     * ����street���Ե�ֵ��
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
     * ��ȡtrackingNumber���Ե�ֵ��
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
     * ����trackingNumber���Ե�ֵ��
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
     * ��ȡtransactionId���Ե�ֵ��
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
     * ����transactionId���Ե�ֵ��
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
