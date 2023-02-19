package com.amazon.buspassmanagement.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.amazon.buspassmanagement.BusPassSession;
import com.amazon.buspassmanagement.DuplicateBusPassException;
import com.amazon.buspassmanagement.DuplicateSuspendedBusPassException;
import com.amazon.buspassmanagement.db.BusPassDAO;
import com.amazon.buspassmanagement.db.DB;
import com.amazon.buspassmanagement.model.BusPass;
import com.amazon.buspassmanagement.model.Route;

public class BusPassService {

	DB db = DB.getInstance();
	BusPassDAO passDAO = new BusPassDAO();
	
	// Create it as a Singleton 
	private static BusPassService passService = new BusPassService();
	Scanner scanner = new Scanner(System.in);
	
	public static BusPassService getInstance() {
		return passService;
	}
	
	private BusPassService() {
	
	}
	
	// Handler for the Bus Pass :)
	public void requestPass() throws DuplicateBusPassException, DuplicateSuspendedBusPassException {
		
		BusPass pass = new BusPass();
		pass.getDetails(false);
		
		String sql1 = "SELECT CASE WHEN count(1) > 0 THEN '1' ELSE '0' END AS Duplicate from BusPass where uid = "+BusPassSession.user.id+" AND routeId="+pass.routeId;
		ResultSet set1 = db.executeQuery(sql1);
		String sql2 = "SELECT CASE WHEN count(1) > 0 THEN '1' ELSE '0' END AS DuplicateSuspended from BusPass where uid = "+BusPassSession.user.id+" AND routeId = "+pass.routeId+" AND status = 4";
		ResultSet set2 = db.executeQuery(sql2);
		
		try {
			
			while(set2.next()) {
			int result2 = set2.getInt("DuplicateSuspended");
				if (result2==1) {
			        throw new DuplicateSuspendedBusPassException(pass.routeId);
			    }
			}
			
			while(set1.next()) {
			int result1 = set1.getInt("Duplicate");
			   if (result1==1) {
			        throw new DuplicateBusPassException(pass.routeId);
			    }
			}
			
			// Add the User ID Implicitly.
			pass.uid = BusPassSession.user.id;
			
			// As initially record will be inserted by User where it is a request
			pass.status = 1; // initial status as requested :)
			
			int result = passDAO.insert(pass);
			String message = (result > 0) ? "Pass Requested Successfully" : "Request for Pass Failed. Try Again.."; 
			System.out.println(message);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deletePass() {
		BusPass pass = new BusPass();
		System.out.println("Enter Pass ID to be deleted: ");
		pass.id = Integer.parseInt(scanner.nextLine());
		int result = passDAO.delete(pass);
		String message = (result > 0) ? "Pass Deleted Successfully" : "Deleting Pass Failed. Try Again.."; 
		System.out.println(message);
	}
	
	/*
	 
	 	Extra Task:
	 	IFF : You wish to UpSkill :)
	 
	 	Scenario: Open the same application in 2 different terminals
	 	1 logged in by user
	 	another logged in by admin
	 	
	 	If admin, approves or rejects the pass -> User should be notified :)
	 	
	 	Reference Link
	 	https://github.com/ishantk/AmazonAtlas22/blob/master/Session8/src/com/amazon/atlas/casestudy/YoutubeApp.java
	 
	 */
	
	public void approveRejectPassRequest() {
		
		BusPass pass = new BusPass();

		System.out.println("Enter Pass ID: ");
		pass.id = scanner.nextInt();
		
		System.out.println("2: Approve");
		System.out.println("3: Cancel");
		System.out.println("Enter Approval Choice: ");
		pass.status = scanner.nextInt();

    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Calendar calendar = Calendar.getInstance();
		Date date1 = calendar.getTime();
		pass.approvedRejectedOn = dateFormat.format(date1);
		
		if(pass.status == 2) {
			calendar.add(Calendar.YEAR, 1);
			Date date2 = calendar.getTime();
			pass.validTill = dateFormat.format(date2);
		}else {
			pass.validTill = pass.approvedRejectedOn;
		}
		
		int result = passDAO.update(pass);
		String message = (result > 0) ? "Pass Request Updated Successfully" : "Updating Pass Request Failed. Try Again.."; 
		System.out.println(message);
	}
	
	public void viewPassRequests() {
		
		System.out.println("Enter Route ID to get All the Pass Reqeuests for a Route");
		System.out.println("Or 0 for All Bus Pass Requests");
		System.out.println("Enter Route ID: ");
		
		int routeId = scanner.nextInt();
		
		List<BusPass> objects = null;
		
		if(routeId == 0) {
			objects = passDAO.retrieve();
		}else {
			String sql = "SELECT * from BusPass where routeId = "+routeId;
			objects = passDAO.retrieve(sql);
		}
		
		for(BusPass object : objects) {
			object.prettyPrint();
		}
	}
	
	public void viewPassRequestsByUser(int uid) {
		
		String sql = "SELECT * from BusPass where uid = "+uid;
		List<BusPass> objects = passDAO.retrieve(sql);
		
		for(BusPass object : objects) {
			object.prettyPrint();
		}
	}

	public void expiredPass() {
		
		String sql = "SELECT * from BusPass where validTill < GETDATE();";
		List<BusPass> objects = passDAO.retrieve(sql);
		
		if(objects.size()==0)
			System.out.println("No Expired BusPass available");
		
		for(BusPass object : objects) {
			object.prettyPrint();
		}	
	}
	

	public void betweenDatePass() {
		
		System.out.println("Enter From Date:(YYYY-MM-DD)");
		String from = scanner.nextLine();
		System.out.println("Enter To Date:(YYYY-MM-DD)");
		String to = scanner.nextLine();
		String sql = "SELECT * from BusPass where requestedOn BETWEEN '"+from+"' AND '"+to+"';";
		List<BusPass> objects = passDAO.retrieve(sql);
		
		if(objects.size()==0)
			System.out.println("No BusPass available between selected date range");
		
		for(BusPass object : objects) {
			object.prettyPrint();
		}
	}
	
}
