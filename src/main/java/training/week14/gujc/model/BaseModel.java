package training.week14.gujc.model;

public interface BaseModel {

    ModelType getModelType();

    Object getRawValue();

    //String getValue();

    String getName();
}
