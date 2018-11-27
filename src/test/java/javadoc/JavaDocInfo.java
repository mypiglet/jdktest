package javadoc;

/**
 * 第一段：概要描述，通常用一句或者一段话简要描述该类的作用，以英文句号作为结束.
 * 
 * <p>
 * 第二段：详细描述，通常用一段或者多段话来详细描述该类的作用，一般每段话都以英文句号作为结束.
 * 
 * <p>
 * {@link java.lang.String#charAt(int)} 用于快速链接到相关代码;{@code 会被解析成
 * <code> text </code>},一般在Javadoc中只要涉及到类名或者方法名，都需要使用{@code java.lang.String}
 * 进行标记.
 * 
 * <p>
 * 详细描述中可以使用html标签，如
 * 
 * <pre>
 * {@code <p>、<pre>、<a>、<ul>、<i>}
 * </pre>
 * 
 * 等标签;详细可查阅
 * <a href="https://blog.csdn.net/vbirdbest/article/details/80296136">javadoc
 * 使用详解</a>.
 * 
 * <p>
 * 第三段：文档标注，用于标注作者、创建时间、参阅类等信息.
 * 
 * <p>
 * 详细描述后面一般使用{@code author}来标记作者，如果一个文件有多个作者来维护就标记多个{@code author}，
 * {@code author} 后面可以跟作者姓名(也可以附带邮箱地址)、组织名称(也可以附带组织官网地址).
 * 
 * <p>
 * see 一般用于标记该类相关联的类,{@code see}即可以用在类上，也可以用在方法上.
 * 
 *      <p>
 * since 一般用于标记文件创建时项目当时对应的版本，一般后面跟版本号，也可以跟是一个时间，表示文件当前创建的时间.
 * 
 *        <p>
 * @version 用于标记当前版本，默认为1.0.
 * 
 * @author mypiglet
 * 
 * @see java.lang.String
 * 
 * @since 1.8
 * 
 * @version 1.0
 *
 * @param <T>
 *            一般类中支持泛型时会通过@param来解释泛型的类型.
 */
public class JavaDocInfo<T> extends BaseJavaDocInfo {

	/** 默认数量 {@value} */
	private static final Integer QUANTITY = 1;

	/**
	 * 写在方法上的文档标注一般分为三段
	 * 
	 * <p>
	 * 第一段：概要描述，通常用一句或者一段话简要描述该方法的作用，以英文句号作为结束.
	 * 
	 * <p>
	 * 第二段：详细描述，通常用一段或者多段话来详细描述该方法的作用，一般每段话都以英文句号作为结束.
	 * 
	 * <p>
	 * 第三段：文档标注，用于标注参数、返回值、异常、参阅等.
	 * 
	 * <p>
	 * pre元素可定义预格式化的文本，被包围在pre元素中的文本通常会保留空格和换行符.
	 * 
	 * <pre class="code">
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * 
	 * @see java.net.URLDecoder#decode(String, String)
	 * 
	 * @param num 数量
	 *            {@code Integer}
	 * @return 返回字符串
	 * @throws IllegalArgumentException 异常
	 */
	public String doMethod(Integer num) throws IllegalArgumentException {
		return QUANTITY.toString();
	}

	/*
	 * @inheritDoc
	 * 
	 * @see javadoc.BaseJavaDocInfo#handle(java.lang.Integer)
	 */
	@Override
	String handle(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
