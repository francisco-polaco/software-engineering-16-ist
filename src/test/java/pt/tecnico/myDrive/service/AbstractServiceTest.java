package pt.tecnico.myDrive.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import pt.tecnico.myDrive.MyDriveApplication;


public abstract class AbstractServiceTest {
    protected static final Logger log = LogManager.getRootLogger();

    @BeforeClass // run once berfore each test class
    public static void setUpBeforeAll() throws Exception {
        // run tests with a clean database!!! Granted by dbclean plugin on mvn cycle phase
        MyDriveApplication.setup();
    }

    @Before // run before each test
    public void setUp() throws Exception {
        try {
            FenixFramework.getTransactionManager().begin(false);
            populate();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After // rollback after each test
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    protected abstract void populate(); // each test adds its own data
}