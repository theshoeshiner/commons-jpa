package org.hibernate.util;

import org.apache.commons.lang.StringUtils;

public class ColumnNameUtils {

	public static final String join(String sep,String... strings) {
		return StringUtils.join(strings, sep);
	}
	
	public static final String joinUnderscore(String... strings) {
		return StringUtils.join(strings, "_");
	}
	
}
