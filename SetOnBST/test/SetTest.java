import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Jared Malto and Kelvin Nguyen
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new set
     * @ensures constructorRef = {}
     */
    protected abstract Set<String> constructorRef();

    /**
     * Creates and returns a {@code Set<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsTest = [entries in args]
     */
    private Set<String> createFromArgsTest(String... args) {
        Set<String> set = this.constructorTest();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Creates and returns a {@code Set<String>} of the reference implementation
     * type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsRef = [entries in args]
     */
    private Set<String> createFromArgsRef(String... args) {
        Set<String> set = this.constructorRef();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /*
     * Tests for constructor--String
     */

    /**
     * Test empty constructor
     */
    @Test
    public final void testConstructor() {
        Set<String> sActual = this.constructorTest();
        Set<String> sExp = this.constructorRef();

        assertEquals(sExp, sActual);
    }

    /*
     * Tests for add
     */

    /**
     * Test add with adding to an empty set.
     */
    @Test
    public final void testAddElementEmpty() {
        Set<String> sActual = this.createFromArgsTest();
        Set<String> sExp = this.createFromArgsTest("1");

        sActual.add("1");

        assertEquals(sExp, sActual);
    }

    /**
     * Test add with adding to a non-empty set.
     */
    @Test
    public final void testAddElementNonEmpty() {
        Set<String> sActual = this.createFromArgsTest("2", "3", "4");
        Set<String> sExp = this.createFromArgsTest("1", "2", "3", "4");

        sActual.add("1");

        assertEquals(sActual, sExp);
    }

    /*
     * Tests for remove
     */

    /**
     * Test remove with the set only containing one element.
     */
    @Test
    public final void testRemoveOneElement() {
        Set<String> sActual = this.createFromArgsTest("1");
        Set<String> sExp = this.createFromArgsTest();

        String actualElement = sActual.remove("1");

        assertEquals(sExp, sActual);
        assertEquals("1", actualElement);
    }

    /**
     * Test remove with the set containing many elements.
     */
    @Test
    public final void testRemoveManyElement() {
        Set<String> sActual = this.createFromArgsTest("1", "2", "3", "4");
        Set<String> sExp = this.createFromArgsTest("1", "3", "4");

        String actualElement = sActual.remove("2");

        assertEquals(sExp, sActual);
        assertEquals("2", actualElement);
    }

    /*
     * Tests for removeAny
     */

    /**
     * Test removeAny with the set only containing one element.
     */
    @Test
    public final void testRemoveAnyOneElement() {
        Set<String> sActual = this.createFromArgsTest("1");
        Set<String> sExp = this.createFromArgsTest("1");

        String expElement = sActual.removeAny();
        String actualElement = sExp.removeAny();

        assertEquals(sExp, sActual);
        assertEquals(expElement, actualElement);
    }

    /**
     * Test removeAny with the set containing many elements.
     */
    @Test
    public final void testRemoveAnyManyElements() {
        Set<String> sActual = this.createFromArgsTest("The", "Ohio", "State",
                "University", " founded", "in 18", "70");
        Set<String> sExp = this.createFromArgsTest("The", "Ohio", "State",
                "University", " founded", "in 18", "70");

        String expElement = sActual.removeAny();
        String actualElement = sExp.removeAny();

        assertEquals(sExp, sActual);
        assertEquals(expElement, actualElement);
    }

    /*
     * Tests for contains
     */

    /**
     * Test contains method with set containing no elements
     */
    @Test
    public final void testContainsEmpty() {
        Set<String> sActual = this.createFromArgsTest();
        Set<String> sExp = this.createFromArgsRef();

        boolean actual = sActual.contains("");
        boolean exp = sExp.contains("");
        assertEquals(sExp, sActual);
        assertTrue(actual == exp);
        assertTrue(actual == false);
    }

    /**
     * Test contains method with set containing one element when the set does
     * not contain that element.
     */
    @Test
    public final void testContainsOneElementFalse() {
        Set<String> sActual = this.createFromArgsTest("Alpha");
        Set<String> sExp = this.createFromArgsRef("Alpha");

        boolean actual = sActual.contains("Beta");
        boolean exp = sExp.contains("Beta");
        assertEquals(sExp, sActual);
        assertTrue(actual == exp);
        assertTrue(actual == false);
    }

    /**
     * Test contains method with set containing one element when the set does
     * contain that element.
     */
    @Test
    public final void testContainsOneElementTrue() {
        Set<String> sActual = this.createFromArgsTest("Alpha");
        Set<String> sExp = this.createFromArgsRef("Alpha");

        boolean actual = sActual.contains("Alpha");
        boolean exp = sExp.contains("Alpha");
        assertEquals(sExp, sActual);
        assertTrue(actual == exp);
        assertTrue(actual == true);
    }

    /**
     * Test contains method with set containing one element when the set does
     * not contain that element.
     */
    @Test
    public final void testContainsMultipleElementsFalse() {
        Set<String> sActual = this.createFromArgsTest("Alpha", "Beta", "Gamma",
                "Delta", "Epsilon", "Zeta", "Eta", "Theta");
        Set<String> sExp = this.createFromArgsRef("Alpha", "Beta", "Gamma",
                "Delta", "Epsilon", "Zeta", "Eta", "Theta");

        boolean actual = sActual.contains("Zeta");
        boolean exp = sExp.contains("Zeta");
        assertEquals(sExp, sActual);
        assertTrue(actual == exp);
        assertTrue(actual == true);
    }

    /*
     * Tests for size
     */

    /**
     * Test size with an empty set
     */
    @Test
    public final void testSizeEmpty() {
        Set<String> sActual = this.createFromArgsTest();
        int expectedSize = 0;

        assertEquals(expectedSize, sActual.size());
    }

    /**
     * Test size with a set with one element.
     */
    @Test
    public final void testSizeOneElement() {
        Set<String> sActual = this.createFromArgsTest("1");
        int expectedSize = 1;

        assertEquals(expectedSize, sActual.size());
    }

    /**
     * Test size with a set with many elements.
     */
    @Test
    public final void testSizeManyElement() {
        Set<String> sActual = this.createFromArgsTest("1", "2", "3", "4");
        int expectedSize = 4;

        assertEquals(expectedSize, sActual.size());
    }

}
