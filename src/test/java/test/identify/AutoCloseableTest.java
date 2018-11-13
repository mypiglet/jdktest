package test.identify;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 实现自动关闭资源,在java.lang包中; 在1.7之前，我们通过try{} finally{} 在finally中释放资源;
 * 对于实现AutoCloseable接口的类的实例，将其放到try后面（我们称之为：带资源的try语句），在try结束的时候，会自动将这些资源关闭（
 * 调用close方法）; 在try代码中声明的资源被隐式声明为fianl； Closeable扩展了AutoCloseable;
 * 
 * @author dinghx14714
 *
 */
public class AutoCloseableTest {

	@Test
	public void test() {
		MyResourceManager myResource2 = null;
		try (MyResourceManager myResource = new MyResourceManager()) {
			myResource2 = myResource;
			Assert.assertEquals(myResource.isClosed(), false);
			//throw new IOException();
		} catch (Exception e) {
			Assert.assertEquals(myResource2.isClosed(), true);
			e.printStackTrace();
		}
		Assert.assertEquals(myResource2.isClosed(), true);
	}

	public static class MyResourceManager implements AutoCloseable {

		private boolean closed = false;

		public boolean isClosed() {
			return closed;
		}

		public void setClosed(boolean closed) {
			this.closed = closed;
		}

		@Override
		public void close() throws Exception {
			closed = true;
		}

	}

}
