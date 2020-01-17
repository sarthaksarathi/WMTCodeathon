package com.wm.azuredemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBConnection {

	@Value("${database.server}")
	private String databaseServer;

	@Value("${database.name}")
	private String databaseName;
	
	@Value("${database.username}")
	private String username;
	
	@Value("${database.password}")
	private String password;
	
	public List<Inventory> getInventory(){
		
		/*
		 * String url
		 * ="jdbc:mysql://sarthakdbdemo.mysql.database.azure.com:3306/azuredemo?useSSL=true&requireSSL=false&"
		 * +
		 * "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		 */
		
		String url = DBConstants.urlPrefix + databaseServer +"/" +databaseName + "?" + DBConstants.sslConfig + "&" + DBConstants.timeConfig;
		
		List<Inventory> inventoryList = new ArrayList<>();
			
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection myDbConn = DriverManager.getConnection(url, username , password);
			
			String sql = "Select * from inventory;";
			
			Statement statement = myDbConn.createStatement();
			ResultSet results = statement.executeQuery(sql);
			
			while (results.next())
			{
				Inventory inventory = new Inventory(results.getString(2), results.getInt(3));
				inventoryList.add(inventory);
				String outputString = 
					String.format(
						"Data row = (%s, %s, %s)",
						results.getString(1),
						results.getString(2),
						results.getInt(3));
				System.out.println(outputString);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return inventoryList;
	}

	public String getDatabaseServer() {
		return databaseServer;
	}

	public void setDatabaseServer(String databaseServer) {
		this.databaseServer = databaseServer;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
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

}
