package training.week3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Set;


/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/8/8
 */
public class Week3BH implements Week3Worker {

    private Integer count = 0;

    public String toString(JSON json) {
        return toJsonString(json,false);
    }

    public String toFormatString(JSON json) {
        return toJsonString(json,true);
    }

    private String toJsonString(Object obj, boolean format) {
        StringBuilder sb = new StringBuilder();
        if (null == obj) {
            sb.append("null");
        } else if (obj instanceof String) {
            sb.append("\"").append(obj).append("\"");
        } else if (obj instanceof JSONObject) {
            sb.append("{");
            Set<Map.Entry<String, Object>> entries = ((JSONObject) obj).entrySet();
            int size = entries.size();
            int num = 0;
            boolean indent = format && size > 0;
            if (indent) {
                sb.append(addIndentWithCountAdd());
            }
            for (Map.Entry<String, Object> entry : entries) {
                sb.append("\"").append(entry.getKey()).append("\"");
                sb.append(":");
                sb.append(toJsonString(entry.getValue(),format));
                if (num++ < size - 1) {
                    sb.append(",");
                    if (format && entries.size() > 0) {
                        sb.append(addIndent());
                    }
                }
            }
            if (indent) {
                sb.append(subIndentWithCountSub());
            }
            sb.append("}");
        } else if (obj instanceof JSONArray) {
            sb.append("[");
            int size = ((JSONArray) obj).size();
            boolean indent = size > 0 && format;
            if (indent) {
                sb.append(addIndentWithCountAdd());
            }
            for (int i = 0; i < size; i++) {
                String result = toJsonString(((JSONArray) obj).get(i),format);
                sb.append(result);
                if (i < size - 1) {
                    sb.append(",");
                }
                if (format && i < size -1) {
                    sb.append(addIndent());
                }
                if (format && i == size -1) {
                    sb.append(subIndentWithCountSub());
                }
            }
            if (indent) {
                this.count --;
            }
            sb.append("]");
        } else {
            sb.append(obj.toString());
        }
        return sb.toString();
    }

    private String addIndentWithCountAdd() {
        this.count ++;
        return addIndent();
    }

    private String subIndentWithCountSub() {
        this.count --;
        return addIndent();
    }

    private String addIndent() {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        for (int j = 0; j < this.count; j++) {
            result.append("\t");
        }
        return result.toString();
    }
}
