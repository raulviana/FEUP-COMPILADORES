import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.Test;

public class ImportTest {

    private static String CLASS_WITH_MAIN = "jmm";


    @Test
    public void testImports() {
        CompUtils.testParser("fixtures/public/ImportStressTest.jmm", false, CLASS_WITH_MAIN);
    }


}
