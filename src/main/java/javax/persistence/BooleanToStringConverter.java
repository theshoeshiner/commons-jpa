package javax.persistence;

public abstract class BooleanToStringConverter implements AttributeConverter<Boolean, String> {
	
	String affirmativeString;
	String negativeString;
	boolean silent = true;
	
	public BooleanToStringConverter() {}
	
	public BooleanToStringConverter(String affirmativeString, String negativeString) {
		super();
		this.affirmativeString = affirmativeString;
		this.negativeString = negativeString;
	}


	public String getAffirmativeString() {
		return affirmativeString;
	}

	public void setAffirmativeString(String affirmativeString) {
		this.affirmativeString = affirmativeString;
	}

	public String getNegativeString() {
		return negativeString;
	}

	public void setNegativeString(String negativeString) {
		this.negativeString = negativeString;
	}

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		if(attribute==null) return null;
		else if(attribute) return affirmativeString;
		else return negativeString;
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		if(dbData == null) return null;
		else if(dbData.equals(affirmativeString))return true;
		else if(dbData.equals(negativeString)) return false;
		else {
			if(silent)return null;
			else throw new IllegalArgumentException("String is not valid: "+dbData);
		}
	}

}
