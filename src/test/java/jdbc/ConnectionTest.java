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

	@Test
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
	
	public Connection getNewConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/spring_rest?useUnicode=true&characterEncoding=UTF-8&useSSL=false&rewriteBatchedStatements=true", "root",
				"123456");
	}

}
