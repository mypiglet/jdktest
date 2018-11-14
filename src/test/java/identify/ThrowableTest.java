package identify;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 在java.lang包里；
 * 所有异常和错误的基类;
 * Throwable下有两个大类那就是异常(Exception)和错误(Error);
 * 
 * @author mypiglet
 *
 */
public class ThrowableTest {
	
	@Test
	public void test(){
		Assert.expectThrows(Throwable.class, ()->{
			throw new Exception();
		});
		
		Assert.expectThrows(Throwable.class, ()->{
			throw new MyException();
		});
	}
	
	public static class MyException extends Throwable{

		private static final long serialVersionUID = -3396422963724565169L;
		
	}

}
