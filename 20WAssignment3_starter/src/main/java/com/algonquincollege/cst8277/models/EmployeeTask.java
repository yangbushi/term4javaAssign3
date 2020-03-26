/***************************************************************************f******************u************zz*******y**
 * File: EmployeeTask.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 * @author George Yang 040885396
 * @date 2020 02
 *
 */
package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//JPA Annotations here
/**
 * 
 * the task of employees'
 *
 */
@Embeddable
public class EmployeeTask {

    /**
     * description
     */
    protected String description;
    // TODO - additional properties to match EMPLOYEE_TASKS table
    /**
     * startDate
     */
    protected LocalDateTime startDate;
    /**
     * endDate
     */
    protected LocalDateTime endDate;
    /**
     * isDone
     */
    protected boolean isDone;
    /**
     * owningEmployee
     */
    protected EmployeePojo owningEmployee;
    
    /**
     * constructor
     */
    public EmployeeTask() {
    }
    
    /**
     * 
     * @return description
     */
    @Column(name = "TASK_DESCRIPTION")
    public String getDescription() {
        return description;
    }
    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * 
     * @return startDate
     */
    @Column(name = "TASK_START")
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    /**
     * 
     * @return endDate
     */
    @Column(name = "TASK_END_DATE")
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    /**
     * 
     * @return isDone
     */
    @Column(name = "TASK_DONE")
    public boolean isDone() {
        return isDone;
    }

    /**
     * 
     * @param isDone
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * 
     * @return owningEmployee
     */
    public EmployeePojo getOwningEmployee() {
        return owningEmployee;
    }

    /**
     * 
     * @param owningEmployee
     */
    public void setOwningEmployee(EmployeePojo owningEmployee) {
        this.owningEmployee = owningEmployee;
    }

}