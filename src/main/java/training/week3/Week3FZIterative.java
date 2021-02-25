package training.week3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wayue.olympus.common.ListUtil;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

public class Week3FZIterative implements Week3Worker {
	private static String toString(JSON json, String line, String indent) {
		@AllArgsConstructor
		class Node {
			private String indents, prefix, suffix;
			private Object value;
		}
		Deque<Node> queue = new LinkedList<>();
		queue.push(new Node("", "", "", json));
		StringBuilder sb = new StringBuilder();
		for (Node node = queue.poll(); node != null; node = queue.poll()) {
			sb.append(node.prefix);
			if (node.value == null) {
				sb.append("null").append(node.suffix);
			} else if (node.value instanceof String) {
				sb.append('"').append(node.value).append('"').append(node.suffix);
			} else if (node.value instanceof JSONObject) {
				sb.append(((JSONObject) node.value).isEmpty() ? "{}" + node.suffix : "{");
				int i = 0;
				for (Map.Entry<String, Object> entry : ListUtil.reversed(new ArrayList<>(((JSONObject) node.value).entrySet()))) {
					String p = line + node.indents + indent + "\"" + entry.getKey() + "\":";
					String s = 0 < i++ ? "," : line + node.indents + "}" + node.suffix;
					queue.push(new Node(node.indents + indent, p, s, entry.getValue()));
				}
			} else if (node.value instanceof JSONArray) {
				sb.append(((JSONArray) node.value).isEmpty() ? "[]" + node.suffix : "[");
				int i = 0;
				for (Object v : ListUtil.reversed((JSONArray) node.value)) {
					String p = line + node.indents + indent;
					String s = 0 < i++ ? "," : line + node.indents + "]" + node.suffix;
					queue.push(new Node(node.indents + indent, p, s, v));
				}
			} else {
				sb.append(node.value).append(node.suffix);
			}
		}
		return sb.toString();
	}

	@Override
	public String toFormatString(JSON json) {
		return toString(json, "\n", "\t");
	}

	@Override
	public String toString(JSON json) {
		return toString(json, "", "");
	}
}