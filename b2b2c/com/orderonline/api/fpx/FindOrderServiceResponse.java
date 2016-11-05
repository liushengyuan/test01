
package com.orderonline.api.fpx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>findOrderServiceResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="findOrderServiceResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{entity.orderonline.api.fpx}findOrderResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findOrderServiceResponse", propOrder = {
    "_return"
})
public class FindOrderServiceResponse {

    @XmlElement(name = "return")
    protected FindOrderResponse _return;

    /**
     * 获取return属性的值。
     * 
     * @return
     *     possible object is
     *     {@link FindOrderResponse }
     *     
     */
    public FindOrderResponse getReturn() {
        return _return;
    }

    /**
     * 设置return属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link FindOrderResponse }
     *     
     */
    public void setReturn(FindOrderResponse value) {
        this._return = value;
    }

}
