package training.week13.zq.json.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Triple;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C), 2018-2019 善诊
 * BaseField
 *
 * @Author: zhuqiang
 * @Date: 2020/1/4
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseField implements Serializable {

    private String fieldName;
    private List<Triple<String, String,Object>>  values;
    private String fieldType;
}
