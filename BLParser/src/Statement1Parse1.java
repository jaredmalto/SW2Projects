import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Jared Malto and Kelvin Nguyen
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer
                .isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF") : ""
                + "Violation of: <\"IF\"> is proper prefix of tokens";

        //we can assume the first token will be if per requires clause, no need to store
        tokens.dequeue();

        //check whether the token is a valid condition
        String conditionString = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isCondition(conditionString),
                "ERROR: Invalid Condition: " + conditionString);
        //parse the condition to add it to the statement
        Condition testCondition = parseCondition(conditionString);

        String then = tokens.dequeue();
        Reporter.assertElseFatalError(then.equals("THEN"),
                "ERROR: Token not \"THEN\". Actual: " + "\"" + tokens.front()
                        + "\"");

        Statement ifBlock = s.newInstance();
        //parse the block
        ifBlock.parseBlock(tokens);

        //we expect else or end after the block, check for that and report error if needed
        Reporter.assertElseFatalError(
                tokens.front().equals("ELSE") || tokens.front().equals("END"),
                "ERROR: Expected ELSE or END, found " + tokens.front());

        //check if the front of the queue is else to see if we should assemble if else
        if (tokens.front().equals("ELSE")) {
            //don't need to store the else token
            tokens.dequeue();
            //parse the else block
            Statement elseBlock = s.newInstance();
            elseBlock.parseBlock(tokens);
            //assemble the statement
            s.assembleIfElse(testCondition, ifBlock, elseBlock);
            //next token should be end, check syntax
            String end = tokens.dequeue();
            Reporter.assertElseFatalError(end.equals("END"),
                    "ERROR: Token not \"END\". Actual: " + "\"" + end + "\"");

        } else {
            //else, just assemble the statement and check if end is the next token
            s.assembleIf(testCondition, ifBlock);

            String end = tokens.dequeue();
            Reporter.assertElseFatalError(end.equals("END"),
                    "Error: Token not \"END\". Actual: " + "\"" + end + "\"");

        }

        //check if the last token in this statement is if
        String lastIf = tokens.dequeue();
        Reporter.assertElseFatalError(lastIf.equals("IF"),
                "ERROR: Token not \"IF\". Actual: " + "\"" + lastIf + "\"");

    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"WHILE"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE") : ""
                + "Violation of: <\"WHILE\"> is proper prefix of tokens";

        tokens.dequeue();

        String conditionString = tokens.dequeue();
        //check if this current string is valid and report error if not
        Reporter.assertElseFatalError(Tokenizer.isCondition(conditionString),
                "ERROR: invalid condition.");
        //once checked, parse the condition
        Condition testCondition = parseCondition(conditionString);

        //next, we expect DO
        String expDo = tokens.dequeue();
        Reporter.assertElseFatalError(expDo.equals("DO"),
                "ERROR: Expected token: \"DO\". Actual token: " + expDo);

        //parse the following block
        Statement whileBlock = s.newInstance();
        whileBlock.parseBlock(tokens);

        //assemble the while statement
        s.assembleWhile(testCondition, whileBlock);

        String endWhile = tokens.dequeue();
        Reporter.assertElseFatalError(endWhile.equals("END"),
                "ERROR: \"END\" expected");

        String endToken = tokens.dequeue();
        Reporter.assertElseFatalError(endToken.equals("WHILE"),
                "ERROR: \"WHILE\" expected");

    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0
                && Tokenizer.isIdentifier(tokens.front()) : ""
                        + "Violation of: identifier string is proper prefix of tokens";

        //calls are just single elements
        String call = tokens.dequeue();
        s.assembleCall(call);

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        //replaces parameter mode
        this.clear();

        //different cases
        if (tokens.front().equals("IF")) {
            parseIf(tokens, this);
        } else if (tokens.front().equals("WHILE")) {
            parseWhile(tokens, this);
        } else {
            parseCall(tokens, this);
        }

    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        //replaces parameter mode
        this.clear();

        Statement children = this.newInstance();

        /*
         * we're basically adding to the block until we hit END, ELSE, or we
         * reach the end of input.
         */
        int i = 0;
        while (!tokens.front().equals("END") && !tokens.front().equals("ELSE")
                && !tokens.front().equals(Tokenizer.END_OF_INPUT)) {
            children.parse(tokens);
            this.addToBlock(i, children);
            i++;
        }

    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
