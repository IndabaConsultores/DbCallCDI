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

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextualFactory<T> implements ContextualLifecycle<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextualFactory.class);
	
	@SuppressWarnings("rawtypes")
	private Class derivedClass;

	@SuppressWarnings("rawtypes")
	public ContextualFactory(Class t) {
		this.derivedClass = t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T create(final Bean<T> bean, final CreationalContext<T> creationalContext) {
		Object object = null;
		try {
			object = derivedClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.warn("Error instantiatin class {}",derivedClass,e);
		}
		return (T) object;
	}

	@Override
	public void destroy(final Bean<T> bean, final T instance, final CreationalContext<T> creationalContext) {
	}
}