//package training.week13.xh;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.wayue.olympus.common.IOUtil;
//import training.week13.XH.JsonMetaNodes;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Set;
//import java.util.stream.StreamSupport;
//
//public class JsonMetaNodesTest {
//	public static void main(String[] args) throws IOException {
////		create();
//		try (InputStream is = IOUtil.inputStream("week13/json-20191225.txt")) {
//			Iterable<JSONObject> jsons = StreamSupport
//					.stream(IOUtil.readLines(is).spliterator(), false)
//					.map(JSON::parseObject)
//					::iterator;
//			for (JSONObject json : jsons) {
//				Set<String> strings = json.keySet();
//				System.out.println(strings);
//			}
//			System.out.println(JsonMetaNodes.parse("ROOT", jsons).prettyPrint());
//			//TODO
//		}
//	}
//}