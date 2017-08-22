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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.junit.Test;

import es.indaba.jdbc.test.interceptor.TestBean;
import es.indaba.jdbc.test.result.ProcedureResult;

public class InterceptorTest extends AbstractTest {

    @Test
    public void testEmpty() throws Exception {
        TestBean testService = BeanProvider.getContextualReference(TestBean.class, false);
        ProcedureResult<String> result = testService.callEchoEmptyAsFunction();
        assertNotNull(result);
        assertNull(result.getValue());

        testService.callEchoEmptyAsProcedure();
        assertTrue(true);
    }

    @Test
    public void testString() throws Exception {
        String testVal = "testVal";
        TestBean testService = BeanProvider.getContextualReference(TestBean.class, false);
        ProcedureResult<String> result = testService.callEchoAsFunction(testVal);
        assertNotNull(result);
        assertEquals(testVal, result.getValue());

        result = testService.callEchoAsProcedure(testVal);
        assertNotNull(result);
        assertEquals(testVal, result.getValue());
    }

    @Test
    public void testLong() throws Exception {
        Long testVal = 1l;
        TestBean testService = BeanProvider.getContextualReference(TestBean.class, false);
        ProcedureResult<Long> result = testService.callEchoNumberAsFunction(testVal);
        assertNotNull(result);
        assertEquals(testVal, result.getValue());

        result = testService.callEchoNumberAsProcedure(testVal);
        assertNotNull(result);
        assertEquals(testVal, result.getValue());
    }

    @Test
    public void testDate() throws Exception {
        Date testVal = new Date();
        TestBean testService = BeanProvider.getContextualReference(TestBean.class, false);
        ProcedureResult<Date> result = testService.callEchoDateAsFunction(testVal);
        assertNotNull(result);
        assertEquals(DateUtils.truncate(testVal, Calendar.DAY_OF_MONTH),
                DateUtils.truncate(result.getValue(), Calendar.DAY_OF_MONTH));

        result = testService.callEchoDateAsProcedure(testVal);
        assertNotNull(result);
        assertEquals(DateUtils.truncate(testVal, Calendar.DAY_OF_MONTH),
                DateUtils.truncate(result.getValue(), Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testTime() throws Exception {
        Calendar testCal = Calendar.getInstance();
        Date testVal = testCal.getTime();
        TestBean testService = BeanProvider.getContextualReference(TestBean.class, false);
        ProcedureResult<Date> result = testService.callEchoTimeAsFunction(testVal);

        Calendar resCal = Calendar.getInstance();
        resCal.setTime(result.getValue());

        assertNotNull(result);
        assertEquals(testCal.get(Calendar.HOUR), resCal.get(Calendar.HOUR));
        assertEquals(testCal.get(Calendar.MINUTE), resCal.get(Calendar.MINUTE));
        assertEquals(testCal.get(Calendar.SECOND), resCal.get(Calendar.SECOND));

        result = testService.callEchoTimeAsProcedure(testVal);
        resCal.setTime(result.getValue());

        assertNotNull(result);
        assertEquals(testCal.get(Calendar.HOUR), resCal.get(Calendar.HOUR));
        assertEquals(testCal.get(Calendar.MINUTE), resCal.get(Calendar.MINUTE));
        assertEquals(testCal.get(Calendar.SECOND), resCal.get(Calendar.SECOND));
    }

    @Test
    public void testTimestamp() throws Exception {
        Date testVal = new Date();
        TestBean testService = BeanProvider.getContextualReference(TestBean.class, false);
        ProcedureResult<Date> result = testService.callEchoTimestampAsFunction(testVal);
        assertNotNull(result);
        assertEquals(testVal, result.getValue());

        result = testService.callEchoTimestampAsProcedure(testVal);
        assertNotNull(result);
        assertEquals(testVal, result.getValue());
    }

    @Test
    public void testEchoEM() throws Exception {
        TestBean testService = BeanProvider.getContextualReference(TestBean.class, false);
        ProcedureResult<String> result = testService.callEchoEM();
        assertNotNull(result);
        assertEquals("default", result.getValue());
    }

    @Test
    public void testEchoEMSecond() throws Exception {
        TestBean testService = BeanProvider.getContextualReference(TestBean.class, false);
        ProcedureResult<String> result = testService.callEchoEMSecond();
        assertNotNull(result);
        assertEquals("second", result.getValue());
    }
}
