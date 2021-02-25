package training.week13.XH.nodes;

import lombok.Getter;
import lombok.ToString;
import training.week13.XH.OriginalNode;
import training.week13.XH.GeneralScanException;

import java.math.BigDecimal;

@ToString(callSuper = true)
@Getter
public class NumberNode extends AbstractNode implements OriginalNode {
	@ToString.Exclude protected NumberType numberType;

	public NumberNode(String name, NumberType numberType) {
		super(name, "Number");
		this.numberType = numberType;
	}

	@Override
	public String getType() {
		return numberType.name();
	}

	public enum NumberType {
		INTEGER, LONG, DECIMAL;

		public static NumberType fromValue(Number n) {
			if (n instanceof BigDecimal) {
				return DECIMAL;
			} else if (n instanceof Long) {
				return LONG;
			} else if (n instanceof Integer) {
				return INTEGER;
			} else {
				throw new GeneralScanException("unknown number type: " + n.getClass());
			}
		}

		NumberType merge(NumberType type) {
			return NumberType.values()[Integer.max(ordinal(), type.ordinal())];
		}
	}
}