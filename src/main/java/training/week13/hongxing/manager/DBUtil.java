package training.week13.hongxing.manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@DBUtil.JDBCConfig(ip = "47.100.94.134", port = 3001, database = "olympus_playground", userName = "aesir", password = "Eh3NgmyzxxhhohFq")
public class DBUtil {
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		JDBCConfig config = DBUtil.class.getAnnotation(JDBCConfig.class);
		String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s", config.ip(), config.port(), config.database(), config.charset());
		return DriverManager.getConnection(url, config.userName(), config.password());
	}

	@Target({ ElementType.METHOD, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface JDBCConfig {
		String ip();

		int port() default 3306;

		String database();

		String charset() default "UTF-8";

		String userName();

		String password();
	}
}
