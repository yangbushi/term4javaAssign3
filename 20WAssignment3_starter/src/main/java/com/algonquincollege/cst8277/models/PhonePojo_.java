package com.algonquincollege.cst8277.models;

import com.algonquincollege.cst8277.models.PhonePojo.PhoneType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-03-26T00:10:22.559-0400")
@StaticMetamodel(PhonePojo.class)
public class PhonePojo_ extends PojoBase_ {
	public static volatile SingularAttribute<PhonePojo, String> areaCode;
	public static volatile SingularAttribute<PhonePojo, String> phoneNumber;
	public static volatile SingularAttribute<PhonePojo, PhoneType> phoneType;
	public static volatile SingularAttribute<PhonePojo, EmployeePojo> owningEmployee;
}
