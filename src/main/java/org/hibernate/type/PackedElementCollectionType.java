package org.hibernate.type;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("serial")
public class PackedElementCollectionType<T> extends AbstractSingleColumnStandardBasicType<List<T>> implements DiscriminatorType<List<T>> {

	protected static final Logger LOGGER = LoggerFactory.getLogger(PackedElementCollectionType.class);
	
	public static final PackedElementCollectionType INSTANCE = new PackedElementCollectionType();

	public ChronoUnit unit;
	
	public PackedElementCollectionType() {
		super(VarcharTypeDescriptor.INSTANCE, new PackedElementCollectionJavaDescriptor());
		LOGGER.debug("ScalableDurationType()");
	}
	
	
	@Override
	public List<T> stringToObject(String xml) throws Exception {
		return fromString(xml);
	}

	@Override
	public String objectToSQLString(List<T> value, Dialect dialect) throws Exception {
		return toString(value);
	}

	@Override
	public String getName() {
		return "element-collection";
	}

	

	

}