package training.week14.gujc.table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import training.week13.gujc.BaseNode;
import training.week13.gujc.ComplexNode;
import training.week13.gujc.NodeType;
import training.week13.gujc.SimpleNode;
import training.week13.gujc.node.*;
import training.week14.gujc.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ModelTable {
    private final static String PRE_NAME = "gujc_";
    @Getter
    String name;
    private boolean hasParent = false;
    @Getter
    private String parentCode;

    @Getter
    private String ukCode;

    @Getter
    private Object value;

    private List<BaseModel> models = new ArrayList<>();

    ModelTable(BaseNode node, ModelTable parent) {
        if (parent != null) {
            this.parentCode = parent.getUkCode();
            this.name = parent.getName() + "." + node.getName();
            hasParent = true;
        } else {
            this.name = PRE_NAME + node.getName();
        }
        this.ukCode = UUID.randomUUID().toString();
        if (node.getType() == NodeType.OBJECT) {
            List<BaseNode> children = ((ComplexNode) node).getChildren();
            for (BaseNode child : children) {
                switch (child.getType()) {
                    case LONG:
                        this.models.add(new LongModel(child.getName(), ((LongNode) child).getValue()));
                        break;
                    case DECIMAL:
                        this.models.add(new DecimalModel(child.getName(),  ((DecimalNode) child).getValue()));
                        break;
                    case STRING:
                        this.models.add(new StringModel(child.getName(),  ((StringNode) child).getValue()));
                        break;
                    case BOOLEAN:
                        this.models.add(new BooleanModel(child.getName(),  ((BooleanNode) child).getValue()));
                        break;
                    case EMPTY:
                        break;
                }
            }
        } else if (node.getType() == NodeType.ARRAY) {
            //TODO
        }

    }

    public String getCreateSql() {
        StringBuilder sb = new StringBuilder("USE gujc_test;\n");

        sb.append("CREATE TABLE `").append(name).append("` (\n");
        sb.append("  id bigint(20) unsigned NOT NULL AUTO_INCREMENT,\n");
        sb.append("  ukCode varchar(36) NOT NULL COMMENT '',\n");
        if (hasParent) {
            sb.append("  parentCode varchar(36) NOT NULL COMMENT '',\n");
        }
        for (BaseModel model : models) {
            sb.append("  `").append(model.getName()).append("` ");
            switch (model.getModelType()) {
                case LONG:
                    sb.append("bigint(20)");
                    break;
                case DECIMAL:
                    sb.append("decimal(20,10)");
                    break;
                case STRING:
                    sb.append("varchar(512)");
                    break;
                case BOOLEAN:
                    sb.append("bit(1)");
                    break;
            }
            sb.append(" DEFAULT NULL COMMENT '',\n");
        }
        sb.append("  PRIMARY KEY (id),\n");
        if (hasParent) {
            sb.append("  KEY parentCode (parentCode) USING BTREE,\n");
        }
        sb.append("  UNIQUE KEY ukCode (ukCode)\n");
        sb.append(") ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='';\r\n");

        return sb.toString();
    }

    public String getInsertSql() {
        String columsStr = models.stream().map(m -> "`" + m.getName() + "`").collect(Collectors.joining(","));
        columsStr = hasParent ? "ukCode, parentCode," + columsStr : "ukCode," + columsStr;
        String valuesStr = models.stream()
                .map(m -> m.getModelType() != ModelType.STRING ? m.getRawValue().toString() : "'" + m.getRawValue().toString() + "'")
                .collect(Collectors.joining(","));
        valuesStr = "'" + ukCode + "'," + (hasParent ? "'" + parentCode + "'," : "") + valuesStr;
        String sql = String.format("INSERT INTO `%s` (%s) VALUES (%s);", name, columsStr, valuesStr);
        return sql;
    }

    public static void main(String[] args) {
        ModelTable mt = new ModelTable();
        mt.ukCode = "100";
        mt.name = "test1";
        mt.hasParent = true;
        mt.parentCode = "10";
        mt.models.add(new LongModel("numLong", 123L));
        mt.models.add(new DecimalModel("numDouble", BigDecimal.valueOf(123.123D)));
        mt.models.add(new BooleanModel("boo", true));
        mt.models.add(new StringModel("str", "哈哈哈"));

        System.out.println();

        System.out.println(mt.getCreateSql());

        System.out.println(mt.getInsertSql());
    }
}
