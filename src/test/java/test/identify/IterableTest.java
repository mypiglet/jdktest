package test.identify;

import java.util.Iterator;

import org.testng.annotations.Test;

/**
 * 在java.lang包里； Iterable ：故名思议，实现了这个接口的集合对象支持迭代，是可迭代的。able结尾的表示 能...样，可以做...;
 * Iterator: 在英语中or 结尾是都是表示 ...样的人 or ...
 * 者。如creator就是创作者的意思。这里也是一样：iterator就是迭代者，我们一般叫迭代器，它就是提供迭代机制的对象，具体如何迭代，
 * 都是Iterator接口规范的;
 * 
 * 一个集合对象要表明自己支持迭代，能有使用foreach语句的特权，就必须实现Iterable接口，表明我是可迭代的;
 * 
 * 1、每次在迭代前 ，先调用hasNext()探测是否迭代到终点（本次还能再迭代吗？）; 2、next方法不仅要返回当前元素，还要后移游标cursor;
 * 3、remove()方法用来删除最近一次已经迭代出的元素; 4、 迭代出的元素是原集合中元素的拷贝（重要）; 5、配合foreach使用;
 * 
 * @author dinghx14714
 *
 */
public class IterableTest {

	@Test
	public void test() {

		MyList<String> list = new MyList<String>();
		list.add("jack");
		list.add("amy");
		list.add("rose");
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}

		for (String str : list) {
			System.out.println(str);
		}

	}

	public static class MyList<T> implements Iterable<T> {

		private Object[] arr = new Object[100];;
		private int size;

		public MyList() {
		}

		public boolean add(T obj) {
			int index = size++;
			arr[index] = obj;
			return true;
		}

		@SuppressWarnings("unchecked")
		public T get(int index) {
			return (T) arr[index];
		}

		@Override
		public Iterator<T> iterator() {
			return new MyIterator();
		}

		private class MyIterator implements Iterator<T> {

			private int currentIndex;

			@Override
			public boolean hasNext() {
				return currentIndex != size;
			}

			@Override
			public T next() {
				int i = currentIndex;
				T obj = get(i);
				currentIndex++;
				return obj;
			}

		}

	}

}
