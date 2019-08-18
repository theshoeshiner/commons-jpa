package javax.persistence;

@Converter
public class StringArrayConverter extends ListConverter<String> {

	public StringArrayConverter() {
		super(String.class);
	}

	@Override
	public String convertToEntityObject(String val) {
		return val;
	}

	

}
