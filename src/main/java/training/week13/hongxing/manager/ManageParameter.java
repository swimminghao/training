package training.week13.hongxing.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.function.BinaryOperator;

@AllArgsConstructor
@Builder
@Getter
public class ManageParameter {
	@Builder.Default private final String name = "root";
	@Builder.Default private final String arrayElementName = "element";
	@Builder.Default private final String pkName = "ID";
	@Builder.Default private final String arrayIndexName = "INDEX";
	@Builder.Default private final String parentPkName = "PID";
	@Builder.Default private final BinaryOperator<String> tableNameGenerator = (parentTableName, key) -> parentTableName + "." + key;
}
