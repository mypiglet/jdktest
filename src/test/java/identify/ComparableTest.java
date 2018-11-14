package identify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

/**
 * 在java.lang包里；
 * 所有想要具有比较功能的类，都建议实现这个接口; 此接口强行对实现它的每个类的对象进行整体排序; 这种排序被称为类的自然排序，类的 compareTo
 * 方法被称为它的自然比较方法; 实现此接口的对象列表（和数组）可以通过 Collections.sort（和 Arrays.sort）进行自动排序;
 * 实现此接口的对象可以用作有序映射中的键或有序集合中的元素，无需指定比较器;
 * 
 * @author mypiglet
 *
 */
public class ComparableTest {

	@Test
	public void test() {
		
		List<MyObject> list = new ArrayList<MyObject>();
		list.add(new MyObject(5));
		list.add(new MyObject(7));
		list.add(new MyObject(2));
		list.add(new MyObject(4));

		Collections.sort(list);
		for (MyObject obj : list) {
			System.out.println(obj.getValue());
		}
	}
	
	public static class MyParentObject{
		private int value;

		public MyParentObject(int v) {
			this.value = v;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	public static class MyObject extends MyParentObject implements Comparable<MyObject> {

		public MyObject(int v) {
			super(v);
		}

		@Override
		public int compareTo(MyObject otherObject) {
			return this.getValue() > otherObject.getValue() ? 1 : (this.getValue() == otherObject.getValue() ? 0 : -1);
		}

	}

}
