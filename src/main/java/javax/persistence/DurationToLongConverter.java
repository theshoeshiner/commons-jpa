package javax.persistence;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationToLongConverter implements AttributeConverter<Duration, Long>{

	@Override
	public Long convertToDatabaseColumn(Duration attribute) {
		if(attribute == null) return null;
		else return attribute.toMillis();
	}

	@Override
	public Duration convertToEntityAttribute(Long dbData) {
		if(dbData == null) return null;
		else return Duration.of(dbData, ChronoUnit.MILLIS);
	}

}
