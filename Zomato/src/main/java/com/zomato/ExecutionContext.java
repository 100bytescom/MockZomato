package com.zomato;

import org.glassfish.hk2.api.ServiceLocator;

/**
 * Thread local Context for each request 
 * @author ashok
 */

public final class ExecutionContext {

	private ServiceLocator serviceLocator;
	private String subject;
	private static ThreadLocal<ExecutionContext> CONTEXT = new ThreadLocal<ExecutionContext>();
	
	public ServiceLocator getServiceLocator() {
		return serviceLocator;
	}
	public String getSubject() {
		return subject;
	}
	
	public ExecutionContext(ServiceLocator sl, String subject)	{
		this.serviceLocator = sl;
		this.subject = subject;
	}
	public static ExecutionContext get()	{
		return CONTEXT.get();
	}
	public static void set(ExecutionContext context) {
		CONTEXT.set(context);
	}
}
