package javax.persistence;

import org.thshsh.text.cases.Case;

public class CaseEnumAttributeConverter<T extends Enum<T>> extends EnumAttributeConverter<T> {

	protected Case from;
	protected Case to;
	
	public CaseEnumAttributeConverter(Class<T> concrete,Case from, Case to) {
		super(concrete);
	}

	@Override
	public String convertEnum(T e) {
		return to.format(from.parse(e.name()));
	}

}
