import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Jared Malto and Kelvin Nguyen
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /*
     * Sample test cases.
     */

    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAddEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("green");
        assertEquals(mExpected, m);
    }

    // TODO - add test cases for add, changeToExtractionMode, removeFirst,
    // isInInsertionMode, order, and size

    /*
     * Tests for add
     */

    /**
     * Test add by adding to a nonempty SortingMachine
     */
    @Test
    public final void testAddNonEmpty1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "red");
        m.add("red");
        assertEquals(mExpected, m);
    }

    /**
     * Test add by adding to a nonempty SortingMachine
     */
    @Test
    public final void testAddNonEmpty2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "red", "blue", "orange");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "red", "blue", "orange", "purple");
        m.add("purple");
        assertEquals(mExpected, m);
    }

    /**
     * Test add by adding to a nonempty SortingMachine multiple times
     */
    @Test
    public final void testAddNonEmptyMultiple() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "red", "blue");
        m.add("red");
        m.add("blue");
        assertEquals(mExpected, m);
    }

    /*
     * Tests for changeToExtractionMode
     */

    /**
     * Test changeToExtractionMode on an empty SortingMachine
     */
    @Test
    public final void testChangeToExtractionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    /**
     * Test changeToExtractionMode on an nonempty SortingMachine
     */
    @Test
    public final void testChangeToExtractionModeNonEmpty1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green");
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    /**
     * Test changeToExtractionMode on an nonempty SortingMachine
     */
    @Test
    public final void testChangeToExtractionModeNonEmpty2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "blue", "red", "orange", "purple");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "blue", "red", "orange", "purple");
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    /*
     * Tests for removeFirst
     */

    /**
     * Test removeFirst on a SortingMachine with heap size 1
     */
    @Test
    public final void testRemoveFirstSizeOneHeap() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green");

        String removedActual = m.removeFirst();
        String removedExp = mExpected.removeFirst();

        assertEquals(mExpected, m);
        assertEquals(removedExp, removedActual);
    }

    /**
     * Test removeFirst on a SortingMachine with heap size 2
     */
    @Test
    public final void testRemoveFirstSizeTwoHeap() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "yellow");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "yellow");

        String removedActual = m.removeFirst();
        String removedExp = mExpected.removeFirst();

        assertEquals(mExpected, m);
        assertEquals(removedExp, removedActual);
    }

    /**
     * Test removeFirst on a SortingMachine with heap size 5
     */
    @Test
    public final void testRemoveFirstSizeFiveHeap() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "yellow", "blue", "red", "purple", "orange");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "yellow", "blue", "red", "purple", "orange");

        String removedActual = m.removeFirst();
        String removedExp = mExpected.removeFirst();

        assertEquals(mExpected, m);
        assertEquals(removedExp, removedActual);
    }

    /*
     * Tests for isInInsertionMode
     */

    /**
     * Test isInInsertionMode when it is true and heap is empty
     */
    @Test
    public final void testisInInsertionModeTrueEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);

        boolean actual = m.isInInsertionMode();
        boolean expected = mExpected.isInInsertionMode();

        assertEquals(actual, expected);
        assertEquals(m, mExpected);
    }

    /**
     * Test isInInsertionMode when it is false and heap is empty
     */
    @Test
    public final void testisInInsertionModeFalseEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);

        boolean actual = m.isInInsertionMode();
        boolean expected = mExpected.isInInsertionMode();

        assertEquals(actual, expected);
        assertEquals(m, mExpected);
    }

    /**
     * Test isInInsertionMode when it is true and heap is not empty
     */
    @Test
    public final void testisInInsertionModeTrueNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "yellow", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "yellow", "blue");

        boolean actual = m.isInInsertionMode();
        boolean expected = mExpected.isInInsertionMode();

        assertEquals(actual, expected);
        assertEquals(m, mExpected);
    }

    /**
     * Test isInInsertionMode when it is false and heap is not empty
     */
    @Test
    public final void testisInInsertionModeFalseNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "yellow", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "yellow", "blue");

        boolean actual = m.isInInsertionMode();
        boolean expected = mExpected.isInInsertionMode();

        assertEquals(actual, expected);
        assertEquals(m, mExpected);
    }

    /*
     * Tests for order
     */

    /**
     * Test order when isInInsertionMode is true and heap is empty
     */
    @Test
    public final void testOrderTrueEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);

        Comparator<String> actual = m.order();
        Comparator<String> expected = mExpected.order();

        assertEquals(actual, expected);
        assertEquals(m, mExpected);
    }

    /**
     * Test order when isInInsertionMode is false and heap is empty
     */
    @Test
    public final void testOrderFalseEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);

        Comparator<String> actual = m.order();
        Comparator<String> expected = mExpected.order();

        assertEquals(actual, expected);
        assertEquals(m, mExpected);
    }

    /**
     * Test order when isInInsertionMode is true and heap is not empty
     */
    @Test
    public final void testOrderTrueNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "yellow", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "yellow", "blue");

        Comparator<String> actual = m.order();
        Comparator<String> expected = mExpected.order();

        assertEquals(actual, expected);
        assertEquals(m, mExpected);
    }

    /**
     * Test order when isInInsertionMode is false and heap is not empty
     */
    @Test
    public final void testOrderFalseNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "yellow", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "yellow", "blue");

        Comparator<String> actual = m.order();
        Comparator<String> expected = mExpected.order();

        assertEquals(actual, expected);
        assertEquals(m, mExpected);
    }

    /*
     * Tests for size.
     */

    /**
     * Test size method of 0 while in insertion mode.
     */
    @Test
    public final void testSizeEmptyInsertionMode() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExp = this.constructorRef(ORDER);

        int sizeAct = m.size();
        int sizeExp = mExp.size();

        assertTrue(sizeExp == sizeAct);
        assertEquals(mExp, m);

    }

    /**
     * Test size method of 1 while in insertion mode.
     */
    @Test
    public final void testSizeOneInsertionMode() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExp = this.createFromArgsRef(ORDER, true,
                "green");

        int sizeAct = m.size();
        int sizeExp = mExp.size();

        assertTrue(sizeExp == sizeAct);
        assertEquals(mExp, m);

    }

    /**
     * Test size method of 1 while in insertion mode.
     */
    @Test
    public final void testSizeFiveInsertionMode() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExp = this.createFromArgsRef(ORDER, true,
                "green");

        int sizeAct = m.size();
        int sizeExp = mExp.size();

        assertTrue(sizeExp == sizeAct);
        assertEquals(mExp, m);

    }

    /**
     * Test size method of 0 while in insertion mode.
     */
    @Test
    public final void testSizeEmptyExtractionMode() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExp = this.constructorRef(ORDER);

        m.changeToExtractionMode();
        mExp.changeToExtractionMode();

        int sizeAct = m.size();
        int sizeExp = mExp.size();

        assertTrue(sizeExp == sizeAct);
        assertEquals(mExp, m);

    }

    /**
     * Test size method of 1 while in insertion mode.
     */
    @Test
    public final void testSizeOneExtractionMode() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> mExp = this.createFromArgsRef(ORDER, false,
                "green");

        int sizeAct = m.size();
        int sizeExp = mExp.size();

        assertTrue(sizeExp == sizeAct);
        assertEquals(mExp, m);

    }

    /**
     * Test size method of 1 while in insertion mode.
     */
    @Test
    public final void testSizeFiveExtractionMode() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "red", "blue", "orange", "brown");
        SortingMachine<String> mExp = this.createFromArgsRef(ORDER, false,
                "green", "red", "blue", "orange", "brown");

        int sizeAct = m.size();
        int sizeExp = mExp.size();

        assertTrue(sizeExp == sizeAct);
        assertEquals(mExp, m);

    }

}
