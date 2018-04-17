package org.eclipse.pahodemoAsync;

//STEP 1. Import required packages
import java.sql.*;

public class tmMysql_obj {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://192.168.1.71/varstor";
	//static final String DB_URL ="jdbc:mysql://jonas-home.duckdns.org:10011/varstor";

	// Database credentials
	static final String USER = "jonas";
	static final String PASS = "jonas";
	
	//imstance fields
	/*private String mqttMessage = null;
	private String mqttSubscribeTo = null;
	private Integer	 mqttTimestamp = null;
	private Integer   mqttID = null;*/
	

	public void get2Database(String chipID, Integer timestamp, Integer deviceID, Integer heap, Float voltage) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver b8uggerbuggerbugger
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			//System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// STEP 3a: check if table exists for chipID and if not- create table and trigger
			stmt = conn.createStatement();
			ResultSet resultSet = null;
			String sqlCheck;
			sqlCheck = "SELECT COUNT(*)" + 
					"FROM information_schema.tables " + 
					"WHERE table_schema = 'varstor' " + 
					"AND table_name = 'esp"+chipID+"'";
			

			resultSet = stmt.executeQuery(sqlCheck);
			resultSet.next();// have to to get top 1st line of result set
			if (!resultSet.getBoolean(1) )		{ 
				
				sqlCheck = "CREATE TABLE IF NOT EXISTS esp"+chipID+" LIKE esp408776";//"408776" is the original chipid and used for very first table
				stmt.executeUpdate(sqlCheck);
				//have to make a trigger to suit new table, trigger limits table size to 6000 rows
				sqlCheck = "CREATE "
						//+"DEFINER=jonas@% "
						+"TRIGGER esp"+chipID+"before_insert "
						+"BEFORE INSERT ON esp"+chipID+" "
						+"FOR EACH ROW "
						+"BEGIN if (select COUNT(*) from esp"+chipID+") > 20000 "
							+"then delete from esp"+chipID+" where id > 0 "
							+"order by id asc limit 1; "
						+"end if; "
							+"END ";
				stmt.execute(sqlCheck);
				stmt = null;
			}
			// STEP 4: Execute a query
			//System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			
			sql = "INSERT INTO esp"+chipID+" (bat_serviceV,esp_heap) VALUES  ("+voltage+","+heap+" )";
			stmt.executeUpdate(sql);

			System.out.println("chipID of sender; "+chipID+"\n");
			System.out.println("data sent to DB, volts "+voltage);			
			
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
	//System.out.println("Goodbye!");
	}// end main
}// end FirstExample
