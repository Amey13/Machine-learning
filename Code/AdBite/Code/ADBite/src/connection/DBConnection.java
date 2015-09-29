package connection;

import java.sql.*;

public class DBConnection
{
	
	public static final String DBNAME="ADEATER2";//"drjoping";
	public static final String USERNAME="root";//"drjoping";
	public static final String PASSWORD="123456789";//"786786786";
	public static final String HOST="localhost:3306";//"s218.eatj.com:3307";
	
	/*
	public static final String DBNAME="drjoping";
	public static final String USERNAME="drjoping";
	public static final String PASSWORD="786786786";
	public static final String HOST="s218.eatj.com:3307";
	*/
	public static Connection connect()
	{
		Connection connection=null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection=DriverManager.getConnection("jdbc:mysql://"+HOST+"/"+DBNAME,USERNAME,PASSWORD);

			if(connection==null)
				System.out.println("MYSQL SERVER NOT STARTED!!!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return connection;
	}
}
