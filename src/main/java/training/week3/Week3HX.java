package training.week3;

import com.alibaba.fastjson.JSON;

import java.util.Collection;
import java.util.Map;
import java.util.stream.IntStream;

public class Week3HX implements Week3Worker {
	@Override
	public String toString(JSON json) {
		return toJsonString(json, false, 0);
	}

	@Override
	public String toFormatString(JSON json) {
		return toJsonString(json, true, 0);
	}

	public static String toJsonString(Object object, boolean pretty, int indent) {
		if (object == null) {
			return "null";
		} else if (object instanceof CharSequence) {
			return "\"" + object + "\"";
		} else if (object instanceof Map) {
			StringBuilder sb = new StringBuilder("{");
			int count = 0;
			for (Map.Entry current : ((Map<?, ?>) object).entrySet()) {
				if (count++ != 0) {
					sb.append(",");
				}
				prettyFormat(sb, indent + 1, pretty);
				sb.append(toJsonString(current.getKey(), pretty, indent + 1)).append(":");
				sb.append(toJsonString(current.getValue(), pretty, indent + 1));
			}
			prettyFormat(sb, indent, pretty && count != 0);
			return sb.append("}").toString();
		} else if (object instanceof Collection) {
			StringBuilder sb = new StringBuilder("[");
			int count = 0;
			for (Object current : ((Collection) object)) {
				if (count++ != 0) {
					sb.append(",");
				}
				prettyFormat(sb, indent + 1, pretty);
				sb.append(toJsonString(current, pretty, indent + 1));
			}
			prettyFormat(sb, indent, pretty && count != 0);
			return sb.append("]").toString();
		}
		return object.toString();
	}

	private static void prettyFormat(StringBuilder stringBuilder, int indent, boolean enable) {
		if (enable) {
			stringBuilder.append("\n");
			IntStream.range(0, indent).forEach(value -> stringBuilder.append("\t"));
		}
	}
}
