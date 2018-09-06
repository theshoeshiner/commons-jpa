package org.hibernate.vaadin;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.session.SessionManager;


/**
 * This class is used to wrap database services that implement SessionManager
 * It prevents us from having to pass references to database services around.
 * Since services that implement SessionManager are usually not Serializable, we
 * need to keep those references in the Vaadin application object so that it's
 * re-attached in the event of a session failover.
 * @author q775001
 *
 */
@SuppressWarnings("serial")
public abstract class SessionManagerWrapper implements SessionManager,Serializable {

	public abstract SessionManager getSessionManager();
	
	@Override
	public Session getCurrentSession() {
		return getSessionManager().getCurrentSession();
	}

	@Override
	public void closeCurrentSession() {
		getSessionManager().closeCurrentSession();
	}

	@Override
	public void commitCurrentSession() {
		getSessionManager().commitCurrentSession();
	}

	@Override
	public Session openSession() {
		return getSessionManager().openSession();
	}

	@Override
	public void flushAndClearSession() {
		getSessionManager().flushAndClearSession();
	}
	
}
