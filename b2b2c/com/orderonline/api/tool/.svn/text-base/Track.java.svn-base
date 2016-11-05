
package com.orderonline.api.tool;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>track complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="track">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="destinationCountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signForDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signForPerson" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackInfo" type="{entity.orderonlinetool.api.fpx}trackInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="trackingNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "track", propOrder = {
    "destinationCountryCode",
    "productCode",
    "signForDate",
    "signForPerson",
    "trackInfo",
    "trackingNumber"
})
public class Track {

    protected String destinationCountryCode;
    protected String productCode;
    protected String signForDate;
    protected String signForPerson;
    @XmlElement(nillable = true)
    protected List<TrackInfo> trackInfo;
    protected String trackingNumber;

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
     * ��ȡsignForDate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignForDate() {
        return signForDate;
    }

    /**
     * ����signForDate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignForDate(String value) {
        this.signForDate = value;
    }

    /**
     * ��ȡsignForPerson���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignForPerson() {
        return signForPerson;
    }

    /**
     * ����signForPerson���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignForPerson(String value) {
        this.signForPerson = value;
    }

    /**
     * Gets the value of the trackInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trackInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrackInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TrackInfo }
     * 
     * 
     */
    public List<TrackInfo> getTrackInfo() {
        if (trackInfo == null) {
            trackInfo = new ArrayList<TrackInfo>();
        }
        return this.trackInfo;
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

}
