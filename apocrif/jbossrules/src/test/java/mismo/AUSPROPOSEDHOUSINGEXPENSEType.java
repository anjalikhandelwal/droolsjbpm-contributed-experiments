//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.05.21 at 09:59:49 PM BST 
//


package mismo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for AUS_PROPOSED_HOUSING_EXPENSE_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AUS_PROPOSED_HOUSING_EXPENSE_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="_ID" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="HousingExpenseType" type="{}AUS_HousingExpenseTypeEnumerated" />
 *       &lt;attribute name="_PaymentAmount" type="{}AUS_MISMOMoney" />
 *       &lt;attribute name="HousingExpenseTypeOtherDescription" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AUS_PROPOSED_HOUSING_EXPENSE_Type")
public class AUSPROPOSEDHOUSINGEXPENSEType {

    @XmlAttribute(name = "_ID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "HousingExpenseType")
    protected AUSHousingExpenseTypeEnumerated housingExpenseType;
    @XmlAttribute(name = "_PaymentAmount")
    protected String paymentAmount;
    @XmlAttribute(name = "HousingExpenseTypeOtherDescription")
    protected String housingExpenseTypeOtherDescription;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the housingExpenseType property.
     * 
     * @return
     *     possible object is
     *     {@link AUSHousingExpenseTypeEnumerated }
     *     
     */
    public AUSHousingExpenseTypeEnumerated getHousingExpenseType() {
        return housingExpenseType;
    }

    /**
     * Sets the value of the housingExpenseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AUSHousingExpenseTypeEnumerated }
     *     
     */
    public void setHousingExpenseType(AUSHousingExpenseTypeEnumerated value) {
        this.housingExpenseType = value;
    }

    /**
     * Gets the value of the paymentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the value of the paymentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentAmount(String value) {
        this.paymentAmount = value;
    }

    /**
     * Gets the value of the housingExpenseTypeOtherDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHousingExpenseTypeOtherDescription() {
        return housingExpenseTypeOtherDescription;
    }

    /**
     * Sets the value of the housingExpenseTypeOtherDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHousingExpenseTypeOtherDescription(String value) {
        this.housingExpenseTypeOtherDescription = value;
    }

}
