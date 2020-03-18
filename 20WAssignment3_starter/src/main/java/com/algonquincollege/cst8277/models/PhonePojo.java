/***************************************************************************f******************u************zz*******y**
 * File: PhonePojo.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 * (Modified) @date 2020 02
 *
 * Copyright (c) 1998, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Original @authors dclarke, mbraeuer
 */
package com.algonquincollege.cst8277.models;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

/**
 * Phone class
 * 
 */
@MappedSuperclass
@Entity(name = "Phone")
@Table(name = "Phone")
@AttributeOverride(name = "id", column = @Column(name="PHONE_ID"))
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class PhonePojo extends PojoBase {
    public enum PhoneType {
        H, W, M
    }
    // TODO - persistent properties
    protected String areaCode;
    protected String phoneNumber;
    protected PhoneType phoneType;
    protected EmployeePojo owningEmployee;
    
    // JPA requires each @Entity class have a default constructor
    public PhonePojo() {
        super();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "PHONE_TYPE")
    @Enumerated(EnumType.STRING)
    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    @ManyToOne
    @JoinColumn(name = "OWNING_EMP_ID")
    public EmployeePojo getOwningEmployee() {
        return owningEmployee;
    }

    public void setOwningEmployee(EmployeePojo owningEmployee) {
        this.owningEmployee = owningEmployee;
    }
    
    
    
}