package jdbc.template;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import jdbc.pool.DataSourceTest;

/**
 * 常用简化代码示例
 * 
 * @author mypiglet
 *
 */
public class CodeAutoTemplateTest extends DataSourceTest {

	private JdbcOperations jdbc;
	private NamedParameterJdbcOperations namedJdbc;

	@BeforeTest
	public void test() {
		jdbc = new JdbcTemplate(this.getDataSource());
		namedJdbc = new NamedParameterJdbcTemplate(this.getDataSource());
		Assert.assertNotNull(jdbc);
		Assert.assertNotNull(namedJdbc);
	}

	/*
	 * 返回结果映射简化
	 */
	@Test(enabled = false, priority = 1)
	public void codeAutoTest2() {

		List<User> list = this.jdbc.query("SELECT * FROM demo_user", BeanPropertyRowMapper.newInstance(User.class));
		Assert.assertEquals(list.size() > 0, true);
		System.out.println(list.size());

	}

	@Test(enabled = false)
	public void codeAutoTest3() {

		List<User> list = this.jdbc.query("SELECT * FROM demo_user WHERE ID=?", new Object[] { 2 },
				BeanPropertyRowMapper.newInstance(User.class));
		Assert.assertEquals(list.size() > 0, true);
		System.out.println(list.size());

	}

	@Test(enabled = true)
	public void codeAutoTest4() {

		User user = new User();
		user.setName("asg");
		user.setPassword("5678");
		int n = this.namedJdbc.update("INSERT INTO demo_user(NAME,PASSWORD) VALUES(:name,:password)",
				new BeanPropertySqlParameterSource(user));

		System.out.println(n);

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
