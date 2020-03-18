/***************************************************************************f******************u************zz*******y**
 * File: EmployeeTask.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
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

@Embeddable
public class EmployeeTask {

    
    protected String description;
    // TODO - additional properties to match EMPLOYEE_TASKS table
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;
    protected boolean isDone;
    protected EmployeePojo owningEmployee;
    
    public EmployeeTask() {
    }
    @Column(name = "TASK_DESCRIPTION")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "TASK_START")
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    @Column(name = "TASK_END_DATE")
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    @Column(name = "TASK_DONE")
    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public EmployeePojo getOwningEmployee() {
        return owningEmployee;
    }

    public void setOwningEmployee(EmployeePojo owningEmployee) {
        this.owningEmployee = owningEmployee;
    }

}