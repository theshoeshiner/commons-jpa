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
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		int columnIndex = rs.findColumn(names[0]);
		LOGGER.debug("nullSafeGet: {}",new Object[]{columnIndex});
		InputStream is = rs.getBinaryStream(columnIndex);
		BlobStream bs = new BlobStream(is, -1);
		return bs;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if (value != null) {
			BlobStream blob = (BlobStream) value;
			if (blob.getStream() != null) {
				InputStream stream = blob.getStream();
				Long length = blob.getLength();
				LOGGER.debug("Setting stream: {} of length: {}",new Object[]{stream,length});
				st.setBinaryStream(index, stream, length.intValue()); //TODO handle unknown lengths
			}
			else {
				st.setBlob(index, (Blob) null);
			}
		} 
		else {
			st.setBlob(index, (Blob) null);
		}
		
	}


}
