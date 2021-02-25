package training.week13.fz.writer;

import java.util.List;

public interface Table<C extends Column> extends AutoCloseable {
	void addColumn(C column);

	@Override
	void close() throws Exception;

	Object generateID();

	C getColumn(String name);

	List<C> getColumns();

	void open() throws Exception;

	boolean removeColumn(String name);

	void setName(String name);

	void write(Object o) throws Exception;

	void write(Object id, int index, Object o) throws Exception;
}
