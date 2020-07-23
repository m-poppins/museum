import java.sql.ResultSet;
import java.sql.Statement;
public class DBTableWorker {
	protected DBConnection connection;
	public DBTableWorker(){
		connection = DBConnection.connect();
	}
	protected String table_name()
	{
		return null;
	}
	protected String[] column_definitions()
	{
		return null;
	}
	public String[] column_names()
	{
		return null;
	}
	public void create()
	{
		if (this.table_name() != null) {
			String sql = "CREATE TABLE " + this.table_name() + "(" + String.join(",", column_definitions()) + ")";
			try {
				Statement stmt = connection.cbase.createStatement();
				stmt.executeUpdate(sql);
			}catch(Exception e){
				System.out.println(sql);
				System.out.println(e);
			}
		}
	}
	public void drop()
	{
		if (this.table_name() != null) {
			String sql = "DROP TABLE " + this.table_name()+" cascade constraints";
			try{
				Statement stmt = connection.cbase.createStatement();
				stmt.executeUpdate(sql);
			}catch(Exception e){
				System.out.println(sql);
				System.out.println(e);
			}
		}
	}
	
	public String data_to_field(String data)
	{
		if(data.trim().length() == 0)
		return "null";
		else
		return data.trim();
	}
	public boolean test_connection()
	{
		try{
			String res;
			Statement stmt = connection.cbase.createStatement();

			ResultSet rs = stmt.executeQuery("select 1 from dual");
			rs.next();
			res = rs.getString(1);
			if (res.equals("1")) return true;
			return false;
		}catch(Exception e){
			return false;
		}
	}
}
