package com.ms.wmadv.modelrecommend;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ModelRecommendRepository {

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
	
	public List<Product> fetchProductById(String id) throws Exception {
		
		List<Product> products = null;
		Connection myDbConn = null;
			
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "Select * from product where symbol=?;";
			
			PreparedStatement prepStatement = myDbConn.prepareStatement(sql);
			prepStatement.setString(1, id);
			ResultSet result = prepStatement.executeQuery();
			
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

	public List<Product> fetchAllProducts() throws Exception {
		
		List<Product> products = null;
		Connection myDbConn = null;
			
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "Select * from product;";
			
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
	
	public List<Product> fetchProductsBySymbol(String symbol) throws Exception {
		
		List<Product> products = null;
		Connection myDbConn = null;
			
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "Select * from product where symbol like ?;";
			
			PreparedStatement prepStatement = myDbConn.prepareStatement(sql);
			prepStatement.setString(1, "%" + symbol + "%");
			ResultSet result = prepStatement.executeQuery();
			
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

	public List<Product> fetchProductRecommendations(Double riskScore) throws Exception {
		
		List<Product> products = null;
		Connection myDbConn = null;
			
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "Select TOP (10) * from product;";
			
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

	public void saveModel(Model model) throws Exception {
		
		Connection myDbConn = null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			Integer modelId = model.getModel_id();
			System.out.println("Saving model for: " + modelId);
			String sql;
			if(modelId == null) {
				// insert
				sql = "INSERT INTO MODEL (rownum,account_name,inv_horizon,inv_obj_least,"
						+ "inv_obj_most,inv_obj_imp,inv_obj_some_imp,inv_amount,liquidy_need,"
						+ "model_id,model_name,primary_fin_need,risk_profile,risk_tolerance,volatility) "
						+ "OUTPUT Inserted.model_id SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, MAX(model_id) + 1, "
						+ "?, ?, ?, ?, ? FROM MODEL";
			}
			else {
				// update
				sql = "UPDATE MODEL SET rownum=?,account_name=?,inv_horizon=?,inv_obj_least=?,"
						+ "inv_obj_most=?,inv_obj_imp=?,inv_obj_some_imp=?,inv_amount=?,liquidy_need=?,"
						+ "model_name=?,primary_fin_need=?,risk_profile=?,risk_tolerance=?,volatility=? "
						+ "OUTPUT Inserted.model_id WHERE model_id = " + modelId;
			}
			
			PreparedStatement statement = myDbConn.prepareStatement(sql);
			statement.setInt(1, model.getRownum());
			statement.setString(2, model.getAccount_name());
			statement.setString(3, model.getInv_horizon());
			statement.setInt(4, model.getInv_obj_least());
			statement.setInt(5, model.getInv_obj_most());
			statement.setInt(6, model.getInv_obj_imp());
			statement.setInt(7, model.getInv_obj_some_imp());
			statement.setInt(8, model.getInv_amount());
			statement.setString(9, model.getLiquidy_need());
			statement.setString(10, model.getModel_name());
			statement.setString(11, model.getPrimary_fin_need());
			statement.setInt(12, model.getRisk_profile());
			statement.setInt(13, model.getRisk_tolerance());
			statement.setDouble(14, model.getVolatility());
			
			ResultSet result = statement.executeQuery();
			System.out.println("Model saved");
			
			result.next();
			modelId = result.getInt(1);
			model.setModel_id(modelId);
			System.out.println("Generated model id: " + modelId);
			
			List<ModelHolding> modelHoldings = model.getModelHoldings();
			if(modelHoldings != null && !modelHoldings.isEmpty()) {
				System.out.println("Save model holdings for: " + modelId);
				saveModelHoldings(modelId, modelHoldings);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
	}

	private void saveModelHoldings(Integer modelId, List<ModelHolding> modelHoldings) throws Exception {
		System.out.println("Saving model holdings for: " + modelId);
		Connection myDbConn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			for(ModelHolding modelHolding : modelHoldings) {
				List<ModelHolding> modelHoldingsDB = getModelHoldings(modelId, modelHolding.getTicker());
				String sql;
				if(modelHoldingsDB == null || modelHoldingsDB.isEmpty()) {
					// insert
					sql = "INSERT INTO MODEL_HOLDINGS (rownum,percentage,model_id,ticker) "
							+ "VALUES (?, ?, ?, ?)";
				}
				else {
					// update
					sql = "UPDATE MODEL_HOLDINGS SET rownum=?,percentage=? "
							+ "WHERE model_id = ? AND ticker = ?";
				}
				
				PreparedStatement statement = myDbConn.prepareStatement(sql);
				statement.setInt(1, modelHolding.getRownum());
				statement.setDouble(2, modelHolding.getPercentage());
				statement.setInt(3, modelId);
				statement.setString(4, modelHolding.getTicker());
				
				statement.executeUpdate();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
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
			
			model.setModelHoldings(getModelHoldings(id));
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
	
	public List<ModelHolding> getModelHoldings(Integer id) throws Exception {
		
		Connection myDbConn = null;
		List<ModelHolding> modelHoldings = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "SELECT * FROM MODEL_HOLDINGS WHERE model_id = " + id;
			Statement statement = myDbConn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			modelHoldings = populateData(result, ModelHolding.class);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return modelHoldings;
	}
	
	public List<ModelHolding> getModelHoldings(Integer id, String symbol) throws Exception {
		
		Connection myDbConn = null;
		List<ModelHolding> modelHoldings = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "SELECT * FROM MODEL_HOLDINGS WHERE model_id = " + id
					+ " AND ticker = '" + symbol + "'";
			Statement statement = myDbConn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			modelHoldings = populateData(result, ModelHolding.class);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return modelHoldings;
	}

	public List<Model> fetchAllModels() throws Exception {
		Connection myDbConn = null;
		List<Model> models = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "SELECT * FROM MODEL";
			Statement statement = myDbConn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			models = populateData(result, Model.class);
			
		}catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return models;
	}

	public List<Model> fetchModelsByName(String query) throws Exception {
		Connection myDbConn = null;
		List<Model> models = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			myDbConn = DriverManager.getConnection(url);
			
			String sql = "Select * from model where model_name like ?;";
			
			PreparedStatement prepStatement = myDbConn.prepareStatement(sql);
			prepStatement.setString(1, "%" + query + "%");
			ResultSet result = prepStatement.executeQuery();
			models = populateData(result, Model.class);
			
		}catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			try { myDbConn.commit(); } catch(Exception e) {}
			try { myDbConn.close(); } catch(Exception e) {}
		}
		
		return models;
	}

}