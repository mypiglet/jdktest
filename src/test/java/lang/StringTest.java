package lang;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 
 * 
 * @author mypiglet
 *
 */
public class StringTest {

	@Test
	public void test() {

		String str = "1";
		resetValue(str);
		Assert.assertEquals(str, "1");

	}

	private void resetValue(String str) {
		str = "2";
	}

}
