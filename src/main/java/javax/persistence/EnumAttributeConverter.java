package javax.persistence;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Converter that maps enums to custom strings in a databas (as opposed to using their Java name)
 * Convenient when your database model prefers a specific string format that doesnt fit with your java enum names 
 * By default creates snake_case strings, but can override @javax.persistence.EnumAttributeConverter.convertEnum(T) to generate any string.
 * @author theshoeshiner
 *
 * @param <T>
 */
public abstract class EnumAttributeConverter<T extends Enum<T>> implements AttributeConverter<T, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnumAttributeConverter.class);
	
	protected BiMap<T,String> map;
	
	public EnumAttributeConverter(Class<T> concrete) {
		map = HashBiMap.create();
		T[] values = concrete.getEnumConstants();
		for(T t : values) {
			String s = convertEnum(t);
			LOGGER.info("Enum value: {} database value: {}",t,s);
			map.put(t, s);
		}
	}
	
	public EnumAttributeConverter(Class<T> concrete,Function<T,String> convertFunction) {
		map = HashBiMap.create();
		T[] values = concrete.getEnumConstants();
		for(T t : values) {
			String s = convertFunction.apply(t);
			LOGGER.info("Enum value: {} database value: {}",t,s);
			map.put(t, s);
		}
	}
	
	@Override
	public String convertToDatabaseColumn(T attribute) {
		if(attribute == null) return null;
		else return map.get(attribute);
	}

	@Override
	public T convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		else return map.inverse().get(dbData);
	}
	

	public String convertEnum(T e) {
		return e.name();
	}
	

}
