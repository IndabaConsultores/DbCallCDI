package es.indaba.jdbc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import es.indaba.jdbc.test.extension.PostgreDBTester;
import es.indaba.jdbc.test.result.ProcedureResult;

@Ignore("Depends on ElephandSQL Service")
public class PostgreSQLTest {

    private static final Logger logger = Logger.getLogger(AbstractTest.class.getName());

    private static CdiContainer cdiContainer;
    private static ContextControl contextControl;

    @BeforeClass
    public static void setUp() {
        cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();

        contextControl = cdiContainer.getContextControl();
        contextControl.startContext(ApplicationScoped.class);
    }

    @AfterClass
    public static void tearDown() {
        // clearDatabase();
        contextControl.stopContexts();
        cdiContainer.shutdown();
    }


    @SuppressWarnings("unused")
    private static void clearDatabase() {
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class, false);
        Query q = em.createNativeQuery("DROP SCHEMA PUBLIC CASCADE");
        try {
            em.getTransaction().begin();
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }


    @Test
    public void stringTest() throws Exception {
        String testVal = "testVal";
        PostgreDBTester dbTester = BeanProvider.getContextualReference(PostgreDBTester.class, false);
        ProcedureResult<String> result = dbTester.callEchoAsFunction(testVal);
        assertNotNull(result);
        assertEquals(testVal, result.getValue());

        result = dbTester.callEchoAsProcedure(testVal);
        assertNotNull(result);
        assertEquals(testVal, result.getValue());

    }


}
