package org.thshsh.hibernate.boot;

import java.util.stream.Collectors;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.boot.model.naming.ImplicitUniqueKeyNameSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ImplicitNamingStrategy extends ImplicitNamingStrategyComponentPathImpl {

	
	public static final Logger LOGGER = LoggerFactory.getLogger(ImplicitNamingStrategy.class);
	
	public ImplicitNamingStrategy() {
		super();
	}
	
	 
	@Override
	public Identifier determineIndexName(ImplicitIndexNameSource source) {
		String name = source.getTableName().getText()+"_"+String.join("_", source.getColumnNames().stream().map(Identifier::getText).collect(Collectors.toList()));
		return Identifier.toIdentifier(name);
	}

	@Override
	public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
		String name = "unique_"+String.join("_", source.getColumnNames().stream().map(Identifier::getText).collect(Collectors.toList()));
		return Identifier.toIdentifier(name);
	}

	
	
	@Override
	public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
		if(source.getColumnNames().size()>1) {
			throw new IllegalArgumentException("Cannot handle mulitple column keys");
		} 
		String name = source.getTableName().getText()+"_"+source.getReferencedTableName().getText();
		LOGGER.debug("determineForeignKeyName: {} + {} = {}",new Object[] {source.getTableName().getText(),source.getReferencedTableName().getText(),name});
		return Identifier.toIdentifier(name); 
	}
	
	
	
	
	
	
}
