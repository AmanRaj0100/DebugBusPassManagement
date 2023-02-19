package com.amazon.buspassmanagement;

//	2.4 Implement User defined Exception which should be thrown if User applies the pass again on the same route, given user has a pass suspended on the route
@SuppressWarnings("serial")
public class DuplicateSuspendedBusPassException extends Exception {
	
	public DuplicateSuspendedBusPassException(int routeId) {
	    super("A bus pass for route " + routeId + " has already been applied which is already suspended.");
	}
	
}