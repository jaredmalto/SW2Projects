import java.util.Iterator;

import components.binarytree.BinaryTree;
import components.binarytree.BinaryTree1;
import components.set.Set;
import components.set.SetSecondary;

/**
 * {@code Set} represented as a {@code BinaryTree} (maintained as a binary
 * search tree) of elements with implementations of primary methods.
 *
 * @param <T>
 *            type of {@code Set} elements
 * @mathdefinitions <pre>
 * IS_BST(
 *   tree: binary tree of T
 *  ): boolean satisfies
 *  [tree satisfies the binary search tree properties as described in the
 *   slides with the ordering reported by compareTo for T, including that
 *   it has no duplicate labels]
 * </pre>
 * @convention IS_BST($this.tree)
 * @correspondence this = labels($this.tree)
 *
 * @author Jared Malto and Kelvin Nguyen
 *
 */
public class Set3a<T extends Comparable<T>> extends SetSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Elements included in {@code this}.
     */
    private BinaryTree<T> tree;

    /**
     * Returns whether {@code x} is in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be searched for
     * @return true if t contains x, false otherwise
     * @requires IS_BST(t)
     * @ensures isInTree = (x is in labels(t))
     */
    private static <T extends Comparable<T>> boolean isInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        boolean xIsInTree = false;

        /*
         * we only want to keep searching if there is still more to disassemble
         * and if we haven't found the element yet
         *
         */
        if (t.size() > 0) {
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();

            T elem = t.disassemble(left, right);

            if (elem.equals(x)) {
                //if the current root is what we want
                xIsInTree = true;
            } else if (elem.compareTo(x) < 0) {
                //if the current root is less than what we want, look at the right side
                xIsInTree = isInTree(right, x);
            } else {
                //else, look left
                xIsInTree = isInTree(left, x);
            }
            //reassemble the tree
            t.assemble(elem, left, right);

        }

        return xIsInTree;
    }

    /**
     * Inserts {@code x} in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be inserted
     * @aliases reference {@code x}
     * @updates t
     * @requires IS_BST(t) and x is not in labels(t)
     * @ensures IS_BST(t) and labels(t) = labels(#t) union {x}
     */
    private static <T extends Comparable<T>> void insertInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();

        //base case is t is empty and we simply assemble the tree
        if (t.size() == 0) {
            t.assemble(x, left, right);
        } else {
            T elem = t.disassemble(left, right);
            //if element is bigger, put on the right subtree
            if (elem.compareTo(x) > 0) {
                insertInTree(left, x);
            } else {
                //otherwise put on left subtree
                insertInTree(right, x);
            }
            //reassemble the tree to read all removed elements
            t.assemble(elem, left, right);
        }
    }

    /**
     * Removes and returns the smallest (left-most) label in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove the label
     * @return the smallest label in the given {@code BinaryTree}
     * @updates t
     * @requires IS_BST(t) and |t| > 0
     * @ensures <pre>
     * IS_BST(t)  and  removeSmallest = [the smallest label in #t]  and
     *  labels(t) = labels(#t) \ {removeSmallest}
     * </pre>
     */
    private static <T> T removeSmallest(BinaryTree<T> t) {
        assert t != null : "Violation of: t is not null";
        assert t.size() > 0 : "Violation of: |t| > 0";

        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();

        T smallest = t.disassemble(left, right);

        if (left.height() > 0) {
            /*
             * if there are more elements within left subtrees, then keep
             * calling on the left side
             */
            smallest = removeSmallest(left);
        } else {
            /*
             * if there are no nodes smaller, then the element on the right is
             * the smallest
             */
            t.transferFrom(right);
        }

        return smallest;
    }

    /**
     * Finds label {@code x} in {@code t}, removes it from {@code t}, and
     * returns it.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove label {@code x}
     * @param x
     *            the label to be removed
     * @return the removed label
     * @updates t
     * @requires IS_BST(t) and x is in labels(t)
     * @ensures <pre>
     * IS_BST(t)  and  removeFromTree = x  and
     *  labels(t) = labels(#t) \ {x}
     * </pre>
     */
    private static <T extends Comparable<T>> T removeFromTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        assert t.size() > 0 : "Violation of: x is in labels(t)";

        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        T elem = t.root();

        //if x is equal to root
        if (x.compareTo(t.root()) == 0) {
            T root = t.disassemble(left, right);
            elem = root;
            //if the size of the left tree is more than 0 and the right isn't
            if (left.size() > 0 && right.size() == 0) {
                t.transferFrom(left);
                //if the size of the right tree is more than 0 and the left isn't
            } else if (left.size() == 0 && right.size() > 0) {
                t.transferFrom(right);
                //if both the left and right tree are more than 0
            } else if (left.size() > 0 && right.size() > 0) {
                elem = removeSmallest(right);
                t.assemble(root, left, right);
            }

            //if x is less than root
        } else if (x.compareTo(t.root()) < 0) {
            T root = t.disassemble(left, right);
            elem = removeFromTree(left, x);
            t.assemble(root, left, right);
            //if x is more than root
        } else {
            T root = t.disassemble(left, right);
            elem = removeFromTree(right, x);
            t.assemble(root, left, right);
        }

        return elem;
    }

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {

        //use BinaryTree1 for representation
        this.tree = new BinaryTree1<>();

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Set3a() {

        this.createNewRep();

    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final Set<T> newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Set<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Set3a<?> : ""
                + "Violation of: source is of dynamic type Set3<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Set3a<?>, and
         * the ? must be T or the call would not have compiled.
         */
        Set3a<T> localSource = (Set3a<T>) source;
        this.tree = localSource.tree;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(T x) {
        assert x != null : "Violation of: x is not null";
        assert !this.contains(x) : "Violation of: x is not in this";

        insertInTree(this.tree, x);

    }

    @Override
    public final T remove(T x) {
        assert x != null : "Violation of: x is not null";
        assert this.contains(x) : "Violation of: x is in this";

        return removeFromTree(this.tree, x);
    }

    @Override
    public final T removeAny() {
        assert this.size() > 0 : "Violation of: this /= empty_set";

        return removeSmallest(this.tree);
    }

    @Override
    public final boolean contains(T x) {
        assert x != null : "Violation of: x is not null";

        return isInTree(this.tree, x);
    }

    @Override
    public final int size() {
        return this.tree.size();
    }

    @Override
    public final Iterator<T> iterator() {
        return this.tree.iterator();
    }

}
