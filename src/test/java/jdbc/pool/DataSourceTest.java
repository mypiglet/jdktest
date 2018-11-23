package jdbc.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * HikariCP（希卡里）数据库连接池，号称性能最好；
 * 
 * HikariCP所做的一些优化： 1、字节码精简：优化代码，直到编译后的字节码最少，这样，CPU缓存可以加载更多的程序代码；
 * 2、优化代理和拦截器：减少代码，例如HikariCP的Statement proxy只有100行代码，只有BoneCP的十分之一；
 * 3、自定义数组类型（FastStatementList）代替ArrayList：避免每次get()调用都要进行range
 * check，避免调用remove()时的从头到尾的扫描； 4、自定义集合类型（ConcurrentBag）：提高并发读写的效率；
 * 5、其他针对BoneCP缺陷的优化，比如对于耗时超过一个CPU时间片的方法调用的研究（但没说具体怎么优化）；
 * 
 * @author mypiglet
 *
 */
public class DataSourceTest {

	private DataSource dataSource;
	private String driverClassName = "com.mysql.jdbc.Driver";
	private String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/spring_rest?useUnicode=true&characterEncoding=UTF-8&useSSL=false&useServerPrepStmts=true&cachePrepStmts=true";
	private String username = "root";
	private String password = "123456";

	@BeforeTest(enabled = false)
	public void test() throws SQLException {

		String path = DataSourceTest.class.getResource("/").toString().replaceAll("file:", "");
		// Thread.currentThread().getContextClassLoader().getResource("/").getPath();
		System.setProperty("hikaricp.configurationFile", path + "hikari.properties");
		dataSource = new HikariDataSource();

	}

	@BeforeTest(enabled = false)
	public void test2() throws SQLException {

		Properties pro = new Properties();
		pro.setProperty("driverClassName", driverClassName);
		pro.setProperty("jdbcUrl", jdbcUrl);
		pro.setProperty("username", username);
		pro.setProperty("password", password);
		HikariConfig config = new HikariConfig(pro);
		dataSource = new HikariDataSource(config);

	}

	@BeforeTest
	public void test3() throws SQLException {

		HikariConfig config = new HikariConfig();
		config.setDriverClassName(driverClassName);
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(username);
		config.setPassword(password);
		config.setMinimumIdle(10);
		config.setMaximumPoolSize(20);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		dataSource = new HikariDataSource(config);

	}

	@Test
	public void connectionTest() throws SQLException {
		Connection conn = dataSource.getConnection();
		Assert.assertNotNull(conn);
		Assert.assertEquals(conn.isClosed(), false);
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
