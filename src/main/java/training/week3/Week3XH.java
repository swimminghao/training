package training.week3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author xh
 */
public class Week3XH implements Week3Worker {
    public String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                    sb.append(current);
                    if (i + 1 <= jsonStr.length() && jsonStr.charAt(i + 1) != '}') {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '[':
                    sb.append(current);
                    if (i + 1 <= jsonStr.length() && jsonStr.charAt(i + 1) != ']') {
                        sb.append('\n');

                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                    if (jsonStr.charAt(i - 1) != '{') {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ']':
                    if (jsonStr.charAt(i - 1) != '[') {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param indent
     * @author xh
     */
    private void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    public void toString(StringBuilder sb, Object ob) {
        if (ob == null) {
            sb.append("null");
        } else if (ob instanceof String) {
            sb.append('"').append(ob).append('"');
        } else if (ob instanceof Number || ob instanceof Boolean) {
            sb.append(ob);
        } else if (ob instanceof JSONObject) {
            sb.append(((JSONObject) ob).isEmpty() ? "{}" : "{");
            int i = 0;
            for (Map.Entry<String, Object> entry : ((JSONObject) ob).entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":");
                toString(sb, entry.getValue());
                if (++i < ((JSONObject) ob).size()) {
                    sb.append(",");
                } else {
                    sb.append("}");
                }

            }
        } else if (ob instanceof JSONArray) {
            sb.append(((JSONArray) ob).isEmpty() ? "[]" : "[");
            int i = 0;
            for (Object v : (JSONArray) ob) {

                toString(sb, v);
                if (++i < ((JSONArray) ob).size()) {
                    sb.append(",");
                } else {
                    sb.append("]");
                }
            }
        }
    }

    @Override
    public String toString(JSON json) {
        StringBuilder sb = new StringBuilder();
        toString(sb, json);
        return sb.toString();
    }

    @Override
    public String toFormatString(JSON json) {
        String s = toString(json);
        String prettyJson = formatJson(s);
        return prettyJson;
    }
}
