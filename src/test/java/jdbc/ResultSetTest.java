package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * cursor类型: 1、ResultSet.TYPE_FORWARD_ONLY；
 * 默认的cursor类型，仅仅支持向前forward，不支持backforward，random，last，first操作，类似单向链表；
 * TYPE_FORWARD_ONLY类型通常是效率最高最快的cursor类型； 2、ResultSet.TYPE_SCROLL_INSENSITIVE；
 * 支持backforward，random，last，first操作，对其它数据session对选择数据做出的更改是不敏感，不可见的；
 * 3、ResultSet.TYPE_SCROLL_SENSITIVE；
 * 支持backforward，random，last，first操作，对其它数据session对选择数据做出的更改是敏感，可见的；
 * 
 * JDBC对数据库进行数据查询时，数据库会创建查询结果的cache和cursor；
 * 
 * 一、移动游标： 1、void beforeFirst()：把光标放到第一行的前面，这也是光标默认的位置（虚拟位置）； 2、void
 * afterLast()：把光标放到最后一行的后面（虚拟位置）； 3、boolean first()：把光标放到第一行的位置，返回值表示调控光标是否成功；
 * 4、boolean previous():把光标向上挪一行； 5、boolean last():把光标放到最后一行的位置上； 6、boolean
 * next()：把光标向下挪一行； 7、boolean relative(int
 * row):相对位移，当row为正数时，表示向下移动row行，为负数时表示向上移动row行； 8、boolean absolute(int
 * row):绝对位移，把光标移动到指定的行上；
 * 
 * 二、判断游标： 1、boolean isBeforeFirst():当前光标位置是否在第一行的前面； 2、boolean
 * isAfterLast():当前光标位置是否在最后一行的后面； 3、boolean isFirst():当前光标位置是否在第一行上； 4、boolean
 * isLast():当前光标位置是否在最后一行上； 5、int getRow()：返回当前光标的位置；
 * 
 * 三、获取总行数：先执行rs.last()把光标移动到最后一行，再执行rs.getRow()；获得当前光标所在行，可以得到结果集一共有多少行；
 * 
 * 四、获取总列数： 1、先获取结果集的原数据ResultSetMetaData ramd=ra.getMetaData(); 2、获取结果集列数：int
 * len=ramd.getColumnCount(); 3、获取指定列的列名：String name=ramd.getColumnName(int
 * colIndex);
 * 
 * @author mypiglet
 *
 */
public class ResultSetTest extends ConnectionTest {

	/**
	 * MySQL JDBC默认客户端数据接收方式为如下: 默认为从服务器一次取出所有数据放在客户端内存中，fetch
	 * size参数不起作用，当一条SQL返回数据量较大时可能会出现JVM OOM;
	 * 当statement设置以下属性时，采用的是流数据接收方式，每次只从服务器接收部份数据，直到所有数据处理完毕，不会发生JVM OOM:
	 * setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
	 * setFetchSize(Integer.MIN_VALUE);
	 * 
	 * MySQL判断是否开启流式读取结果的方法，有三个条件forward-only，read-only，fatch
	 * size是Integer.MIN_VALUE；
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test(timeOut = 1000)
	public void test() throws SQLException, ClassNotFoundException {

		Connection connection = this.getNewConnection();
		Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

		// 这里要必须设置成Integer.MIN_VALUE才能开启流式读取结果
		statement.setFetchSize(Integer.MIN_VALUE);
		ResultSet rs = statement.executeQuery("select * from demo_news");
		while (rs.next()) {
			System.out.println("只输出第一条：" + rs.getString("title"));
			break;
		}
	}

	/**
	 * TYPE_SCROLL_INSENSITIVE验证
	 */
	@Test
	public void test2() throws SQLException, ClassNotFoundException {

		Connection connection = this.getNewConnection();
		Connection connection2 = this.getNewConnection();
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.execute("update demo_user set password='654321' where id=1");
		ResultSet rs = statement.executeQuery("select * from demo_user where id=1");

		Statement statement2 = connection2.createStatement();
		statement2.execute("update demo_user set password='111111' where id=1");

		while (rs.next()) {
			System.out.println("可以输出旧值：" + rs.getString("password"));
			Assert.assertEquals(rs.getString("password"), "654321");
		}

	}

	/**
	 * TYPE_SCROLL_SENSITIVE验证【没起作用】；
	 */
	@Test(enabled = false)
	public void test3() throws ClassNotFoundException, SQLException {
		Connection connection = this.getNewConnection();
		Connection connection2 = this.getNewConnection();
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.execute("update demo_user set password='654321' where id=1");
		ResultSet rs = statement.executeQuery("select * from demo_user where id=1");

		Statement statement2 = connection2.createStatement();
		statement2.execute("update demo_user set password='111111' where id=1");

		while (rs.next()) {
			System.out.println("可以输出旧值：" + rs.getString("password"));
			Assert.assertEquals(rs.getString("password"), "111111");
		}

	}

	@Test(enabled = false)
	public void test4() throws ClassNotFoundException, SQLException {

		Connection connection = this.getNewConnection();
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = statement.executeQuery("select * from demo_user where id=2");
		while (rs.next()) {
			rs.updateString("password", "666666");
			rs.updateRow();
			Assert.assertEquals(rs.getString("password"), "666666");
		}

	}

	/**
	 * 验证【没起作用】
	 * 可保持性（Holdability）; 指当ResultSet的结果被提交时，是被关闭还是不被关闭;
	 * 在JDBC3.0中，我们可以设置ResultSet是否关闭; 可保持性级别：
	 * 1、ResultSet.HOLD_CURSORS_OVER_COMMIT（1）:表示修改提交时，不关闭数据库;
	 * 2、ResultSet.CLOSE_CURSORS_AT_COMMIT（2）：表示修改提交时ResultSet关闭;
	 * 
	 */
	@Test(enabled = false)
	public void holdabilityTest() throws SQLException, ClassNotFoundException {

		Connection connection = this.getNewConnection();
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY,
				ResultSet.HOLD_CURSORS_OVER_COMMIT);
		ResultSet rs = statement.executeQuery("select * from demo_user where id=1");
		System.out.println("rs：" + rs.isClosed());
		ResultSet rs2 = statement.executeQuery("select * from demo_user where id=2");
		System.out.println("rs：" + rs.isClosed());
		System.out.println("rs2：" + rs2.isClosed());

		while (rs2.next()) {
			System.out.println(rs2.getString("password"));
		}
		while (rs.next()) {
			System.out.println(rs.getString("password"));
		}

	}

}
