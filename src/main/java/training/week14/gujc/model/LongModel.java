package training.week14.gujc.model;

public class LongModel extends AbstractModel {

    Long value;

    public LongModel(String name, Long value) {
        super(name, ModelType.LONG);
        this.value = value;
    }

    @Override
    public ModelType getModelType() {
        return ModelType.LONG;
    }

    @Override
    public Long getRawValue() {
        return value;
    }
}
