package org.eclipse.pahodemoAsync;

//STEP 1. Import required packages
import java.sql.*;

public class tmMysql_obj {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://192.168.1.71/varstor";
	// static final String DB_URL =
	// "jdbc:mysql://jonas-home.duckdns.org:10011/varstor";

	// Database credentials
	static final String USER = "jonas";
	static final String PASS = "jonas";
	
	//imstance fields
	private String mqttMessage = null;
	private String mqttSubscribeTo = null;
	private Integer	 mqttTimestamp = null;
	private Integer   mqttID = null;
	

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver fufckfuckfuck
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			// sql = "SELECT * FROM quest2";
			sql = "INSERT INTO quest2 (bat_serviceV,esp_heap) VALUES  (23.987,28500 bfz58)";
			stmt.executeUpdate(sql);
			
						
			sql = "SELECT * FROM quest2";
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set and fuck
			while (rs.next()) {
				// Retrieve by column name
				int id = rs.getInt("id");
				float age = rs.getFloat("bat_serviceV");
				String first = rs.getString("esp_heap");
				String last = rs.getString("timestamp");

				// Display values
				System.out.print("ID: " + id);
				System.out.print(", Voltage: " + age);
				System.out.print(", Heap: " + first);
				System.out.println(", Timestamp: " + last);
			}
			
			sql = "SELECT count(*)" +
					" FROM quest2";
			ResultSet boing = stmt.executeQuery(sql);
			//System.out.println(boing+"fuck");
			while (boing.next()) {
				System.out.println(boing.getString(1));
				
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye!");
	}// end main
}// end FirstExample
