package javax.persistence;

import org.thshsh.text.cases.PascalCase;
import org.thshsh.text.cases.SnakeCase;

public class PascalToSnakeCaseEnumAttributeConverter<T extends Enum<T>> extends CaseEnumAttributeConverter<T>{

	public PascalToSnakeCaseEnumAttributeConverter(Class<T> concrete) {
		super(concrete, PascalCase.INSTANCE, SnakeCase.INSTANCE	);
	}

}
