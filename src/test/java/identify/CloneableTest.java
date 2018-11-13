package identify;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 标记接口(Mark Interface);
 * 如果试图用一个不支持Cloneable接口的类调用clone( )方法，将引发一个CloneNotSupportedException异常；
 * 
 * @author dinghx14714
 *
 */
public class CloneableTest {

	@Test
	public void test() {

		User user = new User();
		user.setName("jack");

		Assert.expectThrows(CloneNotSupportedException.class, () -> {
			user.getCopy();
		});

		User2 user2 = new User2();
		user2.setName("jack");
		user2.setUser(user);

		try {
			Object obj = user2.getCopy();
			user2.setName("amy");
			Assert.assertEquals(((User2) obj).getName(), "jack");
			user2.getUser().setName("amy");
			//说明为浅拷贝
			Assert.assertEquals(((User2) obj).getUser().getName(), "amy");
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}

	public static class User {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getCopy() throws CloneNotSupportedException {
			return this.clone();
		}

	}

	public static class User2 implements Cloneable {

		private String name;
		private User user;

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getCopy() throws CloneNotSupportedException {
			return this.clone();
		}

	}

}
