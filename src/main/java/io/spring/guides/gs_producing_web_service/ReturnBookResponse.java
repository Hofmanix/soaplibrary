//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.07 at 06:49:57 PM CEST 
//


package io.spring.guides.gs_producing_web_service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://spring.io/guides/gs-producing-web-service}errorResponse">
 *       &lt;sequence>
 *         &lt;element name="returnBook" type="{http://spring.io/guides/gs-producing-web-service}bookResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "returnBook"
})
@XmlRootElement(name = "returnBookResponse")
public class ReturnBookResponse
    extends ErrorResponse
{

    protected BookResponse returnBook;

    /**
     * Gets the value of the returnBook property.
     * 
     * @return
     *     possible object is
     *     {@link BookResponse }
     *     
     */
    public BookResponse getReturnBook() {
        return returnBook;
    }

    /**
     * Sets the value of the returnBook property.
     * 
     * @param value
     *     allowed object is
     *     {@link BookResponse }
     *     
     */
    public void setReturnBook(BookResponse value) {
        this.returnBook = value;
    }

}
