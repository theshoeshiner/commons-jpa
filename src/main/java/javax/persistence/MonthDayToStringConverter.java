package javax.persistence;

import java.time.MonthDay;

/**
 * Converts a java MonthDay object to a string.
 * useful when you dont want to store an entire database timestamp object just to hold a month/day value
 *
 */
public class MonthDayToStringConverter implements AttributeConverter<MonthDay, String> {

	protected String separator = "-";
	
	@Override
	public String convertToDatabaseColumn(MonthDay attribute) {
		if(attribute == null) return null;
		return attribute.getMonthValue()+separator+attribute.getDayOfMonth();
	}

	@Override
	public MonthDay convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		String[] d =dbData.split(separator);
		return MonthDay.of(Integer.valueOf(d[0]), Integer.valueOf(d[1]));
		
	}

}
