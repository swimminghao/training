package training.week13.zq.json.database.create.mysql;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import training.week13.zq.json.database.BaseField;
import training.week13.zq.json.database.DataStructNode;
import training.week13.zq.json.database.create.AbstractDataBaseStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2018-2019 善诊
 * AbstractDataBaseStruct
 *
 * @author : zhuqiang
 * @date : 2020/1/4
 */
public class MysqlStructNodeDecorator extends AbstractDataBaseStruct {
    private final static String TABLE_NAME = "zq_";
    private final static String INSERT_SQL = "INSERT INTO %s(%s) values %s";

    private final static String CREATE_TABLE_SQL = "create table %s (%s) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;";

    @Override
    public MysqlDataStructNode decoratorStructNode(DataStructNode node) {
        MysqlDataStructNode mysqlDataStructNode = (MysqlDataStructNode) node;
        mysqlDataStructNode.setCreateTableSql(String.format(CREATE_TABLE_SQL, mysqlDataStructNode.getTableName(), buildSqlFiled(mysqlDataStructNode.getFields())));
        mysqlDataStructNode.setInsertSql(
                String.format(INSERT_SQL, mysqlDataStructNode.getTableName(),buildInsertField(mysqlDataStructNode.getFields()), buildInsertSql(mysqlDataStructNode.getFields()))
        );
        return mysqlDataStructNode;
    }

    private String buildSqlFiled(List<BaseField> fields) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id varchar(20),\n").append("pid varchar(20),\n");
        fields.stream().forEach(e -> stringBuilder.append(e.getFieldName())
                .append(" ")
                .append(e.getFieldType())
                .append(" ")
                .append(",")
                .append("\n"));
        String sql=stringBuilder.toString();
        return sql.length()>2? sql.substring(0,stringBuilder.toString().length()-2):sql;
    }

    private String buildInsertSql(List<BaseField> fields) {
        Map<Pair<String, String>, List<Object>> value = new HashMap<>();
        fields.stream().map(BaseField::getValues)
                .flatMap(List::stream)
                .forEach(e -> value.computeIfAbsent(Pair.of(e.getLeft(), e.getMiddle()), a -> new ArrayList<>()).add(e.getRight())
                );
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Pair<String, String>, List<Object>> pairListEntry : value.entrySet()) {
            StringBuilder c = new StringBuilder();
            for (Object v :
                    pairListEntry.getValue()) {
                c.append(v).append(",");
            }
            stringBuilder.append("(")
                    .append(pairListEntry.getKey().getKey())
                    .append(",")
                    .append(pairListEntry.getKey().getRight())
                    .append(",")
                    .append(c.toString())
                    .append("),\n");
        }
        String sql=stringBuilder.toString();
        return sql.length()>2? sql.substring(0,stringBuilder.toString().length()-2):sql;
    }

    private String buildInsertField(List<BaseField> fields) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id").append("pid,");
        fields.stream().forEach(e -> stringBuilder.append(e.getFieldName())
                .append(","));
        String sql=stringBuilder.toString();
        return sql.length()>2? sql.substring(0,stringBuilder.toString().length()-2):sql;

    }

    @Override
    public MysqlDataStructNode structNodeCreate() {
        return new MysqlDataStructNode();
    }


    @Override
    public BaseField fieldCreate(String fieldName, List<Triple<String, String, Object>> values, String fieldType) {
        return MysqlField.creatMysqlField(fieldName, values, fieldType);
    }

    @Override
    public String getDataName() {
        return TABLE_NAME;
    }

}
