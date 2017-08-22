/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details. You should have received a copy of the GNU Lesser General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>
 * 
 *******************************************************************************/
package es.indaba.jdbc.annotations.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldResult {

    int RESULTSET = 0;

    /**
     * @return Name of the property of the returning type.
     */
    String name();

    @SuppressWarnings("rawtypes")
    /**
     * @return Type of the returning property
     */
    Class type() default String.class;

    /**
     * Position of the parameter in the call string. Position 0 refers to the value obtained form resultset after
     * procedure invocation. Any position 0 will be registered as an OUT parameter
     * 
     * @return the position
     */
    int position();

    /**
     * @return JDBC type of the parameter
     */
    @SuppressWarnings("rawtypes")
    Class sqlType() default Object.class;
}
