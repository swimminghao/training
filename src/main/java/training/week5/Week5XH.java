package training.week5;

import com.wayue.olympus.common.excel.ExcelUtil;
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

/**
 * @author xh
 * @date 2019/8/22
 * @description week5作业_组队
 */
public class Week5XH implements Week5Worker {
    /**
     * @author xh
     * @date 2019/8/22
     * @description 工人类
     */
    class Employee {
        private String name;
        private String department;
        private String gender;
        //吃饭次数
        private int times = 0;

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public Employee(String name, String department, String gender) {
            this.name = name;
            this.department = department;
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    /**
     * @author xh
     * @date 2019/8/22
     * @description 吃饭记录类
     */
    class Record {
        private String date;
        private List<Employee> employees;
        //此记录是否成功
        private boolean isSuccess = true;
        //不成功的原因
        private String reason = null;

        public Record(String date, List<Employee> employees) {
            this.date = date;
            this.employees = employees;
        }

        public String[] toStringArray() {
            String[] array = new String[employees.size() + 3];
            array[0] = date;
            for (int i = 1; i <= employees.size(); i++) {
                array[i] = employees.get(i - 1).getName();
            }
            array[employees.size() + 1] = !isSuccess ? "失败" : "成功";
            array[employees.size() + 2] = reason;
            return array;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(List<Employee> employees) {
            this.employees = employees;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    @Override
    public void compute(File input, File output) {
        try {
            FileInputStream in = new FileInputStream(input);
            FileOutputStream out = new FileOutputStream(output);
            Workbook wb = WorkbookFactory.create(in);
            //读取员工信息到员工->员工类的map
            Map<String, Employee> employeesMap = readFromEmploreesExc(wb.getSheet("部门"));
            //读取饭局记录
            List<Record> records = readFromDinnerExc(wb.getSheet("记录"), employeesMap);
            isSuccess(records);
            //输出约饭结果至Excel
            Sheet resultSheet = wb.createSheet("结果");
            ExcelUtil.writeRow(resultSheet, "时间", "人员1", "人员2", "人员3", "结果", "失败原因");
            for (Record record : records) {
                ExcelUtil.writeRow(resultSheet, record.toStringArray());
            }
            //输出约饭次数至Excel
            Sheet timesSheet = wb.createSheet("次数");
            ExcelUtil.writeRow(timesSheet, "部门", "姓名", "次数");
            for (Employee employee : employeesMap.values()) {
                ExcelUtil.writeRow(timesSheet, employee.getDepartment(), employee.getName(), employee.getTimes());
            }
            wb.write(out);
            System.out.println("asdfas");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param sheet
     * @return 员工->员工类的map
     * @description 读取excel到员工->员工类的map
     */
    private Map<String, Employee> readFromEmploreesExc(Sheet sheet) {
        Set<Employee> employees = new HashSet<>();
        Iterable<Map<String, String>> read = ExcelUtil.read(sheet, "姓名", "部门", "性别");
        Iterator<Map<String, String>> iterator = read.iterator();
        while (iterator.hasNext()) {
            Map<String, String> next = iterator.next();
            employees.add(new Employee(next.get("姓名"), next.get("部门"), next.get("性别")));
        }
        Map<String, Employee> map = employees.stream().collect(Collectors.toMap(Employee::getName, Function.identity()));
        return map;
    }

    /**
     * @param sheet
     * @param map
     * @return 饭局记录
     * @description 初始化饭局记录
     */
    private List<Record> readFromDinnerExc(Sheet sheet, Map<String, Employee> map) {
        List<Record> records = new ArrayList<>();
        String date = null;
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            if (row.getCell(1) == null) {
                date = ExcelUtil.getValue(row.getCell(0));
            } else {
                Employee employee = map.get(ExcelUtil.getValue(row.getCell(0)));
                records.add(new Record(date, Arrays.asList(
                        map.get(ExcelUtil.getValue(row.getCell(0))),
                        map.get(ExcelUtil.getValue(row.getCell(1))),
                        map.get(ExcelUtil.getValue(row.getCell(2))))));
            }
        }
        return records;
    }

    /**
     * @param records
     * @description 判断不成立的饭局
     */
    private void isSuccess(List<Record> records) {
        Set<String> relations = new HashSet<>();
        for (Record record : records) {
            List<Employee> employees = record.getEmployees();
            List<String> combination = employees.stream().map(Employee::getName).collect(Collectors.toList());
            Map<String, List<Employee>> collect1 = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));
            Map<String, List<Employee>> collect2 = employees.stream().collect(Collectors.groupingBy(Employee::getGender));
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;
            //判断部门不同
            if (collect1.size() != 3) {
                record.setReason("非不同部门");
                record.setSuccess(false);
                flag1 = false;
            }
            //判断性别不同
            if (collect2.size() == 1) {
                record.setReason("性别一样");
                record.setSuccess(false);
                flag2 = false;
            }
            //判断组队重复
            Set<String> tmp = new HashSet<>();
            {
                for (int i = 0; i < combination.size() - 1; i++) {
                    for (int j = i + 1; j < combination.size(); j++) {
                        if (combination.get(i).compareTo(combination.get(j)) < 0) {
                            tmp.add(combination.get(i) + "," + combination.get(j));
                        } else {
                            tmp.add(combination.get(j) + "," + combination.get(i));
                        }
                    }
                }
                if (relations.stream().anyMatch(tmp::contains)) {
                    record.setReason("组队重复");
                    record.setSuccess(false);
                    flag3 = false;
                }
            }
            //满足以上3个条件，记一次数约饭成功
            if (flag1 && flag2 && flag3) {
                relations.addAll(tmp);
                if (record.isSuccess) {
                    for (Employee employee : employees) {
                        employee.setTimes(employee.getTimes() + 1);
                    }
                }
            }
        }

    }
}