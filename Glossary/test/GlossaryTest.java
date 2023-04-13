import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Tests of the Glossary class.
 *
 * @author Jared malto
 *
 */

public class GlossaryTest {

    /*
     * Tests of the generateElements method.
     */

    /**
     * Base case. Takes a single element and adds it to the set.
     */
    @Test
    public void generateElementsTest1() {
        //testing the shortest possible input
        String testStr = "a";
        Set<Character> expected = new Set1L<>();
        expected.add('a');

        //the incoming set should contain elements, since it is being replaced
        Set<Character> actual = expected.newInstance();
        actual.add('a');
        actual.add('b');
        actual.add('c');

        //call method
        Glossary.generateElements(testStr, actual);
        //assert statement
        assertEquals(expected, actual);
    }

    /**
     * Routine case. Takes in a test string with repeat elements.
     */
    @Test
    public void generateElementsTest2() {
        //takes average input
        String testStr = "abbcdefgg";
        Set<Character> expected = new Set1L<>();
        expected.add('a');
        expected.add('b');
        expected.add('c');
        expected.add('d');
        expected.add('e');
        expected.add('f');
        expected.add('g');

        //the incoming set should contain elements, since it is being replaced
        Set<Character> actual = expected.newInstance();
        actual.add('a');
        actual.add('b');
        actual.add('c');

        //call method
        Glossary.generateElements(testStr, actual);
        //assert statement
        assertEquals(expected, actual);
    }

    /**
     * Routine case. Takes in a test string with no repeat elements.
     */
    @Test
    public void generateElementsTest3() {
        //takes average input
        String testStr = "abcdefg";
        Set<Character> expected = new Set1L<>();
        expected.add('a');
        expected.add('b');
        expected.add('c');
        expected.add('d');
        expected.add('e');
        expected.add('f');
        expected.add('g');

        //the incoming set should contain elements, since it is being replaced
        Set<Character> actual = expected.newInstance();
        actual.add('a');
        actual.add('b');
        actual.add('c');

        //call method
        Glossary.generateElements(testStr, actual);
        //assert statement
        assertEquals(expected, actual);
    }

    /**
     * Challenging case. Takes in a test string with no repeat elements.
     */
    @Test
    public void generateElementsTest4() {
        //takes a really long arbitrary input with variety of characters
        String testStr = "abcdefghijklmnopqrstuvwxyz`1234567890-=[]";
        Set<Character> expected = new Set1L<>();
        for (int i = 0; i < testStr.length(); i++) {
            expected.add(testStr.charAt(i));
        }

        //the incoming set should contain elements, since it is being replaced
        Set<Character> actual = expected.newInstance();
        actual.add('a');
        actual.add('b');
        actual.add('c');
        //call method
        Glossary.generateElements(testStr, actual);
        //assert statement
        assertEquals(expected, actual);
    }

    /**
     * Challenging case. Takes in a test string with repeat elements.
     */
    @Test
    public void generateElementsTest5() {
        //takes a really long arbitrary input with variety of characters
        String testStr = "aabcdefghijklmnopqrstuvwxyz`1234567890-=[]]";
        Set<Character> expected = new Set1L<>();
        for (int i = 1; i < testStr.length() - 1; i++) {
            expected.add(testStr.charAt(i));
        }

        //the incoming set should contain elements, since it is being replaced
        Set<Character> actual = expected.newInstance();
        actual.add('a');
        actual.add('b');
        actual.add('c');

        //call method
        Glossary.generateElements(testStr, actual);
        //assert statement
        assertEquals(expected, actual);
    }

    /*
     * Test of the nextWordOrSeparator method.
     */

    /**
     * Base case of nextWordOrSeparator. My input is simply a space and should
     * return a space.
     */
    @Test
    public void nextWordOrSeparatorTest1() {
        String testStr = " ";
        /*
         * copied from glossary implementation
         */
        //create separator set to use in conjunction with nextwordorseparator
        Set<Character> separatorSet = new Set1L<>();
        //have the separator strings, spaces, tabs, newlines
        final String separators = " \t, , \n, \r";
        //make a separator set
        Glossary.generateElements(separators, separatorSet);

        //i expect a single space returned
        String expected = " ";

        //call method
        String actual = Glossary.nextWordOrSeparator(testStr, 0, separatorSet);

        assertEquals(expected, actual);
    }

    /**
     * Routine case of nextWordOrSeparator. My input is simply a space and
     * should return a space.
     */
    @Test
    public void nextWordOrSeparatorTest2() {
        String testStr = "This is a test.";

        //create separator set to use in conjunction with nextwordorseparator
        Set<Character> separatorSet = new Set1L<>();
        //have the separator strings, spaces, tabs, newlines
        final String separators = " \t, , \n, \r";
        //make a separator set
        Glossary.generateElements(separators, separatorSet);
        //i expect this to be returned, as it is the first word
        String expected = "This";

        //call method
        String actual = Glossary.nextWordOrSeparator(testStr, 0, separatorSet);

        assertEquals(expected, actual);
    }

    /**
     * Routine case of nextWordOrSeparator. The input is a string with start
     * index in the middle.
     */
    @Test
    public void nextWordOrSeparatorTest3() {
        String testStr = "This is a test. bing bong";
        final int startPos = 10;
        /*
         * copied from glossary implementation
         */
        //create separator set to use in conjunction with nextwordorseparator
        Set<Character> separatorSet = new Set1L<>();
        //have the separator strings, spaces, tabs, newlines
        final String separators = " \t, , \n, \r";
        //make a separator set
        Glossary.generateElements(separators, separatorSet);

        //i expect test. to be returned, as it starts at the position and is the last word
        String expected = "test.";

        //call method
        String actual = Glossary.nextWordOrSeparator(testStr, startPos,
                separatorSet);

        assertEquals(expected, actual);
    }

    /**
     * Challenging case of nextWordOrSeparator. This input is super random and
     * long.
     */
    @Test
    public void nextWordOrSeparatorTest4() {
        String testStr = "This is a jrgjsdbaGNER$$$rgsdn;ghil8w4h8wp48tw"
                + "gwughweiufhskdghwipug3877&^^^&[][ighiu&^DJLSHDG. bing bong";
        final int startPos = 10;
        /*
         * copied from glossary implementation
         */
        //create separator set to use in conjunction with nextwordorseparator
        Set<Character> separatorSet = new Set1L<>();
        //have the separator strings, spaces, tabs, newlines
        final String separators = " \t, , \n, \r";
        //make a separator set
        Glossary.generateElements(separators, separatorSet);

        /*
         * i expect whatever this is to be returned, as it starts at the
         * position and is the last word
         */
        String expected = "jrgjsdbaGNER$$$rgsdn;ghil8w4h8wp48tw"
                + "gwughweiufhskdghwipug3877&^^^&[][ighiu&^DJLSHDG.";

        //call method
        String actual = Glossary.nextWordOrSeparator(testStr, startPos,
                separatorSet);

        assertEquals(expected, actual);
    }

    /*
     * Tests of the mapInput method.
     */

    /**
     * Base case of mapInput method.
     */
    @Test
    public void mapInputTest1() {
        SimpleReader readFile = new SimpleReader1L("testFiles/testInput2.txt");
        Map<String, String> actual = Glossary.mapInput(readFile);

        Map<String, String> expected = actual.newInstance();
        expected.add("A", "B");

        assertEquals(expected, actual);
        assertTrue(readFile.atEOS());
    }

    /**
     * Routine case of mapInput method.
     */
    @Test
    public void mapInputTest2() {
        SimpleReader readFile = new SimpleReader1L("testFiles/testInput.txt");
        Map<String, String> actual = Glossary.mapInput(readFile);

        Map<String, String> expected = actual.newInstance();
        expected.add("Beep", "Boop Beep Boop.");
        expected.add("This is a test", "Hello, grader. Nice to see you again.");

        assertEquals(expected, actual);
        assertTrue(readFile.atEOS());
    }

    /*
     * Tests of the alphabetizeTerms method.
     */

    /*
     * Useful comparator for the alphabetizeTerms method parameter. This needs
     * to be imported because alphabetizeTerms requires a comparator as a
     * parameter.
     */

    /**
     * Compare {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareToIgnoreCase(o2);
        }
    }

    /**
     * Base case of alphabetizeTerms method with map of size 0.
     */
    @Test
    public void alphabetizeTermsTest1() {
        //an empty input should result in an empty queue
        Map<String, String> mapActual = new Map1L<>();
        Map<String, String> mapExpected = new Map1L<>();
        Comparator<String> cs = new StringLT();

        Queue<String> qActual = Glossary.alphabetizeTerms(mapActual, cs);
        Queue<String> qExpected = new Queue1L<>();

        assertEquals(qExpected, qActual);
        assertEquals(mapExpected, mapActual);
    }

    /**
     * Routine case of alphabetizeTerms method. Some capital letters thrown in
     * there.
     */
    @Test
    public void alphabetizeTermsTest2() {
        //create input map. This input is letters a-h, so this should be easy
        Map<String, String> mapActual = new Map1L<>();
        mapActual.add("h", "1");
        mapActual.add("a", "1");
        mapActual.add("b", "1");
        mapActual.add("c", "1");
        mapActual.add("g", "1");
        mapActual.add("d", "1");
        mapActual.add("e", "1");
        mapActual.add("F", "1");

        //create expected map
        Map<String, String> mapExpected = new Map1L<>();
        mapExpected.add("h", "1");
        mapExpected.add("a", "1");
        mapExpected.add("b", "1");
        mapExpected.add("c", "1");
        mapExpected.add("g", "1");
        mapExpected.add("d", "1");
        mapExpected.add("e", "1");
        mapExpected.add("F", "1");

        //alphabetizeTerms takes a comparator
        Comparator<String> cs = new StringLT();

        //call method
        Queue<String> qActual = Glossary.alphabetizeTerms(mapActual, cs);
        //the expected should be in alphabetical order
        Queue<String> qExpected = new Queue1L<>();
        qExpected.enqueue("a");
        qExpected.enqueue("b");
        qExpected.enqueue("c");
        qExpected.enqueue("d");
        qExpected.enqueue("e");
        qExpected.enqueue("F");
        qExpected.enqueue("g");
        qExpected.enqueue("h");

        //want queues to be the same
        assertEquals(qExpected, qActual);
        //map is supposed to be restored after method call--check equality
        assertEquals(mapExpected, mapActual);
    }

    /**
     * Challenging case of alphabetizeTerms method. Since the keys are the only
     * thing that really matter, I made the keys extremely long random
     * characters and left the values the same. Some capitals thrown in there.
     */
    @Test
    public void alphabetizeTermsTest3() {
        //create input map
        Map<String, String> mapActual = new Map1L<>();
        mapActual.add("iuegiudsgldgh", "1");
        mapActual.add("eqwiotjskdfjb", "1");
        mapActual.add("sjehgcjbf", "1");
        mapActual.add("wioutgisdf", "1");
        mapActual.add("jgshrkj", "1");
        mapActual.add("aeirugg", "1");
        mapActual.add("ajkbdbgkf", "1");
        mapActual.add("geiurgc", "1");
        mapActual.add("iwurtj", "1");
        mapActual.add("iwuegfmbn", "1");
        mapActual.add("ojlghjdb", "1");
        mapActual.add("oirhiohbnmcrn", "1");
        mapActual.add("gkjerijkcnvmngklsjghklnfgiudfhgiondfkljguor", "1");
        mapActual.add("gwuohgksnfncxghiorshgi", "1");
        mapActual.add("jkbgxnnvjkbhgiusebh", "1");
        mapActual.add("Swjklethvcxnvcxmu", "1");

        //create expected map
        Map<String, String> mapExpected = new Map1L<>();
        mapExpected.add("iuegiudsgldgh", "1");
        mapExpected.add("eqwiotjskdfjb", "1");
        mapExpected.add("sjehgcjbf", "1");
        mapExpected.add("wioutgisdf", "1");
        mapExpected.add("jgshrkj", "1");
        mapExpected.add("aeirugg", "1");
        mapExpected.add("ajkbdbgkf", "1");
        mapExpected.add("geiurgc", "1");
        mapExpected.add("iwurtj", "1");
        mapExpected.add("iwuegfmbn", "1");
        mapExpected.add("ojlghjdb", "1");
        mapExpected.add("oirhiohbnmcrn", "1");
        mapExpected.add("gkjerijkcnvmngklsjghklnfgiudfhgiondfkljguor", "1");
        mapExpected.add("gwuohgksnfncxghiorshgi", "1");
        mapExpected.add("jkbgxnnvjkbhgiusebh", "1");
        mapExpected.add("Swjklethvcxnvcxmu", "1");

        //alphabetizeTerms takes a comparator
        Comparator<String> cs = new StringLT();

        //call method
        Queue<String> qActual = Glossary.alphabetizeTerms(mapActual, cs);
        //the expected should be in alphabetical order
        Queue<String> qExpected = new Queue1L<>();
        qExpected.enqueue("aeirugg");
        qExpected.enqueue("ajkbdbgkf");
        qExpected.enqueue("eqwiotjskdfjb");
        qExpected.enqueue("geiurgc");
        qExpected.enqueue("gkjerijkcnvmngklsjghklnfgiudfhgiondfkljguor");
        qExpected.enqueue("gwuohgksnfncxghiorshgi");
        qExpected.enqueue("iuegiudsgldgh");
        qExpected.enqueue("iwuegfmbn");
        qExpected.enqueue("iwurtj");
        qExpected.enqueue("jgshrkj");
        qExpected.enqueue("jkbgxnnvjkbhgiusebh");
        qExpected.enqueue("oirhiohbnmcrn");
        qExpected.enqueue("ojlghjdb");
        qExpected.enqueue("sjehgcjbf");
        qExpected.enqueue("Swjklethvcxnvcxmu");
        qExpected.enqueue("wioutgisdf");

        //want queues to be the same
        assertEquals(qExpected, qActual);
        //map is supposed to be restored after method call--check equality
        assertEquals(mapExpected, mapActual);
    }

    /*
     * Test of the outputHeader and outputFooter method. There can only be one
     * test done on these since it is printing one thing.
     */

    /**
     * The test of outputHeader.
     */
    @Test
    public void outputHeaderTest() {
        //create an output stream
        SimpleWriter out = new SimpleWriter1L("testFiles/outputHeaderTest.txt");
        //a word to be printed with method call
        String word = "test";
        //call method--out should be updated
        Glossary.outputHeader(out, word);
        //create simpleReaders to read both of the files
        //one to read the file that was called
        SimpleReader readActual = new SimpleReader1L(
                "testFiles/outputHeaderTest.txt");
        //one to read the file that was expected
        SimpleReader readExpected = new SimpleReader1L(
                "testFiles/testOutputHeaderExpected.txt");

        //loop through the files
        while (!readExpected.atEOS() && !readActual.atEOS()) {
            //assertEquals each line of the expected and acual
            assertEquals(readExpected.nextLine(), readActual.nextLine());
        }
        //we want both of the files to be exactly the same, check if they are both at EOS
        assertTrue(readExpected.atEOS());
        assertTrue(readActual.atEOS());

        readActual.close();
        readExpected.close();
    }

    /**
     * The test of outputFooter.
     */
    @Test
    public void outputFooterTest() {
        //create an output stream
        SimpleWriter out = new SimpleWriter1L("testFiles/outputFooterTest.txt");
        //call method--out should be updated
        Glossary.outputFooter(out);
        //create simpleReaders to read both of the files
        //one to read the file that was called
        SimpleReader readActual = new SimpleReader1L(
                "testFiles/outputFooterTest.txt");
        //one to read the file that was expected
        SimpleReader readExpected = new SimpleReader1L(
                "testFiles/outputFooterExpected.txt");

        //loop through the files
        while (!readExpected.atEOS() && !readActual.atEOS()) {
            //assertEquals each line of the expected and acual
            assertEquals(readExpected.nextLine(), readActual.nextLine());
        }
        //we want both of the files to be exactly the same, check if they are both at EOS
        assertTrue(readExpected.atEOS());
        assertTrue(readActual.atEOS());

        readActual.close();
        readExpected.close();
    }

    /*
     * Tests of generateIndex method.
     */

    /**
     * Base case test of generateIndex with 0 elements in queue.
     */
    @Test
    public void generateIndexTest1() {
        //create queue
        Queue<String> qTest = new Queue1L<>();

        //q is suppsed to be restored
        Queue<String> qExp = new Queue1L<>();

        //create an output stream
        SimpleWriter out = new SimpleWriter1L(
                "testFiles/generateIndexActual.txt");
        //call method
        Glossary.generateIndex(qTest, out);

        //read the expected and actual files
        SimpleReader readExpected = new SimpleReader1L(
                "testFiles/generateIndexExpected.txt");
        SimpleReader readActual = new SimpleReader1L(
                "testFiles/generateIndexActual.txt");

        //loop through both inputs and assert they are equal line by line
        while (!readExpected.atEOS() && !readActual.atEOS()) {
            assertEquals(readExpected.nextLine(), readActual.nextLine());
        }
        //want both streams to be at end
        assertTrue(readExpected.atEOS());
        assertTrue(readExpected.atEOS());

        readExpected.close();
        readActual.close();

        //want queues to be the same
        assertEquals(qExp, qTest);

    }

    /**
     * Routine case test of generateIndex.
     */
    @Test
    public void generateIndexTest2() {
        //create queue with list of words--should have them displayed in order
        Queue<String> qTest = new Queue1L<>();
        qTest.enqueue("Somebody");
        qTest.enqueue("Once");
        qTest.enqueue("Told");
        qTest.enqueue("Me");
        //q is suppsed to be restored
        Queue<String> qExp = new Queue1L<>();
        qExp.enqueue("Somebody");
        qExp.enqueue("Once");
        qExp.enqueue("Told");
        qExp.enqueue("Me");

        //create an output stream
        SimpleWriter out = new SimpleWriter1L(
                "testFiles/generateIndexActual2.txt");
        //call method
        Glossary.generateIndex(qTest, out);

        //read the expected and actual files
        SimpleReader readExpected = new SimpleReader1L(
                "testFiles/generateIndexExpected2.txt");
        SimpleReader readActual = new SimpleReader1L(
                "testFiles/generateIndexActual2.txt");

        //loop through both inputs and assert they are equal line by line
        while (!readExpected.atEOS() && !readActual.atEOS()) {
            assertEquals(readExpected.nextLine(), readActual.nextLine());
        }
        //want both streams to be at end
        assertTrue(readExpected.atEOS());
        assertTrue(readExpected.atEOS());

        readExpected.close();
        readActual.close();

        //want queues to be the same
        assertEquals(qExp, qTest);

    }

    /*
     * Tests of outputDefition method.
     */

    /**
     * Base case of outputDefinition method with an empty map and string.
     */
    @Test
    public void outputDefinitionTest1() {
        SimpleWriter outActual = new SimpleWriter1L(
                "testFiles/outputDefinitionActual.txt");
        String term = "";
        String def = "";
        Map<String, String> mapActual = new Map1L<>();
        Map<String, String> mapExpected = new Map1L<>();
        Glossary.outputDefinition(outActual, mapActual, term, def);

        //read the expected and actual files
        SimpleReader readExpected = new SimpleReader1L(
                "testFiles/outputDefinitionExpected.txt");
        SimpleReader readActual = new SimpleReader1L(
                "testFiles/outputDefinitionActual.txt");

        //loop through both inputs and assert they are equal line by line
        while (!readExpected.atEOS() && !readActual.atEOS()) {
            assertEquals(readExpected.nextLine(), readActual.nextLine());
        }
        //want both streams to be at end
        assertTrue(readExpected.atEOS());
        assertTrue(readExpected.atEOS());

        assertEquals(mapExpected, mapActual);

        readExpected.close();
        readActual.close();

    }

    /**
     * Routine case with short definition. In this case, a hyperlink needs to be
     * added to test because it is contained in the map.
     */
    @Test
    public void outputDefinitionTest2() {
        SimpleWriter outActual = new SimpleWriter1L(
                "testFiles/outputDefinitionActual2.txt");
        String term = "test";
        String def = "this is a test";
        Map<String, String> mapActual = new Map1L<>();
        mapActual.add(term, def);
        Map<String, String> mapExpected = new Map1L<>();
        mapExpected.add(term, def);
        Glossary.outputDefinition(outActual, mapActual, term, def);

        //read the expected and actual files
        SimpleReader readExpected = new SimpleReader1L(
                "testFiles/outputDefinitionExpected2.txt");
        SimpleReader readActual = new SimpleReader1L(
                "testFiles/outputDefinitionActual2.txt");

        //loop through both inputs and assert they are equal line by line
        while (!readExpected.atEOS() && !readActual.atEOS()) {
            assertEquals(readExpected.nextLine(), readActual.nextLine());
        }
        //want both streams to be at end
        assertTrue(readExpected.atEOS());
        assertTrue(readExpected.atEOS());

        //map should be restored
        assertEquals(mapExpected, mapActual);

        readExpected.close();
        readActual.close();

    }

    /**
     * Challenging case where multiple terms in the definition are contained
     * within the map.
     */
    @Test
    public void outputDefinitionTest3() {
        SimpleWriter outActual = new SimpleWriter1L(
                "testFiles/outputDefinitionActual3.txt");
        String term = "test1";
        String def = "this is a test1 test2 test3";
        Map<String, String> mapActual = new Map1L<>();
        mapActual.add(term, def);
        mapActual.add("test2", "testing testing");
        mapActual.add("test3", "testing testing testing");
        Map<String, String> mapExpected = new Map1L<>();
        mapExpected.add(term, def);
        mapExpected.add("test2", "testing testing");
        mapExpected.add("test3", "testing testing testing");
        Glossary.outputDefinition(outActual, mapActual, term, def);

        //read the expected and actual files
        SimpleReader readExpected = new SimpleReader1L(
                "testFiles/outputDefinitionExpected3.txt");
        SimpleReader readActual = new SimpleReader1L(
                "testFiles/outputDefinitionActual3.txt");

        //loop through both inputs and assert they are equal line by line
        while (!readExpected.atEOS() && !readActual.atEOS()) {
            assertEquals(readExpected.nextLine(), readActual.nextLine());
        }
        //want both streams to be at end
        assertTrue(readExpected.atEOS());
        assertTrue(readExpected.atEOS());

        //map should be restored
        assertEquals(mapExpected, mapActual);

        readExpected.close();
        readActual.close();

    }
}
