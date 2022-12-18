import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * JUnit test fixture for {@code NaturalNumber}'s constructors and kernel
 * methods.
 *
 * @author Jared Malto and Kelvin Nguyen
 *
 */
public abstract class NaturalNumberTest {

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new number
     * @ensures constructorTest = 0
     */
    protected abstract NaturalNumber constructorTest();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorTest = i
     */
    protected abstract NaturalNumber constructorTest(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorTest)
     */
    protected abstract NaturalNumber constructorTest(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorTest = n
     */
    protected abstract NaturalNumber constructorTest(NaturalNumber n);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @return the new number
     * @ensures constructorRef = 0
     */
    protected abstract NaturalNumber constructorRef();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorRef = i
     */
    protected abstract NaturalNumber constructorRef(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorRef)
     */
    protected abstract NaturalNumber constructorRef(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorRef = n
     */
    protected abstract NaturalNumber constructorRef(NaturalNumber n);

    /*
     * Test cases for the {@code NaturalNumber}s
     */

    /**
     * Test of the constructor with no parameters.
     */
    @Test
    public final void testConstructorNoArgs() {
        NaturalNumber nAct = this.constructorTest();
        NaturalNumber nExp = this.constructorRef();

        assertEquals(nExp, nAct);
    }

    //Tests for int constructors

    /**
     * Test of the constructor with a simple integer as parameter.
     */
    @Test
    public final void testConstructorIntegerArgs_1() {
        NaturalNumber nAct = this.constructorTest(1);
        NaturalNumber nExp = this.constructorRef(1);

        assertEquals(nExp, nAct);
    }

    /**
     * Test of the constructor with a larger integer as parameter.
     */
    @Test
    public final void testConstructorIntegerArgs_2() {
        final int largerInt = 1981;
        NaturalNumber nAct = this.constructorTest(largerInt);
        NaturalNumber nExp = this.constructorRef(largerInt);

        assertEquals(nExp, nAct);

    }

    /**
     * Test of the constructor with the maximum integer as parameter.
     */
    @Test
    public final void testConstructorIntegerArgs_3() {
        NaturalNumber nAct = this.constructorTest(Integer.MAX_VALUE);
        NaturalNumber nExp = this.constructorRef(Integer.MAX_VALUE);

        assertEquals(nExp, nAct);

    }

    //Tests for the string constructors.

    /**
     * Test of the constructor with string input of "0".
     */
    @Test
    public final void testConstructorStringArgs_1() {
        String natNum = "0";
        NaturalNumber nAct = this.constructorTest(natNum);
        NaturalNumber nExp = this.constructorRef(natNum);

        assertEquals(nExp, nAct);

    }

    /**
     * Test of the constructor with string input of "5".
     */
    @Test
    public final void testConstructorStringArgs_2() {
        String natNum = "5";
        NaturalNumber nAct = this.constructorTest(natNum);
        NaturalNumber nExp = this.constructorRef(natNum);

        assertEquals(nExp, nAct);

    }

    /**
     * Test of the constructor with a very large value.
     */
    @Test
    public final void testConstructorStringArgs_3() {
        String natNum = "2387462387462345987";
        NaturalNumber nAct = this.constructorTest(natNum);
        NaturalNumber nExp = this.constructorRef(natNum);

        assertEquals(nExp, nAct);
    }

    //Tests for the NatuarlNumber constructors

    /**
     * Test of the constructor with default NaturalNumber value.
     */
    @Test
    public final void testConstructorNaturalNumberArgs_1() {
        NaturalNumber nAct = this.constructorTest(new NaturalNumber3());
        NaturalNumber nExp = this.constructorRef(new NaturalNumber2());

        assertEquals(nExp, nAct);
    }

    /**
     * Test of the constructor with single digit NaturalNumber value.
     */
    @Test
    public final void testConstructorNaturalNumberArgs_2() {

        String natNum = "4";
        NaturalNumber nAct = this.constructorTest(new NaturalNumber3(natNum));
        NaturalNumber nExp = this.constructorRef(new NaturalNumber2(natNum));

        assertEquals(nExp, nAct);
    }

    /**
     * Test of the constructor with very large digit NaturalNumber value.
     */
    @Test
    public final void testConstructorNaturalNumberArgs_3() {

        String natNum = "3874563489573489573457983768934768947";
        NaturalNumber nAct = this.constructorTest(new NaturalNumber3(natNum));
        NaturalNumber nExp = this.constructorRef(new NaturalNumber2(natNum));

        assertEquals(nExp, nAct);
    }

    /*
     * Tests of kernel methods using strings.
     */

    /*
     * Tests for the multiplyBy10 method
     */

    /**
     * Test multiplyBy10 on the default value with parameter "0".
     */
    @Test
    public final void testMultiplyBy10_1_String() {
        NaturalNumber nAct = this.constructorTest();
        NaturalNumber nExp = this.constructorRef();

        nAct.multiplyBy10(1);
        nExp.multiplyBy10(1);

        assertEquals(nExp, nAct);
    }

    /**
     * Test multiplyBy10 on the default value with a single digit.
     */
    @Test
    public final void testMultiplyBy10_2_String() {
        final int k = 7;
        NaturalNumber nAct = this.constructorTest();
        NaturalNumber nExp = this.constructorRef();

        nAct.multiplyBy10(k);
        nExp.multiplyBy10(k);

        assertEquals(nExp, nAct);
    }

    /**
     * Test multiplyBy10 on initial value 150 with parameter "5".
     */
    @Test
    public final void testMultiplyBy10_3_String() {
        String natNum = "150";
        final int k = 5;

        NaturalNumber nAct = this.constructorTest(natNum);
        NaturalNumber nExp = this.constructorRef(natNum);

        nAct.multiplyBy10(k);
        nExp.multiplyBy10(k);

        assertEquals(nExp, nAct);
    }

    /**
     * Test multiplyBy10 on a very large value with parameter "3".
     */
    @Test
    public final void testMultiplyBy10_4_String() {
        String natNum = "45827635782346782364782346897";
        final int k = 3;

        NaturalNumber nAct = this.constructorTest(natNum);
        NaturalNumber nExp = this.constructorRef(natNum);

        nAct.multiplyBy10(k);
        nExp.multiplyBy10(k);

        assertEquals(nExp, nAct);
    }

    /*
     * Tests of divideBy10 methods.
     */

    /**
     * Test divideBy10 on no number.
     */
    @Test
    public final void testDivideBy10onEmpty_1_String() {
        NaturalNumber input = this.constructorTest();
        NaturalNumber expected = this.constructorRef();
        int remainder = input.divideBy10();
        final int expRemainder = 0;
        assertTrue(remainder == expRemainder);
        assertEquals(input, expected);
    }

    /**
     * Test divideBy10 with 0 as a parameter.
     */
    @Test
    public final void testDivideBy10onEmpty_2_String() {
        NaturalNumber input = this.constructorTest("0");
        NaturalNumber expected = this.constructorRef("0");
        int remainder = input.divideBy10();
        final int expRemainder = 0;
        assertTrue(remainder == expRemainder);
        assertEquals(input, expected);
    }

    /**
     * Test divideBy10 on "150".
     */
    @Test
    public final void testDivideBy10NonEmpty_1_String() {
        NaturalNumber input = this.constructorTest("150");
        NaturalNumber expected = this.constructorRef("15");
        int remainder = input.divideBy10();
        final int expRemainder = 0;
        assertTrue(remainder == expRemainder);
        assertEquals(input, expected);
    }

    /**
     * Test divideBy10 on a very large number.
     */
    @Test
    public final void testDivideBy10NonEmpty_2_String() {
        NaturalNumber input = this
                .constructorTest("923857892364782364896578346897");
        NaturalNumber expected = this
                .constructorRef("92385789236478236489657834689");
        int remainder = input.divideBy10();
        final int expRemainder = 7;
        assertTrue(remainder == expRemainder);
        assertEquals(input, expected);
    }

    /*
     * Tests for isZero method
     */

    /**
     * Test isZero when true given no parameter.
     */
    @Test
    public final void testisZeroNoParams_String() {
        NaturalNumber nAct = this.constructorTest();
        NaturalNumber nExp = this.constructorRef();

        assertTrue(nAct.isZero() == nExp.isZero());

    }

    /**
     * Test isZero when true given 0 as a parameter.
     */
    @Test
    public final void testisZeroWithParam_1_String() {
        NaturalNumber nAct = this.constructorTest("0");
        NaturalNumber nExp = this.constructorRef("0");

        assertTrue(nAct.isZero() == nExp.isZero());
    }

    /**
     * Test isZero when true given a non-zero number.
     */
    @Test
    public final void testisZeroWithParam_2_String() {
        NaturalNumber nAct = this.constructorTest("5");
        NaturalNumber nExp = this.constructorRef("5");

        assertTrue(nAct.isZero() == nExp.isZero());

    }

    /*
     * Tests of kernel methods using ints.
     */

    /**
     * Test multiplyBy10 on initial value 340 with parameter "5".
     */
    @Test
    public final void testMultiplyBy10_1_Integer() {
        final int natNum = 340;
        final int k = 5;

        NaturalNumber nAct = this.constructorTest(natNum);
        NaturalNumber nExp = this.constructorRef(natNum);

        nAct.multiplyBy10(k);
        nExp.multiplyBy10(k);

        assertEquals(nExp, nAct);
    }

    /**
     * Test multiplyBy10 on the max integer.
     */
    @Test
    public final void testMultiplyBy10_2_Integer() {
        final int natNum = Integer.MAX_VALUE - 3;
        final int k = 3;

        NaturalNumber nAct = this.constructorTest(natNum);
        NaturalNumber nExp = this.constructorRef(natNum);

        nAct.multiplyBy10(k);
        nExp.multiplyBy10(k);

        assertEquals(nExp, nAct);
    }

    /*
     * Tests of divideBy10 methods.
     */

    /**
     * Test divideBy10 with 0 as a parameter.
     */
    @Test
    public final void testDivideBy10onEmpty_Integer() {
        NaturalNumber input = this.constructorTest(0);
        NaturalNumber expected = this.constructorRef(0);
        int remainder = input.divideBy10();
        final int expRemainder = 0;
        assertTrue(remainder == expRemainder);
        assertEquals(input, expected);
    }

    /**
     * Test divideBy10 on 1234.
     */
    @Test
    public final void testDivideBy10NonEmpty_1_Integer() {
        final int natNum = 1234;
        NaturalNumber input = this.constructorTest(natNum);
        NaturalNumber expected = this
                .constructorRef(natNum / NaturalNumber.RADIX);
        int remainder = input.divideBy10();
        final int expRemainder = 4;
        assertTrue(remainder == expRemainder);
        assertEquals(expected, input);
    }

    /**
     * Test divideBy10 on a very large number.
     */
    @Test
    public final void testDivideBy10NonEmpty_2_Integer() {
        NaturalNumber input = this.constructorTest(Integer.MAX_VALUE);
        NaturalNumber expected = this
                .constructorRef(Integer.MAX_VALUE / NaturalNumber.RADIX);
        int remainder = input.divideBy10();
        final int expRemainder = 7;
        assertTrue(remainder == expRemainder);
        assertEquals(expected, input);
    }

    /*
     * Tests for isZero method
     */

    /**
     * Test isZero when true given 0 as a parameter.
     */
    @Test
    public final void testisZeroWithParam_1_Integer() {
        NaturalNumber nAct = this.constructorTest(0);
        NaturalNumber nExp = this.constructorRef(0);

        assertTrue(nAct.isZero() == nExp.isZero());
    }

    /**
     * Test isZero when true given a non-zero number.
     */
    @Test
    public final void testisZeroWithParam_2_Integer() {
        final int natNum = 5;
        NaturalNumber nAct = this.constructorTest(natNum);
        NaturalNumber nExp = this.constructorRef(natNum);

        assertTrue(nAct.isZero() == nExp.isZero());

    }

    /*
     * Tests of kernel methods using NaturalNumbers.
     */

    /**
     * Test multiplyBy10 on initial value 150 with parameter 5.
     */
    @Test
    public final void testMultiplyBy10_1_NN() {
        final int natNum = 780;
        final int k = 5;

        NaturalNumber nAct = this.constructorTest(new NaturalNumber3(natNum));
        NaturalNumber nExp = this.constructorRef(new NaturalNumber2(natNum));

        nAct.multiplyBy10(k);
        nExp.multiplyBy10(k);

        assertEquals(nExp, nAct);
    }

    /**
     * Test multiplyBy10 on the max integer.
     */
    @Test
    public final void testMultiplyBy10_2_NN() {
        final int natNum = Integer.MAX_VALUE;
        final int k = 5;

        NaturalNumber nAct = this.constructorTest(new NaturalNumber3(natNum));
        NaturalNumber nExp = this.constructorRef(new NaturalNumber3(natNum));

        nAct.multiplyBy10(k);
        nExp.multiplyBy10(k);

        assertEquals(nExp, nAct);
    }

    /*
     * Tests of divideBy10 methods.
     */

    /**
     * Test divideBy10 with 0 as a parameter.
     */
    @Test
    public final void testDivideBy10onEmpty_NN() {
        NaturalNumber input = this.constructorTest(new NaturalNumber3());
        NaturalNumber expected = this.constructorRef(new NaturalNumber2());
        int remainder = input.divideBy10();
        final int expRemainder = 0;
        assertTrue(remainder == expRemainder);
        assertEquals(input, expected);
    }

    /**
     * Test divideBy10 on 154978.
     */
    @Test
    public final void testDivideBy10NonEmpty_1_NN() {
        final int natNum = 154978;
        NaturalNumber input = this.constructorTest(new NaturalNumber3(natNum));
        NaturalNumber expected = this
                .constructorRef(new NaturalNumber2(natNum / 10));
        int remainder = input.divideBy10();
        final int expRemainder = 8;
        assertTrue(remainder == expRemainder);
        assertEquals(expected, input);
    }

    /**
     * Test divideBy10 on a very large number.
     */
    @Test
    public final void testDivideBy10NonEmpty_2_NN() {
        NaturalNumber input = this
                .constructorTest(new NaturalNumber3(Integer.MAX_VALUE));
        NaturalNumber expected = this.constructorRef(
                new NaturalNumber2(Integer.MAX_VALUE / NaturalNumber.RADIX));
        int remainder = input.divideBy10();
        final int expRemainder = 7;
        assertTrue(remainder == expRemainder);
        assertEquals(expected, input);
    }

    /*
     * Tests for isZero method
     */

    /**
     * Test isZero when true given 0 as a parameter.
     */
    @Test
    public final void testisZeroWithParam_1_NN() {
        NaturalNumber nAct = this.constructorTest(new NaturalNumber3());
        NaturalNumber nExp = this.constructorRef(new NaturalNumber2());

        assertTrue(nAct.isZero() == nExp.isZero());
    }

    /**
     * Test isZero when true given a non-zero number.
     */
    @Test
    public final void testisZeroWithParam_2_NN() {
        final int natNum = 5;
        NaturalNumber nAct = this.constructorTest(new NaturalNumber3(natNum));
        NaturalNumber nExp = this.constructorRef(new NaturalNumber2(natNum));

        assertTrue(nAct.isZero() == nExp.isZero());

    }

}
