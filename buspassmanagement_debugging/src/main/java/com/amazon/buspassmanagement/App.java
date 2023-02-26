package com.amazon.buspassmanagement;

import com.amazon.buspassmanagement.db.DB;

public class App {
	
    public static void main( String[] args ){
       
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println( "Welcome to Bus Pass Management Debug Application" );
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	
    	Menu menu = new Menu();
        
        if(args.length > 0) {
        	DB.FILEPATH = args[0];
        }
        
        menu.showMainMenu();
		
    }
}
