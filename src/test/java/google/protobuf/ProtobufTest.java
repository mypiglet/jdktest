package google.protobuf;

import java.util.List;

import org.testng.annotations.Test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;

import google.protobuf.PersonProbuf.Person;
import google.protobuf.PersonProbuf.Person.PhoneNumber;

/**
 * protobuf是谷歌定义的一种语言无关、平台无关的数据交换格式，可以将对象序列化为字节数组、将字节数组反序列化为对象；
 * 
 * @author mypiglet
 *
 */
public class ProtobufTest {

	@Test
	public void test() {

		PersonProbuf.Person.Builder builder = PersonProbuf.Person.newBuilder();
		builder.setEmail("xiaoxiangzi@email.com");
		builder.setId(1);
		builder.setName("jack");
		builder.addPhone(PersonProbuf.Person.PhoneNumber.newBuilder().setNumber("1001")
				.setType(PersonProbuf.Person.PhoneType.MOBILE));
		builder.addPhone(PersonProbuf.Person.PhoneNumber.newBuilder().setNumber("1002")
				.setType(PersonProbuf.Person.PhoneType.HOME));

		Person person = builder.build();
		byte[] buf = person.toByteArray();
		try {
			Person person2 = PersonProbuf.Person.parseFrom(buf);
			System.out.println(person2.getName() + ", " + person2.getEmail());

			JsonFormat jsonFormat = new JsonFormat();
			System.out.println(jsonFormat.printToString(person2));

			List<PhoneNumber> lstPhones = person2.getPhoneList();
			for (PhoneNumber phoneNumber : lstPhones) {
				System.out.println(phoneNumber.getNumber());
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		System.out.println(buf);

	}

}
