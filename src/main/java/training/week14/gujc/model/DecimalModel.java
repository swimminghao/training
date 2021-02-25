package training.week14.gujc.model;

import java.math.BigDecimal;

public class DecimalModel extends AbstractModel {

    BigDecimal value;

    public DecimalModel(String name, BigDecimal value) {
        super(name, ModelType.DECIMAL);
        this.value = value;
    }

    @Override
    public ModelType getModelType() {
        return ModelType.DECIMAL;
    }

    @Override
    public BigDecimal getRawValue() {
        return value;
    }
}
