package identify;

import org.testng.annotations.Test;

/**
 * 在java.lang包里；
 * 实现Runnable接口比继承Thread类所具有的优势; 1、适合多个相同的程序代码的线程去处理同一个资源； 2、可以避免java中的单继承的限制；
 * 3、增加程序的健壮性，代码可以被多个线程共享，代码和数据独立；
 * 
 * @author mypiglet
 *
 */
public class RunnableTest {

	@Test
	public void test() {

		Thread thead = new Thread(new MyProcessor());
		thead.start();
		
		System.out.println("r");
	}

	public static class MyProcessor implements Runnable {

		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				System.out.println(i);
			}
		}

	}

}
