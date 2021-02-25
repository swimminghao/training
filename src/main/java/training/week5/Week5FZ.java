package training.week5;

import com.wayue.olympus.common.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Week5FZ implements Week5Worker {
	@Override
	public void compute(File input, File output) {
		try {
			Workbook workbook;
			try (FileInputStream is = new FileInputStream(input)) {
				workbook = WorkbookFactory.create(is);
			}
			Map<String, Map<String, String>> personInfo = new HashMap<>();//姓名->(姓名,部门,性别)
			for (Map<String, String> map : ExcelUtil.read(workbook.getSheet("部门"), "姓名", "部门", "性别")) {
				personInfo.put(map.get("姓名"), map);
			}
			Set<String> relations = new HashSet<>();//Set of relation "A+B"
			Map<String, Integer> counts = new HashMap<>();//姓名->count;
			Sheet evaluate = workbook.createSheet("记录评估");
			ExcelUtil.writeRow(evaluate, "时间", "人员1", "人员2", "人员3", "结果", "原因");
			String date = null;
			for (Row row : workbook.getSheet("记录")) {
				List<String> values = StreamSupport.stream(row.spliterator(), false)
						.map(Cell::getStringCellValue)
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
				if (values.size() == 1) {//日期
					date = values.get(0);
				} else if (values.size() == 3) {//三人组
					List<String> warnings = new ArrayList<>();
					if (values.stream().map(personInfo::get).map(m -> m.get("性别")).distinct().count() == 1) {//校验性别
						warnings.add("单一性别");
					}
					if (values.stream().map(personInfo::get).map(m -> m.get("部门")).distinct().count() != 3) {//校验性别
						warnings.add("部门重复");
					}
					Set<String> tempRelation = new HashSet<>();//组内两两关系
					{//校验组队重复
						for (String s1 : values) {
							for (String s2 : values) {
								if (s1.compareTo(s2) < 0) {
									tempRelation.add(s1 + "," + s2);
								}
							}
						}
						if (relations.stream().anyMatch(tempRelation::contains)) {
							warnings.add("组队重复");
						}
					}
					ExcelUtil.writeRow(evaluate, date, values.get(0), values.get(1), values.get(2), warnings.isEmpty() ? "成功" : "失败", String.join(";", warnings));
					if (warnings.isEmpty()) {
						relations.addAll(tempRelation);
						for (String person : values) {
							counts.put(person, counts.getOrDefault(person, 0) + 1);
						}
					}
				}
			}
			{
				Sheet sheet = workbook.createSheet("次数");
				ExcelUtil.writeRow(sheet, "部门", "姓名", "次数");
				for (Map<String, String> info : personInfo.values()) {
					ExcelUtil.writeRow(sheet, info.get("部门"), info.get("姓名"), counts.getOrDefault(info.get("姓名"), 0));
				}
			}
			try (FileOutputStream os = new FileOutputStream(output)) {
				workbook.write(os);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
