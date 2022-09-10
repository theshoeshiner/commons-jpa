package org.hibernate.blob;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlobStreamType implements UserType  {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(BlobStreamType.class);
	
	public Class<?> returnedClass() {
		return BlobStream.class;
	}

	public int[] sqlTypes() {
		return new int[] { Types.BLOB };
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == null)
			if (y == null)
				return true;
			else
				return false;
		return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		LOGGER.info("disassemble");
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		LOGGER.info("assemble");
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		LOGGER.info("replace");
		return original;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)throws HibernateException, SQLException {
		Object result = null;
		int columnIndex = rs.findColumn(names[0]);
		LOGGER.debug("nullSafeGet: {}",new Object[]{columnIndex});
		//NOTE using getBlob works with database OID types but does not work with bytearray types, need to create a new type for that
		Blob b = rs.getBlob(columnIndex);
		if(b!=null) result = new BlobStream(b.getBinaryStream());
		return result;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
		LOGGER.debug("nullSafeSet: {}",new Object[]{index});
		InputStream stream = null;
		if (value != null) {
			BlobStream blob = (BlobStream) value;
			LOGGER.debug("BlobStream: {}",blob);
			stream = blob.getStream();
			LOGGER.debug("stream: {}",stream);
		} 
		st.setBlob(index, stream);
		LOGGER.debug("nullSafeSet done");
		
	}


}
