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
package es.indaba.jdbc.annotations.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.indaba.jdbc.annotations.api.FieldResult;
import es.indaba.jdbc.annotations.api.StoredProcedure;
import es.indaba.jdbc.annotations.api.StoredProcedureResult;

@SuppressWarnings("rawtypes")
public class GenericWork implements Work {
	
	private static final Logger logger = LoggerFactory.getLogger(GenericWork.class);

	StoredProcedure procedure;
	StoredProcedureResult proceduresResult;
	List<SQLParameter> parameters;
	Class returnType;
	Object resultObject;
	Exception workException;
	

	public StoredProcedure getProcedure() {
		return procedure;
	}

	public void setProcedure(StoredProcedure procedure) {
		this.procedure = procedure;
	}

	public StoredProcedureResult getProceduresResult() {
		return proceduresResult;
	}

	public void setProceduresResult(StoredProcedureResult proceduresResult) {
		this.proceduresResult = proceduresResult;
	}

	public List<SQLParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<SQLParameter> parameters) {
		this.parameters = parameters;
	}

	public Class getReturnType() {
		return returnType;
	}

	public void setReturnType(Class returnType) {
		this.returnType = returnType;
	}
	
	public Exception getWorkException() {
		return workException;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Connection con) throws SQLException {
		String procedureCall = procedure.value();
		FieldResult[] fields = proceduresResult == null ? new FieldResult[0] : proceduresResult.value();
		
		CallableStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareCall(procedureCall);
			for (SQLParameter p : parameters) {
				int pos = p.getPosition();
				Object val = p.getValue();
				Class type = p.getType();
				Class sqlType = p.getSqlType();
				Integer jdbcType = SQLTypeMapping.getSqlTypeforClass(type);
				if (jdbcType != null) {
					if (val != null) {
						SQLTypeMapping.setSqlParameter(st, type, sqlType, pos, val);
					} else {
						st.setNull(pos, jdbcType);
					}
				}
			}
			for (FieldResult field : fields) {
				int position = field.position();
				Class type = field.sqlType();
				if (type == null || type.equals(Object.class)) {
					type = field.type();
				}
				Integer jdbcType = SQLTypeMapping.getSqlTypeforClass(type);
				if (position != FieldResult.RESULTSET) {
					st.registerOutParameter(position, jdbcType);
				}
			}
			st.execute();

			if (!returnType.equals(void.class)) {
				// Return instance
				resultObject = returnType.newInstance();
				rs = st.getResultSet();
				for (FieldResult field : fields) {
					String property = field.name();
					Object result = null;
					if (field.position() == FieldResult.RESULTSET) {
						rs.next();
						result = SQLTypeMapping.getSqlResultsetResult(rs, field.type(), field.sqlType(), 1);
					} else {
						result = SQLTypeMapping.getSqlResult(st, field.type(), field.sqlType(), field.position());
					}
					BeanUtils.setProperty(resultObject, property, result);
				}
			}
		} catch (Exception e) {
			
			logger.error("DBCallCDI - Error calling {}",procedureCall, e);
			workException = e;
			
		} finally {
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		}
		
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}
}