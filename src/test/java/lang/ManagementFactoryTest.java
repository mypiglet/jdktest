package lang;

import java.lang.management.ManagementFactory;

import org.testng.annotations.Test;

/**
 * 为我们提供各种获取JVM信息的工厂类.
 * 
 * @author mypiglet
 *
 */
public class ManagementFactoryTest {

	@Test
	public void test() {
		System.out.println("Pid:" + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
	}

}
