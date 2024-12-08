//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.12.08 at 03:58:36 PM MSK 
//


package ru.andryss.killers.soap.gen;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="teamId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="teamName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="teamSize" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="startCaveId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "teamId",
    "teamName",
    "teamSize",
    "startCaveId"
})
@XmlRootElement(name = "createKillerTeamRequest")
public class CreateKillerTeamRequest {

    protected int teamId;
    @XmlElement(required = true)
    protected String teamName;
    protected int teamSize;
    protected int startCaveId;

    /**
     * Gets the value of the teamId property.
     * 
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Sets the value of the teamId property.
     * 
     */
    public void setTeamId(int value) {
        this.teamId = value;
    }

    /**
     * Gets the value of the teamName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Sets the value of the teamName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeamName(String value) {
        this.teamName = value;
    }

    /**
     * Gets the value of the teamSize property.
     * 
     */
    public int getTeamSize() {
        return teamSize;
    }

    /**
     * Sets the value of the teamSize property.
     * 
     */
    public void setTeamSize(int value) {
        this.teamSize = value;
    }

    /**
     * Gets the value of the startCaveId property.
     * 
     */
    public int getStartCaveId() {
        return startCaveId;
    }

    /**
     * Sets the value of the startCaveId property.
     * 
     */
    public void setStartCaveId(int value) {
        this.startCaveId = value;
    }

}