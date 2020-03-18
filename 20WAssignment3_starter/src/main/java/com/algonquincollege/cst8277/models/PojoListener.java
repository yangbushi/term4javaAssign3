/***************************************************************************f******************u************zz*******y**
 * File: PojoListener.java
 * Course materials (20W) CST 8277
 *
 * @author (original) Mike Norman
 *
 */
package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PojoListener {
    @PrePersist
    public void setCreatedOnDate(PojoBase base) {
        LocalDateTime now = LocalDateTime.now();
        base.setCreated(now);
        base.setUpdated(now);
    }

    @PreUpdate
    public void setUpdatedDate(PojoBase base) {
        base.setUpdated(LocalDateTime.now());
    }
}