package lang.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.testng.annotations.Test;

/**
 * 
 * 
 * @author mypiglet
 *
 */
public class ReflectTest {

	@Test
	public void test() {

		handle(1, "a");

	}

	public void handle(int num, String name) {

		Class[] arr = getParameterTypes();
		System.out.println(arr);

	}

	// 获取某方法的参数类型数组
	public Class[] getParameterTypes() {
		return this.getParameterTypes(2);
	}

	public Class[] getParameterTypes(int i) {
		StackTraceElement[] arrStackTrace = new Throwable().getStackTrace();
		StackTraceElement stackTraceElement = new Throwable().getStackTrace()[i];
		try {
			Method[] ms = Class.forName(stackTraceElement.getClassName()).getMethods();
			for (Method m : ms) {
				if (m.getName().equals(stackTraceElement.getMethodName())) {
					Parameter[] arrParameter = m.getParameters();
					return m.getParameterTypes();
				}
			}
		} catch (ClassNotFoundException ex) {
		}
		return new Class[0];
	}

}
