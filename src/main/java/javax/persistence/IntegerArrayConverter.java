package javax.persistence;

public class IntegerArrayConverter extends ListConverter<Integer> {

	public IntegerArrayConverter() {
		super(Integer.class);
	}

	@Override
	public Integer convertToEntityObject(String val) {
		return Integer.parseInt(val);
	}

}
