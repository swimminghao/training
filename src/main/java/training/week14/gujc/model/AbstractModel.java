package training.week14.gujc.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractModel implements BaseModel {
    @Getter
    String name;
    @Getter
    ModelType type;

}
