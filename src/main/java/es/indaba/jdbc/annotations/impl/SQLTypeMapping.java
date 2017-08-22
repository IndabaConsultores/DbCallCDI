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

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class SQLTypeMapping {

    private static final Logger LOGGER = Logger.getLogger(SQLTypeMapping.class.getName());

    /* The mapping from Java to SQL */
    private static final Map<Class, Integer> java2SQL = new HashMap<>();
    /* The mapping from type codes (as defined in java.sql.Types) to SQL */
    private static final Map<Integer, Class> sql2Java = new HashMap<>();
    private static final Map<Class, Method> java2Setter = new HashMap<>();
    private static final Map<Class, Method> java2Getter = new HashMap<>();

    static {
        java2SQL.put(Boolean.class, Types.BOOLEAN);
        java2SQL.put(boolean.class, Types.BOOLEAN);
        java2SQL.put(String.class, Types.VARCHAR);
        java2SQL.put(java.util.Date.class, Types.DATE);
        java2SQL.put(java.sql.Date.class, Types.DATE);
        java2SQL.put(java.sql.Timestamp.class, Types.TIMESTAMP);
        java2SQL.put(java.sql.Time.class, Types.TIME);
        java2SQL.put(int.class, Types.INTEGER);
        java2SQL.put(Integer.class, Types.INTEGER);
        java2SQL.put(long.class, Types.BIGINT);
        java2SQL.put(Long.class, Types.BIGINT);
        java2SQL.put(float.class, Types.FLOAT);
        java2SQL.put(Float.class, Types.FLOAT);
        java2SQL.put(double.class, Types.DOUBLE);
        java2SQL.put(Double.class, Types.DOUBLE);
        java2SQL.put(Character.class, Types.CHAR);
        java2SQL.put(char.class, Types.CHAR);
        java2SQL.put(short.class, Types.SMALLINT);
        java2SQL.put(Short.class, Types.SMALLINT);
        java2SQL.put(byte.class, Types.SMALLINT);
        java2SQL.put(Byte.class, Types.SMALLINT);

        // Populate type mapping
        sql2Java.put(Types.VARCHAR, String.class);
        sql2Java.put(Types.TIMESTAMP, java.util.Date.class);
        sql2Java.put(Types.DATE, java.util.Date.class);
        sql2Java.put(Types.TIME, java.util.Date.class);
        sql2Java.put(Types.INTEGER, Integer.class);
        sql2Java.put(Types.DOUBLE, Double.class);
        sql2Java.put(Types.FLOAT, Float.class);
        sql2Java.put(Types.BOOLEAN, Boolean.class);
        sql2Java.put(Types.CHAR, Character.class);
        sql2Java.put(Types.BIGINT, Long.class);

        try {

            final Class c = CallableStatement.class;
            Method setter = c.getMethod("setString", int.class, String.class);
            java2Setter.put(String.class, setter);
            java2Setter.put(Character.class, setter);
            java2Setter.put(char.class, setter);
            setter = c.getMethod("setBoolean", int.class, boolean.class);
            java2Setter.put(Boolean.class, setter);
            java2Setter.put(boolean.class, setter);
            setter = c.getMethod("setDate", int.class, java.sql.Date.class);
            java2Setter.put(java.util.Date.class, setter);
            java2Setter.put(java.sql.Date.class, setter);
            setter = c.getMethod("setTimestamp", int.class, java.sql.Timestamp.class);
            java2Setter.put(java.util.Calendar.class, setter);
            setter = c.getMethod("setInt", int.class, int.class);
            java2Setter.put(Integer.class, setter);
            java2Setter.put(int.class, setter);
            setter = c.getMethod("setLong", int.class, long.class);
            java2Setter.put(Long.class, setter);
            java2Setter.put(long.class, setter);
            setter = c.getMethod("setFloat", int.class, float.class);
            java2Setter.put(Float.class, setter);
            java2Setter.put(float.class, setter);
            setter = c.getMethod("setDouble", int.class, double.class);
            java2Setter.put(Double.class, setter);
            java2Setter.put(double.class, setter);
            setter = c.getMethod("setShort", int.class, short.class);
            java2Setter.put(Short.class, setter);
            java2Setter.put(short.class, setter);
            java2Setter.put(Byte.class, setter);
            java2Setter.put(byte.class, setter);

            Method getter = c.getMethod("getString", int.class);
            java2Getter.put(String.class, getter);
            java2Getter.put(Character.class, getter);
            java2Getter.put(char.class, getter);
            getter = c.getMethod("getBoolean", int.class);
            java2Getter.put(Boolean.class, getter);
            java2Getter.put(boolean.class, getter);
            getter = c.getMethod("getDate", int.class);
            java2Getter.put(java.util.Date.class, getter);
            java2Getter.put(java.sql.Date.class, getter);
            getter = c.getMethod("getTimestamp", int.class);
            java2Getter.put(java.util.Calendar.class, getter);
            getter = c.getMethod("getInt", int.class);
            java2Getter.put(Integer.class, getter);
            java2Getter.put(int.class, getter);
            getter = c.getMethod("getLong", int.class);
            java2Getter.put(Long.class, getter);
            java2Getter.put(long.class, getter);
            getter = c.getMethod("getFloat", int.class);
            java2Getter.put(Float.class, getter);
            java2Getter.put(float.class, getter);
            getter = c.getMethod("getDouble", int.class);
            java2Getter.put(Double.class, getter);
            java2Getter.put(double.class, getter);
            getter = c.getMethod("getShort", int.class);
            java2Getter.put(Short.class, getter);
            java2Getter.put(short.class, getter);
            java2Getter.put(Byte.class, getter);
            java2Getter.put(byte.class, getter);

        } catch (final ReflectiveOperationException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private SQLTypeMapping() {

    }

    public static Integer getSqlTypeforClass(final Class c) {
        return java2SQL.get(c);
    }

    public static <T> void setSqlParameter(final CallableStatement cs, final Class<T> javaType,
            final Class<? extends java.util.Date> sqlType, final int position, final T value)
            throws SQLException, ReflectiveOperationException {
        if (sqlType == null || sqlType.equals(Object.class)) {
            setSqlParameter(cs, javaType, position, value);
        } else {
            java.util.Date convertedValue = null;
            if (value instanceof java.util.Date) {
                if (sqlType.equals(java.sql.Date.class)) {
                    convertedValue = new java.sql.Date(((java.util.Date) value).getTime());
                } else if (sqlType.equals(java.sql.Timestamp.class)) {
                    convertedValue = new java.sql.Timestamp(((java.util.Date) value).getTime());
                } else if (sqlType.equals(java.sql.Time.class)) {
                    convertedValue = new java.sql.Time(((java.util.Date) value).getTime());
                }
            }
            cs.setObject(position, convertedValue, java2SQL.get(sqlType));
        }
    }

    public static <T> void setSqlParameter(final CallableStatement cs, final Class<T> javaType, final int position,
            final T value) throws ReflectiveOperationException {
        Object convertedValue = null;
        if (value instanceof java.util.Date) {
            convertedValue = new java.sql.Date(((java.util.Date) value).getTime());
        }
        final Method setter = java2Setter.get(javaType);
        setter.invoke(cs, position, convertedValue == null ? value : convertedValue);
    }

    public static <T> T getSqlResult(final CallableStatement cs, final Class<T> javaType,
            final Class<? extends java.util.Date> sqlType, final int position)
            throws SQLException, ReflectiveOperationException {
        if (sqlType == null || sqlType.equals(Object.class)) {
            return getSqlResult(cs, javaType, position);
        } else {
            return (T) cs.getObject(position, sqlType);
        }
    }

    public static <T> T getSqlResult(final CallableStatement cs, final Class<T> javaType, final int position)
            throws SQLException, ReflectiveOperationException {
        final Method getter = java2Getter.get(javaType);
        Object result = getter.invoke(cs, position);
        result = cs.wasNull() ? null : result;
        if (result instanceof java.sql.Date) {
            result = new java.util.Date(((java.sql.Date) result).getTime());
        }
        return (T) result;
    }

    public static <T> T getSqlResultsetResult(final ResultSet rs, final Class<T> javaType,
            final Class<? extends java.util.Date> sqlType, final int position)
            throws SQLException, ReflectiveOperationException {
        if (sqlType == null || sqlType.equals(Object.class)) {
            return getSqlResultsetResult(rs, javaType, position);
        } else {
            return (T) rs.getObject(position, sqlType);
        }
    }

    public static <T> T getSqlResultsetResult(final ResultSet rs, final Class<T> javaType, final int position)
            throws SQLException, ReflectiveOperationException {
        Method getter = java2Getter.get(javaType);
        final String methodName = getter.getName();
        final Class rsClass = ResultSet.class;
        getter = rsClass.getMethod(methodName, int.class);
        Object result = getter.invoke(rs, position);
        result = rs.wasNull() ? null : result;
        if (result instanceof java.sql.Date) {
            result = new java.util.Date(((java.sql.Date) result).getTime());
        }
        return (T) result;
    }
}
