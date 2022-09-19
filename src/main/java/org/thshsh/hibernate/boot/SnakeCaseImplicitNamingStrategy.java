package org.thshsh.hibernate.boot;

import org.thshsh.text.CaseUtils;

@SuppressWarnings("serial")
public class SnakeCaseImplicitNamingStrategy extends ImplicitNamingStrategy {

	public SnakeCaseImplicitNamingStrategy() {
		super(CaseUtils::toSnakeCase);
	}

}
