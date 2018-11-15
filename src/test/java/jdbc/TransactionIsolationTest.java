package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.Test;

/**
 * JDBC事务隔离级别; 1、TRANSACTION_NONE（0） 说明不支持事务； 2、TRANSACTION_READ_UNCOMMITTED（1）
 * 说明在提交前一个事务可以看到另一个事务的变化。这样脏读、不可重复的读和虚读都是允许的； 3、TRANSACTION_READ_COMMITTED（2）
 * 说明读取未提交的数据是不允许的。这个级别仍然允许不可重复的读和虚读产生； 4、TRANSACTION_REPEATABLE_READ（4）
 * 说明事务保证能够再次读取相同的数据而不会失败，但虚读仍然会出现； 5、TRANSACTION_SERIALIZABLE（8）
 * 是最高的事务级别，它防止脏读、不可重复的读和虚读；
 * 
 * @author mypiglet
 *
 */
public class TransactionIsolationTest extends ConnectionTest {

	/**
	 * 并发事务问题，两类是更新问题三类是读问题:
	 * 1、脏读（dirty read）：读到另一个事务的未提交新数据，即读取到了脏数据；
	 * 2、不可重复读（unrepeatable）：对同一记录的两次读取不一致，因为另一事务对该记录做了修改；
	 * 3、幻读（虚读）（phantom read）：对同一张表的两次查询不一致，因为另一事务插入了一条记录；
	 * 
	 * REPEATABLE READ(可重复读)
	 * 防止脏读和不可重复读，不能处理幻读；
	 * 性能比SERIALIZABLE好；
	 */
	@Test(enabled = false)
	public void test() throws ClassNotFoundException, SQLException {
		transactionIsolationTest(Connection.TRANSACTION_REPEATABLE_READ);
	}
	
	@Test(timeOut = 1000,enabled = false)
	public void test2() throws ClassNotFoundException, SQLException {
		transactionIsolationTest(Connection.TRANSACTION_SERIALIZABLE);
	}
	
	/**
	 * READ COMMITTED(读已提交数据)
	 * 防止脏读，不能处理不可重复读和幻读；
	 * 性能比REPEATABLE READ好；
	 */
	@Test(enabled = false)
	public void test3() throws ClassNotFoundException, SQLException {
		transactionIsolationTest(Connection.TRANSACTION_READ_COMMITTED);
	}
	
	/**
	 * READ UNCOMMITTED(读未提交数据)
	 * 可能出现任何事物并发问题，什么都不处理;
	 * 性能最好;
	 */
	@Test
	public void test4() throws ClassNotFoundException, SQLException {
		transactionIsolationTest(Connection.TRANSACTION_READ_UNCOMMITTED);
	}
	
	private void transactionIsolationTest(int transaction) throws ClassNotFoundException, SQLException{
		Connection connection = this.getNewConnection();
		connection.setAutoCommit(false);
		//默认事务隔离级别，可重复读
		connection.setTransactionIsolation(transaction);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM demo_user");
		while(rs.next()){
			System.out.println(rs.getString("name"));
		}
		rs.close();
		statement.close();
		
		insertUser();
		
		System.out.println("第二次读：-----------------------------------");
		Statement statement2 = connection.createStatement();
		ResultSet rs2 = statement2.executeQuery("SELECT * FROM demo_user");
		while(rs2.next()){
			System.out.println(rs2.getString("name"));
		}
		rs2.close();
		statement2.close();
		connection.close();
	}
	
	private void insertUser() throws ClassNotFoundException, SQLException{
		Connection connection = this.getNewConnection();
		Statement statement = connection.createStatement();
		statement.executeUpdate("INSERT INTO demo_user(name,password) VALUES('gg','nn')");
		statement.close();
		connection.close();
	}

	/**
	 * JDBC中的事务:
	 * Connection的三个方法与事务有关：
	 * 1、setAutoCommit（boolean）:设置是否为自动提交事务，如果true（默认值为true）表示自动提交，也就是每条执行的SQL语句都是一个单独的事务，如果设置为false，那么相当于开启了事务了；con.setAutoCommit(false) 表示开启事务；
	 * 2、commit（）：提交结束事务；
	 * 3、rollback（）：回滚结束事务。
	 * 
	 * 示例：
	 * try{
	 *     con.setAutoCommit(false);//开启事务
	 *     ......
	 *     con.commit();//try的最后提交事务      
	 * } catch() {
	 *     con.rollback();//回滚事务
	 * }
	 */
	@Test
	public void commonTransactionTest() throws ClassNotFoundException, SQLException {

		Connection connection = this.getNewConnection();
		connection.setAutoCommit(false);
		Statement statement = connection.createStatement();

		try {
			statement.executeUpdate("update demo_user set password='999999' where id=2");
			statement.executeUpdate("update demo_user2 set password='999999' where id=1");
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			statement.close();
			connection.close();
		}

	}

}
