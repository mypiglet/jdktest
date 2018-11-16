package jdbc.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import jdbc.pool.DataSourceTest;

/**
 * JdbcTemplate基础方法测试验证
 * 
 * @author mypiglet
 *
 */
public class JdbcTemplateTest extends DataSourceTest {

	private JdbcOperations jdbc;

	private final static String SQL_UPDATE_USER = "UPDATE demo_user SET NAME='pp2' WHERE PASSWORD='123'";
	private final static String SQL_SELECT_USER = "SELECT * FROM demo_user WHERE ID=?";

	@BeforeTest
	public void test() {
		jdbc = new JdbcTemplate(this.getDataSource());
		Assert.assertNotNull(jdbc);
	}

	@Test(enabled = false)
	public void executeTest() {

		final int id = 2;

		User user = this.jdbc.execute(new ConnectionCallback<User>() {

			@Override
			public User doInConnection(Connection con) throws SQLException, DataAccessException {
				PreparedStatement ps = con.prepareCall(SQL_SELECT_USER);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				User user = null;
				while (rs.next()) {
					user = new User();
					user.setId(rs.getInt("ID"));
					user.setName(rs.getString("NAME"));
					user.setPassword(rs.getString("PASSWORD"));
					user.setCreated(rs.getDate("CREATED"));
				}
				return user;
			}

		});

		Assert.assertNotNull(user);
		Assert.assertEquals(user.getId(), id);

	}

	@Test(enabled = false)
	public void executeTest2() {

		List<User> userList = this.jdbc.execute(new StatementCallback<List<User>>() {

			@Override
			public List<User> doInStatement(Statement stat) throws SQLException, DataAccessException {
				ResultSet rs = stat.executeQuery("SELECT * FROM demo_user WHERE PASSWORD='123'");
				List<User> userList = new ArrayList<User>();
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getInt("ID"));
					user.setName(rs.getString("NAME"));
					user.setPassword(rs.getString("PASSWORD"));
					user.setCreated(rs.getDate("CREATED"));
					userList.add(user);
				}
				return userList;
			}

		});

		Assert.assertEquals(userList.size() > 0, true);
		System.out.println(userList.size());

	}

	@Test(enabled = false)
	public void executeTest3() {
		this.jdbc.execute(SQL_UPDATE_USER);
	}

	@Test(enabled = false)
	public void executeTest4() {

		User user = this.jdbc.execute(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement("SELECT * FROM demo_user WHERE ID=2");
			}

		}, new PreparedStatementCallback<User>() {

			@Override
			public User doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ResultSet rs = ps.executeQuery();
				User user = null;
				while (rs.next()) {
					user = new User();
					user.setId(rs.getInt("ID"));
					user.setName(rs.getString("NAME"));
					user.setPassword(rs.getString("PASSWORD"));
					user.setCreated(rs.getDate("CREATED"));
				}
				return user;
			}

		});
		
		Assert.assertNotNull(user);
		Assert.assertEquals(user.getId(), 2);

	}

	@Test(enabled = false)
	public void queryTest() {

		final int id = 2;
		User user = this.jdbc.query("SELECT * FROM demo_user WHERE ID=" + id, new ResultSetExtractor<User>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				User user = null;
				while (rs.next()) {
					user = new User();
					user.setId(rs.getInt("ID"));
					user.setName(rs.getString("NAME"));
					user.setPassword(rs.getString("PASSWORD"));
					user.setCreated(rs.getDate("CREATED"));
				}
				return user;
			}

		});

		Assert.assertNotNull(user);
		Assert.assertEquals(user.getId(), id);

	}

	@Test(enabled = false)
	public void queryTest2() {

		this.jdbc.query("SELECT * FROM demo_user WHERE PASSWORD=123", new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.println(rs.getString("NAME"));
			}

		});

	}

	@Test(enabled = false)
	public void queryTest3() {

		List<User> userList = this.jdbc.query("SELECT * FROM demo_user WHERE PASSWORD=123", new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				System.out.println(rowNum);
				User user = new User();
				user.setId(rs.getInt("ID"));
				user.setName(rs.getString("NAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setCreated(rs.getDate("CREATED"));
				return user;
			}

		});

		Assert.assertEquals(userList.size() > 0, true);
		System.out.println(userList.size());

	}
	
	@Test(enabled = true)
	public void queryTest4() {
		
		List<User> userList = this.jdbc.query("SELECT * FROM demo_user WHERE PASSWORD=?", new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, "123");
			}
			
		}, new RowMapper<User>(){

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("ID"));
				user.setName(rs.getString("NAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setCreated(rs.getDate("CREATED"));
				return user;
			}
			
		});
		
		Assert.assertEquals(userList.size() > 0, true);
		System.out.println(userList.size());
		
	}

	@Test(enabled = false)
	public void queryForObjectTest() {

		User user = this.jdbc.queryForObject("SELECT * FROM demo_user WHERE ID=2", new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				System.out.println(rowNum);
				User user = new User();
				user.setId(rs.getInt("ID"));
				user.setName(rs.getString("NAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setCreated(rs.getDate("CREATED"));
				return user;
			}

		});

		Assert.assertNotNull(user);
		Assert.assertEquals(user.getId(), 2);

	}

	@Test(enabled = false)
	public void queryForObjectTest2() {
		String name = this.jdbc.queryForObject("SELECT NAME FROM demo_user WHERE ID=2", String.class);
		Assert.assertNotNull(name);
	}

	@Test(enabled = false)
	public void queryForMapTest() {
		Map<String, Object> map = this.jdbc.queryForMap("SELECT * FROM demo_user WHERE ID=2");
		Assert.assertNotNull(map);
	}

	@Test(enabled = false)
	public void queryForListTest() {
		List<String> userList = this.jdbc.queryForList("SELECT NAME FROM demo_user WHERE PASSWORD=123", String.class);
		Assert.assertNotNull(userList);
	}

	@Test(enabled = false)
	public void queryForListTest2() {
		List<Map<String, Object>> userList = this.jdbc.queryForList("SELECT * FROM demo_user WHERE PASSWORD=123");
		Assert.assertNotNull(userList);
	}

	@Test(enabled = false)
	public void queryForRowSetTest() {
		SqlRowSet rowSet = this.jdbc.queryForRowSet("SELECT * FROM demo_user WHERE PASSWORD=123");
		Assert.assertNotNull(rowSet);
		while (rowSet.next()) {
			System.out.println(rowSet.getString("ID"));
		}
	}

	@SuppressWarnings("unused")
	private static class User {

		/**
		 * 编号
		 */
		private int id;
		/**
		 * 用户名
		 */
		private String name;
		/**
		 * 用户密码
		 */
		private String password;
		/**
		 * 创建日期
		 */
		private Date created;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Date getCreated() {
			return created;
		}

		public void setCreated(Date created) {
			this.created = created;
		}
	}

}
