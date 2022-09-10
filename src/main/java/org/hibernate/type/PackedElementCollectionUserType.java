package org.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackedElementCollectionUserType implements UserType,ParameterizedType {

	protected static final Logger LOGGER = LoggerFactory.getLogger( ScalabeDurationUserType.class );
	
	public static final String TYPE = "org.hibernate.type.PackedElementCollectionUserType";
	
	public static final PackedElementCollectionUserType INSTANCE = new PackedElementCollectionUserType();
	
	

    protected PackedElementCollectionJavaDescriptor descriptor = new PackedElementCollectionJavaDescriptor();
    
    
    public PackedElementCollectionUserType() {
    	super();
    }
    
    @Override
    public int[] sqlTypes() {
        return new int[] {Types.VARCHAR};
    }

    @Override
    public Class<?> returnedClass() {
        return List.class;
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
		//descriptor.setUnit(unit);
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		 String columnName = names[0];
	        String columnValue =rs.getString( columnName );
	        return columnValue == null ? null :descriptor.convert( columnValue );
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
	     if ( value == null ) {
	            st.setNull( index, Types.BIGINT );
	        }
	        else {
	            String longVal = descriptor.convert( (List) value );
	            st.setString(index, longVal);
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