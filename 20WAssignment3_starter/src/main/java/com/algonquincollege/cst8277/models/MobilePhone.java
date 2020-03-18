/***************************************************************************f******************u************zz*******y**
 * File: MobilePhone.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 * @date 2020 02
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

//JPA Annotations here
@Entity
@Table(name="MPHONE")
@PrimaryKeyJoinColumn(referencedColumnName="PHONE_ID")
public class MobilePhone extends PhonePojo implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

  // TODO - additional properties to match MPHONE table
    protected String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
    
}