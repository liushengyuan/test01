
package com.orderonline.api.fpx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>declareInvoiceItem complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="declareInvoiceItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="declareNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="declarePieces" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="declareUnitCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "declareInvoiceItem", propOrder = {
    "declareNote",
    "declarePieces",
    "declareUnitCode",
    "name",
    "unitPrice",
    "eName"
})
public class DeclareInvoiceItem {

    protected String declareNote;
    protected String declarePieces;
    protected String declareUnitCode;
    protected String name;
    protected String unitPrice;
    protected String eName;

    /**
     * 获取declareNote属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeclareNote() {
        return declareNote;
    }

    /**
     * 设置declareNote属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeclareNote(String value) {
        this.declareNote = value;
    }

    /**
     * 获取declarePieces属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeclarePieces() {
        return declarePieces;
    }

    /**
     * 设置declarePieces属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeclarePieces(String value) {
        this.declarePieces = value;
    }

    /**
     * 获取declareUnitCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeclareUnitCode() {
        return declareUnitCode;
    }

    /**
     * 设置declareUnitCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeclareUnitCode(String value) {
        this.declareUnitCode = value;
    }

    /**
     * 获取name属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * 获取unitPrice属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitPrice() {
        return unitPrice;
    }

    /**
     * 设置unitPrice属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitPrice(String value) {
        this.unitPrice = value;
    }

    /**
     * 获取eName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEName() {
        return eName;
    }

    /**
     * 设置eName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEName(String value) {
        this.eName = value;
    }

}
