//package training.week13.fz;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.wayue.olympus.common.IOUtil;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//public class SchemaNodesTest {
//	public static void main(String[] args) throws IOException {
////		create();
//		try (InputStream is = IOUtil.inputStream("week13/json-20191225.txt")) {
//			Iterable<JSONObject> jsons = StreamSupport
//					.stream(IOUtil.readLines(is).spliterator(), false)
//					.map(JSON::parseObject)
//					::iterator;
//			System.out.println(SchemaNodes.parseIterative("ROOT", jsons).prettyPrint());
//			//TODO
//		}
//	}
//
//	private static void create() throws IOException {
//		try (Writer writer = new BufferedWriter(new FileWriter("D:\\repositories\\data-team-training\\src\\main\\resources\\week13\\json-20191225.txt"))) {
//			try (FileInputStream is = new FileInputStream("E:\\百度百科100条.json.txt")) {
//				for (String s : IOUtil.readLines(is)) {
//					JSONObject json = JSON.parseObject(s);
//					{
//						json.put("updateTime", Long.valueOf(json.getJSONObject("updateTime").getJSONObject("$date").getString("$numberLong")));
//						json.put("createTime", Long.valueOf(json.getJSONObject("createTime").getJSONObject("$date").getString("$numberLong")));
//					}
//					{
//						JSONObject test = new JSONObject(true);
//						json.put("_test", test);
//						test.put("null", null);
//						test.put("object", new JSONObject().fluentPut("f1", "abc" + Math.random()).fluentPut("f2", "abc" + Math.random()));
//						test.put("ARRAY-empty", new JSONArray());
//						test.put("ARRAY-ARRAY", new JSONArray().fluentAdd(new JSONArray().fluentAdd(Math.random())));
//						test.put("ARRAY-OBJECT", new JSONArray().fluentAdd(new JSONObject().fluentPut("f1", "abc" + Math.random()).fluentPut("f2", "abc" + Math.random())));
//						test.put("ARRAY-null", new JSONArray().fluentAdd(null));
//						test.put("BOOLEAN", Math.random() < 0.1 ? null : Math.random() < 0.5);
//						test.put("DECIMAL", Math.random());
//						test.put("INTEGER", (int) (Integer.MAX_VALUE * Math.random()));
//						List<? extends List<? extends Number>> numbers = Arrays.asList(Stream.generate(Math::random)
//								                                                               .map(f -> Integer.MAX_VALUE * f)
//								                                                               .map(Double::intValue)
//								                                                               .limit(1 + (long) (5 * Math.random()))
//								                                                               .collect(Collectors.toList()),
//						                                                               Stream.generate(Math::random)
//								                                                               .map(f -> Long.MAX_VALUE * f)
//								                                                               .map(Double::longValue)
//								                                                               .limit(1 + (long) (5 * Math.random()))
//								                                                               .collect(Collectors.toList()),
//						                                                               Stream.generate(Math::random)
//								                                                               .limit(1 + (long) (5 * Math.random()))
//								                                                               .collect(Collectors.toList()));
//						for (int i = 0; i < 1 << 3; i++) {
//							List<Object> objects = new ArrayList<>();
//							for (int j = 0; j < 3; j++) {
//								if ((i & 1 << j) != 0) {
//									objects.addAll(numbers.get(j));
//								}
//							}
//							test.put("ARRAY-" + i, objects);
//						}
//					}
//					writer.append(JSON.toJSONString(json, SerializerFeature.WriteMapNullValue, SerializerFeature.SortField)).append('\n');
//				}
//			}
//		}
//	}
//}