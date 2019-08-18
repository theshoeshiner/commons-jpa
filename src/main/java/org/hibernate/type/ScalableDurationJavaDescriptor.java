package org.hibernate.type;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScalableDurationJavaDescriptor extends AbstractTypeDescriptor<Duration> {
	
	private static final long serialVersionUID = 7104088013451001627L;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(ScalableDurationJavaDescriptor.class);

	public static final ScalableDurationJavaDescriptor INSTANCE = new ScalableDurationJavaDescriptor();
	
	protected ChronoUnit unit = ChronoUnit.NANOS;

	@SuppressWarnings("unchecked")
	public ScalableDurationJavaDescriptor() {
		super( Duration.class, ImmutableMutabilityPlan.INSTANCE );
	}
	
	@SuppressWarnings("unchecked")
	public ScalableDurationJavaDescriptor(ChronoUnit unit) {
		super( Duration.class, ImmutableMutabilityPlan.INSTANCE );
		this.unit = unit;
	}

	@Override
	public String toString(Duration value) {
		if ( value == null ) {
			return null;
		}
		return String.valueOf(convert(value, unit) );
	}

	@Override
	public Duration fromString(String string) {
		if ( string == null ) {
			return null;
		}
		return convert( Long.valueOf( string ) ,unit);
	}
	
	public Duration convert(Long duration) {
		return convert(duration, unit);
	}
	
	public Long convert(Duration duration) {
		return convert(duration, unit);
	}
	
	public static Duration convert(Long d,ChronoUnit unit) {
		Long duration = d;
		switch(unit) {
		case MILLIS: //TODO
			break;
		case MINUTES: //TODO
			break;
		case NANOS: //TODO
			break;
		case SECONDS:
			duration = duration * 1000000000l;
			break;
		default:
			throw new IllegalStateException();
		}
		Duration dur = Duration.ofNanos( (Long) duration );
		LOGGER.trace("num to duration {} = {}",d,dur);
		return dur;
	}
	
	public static Long convert(Duration duration,ChronoUnit unit) {
		long nanos = duration.toNanos();
		switch(unit) {
			case MILLIS:
				nanos = nanos / (1000000l);
				break;
			case MINUTES:
				nanos = nanos / (6000000000l);
				break;
			case NANOS:
				break;
			case SECONDS:
				nanos = nanos / (1000000000l);
				break;
			default:
				throw new IllegalStateException();
		}
		LOGGER.trace("duration to num {} = {}",duration,nanos);
		return nanos;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <X> X unwrap(Duration duration, Class<X> type, WrapperOptions options) {
		if ( duration == null ) {
			return null;
		}

		if ( Duration.class.isAssignableFrom( type ) ) {
			return (X) duration;
		}

		if ( Long.class.isAssignableFrom( type ) ) {
			return (X) convert(duration,unit);
		}

		throw unknownUnwrap( type );
	}

	@Override
	public <X> Duration wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}

		if ( Duration.class.isInstance( value ) ) {
			return (Duration) value;
		}

		if ( Long.class.isInstance( value ) ) {
			return convert( (Long) value ,unit);
		}

		throw unknownWrap( value.getClass() );
	}

	public ChronoUnit getUnit() {
		return unit;
	}

	public void setUnit(ChronoUnit unit) {
		LOGGER.debug("set unit {}",unit);
		this.unit = unit;
	}
	
	
	
}
