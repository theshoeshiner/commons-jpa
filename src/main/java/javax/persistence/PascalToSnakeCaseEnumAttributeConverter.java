package javax.persistence;

import org.thshsh.text.cases.DelimitedCase;
import org.thshsh.text.cases.PascalCase;

public class PascalToSnakeCaseEnumAttributeConverter<T extends Enum<T>> extends CaseEnumAttributeConverter<T>{

	public PascalToSnakeCaseEnumAttributeConverter(Class<T> concrete) {
		super(concrete, PascalCase.INSTANCE, new DelimitedCase('_') {

			@Override
			public String format(Iterable<String> tokens) {
				String s = super.format(tokens);
				return s.toLowerCase();
			}
			
		});
	}

}
