package training.week13.zq.json.database.create.mysql;


import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Triple;
import training.week13.zq.json.database.BaseField;

import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * MysqlField
 *
 * @Author: zhuqiang
 * @Date: 2020/1/4
 */

@Data
@ToString(callSuper = true)
public class MysqlField extends BaseField {


    public MysqlField(String fieldName, List<Triple<String, String,Object>>  values, String fieldType) {
        super(fieldName, values, fieldType);
    }

    @Getter
    public enum MysqlFieldType {
        VARCHAR(1, "Text", "VARCHAR(%s)"),
        LONG(2, "Long", "LONG"),
        DOUBLE(3, "BigDecimal", "DOUBLE"),
        INTEGER(4, "Integer", "INT"),
        BOOLEAN(5, "Boolean", "BOOLEAN");

        Integer type;
        String jsonFiledType;
        String mysqlType;


        MysqlFieldType(Integer type, String jsonFiledType, String mysqlType) {
            this.type = type;
            this.jsonFiledType = jsonFiledType;
            this.mysqlType = mysqlType;
        }

        public static MysqlFieldType getByFieldType(String fieldType) {
            for (MysqlFieldType type :
                    values()) {
                if (type.jsonFiledType.equals(fieldType)) {
                    return type;
                }
            }
            ;
            return MysqlFieldType.VARCHAR;
        }
    }


    public static MysqlField creatMysqlField(String fieldName, List<Triple<String, String,Object>>  values, String fieldType) {
        Integer size = null == values ? 30 : values.stream().mapToInt(e -> String.valueOf(e.getRight()).length()).max().orElse(30);
        return new MysqlField(fieldName, values, String.format(MysqlFieldType.getByFieldType(fieldType).getMysqlType(), size));
    }

}

