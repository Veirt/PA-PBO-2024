package pa;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
	public static Connection con;

	public DB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/anime99", "root", "");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}