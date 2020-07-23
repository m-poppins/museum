import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import oracle.jdbc.OraclePreparedStatement;
public class PeopleTable extends DBTableWorker {
	protected String table_name()
	{
		return "collection";
	}
	protected String[] column_definitions()
	{
		return new String[]
		{"ID_coll number(38) PRIMARY KEY",
		"name_coll varchar2(64 CHAR) NOT NULL",
		"start_date date NOT NULL",
		"finish_date date not null",
		" desc_coll VARCHAR2(1000 CHAR)"};
	}
	public String[] column_names()
	{
		return new String[]
		{"ID_coll",
 		"name_coll",
 		"start_date",
 		"finish_date"};
	}
	public void create()
	{
		super.create();
		String sql = "CREATE SEQUENCE co_ID_SEQ increment by 1 start with 10 order ";
		try{
			Statement stmt = connection.cbase.createStatement();
			stmt.executeUpdate(sql); 
		}catch(Exception e){
			System.out.println(sql);
			System.out.println(e);
		}
	}
	public void drop()
	{
		String sql = "DROP SEQUENCE co_ID_SEQ";
		try{
			Statement stmt = connection.cbase.createStatement();
			stmt.executeUpdate(sql);
		}catch(Exception e){
			System.out.println(sql);
			System.out.println(e);
		}
		super.drop();
	}
	public String[][] all() //заполнение массива значениями таблиц
	{
		ArrayList<String[]> result = new ArrayList<String[]>();
		String sql = "SELECT name_coll, start_date, finish_date, desc_coll FROM collection ORDER BY name_coll, start_date, finish_date";
		try{
			Statement stmt = connection.cbase.createStatement(); 
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String[] row = new String[5];
				row[0] = Integer.toString(rs.getRow());
				row[1] = rs.getString(1);
				row[2] = rs.getString(2);
				row[3] = rs.getString(3);
				row[4] = rs.getString(4);
				result.add(row);
			}
		}catch(Exception e){
			System.out.println(sql);
			System.out.println(e);
		}
		String[][] str_result = new String[result.size()][4];
		return result.toArray(str_result);
	}
	public String[] find_by_position(int position) //метод нахождения коллекции по заданной позиции
 	{
 		String[] result = new String[5];
 		result[0] = "";
 		String sql =
 		// "SELECT id_coll, name_coll, start_date, finish_date FROM collection ORDER BY name_coll, start_date, finish_date where rownum <?+1 and ";
		"SELECT id_coll, name_coll, start_date, finish_date, num from (SELECT id_coll, name_coll, start_date, finish_date, rownum as num from (select id_coll, name_coll, where num=?"; 
		try{

			PreparedStatement pstmt = connection.cbase.prepareStatement(sql);

 			((OraclePreparedStatement)pstmt).setInt(1, position );

 			pstmt.execute();
 			ResultSet rs = pstmt.executeQuery();

 			while(rs.next()){
				result[0] = rs.getString(1);
				result[1] = rs.getString(2);
				result[2] = rs.getString(3);
				result[3] = rs.getString(4);
				result[4] = rs.getString(5);

			}
 			pstmt.close();

 		}catch(Exception e){
 			System.out.println(sql);
 			System.out.println(e);
 		}
 		return result;
 	}
 	public void create_one(String[] data) //метод создания клддекции
	{
		String sql = "INSERT INTO collection(id_coll, name_coll, start_date, finish_date,desc_coll) VALUES(co_id_seq.nextval, ?, ?, ?,?)";
		try{
 			PreparedStatement pstmt = connection.cbase.prepareStatement(sql);
 			pstmt.setString(1, data_to_field(data[0]));
 			pstmt.setString(2, data_to_field(data[1]));
 			pstmt.setString(3, data_to_field(data[2]));
 			pstmt.setString(4, data_to_field(data[3]));
			pstmt.executeUpdate();
 			pstmt.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}

 	public void del_one(int position) // метод удаления коллекции
	{
 		//String[] result = new String[4];
 		String sql2="delete from ex where collection_id_coll in (select id_coll from(select id_coll,rownum as num from (select * from collection order by name_coll, start_date, finish_date)) where num = ?)";
 		// result[0] = "";
 		String sql =
 		// "SELECT id_coll, name_coll, start_date, finish_date FROM collection ORDER BY name_coll, start_date, finish_date where rownum <?+1 and ";
		// "delete from collection where id_coll in (select id_coll from (select * from collection order by name_coll, start_date, finish_date) where rownum = ?)";
		" delete from collection where id_coll in (select id_coll from(select id_coll,rownum as num from (select * from collection order by name_coll, start_date, finish_date)) where num = ?)";
		try{ 
			PreparedStatement pstmt1 =
			connection.cbase.prepareStatement(sql2);
			pstmt1.setInt(1, position);
			pstmt1.executeUpdate();
 			pstmt1.close();

 			PreparedStatement pstmt = connection.cbase.prepareStatement(sql);

 			pstmt.setInt(1, position);
 			pstmt.executeUpdate();
 			pstmt.close();

 		}catch(Exception e){
 			System.out.println(sql);
 			System.out.println(e);
 		}

 	}
}