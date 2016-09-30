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

import javax.enterprise.inject.spi.BeanManager;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.AnnotationUtils;
import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;
import org.hibernate.Session;

import es.indaba.jdbc.annotations.api.DatabaseCall;

@DatabaseCall
@Interceptor
public class StoredProcedureInterceptor {

	@AroundInvoke
	public Object callProcedure(InvocationContext invocationContext)
			throws Exception {

		BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
		Annotation[] annotations = invocationContext.getMethod().getAnnotations();
		DatabaseCall dbCall = AnnotationUtils.findAnnotation(beanManager,annotations, DatabaseCall.class);
		if (dbCall == null) return null;
		
		GenericWork callWork = AnnotationProcessor.buildWork(invocationContext.getMethod(), invocationContext.getParameters());

		// Get EM based on provided qualifier
		EntityManager manager = BeanProvider.getContextualReference(EntityManager.class,false,AnnotationInstanceProvider.of(dbCall.qualifier()));
		Session delegate = (Session) manager.getDelegate();

		delegate.doWork(callWork);
		
		if (callWork.getWorkException()!=null) {
			throw callWork.getWorkException();
		}
		
		invocationContext.proceed();

		return callWork.getResultObject();
	}
}