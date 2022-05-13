package org.hibernate.dialect;

import java.sql.Types;

import org.hibernate.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snowflake has a similar dialect to SQL Server, but we need to override certain portions to match
 * @author daniel.watson
 *
 */
public class SnowflakeDialect extends SQLServer2012Dialect {

	
	protected static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeDialect.class);
	
	public SnowflakeDialect() {
		super();
		registerColumnType( Types.TIMESTAMP, "datetime" );
	}


	@Override
	public String getSequenceNextValString(String sequenceName) {
		return "select " + getSelectSequenceNextValString( sequenceName ) + " from dual";
	}

	@Override
	public String getSelectSequenceNextValString(String sequenceName) {
		return sequenceName + ".nextval";
		
	}
	

	public String getCreateSequenceString(String sequenceName, int initialValue, int incrementSize) throws MappingException {
		return getCreateSequenceString( sequenceName ) + " start " + initialValue + " increment " + incrementSize;
	}
	


}
