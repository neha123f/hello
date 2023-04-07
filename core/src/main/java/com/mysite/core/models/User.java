package com.mysite.core.models;

import java.util.List;


public class User {
	private String username;
    private String password;
    private String role;
    private int eid;
    private String email;
    private String phonenumber;
    private List<Booking> bookings;

    public User(String username, String password, String role, int eid, String email, String phonenumber, List<Booking> bookings) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.eid = eid;
        this.email = email;
        this.phonenumber = phonenumber;
        this.bookings = bookings;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<Booking> getBookings() {
        return bookings;
    }

    public int getEid() {
    	return eid;
    }

    public String getemail() {
    	return email;
    }

    public String getPhonenumber() {
    	return phonenumber;
    }
   
    public static class Booking {
        private String bookingid;
        private String floor;
        private String seat_no;
        private String timing;
        private boolean lunch_opted;
        private String fromdate;
        private String todate;
        
    public Booking(String bookingid, String floor, String seat_no, String timing, boolean lunch_opted, String fromdate,
			String todate) {
		super();
		this.bookingid = bookingid;
		this.floor = floor;
		this.seat_no = seat_no;
		this.timing = timing;
		this.lunch_opted = lunch_opted;
		this.fromdate = fromdate;
		this.todate = todate;
	}

	public String getBookingid() {
		return bookingid;
	}
	
	public String getFloor() {
		return floor;
	}
	
	public String getSeat_no() {
		return seat_no;
	}
	
	public String getTiming() {
		return timing;
	}
	
	public boolean isLunch_opted() {
		return lunch_opted;
	}
	
	public String getFromdate() {
		return fromdate;
	}
	
	public String getTodate() {
		return todate;
	}
}
}
