import java.util.Comparator;

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
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;
import components.utilities.Reporter;

/**
 * Takes user input file and generates a user given number of the words that
 * appear the most in alphabetical order.
 *
 * @author Jared Malto and Kelvin Nguyen
 */
public final class TagCloudGenerator {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private TagCloudGenerator() {
        // no code needed here
    }

    //declare string constant of separators
    private static final String SEPARATORS = " \t\n\r,-.!?[]';:/()";

    //min and max font sizes
    private static final int FONT_MAX_SIZE = 48;
    private static final int FONT_MIN_SIZE = 11;

    /*
     * Nested comparator classes implementing map<string, integer>
     */

    /**
     * Compare {@code String}s in alphabetical order.
     */
    private static class CompareKeys
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            /*
             * want in alphabetical order not lexicographical->ignore case
             */

            String s1 = o1.key();
            String s2 = o2.key();

            return s1.compareToIgnoreCase(s2);
        }
    }

    /**
     * Compare {@code String}s in alphabetical order.
     */
    private static class CompareValues
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {

            int val1 = o1.value();
            int val2 = o2.value();

            if (val1 < val2) {
                return 1;
            } else if (val1 > val2) {
                return -1;
            } else {
                return 0;
            }
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
        generateElements(SEPARATORS, punctuationAndSeparatorSet);

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
                    //change everything to lowercase to make things easier
                    /*
                     * For example, normally Abc /= abc; changing everything to
                     * lowercase ensures count will exclude these cases.
                     */
                    word = word.toLowerCase();
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
     * Sorts {@code m}'s values according to number of appearances
     *
     * @param m
     *            mapped input of words to the number of times it appears
     * @param order
     *            comparator
     * @param numWords
     *            number of user's desired words
     * @return Queue of pair values sorted in descending order with
     */
    public static Queue<Map.Pair<String, Integer>> sortByNumberOfApperances(
            Map<String, Integer> m, Comparator<Map.Pair<String, Integer>> order,
            int numWords) {

        //create new queue to return
        Queue<Map.Pair<String, Integer>> q = new Queue1L<>();

        //create sorting machine to sort map inputs to queue
        SortingMachine<Map.Pair<String, Integer>> sm = new SortingMachine1L<>(
                order);

        //create temp map
        Map<String, Integer> temp = m.newInstance();
        temp.transferFrom(m);

        //iterate until temp is empty
        while (temp.size() > 0) {
            //remove arbitrary value from elem
            Map.Pair<String, Integer> elem = temp.removeAny();
            //add each element to the sortingmachine
            sm.add(elem);
            //restore m
            m.add(elem.key(), elem.value());
        }

        //now that we have all the elements we want in sm, we can change to extraction
        sm.changeToExtractionMode();
        /*
         * calculate the number of elements we want to leave in the sorting
         * machine using the user input number of words
         */
        int numElementsToLeave = sm.size() - numWords;
        //iterate while the size is greater than how many elements we want in the sorting machine
        //this ensures that we only remove the first numWords elements
        while (sm.size() > numElementsToLeave) {
            //remove first element and add it to the queue
            Map.Pair<String, Integer> elem = sm.removeFirst();
            q.enqueue(elem);
        }

        return q;
    }

    /**
     * Uses SortingMachine component to organize {@code q} entries
     * alphabetically.
     *
     * @param q
     *            Entries sorted by decreasing appearance
     * @param order
     *            Order in which to compare entries
     * @updates q
     * @ensures keys in {@code q} are sorted in alphabetical order regardless of
     *          case
     *
     */
    public static void sortAlphabetically(Queue<Map.Pair<String, Integer>> q,
            Comparator<Map.Pair<String, Integer>> order) {

        //specs require sorting machine
        SortingMachine<Map.Pair<String, Integer>> sm = new SortingMachine1L<>(
                order);
        //temp map
        Queue<Map.Pair<String, Integer>> tmp = q.newInstance();
        tmp.transferFrom(q);

        //remove everything and add to the sorting machine
        while (tmp.length() > 0) {
            Map.Pair<String, Integer> elem = tmp.dequeue();
            sm.add(elem);
        }

        //now that all elements are in sorting machine, we can change it to extraction mode
        sm.changeToExtractionMode();

        //remove everything from the sorting machine and add it to the queue
        //should be sorted after
        while (sm.size() > 0) {
            Map.Pair<String, Integer> elem = sm.removeFirst();
            q.enqueue(elem);
        }
    }

    /**
     * Generates HTML header for the output.
     *
     * @param out
     *            the output stream
     * @param numWords
     *            number of words to include
     * @param input
     *            name of the input file
     * @ensures header is well formatted HTML
     */
    public static void generateHeader(SimpleWriter out, int numWords,
            String input) {
        out.println("<html>");
        out.println("<head>");
        out.println(
                "<title>Top " + numWords + " words in " + input + "</title>");
        out.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-sw2/assignments/projects/"
                        + "tag-cloud-generator/data/tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println(
                "<link href=\"data/tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");
    }

    /**
     * Outputs the body of the HTML file.
     *
     * @param out
     *            the output stream
     * @param numWords
     *            number of words to include in the tag cloud
     * @param input
     *            the input file and name
     * @param q
     *            queue of sorted elements
     * @param min
     *            smallest number of appearances within included words
     * @param max
     *            highest number of appearances within included words
     * @requires out.is_open
     * @ensures output body is well formatted HTML and displayed in order
     *          specified by comparator
     */
    public static void generateBodyAndFooter(SimpleWriter out, int numWords,
            String input, Queue<Map.Pair<String, Integer>> q, int min,
            int max) {
        //body tags
        out.println("<body>");
        out.println("<h2>Top " + numWords + " words in " + input + "</h2>");
        out.println("<hr>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");

        //remove everything from the queue
        while (q.length() > 0) {
            Map.Pair<String, Integer> elem = q.dequeue();
            //store the occurences
            int occurences = elem.value();
            //calculate the font size
            int fontSize = fontSize(occurences, min, max);
            //output each element
            out.println("<span style=\"cursor:default\" class=\"f" + fontSize
                    + "\" title=\"count: " + occurences + "\">" + elem.key()
                    + "</span>");
        }

        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Finds and returns the highest value in {@code q}
     *
     * @param q
     *            Queue of map pairs sorted by number of appearances in
     *            descending order
     * @requires q's values are sorted in descending order
     * @return the highest word count in the map
     */
    public static int getMax(Queue<Map.Pair<String, Integer>> q) {
        //create temp map
        Queue<Map.Pair<String, Integer>> tmp = q.newInstance();
        tmp.transferFrom(q);

        /*
         * since we require the queue to be sorted in descending order, we know
         * the first element will be the maximum
         */
        Map.Pair<String, Integer> max = tmp.dequeue();
        //put it back in the queue since it will be needed later
        q.enqueue(max);

        //restore q
        while (tmp.length() > 0) {
            Map.Pair<String, Integer> elem = tmp.dequeue();
            q.enqueue(elem);
        }
        return max.value();
    }

    /**
     * Finds and returns the highest value in {@code q}
     *
     * @param q
     *            Queue of map pairs sorted by number of appearances in
     *            descending order
     * @requires q's values are sorted in descending order
     * @return the highest word count in the map
     */
    public static int getMin(Queue<Map.Pair<String, Integer>> q) {
        //create temp map
        Queue<Map.Pair<String, Integer>> tmp = q.newInstance();
        tmp.transferFrom(q);

        /*
         * Since we require the queue to be in descending order, we know the
         * last element will be the min, so keep removing elements until we have
         * 1 left
         */
        while (tmp.length() > 1) {
            Map.Pair<String, Integer> elem = tmp.dequeue();
            //restore q
            q.enqueue(elem);
        }

        //store the last value as the min and restore q
        Map.Pair<String, Integer> min = tmp.dequeue();
        q.enqueue(min);

        return min.value();

    }

    /**
     * Calculates a font size for a word proportional to how many times it
     * occurs.
     *
     * @param occurences
     *            number of times a word appears in the input text file
     * @param minOccurence
     *            the smallest number of times a word appears within the top
     *            words
     * @param maxOccurence
     *            the highest number of times a word appears within the top
     *            words
     * @return an integer proportional to the occurence representing the CSS
     *         text size
     * @ensures fontSize(occurences, minOccurence, maxOccurence) is in interval
     *          [MIN_FONT_SIZE, MAX_FONT_SIZE] and returned value is
     *          proportional to the maximum, minimum, and number of occurences
     */
    public static int fontSize(int occurences, int minOccurence,
            int maxOccurence) {

        /*
         * For this, we want to find a function bounded from [11, 48] along
         * domain [minOccurence, maxOccurence].
         */
        int size = 0;

        /*
         * We will be using the function font(x(occurences)) = 37x(occurences) +
         * 11 where for all occurences, x(occurence) belongs to the interval [0,
         * 1]. This ensures lim x->0, the output will approach 11 (min font
         * size) and as lim x->1, the output will aproach 48 (max font size).
         * For the function x(occurences), we chose (occurences - minOccurence)
         * / (maxOccurence - minOccurence). We know this works because the
         * current occurence will always be greater than or equal to the minumum
         * occurence, meaning that we can ensure this function is at least
         * always positive. Since we know the occurence is in interval
         * [minOccurence, maxOccurence], dividing this by
         * maxOccurence-minOccurence will give us an output between [0, 1].
         * Using this output in the font function will then scale final return
         * value between our desired font interval.
         */
        int slope = FONT_MAX_SIZE - FONT_MIN_SIZE;
        int lowerBound = FONT_MIN_SIZE;

        /*
         * avoid case where min occurence = max occurence since it will result
         * in divide by 0
         */
        if (maxOccurence != minOccurence) {
            size = slope * (occurences - minOccurence);
            size /= (maxOccurence - minOccurence);
            size += lowerBound;
        }

        return size;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        //get input text file
        out.print("Enter an input file: ");
        String input = in.nextLine();
        SimpleReader inFile = new SimpleReader1L(input);

        out.print("Enter an output file: ");
        String outFileName = in.nextLine();
        //create simplewriter for output
        SimpleWriter outFile = new SimpleWriter1L(outFileName);

        //throw an error if the input and output are the same
        Reporter.assertElseFatalError(!outFileName.equals(input),
                "ERROR: Input and output files must be different.");

        //get number of words to include
        out.print("Enter the number of words to include: ");
        int numberOfWords = in.nextInteger();

        //throw an error if the user enters a negative number
        Reporter.assertElseFatalError(numberOfWords > 0,
                "ERROR: Must have positive number of words to include.");

        //map input
        //word -> # of occurences
        Map<String, Integer> mappedInput = new Map1L<>();
        mapInput(mappedInput, inFile);

        //sort the mapped input by # of word appearances
        Comparator<Map.Pair<String, Integer>> ci = new CompareValues();
        Queue<Map.Pair<String, Integer>> sortedElements = sortByNumberOfApperances(
                mappedInput, ci, numberOfWords);

        /*
         * while we have the queue sorted by # of appearances, find the max and
         * min by looking at the beginning and end of the queue
         */
        int minOccurences = getMin(sortedElements);
        int maxOccurences = getMax(sortedElements);

        //sort the mapped input in alphabetical order
        Comparator<Map.Pair<String, Integer>> cs = new CompareKeys();
        sortAlphabetically(sortedElements, cs);

        /*
         * At this point, the processing of the files are done, now we can begin
         * outputting the file.
         */

        generateHeader(outFile, numberOfWords, input);
        generateBodyAndFooter(outFile, numberOfWords, input, sortedElements,
                minOccurences, maxOccurences);

        //close all simplereaders and writers
        outFile.close();
        in.close();
        out.close();

    }
}
