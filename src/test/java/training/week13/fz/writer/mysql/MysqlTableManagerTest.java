//package training.week13.fz.writer.mysql;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.wayue.olympus.common.IOUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import training.week13.fz.CompositeNode;
//import training.week13.fz.SchemaNode;
//import training.week13.fz.SchemaNodes;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLEncoder;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.stream.StreamSupport;
//
//@Slf4j
//public class MysqlTableManagerTest {
//	@Test
//	public void test() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
//		SchemaNode root;
//		try (InputStream is = IOUtil.inputStream("week13/json-20191225.txt")) {
//			Iterable<JSONObject> jsons = StreamSupport
//					.stream(IOUtil.readLines(is).spliterator(), false)
//					.map(JSON::parseObject)
//					::iterator;
//			root = SchemaNodes.parseIterative("ROOT", jsons);
//			System.out.println(root.prettyPrint());
//		}
//		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//		String server = "47.100.94.134:3001/", schema = "olympus_playground", user = "aesir", pass = "Eh3NgmyzxxhhohFq";
//		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + server + schema + "?user=" + URLEncoder.encode(user, "utf-8") + "&password=" + URLEncoder.encode(pass, "utf-8"))) {
//			ResultSet resultSet = connection
//					.createStatement()
//					.executeQuery("select *\n" +
//					              "from information_schema.tables\n" +
//					              "where table_type = 'BASE TABLE'\n" +
//					              "  and TABLE_SCHEMA = 'olympus_playground'\n" +
//					              "  and TABLE_NAME like 'fz_%'");
//			for (; resultSet.next(); ) {
//				String table_name = resultSet.getString("TABLE_NAME");
//				log.info("drop " + table_name);
//				connection.createStatement().execute(String.format("DROP TABLE IF EXISTS `%s`", table_name));
//			}
//			try (MySQLTable table = new MySQLTableManager(connection).create((CompositeNode) root)) {
//				table.open();
//				try (InputStream is = IOUtil.inputStream("week13/json-20191225.txt")) {
//					for (JSONObject json : (Iterable<JSONObject>) StreamSupport
//							.stream(IOUtil.readLines(is).spliterator(), false)
//							.map(JSON::parseObject)
//							::iterator) {
//						table.write(json);
//					}
//				}
//			}
//		}
//	}
//}