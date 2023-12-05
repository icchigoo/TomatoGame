package com.perisic.tomato.peripherals;

public class LoginData {
	
	boolean checkPassword(String username, String passwd) { 
		if( username.equals("Ada") && passwd.equals("hello23")) return true; 
		return false; 
		
	}
}
