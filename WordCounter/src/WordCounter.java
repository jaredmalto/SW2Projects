import java.util.Comparator;

import components.map.Map;
import components.map.Map2;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Counts words in a user supplied input texts and outputs an html file with the
 * number of times each word appears.
 *
 * @author Jared Malto
 */
public final class WordCounter {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private WordCounter() {
        // no code needed here
    }

    /**
     * Compare {@code String}s in alphabetical order.
     */
    private static class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            /*
             * want in alphabetical order not lexicographical->ignore case
             */
            return o1.compareToIgnoreCase(o2);
        }
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param charSet
     *            the {@code Set} to be replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    public static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";

        charSet.clear();

        //iterate through the string
        for (int i = 0; i < str.length(); i++) {
            //take each character of the string to check if it can go into the set
            char element = str.charAt(i);
            //if the set does not already contain the current element, add it to the set
            if (!charSet.contains(element)) {
                charSet.add(element);
            }
        }
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        //create a substring and a full word or sep string to build upon
        String sub = text.substring(position);
        String wordOrSep = sub.substring(0, 1);

        int i = 0;

        //if the current position is a separator
        if (separators.contains(sub.charAt(0))) {
            //iterate until there are no separators
            while (i < sub.length() && separators.contains(sub.charAt(i))) {
                //increment index, substrings are up to and dont include i
                i++;
                wordOrSep = sub.substring(0, i);
            }
        } else {
            /*
             * if the current position is not a separator, keep building the
             * substring until we hit a separator
             */
            while (i < sub.length() && !separators.contains(sub.charAt(i))) {

                i++;
                wordOrSep = sub.substring(0, i);
            }
        }
        return wordOrSep;
    }

    /**
     * Generates a {@code map} of a word in an input text to the number of times
     * it appears throughout.
     *
     * @param map
     *            mapped input of words to number of occurences
     * @param in
     *            input file
     * @replaces map
     * @ensures map = word-># of appearances
     */
    public static void mapInput(Map<String, Integer> map, SimpleReader in) {

        //make sure the map is clear before performing operations
        map.clear();

        //exclude punctuation and spaces--create set with them
        Set<Character> punctuationAndSeparatorSet = new Set1L<>();
        String punctuationAndSeparators = " \t;,.-'\"\n\r";
        generateElements(punctuationAndSeparators, punctuationAndSeparatorSet);

        //keep processing text while not at the end of the input stream
        while (!in.atEOS()) {
            //read line by line
            String currLine = in.nextLine();

            //starting index
            int i = 0;

            while (i < currLine.length()) {
                String word = nextWordOrSeparator(currLine, i,
                        punctuationAndSeparatorSet);
                /*
                 * the returned value is only a word if the character at 0 is
                 * not in the excluded set
                 */
                boolean isWord = !punctuationAndSeparatorSet
                        .contains(word.charAt(0));
                //only perform if the returned value is a word
                if (isWord) {
                    /*
                     * 1st case--the current word has not yet been read and
                     * added to the map
                     */
                    if (!map.hasKey(word)) {
                        //add the key word to the value 1 since it has only appeared once
                        map.add(word, 1);
                    } else {
                        /*
                         * 2nd case--the current word has already been read
                         * return the value and replace it with incremented
                         * value
                         */
                        int wordCount = map.value(word) + 1;
                        map.replaceValue(word, wordCount);
                    }
                }
                //increment index by the word length
                i += word.length();
            }
        }
        //we're done with the input
        in.close();
    }

    /**
     * Arranges {@code map}'s keys and stores data in {@code q}.
     *
     * @param q
     *            queue of alphabetized strings
     * @param map
     *            mapped input of words to number of appearances
     * @param order
     *            order by which to compare entries
     * @updates q
     * @ensures q=[alphabetized list of words]
     */
    public static void alphabetize(Queue<String> q, Map<String, Integer> map,
            Comparator<String> order) {
        //create temp map
        Map<String, Integer> temp = map.newInstance();
        temp.transferFrom(map);

        while (temp.size() > 0) {
            //remove arbitrary element from the map
            Map.Pair<String, Integer> word = temp.removeAny();
            //add the key of this element to the key
            q.enqueue(word.key());
            //restore the map to use for later
            map.add(word.key(), word.value());
        }
        //sort the queue with the comparator
        q.sort(order);
    }

    /**
     * Generates opening HTML tags.
     *
     * @param out
     *            the output stream
     * @param fileName
     *            name of user input file
     * @ensures out.is_open
     */
    public static void generateHeader(SimpleWriter out, String fileName) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Words Counted in " + fileName + "</title>");
        out.println("</head>");
    }

    /**
     * Generates body of html output.
     *
     * @param out
     *            the output stream
     * @param fileName
     *            name of the file to display
     * @param map
     *            mapped input
     * @param q
     *            alphabetized list of words
     * @updates q
     * @updates map
     * @ensures out.is_closed
     */
    public static void generateBodyAndFooter(SimpleWriter out, String fileName,
            Map<String, Integer> map, Queue<String> q) {
        out.println("<body>");
        out.println("<h2>Words Counted in " + fileName + "</h2>");
        out.println("<hr />");
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>Words</th>");
        out.println("<th>Counts</th>");
        out.println("</tr>");

        //begin dequeueing words to add to new table rows
        while (q.length() > 0) {
            String word = q.dequeue();
            //remove the corresponding pair from the map
            Map.Pair<String, Integer> elem = map.remove(word);

            out.println("<tr>");
            out.println("<td>" + elem.key() + "</td>");
            out.println("<td>" + elem.value() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</body>");
        out.println("</html>");

        //we're done with the output file
        out.close();
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();

        //prompt for input and output files
        out.print("Enter an input file: ");
        String input = in.nextLine();
        SimpleReader inFile = new SimpleReader1L(input);

        out.print("Enter an output file: ");
        SimpleWriter outFile = new SimpleWriter1L(in.nextLine());

        //1. Map the input string->integers
        Map<String, Integer> mappedInput = new Map2<>();
        //call map method
        mapInput(mappedInput, inFile);

        //2. Sort the map's keys
        Queue<String> alphabetizedTerms = new Queue1L<>();
        //create comparator object to sort
        Comparator<String> cs = new StringLT();
        //call alphabetize method
        alphabetize(alphabetizedTerms, mappedInput, cs);

        //generate html elements
        generateHeader(outFile, input);
        generateBodyAndFooter(outFile, input, mappedInput, alphabetizedTerms);

        out.close();
        in.close();

    }
}
