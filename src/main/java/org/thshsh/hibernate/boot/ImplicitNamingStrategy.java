package org.thshsh.hibernate.boot;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitIdentifierColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.boot.model.naming.ImplicitUniqueKeyNameSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ImplicitNamingStrategy extends ImplicitNamingStrategyComponentPathImpl {

	
	public static final Logger LOGGER = LoggerFactory.getLogger(ImplicitNamingStrategy.class);
	
	protected Function<? super String,? extends String> nameFunction;

	public ImplicitNamingStrategy(Function<? super String,? extends String> f) {
		super();
		this.nameFunction = f;
	}
	
	@Override
	public Identifier determineIndexName(ImplicitIndexNameSource source) {
		if(source.getUserProvidedIdentifier()!=null) return source.getUserProvidedIdentifier();
		String name = "ix_"+source.getTableName().getText()+"_"+String.join("_", 
				source
				.getColumnNames()
				.stream()
				.map(Identifier::getText)
				.map(nameFunction)
				.collect(Collectors.toList()));
		LOGGER.debug("determineIndexName: {}",name);
		return Identifier.toIdentifier(name);
	}

	@Override
	public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
		if(source.getUserProvidedIdentifier()!=null) return source.getUserProvidedIdentifier();
		String name = "uk_"+source.getTableName().getText()+"_"+String.join("_", 
				source
				.getColumnNames()
				.stream()
				.map(Identifier::getText)
				.map(nameFunction)
				.collect(Collectors.toList()));
		LOGGER.debug("determineUniqueKeyName: {}",name);
		return Identifier.toIdentifier(name);  
	}

	
	
	@Override
	public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
		if(source.getUserProvidedIdentifier()!=null) return source.getUserProvidedIdentifier();
		String columns = String.join("_", 
				source
				.getColumnNames()
				.stream()
				.map(Identifier::getText)
				.map(nameFunction)
				.collect(Collectors.toList()));
		String name = "fk_"+source.getTableName().getText()+"_"+source.getReferencedTableName().getText()+"_"+columns;
		LOGGER.debug("determineForeignKeyName: {}",name);
		return Identifier.toIdentifier(name);   
	}

	@Override
	public Identifier determineIdentifierColumnName(ImplicitIdentifierColumnNameSource source) {
		Identifier i = super.determineIdentifierColumnName(source);
		LOGGER.debug("determineIdentifierColumnName: {}",i);
		return i;
	}
	
	
	
	
	
	
}
