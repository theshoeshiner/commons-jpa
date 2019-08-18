package org.hibernate.type;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.persistence.Enumerated;
import javax.persistence.MapKeyEnumerated;

import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.CoreLogging;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.metamodel.model.convert.internal.NamedEnumValueConverter;
import org.hibernate.metamodel.model.convert.internal.OrdinalEnumValueConverter;
import org.hibernate.metamodel.model.convert.spi.EnumValueConverter;
import org.hibernate.type.descriptor.java.EnumJavaTypeDescriptor;
import org.hibernate.type.spi.TypeConfiguration;
import org.hibernate.type.spi.TypeConfigurationAware;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.LoggableUserType;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.hibernate.usertype.DynamicParameterizedType.ParameterType;
import org.jboss.logging.Logger;

@SuppressWarnings({"unchecked","rawtypes"})
public class EnumListType<T extends Collection<Enum>> implements UserType, ParameterizedType,TypeConfigurationAware, Serializable {
	

	//@SuppressWarnings("unchecked")
	//public class EnumType<T extends Enum>
			
		private static final Logger LOG = CoreLogging.logger( EnumType.class );
		
		public static final String DEFAULT_SEP = ",";

		public static final String ENUM = "enumClass";
		public static final String NAMED = "useNamed";
		public static final String TYPE = "type";

		private String separator = DEFAULT_SEP;
		
		private Class enumClass;

		private EnumValueConverter enumValueConverter;

		private TypeConfiguration typeConfiguration;

		
		@Override
		public void setParameterValues(Properties parameters) {
			// IMPL NOTE: we handle 2 distinct cases here:
			// 		1) we are passed a ParameterType instance in the incoming Properties - generally
			//			speaking this indicates the annotation-binding case, and the passed ParameterType
			//			represents information about the attribute and annotation
			//		2) we are not passed a ParameterType - generally this indicates a hbm.xml binding case.
			/*final ParameterType reader = (ParameterType) parameters.get( PARAMETER_TYPE );

			if ( reader != null ) {
				enumClass = reader.getReturnedClass().asSubclass( Enum.class );

				final boolean isOrdinal;
				final javax.persistence.EnumType enumType = getEnumType( reader );
				if ( enumType == null ) {
					isOrdinal = true;
				}
				else if ( javax.persistence.EnumType.ORDINAL.equals( enumType ) ) {
					isOrdinal = true;
				}
				else if ( javax.persistence.EnumType.STRING.equals( enumType ) ) {
					isOrdinal = false;
				}
				else {
					throw new AssertionFailure( "Unknown EnumType: " + enumType );
				}

				final EnumJavaTypeDescriptor enumJavaDescriptor = (EnumJavaTypeDescriptor) typeConfiguration
						.getJavaTypeDescriptorRegistry()
						.getDescriptor( enumClass );

				if ( isOrdinal ) {
					this.enumValueConverter = new OrdinalEnumValueConverter( enumJavaDescriptor );
				}
				else {
					this.enumValueConverter = new NamedEnumValueConverter( enumJavaDescriptor );
				}
			}
			else {*/
				final String enumClassName = (String) parameters.get( ENUM );
				try {
					enumClass = ReflectHelper.classForName( enumClassName, this.getClass() ).asSubclass( Enum.class );
				}
				catch ( ClassNotFoundException exception ) {
					throw new HibernateException( "Enum class not found: " + enumClassName, exception );
				}

				this.enumValueConverter = interpretParameters( parameters );
			//}

			LOG.debugf(
					"Using %s-based conversion for Enum %s",
					isOrdinal() ? "ORDINAL" : "NAMED",
					enumClass.getName()
			);
		}

		private javax.persistence.EnumType getEnumType(ParameterType reader) {
			javax.persistence.EnumType enumType = null;
			if ( reader.isPrimaryKey() ) {
				MapKeyEnumerated enumAnn = getAnnotation( reader.getAnnotationsMethod(), MapKeyEnumerated.class );
				if ( enumAnn != null ) {
					enumType = enumAnn.value();
				}
			}
			else {
				Enumerated enumAnn = getAnnotation( reader.getAnnotationsMethod(), Enumerated.class );
				if ( enumAnn != null ) {
					enumType = enumAnn.value();
				}
			}
			return enumType;
		}

		private <A extends Annotation> A getAnnotation(Annotation[] annotations, Class<A> anClass) {
			for ( Annotation annotation : annotations ) {
				if ( anClass.isInstance( annotation ) ) {
					return (A) annotation;
				}
			}
			return null;
		}

		private EnumValueConverter interpretParameters(Properties parameters) {
			final EnumJavaTypeDescriptor javaTypeDescriptor = (EnumJavaTypeDescriptor) typeConfiguration
					.getJavaTypeDescriptorRegistry()
					.getDescriptor( enumClass );
			if ( parameters.containsKey( NAMED ) ) {
				final boolean useNamed = ConfigurationHelper.getBoolean( NAMED, parameters );
				if ( useNamed ) {
					return new NamedEnumValueConverter( javaTypeDescriptor );
				}
				else {
					return new OrdinalEnumValueConverter( javaTypeDescriptor );
				}
			}

			if ( parameters.containsKey( TYPE ) ) {
				final int type = Integer.decode( (String) parameters.get( TYPE ) );
				if ( isNumericType( type ) ) {
					return new OrdinalEnumValueConverter( javaTypeDescriptor );
				}
				else if ( isCharacterType( type ) ) {
					return new NamedEnumValueConverter( javaTypeDescriptor );
				}
				else {
					throw new HibernateException(
							String.format(
									Locale.ENGLISH,
									"Passed JDBC type code [%s] not recognized as numeric nor character",
									type
							)
					);
				}
			}

			// the fallback
			return new OrdinalEnumValueConverter( javaTypeDescriptor );
		}

		private boolean isCharacterType(int jdbcTypeCode) {
			switch ( jdbcTypeCode ) {
				case Types.CHAR:
				case Types.LONGVARCHAR:
				case Types.VARCHAR: {
					return true;
				}
				default: {
					return false;
				}
			}
		}

		private boolean isNumericType(int jdbcTypeCode) {
			switch ( jdbcTypeCode ) {
				case Types.INTEGER:
				case Types.NUMERIC:
				case Types.SMALLINT:
				case Types.TINYINT:
				case Types.BIGINT:
				case Types.DECIMAL:
				case Types.DOUBLE:
				case Types.FLOAT: {
					return true;
				}
				default:
					return false;
			}
		}

		@Override
		public int[] sqlTypes() {
			verifyConfigured();
			//return new int[] { enumValueConverter.getJdbcTypeCode() };
			return new int[] { Types.VARCHAR};
		}

		
		@Override
		public Class<? extends Collection> returnedClass() {
			return List.class;
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
		public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
			verifyConfigured();
			String values = rs.getString(names[0]);
			if(values == null) return Collections.emptyList();
			else {
				List<Enum> enums = convertToEnums(values);
				return enums;
			}
			
		}

		private void verifyConfigured() {
			if ( enumValueConverter == null ) {
				throw new AssertionFailure( "EnumType (" + enumClass.getName() + ") not properly, fully configured" );
			}
		}

		@Override
		public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
			verifyConfigured();
			//enumValueConverter.writeValue( st, (Enum) value, index );
			List<Enum> enums = (List<Enum>) value;
			if(value == null || enums.isEmpty()) st.setNull(index, sqlTypes()[0]);
			else {
				String rel = convertToString(enums);
				st.setString(index, rel.toString());
			}
			
			/*String values = rs.getString(names[0]);
			if(values == null) return Collections.emptyList();
			else {
				List<Enum> enums = new ArrayList<>();
				String[] vals = values.split(DEFAULT_SEP);
				for(String val :vals) {
					enums.add((Enum) enumValueConverter.toDomainValue(val));
				}
				return enums;
			}*/
			
		}
		
		protected String convertToString(Collection<Enum> enums) {
			StringBuilder rel = new StringBuilder();
			for(Iterator<Enum> it = enums.iterator();it.hasNext();) {
				Enum next = it.next();
				Object s = enumValueConverter.toRelationalValue(next);
				rel.append(s);
				if(it.hasNext())rel.append(separator);
			}
			return rel.toString();
		}
		
		protected List<Enum> convertToEnums(String values) {
			List<Enum> enums = new ArrayList<>();
			String[] vals = values.split(DEFAULT_SEP);
			for(String val :vals) {
				enums.add((Enum) enumValueConverter.toDomainValue(val));
			}
			return enums;
		}

		@Override
		public Object deepCopy(Object value) throws HibernateException {
			Collection c = (Collection) value;
			return new ArrayList<>(c);
		}

		@Override
		public boolean isMutable() {
			return true;
		}

		@Override
		public Serializable disassemble(Object value) throws HibernateException {
			return ( Serializable ) value;
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
		public TypeConfiguration getTypeConfiguration() {
			return typeConfiguration;
		}

		@Override
		public void setTypeConfiguration(TypeConfiguration typeConfiguration) {
			this.typeConfiguration = typeConfiguration;
		}

		/*@Override
		public String objectToSQLString(Object value) {
			verifyConfigured();
			
			return enumValueConverter.toSqlLiteral( value );
		}

		@Override
		public String toXMLString(Object value) {
			verifyConfigured();
			return (String) enumValueConverter.getJavaDescriptor().unwrap( (Enum) value, String.class, null );
		}

		@Override
		@SuppressWarnings("RedundantCast")
		public Object fromXMLString(String xmlValue) {
			verifyConfigured();
			return (T) enumValueConverter.getJavaDescriptor().wrap( xmlValue, null );
		}*/

		/*@Override
		public String toLoggableString(Object value, SessionFactoryImplementor factory) {
			verifyConfigured();
			return enumValueConverter.getJavaDescriptor().toString( (Enum) value );
		}*/
		
		public boolean isOrdinal() {
			verifyConfigured();
			return enumValueConverter instanceof OrdinalEnumValueConverter;
		}
	}
