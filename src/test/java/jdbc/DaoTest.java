package jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 基础操作示例
 * 
 * Statement 和 PreparedStatement之间的关系和区别； 关系：PreparedStatement继承自Statement,都是接口；
 * 区别：PreparedStatement可以使用占位符，是预编译的，批处理比Statement效率高；
 * 
 * @author mypiglet
 *
 */
public class DaoTest extends ConnectionTest {

	private final static String SQL_INSERT_USER = "INSERT INTO demo_user(NAME,PASSWORD) VALUES('ee','123')";
	private final static String SQL_UPDATE_USER = "UPDATE demo_user SET NAME='pp' WHERE PASSWORD='123'";
	private final static String SQL_SELECT_USER = "SELECT * FROM demo_user WHERE PASSWORD='123'";
	private final static String SQL_DELETE_USER = "DELETE FROM demo_user WHERE PASSWORD=?";

	@Test(enabled = false)
	public void insertTest() throws ClassNotFoundException, SQLException {

		Connection con = this.getNewConnection();
		Statement statement = con.createStatement();
		boolean b = statement.execute(SQL_INSERT_USER);
		Assert.assertEquals(b, false);

		// 返回false代表没有ResultSet
		boolean b2 = statement.execute(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
		Assert.assertEquals(b2, false);
		ResultSet rs = statement.getResultSet();
		Assert.assertNull(rs);

		ResultSet rs2 = statement.getGeneratedKeys();
		while (rs2.next()) {
			System.out.println(rs2.getInt(1));
		}

		boolean b3 = statement.execute(SQL_INSERT_USER, new String[] { "ID" });
		Assert.assertEquals(b3, false);
		ResultSet rs3 = statement.getGeneratedKeys();
		while (rs3.next()) {
			System.out.println(rs3.getInt(1));
		}

	}

	@Test(enabled = false)
	public void deleteTest() throws ClassNotFoundException, SQLException {

		Connection con = this.getNewConnection();
		PreparedStatement statement = con.prepareStatement(SQL_DELETE_USER);
		statement.setString(1, "123");
		int ii = statement.executeUpdate();
		System.out.println(ii);
	}

	@Test(enabled = false)
	public void updateTest() throws ClassNotFoundException, SQLException {

		Connection con = this.getNewConnection();
		Statement statement = con.createStatement();
		// 返回影响的行数
		int ii = statement.executeUpdate(SQL_UPDATE_USER);
		System.out.println(ii);

	}

	@Test(enabled = false)
	public void queryTest() throws ClassNotFoundException, SQLException {

		Connection con = this.getNewConnection();
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery(SQL_SELECT_USER);
		while (rs.next()) {
			System.out.println(rs.getInt("ID"));
		}
	}

	/**
	 * 以下sql语句在oracle数据库用，不能应用在mysql
	 */
	@Test(enabled = false)
	public void procedureTest() throws ClassNotFoundException, SQLException {

		final String sql = "BEGIN UPDATE demo_user SET NAME='jj' WHERE ID=? RETURNING NAME INTO ?;END;";
		Connection con = this.getNewConnection();
		CallableStatement call = con.prepareCall(sql);
		call.setInt(1, 2);
		call.registerOutParameter(2, JDBCType.INTEGER);
		call.executeUpdate();
		System.out.println(call.getInt(1));

	}

}
