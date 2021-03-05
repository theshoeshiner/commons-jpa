package javax.persistence;

public class BooleanToYNConverter extends BooleanToStringConverter {
	
	public BooleanToYNConverter() {
		super("Y","N");
	}

}
