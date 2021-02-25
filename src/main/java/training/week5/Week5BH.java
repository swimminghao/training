package training.week5;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author BinHao.Guo
 * @version 1.0
 * @Date 2019/8/23
 */
public class Week5BH implements Week5Worker{

    @Override
    public void compute(File input, File output) {
        if (!input.exists() || !input.isFile()) {
            return;
        }

        input = new File(input.getPath().substring(0,input.getPath().length() - 1));
        output = new File(output.getPath().substring(0,output.getPath().length() - 1));
        if (output.exists()) {
            boolean delete = output.delete();
            if (!delete) {
                return;
            }
        }

        try (FileInputStream is = new FileInputStream(input)){
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("ISO-8859-1");
            Workbook wb = Workbook.getWorkbook(is,workbookSettings);
            if (wb.getSheets().length != 2) {
                return;
            }

            List<Person> persons = readPersons(wb.getSheets()[0]);
            List<Record> records = readRecords(wb.getSheets()[1]);

            List<List<String>> result = calculation(persons, records);

            write(output,result);
        } catch (IOException | BiffException | WriteException e) {
            e.printStackTrace();
        }
    }

    private List<Person> readPersons(Sheet departmentSheet) {
        List<Person> persons = new ArrayList<>();
        for (int row = departmentSheet.getRows() - 1; row > 0; row--) {
            String name = departmentSheet.getCell(0,row).getContents();
            String department = departmentSheet.getCell(1,row).getContents();
            String sex = departmentSheet.getCell(2,row).getContents();
            Person person = new Person(name,department,sex);
            persons.add(person);
        }
        return persons;
    }

    private List<Record> readRecords(Sheet recordSheet) {
        List<Record> records = new ArrayList<>();
        for (int row = recordSheet.getRows() - 1; row >= 0; row--) {
            String personOne = recordSheet.getCell(0,row).getContents();
            String personTwo = recordSheet.getCell(1,row).getContents();
            String personThree = recordSheet.getCell(2,row).getContents();
            if (StringUtils.isEmpty(personOne) || StringUtils.isEmpty(personTwo) || StringUtils.isEmpty(personThree)) {
                continue;
            }
            Record record = new Record(personOne,personTwo,personThree);
            records.add(record);
        }
        return records;
    }

    private List<List<String>> calculation(List<Person> persons, List<Record> records) {

        Map<String, Person> collect = persons.stream().collect(Collectors.toMap(Person::getName, account -> account));
        List<List<String>> result = new ArrayList<>();

        Map<String,Integer> ed = new HashMap<>();
        records.removeIf(item -> collect.get(item.personOne).getSex().equals(collect.get(item.personTwo).getSex())
                && collect.get(item.personOne).getSex().equals(collect.get(item.personThree).getSex()));

        records.removeIf(item -> collect.get(item.personOne).getDepartment().equals(collect.get(item.personTwo).getDepartment())
                && collect.get(item.personOne).getDepartment().equals(collect.get(item.personThree).getDepartment()));

        Iterator<Record> iterator = records.iterator();
        while (iterator.hasNext()) {
            Record next = iterator.next();

            String oneTwo = next.getPersonOne().concat(next.getPersonTwo());
            String twoOne = next.getPersonTwo().concat(next.getPersonOne());

            String oneThree = next.getPersonOne().concat(next.getPersonThree());
            String threeOne = next.getPersonThree().concat(next.getPersonOne());

            String twoThree = next.getPersonTwo().concat(next.getPersonThree());
            String threeTo = next.getPersonThree().concat(next.getPersonTwo());
            if (null != ed.get(oneTwo)
                    || null != ed.get(twoOne)
                    || null != ed.get(oneThree)
                    || null != ed.get(threeOne)
                    || null != ed.get(twoThree)
                    || null != ed.get(threeTo)) {
                iterator.remove();
                continue;
            }
            ed.put(oneTwo,1);
            ed.put(twoOne,1);
            ed.put(oneThree,1);
            ed.put(threeOne,1);
            ed.put(twoThree,1);
            ed.put(threeTo,1);
        }
        Map<String,Integer> count = new HashMap<>();
        for (Record record : records) {
            Integer one = count.get(record.getPersonOne());
            Integer two = count.get(record.getPersonTwo());
            Integer three = count.get(record.getPersonThree());
            if (null == one) {
                count.put(record.getPersonOne(),1);
            } else {
                count.put(record.getPersonOne(),one + 1);
            }

            if (null == two) {
                count.put(record.getPersonTwo(),1);
            } else {
                count.put(record.getPersonTwo(),two + 1);
            }

            if (null == three) {
                count.put(record.getPersonThree(),1);
            } else {
                count.put(record.getPersonThree(),three + 1);
            }
        }

        for (Map.Entry<String, Integer> item : count.entrySet()) {
            List<String> itemResult = new ArrayList<>();
            itemResult.add(item.getKey());
            Person person = collect.get(item.getKey());
            itemResult.add(person.department);
            itemResult.add(String.valueOf(item.getValue()));
            result.add(itemResult);
        }
        return result;
    }

    private void write(File out, List<List<String>> result) throws WriteException, IOException{
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(out);
            WritableSheet ws = workbook.createSheet("结果", 0);
            for (int row = result.size() - 1; row >= 0; row--) {
                Label cellName = new Label(0, row, result.get(row).get(0));
                ws.addCell(cellName);
                Label cellDepart = new Label(1, row, result.get(row).get(1));
                ws.addCell(cellDepart);
                Label cellValue = new Label(2, row, result.get(row).get(2));
                ws.addCell(cellValue);
            }
        } finally {
            if (null != workbook) {
                workbook.write();
                workbook.close();
            }
        }
    }


    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    private class Person {
        private String name;
        private String department;
        private String sex;
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    private class Record {
        private String personOne;
        private String personTwo;
        private String personThree;
    }
}
