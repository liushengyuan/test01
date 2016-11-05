
package entity.orderonline.api.tool;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>labelData complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="labelData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="companyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeAddress3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeTelephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerService" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="highInsureSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insuranceSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productEName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productShortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sales" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "labelData", propOrder = {
    "companyCode",
    "consigneeAddress1",
    "consigneeAddress2",
    "consigneeAddress3",
    "consigneeName",
    "consigneeTelephone",
    "countryCode",
    "countryName",
    "customerService",
    "highInsureSign",
    "insuranceSign",
    "postCode",
    "productCName",
    "productCode",
    "productEName",
    "productShortName",
    "returnAddress",
    "returnSign",
    "sales",
    "zone"
})
public class LabelData {

    protected String companyCode;
    protected String consigneeAddress1;
    protected String consigneeAddress2;
    protected String consigneeAddress3;
    protected String consigneeName;
    protected String consigneeTelephone;
    protected String countryCode;
    protected String countryName;
    protected String customerService;
    protected String highInsureSign;
    protected String insuranceSign;
    protected String postCode;
    protected String productCName;
    protected String productCode;
    protected String productEName;
    protected String productShortName;
    protected String returnAddress;
    protected String returnSign;
    protected String sales;
    protected String zone;

    /**
     * ��ȡcompanyCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * ����companyCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyCode(String value) {
        this.companyCode = value;
    }

    /**
     * ��ȡconsigneeAddress1���Ե�ֵ��
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
     * ����consigneeAddress1���Ե�ֵ��
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
     * ��ȡconsigneeAddress2���Ե�ֵ��
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
     * ����consigneeAddress2���Ե�ֵ��
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
     * ��ȡconsigneeAddress3���Ե�ֵ��
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
     * ����consigneeAddress3���Ե�ֵ��
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
     * ��ȡcountryCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * ����countryCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * ��ȡcountryName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * ����countryName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryName(String value) {
        this.countryName = value;
    }

    /**
     * ��ȡcustomerService���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerService() {
        return customerService;
    }

    /**
     * ����customerService���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerService(String value) {
        this.customerService = value;
    }

    /**
     * ��ȡhighInsureSign���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHighInsureSign() {
        return highInsureSign;
    }

    /**
     * ����highInsureSign���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHighInsureSign(String value) {
        this.highInsureSign = value;
    }

    /**
     * ��ȡinsuranceSign���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsuranceSign() {
        return insuranceSign;
    }

    /**
     * ����insuranceSign���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsuranceSign(String value) {
        this.insuranceSign = value;
    }

    /**
     * ��ȡpostCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * ����postCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostCode(String value) {
        this.postCode = value;
    }

    /**
     * ��ȡproductCName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCName() {
        return productCName;
    }

    /**
     * ����productCName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCName(String value) {
        this.productCName = value;
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
     * ��ȡproductEName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductEName() {
        return productEName;
    }

    /**
     * ����productEName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductEName(String value) {
        this.productEName = value;
    }

    /**
     * ��ȡproductShortName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductShortName() {
        return productShortName;
    }

    /**
     * ����productShortName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductShortName(String value) {
        this.productShortName = value;
    }

    /**
     * ��ȡreturnAddress���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnAddress() {
        return returnAddress;
    }

    /**
     * ����returnAddress���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnAddress(String value) {
        this.returnAddress = value;
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
     * ��ȡsales���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSales() {
        return sales;
    }

    /**
     * ����sales���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSales(String value) {
        this.sales = value;
    }

    /**
     * ��ȡzone���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZone() {
        return zone;
    }

    /**
     * ����zone���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZone(String value) {
        this.zone = value;
    }

}
