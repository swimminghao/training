package training.week13.hongxing.manager;

import com.alibaba.fastjson.JSON;

public interface TableManager<T extends Table> extends AutoCloseable {

	void initTable(Iterable<JSON> jsons);

	void writeTable() throws ManageException;

	void writeRecord(JSON record) throws ManageException;

	void writeRecords(Iterable<JSON> records) throws ManageException;
}
