package test.identify;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 标记接口(Mark Interface);
 * 序列化标记测试 对象序列化，需实现Serializable接口;
 * 使用transient关键字修饰的的变量，在序列化对象的过程中，该属性不会被序列化;实现Externalizable接口可以自定义哪些属性序列化，
 * 此时需提供默认构造函数；
 * 
 * @author dinghx14714
 *
 */
public class SerializableTest {

	@Test
	public void myClassTest() throws IOException {
		Assert.expectThrows(IOException.class, () -> {
			OutputStream outputStream = new FileOutputStream("e:/output.txt");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(new MyClass());
			objectOutputStream.close();
			outputStream.close();
		});
	}

	@Test
	public void personTest() {
		String filePath = "e:/person.txt";
		Person person = new Person();
		person.setName("jack");
		person.setAge(12);
		writeSerializableObject(filePath, person);

		Person person2 = (Person) readSerializableObject(filePath);
		Assert.assertEquals(person2.getName(), person.getName());
		Assert.assertNull(person2.getAge());
	}

	@Test
	public void person2Test() {
		String filePath = "e:/person2.txt";
		Person2 person = new Person2();
		person.setName("jack");
		person.setAge(12);
		writeSerializableObject(filePath, person);

		Person2 person2 = (Person2) readSerializableObject(filePath);
		Assert.assertEquals(person2.getAge(), person.getAge());
		Assert.assertNull(person2.getName());
	}

	public static class MyClass {

		private String title;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

	}

	public static class Person implements Serializable {

		private static final long serialVersionUID = 1L;
		private String name;
		private transient Integer age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

	}

	public static class Person2 implements Externalizable {

		private static final long serialVersionUID = 1L;
		private String name;
		private Integer age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeObject(age);
		}

		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			this.age = (Integer) in.readObject();
		}

	}

	private void writeSerializableObject(String file, Object obj) {
		OutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	private Object readSerializableObject(String file) {
		InputStream inputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			inputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(inputStream);
			return objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (objectInputStream != null) {
					objectInputStream.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

}
