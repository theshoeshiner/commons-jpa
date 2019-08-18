package org.hibernate.type;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ScalableDurationType extends AbstractSingleColumnStandardBasicType<Duration> implements DiscriminatorType<Duration> {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ScalableDurationType.class);
	
	public static final ScalableDurationType INSTANCE = new ScalableDurationType();

	public ChronoUnit unit;
	
	public ScalableDurationType() {
		super(BigIntTypeDescriptor.INSTANCE, new ScalableDurationJavaDescriptor());
		LOGGER.debug("ScalableDurationType()");
	}
	
	
	@Override
	public Duration stringToObject(String xml) throws Exception {
		return fromString(xml);
	}

	@Override
	public String objectToSQLString(Duration value, Dialect dialect) throws Exception {
		return toString(value);
	}

	@Override
	public String getName() {
		return "scalable-duration";
	}

	

	

}