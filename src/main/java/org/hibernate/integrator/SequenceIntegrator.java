package org.hibernate.integrator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.SequenceGenerator;

import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.tuple.ValueGenerator;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allows calling a sequence to populate a non ID column
 * Must be configured on the column via:
 * 
 * @GeneratorType(type = SequenceIntegrator.class, when = GenerationTime.INSERT)
 * @SequenceGenerator(name = "internal_id",schema = "box",sequenceName = "event_seq",allocationSize = 50,initialValue = 1)
 * @ColumnSequence(name="internal_id")
 * 
 * This FQ class name must also be added to: /META-INF/services/org.hibernate.integrator.spi.Integrator
 * 
 * Only one of these can exist on any given entity. The only way around that limitation is to extend this class and add 
 * the children just like this case class
 */
public class SequenceIntegrator implements Integrator, ValueGenerator<Long> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SequenceIntegrator.class);

	private SessionFactoryServiceRegistry serviceRegistry;
	private Metadata metadata;
	
	//each class can only have one generator per class because hibernate will only call this value generator once per class
	///would need to extend this class if we needed multiple
	Map<Class<?>, SequenceStyleGenerator> generators = new HashMap<>();

	@Override
	public void integrate(Metadata md, 
			SessionFactoryImplementor sessionFactoryImplementor,
			SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
		metadata = md;
		serviceRegistry = sessionFactoryServiceRegistry;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Long generateValue(Session session, Object o) {

		Class<?> entityClass = o.getClass();

		if (!generators.containsKey(entityClass)) {

			SequenceStyleGenerator generator = null;
			
			for (Field field : entityClass.getDeclaredFields()) {

				if (field.isAnnotationPresent(ColumnSequence.class)) {

					LOGGER.info("found sequence on field: {} / {}", field, field.getName());

					SequenceGenerator seqProps = field.getAnnotation(SequenceGenerator.class);

					ClassMetadata classMetadata = session.getSessionFactory().getClassMetadata(entityClass);
					Type type = classMetadata.getPropertyType(field.getName());

					Properties properties = new Properties();
					properties.setProperty(SequenceStyleGenerator.SEQUENCE_PARAM, seqProps.sequenceName());
					properties.setProperty(SequenceStyleGenerator.INCREMENT_PARAM, ""+seqProps.allocationSize());

					generator = new SequenceStyleGenerator();
					generator.configure(type, properties, serviceRegistry);
					generator.registerExportables(metadata.getDatabase());

				}

			}
			
			generators.put(entityClass, generator);

		}

		SequenceStyleGenerator generator = generators.get(entityClass);
		if(generator != null) {
			return (Long) generator.generate((SharedSessionContractImplementor) session, o);
		}
		else return null;
		

	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor,SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
	}

	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ColumnSequence {
		String name();
	}

}