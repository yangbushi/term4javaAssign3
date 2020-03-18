/***************************************************************************f******************u************zz*******y**
 * File: TestSuiteBase.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 *
 * @date 2020 02
 *
 * Extend this class when building their TestSuite class
 */
package com.algonquincollege.cst8277;

import static org.eclipse.persistence.config.PersistenceUnitProperties.CATEGORY_LOGGING_LEVEL_;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_DRIVER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_PASSWORD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_URL;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_USER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_LOGGER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_PARAMETERS;
import static org.eclipse.persistence.config.PersistenceUnitProperties.SCHEMA_GENERATION_CREATE_ACTION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.SCHEMA_GENERATION_DATABASE_ACTION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.SCHEMA_GENERATION_SQL_LOAD_SCRIPT_SOURCE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.TARGET_DATABASE;
import static org.eclipse.persistence.logging.SessionLog.CONNECTION;
import static org.eclipse.persistence.logging.SessionLog.FINER_LABEL;
import static org.eclipse.persistence.logging.SessionLog.FINEST_LABEL;
import static org.eclipse.persistence.logging.SessionLog.FINE_LABEL;
import static org.eclipse.persistence.logging.SessionLog.SQL;
import static org.eclipse.persistence.logging.SessionLog.TRANSACTION;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.internal.jpa.config.persistenceunit.PersistenceUnitImpl;
import org.eclipse.persistence.internal.jpa.deployment.PersistenceUnitProcessor;
import org.eclipse.persistence.internal.jpa.deployment.SEPersistenceUnitInfo;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.eclipse.persistence.logging.slf4j.SLF4JLogger;
import org.eclipse.persistence.platform.database.H2Platform;
import org.h2.Driver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.EmployeePojo;
import com.algonquincollege.cst8277.models.EmployeeTask;
import com.algonquincollege.cst8277.models.HomePhone;
import com.algonquincollege.cst8277.models.MobilePhone;
import com.algonquincollege.cst8277.models.PhonePojo;
import com.algonquincollege.cst8277.models.PojoBase;
import com.algonquincollege.cst8277.models.PojoListener;
import com.algonquincollege.cst8277.models.ProjectPojo;
import com.algonquincollege.cst8277.models.WorkPhone;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class TestSuiteBase {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);

    public static final String ASSIGNMENT3_PU_NAME = "assignment3-junit";
    public static final String ECLIPSELINK_LOGGING_SQL = "eclipselink.logging.sql";
    public static final String META_INF_SQL_PREFIX = "META-INF/sql/";
    public static final String META_INF_SQL_SUFFIX = ".sql";
    public static final String H2_DEFAULT_PASSWORD = "password";
    public static final String H2_DEFAULT_USER = "sa";
    public static final String H2_TEST_URL = "jdbc:h2:mem:assignment3-junit;DB_CLOSE_DELAY=-1";

    public static ListAppender<ILoggingEvent> attachListAppender(ch.qos.logback.classic.Logger theLogger, String listAppenderName) {
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.setName(listAppenderName);
        listAppender.start();
        theLogger.addAppender(listAppender);
        listAppender.setContext(theLogger.getLoggerContext());
        return listAppender;
    }

    public static void detachListAppender(ch.qos.logback.classic.Logger theLogger, ListAppender<ILoggingEvent> listAppender) {
        theLogger.detachAppender(listAppender);
    }

    // test fixture(s)
    public static EntityManagerFactory emf;

    @BeforeClass
    public static void setUp() throws Exception {
        emf = buildEntityManagerFactory();
    }

    @AfterClass
    public static void oneTimeTearDown() {
        logger.debug("oneTimeTearDown");
        if (emf != null) {
            emf.close();
        }
    }

    //rather complex way (!) of bootstrapping JPA without persistence.xml
    public static EntityManagerFactory buildEntityManagerFactory() {
        EntityManagerFactory emf = null;
        try {
            SEPersistenceUnitInfo puInfo = new SEPersistenceUnitInfo();
            puInfo.setPersistenceUnitName(ASSIGNMENT3_PU_NAME);
            puInfo.setClassLoader(Thread.currentThread().getContextClassLoader());
            String persistenceFactoryResource = PersistenceUnitImpl.class.getName().replace('.', '/') + ".class";
            URL puURL = PersistenceUnitImpl.class.getClassLoader().getResource(persistenceFactoryResource);
            puURL = PersistenceUnitProcessor.computePURootURL(puURL, persistenceFactoryResource);
            puInfo.setPersistenceUnitRootUrl(puURL);

            List<String> managedClassNames = new ArrayList<>();
            managedClassNames.add(PojoBase.class.getName());
            managedClassNames.add(PojoListener.class.getName());
            managedClassNames.add(AddressPojo.class.getName());
            managedClassNames.add(EmployeePojo.class.getName());
            managedClassNames.add(EmployeeTask.class.getName());
            managedClassNames.add(PhonePojo.class.getName());
            managedClassNames.add(HomePhone.class.getName());
            managedClassNames.add(WorkPhone.class.getName());
            managedClassNames.add(MobilePhone.class.getName());
            managedClassNames.add(ProjectPojo.class.getName());
            puInfo.setManagedClassNames(managedClassNames);

            Map<String, Object> properties = new HashMap<>();
            properties.put(SCHEMA_GENERATION_DATABASE_ACTION, SCHEMA_GENERATION_CREATE_ACTION);
            properties.put(SCHEMA_GENERATION_SQL_LOAD_SCRIPT_SOURCE, META_INF_SQL_PREFIX + "EmployeeSystemTestSuite" + META_INF_SQL_SUFFIX);
            properties.put(JDBC_DRIVER, Driver.class.getName());
            properties.put(JDBC_URL, H2_TEST_URL);
            properties.put(JDBC_USER, H2_DEFAULT_USER);
            properties.put(JDBC_PASSWORD, H2_DEFAULT_PASSWORD);
            properties.put(TARGET_DATABASE, H2Platform.class.getName());
            properties.put(LOGGING_LOGGER, SLF4JLogger.class.getName());
            // EclipseLink uses 'category' logging, not classname: need to know name of category
            //  https://www.eclipse.org/eclipselink/api/2.7/org/eclipse/persistence/logging/LogCategory.html
            properties.put(CATEGORY_LOGGING_LEVEL_ + TRANSACTION, FINER_LABEL);
            properties.put(CATEGORY_LOGGING_LEVEL_ + SQL, FINEST_LABEL);
            properties.put(CATEGORY_LOGGING_LEVEL_ + CONNECTION, FINE_LABEL);
            properties.put(LOGGING_PARAMETERS, Boolean.TRUE.toString());
            emf = new PersistenceProvider().createContainerEntityManagerFactory(puInfo, properties);
        }
        catch (Exception e) {
            logger.error("something went wrong configuring EntityManagerFactory without persistence.xml", e);
        }
        return emf;
    }
}