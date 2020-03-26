/***************************************************************************f******************u************zz*******y**
 * File: MobilePhone.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 * @author George Yang 040885396
 * @date 2020 02
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

//JPA Annotations here
/**
 * 
 * mobile phone
 *
 */
@Entity
@Table(name="MPHONE")
@PrimaryKeyJoinColumn(referencedColumnName="PHONE_ID")
public class MobilePhone extends PhonePojo implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

  // TODO - additional properties to match MPHONE table
    /**
     * provider
     */
    protected String provider;

    /**
     * 
     * @return provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * 
     * @param provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
}