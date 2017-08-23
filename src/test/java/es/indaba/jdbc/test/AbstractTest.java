/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details. You should have received a copy of the GNU Lesser General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 * 
 *******************************************************************************/
package es.indaba.jdbc.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import es.indaba.jdbc.test.cdi.SecondEM;

public abstract class AbstractTest {

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
        clearDatabases();
        contextControl.stopContexts();
        cdiContainer.shutdown();
    }



    private static void clearDatabases() {
        EntityManager defaultEm = BeanProvider.getContextualReference(EntityManager.class, false);
        clearDatabase(defaultEm);
        EntityManager secondEm = BeanProvider.getContextualReference(EntityManager.class, false,
                AnnotationInstanceProvider.of(SecondEM.class));
        clearDatabase(secondEm);
    }

    private static void clearDatabase(EntityManager em) {
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
}
