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
		LOGGER.debug("determineIndexName: {}",name);
		return Identifier.toIdentifier(name);
	}

	@Override
	public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
		String name = "uk_"+source.getTableName().getText()+"_"+String.join("_", source.getColumnNames().stream().map(Identifier::getText).collect(Collectors.toList()));
		LOGGER.debug("determineUniqueKeyName: {}",name);
		return Identifier.toIdentifier(name);
	}

	
	
	@Override
	public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
		String columns = String.join("_", source.getColumnNames().stream().map(Identifier::getText).collect(Collectors.toList()));
		String name = "fk_"+source.getTableName().getText()+"_"+source.getReferencedTableName().getText()+"_"+columns;
		LOGGER.debug("determineForeignKeyName: {}",name);
		return Identifier.toIdentifier(name);   
	}
	
	
	
	
	
	
}
