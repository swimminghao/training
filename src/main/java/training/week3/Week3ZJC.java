package training.week3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.text.Format;
import java.util.Map;
import java.util.Objects;

public class Week3ZJC implements Week3Worker {
	@Override
	public String toString(JSON json) {
		String string = json.toJSONString(json, SerializerFeature.WriteMapNullValue);
		return string;
	}

	@Override
	public String toFormatString(JSON json) {
		String string = toString(json);
		StringBuilder stringBuilder = new StringBuilder();
		int t = 0;
		for (int i = 0; i < string.length(); i++){
			char key = string.charAt(i);
			stringBuilder.append(key);
			if (key == '{' && string.charAt(i+1) != '}'){
				stringBuilder.append('\n');
				t++;
				print_t(t,stringBuilder);
			}

			if (key == '}' && string.charAt(i-1) != '{'){
				stringBuilder.deleteCharAt(stringBuilder.length()-1);
				stringBuilder.append('\n');
				t--;
				print_t(t,stringBuilder);
				stringBuilder.append(key);
			}

			if (key == '[' && string.charAt(i+1) != ']'){
				stringBuilder.append('\n');
				t++;
				print_t(t,stringBuilder);
			}

			if (key == ']' && string.charAt(i-1) != '['){
				stringBuilder.deleteCharAt(stringBuilder.length()-1);
				stringBuilder.append('\n');
				t--;
				print_t(t,stringBuilder);
				stringBuilder.append(key);
			}

			if (key == ','){
				stringBuilder.append('\n');
				print_t(t,stringBuilder);
			}
		}
		return stringBuilder.toString();
	}

	public void print_t(int t, StringBuilder stringBuilder){
		for (int i =0; i<t; i++){
			stringBuilder.append('\t');
		}
	}
}
