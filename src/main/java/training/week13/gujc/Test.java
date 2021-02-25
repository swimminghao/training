//package training.week13.gujc;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.wayue.olympus.common.IOUtil;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.stream.StreamSupport;
//
//public class Test {
//    public static void main(String[] args) {
//        try (InputStream is = IOUtil.inputStream("week13/json-20191225.txt")) {
//            Iterable<JSONObject> jsons = StreamSupport
//                    .stream(IOUtil.readLines(is).spliterator(), false)
//                    .map(JSON::parseObject)
//                    ::iterator;
//            System.out.println(Processor.parse(jsons));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
