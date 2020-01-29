package com.ms.wmadv.modelrecommend;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
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
	
	//private String url = "jdbc:sqlserver://ilasqldbserver.database.windows.net:1433;database=ilasqldb;user=ilamuruk@ilasqldbserver;password=Dec2019M;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
	private String url = "jdbc:sqlserver://azuredtabasesrvr9.database.windows.net:1433;database=azuredatabase9;user=azuredtabaseuser9@azuredtabasesrvr9;password=Welcometoazure#1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
	
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
	
	public List<String> getObjectsMSSql(){
		
		List<String> objList = new ArrayList<>();
		Connection myDbConn = null;
		try {
			System.out.println("system driver prop bef: " + System.getProperty("jdbc.drivers"));
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			System.out.println("system driver prop aft: " + System.getProperty("jdbc.drivers"));
			
			String sql = "SELECT name FROM sysobjects where name like '%test%';";
			
			Statement statement = myDbConn.createStatement();
			ResultSet results = statement.executeQuery(sql);
			
			while (results.next())
			{
				objList.add(results.getString(1));
				System.out.println(objList);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return objList;
	}
	
	public List<String> createObjectMSSql(String name) {
		
		List<String> objList = new ArrayList<>();
		Connection myDbConn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "CREATE TABLE " + name +
	                   "(first VARCHAR(255), " + 
	                   " last VARCHAR(255), " + 
	                   " age INTEGER)";
			
			Statement statement = myDbConn.createStatement();
			statement.executeUpdate(sql);
			try { myDbConn.commit(); } catch(Exception e) {}
			
			objList = getObjectsMSSql();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return objList;
	}
	
	public Model getModel(Integer id) throws Exception {
		
		Connection myDbConn = null;
		Model model = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "SELECT * FROM MODEL WHERE model_id = " + id;
			Statement statement = myDbConn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			List<Model> models = populateData(result, Model.class);
			
			if(models.isEmpty()) {
				throw new Exception("Model not found for id - " + id);
			}
			model = models.get(0);
		}catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return model;
	}
	
	public Model getModelHoldings(Integer id) throws Exception {
		
		Connection myDbConn = null;
		Model model = null;
		try {
			model =  getModel(id);
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "SELECT * FROM MODEL_HOLDINGS WHERE model_id = " + id;
			Statement statement = myDbConn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			List<ModelHolding> modelHoldings = populateData(result, ModelHolding.class);
			model.setModelHoldings(modelHoldings);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return model;
	}
	
	public List<Product> getProducts() throws Exception {
		
		Connection myDbConn = null;
		List<Product> products = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "SELECT TOP (2) * FROM PRODUCT";
			Statement statement = myDbConn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			products = populateData(result, Product.class);
		}catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return products;
	}
	
	private <T> List<T> populateData(ResultSet result, Class<T> klass) throws Exception {
		List<T> records = new ArrayList<T>();
		BeanWrapper beanWrapper = null;
		T object = null;
		
		Field[] fields = klass.getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>();
		for(Field field : fields) {
			field.setAccessible(true);
			fieldNames.add(field.getName().toLowerCase());
		}
		
		ResultSetMetaData rsmd = result.getMetaData();
		int cols = rsmd.getColumnCount();
		while(result.next())
		{
			object = klass.newInstance();
			beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
			for(int i=1; i<=cols; i++) {
				String colName = rsmd.getColumnName(i).toLowerCase();
				Object colVal = result.getObject(i);
				
				if(fieldNames.contains(colName)) {
					beanWrapper.setPropertyValue(colName, colVal);
				}
		    }
			records.add(object);
		}
		
		return records;
	}
	
	public List<HashMap<String, Object>> loadObjectDataMSSql(String name) {
		
		List<HashMap<String, Object>> records = new ArrayList<>();
		Connection myDbConn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "INSERT INTO " + name +
	                   " VALUES ('fname1','lastname1', 30)";
			
			Statement statement = myDbConn.createStatement();
			statement.executeUpdate(sql);
			try { myDbConn.commit(); } catch(Exception e) {}
			
			sql = "INSERT INTO " + name +
	                   " VALUES ('fname2','lastname2', 30)";
			statement.executeUpdate(sql);
			try { myDbConn.commit(); } catch(Exception e) {}
			
			//records = getObjectDataMSSql(name);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return records;
	}
	
	public List<String> dropObjectMSSql(String name) {
		
		List<String> objList = new ArrayList<>();
		Connection myDbConn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "DROP TABLE " + name;
			
			Statement statement = myDbConn.createStatement();
			statement.executeUpdate(sql);
			try { myDbConn.commit(); } catch(Exception e) {}
			
			objList = getObjectsMSSql();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return objList;
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
