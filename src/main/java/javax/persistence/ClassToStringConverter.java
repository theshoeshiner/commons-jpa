package javax.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassToStringConverter implements AttributeConverter<Class<?>, String> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(ClassToStringConverter.class);
	
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
			return null;
		}
	}

}