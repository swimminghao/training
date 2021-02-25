package training.week3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

@RunWith(Parameterized.class)
public class Week3WorkerTest {
	private List<JSON> values = new ArrayList<>();
	private Week3Worker worker;

	{
		values.add(new JSONObject()
				           .fluentPut("a", "b")
				           .fluentPut("b", new JSONArray()
						           .fluentAdd(1)
						           .fluentAdd(1.1)
						           .fluentAdd(new JSONObject())
						           .fluentAdd(true)
						           .fluentAdd(null)
						           .fluentAdd(new JSONObject()
								                      .fluentPut("bbb", new JSONObject()
										                      .fluentPut("x2", 2)
										                      .fluentPut("x22", 2.2)
										                      .fluentPut("array", new JSONArray()))
								                      .fluentPut("ccc", null)
						           )
				           )
		);
	}

	public Week3WorkerTest(Class<? extends Week3Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week3FZIterative.class },
				{ Week3FZRecursive.class },
				{ Week3GJC.class },
				{ Week3HX.class },
				{ Week3XH.class },
				{ Week3ZJC.class },
				{ Week3HZY.class },
				};
	}

	@Test
	public void testToFormatString() {
		for (JSON json : values) {
			String s = worker.toFormatString(json);
			String expected = JSON.toJSONString(json, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
			System.out.println(expected);
			System.out.println(s);
			Assert.assertEquals(s, expected, s);
		}
	}

	@Test
	public void testToString() {
		for (JSON json : values) {
			String s = worker.toString(json);
			String expected = JSON.toJSONString(json, SerializerFeature.WriteMapNullValue);
			System.out.println(expected);
			System.out.println(s);
			Assert.assertEquals(s, expected, s);
		}
	}
}