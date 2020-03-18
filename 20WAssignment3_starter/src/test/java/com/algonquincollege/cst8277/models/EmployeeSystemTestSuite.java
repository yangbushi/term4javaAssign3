/***************************************************************************f******************u************zz*******y**
 * File: EmployeeTestSuite.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 *
 *
 */
package com.algonquincollege.cst8277.models;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringStartsWith.startsWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algonquincollege.cst8277.TestSuiteBase;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

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
    public void test03_update_one_employee() {
        logger.info("check 1 employee successfully updated");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        EmployeePojo emp = em.find(EmployeePojo.class, 1);
        String fName = "George_test03";
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
    public void test04_delete_one_employee() {
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
    public void test05_query_expensive_employees() {
        logger.info("check certain type of employees successfully queried out");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        
        EmployeePojo emp1 = new EmployeePojo();
        String fName = "George1";
        emp1.setFirstName(fName);
        emp1.setSalary(999999.99);
        EmployeePojo emp2 = new EmployeePojo();
        emp2.setFirstName("George2");
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
    public void test06_count_employees() {
        logger.info("check the count of employees is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(e) from Employee e", Long.class);
        Long numEmployees = countQuery.getSingleResult();
        assertEquals(new Long(2), numEmployees);
    }

    @Test
    public void test07_query_employee_has_no_project() {
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
    public void test08_criteria_last_name() {
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
    public void test09_insert_one_address() {
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
    public void test10_update_one_address() {
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
    public void test11_delete_one_address() {
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
    public void test12_query_canada_address() {
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
    public void test13_count_addresses() {
        logger.info("check the count of addresses is correct");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Long> countQuery = em.createQuery(
                "select count(a) from Address a", Long.class);
        Long numAddresses = countQuery.getSingleResult();
        assertEquals(new Long(1), numAddresses);
    }

    @Test
    public void test14_query_employee_has_no_project() {
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
    public void test15_criteria_address_county() {
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

    @Ignore
    public void test16_something() {

    }

    @Ignore
    public void test17_something() {

    }

    @Ignore
    public void test18_something() {

    }

    @Ignore
    public void test19_something() {

    }

    @Ignore
    public void test20_something() {

    }

    @Ignore
    public void test21_something() {

    }

    @Ignore
    public void test22_something() {

    }

    @Ignore
    public void test23_something() {

    }

    @Ignore
    public void test24_something() {

    }

    @Ignore
    public void test25_something() {

    }

    @Ignore
    public void test26_something() {

    }

    @Ignore
    public void test27_something() {

    }

    @Ignore
    public void test28_something() {

    }

    @Ignore
    public void test29_something() {

    }

    @Ignore
    public void test30_something() {

    }

    @Ignore
    public void test31_something() {

    }

    @Ignore
    public void test32_something() {

    }

    @Ignore
    public void test33_something() {

    }

    @Ignore
    public void test34_something() {

    }

    @Ignore
    public void test35_something() {

    }

    @Ignore
    public void test36_something() {

    }

    @Ignore
    public void test37_something() {

    }

    @Ignore
    public void test38_something() {

    }

    @Ignore
    public void test39_something() {

    }

    @Ignore
    public void test40_something() {

    }

    @Ignore
    public void test41_something() {

    }

    @Ignore
    public void test42_something() {

    }

    @Ignore
    public void test43_something() {

    }

    @Ignore
    public void test44_something() {

    }

    @Ignore
    public void test45_something() {

    }

    @Ignore
    public void test46_something() {

    }

    @Ignore
    public void test47_something() {

    }

    @Ignore
    public void test48_something() {

    }

    @Ignore
    public void test49_something() {

    }

    @Ignore
    public void test50_something() {

    }
}