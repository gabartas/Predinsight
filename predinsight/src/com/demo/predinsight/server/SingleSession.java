package com.demo.predinsight.server;

public class SingleSession {
	
	private static SingleSession INSTANCE = new SingleSession();
	private Session session;
	private boolean set = false;

	private SingleSession() {}
	
	public static SingleSession getInstance()
    {   return INSTANCE;
    }

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
		set = true;
	}
	
	public boolean alreadyset() {
		return set;
	}
}
