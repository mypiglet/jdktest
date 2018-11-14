package identify;

import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 标记接口(Mark Interface); 当要实现某些算法时，会判断当前类是否实现了RandomAccess接口会根据结果选择不同的算法;
 * 
 * @author mypiglet
 *
 */
public class RandomAccessTest {

	@Test
	public void test() {

		List<String> list = new ArrayList<String>();
		Assert.assertEquals(list instanceof RandomAccess, true);

	}

	public static class MyList implements RandomAccess {

	}

}