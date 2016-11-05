
package entity.orderonline.api.tool;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>chargeCalculateServiceResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="chargeCalculateServiceResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{entity.orderonlinetool.api.fpx}chargeCalculateResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chargeCalculateServiceResponse", propOrder = {
    "_return"
})
public class ChargeCalculateServiceResponse {

    @XmlElement(name = "return")
    protected ChargeCalculateResponse _return;

    /**
     * ��ȡreturn���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ChargeCalculateResponse }
     *     
     */
    public ChargeCalculateResponse getReturn() {
        return _return;
    }

    /**
     * ����return���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ChargeCalculateResponse }
     *     
     */
    public void setReturn(ChargeCalculateResponse value) {
        this._return = value;
    }

}
