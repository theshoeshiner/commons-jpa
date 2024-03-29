package org.thshsh.hibernate.boot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thshsh.text.cases.CamelCase;
import org.thshsh.text.cases.SnakeCase;

@SuppressWarnings("serial")
public class SnakeCaseImplicitNamingStrategy extends ImplicitNamingStrategy {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SnakeCaseImplicitNamingStrategy.class);

	public SnakeCaseImplicitNamingStrategy() {
		super(s -> {
			if(!StringUtils.lowerCase(s).equals(s)) {
				//if its not all lowercase then assume its camel case
				String snake = SnakeCase.INSTANCE.format(CamelCase.INSTANCE.parse(s)).toLowerCase();
				LOGGER.debug("converting: {} to snake case: {}",s,snake);
				return snake;
			}
			else {
				//already snake case
				return s;
			}
		});		
	}

}
