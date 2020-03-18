package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-03-11T22:44:53.613-0400")
@StaticMetamodel(EmployeeTask.class)
public class EmployeeTask_ {
	public static volatile SingularAttribute<EmployeeTask, String> description;
	public static volatile SingularAttribute<EmployeeTask, LocalDateTime> startDate;
	public static volatile SingularAttribute<EmployeeTask, LocalDateTime> endDate;
	public static volatile SingularAttribute<EmployeeTask, Boolean> done;
	public static volatile SingularAttribute<EmployeeTask, EmployeePojo> owningEmployee;
}
