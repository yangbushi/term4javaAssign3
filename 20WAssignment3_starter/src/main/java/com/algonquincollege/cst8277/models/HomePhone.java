/***************************************************************************f******************u************zz*******y**
 * File: HomePhone.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 * @author George Yang 040885396
 * @date 2020 02
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 
 * home phone
 *
 */
//JPA Annotations here
@Entity
@Table(name="HPHONE")
@PrimaryKeyJoinColumn(referencedColumnName="PHONE_ID")
public class HomePhone extends PhonePojo implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    /**
     * googleMapDirections
     */
    protected String googleMapDirections;
    // TODO - additional properties to match HPHONE table
    /**
     * 
     * @return googleMapDirections
     */
    @Column(name="MAP_COORDS")
    public String getDirections() {
        return googleMapDirections; 
    }
    /**
     * 
     * @param directions
     */
    public void setDirections(String directions) {
        this.googleMapDirections = directions; 
    }
}