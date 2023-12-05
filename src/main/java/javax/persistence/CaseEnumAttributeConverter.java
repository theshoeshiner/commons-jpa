package javax.persistence;

import org.thshsh.text.cases.Case;

public class CaseEnumAttributeConverter<T extends Enum<T>> extends EnumAttributeConverter<T> {

	public CaseEnumAttributeConverter(Class<T> concrete,Case enumCase, Case databaseCase) {
		super(concrete,t -> {
			return databaseCase.format(enumCase.parse(t.name()));
		});
	}

}
