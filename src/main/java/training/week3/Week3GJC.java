package training.week3;

import com.alibaba.fastjson.JSON;
import java.util.Collection;
import java.util.Map;

public class Week3GJC implements Week3Worker {
    @Override
    public String toString(JSON json) {
        return localToString(json, 0, false);
    }

    @Override
    public String toFormatString(JSON json) {
        return localToString(json, 0, true);
    }

    private String space(int level) {
        StringBuilder sb = new StringBuilder();
        while (level-- > 0) {
            sb.append("\t");
        }
        return sb.toString();
    }

    private String localToString(Object o, int level, boolean prettyPrint) {
        StringBuilder sb = new StringBuilder();
        if (o == null) {
            sb.append("null");
        } else if (o instanceof CharSequence) {
            sb.append("\"").append(o).append("\"");
        } else if (o instanceof Collection) {
            sb.append("[").append(((Collection) o).size() > 0 && prettyPrint ? "\n" + space(level + 1) : "");
            int size = ((Collection) o).size();
            int i = 0;
            for (Object obj : (Collection) o) {
                i ++;
                sb.append(localToString(obj, level + 1, prettyPrint))
                        .append(size != i ? "," : "").append(prettyPrint ? "\n" : "")
                        .append(prettyPrint ? (size != i ? space(level + 1) : space(level)) : "");
            }
            sb.append("]");
        } else if (o instanceof Map) {
            sb.append("{").append(((Map) o).size() > 0 && prettyPrint ? "\n" + space(level + 1) : "");
            int size = ((Map) o).size();
            int i = 0;
            for (Object obj : ((Map) o).entrySet()) {
                i ++;
                Map.Entry e = (Map.Entry) obj;
                sb.append("\"").append(e.getKey()).append("\":")
                        .append(localToString(e.getValue(), level + 1, prettyPrint))
                        .append(size != i ? "," : "").append(prettyPrint ? "\n" : "")
                        .append(prettyPrint ? (size != i ? space(level + 1) : space(level)) : "");
            }
            sb.append("}");
        } else {
            sb.append(o.toString());
        }
        return sb.toString();
    }
}
