package training.week14.gujc.model;

public class StringModel extends AbstractModel {

    String value;

    public StringModel(String name, String value) {
        super(name, ModelType.STRING);
        this.value = value;
    }

    @Override
    public ModelType getModelType() {
        return ModelType.STRING;
    }

    @Override
    public String getRawValue() {
        return value;
    }
}
