//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2015.09.21 at 01:55:40 PM PDT
//

package com.xin.portal.bi.tableau.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for workbookType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="workbookType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="connectionCredentials" type="{http://tableau.com/api}connectionCredentialsType" minOccurs="0"/>
 *         &lt;element name="site" type="{http://tableau.com/api}siteType" minOccurs="0"/>
 *         &lt;element name="project" type="{http://tableau.com/api}projectType" minOccurs="0"/>
 *         &lt;element name="owner" type="{http://tableau.com/api}userType" minOccurs="0"/>
 *         &lt;element name="tags" type="{http://tableau.com/api}tagListType" minOccurs="0"/>
 *         &lt;element name="views" type="{http://tableau.com/api}viewListType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://tableau.com/api}resourceIdType" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="contentUrl" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="showTabs" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "workbookType", propOrder = {
    "site",
    "project",
    "owner",
    "tags",
    "views"
})
public class WorkbookType {

    protected SiteType site;
    protected ProjectType project;
    protected UserType owner;
    protected TagListType tags;
    protected ViewListType views;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "contentUrl")
    protected String contentUrl;
    @XmlAttribute(name = "showTabs")
    protected Boolean showTabs;

    /**
     * Gets the value of the site property.
     *
     * @return
     *     possible object is
     *     {@link SiteType }
     *
     */
    public SiteType getSite() {
        return site;
    }

    /**
     * Sets the value of the site property.
     *
     * @param value
     *     allowed object is
     *     {@link SiteType }
     *
     */
    public void setSite(SiteType value) {
        this.site = value;
    }

    /**
     * Gets the value of the project property.
     *
     * @return
     *     possible object is
     *     {@link ProjectType }
     *
     */
    public ProjectType getProject() {
        return project;
    }

    /**
     * Sets the value of the project property.
     *
     * @param value
     *     allowed object is
     *     {@link ProjectType }
     *
     */
    public void setProject(ProjectType value) {
        this.project = value;
    }

    /**
     * Gets the value of the owner property.
     *
     * @return
     *     possible object is
     *     {@link UserType }
     *
     */
    public UserType getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     *
     * @param value
     *     allowed object is
     *     {@link UserType }
     *
     */
    public void setOwner(UserType value) {
        this.owner = value;
    }

    /**
     * Gets the value of the tags property.
     *
     * @return
     *     possible object is
     *     {@link TagListType }
     *
     */
    public TagListType getTags() {
        return tags;
    }

    /**
     * Sets the value of the tags property.
     *
     * @param value
     *     allowed object is
     *     {@link TagListType }
     *
     */
    public void setTags(TagListType value) {
        this.tags = value;
    }

    /**
     * Gets the value of the views property.
     *
     * @return
     *     possible object is
     *     {@link ViewListType }
     *
     */
    public ViewListType getViews() {
        return views;
    }

    /**
     * Sets the value of the views property.
     *
     * @param value
     *     allowed object is
     *     {@link ViewListType }
     *
     */
    public void setViews(ViewListType value) {
        this.views = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
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
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
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
     * Sets the value of the name property.
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
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the contentUrl property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContentUrl() {
        return contentUrl;
    }

    /**
     * Sets the value of the contentUrl property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContentUrl(String value) {
        this.contentUrl = value;
    }

    /**
     * Gets the value of the showTabs property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isShowTabs() {
        return showTabs;
    }

    /**
     * Sets the value of the showTabs property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setShowTabs(Boolean value) {
        this.showTabs = value;
    }

}
