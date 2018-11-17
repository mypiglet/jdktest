package log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

/**
 * Log4j是Apache的一个开放源代码项目，本测试基于log4j2;
 * 
 * 1、可以输出的目的地： 控制台、文件、GUI组件、甚至是套接口服务器、NT的事件记录器、UNIX Syslog守护进程等；
 * 2、可以控制每一条日志的输出格式； 3、可以定义每一条日志信息的级别；
 * 
 * 级别（trace<debug<info<warn<error<fatal - 大于等于设置级别的日志都会输出）： trace：
 * 是追踪，就是程序推进以下，你就可以写个trace输出，所以trace应该会特别多，不过没关系，我们可以设置最低日志级别不让他输出。 debug：
 * 调试么，我一般就只用这个作为最低级别，trace压根不用。是在没办法就用eclipse或者idea的debug功能就好了么。 info：
 * 输出一下你感兴趣的或者重要的信息，这个用的最多了。 warn：
 * 有些信息不是错误信息，但是也要给程序员的一些提示，类似于eclipse中代码的验证不是有error和warn（不算错误但是也请注意，
 * 比如以下depressed的方法）。 error： 错误信息。用的也比较多。 fatal：
 * 级别比较高了。重大错误，这种级别你可以直接停止程序了，是不应该出现的错误么！不用那么紧张，其实就是一个程度的问题。
 * 
 * log4j2底层采用Disruptor高性能框架；
 * 
 * @author mypiglet
 *
 */
public class log4jTest {

	private final static Logger logger = LogManager.getLogger(log4jTest.class);

	/**
	 * 改变默认配置的方式： 1、配置文件log4j2.xml放到工程resource目录下就行了；
	 * （查找顺序：log4j2-test.json/log4j2-test.jsn -> log4j2-test.xml ->
	 * log4j2.json/log4j2.jsn -> log4j2.xml） 2、设置log4j.configurationFile；
	 */
	@Test(enabled = false)
	public void test() {

		// 配置文件重定位
		System.setProperty("log4j.configurationFile", "classpath:custom.xml");
		Logger logger = LogManager.getLogger(log4jTest.class);
		logger.info("输出日志信息！");

	}

	/**
	 * log4j2支持自动重新配置,如果配置了monitorInterval，那么log4j2每隔一段时间就会检查一遍这个文件是否修改；
	 */
	@Test(enabled = false)
	public void test2() throws InterruptedException {

		logger.info("动态改配置之前：输出日志信息！");
		Thread.sleep(6000L);
		logger.info("动态改配置之后：输出日志信息！");

	}

}
