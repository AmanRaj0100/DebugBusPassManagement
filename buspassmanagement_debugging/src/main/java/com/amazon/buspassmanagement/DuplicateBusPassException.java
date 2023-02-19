package com.amazon.buspassmanagement;

//Implement User defined Exception which should be thrown if User applies the pass again on the same route
@SuppressWarnings("serial")
public class DuplicateBusPassException extends Exception {

    public DuplicateBusPassException(int routeId) {
        super("A bus pass for route " + routeId + " has already been applied.");
    }

}
