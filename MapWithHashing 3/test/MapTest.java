import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Jared Malto and Kelvin Nguyen
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    //Tests for constructor.

    /**
     * Test of the constructor with no parameters.
     */
    @Test
    public final void testConstructorNoArgs() {
        Map<String, String> mAct = this.constructorTest();
        Map<String, String> mExp = this.constructorRef();
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the constructor with one parameter.
     */
    @Test
    public final void testConstructorOneArg() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a");
        Map<String, String> mExp = this.createFromArgsRef("1", "a");
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the constructor with multiple parameters.
     */
    @Test
    public final void testConstructorMultipleArgs() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        Map<String, String> mExp = this.createFromArgsRef("1", "a", "2", "b",
                "3", "c");
        assertEquals(mExp, mAct);
    }

    //Tests for add method.

    /**
     * Test of the add with empty mAct.
     */
    @Test
    public final void testAddEmpty() {

        Map<String, String> mAct = this.createFromArgsTest();
        Map<String, String> mExp = this.createFromArgsRef("1", "a");
        mAct.add("1", "a");
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the add with non-empty mAct.
     */
    @Test
    public final void testAddNonEmpty() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a");
        Map<String, String> mExp = this.createFromArgsRef("1", "a", "2", "b");
        mAct.add("2", "b");
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the add with multiple calls of add.
     */
    @Test
    public final void testAddMultipleCalls() {

        Map<String, String> mAct = this.createFromArgsTest();
        Map<String, String> mExp = this.createFromArgsRef("1", "a", "2", "b",
                "3", "c");
        mAct.add("1", "a");
        mAct.add("2", "b");
        mAct.add("3", "c");
        assertEquals(mExp, mAct);
    }

    //Test for remove method.

    /**
     * Test of the remove with with an empty mExp.
     */
    @Test
    public final void testRemoveEmpty() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a");
        Map<String, String> mExp = this.createFromArgsRef();
        mAct.remove("1");
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the remove with with an non-empty mExp.
     */
    @Test
    public final void testRemoveNonEmpty() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        Map<String, String> mExp = this.createFromArgsRef("1", "a", "3", "c");
        mAct.remove("2");
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the remove with multiple calls of remove.
     */
    @Test
    public final void testRemoveMultipleCalls() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        Map<String, String> mExp = this.createFromArgsRef("1", "a");
        mAct.remove("3");
        mAct.remove("2");
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the remove with multiple calls of remove to empty mExp.
     */
    @Test
    public final void testRemoveMultipleCallsWithEmptyNexp() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        Map<String, String> mExp = this.createFromArgsRef();
        mAct.remove("3");
        mAct.remove("2");
        mAct.remove("1");
        assertEquals(mExp, mAct);
    }

    //Test for removeAny method.

    /**
     * Test of the removeAny on empty map.
     */
    @Test
    public final void testRemoveAnyEmptyMact() {

        Map<String, String> mAct = this.createFromArgsTest();
        Map<String, String> mExp = this.createFromArgsRef();
        mAct.removeAny();
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the removeAny on map with size 1.
     */
    @Test
    public final void testRemoveAnyEmptymExp() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a");
        Map<String, String> mExp = this.createFromArgsRef();
        mAct.removeAny();
        assertEquals(mExp, mAct);
    }

    /**
     * Test of the removeAny on non-empty mExp.
     */
    @Test
    public final void testRemoveAnyNonEmptymExp() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b");
        Map<String, String> mExp = this.createFromArgsRef("2", "b");
        mAct.removeAny();
        assertEquals(mExp, mAct);
    }

    //Tests for value method.

    /**
     * Test of the value method on map with size 1.
     */
    @Test
    public final void testValueOnMapWithSizeOne() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a");
        String expected = mAct.value("1");
        assertEquals(expected, "a");
    }

    /**
     * Test of the value method on map with size 3.
     */
    @Test
    public final void testValueOnMapWithSizeThree() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        String expected = mAct.value("3");
        assertEquals(expected, "c");
    }

    /**
     * Test of the value method on map with size 3.
     */
    @Test
    public final void testValueOnMapWithSizeThree2() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        String expected = mAct.value("2");
        assertEquals(expected, "b");
    }

    //Test for HasKey method.

    /**
     * Test of the HasKey method on empty map.
     */
    @Test
    public final void testHasValueEmptyMap() {

        Map<String, String> mAct = this.createFromArgsTest();
        Boolean expected = mAct.hasKey("1");
        assertEquals(expected, false);
    }

    /**
     * Test of the HasKey method on non-empty map.
     */
    @Test
    public final void testHasValueNonEmptyMap() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        Boolean expected = mAct.hasKey("1");
        assertEquals(expected, true);
    }

    /**
     * Test of the HasKey method on non-empty map.
     */
    @Test
    public final void testHasValueNonEmptyMap2() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        Boolean expected = mAct.hasKey("3");
        assertEquals(expected, true);
    }

    /**
     * Test of the HasKey method on non-empty map with an invalid key.
     */
    @Test
    public final void testHasValueNonEmptyMapNoKey() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        Boolean expected = mAct.hasKey("4");
        assertEquals(expected, false);
    }

    //Tests for size method.

    /**
     * Test of the size method on empty map.
     */
    @Test
    public final void testSizeEmptyMap() {

        Map<String, String> mAct = this.createFromArgsTest();
        int expectedSize = mAct.size();
        assertEquals(expectedSize, 0);
    }

    /**
     * Test of the size method on non-empty map with size 1.
     */
    @Test
    public final void testSizeNonEmptyMapSize1() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a");
        int expectedSize = mAct.size();
        assertEquals(expectedSize, 1);
    }

    /**
     * Test of the size method on non-empty map with size 3.
     */
    @Test
    public final void testSizeNonEmptyMapSize3() {

        Map<String, String> mAct = this.createFromArgsTest("1", "a", "2", "b",
                "3", "c");
        int expectedSize = mAct.size();
        assertEquals(expectedSize, 3);
    }
}
