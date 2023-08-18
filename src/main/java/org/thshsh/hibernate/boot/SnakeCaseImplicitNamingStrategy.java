package org.thshsh.hibernate.boot;

import java.util.List;
import java.util.function.Function;

import org.thshsh.text.cases.CamelCase;
import org.thshsh.text.cases.SnakeCase;

@SuppressWarnings("serial")
public class SnakeCaseImplicitNamingStrategy extends ImplicitNamingStrategy {

	public SnakeCaseImplicitNamingStrategy() {
		super(((Function<String,List<String>>)CamelCase.INSTANCE::parse).andThen(SnakeCase.INSTANCE::format));		
	}

}
