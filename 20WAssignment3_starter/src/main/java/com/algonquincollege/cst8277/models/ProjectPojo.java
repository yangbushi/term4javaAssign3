/***************************************************************************f******************u************zz*******y**
 * File: ProjectPojo.java
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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Project class 
 */
@Entity(name = "Project")
@Table(name = "Project")
@AttributeOverride(name = "id", column = @Column(name="PROJ_ID"))
public class ProjectPojo extends PojoBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    // TODO - persistent properties
    protected String description;
    protected String name;
    protected Set<EmployeePojo> employees;
    
    // JPA requires each @Entity class have a default constructor
    public ProjectPojo() {
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy="projects")
    public Set<EmployeePojo> getEmployees() {
        return employees;
    }
    public void setEmployees(Set<EmployeePojo> employees) {
        this.employees = employees;
    }

}