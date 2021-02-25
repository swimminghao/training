package training.week3;

import com.alibaba.fastjson.JSON;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

public class Week3HZY implements Week3Worker {
	public String toFormatStringR(Object json, int level) {
		if (json == null) {
			return null;
		}
		if (json instanceof CharSequence) {
			return "\"" + json + "\"";
		}
		if (json instanceof Map) {
			StringBuilder tojson = new StringBuilder("{");
			int maplevel = level + 1;
			int count = 0;
			for (Object object : ((Map) json).entrySet()) {
				if (count > 0) {
					tojson.append(",");
				}
				count++;
				tojson.append("\n");
				for (int i = 0; i < maplevel; i++) {
					tojson.append("\t");
				}
				Object key = ((Map.Entry) object).getKey();
				Object value = ((Map.Entry) object).getValue();
				tojson.append(toFormatStringR(key, maplevel)).append(":");
				tojson.append(toFormatStringR(value, maplevel));
			}
			if (count!=0){
			tojson.append("\n");
			for (int i = 0; i < level; i++) {
				tojson.append("\t");
			}
			}
			tojson.append("}");
			return tojson.toString();
		}
		if (json instanceof Collection) {
			StringBuilder tojson = new StringBuilder("[");
			int collevel = level + 1;
			int count = 0;
			for (Object coljson : ((Collection) json)) {
				if (count > 0) {
					tojson.append(",");
				}
				count++;
				tojson.append("\n");
				for (int i = 0; i < collevel; i++) {
					tojson.append("\t");
				}
				tojson.append(toFormatStringR(coljson, collevel));
			}
			if (count!=0){
			tojson.append("\n");
			for (int i = 0; i < level; i++) {
				tojson.append("\t");
			}
			}
			tojson.append("]");
			return tojson.toString();
		}
		return json.toString();
	}

	public String toStringR(Object json) {
		int level = 0;
		if (json == null) {
			return null;
		}
		if (json instanceof CharSequence) {
			return "\"" + json + "\"";
		}
		if (json instanceof Map) {
			StringBuilder tojson = new StringBuilder("{");
			int maplevel = level + 1;
			int count = 0;
			for (Object object : ((Map) json).entrySet()) {
				if (count > 0) {
					tojson.append(",");
				}
				count++;
				Object key = ((Map.Entry) object).getKey();
				Object value = ((Map.Entry) object).getValue();
				tojson.append(toStringR(key)).append(":");
				tojson.append(toStringR(value));
			}
			tojson.append("}");
			return tojson.toString();
		}
		if (json instanceof Collection) {
			StringBuilder tojson = new StringBuilder("[");
			int collevel = level + 1;
			int count = 0;
			for (Object coljson : ((Collection) json)) {
				if (count > 0) {
					tojson.append(",");
				}
				count++;
				tojson.append(toStringR(coljson));
			}
			tojson.append("]");
			return tojson.toString();
		}
		return json.toString();
	}

	@Override
	public String toString(JSON json) {
		return toStringR(json);
	}

	@Override
	public String toFormatString(JSON json) {
		return toFormatStringR(json, 0);
	}
}
