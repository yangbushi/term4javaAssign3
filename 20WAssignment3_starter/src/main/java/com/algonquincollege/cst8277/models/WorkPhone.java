/***************************************************************************f******************u************zz*******y**
 * File: WorkPhone.java
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

/**
 * 
 * work phone
 *
 */
//JPA Annotations here
@Entity
@Table(name="WPHONE")
@PrimaryKeyJoinColumn(referencedColumnName="PHONE_ID")
public class WorkPhone extends PhonePojo implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    
  // TODO - additional properties to match MPHONE table
    /**
     * department
     */
    protected String department;

    /**
     * 
     * @return department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * 
     * @param department
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    
}