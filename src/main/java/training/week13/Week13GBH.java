package training.week13;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/12/6
 */
public class Week13GBH {

    private static final MargeStrategy MARGE_STRATEGY = MargeStrategy.IGNORE;
    private static final Class MARGE_DEFAULT_HOIST = String.class;
    private static final Class NULL_DEFAULT = String.class;

    public static void main(String[] args) throws Exception{
        String path = "E:\\Idea\\data-team-training\\src\\main\\java\\training\\week13\\json.txt";
        Map<String, Object> res = parseObject(path);
        System.out.println(res);
    }

    private static final Map<String,Object> parseObject(String path) throws IOException{
        Map<String,Object> result = new HashMap<>();
        for (String line : readJson(path)) {
            JSON root = (JSON) JSON.parse(line);
            parseObject("root", root, result);
        }
        return result;
    }

    private static Iterable<String> readJson(String path) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        return () -> new Iterator<String>() {
            private String line = null;
            @Override
            public boolean hasNext() {
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    return false;
                }
                return  line != null;
            }
            @Override
            public String next() {
                return line;
            }
        };
    }

    private static void parseObject(String key, Object json, Map<String,Object> result) {
        if (null == json) {
            marge(result,key,NULL_DEFAULT);
        } else if(json instanceof String ||json instanceof Boolean || json instanceof Integer ||json instanceof BigDecimal || json instanceof Long) {
            marge(result,key,json.getClass());
        }else if (json instanceof JSONObject) {
            Map<String,Object> resultItem = new HashMap<>();
            for (Map.Entry<String, Object> next : ((JSONObject) json).entrySet()) {
                parseObject(next.getKey(), next.getValue(), resultItem);
            }
            marge(result,key,resultItem.size() == 0 ? NULL_DEFAULT : resultItem);
        } else if (json instanceof JSONArray) {
            Map<String,Object> resultItem = new HashMap<>();
            for (int index = ((JSONArray) json).size() - 1; index >= 0; index--) {
                parseObject(key,((JSONArray) json).get(index),resultItem);
            }
            marge(result,key,resultItem.size() == 0 ? NULL_DEFAULT : resultItem);
        } else {
            System.out.println(String.format("%s is omission, please check",json.getClass()));
        }
    }

    private static void marge(Map<String,Object> map,String key,Object value) {
        Object exist = map.get(key);
        if (null == exist) {
            map.put(key,value);
        } else {
            if (MARGE_STRATEGY == MargeStrategy.IGNORE) {
                //do nothing
            } else {
                map.put(key, MARGE_DEFAULT_HOIST);
            }
        }
    }

    private enum  MargeStrategy {
        IGNORE,
        CLASS_HOIST
    }
}
