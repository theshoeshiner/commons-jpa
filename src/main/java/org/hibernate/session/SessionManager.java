package org.hibernate.session;

import org.hibernate.Session;

public interface SessionManager {

	public Session getCurrentSession();
	
	public void commitCurrentSession();
	
	public void closeCurrentSession();
	
	public Session openSession();
	
	public void flushAndClearSession();
	
}
