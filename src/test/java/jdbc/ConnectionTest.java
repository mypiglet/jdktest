package jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * jdbc设计背景原理： 1.JVM（Java虚拟机）将系统分配的内存区域分为三块（堆区、栈区、方法区）; 2.类的加载：当使用new
 * 关键字新建一个对象，或使用Class.forName()加载指定类时，虚拟机读取类（.class）的字节码文件并加载到方法区。<由类加载器完成>;
 * 3.类在加载时，静态成员会被执行，包括静态成员被赋值，静态代码块被执行;
 * 【任何堆区的Java对象都有一个指针指向方法区中的class对象（每个类只有一个），即方法区共享了类的方法】
 * 
 * 设计描述：
 * 1.sun公司制定接口标准：Connection接口和DriverManager类（Connection接口提供操作数据库的抽象方法，待数据库厂商实现；
 * DriverManager类提供给数据库厂商注册连接和获取连接的方法） ；
 * 2.数据库厂商实现Connection接口中的方法（用来定义具体的数据库操作）；
 * 3.数据库厂商自定义驱动文件Driver类，将2中的连接注册到SUN提供的DriverManager中（此过程在静态代码块中，确保类被加载时即可执行）；
 * 4.程序员通过Class.for("");加载驱动文件，并获取连接，进而对数据库进行操作；
 * 
 * @author mypiglet
 *
 */
public class ConnectionTest {

	private Connection connection;

	@BeforeTest
	public void init() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = getNewConnection();
	}

	@Test(enabled = false)
	public void connectionTest() throws SQLException {

		com.mysql.jdbc.Connection mysqlConnection = connection.unwrap(com.mysql.jdbc.Connection.class);
		Assert.assertNotNull(mysqlConnection);
		Assert.assertNotNull(connection);

		DatabaseMetaData metaData = connection.getMetaData();
		Assert.assertNotNull(metaData);

		System.out.println("JDBC默认值--------------------------------------");
		System.out.println("Catalog:" + connection.getCatalog());
		System.out.println("AutoCommit:" + connection.getAutoCommit());
		System.out.println("Closed:" + connection.isClosed());
		System.out.println("MetaData:" + connection.getMetaData());
		System.out.println("TransactionIsolation:" + connection.getTransactionIsolation());
		System.out.println("Warnings:" + connection.getWarnings());
		System.out.println("ReadOnly:" + connection.isReadOnly());
		System.out.println("TypeMap:" + connection.getTypeMap());
		System.out.println("Holdability:" + connection.getHoldability());
		System.out.println("ClientInfo:" + connection.getClientInfo());
		System.out.println("Schema:" + connection.getSchema());
		System.out.println("----------------------------------------------");

		// Catalog:spring_rest
		// AutoCommit:true
		// Closed:false
		// MetaData:com.mysql.jdbc.JDBC4DatabaseMetaData@4d50efb8
		// TransactionIsolation:4
		// Warnings:null
		// ReadOnly:false
		// TypeMap:{}
		// Holdability:2
		// ClientInfo:{}
		// Schema:null

	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * 在PreparedStatement中开启预编译功能； 设置MySQL连接URL参数：useServerPrepStmts=true；
	 * 
	 * 当使用不同的PreparedStatement对象来执行相同的SQL语句时，还是会出现编译两次的现象，这是因为驱动没有缓存编译后的函数key，
	 * 导致二次编译； 如果希望缓存编译后函数的key，那么就要设置cachePrepStmts参数为true；
	 * 
	 * MySql的JDBC连接的url中要加rewriteBatchedStatements参数，并保证5.1.13以上版本的驱动，
	 * 才能实现高性能的批量插入；
	 */
	public Connection getNewConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/spring_rest?useUnicode=true&characterEncoding=UTF-8&useSSL=false&rewriteBatchedStatements=true&useServerPrepStmts=true&cachePrepStmts=true",
				"root", "123456");
	}

	/*
	 * PreparedStatement的预编译是数据库进行的，
	 * 编译后的函数key是缓存在PreparedStatement中的，编译后的函数是缓存在数据库服务器中的。预编译前有检查sql语句语法是否正确的操作
	 * 。只有数据库服务器支持预编译功能时，JDBC驱动才能够使用数据库的预编译功能，否则会报错。预编译在比较新的JDBC驱动版本中默认是关闭的，
	 * 需要配置连接参数才能够打开。在已经配置好了数据库连接参数的情况下，Statement对于MySQL数据库是不会对编译后的函数进行缓存的，
	 * 数据库不会缓存函数，Statement也不会缓存函数的key，所以多次执行相同的一条sql语句的时候，还是会先检查sql语句语法是否正确，
	 * 然后编译sql语句成函数，最后执行函数。 对于PreparedStatement在设置参数的时候会对参数进行转义处理。
	 * 因为PreparedStatement已经对sql模板进行了编译，并且存储了函数，
	 * 所以PreparedStatement做的就是把参数进行转义后直接传入参数到数据库，然后让函数执行。
	 * 这就是为什么PreparedStatement能够防止sql注入攻击的原因了。
	 * PreparedStatement的预编译还有注意的问题，在数据库端存储的函数和在PreparedStatement中存储的key值，
	 * 都是建立在数据库连接的基础上的，如果当前数据库连接断开了，数据库端的函数会清空，
	 * 建立在连接上的PreparedStatement里面的函数key也会被清空，各个连接之间的预编译都是互相独立的。
	 */

}
