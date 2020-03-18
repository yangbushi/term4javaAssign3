package com.algonquincollege.cst8277;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Assignment3Driver {

    public static final String ASSIGNMENT3_PU_NAME = "assignment3-employeeSystem-PU";

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(ASSIGNMENT3_PU_NAME);
        emf.close();
    }

}
