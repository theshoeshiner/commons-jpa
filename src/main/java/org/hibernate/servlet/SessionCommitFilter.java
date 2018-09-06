package org.hibernate.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;


public class SessionCommitFilter extends GenericFilterBean implements Filter {

	protected static final Logger LOGGER = LoggerFactory.getLogger(SessionCommitFilter.class);
	
	protected List<SessionManager> sessionManagers;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		LOGGER.trace("request start {}",httpRequest.getMethod());
		try {
			chain.doFilter(request, response);
			LOGGER.trace("request end");
			if (httpRequest.getMethod() != "POST") return;
			else commitAll();
		}
		finally {
			closeAll();
		}
		
	}
	
	protected void commitAll(){
		LOGGER.trace("commitAll: {}",sessionManagers);
		for(SessionManager sm : sessionManagers){
			try {
				sm.commitCurrentSession();
			}
			catch(Throwable t){
				LOGGER.warn("Caught throwable from SessionManager.commit of {}",sm,t);
			}
		}
	}

	protected void closeAll(){
		LOGGER.trace("closeAll: {}",sessionManagers);
		for(SessionManager sm : sessionManagers){
			try {
				sm.closeCurrentSession();
			}
			catch(Throwable t){
				LOGGER.warn("Caught throwable from SessionManager.close of {}",sm,t);
			}
		}
	}
	
	
	public List<SessionManager> getSessionManagers() {
		return sessionManagers;
	}

	public void setSessionManagers(List<SessionManager> sessionManagers) {
		this.sessionManagers = sessionManagers;
	}

	@Override
	protected void initFilterBean() throws ServletException {
		
	}

}
