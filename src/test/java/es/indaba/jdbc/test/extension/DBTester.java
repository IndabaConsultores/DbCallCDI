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
package es.indaba.jdbc.test.extension;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import es.indaba.jdbc.annotations.api.DatabaseCall;
import es.indaba.jdbc.annotations.api.FieldResult;
import es.indaba.jdbc.annotations.api.StoredProcedure;
import es.indaba.jdbc.annotations.api.StoredProcedureParameter;
import es.indaba.jdbc.annotations.api.StoredProcedureResult;
import es.indaba.jdbc.test.cdi.SecondEM;
import es.indaba.jdbc.test.result.ProcedureResult;

@ApplicationScoped
public interface DBTester {

	@StoredProcedure("CALL echoEmpty()")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET) })
	@DatabaseCall
	public ProcedureResult<String> callEchoEmptyAsFunction() throws Exception;
	
	@StoredProcedure("CALL echoEmptyProc()")
	@DatabaseCall
	public void callEchoEmptyAsProcedure() throws Exception;
	
	@StoredProcedure("CALL echo(?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET) })
	@DatabaseCall
	public ProcedureResult<String> callEchoAsFunction(@StoredProcedureParameter(1) String name) throws Exception;
	
	@StoredProcedure("CALL echoProc(?,?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = 2) })
	@DatabaseCall
	public ProcedureResult<String> callEchoAsProcedure(@StoredProcedureParameter(1) String name) throws Exception;

	@StoredProcedure("CALL echoNumber(?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET, type=Long.class) })
	@DatabaseCall
	public ProcedureResult<Long> callEchoNumberAsFunction(@StoredProcedureParameter(1) Long name) throws Exception;
	
	@StoredProcedure("CALL echoNumber(?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET, type=Long.class) })
	@DatabaseCall
	public ProcedureResult<Long> callEchoPrimitiveNumberAsFunction(@StoredProcedureParameter(1) long name) throws Exception;
	
	@StoredProcedure("CALL echoNumber(?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET, type=Double.class) })
	@DatabaseCall
	public ProcedureResult<Double> callEchoNumberAsFunction(@StoredProcedureParameter(1) Double name) throws Exception;
	
	@StoredProcedure("CALL echoNumberProc(?,?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = 2, type=Long.class)})
	@DatabaseCall
	public ProcedureResult<Long> callEchoNumberAsProcedure(@StoredProcedureParameter(1) Long name) throws Exception;
	
	@StoredProcedure("CALL echoNumberProc(?,?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = 2, type=Long.class)})
	@DatabaseCall
	public ProcedureResult<Long> callEchoNumberAsProcedure(@StoredProcedureParameter(1) long name) throws Exception;


	@StoredProcedure("CALL echoNumberProc(?,?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = 2, type=Double.class)})
	@DatabaseCall
	public ProcedureResult<Double> callEchoNumberAsProcedure(@StoredProcedureParameter(1) Double name) throws Exception;

	
	@StoredProcedure("CALL echoDate(?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET, type=Date.class) })
	@DatabaseCall
	public ProcedureResult<Date> callEchoDateAsFunction(@StoredProcedureParameter(1) Date name) throws Exception;
	
	@StoredProcedure("CALL echoDateProc(?,?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = 2, type=Date.class)})
	@DatabaseCall
	public ProcedureResult<Date> callEchoDateAsProcedure(@StoredProcedureParameter(1) Date name) throws Exception;
	
	@StoredProcedure("CALL echoTime(?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET, type=Date.class, sqlType=java.sql.Time.class) })
	@DatabaseCall
	public ProcedureResult<Date> callEchoTimeAsFunction(@StoredProcedureParameter(value=1, sqlType=java.sql.Time.class) Date name) throws Exception;
	
	@StoredProcedure("CALL echoTimeProc(?,?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = 2, type=Date.class, sqlType=java.sql.Time.class)})
	@DatabaseCall
	public ProcedureResult<Date> callEchoTimeAsProcedure(@StoredProcedureParameter(value=1, sqlType=java.sql.Time.class) Date name) throws Exception;

	@StoredProcedure("CALL echoTimestamp(?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET, type=Date.class, sqlType=java.sql.Timestamp.class) })
	@DatabaseCall
	public ProcedureResult<Date> callEchoTimestampAsFunction(@StoredProcedureParameter(value=1, sqlType=java.sql.Timestamp.class) Date name) throws Exception;
	
	@StoredProcedure("CALL echoTimestampProc(?,?)")
	@StoredProcedureResult({ @FieldResult(name = "value", position = 2, type=Date.class, sqlType=java.sql.Timestamp.class)})
	@DatabaseCall
	public ProcedureResult<Date> callEchoTimestampAsProcedure(@StoredProcedureParameter(value=1, sqlType=java.sql.Timestamp.class) Date name) throws Exception;

	@StoredProcedure("CALL echoEM()")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET) })
	@DatabaseCall
	public ProcedureResult<String> callEchoEM() throws Exception;
	
	@StoredProcedure("CALL echoEM()")
	@StoredProcedureResult({ @FieldResult(name = "value", position = FieldResult.RESULTSET) })
	@DatabaseCall(qualifier=SecondEM.class)
	public ProcedureResult<String> callEchoEMSecond() throws Exception;
}