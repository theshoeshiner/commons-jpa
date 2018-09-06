package org.hibernate.session;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SessionFactorySessionManager implements SessionManager {
	
	protected SessionFactory sessionFactory;
	protected boolean readOnly = false;
	
	public SessionFactorySessionManager(SessionFactory sf){
		this.sessionFactory = sf;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getCurrentSession() {
        Session currentSession = getSessionFactory().getCurrentSession();
        if(!currentSession.getTransaction().isActive()) {
            currentSession.beginTransaction();
            if(readOnly){
            	currentSession.setDefaultReadOnly(readOnly);
            	currentSession.setHibernateFlushMode(FlushMode.MANUAL);
            }
        }
        return currentSession;
    }
	
	public void closeCurrentSession() {
		Session currentSession = getSessionFactory().getCurrentSession();	     
		if (currentSession.isOpen()) {
			currentSession.close();
		}
	}
	
	public void flushAndClearSession() {
		Session currentSession = getSessionFactory().getCurrentSession();	     
		currentSession.flush();
		currentSession.clear();
	}
	
	public void commitCurrentSession() {
		Session currentSession = getSessionFactory().getCurrentSession();	     
		if (currentSession.getTransaction().isActive() && !readOnly) {
			currentSession.getTransaction().commit();
		}
	}

	public Session openSession() {
		Session currentSession = getSessionFactory().openSession();
		//dont really need this if statement - transaction should always be new?
        if(!currentSession.getTransaction().isActive()) {
        	if(readOnly){
        		currentSession.setDefaultReadOnly(readOnly);
        		currentSession.setHibernateFlushMode(FlushMode.MANUAL);
        	}
            currentSession.beginTransaction();
        }
        return currentSession;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
}
