package training.week5;

import lombok.*;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Week5GJC implements Week5Worker {

    @Override
    public void compute(File input, File output) {
        try (FileInputStream is = new FileInputStream(input);
             FileOutputStream os = new FileOutputStream(output);
             Workbook workbook = new XSSFWorkbook(is)){

            // 获取姓名到人的map
            Map<String, Person> personMap = getPersonMap(workbook.getSheetAt(0));
            // 获取饭局的组队关系
            List<MealTeam> mealTeams = getMealTeams(workbook.getSheet("记录"), personMap);
            System.out.println(mealTeams);
            // 初始化人到组过队的人的map
            Map<Person, Set<String>> personToPeopleMap = initializePersonToPeopleMap(personMap);
            // 检测不符合的组队关系
            judge(mealTeams, personToPeopleMap);
            System.out.println(mealTeams);

            //输出到sheet
            createSheetForTeamResult(workbook.createSheet("结果"), mealTeams);
            createSheetForRecord(workbook.createSheet("次数"), personToPeopleMap);

            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化人->组过队的人的姓名
     * @param personMap 姓名->人的map
     * @return 人->组过队的人的姓名
     */
    private Map<Person, Set<String>> initializePersonToPeopleMap(Map<String, Person> personMap) {
        Map<Person, Set<String>> map = new HashMap<>();
        for (Map.Entry<String, Person> entry : personMap.entrySet()) {
            map.put(entry.getValue(), new HashSet<>());
        }
        return map;
    }

    /**
     * 创建组队结果sheet
     * @param sheet
     * @param personToPeopleMap
     */
    private void createSheetForRecord(Sheet sheet, Map<Person, Set<String>> personToPeopleMap) {
        int i = 0;
        sheet.createRow(i);
        Row row = sheet.getRow(i);
        createRow(row, Arrays.asList("部门", "姓名", "次数"));
        for (Map.Entry<Person, Set<String>> entry : personToPeopleMap.entrySet()) {
            i++;
            sheet.createRow(i);
            row = sheet.getRow(i);
            createRow(row, Arrays.asList(entry.getKey().getDepartment(),
                    entry.getKey().getName(),
                    entry.getValue().size() / 2 + ""));

        }
    }

    /**
     * 创建次数记录sheet
     * @param sheet
     * @param mealTeams
     */
    private void createSheetForTeamResult(Sheet sheet, List<MealTeam> mealTeams) {
        int i = 0;
        sheet.createRow(i);
        Row row = sheet.getRow(i);
        createRow(row, Arrays.asList("时间", "人员1", "人员2", "人员3", "结果", "原因"));
        for (MealTeam mealTeam : mealTeams) {
            i++;
            sheet.createRow(i);
            row = sheet.getRow(i);
            createRow(row, Arrays.asList(mealTeam.getMealName(),
                    mealTeam.getPersons().get(0).getName(),
                    mealTeam.getPersons().get(1).getName(),
                    mealTeam.getPersons().get(2).getName(),
                    mealTeam.isSuccess ? "成功" : "失败",
                    mealTeam.getDesc()));

        }
    }

    private void createRow(Row row, List<String> contents) {
        if (row != null) {
            for (int i = contents.size() - 1; i >= 0; i--) {
                row.createCell(i);
                row.getCell(i).setCellValue(contents.get(i));
            }
        }
    }

    /**
     * 评判饭局是否成功, 并记录
     * @param mealTeams
     */
    private void judge(List<MealTeam> mealTeams, Map<Person, Set<String>> personToPeopleMap) {
        for (MealTeam mealTeam : mealTeams) {
            Pair<Boolean, String> result = judgeGender(mealTeam);
            if (result.getFirst()) {
                result = judgeDepartment(mealTeam);
                if (result.getFirst()) {
                    result = judgePair(mealTeam, personToPeopleMap);
                    if (result.getFirst()) {
                        mealTeam.setSuccess(true);
                        mealTeam.setDesc(result.getSecond());
                        Person person1 = mealTeam.getPersons().get(0);
                        Person person2 = mealTeam.getPersons().get(1);
                        Person person3 = mealTeam.getPersons().get(2);
                        personToPeopleMap.get(person1).add(person2.getName());
                        personToPeopleMap.get(person1).add(person3.getName());
                        personToPeopleMap.get(person2).add(person1.getName());
                        personToPeopleMap.get(person2).add(person3.getName());
                        personToPeopleMap.get(person3).add(person2.getName());
                        personToPeopleMap.get(person3).add(person1.getName());
                    } else {
                        mealTeam.setSuccess(false);
                        mealTeam.setDesc(result.getSecond());
                    }
                } else {
                    mealTeam.setSuccess(false);
                    mealTeam.setDesc(result.getSecond());
                }
            } else {
                mealTeam.setSuccess(false);
                mealTeam.setDesc(result.getSecond());
            }
        }

    }

    /**
     * 判断是否有重复组队情况
     * @param mealTeam
     * @return true 符合，false 不符合
     */
    private Pair<Boolean, String> judgePair(MealTeam mealTeam, Map<Person, Set<String>> personToPeopleMap) {
        for (Person person1 : mealTeam.getPersons()) {
            Set<String> matchedPeople = personToPeopleMap.get(person1);
            if ( matchedPeople != null) {
                for (Person person2 : mealTeam.getPersons()) {
                    if (!person1.equals(person2) && matchedPeople.contains(person2.getName())) {
                        return new Pair<>(false, person2.getName() + "," + person1.getName() + "重复组队");
                    }
                }
            }
        }
        return new Pair<>(true, "");
    }

    /**
     * 判断性别是否符合
     * @param mealTeam
     * @return true 符合，false 不符合
     */
    private Pair<Boolean, String> judgeGender(MealTeam mealTeam) {
        Set<String> genders = mealTeam.getPersons().stream()
                .map(Person::getGender)
                .collect(Collectors.toSet());
        return genders.size() == 2 ? new Pair<>(true, "") : new Pair<>(false, "性别重复");
    }

    /**
     * 判断部门是否符合
     * @param mealTeam
     * @return true 符合，false 不符合
     */
    private Pair<Boolean, String> judgeDepartment(MealTeam mealTeam) {
        Set<String> departments = mealTeam.getPersons().stream()
                .map(Person::getDepartment)
                .collect(Collectors.toSet());
        return departments.size() == 3 ? new Pair<>(true, "") : new Pair<>(false, "部门重复");
    }

    /**
     * 从sheet中获取人的信息map
     * @param sheet  存人的信息的sheet
     * @return 人的信息map，key是姓名，value是Person
     */
    private Map<String, Person> getPersonMap(Sheet sheet) {
        Map<String, Person> personMap = new HashMap<>();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            Person person = new Person(row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(),
                    row.getCell(2).getStringCellValue());
            personMap.put(person.getName(), person);
        }
        return  personMap;
    }

    /**
     * 获取饭局的组队列表
     * @param sheet 存饭局的信息的sheet
     * @param personMap
     * @return 饭局的组队列表
     */
    private List<MealTeam> getMealTeams(Sheet sheet, Map<String, Person> personMap) {
        List<MealTeam> mealTeams = new ArrayList<>();
        String mealName = null;
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            List<Person> persons = new ArrayList<>();
            if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null) {
                persons.add(personMap.get(row.getCell(0).getStringCellValue()));
                persons.add(personMap.get(row.getCell(1).getStringCellValue()));
                persons.add(personMap.get(row.getCell(2).getStringCellValue()));
                mealTeams.add(new MealTeam(mealName, persons));
            } else {
                mealName = row.getCell(0).getStringCellValue();
            }
        }
        return  mealTeams;
    }

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode(exclude = {"gender"})
    @ToString
    class Person {


        private String name;
        private String department;
        private String gender;

    }

    @Getter
    @Setter
    class MealTeam {
        private String mealName;
        private List<Person> persons;
        private boolean isSuccess;
        private String desc;

        MealTeam(String mealName, List<Person> persons) {
            this.mealName = mealName;
            this.persons = persons;
        }

        @Override
        public String toString() {
            return mealName + ":" + persons + "(" + isSuccess + "," + desc + ")";
        }
    }
}
