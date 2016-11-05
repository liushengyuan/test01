
package entity.orderonline.api.tool;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>chargeCalculateResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="chargeCalculateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ack" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="calculateFee" type="{entity.orderonlinetool.api.fpx}calculateFee" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="errors" type="{entity.orderonlinetool.api.fpx}errors" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="timeStamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chargeCalculateResponse", propOrder = {
    "ack",
    "calculateFee",
    "errors",
    "timeStamp"
})
public class ChargeCalculateResponse {

    protected String ack;
    @XmlElement(nillable = true)
    protected List<CalculateFee> calculateFee;
    @XmlElement(nillable = true)
    protected List<Errors> errors;
    protected String timeStamp;

    /**
     * ��ȡack���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAck() {
        return ack;
    }

    /**
     * ����ack���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAck(String value) {
        this.ack = value;
    }

    /**
     * Gets the value of the calculateFee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the calculateFee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCalculateFee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CalculateFee }
     * 
     * 
     */
    public List<CalculateFee> getCalculateFee() {
        if (calculateFee == null) {
            calculateFee = new ArrayList<CalculateFee>();
        }
        return this.calculateFee;
    }

    /**
     * Gets the value of the errors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Errors }
     * 
     * 
     */
    public List<Errors> getErrors() {
        if (errors == null) {
            errors = new ArrayList<Errors>();
        }
        return this.errors;
    }

    /**
     * ��ȡtimeStamp���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * ����timeStamp���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeStamp(String value) {
        this.timeStamp = value;
    }

}
