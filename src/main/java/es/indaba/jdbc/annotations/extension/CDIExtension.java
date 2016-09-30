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
package es.indaba.jdbc.annotations.extension;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.indaba.jdbc.annotations.api.DatabaseCall;
import es.indaba.jdbc.annotations.impl.AnnotationInterfaceObjectFactory;

/**
 * JDBC CDI extension
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CDIExtension implements javax.enterprise.inject.spi.Extension {

	private static final Logger logger = LoggerFactory.getLogger(CDIExtension.class);

	private final Set<Bean> beans = new HashSet<Bean>();

	public <T> void processAnnotatedType(@WithAnnotations({ DatabaseCall.class }) @Observes ProcessAnnotatedType<T> pat, BeanManager bm) {
		Class type = pat.getAnnotatedType().getJavaClass();
		if (type.isInterface()) {
			logger.info("DBCallCDI Module - Type detected!! {}", pat.getAnnotatedType().getBaseType());

			AnnotationInterfaceObjectFactory factory = new AnnotationInterfaceObjectFactory();
			try {

				Class clazz = factory.buildClass(type);
				final BeanBuilder<Object> beanBuilder = new BeanBuilder<Object>(
						bm).passivationCapable(false).beanClass(clazz)
						.name(type.getName())
						.types(pat.getAnnotatedType().getBaseType())
						.beanLifecycle(new ContextualFactory<Object>(clazz));
				beans.add(beanBuilder.create());

			} catch (Exception e) {
				logger.error("Error processing DBCallCDI Annotations", e);
			}
		}
	}

	public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		logger.info("DBCallCDI Module - Activated");
		for (Bean bean : this.beans) {
			logger.info("DBCallCDI Module - DBCall discovered: {}", bean.getName());
			abd.addBean(bean);
		}
		this.beans.clear();
	}
}