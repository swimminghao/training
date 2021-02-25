//package training.week14.gujc;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.wayue.olympus.common.IOUtil;
//import training.week13.gujc.BaseNode;
//import training.week13.gujc.Processor;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.stream.StreamSupport;
//
//public class Main {
//    public static void main(String[] args) {
//        try (InputStream is = IOUtil.inputStream("week13/json-20191225.txt")) {
//            Iterable<JSONObject> jsons = StreamSupport
//                    .stream(IOUtil.readLines(is).spliterator(), false)
//                    .map(JSON::parseObject)
//                    ::iterator;
//            //System.out.println("jsons=" + JSON.toJSONString(jsons));
//            BaseNode root = Processor.parse(jsons);
//            System.out.println(root.prettyPrint());
//
//            //建立表
//            training.week14.gujc.table.Processor.build(root, null);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
