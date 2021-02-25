package training.week13.hongxing.manager;

import java.util.List;
import java.util.Map;

public interface Table<C extends Column, T extends Table<C, T>> {
	String getName();

	T getParentTable();

	List<C> getColumns();

	T addColumn(C column);

	T addSubTable(T table);

	Map<String, T> allTables();

	Object generateId();
}
