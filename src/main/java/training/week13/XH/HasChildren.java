package training.week13.XH;

import java.util.List;

public interface HasChildren<T> {
	void addChild(T child);

	List<T> getChildren();
}
