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

/**
 * Takes input of terms and outputs a well formatted glossary.
 *
 * @author Jared Malto
 *
 */
public final class Glossary {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Glossary() {
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
     * Reads input file and maps terms to their definitions.
     *
     * @param in
     *            user inputted file
     * @return Mapped terms and definitions
     * @ensures term->definition
     */
    public static Map<String, String> mapInput(SimpleReader in) {

        Map<String, String> map = new Map1L<>();

        //go through the full file
        while (!in.atEOS()) {
            /*
             * formating goes -- first line is the term, then the next like is a
             * definition until a blank line separates the next term
             *
             */
            //the first line is the term
            String term = in.nextLine();
            //next is the definition
            String definition = in.nextLine();
            //definition can be longer than 1 mine
            String defNextLine = in.nextLine();

            /*
             * create stringbuilder in case definitions are longer than 2 lines
             * apparently concatenating definitions with long strings is really
             * expensive put everything in the string builder just in case
             */
            StringBuilder defBuilder = new StringBuilder(definition);

            //to tell if the def is longer than 1 line, check if line 2 is empty
            while (!defNextLine.isEmpty()) {
                //concatenate the 2nd line with the first
                defBuilder.append(" " + defNextLine);
                //check to see if the next line is empty. if not, enter the loop again
                defNextLine = in.nextLine();
            }

            //add the term and definition to the map
            //the key is the term, the definition is the value
            map.add(term, defBuilder.toString());

        }

        return map;
    }

    /**
     * Alphabetizes keys in {@code Map}.
     *
     * @param map
     *            Mapped terms and definitions
     * @param order
     *            ordering by which to compare entries
     * @return queue with alphabetized terms
     * @ensures[queue returned is in alphabetical order]
     */
    public static Queue<String> alphabetizeTerms(Map<String, String> map,
            Comparator<String> order) {

        Queue<String> q = new Queue1L<>();
        //temporary map to store values
        Map<String, String> temp = map.newInstance();
        temp.transferFrom(map);

        while (temp.size() > 0) {
            //remove arbitrary element from the map
            Map.Pair<String, String> element = temp.removeAny();
            //enqueue whatever key it is
            q.enqueue(element.key());
            //restore the map--we will need it later
            map.add(element.key(), element.value());
        }
        //alphabetize the queue
        q.sort(order);
        return q;
    }

    /**
     * Generates the index page for the glossary.
     *
     * @param q
     *            Queue of terms in alphabetical order.
     * @param out
     *            output stream
     * @ensures out.content=[well formatted HTML page with terms and hyperlinks
     *          to definitions]
     */
    public static void generateIndex(Queue<String> q, SimpleWriter out) {

        /*
         * Index header
         */
        out.print("<html>\n<head>\n<title>Jared's Glossary</title>\n</head>\n");

        /*
         * Output the body up to the <ul> tag
         */
        out.print("<body>\n<h2>Jared's Glossary</h2>\n<hr />\n"
                + "<h3>Index</h3>\n<ul>\n");

        //create temp queue
        Queue<String> temp = q.newInstance();
        temp.transferFrom(q);

        /*
         * Output the words alphabetically
         */
        while (temp.length() > 0) {
            //dequeue word and print out its line and restore q
            String term = temp.dequeue();
            out.print(
                    "<li><a href=\"" + term + ".html\">" + term + "</a></li>");
            out.println();
            q.enqueue(term);
        }

        /*
         * Output the footer
         */
        out.print("</ul>\n</body>\n</html>");

    }

    /**
     * Generates the header of the term html page.
     *
     * @param term
     * @param out
     * @ensures out.content = #out.content*[HTML header elements] and
     *          out.is_open
     */
    public static void outputHeader(SimpleWriter out, String term) {
        //print the html elements

        /*
         * head
         */
        out.print("<html>\n<head>\n<title>" + term + "</title>\n</head>\n");

        /*
         * begin body
         */
        out.print("<body>\n<h2><b><i><font color=\"green\">" + term
                + "</font></i></b></h2>");
    }

    /**
     * Outputs the definition of one word and searches for words contained in
     * the definition that are also defined in the glossary.
     *
     * @param out
     *            the output stream
     * @param map
     *            map with keys as terms and values as definitions
     * @param term
     *            the current word to output
     * @param def
     * @ensures <blockquote>[element is outputted correctly and any word that is
     *          contained in the definition that is already defined has a
     *          hyperlink included.]</blockquote>
     */
    public static void outputDefinition(SimpleWriter out,
            Map<String, String> map, String term, String def) {

        /*
         * print the <blockquote> portion of the page
         */

        //create separator set to use in conjunction with nextwordorseparator
        Set<Character> separatorSet = new Set1L<>();
        //have the separator strings, spaces, tabs, newlines
        final String separators = " \t, , \n, \r";
        //make a separator set
        generateElements(separators, separatorSet);

        /*
         * Begin the <blockquote> tag
         */
        out.print("<blockquote>");

        //build the definitions using nextwordorseparator until the end of the definition

        int i = 0;
        while (i < def.length()) {
            //NWOS returns a string of either the next word or whitespace
            String currentWordOrSep = nextWordOrSeparator(def, i, separatorSet);

            /*
             * if the map contains the current word being printed out and it is
             * not whitespace, print and add a link to that term's html page.
             */
            if (map.hasKey(currentWordOrSep)
                    && !separatorSet.contains(currentWordOrSep.charAt(0))) {
                out.print("<a href=\"" + currentWordOrSep + ".html\">"
                        + currentWordOrSep + "</a>");
            } else {
                //else, just print out the word or sep
                out.print(currentWordOrSep);
            }
            //increment index by the length of the element just printed
            i += currentWordOrSep.length();
        }
        out.print("</blockquote>");
    }

    /**
     * Outputs the footer for the term html page.
     *
     * @param out
     *            the output stream
     */
    public static void outputFooter(SimpleWriter out) {
        out.println(
                "<hr />\n<p>Return to <a href=\"index.html\">index</a>.</p>");
        out.println("</body>\n</html>");
    }

    /**
     * Creates a full term .html file.
     *
     * @param q
     *            alphabetized list of terms
     * @param map
     *            paired definitions of terms and definitions
     * @param folder
     *            the name of the user inputted folder
     * @updates q
     * @ensures each term in {@code} q has its own HTML file
     */
    public static void makeDefinitionPages(Queue<String> q,
            Map<String, String> map, String folder) {
        /*
         * Create term files for each word in the input. Loop through the queue
         * of alphabetized terms to output it in the correct order.
         */
        while (q.length() > 0) {
            //get the word in the front of the queue
            String term = q.dequeue();
            /*
             * remove the value of the respective term from the map, which is
             * the definition
             */
            String definition = map.value(term);

            //create a simplewriter to output everything to
            /*
             * the term destination looks like outputfolder/word.html--create
             * simple writer in this format
             *
             */
            SimpleWriter termFile = new SimpleWriter1L(
                    folder + "/" + term + ".html");
            /*
             * output html header and h2
             */
            outputHeader(termFile, term);

            /*
             * output the definition and search for words already contained
             */
            outputDefinition(termFile, map, term, definition);
            /*
             * output footer.
             */
            outputFooter(termFile);

        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.print("Enter the name of an input file: ");
        SimpleReader inFile = new SimpleReader1L(in.nextLine());

        out.print("Enter the name of an output folder: ");
        String outFolder = in.nextLine();
        SimpleWriter destination = new SimpleWriter1L(
                outFolder + "/index.html");

        /*
         * My first thought is I should create a map mapping the input terms to
         * their definitions. Similar to the pizza lab.
         */
        Map<String, String> termsAndDefs = mapInput(inFile);

        /*
         * Next, I want to alphabetize the terms. I want to use a queue because
         * the order matters and I'm thinking about doing a loop to dequeue the
         * terms and output them
         */
        Comparator<String> cs = new StringLT();
        Queue<String> alphabetizedTerms = alphabetizeTerms(termsAndDefs, cs);

        /*
         * Now that I have done everything I should do with the words and
         * definitions, I will begin generating the index page.
         */
        generateIndex(alphabetizedTerms, destination);

        //creates and outputs each html page for each def
        makeDefinitionPages(alphabetizedTerms, termsAndDefs, outFolder);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
