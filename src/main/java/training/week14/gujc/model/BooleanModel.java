package training.week14.gujc.model;

public class BooleanModel extends AbstractModel {

    Boolean value;

    public BooleanModel(String name, Boolean value) {
        super(name, ModelType.BOOLEAN);
        this.value = value;
    }

    @Override
    public ModelType getModelType() {
        return ModelType.BOOLEAN;
    }

    @Override
    public Boolean getRawValue() {
        return value;
    }
}
