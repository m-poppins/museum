import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PhonesTable extends DBTableWorker {
	protected String table_name()
	{
		return "ex";
	}
	public String[][] all_by_person_id(int person_id)
	{
		ArrayList<String[]> result = new ArrayList<String[]>();
		String sql = "SELECT collection_id_coll,desc_ex,insurance_val,century,collection_id_coll FROM ex WHERE collection_id_coll = ? ORDER BY desc_ex";

		try
		{
			PreparedStatement pstmt = connection.cbase.prepareStatement(sql);
			pstmt.setInt(1, person_id);
			pstmt.execute();
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String[] row = new String[5];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getString(4);
				row[4] = rs.getString(5);
				result.add(row);
			}
			pstmt.close();
		}catch(Exception e){
			System.out.println(sql);
			System.out.println(e);
		}
		String[][] str_result = new String[result.size()][4];
		return result.toArray(str_result);
	}


	public String[][] col_id_del (int person_id)
	{
		ArrayList<String[]> result = new ArrayList<String[]>();
		String sql = "delete FROM collection WHERE collection_id_coll = ? ORDER BY desc_ex";
		try{
			PreparedStatement pstmt = connection.cbase.prepareStatement(sql);
			pstmt.setInt(1, person_id);
			pstmt.execute();
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String[] row = new String[2];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				result.add(row);
			}
			pstmt.close();
		}catch(Exception e){ 
			System.out.println(sql);
			System.out.println(e);
		}
		String[][] str_result = new String[result.size()][4];
		return result.toArray(str_result);
	}

	protected String[] column_definitions()
	{
		return new String[]
		{"id_ex NUMBER(38)" ,
		" desc_ex VARCHAR2(1000 CHAR)",
		"insurance_val NUMBER(30,2)",
		"century NUMBER(2)",
		"collection_id_coll NUMBER(38) NOT NULL",
		"EX_IN_HALL NUMBER(38) not null"
		};
	}
	public String[] column_names()
	{
		return new String[]
		{"id_ex",
		"desc_ex"};
	}
	public void create_one_ex(String[] data) //метод создания экспоната
	{
		String sql = "INSERT INTO ex(id_ex, desc_ex, insurance_val, century,collection_id_coll,ex_in_hall) VALUES(id_ex_seq.nextval, ?, ?, ?,?,?)";
		try{
			PreparedStatement pstmt = connection.cbase.prepareStatement(sql);
			pstmt.setString(1, data_to_field(data[0]));
			pstmt.setString(2, data_to_field(data[1]));
			pstmt.setString(3, data_to_field(data[2]));
			pstmt.setString(4, data_to_field(data[3]));
			pstmt.setString(5, data_to_field(data[4]));
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public void del_one_ex(int position,int b) // метод удаления экспоната
	{
	//String[] result = new String[4];
	// result[0] = "";
		String sql =
	// "SELECT id_coll, name_coll, start_date, finish_date FROM collection ORDER BY name_coll, start_date, finish_date where rownum <?+1 and ";
	// "delete from collection where id_coll in (select id_coll from (select * from collection order by name_coll, start_date, finish_date) where rownum = ?)";
	// " delete from collection where id_coll in (select id_coll from(select id_coll,rownum as num from (select * from collection order by name_coll, start_date, finish_date)) where num = ?)";
	" delete from ex where id_ex in (select id_ex from(select id_ex,rownum as num,collection_id_coll from (select * from ex order by desc_ex) where collection_id_coll=?) where num = ?)";
		try{
			PreparedStatement pstmt = connection.cbase.prepareStatement(sql);
			pstmt.setInt(1, b);
			pstmt.setInt(2, position);
			pstmt.executeUpdate();
			pstmt.close();

		}catch(Exception e){
		System.out.println(sql);
		System.out.println(e);
		}

	}
}
