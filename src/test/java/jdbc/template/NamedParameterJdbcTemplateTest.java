package jdbc.template;

import java.sql.JDBCType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import jdbc.pool.DataSourceTest;

/**
 * NamedParameterJdbcTemplate基础方法验证
 * 
 * @author mypiglet
 *
 */
public class NamedParameterJdbcTemplateTest extends DataSourceTest {

	private NamedParameterJdbcOperations namedJdbc;

	@BeforeTest
	public void test() {
		namedJdbc = new NamedParameterJdbcTemplate(this.getDataSource());
		Assert.assertNotNull(namedJdbc);
	}

	@Test(enabled = false)
	public void namedJdbcTest2() {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("NAME", "sss");
		paramMap.put("PASSWORD", "234");
		int n = this.namedJdbc.update("INSERT INTO demo_user(NAME,PASSWORD) VALUES(:NAME,:PASSWORD)", paramMap);

		System.out.println(n);

	}

	@Test(enabled = true)
	public void namedJdbcTest3() {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource = mapSqlParameterSource.addValue("NAME", "qqq", JDBCType.VARCHAR.getVendorTypeNumber());
		mapSqlParameterSource = mapSqlParameterSource.addValue("PASSWORD", "333",
				JDBCType.VARCHAR.getVendorTypeNumber());
		int n = this.namedJdbc.update("INSERT INTO demo_user(NAME,PASSWORD) VALUES(:NAME,:PASSWORD)",
				mapSqlParameterSource);

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
