/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details. You should have received a copy of the GNU Lesser General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 * 
 *******************************************************************************/
package es.indaba.jdbc.annotations.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.indaba.jdbc.annotations.api.FieldResult;
import es.indaba.jdbc.annotations.api.StoredProcedure;
import es.indaba.jdbc.annotations.api.StoredProcedureResult;

@SuppressWarnings("rawtypes")
public class GenericWork implements Work {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericWork.class);

    private StoredProcedure procedure;
    private StoredProcedureResult proceduresResult;
    private List<SQLParameter> parameters;
    private Class returnType;
    private Object resultObject;
    private Exception workException;

    public StoredProcedure getProcedure() {
        return procedure;
    }

    public void setProcedure(final StoredProcedure procedure) {
        this.procedure = procedure;
    }

    public StoredProcedureResult getProceduresResult() {
        return proceduresResult;
    }

    public void setProceduresResult(final StoredProcedureResult proceduresResult) {
        this.proceduresResult = proceduresResult;
    }

    public List<SQLParameter> getParameters() {
        return parameters;
    }

    public void setParameters(final List<SQLParameter> parameters) {
        this.parameters = parameters;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(final Class returnType) {
        this.returnType = returnType;
    }

    public Exception getWorkException() {
        return workException;
    }

    @Override
    public void execute(final Connection con) throws SQLException {
        final String procedureCall = procedure.value();
        final FieldResult[] fields = proceduresResult == null ? new FieldResult[0] : proceduresResult.value();

        CallableStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareCall(procedureCall);
            prepareInputParameters(st);
            prepareOutputParameters(fields, st);
            st.execute();

            if (!returnType.equals(void.class)) {
                // Return instance
                rs = collectResult(fields, st);
            }
        } catch (SQLException | ReflectiveOperationException e) {
            LOGGER.error("DBCallCDI - Error calling {}", procedureCall, e);
            workException = e;

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (st != null) {
                st.close();
            }
        }

    }

    @SuppressWarnings("unchecked")
    private ResultSet collectResult(final FieldResult[] fields, final CallableStatement st)
            throws SQLException, ReflectiveOperationException {
        ResultSet rs;
        resultObject = returnType.newInstance();
        rs = st.getResultSet();
        for (final FieldResult field : fields) {
            final String property = field.name();
            Object result = null;
            if (field.position() == FieldResult.RESULTSET) {
                rs.next();
                result = SQLTypeMapping.getSqlResultsetResult(rs, field.type(), field.sqlType(), 1);
            } else {
                result = SQLTypeMapping.getSqlResult(st, field.type(), field.sqlType(), field.position());
            }
            if (result != null) {
                PropertyUtils.setProperty(resultObject, property, result);
            }
        }
        return rs;
    }

    private void prepareOutputParameters(final FieldResult[] fields, final CallableStatement st) throws SQLException {
        for (final FieldResult field : fields) {
            final int position = field.position();
            Class type = field.sqlType();
            if (type == null || type.equals(Object.class)) {
                type = field.type();
            }
            final Integer jdbcType = SQLTypeMapping.getSqlTypeforClass(type);
            if (position != FieldResult.RESULTSET) {
                st.registerOutParameter(position, jdbcType);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void prepareInputParameters(final CallableStatement st) throws SQLException, ReflectiveOperationException {
        for (final SQLParameter p : parameters) {
            final int pos = p.getPosition();
            final Object val = p.getValue();
            final Class type = p.getType();
            final Class sqlType = p.getSqlType();
            final Integer jdbcType = SQLTypeMapping.getSqlTypeforClass(type);
            if (jdbcType != null) {
                if (val != null) {
                    SQLTypeMapping.setSqlParameter(st, type, sqlType, pos, val);
                } else {
                    st.setNull(pos, jdbcType);
                }
            }
        }
    }

    public Object getResultObject() {
        return resultObject;
    }

}
