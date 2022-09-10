package javax.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public abstract class ListConverter<T> implements AttributeConverter<List<T>, String> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ListConverter.class);

	protected String separator = ",";
	protected Class<T> entityClass;
	
	public ListConverter(Class<T> classs) {
		this.entityClass = classs;
	}

	@Override
	public String convertToDatabaseColumn(List<T> array) {
		LOGGER.info("convertToDatabaseColumn: {}",array);
		if(array == null) return null;
		String s = StringUtils.join(array, separator);
		return s;
	}
	

	@Override
	public List<T> convertToEntityAttribute(String arg0) {
		LOGGER.info("convertToEntityAttribute: {}",arg0);
		if(arg0 == null) return new ArrayList<T>();
		String[] array = StringUtils.split(arg0,separator);
		List<T> list = new ArrayList<>();
		for(int i=0;i<array.length;i++) {
			if(StringUtils.isBlank(array[i])) {
				//dont add null values
			}
			else list.add(convertToEntityObject(array[i]));
		}
		return list;
	}

	public abstract T convertToEntityObject(String val);
	
}
