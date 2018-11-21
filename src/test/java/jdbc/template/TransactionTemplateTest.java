package jdbc.template;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import jdbc.pool.DataSourceTest;

/**
 * 
 * 
 * @author mypiglet
 *
 */
public class TransactionTemplateTest extends DataSourceTest {

	private TransactionTemplate transactionTemplate;
	private JdbcOperations jdbc;

	@BeforeTest
	public void transactionTemplateTest() {
		jdbc = new JdbcTemplate(this.getDataSource());
		this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(this.getDataSource()));
		
		Assert.assertNotNull(jdbc);
		Assert.assertNotNull(transactionTemplate);
	}

	@Test
	public void test() {

		final String sql1 = "UPDATE demo_user SET NAME='pp21' WHERE ID='1'";
		final String sql2 = "UPDATE demo_user SET NAME='pp31' WHERE ID='2'";

		this.transactionTemplate.execute((status) -> {
			this.jdbc.execute(sql1);
			this.jdbc.execute(sql2);

			return null;
		});
		
		System.out.println("事务结束！");

	}

}
