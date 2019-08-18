package org.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScalabeDurationUserType implements UserType,ParameterizedType {

	protected static final Logger LOGGER = LoggerFactory.getLogger( ScalabeDurationUserType.class );
	
	public static final String TYPE = "org.hibernate.type.ScalabeDurationUserType";
	
	public static final ScalabeDurationUserType INSTANCE = new ScalabeDurationUserType();
	
	

    protected ScalableDurationJavaDescriptor descriptor = new ScalableDurationJavaDescriptor();
    
    
    public ScalabeDurationUserType() {
    	super();
    	LOGGER.debug("ScalabeDurationUserType()");
    }
    
    @Override
    public int[] sqlTypes() {
        return new int[] {Types.BIGINT};
    }

    @Override
    public Class<?> returnedClass() {
        return Duration.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals( x, y );
    }

    @Override
    public int hashCode(Object x)
			throws HibernateException {
        return Objects.hashCode( x );
    }

    
    @Override
	public void setParameterValues(Properties parameters) {
		LOGGER.debug("setParameterValues {}",parameters);
		ChronoUnit unit = ChronoUnit.valueOf(parameters.get("unit").toString());
		descriptor.setUnit(unit);
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		 String columnName = names[0];
	        Long columnValue =rs.getLong( columnName );
	        return columnValue == null ? null :descriptor.convert( columnValue );
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
	     if ( value == null ) {
	            st.setNull( index, Types.BIGINT );
	        }
	        else {
	            Long longVal = descriptor.convert( (Duration) value );
	            st.setLong(index, longVal);
	        }
		
	}


	@Override
    public Object deepCopy(Object value) throws HibernateException {
        return value == null ? null : Duration.ofNanos( ((Duration)value).toNanos());
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value)throws HibernateException {
        return (Duration) deepCopy( value );
    }

    @Override
    public Object assemble(Serializable cached, Object owner)throws HibernateException {
        return deepCopy( cached );
    }

    @Override
    public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
        return deepCopy( original );
    }
}