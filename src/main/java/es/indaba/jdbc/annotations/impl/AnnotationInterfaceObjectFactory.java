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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.AnnotationUtils;
import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;
import org.hibernate.Session;

import es.indaba.jdbc.annotations.api.DatabaseCall;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

@SuppressWarnings("rawtypes")
public class AnnotationInterfaceObjectFactory<T> {

    public Class buildClass(final Class<T> type) {

        final ProxyFactory factory = new ProxyFactory();
        factory.setInterfaces(new Class[] {type});
        factory.setHandler(new MethodHandler() {
            public Object invoke(final Object arg0, final Method method, final Method arg2, final Object[] parameters)
                    throws Throwable {
                final BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
                final Annotation[] annotations = method.getAnnotations();
                final DatabaseCall dbCall =
                        AnnotationUtils.findAnnotation(beanManager, annotations, DatabaseCall.class);
                if (dbCall == null) {
                    return null;
                }
                final GenericWork callWork = AnnotationProcessor.buildWork(method, parameters);
                if (callWork == null) {
                    return null;
                }
                final EntityManager manager = BeanProvider.getContextualReference(EntityManager.class, false,
                        AnnotationInstanceProvider.of(dbCall.qualifier()));
                final Session delegate = (Session) manager.getDelegate();
                delegate.doWork(callWork);
                if (callWork.getWorkException() != null) {
                    throw callWork.getWorkException();
                }
                return callWork.getResultObject();
            }
        });
        return factory.createClass();
    }

    @SuppressWarnings("unchecked")
    public T getInstance(final Class<T> c) throws InstantiationException, IllegalAccessException {
        final Class derived = buildClass(c);
        return (T) derived.newInstance();
    }
}
