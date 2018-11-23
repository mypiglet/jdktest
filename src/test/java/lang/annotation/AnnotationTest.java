package lang.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 元注解（meta-annotation）
 * 
 * Retention(保留)注解说明，这种类型的注解会被保留到那个阶段，有三个值： 1、RetentionPolicy.SOURCE ——
 * 这种类型的Annotations只在源代码级别保留,编译时就会被忽略 ； 2、RetentionPolicy.CLASS ——
 * 这种类型的Annotations编译时被保留,在class文件中存在,但JVM将会忽略； 3、RetentionPolicy.RUNTIME ——
 * 这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用；
 * 
 * Documented注解： Documented注解表明这个注解应该被 javadoc工具记录，默认情况下，javadoc是不包括注解的；
 * 
 * Target注解: 说明了Annotation所修饰的对象范围：Annotation可被用于
 * packages、types（类、接口、枚举、Annotation类型）、类型成员（方法、构造方法、成员变量、枚举值）、方法参数和本地变量（如循环变量、
 * catch参数）; 1、CONSTRUCTOR:用于描述构造器； 2、FIELD:用于描述域； 3、LOCAL_VARIABLE:用于描述局部变量；
 * 4、METHOD:用于描述方法； 5、PACKAGE:用于描述包； 6、PARAMETER:用于描述参数； 7、TYPE:用于描述类、接口(包括注解类型)
 * 或enum声明；
 * 
 * Inherited注解：
 * 它指明被注解的类会自动继承，更具体地说，如果定义注解时使用了Inherited标记，然后用定义的注解来标注另一个父类，父类又有一个子类(subclass)
 * ，则父类的所有属性将被继承到它的子类中；
 * 
 * @author mypiglet
 *
 */
public class AnnotationTest extends BaseCloud implements Cloud {

	@Test
	public void test() {

		Class<?>[] arr2 = AnnotationTest.class.getInterfaces();

		for (int i = 0; i < arr2.length; i++) {
			Class<?> cls = arr2[i];
			if (cls.isAnnotationPresent(MyCloud.class)) {
				Annotation[] arr = cls.getAnnotations();
				MyCloud myCloud = (MyCloud) arr[0];

				Assert.assertEquals(myCloud.color(), "red");
				Assert.assertEquals(myCloud.value(), "hello");
			}
		}
		
		//getFields()获得某个类的所有的公共（public）的字段，包括父类。 
		//getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced， 
		//但是不包括父类的申明字段。 同样类似的还有getConstructors()和getDeclaredConstructors()， getMethods()和getDeclaredMethods()。 
		//clazz.getAnnotations()可以打印出当前类的注解和父类的注解 
		//clazz.getDeclaredAnnotations()只会打印出当前类的注解 
		System.out.println(Arrays.toString(AnnotationTest.class.getAnnotations()));

	}

}
