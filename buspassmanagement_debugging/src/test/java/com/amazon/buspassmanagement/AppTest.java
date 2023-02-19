package com.amazon.buspassmanagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.amazon.buspassmanagement.controller.AuthenticationService;
import com.amazon.buspassmanagement.db.BusPassDAO;
import com.amazon.buspassmanagement.db.FeedbackDAO;
import com.amazon.buspassmanagement.db.StopDAO;
import com.amazon.buspassmanagement.db.VehicleDAO;
import com.amazon.buspassmanagement.model.BusPass;
import com.amazon.buspassmanagement.model.Feedback;
import com.amazon.buspassmanagement.model.Stop;
import com.amazon.buspassmanagement.model.User;
import com.amazon.buspassmanagement.model.Vehicle;

// Reference Link to Use JUnit as Testing Tool in your Project
// https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html

public class AppTest {
    
	AuthenticationService authService = AuthenticationService.getInstance();
	BusPassDAO passdao = new BusPassDAO();
	VehicleDAO vehicledao = new VehicleDAO();
	StopDAO stopdao = new StopDAO();
	FeedbackDAO feedbackdao = new FeedbackDAO();
	
	// UNIT TESTS
	
	@Test
	public void testUserLogin() {
		
		User user = new User();
		user.email = "user@example.com";
		user.password = "5gbjiw2MGbJM8O44CBgxYup81j/3kS27IrXoAyhrREY=";
		
		boolean result = authService.loginUser(user);
		
		// Assertion -> Either Test Cases Passes or It will Fail :)
		Assert.assertEquals(true, result);
		Assert.assertEquals(2, user.type);
		
	}
	
	@Test
	public void testAdminLogin() {
		
		User user = new User();
		user.email = "admin@example.com";
		user.password = "JAvlGPq9JyTdtvBO6x2llnRI1+gxwIyPqCKAn3THIKk=";
		
		boolean result = authService.loginUser(user);
		
		// Assertion -> Either Test Cases Passes or It will Fail :)
		Assert.assertEquals(true, result);
		Assert.assertEquals(1, user.type); // 1 should be equal to 1
		
	}
	
	@Test
	public void testPassValidity() throws ParseException {
		
		List<BusPass> passes = new ArrayList<BusPass>();
		String sql = "SELECT * FROM BusPass WHERE id = 4";
		passes = passdao.retrieve(sql);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        String currentDate = dateFormat.format(date);
        
        Date date1 = dateFormat.parse((passes.get(0)).validTill);  
        Date date2 = dateFormat.parse(currentDate);
        
        boolean result = date1.after(date2);  
        
        Assert.assertEquals(true, result);
	}
	
	
	@Test
	public void testVehicleExists() {
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		String sql = "SELECT * FROM Vehicle WHERE routeID = 2";
		vehicles = vehicledao.retrieve(sql);
		
		boolean result = false;
		
		if(vehicles.size()>0) {
			result = true;
		}
        
		Assert.assertEquals(true, result);
	}
	
	
	@Test
	public void testStopExists() {
		
		List<Stop> stops = new ArrayList<Stop>();
		String sql = "SELECT * FROM Stop WHERE routeID = 2";
		stops = stopdao.retrieve(sql);
		
		boolean result = false;
		
		if(stops.size()>0) {
			result = true;
		}
        
		Assert.assertEquals(true, result);
	}
	
	
	@Test
	public void testMinStopExists() {
		
		List<Stop> stops = new ArrayList<Stop>();
		String sql = "SELECT * FROM Stop WHERE routeID = 2";
		stops = stopdao.retrieve(sql);
		
		boolean result = false;
		
		if(stops.size()>=2) {
			result = true;
		}
        
		Assert.assertEquals(true, result);
	}
	
	
	@Test
	public void raiseComplaint() {
		
		User user = new User();
		Feedback feedback = new Feedback();
		
		user.email = "user@example.com";
		user.password = "5gbjiw2MGbJM8O44CBgxYup81j/3kS27IrXoAyhrREY=";
		
		authService.loginUser(user);
		
		BusPassSession.user = user;

		feedback.type=2;
		feedback.title="Complaint";
		feedback.description="Aisa hi hun ji!!";
		feedback.userId = BusPassSession.user.id;
		feedback.raisedBy = BusPassSession.user.email;
		
		boolean result = false;
		
		if(feedbackdao.insert(feedback)>0) {
			result = true;
		}
        
		Assert.assertEquals(true, result);
	}
}


