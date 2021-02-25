package training.week13.gujc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.tuple.Triple;
import training.week13.gujc.node.*;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;

public class Processor {

    public static BaseNode parse(Iterable<JSONObject> jsons) {
        //TODO
        JSONObject json1 = jsons.iterator().next();

        Deque<Triple<String, Object, ComplexNode>> stack = new LinkedList<>();
        ComplexNode root = new ObjectNode("root");
        json1.forEach((key, value) -> stack.add(Triple.of(key, value, root)));
        while (stack.size() > 0) {
            Triple<String, Object, ComplexNode> triple = stack.pop();
            String name = triple.getLeft();
            Object o = triple.getMiddle();
            ComplexNode node = triple.getRight();
            if (o == null) {
                node.add(new NullNode(name));
            } else if (o instanceof Boolean) {
                node.add(new BooleanNode(name, (Boolean) o));
            } else if (o instanceof Long) {
                node.add(new LongNode(name, (Long) o));
            } else if (o instanceof Integer) {
                 node.add(new LongNode(name, Long.valueOf((Integer)o)));
            } else if (o instanceof Number) {
                node.add(new DecimalNode(name, (BigDecimal) o));
            } else if (o instanceof CharSequence) {
                node.add(new StringNode(name, (String) o));
            } else if (o instanceof JSONArray) {
                ArrayNode newNode = new ArrayNode(name);
                node.add(newNode);
                ((JSONArray) o).forEach(e -> stack.add(Triple.of("$", e, newNode)));
            } else if (o instanceof JSONObject) {
                ObjectNode newNode = new ObjectNode(name);
                node.add(newNode);
                ((JSONObject) o).forEach((key, value) -> stack.add(Triple.of(key, value, newNode)));
            }
        }
        return root;
    }

}
