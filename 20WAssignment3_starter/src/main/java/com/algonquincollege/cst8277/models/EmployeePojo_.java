package com.algonquincollege.cst8277.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-03-15T22:30:01.352-0400")
@StaticMetamodel(EmployeePojo.class)
public class EmployeePojo_ extends PojoBase_ {
	public static volatile SetAttribute<EmployeePojo, ProjectPojo> projects;
	public static volatile ListAttribute<EmployeePojo, EmployeeTask> employeeTasks;
	public static volatile ListAttribute<EmployeePojo, PhonePojo> phones;
	public static volatile SingularAttribute<EmployeePojo, AddressPojo> address;
	public static volatile SingularAttribute<EmployeePojo, String> firstName;
	public static volatile SingularAttribute<EmployeePojo, String> lastName;
	public static volatile SingularAttribute<EmployeePojo, String> email;
	public static volatile SingularAttribute<EmployeePojo, String> title;
	public static volatile SingularAttribute<EmployeePojo, Double> salary;
}
