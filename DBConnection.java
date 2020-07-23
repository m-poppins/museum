import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnection {
	private static volatile DBConnection connection;
	public Connection cbase;
	public static DBConnection connect(String user, String password)
	{
		DBConnection local_connection = connection;
		if (local_connection == null) {
			synchronized (DBConnection.class) {
				local_connection = connection;
				if (local_connection == null) {
					connection = local_connection = new DBConnection(user, password);
				}
			}
		}
		return local_connection;
	}

	public static DBConnection connect()
	{
		return DBConnection.connect(null, null);
	}

	public static DBConnection reconnect(String user, String password)
	{
		DBConnection local_connection = connection;
		if (local_connection == null) {
			synchronized (DBConnection.class) {
				local_connection = connection;
				if (local_connection == null) {
					connection = local_connection = new DBConnection(user, password);
				}
			}
		}
		else if (local_connection.cbase == null) {
			connection = local_connection = new DBConnection(user, password);
		}
		else {
			DBConnection.disconnect();
			connection = local_connection = new DBConnection(user, password);
		}
		return local_connection;
	}

	public static void disconnect()
	{
		DBConnection local_connection = connection;
		if (local_connection == null) {
			synchronized (DBConnection.class) {
				local_connection = connection;
				if (local_connection != null) {
					local_connection.close();
				}
			}
		}
	}

	public void close()
	{
		try{
			if (this.cbase != null) {
				this.cbase.close();
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}

	private DBConnection(String user, String password)
	{
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");

			this.cbase = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe", user, password); //Для дома
			// "jdbc:oracle:thin:@//ifebora.ifeb.mephi.ru:1521/pdborcl.ifeb.mephi.ru", user, password); // Для МИФИ
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
