package jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.testng.annotations.Test;

/**
 * MySQL使用load data local infile 从文件中导入数据比executeBatch语句要快；
 * 使用setLocalInfileInputStream方法，会直接忽略掉文件名称，而直接将IO流导入到数据库中；
 * 
 * @author MyPiglet
 *
 */
public class BatchTest extends ConnectionTest {

	/**
	 * MySql的JDBC连接的url中要加rewriteBatchedStatements参数，并保证5.1.13以上版本的驱动，
	 * 才能实现高性能的批量插入； 优化插入性能，用JDBC的addBatch方法，但是注意在连接字符串加上面写的参数； 例如： String
	 * connectionUrl=
	 * "jdbc:mysql://127.0.0.1:3306/spring_rest?useUnicode=true&characterEncoding=UTF-8&useSSL=false&rewriteBatchedStatements=true"；
	 */
	@Test(enabled = false)
	public void commonBatchTest() throws SQLException {

		String sql = "INSERT INTO demo_news(title,content) VALUES(?,?)";
		PreparedStatement statement = this.getConnection().prepareStatement(sql);
		//设置手动提交
		this.getConnection().setAutoCommit(false);
		int len = 100000;
		long beginTime = System.currentTimeMillis();
		for (int i = 0; i < len; i++) {
			statement.setString(1, "tt");
			statement.setString(2, "55");
			statement.addBatch();
		}
		statement.executeBatch();
		this.getConnection().commit();
		//statement.clearBatch();
		long endTime = System.currentTimeMillis();
		System.out.println("commonBatchTest:" + (endTime - beginTime));
	}

	@Test(enabled = false)
	public void fastBatchtest() {

		String testSql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE demo_news (title,content)";
		InputStream dataStream = getTestDataInputStream();
		try {
			long beginTime = System.currentTimeMillis();
			int rows = bulkLoadFromInputStream(testSql, dataStream);
			long endTime = System.currentTimeMillis();
			System.out
					.println("importing " + rows + " rows data into mysql and cost " + (endTime - beginTime) + " ms!");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public int bulkLoadFromInputStream(String loadDataSql, InputStream dataStream) throws SQLException {
		if (dataStream == null) {
			System.out.println("InputStream is null ,No data is imported");
			return 0;
		}
		PreparedStatement statement = this.getConnection().prepareStatement(loadDataSql);
		int result = 0;
		if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {
			com.mysql.jdbc.PreparedStatement mysqlStatement = statement.unwrap(com.mysql.jdbc.PreparedStatement.class);
			mysqlStatement.setLocalInfileInputStream(dataStream);
			result = mysqlStatement.executeUpdate();
		}
		return result;
	}

	/**
	 * 字段默认用制表符隔开，每条记录用换行符隔开
	 */
	public InputStream getTestDataInputStream() {
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i <= 100; i++) {
			for (int j = 0; j < 10000; j++) {

				builder.append("hh");
				builder.append("\t");
				builder.append("cc");
				builder.append("\n");
			}
		}
		byte[] bytes = builder.toString().getBytes();
		InputStream is = new ByteArrayInputStream(bytes);
		return is;
	}

}
