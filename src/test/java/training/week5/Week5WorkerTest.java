package training.week5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

@RunWith(Parameterized.class)
public class Week5WorkerTest {
	private Week5Worker worker;

	public Week5WorkerTest(Class<? extends Week5Worker> clazz) throws ReflectiveOperationException {
		worker = clazz.newInstance();
	}

	@Parameterized.Parameters(name = "{0}")
	public static Object[][] parameters() {
		return new Object[][] {
				{ Week5GJC.class },
				{ Week5HX.class },
				{ Week5FZ.class },
				{ Week5BH.class },
				};
	}

	@Test
	public void test() {

		String inputPath = "src/main/java/training/week5/input1.xlsx";
		String outputPath = "src/main/java/training/week5/" + worker.getClass().getSimpleName() + "-Output.xlsx";

		worker.compute(new File(inputPath), new File(outputPath));

	}

}