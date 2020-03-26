/***************************************************************************f******************u************zz*******y**
 * File: EmployeeTestSuite.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author George Yang 040885396
 *
 *
 */
package com.algonquincollege.cst8277.models;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringStartsWith.startsWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algonquincollege.cst8277.TestSuiteBase;
import com.algonquincollege.cst8277.models.PhonePojo.PhoneType;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * 
 * all test cases
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeSystemTestSuite extends TestSuiteBase {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);

    private static final ch.qos.logback.classic.Logger eclipselinkSqlLogger =
        (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ECLIPSELINK_LOGGING_SQL);

    private static final String SELECT_EMPLOYEE_1 =
        "SELECT EMP_ID, CREATED_DATE, EMAIL, FNAME, LNAME, SALARY, TITLE, UPDATED_DATE, VERSION, ADDR_ID FROM EMPLOYEE WHERE (EMP_ID = ?)";
    @Test
    public void test01_no_Employees_at_start() {
        logger.info("no employees, just demonstrating how to capture generated SQL");
        EntityManager em = emf.createEntityManager();

        ListAppender<ILoggingEvent> listAppender = attachListAppender(eclipselinkSqlLogger, ECLIPSELINK_LOGGING_SQL);
        EmployeePojo emp1 = em.find(EmployeePojo.class, 1);
        detachListAppender(eclipselinkSqlLogger, listAppender);

        assertNull(emp1);
        List<ILoggingEvent> loggingEvents = listAppender.list;
        assertEquals(1, loggingEvents.size());
        assertThat(loggingEvents.get(0).getMessage(),
            startsWith(SELECT_EMPLOYEE_1));

        em.close();
    }

    // C-R-U-D lifecycle

    // 'Interesting' Queries
    @Test
    public void test02_insert_one_employee() {
        logger.info("check 1 employee successfully inserted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String fName = "George";
        EmployeePojo emp1 = new EmployeePojo();
        emp1.setFirstName(fName);
        
        et.begin();
        em.persist(emp1);
        et.commit();
        
        EmployeePojo empGot = em.find(EmployeePojo.class, 1);
        assertNotNull(empGot);
        assertEquals(fName, empGot.getFirstName());
        em.close();
    }

    @Test
    public void test03_insert_two_employees() {
        logger.info("check 2 employees successfully inserted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String fName1 = "George1";
        EmployeePojo emp1 = new EmployeePojo();
        emp1.setFirstName(fName1);
        String fName2 = "George2";
        EmployeePojo emp2 = new EmployeePojo();
        emp2.setFirstName(fName2);
        
        et.begin();
        em.persist(emp1);
        et.commit();
        EmployeePojo empGot1 = em.find(EmployeePojo.class, 2);
        assertNotNull(empGot1);
        assertEquals(fName1, empGot1.getFirstName());
        
        et.begin();
        em.persist(emp2);
        et.commit();
        EmployeePojo empGot2 = em.find(EmployeePojo.class, 3);
        assertNotNull(empGot2);
        assertEquals(fName2, empGot2.getFirstName());
        em.close();
    }
    
    @Test
    public void test04_update_one_employee() {
        logger.info("check 1 employee successfully updated");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        EmployeePojo emp = em.find(EmployeePojo.class, 1);
        String fName = "George_test04";
        emp.setFirstName(fName);

        et.begin();
        em.merge(emp);
        et.commit();
        
        EmployeePojo empGot = em.find(EmployeePojo.class, 1);
        assertNotNull(empGot);
        assertEquals(fName, empGot.getFirstName());
        em.close();
    }

    @Test
    public void test05_update_two_employees() {
        logger.info("check 2 employees successfully updated");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        EmployeePojo emp1 = em.find(EmployeePojo.class, 1);
        String fName1 = "George_test05";
        emp1.setFirstName(fName1);
        EmployeePojo emp2 = em.find(EmployeePojo.class, 2);
        String fName2 = "George_test06";
        emp2.setFirstName(fName2);
        et.begin();
        em.merge(emp1);
        em.merge(emp2);
        et.commit();
        
        EmployeePojo empGot1 = em.find(EmployeePojo.class, 1);
        assertNotNull(empGot1);
        assertEquals(fName1, empGot1.getFirstName());
        EmployeePojo empGot2 = em.find(EmployeePojo.class, 2);
        assertNotNull(empGot2);
        assertEquals(fName2, empGot2.getFirstName());
        em.close();
    }
    
    @Test
    public void test06_delete_one_employee() {
        logger.info("check 1 employee successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        EmployeePojo emp = em.find(EmployeePojo.class, 1);
        assertNotNull(emp);
        
        et.begin();
        em.remove(emp);
        et.commit();
        
        EmployeePojo empGot = em.find(EmployeePojo.class, 1);
        assertNull(empGot);
        em.close();
    }

    @Test
    public void test07_delete_two_employees() {
        logger.info("check 2 employee successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        EmployeePojo emp1 = em.find(EmployeePojo.class, 2);
        assertNotNull(emp1);
        EmployeePojo emp2 = em.find(EmployeePojo.class, 3);
        assertNotNull(emp2);
        
        et.begin();
        em.remove(emp1);
        em.remove(emp2);
        et.commit();
        
        EmployeePojo empGot1 = em.find(EmployeePojo.class, 2);
        assertNull(empGot1);
        EmployeePojo empGot2 = em.find(EmployeePojo.class, 3);
        assertNull(empGot2);
        em.close();
    }
    
    @Test
    public void test08_delete_all_employees() {
        logger.info("check all employees successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String fName1 = "George1";
        EmployeePojo emp1 = new EmployeePojo();
        emp1.setFirstName(fName1);
        String fName2 = "George2";
        EmployeePojo emp2 = new EmployeePojo();
        emp2.setFirstName(fName2);
        
        et.begin();
        em.persist(emp1);
        em.persist(emp2);
        et.commit();
        
        TypedQuery<Long> empQuery = em.createQuery(
                "select count(e) from Employee e ",
                Long.class);
        Long numEmps = empQuery.getSingleResult();
        assertEquals(new Long(2), numEmps);
        
        et.begin();
        em.createQuery("DELETE FROM Employee").executeUpdate();
        et.commit();
        
        empQuery = em.createQuery(
                "select count(e) from Employee e ",
                Long.class);
        numEmps = empQuery.getSingleResult();
        assertEquals(new Long(0), numEmps);
        em.close();
    }
    
    @Test
    public void test09_query_expensive_employees() {
        logger.info("check certain type of employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        EmployeePojo emp1 = new EmployeePojo();
        String fName = "George3";
        emp1.setFirstName(fName);
        emp1.setSalary(999999.99);
        EmployeePojo emp2 = new EmployeePojo();
        emp2.setFirstName("George4");
        emp2.setSalary(111111.11);
        
        et.begin();
        em.persist(emp1);
        em.persist(emp2);
        et.commit();
        
        TypedQuery<EmployeePojo> expensiveQuery = em.createQuery(
                "select e from Employee e where e.salary > 200000",
                EmployeePojo.class);
        List<EmployeePojo> expensiveEmps = expensiveQuery.getResultList();
        assertNotNull(expensiveEmps);
        assertNotNull(expensiveEmps.get(0));
        assertThat(expensiveEmps.get(0).getFirstName(), equalTo(fName));
        em.close();
    }

    @Test
    public void test10_query_employee_by_fname() {
        logger.info("check certain type of employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        
        String fName = "George3";
        
        TypedQuery<EmployeePojo> empQuery = em.createQuery(
                "select e from Employee e where e.firstName = :param1",
                EmployeePojo.class);
        empQuery.setParameter("param1", fName);
        List<EmployeePojo> emps = empQuery.getResultList();
        assertNotNull(emps);
        assertNotNull(emps.get(0));
        assertThat(emps.get(0).getFirstName(), equalTo(fName));
        em.close();
    }
    
    @Test
    public void test11_count_employees() {
        logger.info("check the count of employees is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(e) from Employee e", Long.class);
        Long numEmployees = countQuery.getSingleResult();
        assertEquals(new Long(2), numEmployees);
    }

    @Test
    public void test12_count_employees_by_fname() {
        logger.info("check the count of employees for certain first name is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(e) from Employee e where e.firstName = :param1", Long.class);
        String fName = "George3";
        countQuery.setParameter("param1", fName);
        Long numEmployees = countQuery.getSingleResult();
        assertEquals(new Long(1), numEmployees);
    }
    
    @Test
    public void test13_query_employee_has_no_project() {
        logger.info("check no project employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        ProjectPojo project1 = new ProjectPojo();
        project1.setName("project1");
        et.begin();
        em.persist(project1);
        et.commit();
        
        et.begin();
        em.createQuery("DELETE FROM Employee").executeUpdate();
        et.commit();
        
        EmployeePojo emp1 = new EmployeePojo();
        emp1.setFirstName("George1");
        emp1.setLastName("Yang");
        EmployeePojo emp2 = new EmployeePojo();
        emp2.setFirstName("George2");
        et.begin();
        em.persist(emp1);
        em.persist(emp2);
        et.commit();
        
        Set<ProjectPojo> projects = new HashSet<ProjectPojo>();
        projects.add(project1);
        emp1.setProjects(projects); // employee 1 do project 1
        et.begin();
        em.merge(emp1);
        et.commit();
        
        TypedQuery<EmployeePojo> noProjEmpQuery = em.createQuery(
                "select e from Employee e where e.projects is empty",
                EmployeePojo.class);
        List<EmployeePojo> noProjEmps = noProjEmpQuery.getResultList();
        assertNotNull(noProjEmps);
        assertNotNull(noProjEmps.get(0));
        assertThat(noProjEmps.get(0).getFirstName(), equalTo(emp2.getFirstName()));
        em.close();
    }

    @Test
    public void test14_query_employee_has_project() {
        logger.info("check with project employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<EmployeePojo> noProjEmpQuery = em.createQuery(
                "select e from Employee e where e.projects is not empty",
                EmployeePojo.class);
        List<EmployeePojo> projEmps = noProjEmpQuery.getResultList();
        assertNotNull(projEmps);
        assertNotNull(projEmps.get(0));
        assertThat(projEmps.get(0).getFirstName(), equalTo("George1"));
        em.close();
    }
    
    @Test
    public void test15_criteria_last_name() {
        logger.info("check criteria query successfully get employees by last name");
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
        CriteriaQuery<EmployeePojo> query = queryBuilder.createQuery(EmployeePojo.class);
        Root<EmployeePojo> root = query.from(EmployeePojo.class);
        query.where(queryBuilder.like(queryBuilder.upper(root.get(EmployeePojo_.lastName)),"Y%"));
        List<EmployeePojo> lNameEmps = em.createQuery(query).getResultList();
        assertNotNull(lNameEmps);
        assertNotNull(lNameEmps.get(0));
        assertThat(lNameEmps.get(0).getLastName(), startsWith("Y"));
        em.close();
    }

    @Test
    public void test16_criteria_first_name() {
        logger.info("check criteria query successfully get employees by first name");
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
        CriteriaQuery<EmployeePojo> query = queryBuilder.createQuery(EmployeePojo.class);
        Root<EmployeePojo> root = query.from(EmployeePojo.class);
        query.where(queryBuilder.like(queryBuilder.upper(root.get(EmployeePojo_.firstName)),"GEORGE%"));
        List<EmployeePojo> fNameEmps = em.createQuery(query).getResultList();
        assertNotNull(fNameEmps);
        assertEquals(2, fNameEmps.size());
        em.close();
    }
    
    @Test
    public void test17_insert_one_address() {
        logger.info("check 1 address successfully inserted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String city = "Ottawa";
        AddressPojo add1 = new AddressPojo();
        add1.setCity(city);
        
        et.begin();
        em.persist(add1);
        et.commit();
        
        AddressPojo addGot = em.find(AddressPojo.class, 1);
        assertNotNull(addGot);
        assertEquals(city, addGot.getCity());
        em.close();
    }

    @Test
    public void test18_insert_two_addresses() {
        logger.info("check 2 addresses successfully inserted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String city1 = "Montreal";
        AddressPojo add1 = new AddressPojo();
        add1.setCity(city1);
        String city2 = "Toronto";
        AddressPojo add2 = new AddressPojo();
        add2.setCity(city2);
        
        et.begin();
        em.persist(add1);
        et.commit();
        AddressPojo addGot1 = em.find(AddressPojo.class, 2);
        assertNotNull(addGot1);
        assertEquals(city1, addGot1.getCity());
        
        et.begin();
        em.persist(add2);
        et.commit();
        AddressPojo addGot2 = em.find(AddressPojo.class, 3);
        assertNotNull(addGot2);
        assertEquals(city2, addGot2.getCity());
        em.close();
    }
    
    @Test
    public void test19_update_one_address() {
        logger.info("check 1 address successfully updated");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        AddressPojo add1 = em.find(AddressPojo.class, 1);
        String country1 = "Canada";
        add1.setCountry(country1);

        et.begin();
        em.merge(add1);
        et.commit();
        
        AddressPojo addGot = em.find(AddressPojo.class, 1);
        assertNotNull(addGot);
        assertEquals(country1, addGot.getCountry());
        em.close();
    }

    @Test
    public void test20_update_two_address() {
        logger.info("check 2 addresses successfully updated");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String state = "On";
        AddressPojo add1 = em.find(AddressPojo.class, 1);
        add1.setState(state);
        AddressPojo add3 = em.find(AddressPojo.class, 3);
        add3.setState(state);

        et.begin();
        em.merge(add1);
        em.merge(add3);
        et.commit();
        
        AddressPojo addGot1 = em.find(AddressPojo.class, 1);
        assertNotNull(addGot1);
        assertEquals(state, addGot1.getState());
        AddressPojo addGot3 = em.find(AddressPojo.class, 3);
        assertNotNull(addGot3);
        assertEquals(state, addGot3.getState());
        em.close();
    }
    
    @Test
    public void test21_delete_one_address() {
        logger.info("check 1 address successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        AddressPojo add1 = em.find(AddressPojo.class, 1);
        assertNotNull(add1);
        
        et.begin();
        em.remove(add1);
        et.commit();
        
        AddressPojo addGot = em.find(AddressPojo.class, 1);
        assertNull(addGot);
        em.close();
    }

    @Test
    public void test22_delete_two_addresses() {
        logger.info("check 2 addresses successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        AddressPojo add2 = em.find(AddressPojo.class, 2);
        assertNotNull(add2);
        AddressPojo add3 = em.find(AddressPojo.class, 3);
        assertNotNull(add3);
        
        et.begin();
        em.remove(add2);
        em.remove(add3);
        et.commit();
        
        AddressPojo addGot2 = em.find(AddressPojo.class, 2);
        assertNull(addGot2);
        AddressPojo addGot3 = em.find(AddressPojo.class, 3);
        assertNull(addGot3);
        em.close();
    }
    
    @Test
    public void test23_delete_all_addresses() {
        logger.info("check all addresses successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String city1 = "Montreal";
        AddressPojo add1 = new AddressPojo();
        add1.setCity(city1);
        String city2 = "Toronto";
        AddressPojo add2 = new AddressPojo();
        add2.setCity(city2);
        
        et.begin();
        em.persist(add1);
        em.persist(add2);
        et.commit();
        
        TypedQuery<Long> addQuery = em.createQuery(
                "select count(a) from Address a ",
                Long.class);
        Long numAdds = addQuery.getSingleResult();
        assertEquals(new Long(2), numAdds);
        
        et.begin();
        em.createQuery("DELETE FROM Address").executeUpdate();
        et.commit();
        
        addQuery = em.createQuery(
                "select count(a) from Address a ",
                Long.class);
        numAdds = addQuery.getSingleResult();
        assertEquals(new Long(0), numAdds);
        em.close();
    }
    
    @Test
    public void test24_query_canada_address() {
        logger.info("check Canadian addresses successfully queried out");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        AddressPojo add1 = new AddressPojo();
        add1.setCity("Ottawa");
        String country1 = "Canada";
        add1.setCountry(country1);

        et.begin();
        em.persist(add1);
        et.commit();
        
        TypedQuery<AddressPojo> addressQuery = em.createQuery(
                "select a from Address a where a.country = 'Canada'",
                AddressPojo.class);
        List<AddressPojo> canadianAdds = addressQuery.getResultList();
        assertNotNull(canadianAdds);
        assertNotNull(canadianAdds.get(0));
        assertThat(canadianAdds.get(0).getCountry(), equalTo("Canada"));
        em.close();
    }

    @Test
    public void test25_query_address_by_city() {
        logger.info("check Ottawa addresses successfully queried out");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<AddressPojo> addressQuery = em.createQuery(
                "select a from Address a where a.city = 'Ottawa'",
                AddressPojo.class);
        AddressPojo address = addressQuery.getSingleResult();
        assertNotNull(address);
        assertThat(address.getCity(), equalTo("Ottawa"));
        em.close();
    }
    
    @Test
    public void test26_count_addresses() {
        logger.info("check the count of addresses is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(a) from Address a", Long.class);
        Long numAddresses = countQuery.getSingleResult();
        assertEquals(new Long(1), numAddresses);
    }

    @Test
    public void test27_count_addresses_by_city() {
        logger.info("check the count of addresses is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(a) from Address a where a.city = 'Ottawa'", Long.class);
        Long numAddresses = countQuery.getSingleResult();
        assertEquals(new Long(1), numAddresses);
    }
    
    @Test
    public void test28_query_employee_has_no_address() {
        logger.info("check no address employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createQuery("DELETE FROM Employee").executeUpdate();
        et.commit();
        
        EmployeePojo emp1 = new EmployeePojo();
        emp1.setFirstName("George1");
        emp1.setLastName("Yang");
        EmployeePojo emp2 = new EmployeePojo();
        emp2.setFirstName("George2");
        et.begin();
        em.persist(emp1);
        em.persist(emp2);
        et.commit();
        
        String city = "Toronto";
        AddressPojo add1 = new AddressPojo();
        add1.setCity(city);
        et.begin();
        em.persist(add1);
        et.commit();
        
        emp1.setAddress(add1); // employee 1 has address 1
        et.begin();
        em.merge(emp1);
        et.commit();
        
        TypedQuery<EmployeePojo> noAddEmpQuery = em.createQuery(
                "select e from Employee e where e.address is null",
                EmployeePojo.class);
        List<EmployeePojo> noAddEmps = noAddEmpQuery.getResultList();
        assertNotNull(noAddEmps);
        assertNotNull(noAddEmps.get(0));
        assertThat(noAddEmps.get(0).getFirstName(), equalTo(emp2.getFirstName()));
        em.close();
    }

    @Test
    public void test29_query_employee_has_address() {
        logger.info("check with address employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<EmployeePojo> empQuery = em.createQuery(
                "select e from Employee e where e.address is not null",
                EmployeePojo.class);
        EmployeePojo emp = empQuery.getSingleResult();
        assertNotNull(emp);
        assertThat(emp.getFirstName(), equalTo("George1"));
        em.close();
    }
    
    @Test
    public void test30_criteria_address_county() {
        logger.info("check criteria query successfully get addresses by county");
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
        CriteriaQuery<AddressPojo> query = queryBuilder.createQuery(AddressPojo.class);
        Root<AddressPojo> root = query.from(AddressPojo.class);
        query.where(queryBuilder.like(queryBuilder.upper(root.get(AddressPojo_.country)),"CA%"));
        List<AddressPojo> caAdds = em.createQuery(query).getResultList();
        assertNotNull(caAdds);
        assertNotNull(caAdds.get(0));
        assertThat(caAdds.get(0).getCountry(), startsWith("Ca"));
        em.close();
    }

    @Test
    public void test31_criteria_address_city() {
        logger.info("check criteria query successfully get addresses by city");
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
        CriteriaQuery<AddressPojo> query = queryBuilder.createQuery(AddressPojo.class);
        Root<AddressPojo> root = query.from(AddressPojo.class);
        query.where(queryBuilder.like(queryBuilder.upper(root.get(AddressPojo_.city)),"TO%"));
        List<AddressPojo> adds = em.createQuery(query).getResultList();
        assertNotNull(adds);
        assertNotNull(adds.get(0));
        assertThat(adds.get(0).getCity(), startsWith("To"));
        em.close();
    }
    
    @Test
    public void test32_insert_one_phone() {
        logger.info("check 1 phone successfully inserted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String areaCode = "613";
        String phoneNumber = "1234567";
        PhoneType phoneType = PhoneType.H;
        PhonePojo hPhone1 = new HomePhone();
        hPhone1.setAreaCode(areaCode);
        hPhone1.setPhoneNumber(phoneNumber);
        hPhone1.setPhoneType(phoneType);
        
        et.begin();
        em.persist(hPhone1);
        et.commit();
        
        PhonePojo phoneGot = em.find(HomePhone.class, 1);
        assertNotNull(phoneGot);
        assertThat(phoneGot.getAreaCode(), is(areaCode));
        em.close();
    }

    @Test
    public void test33_insert_two_phones() {
        logger.info("check 2 phones successfully inserted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String areaCode1 = "416";
        String phoneNumber1 = "1111111";
        PhoneType phoneType1 = PhoneType.M;
        PhonePojo mPhone1 = new MobilePhone();
        mPhone1.setAreaCode(areaCode1);
        mPhone1.setPhoneNumber(phoneNumber1);
        mPhone1.setPhoneType(phoneType1);
        String areaCode2 = "514";
        String phoneNumber2 = "2222222";
        PhoneType phoneType2 = PhoneType.W;
        PhonePojo wPhone2 = new WorkPhone();
        wPhone2.setAreaCode(areaCode2);
        wPhone2.setPhoneNumber(phoneNumber2);
        wPhone2.setPhoneType(phoneType2);
        
        et.begin();
        em.persist(mPhone1);
        em.persist(wPhone2);
        et.commit();
        
        PhonePojo phoneGot1 = em.find(MobilePhone.class, 2);
        assertNotNull(phoneGot1);
        assertThat(phoneGot1.getAreaCode(), is(areaCode1));
        PhonePojo phoneGot2 = em.find(WorkPhone.class, 3);
        assertNotNull(phoneGot2);
        assertThat(phoneGot2.getAreaCode(), is(areaCode2));
        em.close();
    }
    
    @Test
    public void test34_update_one_phone() {
        logger.info("check 1 phone successfully updated");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        PhonePojo phone1 = em.find(HomePhone.class, 1);
        String phoneNumber = "1111111";
        phone1.setPhoneNumber(phoneNumber);

        et.begin();
        em.merge(phone1);
        et.commit();
        
        PhonePojo phoneGot = em.find(HomePhone.class, 1);
        assertNotNull(phoneGot);
        assertThat(phoneGot.getPhoneNumber(), is(phoneNumber));
        em.close();
    }

    @Test
    public void test35_update_two_phones() {
        logger.info("check 2 phones successfully updated");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        MobilePhone phone1 = em.find(MobilePhone.class, 2);
        String provider = "bell";
        phone1.setProvider(provider);
        WorkPhone phone2 = em.find(WorkPhone.class, 3);
        String department = "cra";
        phone2.setDepartment(department);

        et.begin();
        em.merge(phone1);
        em.merge(phone2);
        et.commit();
        
        MobilePhone phoneGot1 = em.find(MobilePhone.class, 2);
        assertNotNull(phoneGot1);
        assertThat(phoneGot1.getProvider(), is(provider));
        WorkPhone phoneGot2 = em.find(WorkPhone.class, 3);
        assertNotNull(phoneGot2);
        assertThat(phoneGot2.getDepartment(), is(department));
        em.close();
    }
    
    @Test
    public void test36_delete_one_phone() {
        logger.info("check 1 phone successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        PhonePojo phone1 = em.find(HomePhone.class, 1);
        assertNotNull(phone1);
        
        et.begin();
        em.remove(phone1);
        et.commit();
        
        PhonePojo phoneGot = em.find(HomePhone.class, 1);
        assertNull(phoneGot);
        em.close();
    }

    @Test
    public void test37_delete_two_phones() {
        logger.info("check 2 phones successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        PhonePojo phone1 = em.find(MobilePhone.class, 2);
        assertNotNull(phone1);
        PhonePojo phone2 = em.find(WorkPhone.class, 3);
        assertNotNull(phone2);
        
        et.begin();
        em.remove(phone1);
        em.remove(phone2);
        et.commit();
        
        PhonePojo phoneGot1 = em.find(MobilePhone.class, 2);
        assertNull(phoneGot1);
        PhonePojo phoneGot2 = em.find(WorkPhone.class, 3);
        assertNull(phoneGot2);
        em.close();
    }
    
    @Test
    public void test38_delete_all_phones() {
        logger.info("check all phones successfully deleted");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String areaCode1 = "416";
        String phoneNumber1 = "1111111";
        PhoneType phoneType1 = PhoneType.M;
        PhonePojo mPhone1 = new MobilePhone();
        mPhone1.setAreaCode(areaCode1);
        mPhone1.setPhoneNumber(phoneNumber1);
        mPhone1.setPhoneType(phoneType1);
        String areaCode2 = "514";
        String phoneNumber2 = "2222222";
        PhoneType phoneType2 = PhoneType.W;
        PhonePojo wPhone2 = new WorkPhone();
        wPhone2.setAreaCode(areaCode2);
        wPhone2.setPhoneNumber(phoneNumber2);
        wPhone2.setPhoneType(phoneType2);
        
        et.begin();
        em.persist(mPhone1);
        em.persist(wPhone2);
        et.commit();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(p) from Phone p", Long.class);
        Long numPhones = countQuery.getSingleResult();
        assertEquals(new Long(2), numPhones);
        
        et.begin();
        em.createQuery("DELETE FROM Phone").executeUpdate();
        et.commit();
        
        countQuery = em.createQuery(
                "select count(p) from Phone p", Long.class);
        numPhones = countQuery.getSingleResult();
        assertEquals(new Long(0), numPhones);
        em.close();
    }
    
    @Test
    public void test39_query_phone_phoneNumber() {
        logger.info("check certain type of phone successfully queried out");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String areaCode = "613";
        String phoneNumber = "1234567";
        PhoneType phoneType = PhoneType.H;
        PhonePojo hPhone1 = new HomePhone();
        hPhone1.setAreaCode(areaCode);
        hPhone1.setPhoneNumber(phoneNumber);
        hPhone1.setPhoneType(phoneType);
        
        et.begin();
        em.persist(hPhone1);
        et.commit();
        
        TypedQuery<HomePhone> hPhoneQuery = em.createQuery(
                "select h from HomePhone h where h.phoneNumber = :param1", HomePhone.class);
        hPhoneQuery.setParameter("param1", phoneNumber);
        PhonePojo phone1 = hPhoneQuery.getSingleResult();
        assertNotNull(phone1);
        assertThat(phone1.getPhoneNumber(), equalTo(phoneNumber));
        em.close();
    }

    @Test
    public void test40_query_phone_by_area() {
        logger.info("check certain type of phone successfully queried out");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String areaCode = "514";
        String phoneNumber = "1234567";
        PhoneType phoneType = PhoneType.H;
        PhonePojo hPhone1 = new HomePhone();
        hPhone1.setAreaCode(areaCode);
        hPhone1.setPhoneNumber(phoneNumber);
        hPhone1.setPhoneType(phoneType);
        
        et.begin();
        em.persist(hPhone1);
        et.commit();
        
        TypedQuery<HomePhone> hPhoneQuery = em.createQuery(
                "select h from HomePhone h where h.areaCode = :param1", HomePhone.class);
        hPhoneQuery.setParameter("param1", areaCode);
        PhonePojo phone1 = hPhoneQuery.getSingleResult();
        assertNotNull(phone1);
        assertThat(phone1.getAreaCode(), equalTo(areaCode));
        em.close();
    }
    
    @Test
    public void test41_count_phones() {
        logger.info("check the count of phones is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(p) from Phone p", Long.class);
        Long numPhones = countQuery.getSingleResult();
        assertEquals(new Long(2), numPhones);
    }

    @Test
    public void test42_count_phones_by_area() {
        logger.info("check the count of phones is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(p) from Phone p where p.areaCode = :param1", Long.class);
        String areaCode = "514";
        countQuery.setParameter("param1", areaCode);
        Long numPhones = countQuery.getSingleResult();
        assertEquals(new Long(1), numPhones);
    }
    
    @Test
    public void test43_count_homePhones() {
        logger.info("check the count of home phones is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(h) from HomePhone h", Long.class);
        Long numPhones = countQuery.getSingleResult();
        assertEquals(new Long(2), numPhones);
    }

    @Test
    public void test44_count_homePhones_from_phone() {
        logger.info("check the count of home phones is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(p) from Phone p where p.phoneType = :param1", Long.class);
        countQuery.setParameter("param1", PhoneType.H);
        Long numPhones = countQuery.getSingleResult();
        assertEquals(new Long(2), numPhones);
    }
    
    @Test
    public void test45_count_mobilePhones() {
        logger.info("check the count of mobile phones is correct");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String areaCode1 = "416";
        String phoneNumber1 = "1111111";
        PhoneType phoneType1 = PhoneType.M;
        PhonePojo mPhone1 = new MobilePhone();
        mPhone1.setAreaCode(areaCode1);
        mPhone1.setPhoneNumber(phoneNumber1);
        mPhone1.setPhoneType(phoneType1);
        
        et.begin();
        em.persist(mPhone1);
        et.commit();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(m) from MobilePhone m", Long.class);
        Long numPhones = countQuery.getSingleResult();
        assertEquals(new Long(1), numPhones);
    }

    @Test
    public void test46_count_mobilePhones_from_phone() {
        logger.info("check the count of mobile phones is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(p) from Phone p where p.phoneType = :param1", Long.class);
        countQuery.setParameter("param1", PhoneType.M);
        Long numPhones = countQuery.getSingleResult();
        assertEquals(new Long(1), numPhones);
    }
    
    @Test
    public void test47_query_employee_has_no_phone() {
        logger.info("check no phone employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String areaCode = "613";
        String phoneNumber = "7654321";
        PhoneType phoneType = PhoneType.M;
        PhonePojo mPhone1 = new MobilePhone();
        mPhone1.setAreaCode(areaCode);
        mPhone1.setPhoneNumber(phoneNumber);
        mPhone1.setPhoneType(phoneType);
        
        et.begin();
        em.persist(mPhone1);
        et.commit();
        
        et.begin();
        em.createQuery("DELETE FROM Employee").executeUpdate();
        et.commit();
        
        EmployeePojo emp1 = new EmployeePojo();
        emp1.setFirstName("George1");
        emp1.setLastName("Yang");
        EmployeePojo emp2 = new EmployeePojo();
        emp2.setFirstName("George2");
        et.begin();
        em.persist(emp1);
        em.persist(emp2);
        et.commit();
        
        List<PhonePojo> phones = new ArrayList<>();
        phones.add(mPhone1);
        emp1.setPhones(phones); // employee 1 has mobile phone 1
        et.begin();
        em.merge(emp1);
        et.commit();
        
        EmployeePojo owningEmployee = emp1;
        mPhone1.setOwningEmployee(owningEmployee);
        et.begin();
        em.merge(mPhone1);
        et.commit();
        
        TypedQuery<EmployeePojo> noPhoneEmpQuery = em.createQuery(
                "select e from Employee e where e.phones is empty",
                EmployeePojo.class);
        EmployeePojo noPhoneEmp = noPhoneEmpQuery.getSingleResult();
        assertNotNull(noPhoneEmp);
        assertThat(noPhoneEmp.getFirstName(), equalTo(emp2.getFirstName()));
        em.close();
    }

    @Test
    public void test48_query_employee_has_phone() {
        logger.info("check with phone employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<EmployeePojo> empQuery = em.createQuery(
                "select e from Employee e where e.phones is not empty",
                EmployeePojo.class);
        EmployeePojo emp = empQuery.getSingleResult();
        assertNotNull(emp);
        assertThat(emp.getFirstName(), equalTo("George1"));
        em.close();
    }
    
    @Test
    public void test49_criteria_emp_by_phone() {
        logger.info("check criteria query successfully get employees by phone area code");
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
        CriteriaQuery<EmployeePojo> query = queryBuilder.createQuery(EmployeePojo.class);
        Root<EmployeePojo> root = query.from(EmployeePojo.class);
        ListJoin<EmployeePojo, PhonePojo> phonesJoin = root.join(EmployeePojo_.phones);
        query.where(queryBuilder.equal(phonesJoin.get(PhonePojo_.areaCode), "613"));
        EmployeePojo employee613 = em.createQuery(query).getSingleResult();
        assertNotNull(employee613);
        assertThat(employee613.getFirstName(), startsWith("George1"));
        em.close();
    }

    @Test
    public void test50_criteria_emp_by_taskDesp() {
        logger.info("check criteria query successfully get employees by task description");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        String description1 = "task1";
        LocalDateTime startDate1 = LocalDateTime.of(2019, 9, 9, 9, 9);
        LocalDateTime endDate1 = LocalDateTime.of(2020, 1, 9, 9, 9);
        EmployeeTask task1 = new EmployeeTask();
        task1.setDescription(description1);
        task1.setDone(true);
        task1.setStartDate(startDate1);
        task1.setEndDate(endDate1);
        
        String description2 = "task2";
        LocalDateTime startDate2 = LocalDateTime.of(2020, 1, 9, 9, 9);
        LocalDateTime endDate2 = LocalDateTime.of(2020, 9, 9, 9, 9);
        EmployeeTask task2 = new EmployeeTask();
        task2.setDescription(description2);
        task2.setDone(false);
        task2.setStartDate(startDate2);
        task2.setEndDate(endDate2);

        List<EmployeeTask> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        
        String fName = "George24";
        EmployeePojo emp1 = new EmployeePojo();
        emp1.setFirstName(fName);
        emp1.setEmployeeTasks(tasks);
        
        et.begin();
        em.persist(emp1);
        et.commit();
        
        CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
        CriteriaQuery<EmployeePojo> query = queryBuilder.createQuery(EmployeePojo.class);
        Root<EmployeePojo> root = query.from(EmployeePojo.class);
        ListJoin<EmployeePojo, EmployeeTask> tasksJoin = root.join(EmployeePojo_.employeeTasks);
        query.where(queryBuilder.equal(tasksJoin.get(EmployeeTask_.description), description1));
        EmployeePojo employeeTask1 = em.createQuery(query).getSingleResult();
        assertNotNull(employeeTask1);
        em.close();
    }

    @Test
    public void test51_query_employee_has_done_tasks() {
        logger.info("check successfully queried out employee who has done tasks");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<EmployeePojo> empQuery = em.createQuery(
                "select e from Employee e join fetch e.employeeTasks tasks where tasks.done = :param1",
                EmployeePojo.class);
        empQuery.setParameter("param1", true);
        EmployeePojo emp = empQuery.getSingleResult();
        assertNotNull(emp);
        assertThat(emp.getFirstName(), equalTo("George24"));
        em.close();
    }

    @Test
    public void test52_query_tasks_by_employee() {
        logger.info("check successfully queried out employee's tasks");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<EmployeePojo> empQuery = em.createQuery(
                "select distinct e from Employee e join fetch e.employeeTasks tasks where e.firstName = :param1",
                EmployeePojo.class);
        empQuery.setParameter("param1", "George24");
        EmployeePojo emp = empQuery.getSingleResult();
        assertNotNull(emp);
        assertEquals(emp.getEmployeeTasks().size(), 2);
        em.close();
    }
    
}