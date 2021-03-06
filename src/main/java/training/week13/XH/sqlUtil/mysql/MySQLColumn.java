package training.week13.XH.sqlUtil.mysql;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import training.week13.XH.sqlUtil.Column;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class MySQLColumn implements Column {
	private boolean constraint;
	private boolean primary;
	private String key;
	private String datatype;
	private String name;
	private MySQLTable subTable;

	public MySQLColumn(String key, String datatype, boolean constraint, boolean primary) {
		this.key = key;
		this.datatype = datatype;
		this.constraint = constraint;
		this.primary = primary;
	}

	public String getName() {
		return ObjectUtils.firstNonNull(name, key);
	}

	@Override
	public Object getValue(Object o) {
		if (o instanceof Map) {
			return ((Map) o).get(this.getKey());
		} else if (o instanceof BigDecimal) {
			return ((BigDecimal) o).doubleValue();
		} else {
			return o;
		}
	}

	public boolean isComposite() {
		return getSubTable() != null;
	}

	public boolean isPrimitive() {
		return getSubTable() == null;
	}
}