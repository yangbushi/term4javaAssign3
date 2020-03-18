/***************************************************************************f******************u************zz*******y**
 * File: PojoBase.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 *
 * @date 2020 02
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Abstract class that is base of (class) hierarchy for all c.a.cst8277.models @Entity classes
 */
@MappedSuperclass
@Access(AccessType.PROPERTY)
public abstract class PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected int version;
    // TODO - add audit properties
    /**
     * The create time of the record.
     */
    protected LocalDateTime created;
    /**
     * The last update time of the record.
     */
    protected LocalDateTime updated;
    
    /**
     * 
     * @return created
     */
    @Column(name="CREATED_DATE")
    public LocalDateTime getCreated() {
        return created;
    }
    /**
     * 
     * @param created to set
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    /**
     * 
     * @return updated
     */
    @Column(name="UPDATED_DATE")
    public LocalDateTime getUpdated() {
        return updated;
    }
    /**
     * 
     * @param updated to set
     */
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Version
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    // Strictly speaking, JPA does not require hashcode() and equals(),
    // but it is a good idea to have one that tests using the PK (@Id) field
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PojoBase)) {
            return false;
        }
        PojoBase other = (PojoBase)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}