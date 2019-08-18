package org.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

public class FlagUserType implements UserType, ParameterizedType, Serializable {

	private static final long serialVersionUID = 8375652423078560392L;

	protected String affirmativeValue;
	protected String negativeValue;

	public FlagUserType() {

	}

	@Override
	public int[] sqlTypes() {
		return new int[] { StringType.INSTANCE.sqlType() };
	}

	@Override
	public Class<?> returnedClass() {
		return Boolean.class;
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
		return x == null ? 0 : x.hashCode();
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
		return (FlagUserType) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return (FlagUserType) cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@Override
	public void setParameterValues(Properties parameters) {
		affirmativeValue = parameters.getProperty("affirmative");
		negativeValue = parameters.getProperty("negative");
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		Object realValue = rs.getObject(names[0]);
		if (rs.wasNull()) {
			return null;
		} else {
			if (realValue.equals(affirmativeValue))
				return true;
			else if (realValue.equals(negativeValue))
				return false;
			else
				throw new HibernateException("Value could not be converted to Boolean");
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {

		if (value == null) {
			st.setNull(index, StringType.INSTANCE.sqlType());
		} else if (value instanceof Boolean) {
			Boolean b = (Boolean) value;
			if (b) {
				st.setString(index, affirmativeValue);
			} else
				st.setString(index, negativeValue);
		} else {
			throw new HibernateException("Value was not Boolean or null");
		}
		
	}
	
	
}
