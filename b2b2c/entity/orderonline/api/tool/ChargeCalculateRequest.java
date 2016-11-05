
package entity.orderonline.api.tool;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>chargeCalculateRequest complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="chargeCalculateRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cargoCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayOrder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="length" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="startShipmentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="width" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chargeCalculateRequest", propOrder = {
    "cargoCode",
    "countryCode",
    "displayOrder",
    "height",
    "length",
    "postCode",
    "productCode",
    "startShipmentId",
    "weight",
    "width"
})
public class ChargeCalculateRequest {

    protected String cargoCode;
    protected String countryCode;
    protected String displayOrder;
    protected String height;
    protected String length;
    protected String postCode;
    @XmlElement(nillable = true)
    protected List<String> productCode;
    protected String startShipmentId;
    protected String weight;
    protected String width;

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
     * ��ȡdisplayOrder���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayOrder() {
        return displayOrder;
    }

    /**
     * ����displayOrder���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayOrder(String value) {
        this.displayOrder = value;
    }

    /**
     * ��ȡheight���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeight() {
        return height;
    }

    /**
     * ����height���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeight(String value) {
        this.height = value;
    }

    /**
     * ��ȡlength���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLength() {
        return length;
    }

    /**
     * ����length���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLength(String value) {
        this.length = value;
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
     * Gets the value of the productCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getProductCode() {
        if (productCode == null) {
            productCode = new ArrayList<String>();
        }
        return this.productCode;
    }

    /**
     * ��ȡstartShipmentId���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartShipmentId() {
        return startShipmentId;
    }

    /**
     * ����startShipmentId���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartShipmentId(String value) {
        this.startShipmentId = value;
    }

    /**
     * ��ȡweight���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * ����weight���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

    /**
     * ��ȡwidth���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWidth() {
        return width;
    }

    /**
     * ����width���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWidth(String value) {
        this.width = value;
    }

}
