package training.week5;

import com.wayue.olympus.common.excel.ExcelUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Week5HX implements Week5Worker {
	@Override
	public void compute(File input, File output) {
		try (FileInputStream is = new FileInputStream(input);
		     FileOutputStream os = new FileOutputStream(output);
		     Workbook workbook = WorkbookFactory.create(is)) {
			//读取人员配置信息
			Set<Person> people = readPersonFromExcel(workbook.getSheet("部门"));
			Map<String, Person> namePersonMap = people.stream().collect(Collectors.toMap(Person::getName, Function.identity()));
			//读取约饭记录
			List<MealRecord> mealRecords = readMealRecordFromExcel(workbook.getSheet("记录"), namePersonMap);
			//校验约饭记录
			checkMealRecords(mealRecords);
			//输出约饭详情结果至Excel
			Sheet resultSheet = workbook.createSheet("结果");
			ExcelUtil.writeRow(resultSheet, "时间", "人员1", "人员2", "人员3", "结果", "失败原因");
			for (MealRecord record : mealRecords) {
				ExcelUtil.writeRow(resultSheet, (Object[]) record.toStringArray());
			}
			//输出人员约饭次数至Excel
			Sheet timesSheet = workbook.createSheet("次数");
			ExcelUtil.writeRow(timesSheet, "姓名", "次数");
			for (Person person : people) {
				ExcelUtil.writeRow(timesSheet, person.getName(), person.getMealTimes());
			}
			//Excel输出至文件
			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Data
	@RequiredArgsConstructor
	@EqualsAndHashCode(exclude = { "mealTimes" })
	class Person {
		private final String name;
		private final String department;
		private final String gender;
		private int mealTimes = 0;
	}

	@RequiredArgsConstructor
	@Data
	class MealRecord {
		private final String date;
		private final Collection<Person> persons;
		private boolean rejected = false;
		private String rejectReason = "";

		public String[] toStringArray() {
			String[] array = new String[persons.size() + 3];
			array[0] = date;
			System.arraycopy(persons.stream().map(Person::getName).toArray(String[]::new), 0, array, 1, persons.size());
			array[persons.size() + 1] = rejected ? "失败" : "成功";
			array[persons.size() + 2] = rejectReason;
			return array;
		}

		public void appendRejectReason(String reason) {
			rejected = true;
			rejectReason += reason;
		}
	}

	private Set<Person> readPersonFromExcel(final Sheet sheet) {
		Set<Person> persons = new HashSet<>();
		for (Map<String, String> map : ExcelUtil.read(sheet, "姓名", "部门", "性别")) {
			persons.add(new Person(map.get("姓名"), map.get("部门"), map.get("性别")));
		}
		return persons;
	}

	private List<MealRecord> readMealRecordFromExcel(final Sheet sheet, final Map<String, Person> personMap) {
		String recordDate = null;
		List<MealRecord> records = new ArrayList<>();
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (StringUtils.isEmpty(ExcelUtil.getValue(row.getCell(1)))) {
				recordDate = ExcelUtil.getValue(row.getCell(0));
			} else {
				records.add(new MealRecord(recordDate, Arrays.asList(personMap.get(ExcelUtil.getValue(row.getCell(0))),
						personMap.get(ExcelUtil.getValue(row.getCell(1))), personMap.get(ExcelUtil.getValue(row.getCell(2))))));
			}
		}
		return records;
	}

	private void checkMealRecords(List<MealRecord> records) {
		//已经组过队的人
		Map<Person, Set<Person>> arrangedMap = records.stream().map(MealRecord::getPersons).flatMap(Collection::stream)
				.distinct().collect(Collectors.toMap(Function.identity(), p -> new HashSet<>(Collections.singleton(p))));
		for (MealRecord record : records) {
			Collection<Person> persons = record.getPersons();
			//按照记录顺序，先判断部门和性别是否重复
			if (persons.stream().collect(Collectors.groupingBy(Person::getDepartment)).size() != 3) {
				record.appendRejectReason("部门重复;");
			}
			if (persons.stream().collect(Collectors.groupingBy(Person::getGender)).size() != 2) {
				record.appendRejectReason("性别单一;");
			}
			//按照约饭记录顺序，根据已经约饭成功的人员配置，判断每次的约饭记录是否有重复人员
			if (persons.stream().anyMatch(p -> Stream.of(arrangedMap.get(p), persons).flatMap(Collection::stream).distinct().count()
					!= arrangedMap.get(p).size() + 2)) {//约饭成立：约饭的每个人对应的已约人数会加2
				record.appendRejectReason("组队重复;");
			} else if (!record.isRejected()) {
				persons.forEach(p -> arrangedMap.get(p).addAll(persons));
				persons.forEach(p -> p.setMealTimes(p.getMealTimes() + 1));
			}
		}
	}
}
