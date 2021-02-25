package training.week3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class Week3FZRecursive implements Week3Worker {
	private static void toString(StringBuilder sb, String indents, Object value, String line, String indent) {
		if (value == null) {
			sb.append("null");
		} else if (value instanceof String) {
			sb.append('"').append(value).append('"');
		} else if (value instanceof JSONObject) {
			sb.append(((JSONObject) value).isEmpty() ? "{}" : "{");
			int i = 0;
			for (Map.Entry<String, Object> entry : ((JSONObject) value).entrySet()) {
				sb.append(line).append(indents).append(indent).append("\"").append(entry.getKey()).append("\":");//prefix
				toString(sb, indents + indent, entry.getValue(), line, indent);
				sb.append(++i < ((JSONObject) value).size() ? "," : line + indents + "}");//suffix
			}
		} else if (value instanceof JSONArray) {
			sb.append(((JSONArray) value).isEmpty() ? "[]" : "[");
			int i = 0;
			for (Object v : (JSONArray) value) {
				sb.append(line).append(indents).append(indent);//prefix
				toString(sb, indents + indent, v, line, indent);
				sb.append(++i < ((JSONArray) value).size() ? "," : line + indents + "]");//suffix
			}
		} else {
			sb.append(value);
		}
	}

	@Override
	public String toFormatString(JSON json) {
		StringBuilder sb = new StringBuilder();
		toString(sb, "", json, "\n", "\t");
		return sb.toString();
	}

	@Override
	public String toString(JSON json) {
		StringBuilder sb = new StringBuilder();
		toString(sb, "", json, "", "");
		return sb.toString();
	}
}
