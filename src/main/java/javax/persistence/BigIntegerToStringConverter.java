package javax.persistence;

import java.math.BigInteger;

public class BigIntegerToStringConverter extends NumberToStringConverter<BigInteger> {

	@Override
	protected BigInteger create(String data) {
		return new BigInteger(data);
	}

}
