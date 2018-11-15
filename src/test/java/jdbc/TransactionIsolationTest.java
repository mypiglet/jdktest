package jdbc;

import org.testng.annotations.Test;

/**
 * JDBC事务隔离级别;
 * 1、TRANSACTION_NONE（0） 说明不支持事务；
 * 2、TRANSACTION_READ_UNCOMMITTED（1） 说明在提交前一个事务可以看到另一个事务的变化。这样脏读、不可重复的读和虚读都是允许的；
 * 3、TRANSACTION_READ_COMMITTED（2） 说明读取未提交的数据是不允许的。这个级别仍然允许不可重复的读和虚读产生；
 * 4、TRANSACTION_REPEATABLE_READ（4） 说明事务保证能够再次读取相同的数据而不会失败，但虚读仍然会出现；
 * 5、TRANSACTION_SERIALIZABLE（8） 是最高的事务级别，它防止脏读、不可重复的读和虚读；
 * 
 * @author mypiglet
 *
 */
public class TransactionIsolationTest extends ConnectionTest {

	@Test
	public void test(){
		
	}
	
}
