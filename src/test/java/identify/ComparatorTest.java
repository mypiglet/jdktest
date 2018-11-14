package identify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.annotations.Test;

import identify.ComparableTest.MyParentObject;

/**
 * 函数式接口@FunctionalInterface；
 * 只能有一个抽象方法,但允许定义java.lang.Object里的public方法，允许定义默认方法及静态方法;
 * 
 * Comparator可以认为是是一个外比较器，一般有两种情况可以使用实现Comparator接口的方式;
 * 1、一个对象不支持自己和自己比较（没有实现Comparable接口），但是又想对两个对象进行比较;
 * 2、一个对象实现了Comparable接口，但是开发者认为compareTo方法中的比较方式并不是自己想要的那种比较方式;
 * 
 * @author mypiglet
 *
 */
public class ComparatorTest {

	@Test
	public void test() {

		List<MyParentObject> list = new ArrayList<MyParentObject>();
		list.add(new MyParentObject(5));
		list.add(new MyParentObject(7));
		list.add(new MyParentObject(2));
		list.add(new MyParentObject(4));

		Collections.sort(list, new MyObjectComparator());
		for (MyParentObject obj : list) {
			System.out.println(obj.getValue());
		}

	}

	public static class MyObjectComparator implements Comparator<MyParentObject> {

		@Override
		public int compare(MyParentObject o1, MyParentObject o2) {
			return o1.getValue() > o2.getValue() ? 1 : (o1.getValue() == o2.getValue() ? 0 : -1);
		}

	}

}
