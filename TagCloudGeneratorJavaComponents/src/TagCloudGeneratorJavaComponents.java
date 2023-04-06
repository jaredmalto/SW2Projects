import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Takes user input file and generates a user given number of the words that
 * appear the most in alphabetical order.
 *
 * @author Jared Malto and Kelvin Nguyen
 */
public final class TagCloudGeneratorJavaComponents {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private TagCloudGeneratorJavaComponents() {
        // no code needed here
    }

    //declare string constant of separators
    private static final String SEPARATORS = " \t\n\r,-.!?[]';:/()";

    //min and max font sizes
    private static final int FONT_MAX_SIZE = 48;
    private static final int FONT_MIN_SIZE = 11;

    /**
     * Compare {@code String}s in alphabetical order.
     */
    private static class CompareKeys
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            /*
             * want in alphabetical order not lexicographical->ignore case
             */

            String s1 = o1.getKey();
            String s2 = o2.getKey();

            return s1.compareToIgnoreCase(s2);
        }
    }

    /**
     * Compare {@code String}s in alphabetical order.
     */
    private static class CompareValues
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {

            int val1 = o1.getValue();
            int val2 = o2.getValue();

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
    public static void mapInput(Map<String, Integer> map, BufferedReader in)
            throws IOException {
        map.clear();

        //create seoarator set
        Set<Character> punctuationAndSeparatorSet = new HashSet<>();
        generateElements(SEPARATORS, punctuationAndSeparatorSet);

        String line = in.readLine();

        while (line != null) {
            //starting index
            int i = 0;

            line = line.toLowerCase();
            //iterate through the line
            while (i < line.length()) {
                String word = nextWordOrSeparator(line, i,
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
                    if (!map.containsKey(word)) {
                        //add the key word to the value 1 since it has only appeared once
                        map.put(word, 1);
                    } else {
                        /*
                         * 2nd case--the current word has already been read
                         * return the value and replace it with incremented
                         * value
                         */
                        int wordCount = map.get(word) + 1;
                        map.replace(word, wordCount);
                    }
                }
                //increment index by the word length
                i += word.length();
            }
            line = in.readLine();

        }
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
     * @return ArrayList of pair values sorted in descending order with
     */
    public static ArrayList<Map.Entry<String, Integer>> sortByNumberOfAppearances(
            Map<String, Integer> map, int numWords) {

        //im going to use arraylist for this because it implements a sort method
        ArrayList<Map.Entry<String, Integer>> tmpList = new ArrayList<>();

        //create an entry set to iterate over
        Set<Map.Entry<String, Integer>> elements = map.entrySet();

        //create a new iterator
        Iterator<Map.Entry<String, Integer>> it = elements.iterator();

        //iterate over everything in the set and add it to the arraylist
        while (it.hasNext()) {
            Map.Entry<String, Integer> elem = it.next();
            tmpList.add(elem);
        }

        Comparator<Map.Entry<String, Integer>> ci = new CompareValues();

        //sort according to comparator above
        tmpList.sort(ci);
        //calculate number of words to leave
        int numElementsToLeave = tmpList.size() - numWords;
        ArrayList<Map.Entry<String, Integer>> listWithFirstNumWords = new ArrayList<>();

        //remove the first n words
        while (tmpList.size() > numElementsToLeave) {
            Map.Entry<String, Integer> elem = tmpList.remove(0);
            listWithFirstNumWords.add(elem);
        }

        return listWithFirstNumWords;
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
    public static void generateHeader(PrintWriter out, int numWords,
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
    public static void generateBodyAndFooter(PrintWriter out, int numWords,
            String input, ArrayList<Map.Entry<String, Integer>> q, int min,
            int max) {
        //body tags
        out.println("<body>");
        out.println("<h2>Top " + numWords + " words in " + input + "</h2>");
        out.println("<hr>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");

        //remove everything from the queue
        while (q.size() > 0) {
            Map.Entry<String, Integer> elem = q.remove(0);
            //store the occurences
            int occurences = elem.getValue();
            //calculate the font size
            int fontSize = fontSize(occurences, min, max);
            //output each element
            out.println("<span style=\"cursor:default\" class=\"f" + fontSize
                    + "\" title=\"count: " + occurences + "\">" + elem.getKey()
                    + "</span>");
        }

        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
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
        //prompt user for input
        System.out.print("Enter an input file: ");

        //take keyboard input stream
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in));
        String inFileString = null;

        //attempt to open file
        try {
            inFileString = input.readLine();
        } catch (IOException e) {
            System.err.println("Error reading input.");
            return;
        }

        //reading the file
        BufferedReader inFile = null;
        try {
            inFile = new BufferedReader(new FileReader(inFileString));
        } catch (IOException e) {
            System.err.println("Error opening file.");
            return;
        }

        //get user number of words
        /*
         * initialize numberOfWords so that it can be in scope for the rest of
         * the program
         */
        int numberOfWords = 0;
        System.out.print("Enter the number of words to include: ");
        try {
            numberOfWords = Integer.parseInt(input.readLine());
        } catch (IOException e) {
            System.err.println("Error reading input.");
        }

        //throw error if user enters anything less than 0
        assert numberOfWords >= 0 : "Number of words to include must be positive";

        System.out.print("Enter an output file: ");
        String outFileString = null;
        PrintWriter outFile = null;
        try {
            outFileString = input.readLine();
            outFile = new PrintWriter(
                    new BufferedWriter(new FileWriter(outFileString)));
        } catch (IOException e) {
            System.err.println("Error creating output file.");
        }

        Map<String, Integer> mappedInput = new HashMap<>();

        //map input
        //word -> # of occurences
        try {
            mapInput(mappedInput, inFile);
        } catch (IOException e) {
            System.err.println("Error processing input.");
        }

        //sort by the number of appearances to find the min and max
        ArrayList<Map.Entry<String, Integer>> sortedElements = sortByNumberOfAppearances(
                mappedInput, numberOfWords);

        int min = sortedElements.get(sortedElements.size() - 1).getValue();
        int max = sortedElements.get(0).getValue();

        //sort alphabetically to display
        Comparator<Map.Entry<String, Integer>> cs = new CompareKeys();
        sortedElements.sort(cs);

        generateHeader(outFile, numberOfWords, inFileString);
        generateBodyAndFooter(outFile, numberOfWords, inFileString,
                sortedElements, min, max);
        //close input
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("Error closing file.");
        }

        outFile.close();
    }
}
