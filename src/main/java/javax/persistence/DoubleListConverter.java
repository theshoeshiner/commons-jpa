package javax.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleListConverter extends ListConverter<Double>{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(DoubleListConverter.class);

	public DoubleListConverter() {
		super(Double.class);
	}

	@Override
	public Double convertToEntityObject(String val) {
		return Double.parseDouble(val);
	}

}
