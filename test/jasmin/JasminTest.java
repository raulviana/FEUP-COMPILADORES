package jasmin;



import org.junit.Test;

public class JasminTest {
	

	@Test
    public void testFindMaximum() {
		JasminUtils.testJmm("fixtures/public/FindMaximum.jmm", "Result: 28");
    }

	@Test
    public void testHelloWorld() {
		JasminUtils.testJmm("fixtures/public/HelloWorld.jmm", "Hello, World!");
    }

	@Test
    public void testMonteCarloPi() {
		JasminUtils.testJmm("fixtures/public/MonteCarloPi.jmm", "Insert number: Result: 0", "-1\n");
    }

	@Test
    public void testQuickSort() {
		JasminUtils.testJmm("fixtures/public/QuickSort.jmm", JasminUtils.getResource("fixtures/public/QuickSort.txt"));
    }

	@Test
    public void testSimple() {
		JasminUtils.testJmm("fixtures/public/Simple.jmm", "30");
    }

	@Test
    public void testTicTacToe() {
		JasminUtils.testJmm("fixtures/public/TicTacToe.jmm", JasminUtils.getResource("fixtures/public/TicTacToe.txt"), JasminUtils.getResource("fixtures/public/TicTacToe.input"));
	}

	@Test
    public void testWhileAndIF() {
		JasminUtils.testJmm("fixtures/public/WhileAndIF.jmm", JasminUtils.getResource("fixtures/public/WhileAndIF.txt"));
    }


	@Test
	public void testGuessNumber() {
		JasminUtils.testJmm("fixtures/CP3_Tests/GuessNumber/GuessNumber.jmm", JasminUtils.getResource("fixtures/CP3_Tests/GuessNumber/GuessNumber.output"), JasminUtils.getResource("fixtures/CP3_Tests/GuessNumber/GuessNumber.input"));
	}

	@Test
	public void testConstPropOpt() {
		JasminUtils.testJmm("fixtures/CP3_Tests/ConstPropOpt/ConstPropOpt.jmm", JasminUtils.getResource("fixtures/CP3_Tests/ConstPropOpt/ConstPropOpt.output"));
	}

	@Test
	public void testArray() {
		JasminUtils.testJmm("fixtures/CP3_Tests/ArrayTest/ArrayTest.jmm", JasminUtils.getResource("fixtures/CP3_Tests/ArrayTest/ArrayTest.output"));
	}

	@Test
	public void testConditional() {
		JasminUtils.testJmm("fixtures/CP3_Tests/ConditionalTest/ConditionalTest.jmm", JasminUtils.getResource("fixtures/CP3_Tests/ConditionalTest/ConditionalTest.output"));
	}

	@Test
	public void testExample() {
		JasminUtils.testJmm("fixtures/CP3_Tests/Example/Example.jmm", JasminUtils.getResource("fixtures/CP3_Tests/Example/Example.output"));
	}

	@Test
	public void testOverview() {
		JasminUtils.testJmm("fixtures/CP3_Tests/Overview/Overview.jmm", JasminUtils.getResource("fixtures/CP3_Tests/Overview/Overview.output"));
	}


	@Test
	public void testSumDigits() {
		JasminUtils.testJmm("fixtures/CP3_Tests/SumDigits/SumDigits.jmm", JasminUtils.getResource("fixtures/CP3_Tests/SumDigits/SumDigits.output"), JasminUtils.getResource("fixtures/CP3_Tests/SumDigits/SumDigits.input"));
	}


	@Test
	public void testChild() {
		JasminUtils.testJmm("fixtures/CP3_Tests/Person/Child.jmm", JasminUtils.getResource("fixtures/CP3_Tests/Person/Child.output"));
	}

	@Test
	public void testConstructors() {
		JasminUtils.testJmm("fixtures/CP3_Tests/Person/Constructors.jmm", JasminUtils.getResource("fixtures/CP3_Tests/Person/Constructors.output"));
	}


}
