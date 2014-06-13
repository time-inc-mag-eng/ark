/*******************************************************************************
 * Copyright 2014 Time Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.timeInc.ark.util.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.LiteralType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.VersionType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.java.JdbcTimestampTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.TimestampTypeDescriptor;

/**
 * The JDBC driver automatically stores a Timestamp type using the current JVM timezone. This type descriptor forces the JDBC methods
 * to use UTC instead of the local time zone.
 * 
 * In addition the connection property for MYSQL db must have useLegacyDatetimeCode=false
 * 
 * see http://bugs.mysql.com/bug.php?id=15604
 * 
 */
public class UtcTimestampType extends AbstractSingleColumnStandardBasicType<Date> implements VersionType<Date>, LiteralType<Date> {

	private static final long serialVersionUID = -5497306660493069627L;

	/** The Constant INSTANCE. */
	public static final UtcTimestampType INSTANCE = new UtcTimestampType();

	/**
	 * Instantiates a new utc timestamp type.
	 */
	public UtcTimestampType() {
		super( UtcTimestampTypeDescriptor.INSTANCE, JdbcTimestampTypeDescriptor.INSTANCE );
	}

	/* (non-Javadoc)
	 * @see org.hibernate.type.Type#getName()
	 */
	public String getName() {
		return TimestampType.INSTANCE.getName();
	}

	/* (non-Javadoc)
	 * @see org.hibernate.type.AbstractStandardBasicType#getRegistrationKeys()
	 */
	@Override
	public String[] getRegistrationKeys() {
		return TimestampType.INSTANCE.getRegistrationKeys();
	}

	/* (non-Javadoc)
	 * @see org.hibernate.type.VersionType#next(java.lang.Object, org.hibernate.engine.SessionImplementor)
	 */
	public Date next(Date current, SessionImplementor session) {
		return TimestampType.INSTANCE.next(current, session);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.type.VersionType#seed(org.hibernate.engine.SessionImplementor)
	 */
	public Date seed(SessionImplementor session) {
		return TimestampType.INSTANCE.seed(session);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.type.VersionType#getComparator()
	 */
	public Comparator<Date> getComparator() {
		return TimestampType.INSTANCE.getComparator();        
	}

	/* (non-Javadoc)
	 * @see org.hibernate.type.LiteralType#objectToSQLString(java.lang.Object, org.hibernate.dialect.Dialect)
	 */
	public String objectToSQLString(Date value, Dialect dialect) throws Exception {
		return TimestampType.INSTANCE.objectToSQLString(value, dialect);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.type.AbstractStandardBasicType#fromStringValue(java.lang.String)
	 */
	public Date fromStringValue(String xml) throws HibernateException {
		return TimestampType.INSTANCE.fromStringValue(xml);
	}


	private static class UtcTimestampTypeDescriptor extends TimestampTypeDescriptor {
		private static final long serialVersionUID = 4330885675665606501L;

		public static final UtcTimestampTypeDescriptor INSTANCE = new UtcTimestampTypeDescriptor();

		private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

		public <X> ValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
			return new BasicBinder<X>( javaTypeDescriptor, this ) {
				@Override
				protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
					st.setTimestamp( index, javaTypeDescriptor.unwrap(value, Timestamp.class, options), Calendar.getInstance(UTC) );
				}
			};
		}

		public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
			TimeZone.setDefault(UTC);
			
			return new BasicExtractor<X>( javaTypeDescriptor, this ) {
				@Override
				protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
					Timestamp stamp = rs.getTimestamp(name, Calendar.getInstance(UTC));
					return javaTypeDescriptor.wrap(stamp, options );
				}
			};
		}
	}
}
