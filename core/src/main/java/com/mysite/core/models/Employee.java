package com.mysite.core.models;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = SlingHttpServletRequest.class)
public class Employee {
	@Inject
	private String username;
	@Inject
	private String password;
	@Inject
	private String role;
	@Inject
	private String email;
	@Inject
	private String eid;
	@Inject
	private String phone;
	@Inject
	private String name;
	
	
	

   public Employee(String username, String password, String role, String email, String eid, String phone,
			String name) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;
		this.eid = eid;
		this.phone = phone;
		this.name = name;
	}




public Employee() {
	super();
	
}




public Employee(String username, String password, String role, String email) {
	super();
	this.username = username;
	this.password = password;
	this.role = role;
	this.email = email;
}




public String getUsername() {
	return username;
}




public void setUsername(String username) {
	this.username = username;
}




public String getPassword() {
	return password;
}




public void setPassword(String password) {
	this.password = password;
}




public String getRole() {
	return role;
}




public void setRole(String role) {
	this.role = role;
}




public String getEmail() {
	return email;
}




public void setEmail(String email) {
	this.email = email;
}




public String getEid() {
	return eid;
}




public void setEid(String eid) {
	this.eid = eid;
}




public String getPhone() {
	return phone;
}




public void setPhone(String phone) {
	this.phone = phone;
}




public String getName() {
	return name;
}




public void setName(String name) {
	this.name = name;
}

  
   

}