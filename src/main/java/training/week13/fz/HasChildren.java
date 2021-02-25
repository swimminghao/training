package training.week13.fz;

import java.util.List;

public interface HasChildren<T> {
	void addChild(T child);

	List<T> getChildren();
}