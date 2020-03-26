/***************************************************************************f******************u************zz*******y**
 * File: PojoListener.java
 * Course materials (20W) CST 8277
 *
 * @author (original) Mike Norman
 * @author George Yang 040885396
 *
 */
package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 
 * listener to set check points
 *
 */
public class PojoListener {
    /**
     * 
     * @param base
     */
    @PrePersist
    public void setCreatedOnDate(PojoBase base) {
        LocalDateTime now = LocalDateTime.now();
        base.setCreated(now);
        base.setUpdated(now);
    }

    /**
     * 
     * @param base
     */
    @PreUpdate
    public void setUpdatedDate(PojoBase base) {
        base.setUpdated(LocalDateTime.now());
    }
}