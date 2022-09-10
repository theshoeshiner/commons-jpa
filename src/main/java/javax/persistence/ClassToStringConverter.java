package javax.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts a java class reference to a string
 * Be careful because if the class is not present in the class loader then 
 * this will be forced to return  {@link ClassToStringConverter.nullClass ClassToStringConverter.nullClass}
 * which defaults to null
 */
public class ClassToStringConverter implements AttributeConverter<Class<?>, String> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(ClassToStringConverter.class);
	
	protected Class<?> nullClass = null;
	
	@Override
	public String convertToDatabaseColumn(Class<?> attribute) {
		if(attribute == null)return null;
		else return attribute.getCanonicalName();
	}

	@Override
	public Class<?> convertToEntityAttribute(String dbData) {
		if(dbData == null)return null;
		try {
			return Class.forName(dbData);
		}
		catch (ClassNotFoundException e) {
			LOGGER.error("Could not convert {}",dbData,e);
			return nullClass;
		}
	}

}