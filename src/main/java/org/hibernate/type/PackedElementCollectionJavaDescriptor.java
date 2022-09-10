package org.hibernate.type;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class PackedElementCollectionJavaDescriptor<T> extends AbstractTypeDescriptor<List> {

	private static final long serialVersionUID = 7104088013451001627L;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(PackedElementCollectionJavaDescriptor.class);

	public static final PackedElementCollectionJavaDescriptor INSTANCE = new PackedElementCollectionJavaDescriptor();
	
	//protected ChronoUnit unit = ChronoUnit.NANOS;

	@SuppressWarnings("unchecked")
	public PackedElementCollectionJavaDescriptor() {
		super( List.class, ImmutableMutabilityPlan.INSTANCE );
	}
	
	/*@SuppressWarnings("unchecked")
	public PackedElementCollectionJavaDescriptor(ChronoUnit unit) {
		super( List.class, ImmutableMutabilityPlan.INSTANCE );
		this.unit = unit;
	}
	*/
	@Override
	public String toString(List value) {
		if ( value == null ) {
			return null;
		}
		return convert(value) ;
	}

	@Override
	public List fromString(String string) {
		if ( string == null ) {
			return null;
		}
		return convert(string );
	}
	
	public List<T> convert(String duration) {
		return convert(duration);
	}
	
	public String convert(List<T> duration) {
		return convert(duration);
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
	public <X> X unwrap(List duration, Class<X> type, WrapperOptions options) {
		if ( duration == null ) {
			return null;
		}

		if ( List.class.isAssignableFrom( type ) ) {
			return (X) duration;
		}

		if ( String.class.isAssignableFrom( type ) ) {
			return (X) convert(duration);
		}

		throw unknownUnwrap( type );
	}

	@Override
	public <X> List wrap(X value, WrapperOptions options) {
		if ( value == null ) {
			return null;
		}

		if ( List.class.isInstance( value ) ) {
			return (List) value;
		}

		if ( String.class.isInstance( value ) ) {
			return convert( (String) value );
		}

		throw unknownWrap( value.getClass() );
	}

	/*public ChronoUnit getUnit() {
		return unit;
	}
	
	public void setUnit(ChronoUnit unit) {
		LOGGER.debug("set unit {}",unit);
		this.unit = unit;
	}*/
	
}
