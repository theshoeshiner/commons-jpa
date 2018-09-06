package org.hibernate.session;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LongRunningSessionManager {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(LongRunningSessionManager.class);
	
	protected boolean flushAndClear = true;
	protected boolean commit = true;
	protected SessionManager sessionManager;
	protected Integer flushSize = 2000; //flush and commit every N DB writes
	protected Integer commitSize = 100000; //flush and commit every N DB writes
	protected Integer rowCount;
	protected ThreadLocal<State> state = new ThreadLocal<State>();
	
	public static class State {
		long flushRowCount = 0;
		long commitRowCount = 0;
		void increment(){
			flushRowCount++;
			commitRowCount++;
		}
	}
	
	public LongRunningSessionManager(SessionManager manager){
		this.sessionManager = manager;
	}
	
	public LongRunningSessionManager(SessionManager manager,boolean flush,boolean commit){
		this.sessionManager = manager;
		this.flushAndClear = flush;
		this.commit = commit;
	}
	
	public void reset(){
		this.state.set(new State());
	}
	
	public Session databaseWrite(){
		State state = this.state.get();
		state.increment();
		Session session = sessionManager.getCurrentSession();
		if(flushAndClear && state.flushRowCount == flushSize){
			LOGGER.debug("flushing and clearing session...");
			sessionManager.flushAndClearSession();
			state.flushRowCount = 0;
			LOGGER.debug("done.");
		}
		if(commit && state.commitRowCount == commitSize){
			LOGGER.debug("committing and re-opening session");
			sessionManager.commitCurrentSession();
			state.commitRowCount = 0;
			session = sessionManager.getCurrentSession(); //get new session
			LOGGER.debug("done.");
		}
		return session;
	}

	public Integer getFlushSize() {
		return flushSize;
	}

	public void setFlushSize(Integer flushSize) {
		this.flushSize = flushSize;
	}

	public Integer getCommitSize() {
		return commitSize;
	}

	public void setCommitSize(Integer commitSize) {
		this.commitSize = commitSize;
	}

	public ThreadLocal<State> getState() {
		return state;
	}

	
	
}
