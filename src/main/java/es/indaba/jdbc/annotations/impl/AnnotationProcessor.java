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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.deltaspike.core.util.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.indaba.jdbc.annotations.api.StoredProcedure;
import es.indaba.jdbc.annotations.api.StoredProcedureParameter;
import es.indaba.jdbc.annotations.api.StoredProcedureResult;

public class AnnotationProcessor {

	private static final Logger logger = LoggerFactory.getLogger(AnnotationProcessor.class);

	@SuppressWarnings("rawtypes")
	public static GenericWork buildWork(Method method, Object[] parameters) throws Exception {

		logger.debug("DBCallCDI - Building GenericWork for method: {}  in class {}", method.getName(),
				method.getDeclaringClass().getName());

		BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
		Annotation[] annotations = method.getAnnotations();
		StoredProcedure sProc = AnnotationUtils.findAnnotation(beanManager, annotations, StoredProcedure.class);
		StoredProcedureResult sProcResult = AnnotationUtils.findAnnotation(beanManager, annotations,
				StoredProcedureResult.class);
		if (sProc == null) {
			logger.warn("DBCallCDI - StoredProcedure is not present in {}", method.getDeclaringClass().getName());
			return null;
		}
		logger.debug("DBCallCDI - Preparing call for procedure {} ", sProc.value());

		LinkedList<SQLParameter> params = new LinkedList<SQLParameter>();

		int idx = 0;
		for (Parameter param : method.getParameters()) {
			StoredProcedureParameter p = AnnotationUtils.findAnnotation(beanManager, param.getAnnotations(),
					StoredProcedureParameter.class);
			Class type = param.getType();

			SQLParameter sqlParam = new SQLParameter();
			sqlParam.setType(type);
			sqlParam.setSqlType(p.sqlType());
			sqlParam.setPosition(p.value());
			sqlParam.setValue(parameters[idx]);
			params.add(sqlParam);

			logger.debug("DBCallCDI {} - #{}  type: {} - sql-type:{} value:{}", sProc.value(), sqlParam.getPosition(),
					sqlParam.getType(), !sqlParam.getSqlType().equals(Object.class)?sqlParam.getSqlType():"<null>", sqlParam.getValue());

			idx++;
		}

		Class returnClass = method.getReturnType();
		
        logger.debug("DBCallCDI {} - Return Type {}",sProc.value(),returnClass);
        
		GenericWork work = new GenericWork();
		work.setProcedure(sProc);
		work.setProceduresResult(sProcResult);
		work.setParameters(params);
		work.setReturnType(returnClass);

		return work;
	}
}