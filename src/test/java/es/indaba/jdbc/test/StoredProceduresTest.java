/*******************************************************************************
 *  This program is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any
 *  later version. This program is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 *  Public License for more details. You should have received a copy of the GNU
 *  Lesser General Public License along with this program. If not, see
 *  <http://www.gnu.org/licenses/>
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

import es.indaba.jdbc.test.extension.DBTester;
import es.indaba.jdbc.test.result.ProcedureResult;

public class StoredProceduresTest extends AbstractTest {

	@Test
	public void emptyTest() throws Exception {
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class,	false);
		ProcedureResult<String> result = dbTester.callEchoEmptyAsFunction();
		assertNotNull(result);
		assertNull(result.getValue());

		dbTester.callEchoEmptyAsProcedure();
		assertTrue(true);
	}

	@Test
	public void stringTest() throws Exception {
		String testVal = "testVal";
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<String> result = dbTester.callEchoAsFunction(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue());

		result = dbTester.callEchoAsProcedure(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue());
	}

	@Test
	public void longTest() throws Exception {
		Long testVal = 1l;
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<Long> result = dbTester.callEchoPrimitiveNumberAsFunction(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue());

		result = dbTester.callEchoNumberAsProcedure(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue());
	}
	
	@Test
	public void primitiveLongTest() throws Exception {
		long testVal = 1l;
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<Long> result = dbTester.callEchoNumberAsFunction(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue().longValue());

		result = dbTester.callEchoNumberAsProcedure(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue().longValue(), result.getValue());
	}
	
//	@Test
//	public void primitiveLongNullTest() throws Exception {
//		Long testVal = null;
//		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
//		ProcedureResult<Long> result = dbTester.callEchoPrimitiveNumberAsFunction(testVal);
//		assertNotNull(result);
//
////		result = dbTester.callEchoNumberAsProcedure(testVal);
////		assertNotNull(result);
////		assertEquals(testVal, result.getValue().longValue(), result.getValue());
//	}

	@Test
	public void doubleTest() throws Exception {
		Double testVal = 1.2d;
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<Double> result = dbTester.callEchoNumberAsFunction(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue());

		result = dbTester.callEchoNumberAsProcedure(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue());
	}
	
	@Test
	public void dateTest() throws Exception {
		Date testVal = new Date();
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<Date> result = dbTester.callEchoDateAsFunction(testVal);
		assertNotNull(result);
		assertEquals(DateUtils.truncate(testVal, Calendar.DAY_OF_MONTH), DateUtils.truncate(result.getValue(), Calendar.DAY_OF_MONTH));

		result = dbTester.callEchoDateAsProcedure(testVal);
		assertNotNull(result);
		assertEquals(DateUtils.truncate(testVal, Calendar.DAY_OF_MONTH), DateUtils.truncate(result.getValue(), Calendar.DAY_OF_MONTH));
	}

	@Test
	public void timeTest() throws Exception {
		Calendar testCal = Calendar.getInstance();
		Date testVal = testCal.getTime();
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<Date> result = dbTester.callEchoTimeAsFunction(testVal);
		
		Calendar resCal = Calendar.getInstance();
		resCal.setTime(result.getValue());

		assertNotNull(result);
		assertEquals(testCal.get(Calendar.HOUR), resCal.get(Calendar.HOUR));
		assertEquals(testCal.get(Calendar.MINUTE), resCal.get(Calendar.MINUTE));
		assertEquals(testCal.get(Calendar.SECOND), resCal.get(Calendar.SECOND));

		result = dbTester.callEchoTimeAsProcedure(testVal);
		resCal.setTime(result.getValue());
		
		assertNotNull(result);
		assertEquals(testCal.get(Calendar.HOUR), resCal.get(Calendar.HOUR));
		assertEquals(testCal.get(Calendar.MINUTE), resCal.get(Calendar.MINUTE));
		assertEquals(testCal.get(Calendar.SECOND), resCal.get(Calendar.SECOND));
	}

	@Test
	public void timeStampTest() throws Exception {
		Date testVal = new Date();
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<Date> result = dbTester.callEchoTimestampAsFunction(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue());

		result = dbTester.callEchoTimestampAsProcedure(testVal);
		assertNotNull(result);
		assertEquals(testVal, result.getValue());
	}
	
	@Test
	public void echoEMTest() throws Exception {
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<String> result = dbTester.callEchoEM();
		assertNotNull(result);
		assertEquals("default", result.getValue());
	}
	
	@Test
	public void echoEMTestSecond() throws Exception {
		DBTester dbTester = BeanProvider.getContextualReference(DBTester.class, false);
		ProcedureResult<String> result = dbTester.callEchoEMSecond();
		assertNotNull(result);
		assertEquals("second", result.getValue());
	}
}