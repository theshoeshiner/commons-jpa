package javax.persistence;

import java.math.BigDecimal;

public class BigDecimalToStringConverter extends NumberToStringConverter<BigDecimal> {

	@Override
	protected BigDecimal create(String data) {
		return new BigDecimal(data);
	}


}
