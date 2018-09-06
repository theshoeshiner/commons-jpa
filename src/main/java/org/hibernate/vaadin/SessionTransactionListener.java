package org.hibernate.vaadin;

import org.hibernate.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

public class SessionTransactionListener implements TransactionListener {

	private static final long serialVersionUID = 3988753248712230721L;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(SessionTransactionListener.class);
	
	protected SessionManager sessionManager;
	protected Application application;

	public SessionTransactionListener(Application application,SessionManager sessionManager) {
		this.sessionManager = sessionManager;
		this.application = application;
	}

	public void transactionStart(Application application, Object transactionData) {
		if (application == this.application) {
			LOGGER.trace("Session start {}",sessionManager);
			//Dont need to do anything here - sessions are opened ad hoc by the underlying implementation
		}
	}

	public void transactionEnd(Application application, Object transactionData) {
		if (application == this.application) {
			LOGGER.trace("Session end {}",sessionManager);
			try {
				sessionManager.commitCurrentSession();
			}
			catch(Exception e){
				LOGGER.error("Could not commit hibernate Session",e);
			}
			try {
				sessionManager.closeCurrentSession();
			}
			catch(Exception e){
				LOGGER.error("Could not close hibernate Session",e);
			}
		}
	}


}