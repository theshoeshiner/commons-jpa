package org.hibernate.session;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VpdSessionManager extends SessionFactorySessionManager {
	
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(VpdSessionManager.class);
	
	protected UsernameManager usernameManager;
	protected String loginFunctionName = "APP_LOGIN";
	protected String logoutUserId = "";
	protected String getCurrentUserSql;
	protected Boolean useVpd = false;

	public VpdSessionManager(SessionFactory sf) {
		super(sf);
	}
	
	public VpdSessionManager(SessionFactory sf,Boolean useVpd,UsernameManager usernameManager) {
		super(sf);
		this.useVpd=useVpd;
		this.usernameManager = usernameManager;
	}

	@Override
	public Session openSession() {
		Session currentSession = super.openSession();
		if(useVpd)doVpdLogin(currentSession);
		return currentSession;
	}
	
	@Override
	public Session getCurrentSession() {
		Session currentSession = getSessionFactory().getCurrentSession();
		if(!currentSession.getTransaction().isActive()) {
			//need this so that we only open VPD once
			super.getCurrentSession();
			if(useVpd)doVpdLogin(currentSession);
		}
		return currentSession;
	}
	
	@Override
	public void closeCurrentSession(){
		Session currentSession = getSessionFactory().getCurrentSession();
		//Logout only if transaction was active
		if(currentSession.getTransaction().isActive()){
			if(useVpd)doVpdLogout(currentSession);
		}
		super.closeCurrentSession();
	}
	
	public void doVpdLogin(Session currentSession){
		doVpd(currentSession,usernameManager.getCurrentUsername());
	}
	
	public void doVpdLogout(Session currentSession){
		doVpd(currentSession,logoutUserId);
	}
	
	private void doVpd(Session currentSession,String userId){
		LOGGER.debug("doVpdLoginInternal {} {}",userId,currentSession);
		VpdLoginWork work = new VpdLoginWork(userId,loginFunctionName,getCurrentUserSql);
		currentSession.doWork(work);
	}

	public static interface UsernameManager {
		public String getCurrentUsername();
	}
	
	public UsernameManager getUsernameManager() {
		return usernameManager;
	}

	public void setUsernameManager(UsernameManager usernameManager) {
		this.usernameManager = usernameManager;
	}
	
	protected static final ThreadLocal<String> DB_USER_CONTEXT = new ThreadLocal<String>();
	
	public static class VpdLoginWork implements Work {

		protected static final Logger LOGGER = LoggerFactory.getLogger(VpdLoginWork.class);
		protected String userId;
		protected String loginFunctionName;
		protected String getCurrentUserSql;

		public VpdLoginWork(String userId,String loginFunctionName,String getCurrentUserSql) {
			this.userId = userId;
			this.loginFunctionName = loginFunctionName;
			this.getCurrentUserSql = getCurrentUserSql;
		}

		public void execute(Connection connection) throws SQLException {

			//set back to old value - saved in threadlocal variable
			 if(userId == null){
				String userId = DB_USER_CONTEXT.get();
				LOGGER.debug("Saved username: {}",userId);
				if(userId == null) userId = "";
				//if(userId == null) throw new IllegalArgumentException("VPDLoginWork could not determine username for Vpd login.");
			 }
			 else {
				//get current name and save
				 if(getCurrentUserSql != null){
					 ResultSet rs = connection.createStatement().executeQuery(getCurrentUserSql);
					 rs.next();
					 String currentUserName = rs.getString(1);
					 DB_USER_CONTEXT.set(currentUserName);
					 LOGGER.debug("Saving current username: {}",currentUserName);
				 }
			 }

			try {
				String sql = "call "+loginFunctionName+"(?)";
				LOGGER.debug("VPD Login:{} , {}",sql,userId);
				CallableStatement cs = connection.prepareCall(sql);
				cs.clearParameters();
				cs.setString(1, userId);
				cs.execute();
			} catch (SQLException sqlEx) {
				LOGGER.error(sqlEx.getMessage(), sqlEx);
				throw sqlEx;
			} 
			finally {
			}
			
		}
	}

	public String getLoginFunctionName() {
		return loginFunctionName;
	}

	public void setLoginFunctionName(String loginFunctionName) {
		this.loginFunctionName = loginFunctionName;
	}

	public String getLogoutUserId() {
		return logoutUserId;
	}

	public void setLogoutUserId(String logoutUserId) {
		this.logoutUserId = logoutUserId;
	}
	
	
}
