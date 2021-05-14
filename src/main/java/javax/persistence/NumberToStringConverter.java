package javax.persistence;

import java.math.BigDecimal;

public abstract class NumberToStringConverter<T extends Number> implements AttributeConverter<T, String> {

	@Override
	public String convertToDatabaseColumn(T attribute) {
		if(attribute == null) return null;
		else return attribute.toString();
	}

	@Override
	public T convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		return create(dbData);
	}
	
	protected abstract T create(String data);

}
