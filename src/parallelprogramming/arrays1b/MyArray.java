package parallelprogramming.arrays1b;

import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.*;

import static java.lang.reflect.Array.newInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A generic array class implemented via a single contiguous buffer.
 */

/**
 * Still to be debugged!
 */


public class MyArray<E>
        implements Iterable<E> {

    static class StringTestClass{

        private MyArray<String> a1;
        private List<String> l1, l2, l3;

        private void printAll(MyArray<? extends Object> a) {
            System.out.println("\n======================================");
            int element = 0;
            for (Object o : a.mElementData){
                System.out.println("["+element+"]" + o);
                element += 1;
            }
            System.out.println("\n======================================");
        }

        @BeforeEach
        @Tag("initData")
        void init() {
            // make some test data
            l1 = new ArrayList<>();
            l1.add("1"); l1.add("2"); l1.add("3"); l1.add("4");

            l2 = new ArrayList<>();
            l2.add("a"); l2.add("b"); l2.add("c");

            l3 = new ArrayList<>();
            l3.add("hello"); l3.add("world");

            // make a new mElementData array
            a1 = new MyArray<String>(4);
        }

        @AfterEach
        @Tag("clearData")
        void tearDown() {
            l1.clear();
            l2.clear();
            l3.clear();
            a1 = new MyArray<String>(4);
        }

        @Test
        @Tag("addAll") // add all to empty array and then non-empty array
        void addAllTest() {
            a1.addAll(l1);
            a1.addAll(l2);
            a1.addAll(l3);

            printAll(a1);

            assertEquals(a1.mSize, 9);
        }

        @Test
        @Tag("addTest1") // add to end of non empty MyArray
        void addTest1(){

            a1.addAll(l1);
            printAll(a1);

            a1.add("from add test 1");
            a1.add("from add test 1");
            a1.add("from add test 1");
            a1.add("from add test 1");

            printAll(a1);

            assertEquals(a1.mElementData[4], "from add test 1"); // verify added to end
            assertEquals(a1.mElementData[5], "from add test 1");
            assertEquals(a1.mElementData[6], "from add test 1");
            assertEquals(a1.mElementData[7], "from add test 1");
            assertEquals(a1.mSize, 8);
        }

        @Test
        @Tag("addTest2") // add to end of empty MyArray (i.e. the first element).
        void addTest2(){
            printAll(a1);

            a1.add("from add test 2");
            a1.add("from add test 2");
            a1.add("from add test 2");
            a1.add("from add test 2");
            a1.add("from add test 2"); // tests growing array by 1 element and adding an item
            System.out.println(a1.mSize);
;
            printAll(a1);

            assertEquals(a1.mElementData[0], "from add test 2"); // verify added to end of empty list
            assertEquals(a1.mElementData[0], "from add test 2");
            assertEquals(a1.mElementData[0], "from add test 2");
            assertEquals(a1.mElementData[0], "from add test 2");
            assertEquals(a1.mSize, 5);

        }

        @Test
        @Tag("sizeTest1") // empty
        void sizeTest1(){
            assertEquals(a1.mSize, 0);
            assertEquals(a1.size(), 0);
        }

        @Test
        @Tag("sizeTest2")
        void sizeTest2(){
            a1.add("hello");
            a1.add("1");
            a1.add("element x");
            assertEquals(a1.size(), 3);
        }

/*
        @Test
        @Tag("")
        void Test(){
            a1.
        }
*/

    }


    public static void main (String [] args) {

        StringTestClass stc = new StringTestClass();


    }

    /**
     * The array buffer that stores all the array elements.  The
     * capacity is the length of this array buffer.
     */
    private Object[] mElementData;

    /**
     * The size of the Array (the number of elements it contains).
     * This field also indicates the next "open" slot in the array,
     * i.e., where a call to add() will place the new element:
     * mElementData[mSize] = element.
     */
    private int mSize;

    /**
     * Default initial capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Shared empty array instance used for empty instances.
     */
    private static final Object[] sEMPTY_ELEMENTDATA = {};

    /*
     * The following methods and nested class use Java 7 features.
     */

    /**
     * Constructs an empty array with an initial capacity of ten.
     */
    public MyArray() {
        mElementData = sEMPTY_ELEMENTDATA;
    }

    /**
     * Constructs an empty array with the specified initial capacity.
     *
     * @param  initialCapacity  the initial capacity of the array
     * @throws IllegalArgumentException if the specified initial capacity
     *         is negative
     */
    public MyArray(int initialCapacity) {
        // TODO -- you fill in here.
        if (initialCapacity < 0) throw new IllegalArgumentException();
        else {
            mElementData = new Object[initialCapacity];
            mSize = 0; // number of elements in array and first open cell
        }

    }

    /**
     * Constructs a array containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this array
     * @throws NullPointerException if the specified collection is null
     */
    public MyArray(Collection<? extends E> c) {
        mElementData = c.toArray();
        mSize = mElementData.length; // (no free space in mElementData)
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    public boolean isEmpty() {
        boolean empty = true;
        for (Object o : mElementData)
            if (!o.equals(null)) { // o.equals(null)
                empty = false;
                break;
            }
        return empty && mSize == 0;
    }

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    public int size() {
        int size = 0;
            for (Object o : mElementData) {
                if (o != null) {
                    size += 1;
                    if(size == Integer.MAX_VALUE) {
                        size = Integer.MAX_VALUE;
                        break;
                    }
                } else {
                    break;
                }
            }
        return size;
    }

    /**
     * Returns the index of the first occurrence of the specified
     * element in this array, or -1 if this array does not contain the
     * element.
     *
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in
     *         this array, or -1 if this array does not contain the element
     */
    public int indexOf(Object o) {
        // TODO -- you fill in here.
        boolean found = false;
        int index = -1, counter = 0;
        do {
            for (Object x : mElementData) // iterate over array while not found
                if (x == o) {
                    index = counter; // store index value of first occurrence
                    found = true; // break out the do-while
                } else {
                    counter++;
                }
        } while (!found);
        return index;
    }


    /**
     * Appends all of the elements in the specified collection to the
     * end of this array, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this
     * operation is undefined if the specified collection is modified
     * while the operation is in progress.  This implies that the
     * behavior of this call is undefined if the specified collection
     * is this array, and this array is nonempty.
     *
     * @param c collection containing elements to be added to this array
     * @return <tt>true</tt> if this array changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        if(c == null) throw new NullPointerException();
        else {
            try {
                int aLen = mElementData.length;
                int bLen = c.size();

                // find the first null, and copy the new items to be added from that index
                int fn_index = 0;
                while(mElementData[fn_index] != null) {
                    fn_index++;
                }

                // if mElementData has data inside it, copy everything from 0..mSize
                if (mSize == aLen){

                    //ensureCapacityInternal(c.size()); no need for this with arraycopy

                    // Create a new array of same type to hold all the elements
                    Object[] new_mElementData = new Object[mSize + c.size()];

                    // Copy the contents of mElementData and collection c into a new array
                    System.arraycopy(mElementData, 0, new_mElementData, 0, fn_index);
                    System.arraycopy(c.toArray(), 0, new_mElementData, fn_index, bLen);

                    // Reinitialize mElementData, mSize
                    mElementData = new_mElementData;
                    mSize = mElementData.length;

                    changed = true;
                }
                else if(mSize == 0 || mSize < mElementData.length) {

                    //ensureCapacityInternal(c.size()); no need for this with arraycopy

                    Object[] new_mElementData = new Object[(mSize + c.size()) * 2]; // array size doubles
                    System.arraycopy(mElementData, 0, new_mElementData, 0, fn_index);
                    System.arraycopy(c.toArray(), 0, new_mElementData, fn_index, bLen);

                    mElementData = new_mElementData;
                    int i = 0;
                    while(mElementData[i] != null) i++;
                    mSize = i;

                    changed = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(changed == true) System.out.println("\n======================================\n" +
                        "... Collection appended successfully");
                else if (changed ==  false) System.out.println("\n======================================\n" +
                        "... Collection append unsuccessful");
            }
        }
        return changed;
    }

    /**
     * Appends all of the elements in the specified Array to the end
     * of this array, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this
     * operation is undefined if the specified collection is modified
     * while the operation is in progress.  This implies that the
     * behavior of this call is undefined if the specified collection
     * is this array, and this array is nonempty.
     *
     * @param a collection containing elements to be added to this array
     * @return <tt>true</tt> if this array changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(MyArray<E> a) {
        // TODO -- you fill in here.
        boolean changed = false;
        if(a == null) throw new NullPointerException();
        else {
            try{
                int aLen = mElementData.length;
                int bLen = a.size();
                E[] concatArray = (E[]) newInstance(this.getClass().getComponentType(), aLen + bLen);
                System.arraycopy(mElementData, 0, concatArray, 0, aLen);
                System.arraycopy(a, 0, concatArray, aLen, bLen);
                mElementData = concatArray;
                mSize = mElementData.length;
                changed = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(changed == true) System.out.println("... Collection appended successfully");
                else if (changed ==  false) System.out.println("... Collection append unsuccessful");
            }
        }
        return changed;
    }



    /**
     * Removes the element at the specified position in this array.
     * Shifts any subsequent elements to the left (subtracts one from
     * their indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the array
     * @throws IndexOutOfBoundsException
     */
    public E remove(int index) {
        // TODO -- you fill in here.
        E removedElement = (E) mElementData[index];
        System.out.println("... Removing " + removedElement.toString());
        E[] removedArr = (E[]) newInstance(this.getClass().getComponentType(), mElementData.length-1);

        if(index == 0) {
            System.arraycopy(mElementData, 1, removedArr, 0, mElementData.length);
            mElementData = removedArr;
            mSize -= 1;
        } else if (index == mElementData.length){
            System.arraycopy(mElementData, 0, removedArr, 0, mElementData.length-1);
            mElementData = removedArr;
            mSize -= 1;
        } else { // index between mElementData[0..n-1]
            System.arraycopy(mElementData, 0, removedArr, 0, index-1); // *********** test
            System.arraycopy(mElementData, index + 1, removedArr, index + 1, mElementData.length);// *********** test
            mElementData = removedArr;
            mSize -= 1;
        }
        return removedElement;
    }

    /**
     * Checks if the given index is in range (i.e., index is
     * non-negative and it not equal to or larger than the size of the
     * Array) and throws the IndexOutOfBoundsException if it's not.
     */
    private void rangeCheck(int index) {
        // TODO -- you fill in here.
        if (index < 0 || index >= mElementData.length) throw new IndexOutOfBoundsException();
        else System.out.println("Within range.");
    }

    /**
     * Returns the element at the specified position in this array.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this array
     * @throws IndexOutOfBoundsException
     */
    public E get(int index) {
        // TODO -- you fill in here.
        E element = null;
        if (index < 0 || index >= mElementData.length) throw new IndexOutOfBoundsException();
        else element = (E) mElementData[index];
        return element;
    }

    /**
     * Replaces the element at the specified position in this array with
     * the specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException
     */
    public E set(int index, E element) {
        // TODO -- you fill in here.
        E prevElement = null;
        if (index < 0 || index >= mElementData.length) throw new IndexOutOfBoundsException();
        else {
            // save old element, copy new element
            prevElement = (E) mElementData[index];
            mElementData[index] = element;
        }
        System.out.println("... Replaced " + index + "=" + prevElement.toString() + " with " + element.toString());
        return prevElement;
    }

    /**
     * Appends the specified element to the end of this array.
     *
     * @param element to be appended to this array
     * @return {@code true}
     */
    public boolean add(E element) {
        // TODO -- you fill in here.
        boolean changed = false;
        try{

            // no free space, so grow the array by one and add the element
            if(mElementData[0] != null && mSize == mElementData.length){
                Object[] new_mElementData = new Object[mElementData.length + 1];
                System.arraycopy(mElementData, 0, new_mElementData, 0, new_mElementData.length-1);
                new_mElementData[new_mElementData.length - 1] = element;
                mElementData = new_mElementData;
                mSize += 1;

            } else if (mElementData[0] == null){
                mElementData[0] = element;
                mSize += 1;
            } else {

                // find the index of the first null/last item/end of the array
                int i = mElementData.length - 1;
                while(mElementData[i] == null) {
                    i -= 1;
                }
                // add the item to the end
                mElementData[i + 1] = element;
                mSize += 1;
            }
            changed = true;
        } catch (NullPointerException e) {
            e.printStackTrace();
            changed = false;
        }
        return changed;
    }

    /**
     * Ensure the array is large enough to hold @a minCapacity
     * elements.  The array will be expanded if necessary.
     */
    private void ensureCapacityInternal(int minCapacity) {
        // TODO -- you fill in here.
        int aLen = mElementData.length;

        if (aLen - mSize - minCapacity >= 0){
            System.out.println("... Enough space to hold elements.");
        } else if (aLen - mSize - minCapacity < 0) {
            System.out.println("... Expanding array.");
            E[] expandedArr = (E[]) newInstance(this.getClass().getComponentType(),
                    mElementData.length + (aLen - mSize - minCapacity));
            System.arraycopy(mElementData, 0, expandedArr, 0, expandedArr.length);
            System.out.println("... Capacity ensured.");
        }
    }

    /**
     * Returns an array containing all of the elements in this Array
     * object in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to
     * it are maintained by this array.  (In other words, this method
     * must allocate a new array).  The caller is thus free to modify
     * the returned array.
     *
     * <p>This method acts as bridge between array-based and
     * collection-based APIs.
     *
     * @return an array containing all of the elements in this Array
     *         object in proper sequence
     */
    public Object[] toArray() {
        return Arrays.copyOf(mElementData, mSize);
    }

    /**
     * Returns an array containing all of the elements in this array in
     * proper sequence (from first to last element); the runtime type
     * of the returned array is that of the specified array.  If the
     * array fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of
     * the specified array and the size of this array.
     *
     * @param a the array into which the elements of the array are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the array
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this array
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < mSize)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(mElementData,
                    mSize,
                    a.getClass());

        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(mElementData,
                0,
                a,
                0,
                mSize);

        if (a.length > mSize)
            a[mSize] = null;
        return a;
    }

    /**
     * Returns an iterator over the elements in this Array in proper
     * sequence.
     *
     * @return an iterator over the elements in this Array in proper
     * sequence
     */
    public Iterator<E> iterator() {
        // TODO - you fill in here.
        return new ArrayIterator();
    }

    /**
     * This class defines an iterator over the elements in an Array in
     * proper sequence.
     */
    private class ArrayIterator
            implements Iterator<E> {
        /**
         * Current position in the Array (defaults to 0).
         */
        // TODO - you fill in here.
        int currIndex = 0;

        /**
         * Index of last element returned; -1 if no such element.
         */
        // TODO - you fill in here.
        int lastReturnedIndex = -1;

        /**
         * @return True if the iteration has more elements that
         * haven't been iterated through yet, else false.
         */
        @Override
        public boolean hasNext() {
            // TODO - you fill in here.
            boolean more;

            if(currIndex < mElementData.length - 1){
                more = true;
            } else { // returns false when on final element in mElementData
                more = false;
            }

            return more;
        }

        /**
         * @return The next element in the iteration.
         */
        @Override
        public E next() {
            // TODO - you fill in here.
            E nextElement = null;
            if (this.hasNext()) {
                nextElement = (E) mElementData[currIndex + 1];
                lastReturnedIndex = currIndex;
                currIndex += 1;
            } else if (this.currIndex == mElementData.length - 2) {
                System.out.println("Returning last element.");
                nextElement = (E) mElementData[mElementData.length - 1];
                lastReturnedIndex = currIndex;
                currIndex = 0; // reset to start of list/ default
            }
            else {
                System.out.println("No more elements available.");
            }
            return nextElement;
        }

        /**
         * Removes from the underlying collection the last element
         * returned by this iterator. This method can be called only
         * once per call to next().
         *
         * @throws IllegalStateException if no last element was
         * returned by the iterator
         */
        @Override
        public void remove() {
            // TODO - you fill in here
            try {
                MyArray.this.remove(this.lastReturnedIndex);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * The following methods and nested class use Java 8 features.
     */

    /**
     * Replaces each element of this array with the result of applying
     * the operator to that element.  Errors or runtime exceptions
     * thrown by the operator are relayed to the caller.
     *
     * @param operator the operator to apply to each element
     */
    public void replaceAll(UnaryOperator<E> operator) {
        // TODO -- you fill in here.
        for(Object o : mElementData){
            operator.apply((E) o); // applies this function to the given argument
        }
    }

    /**
     * Performs the given action for each element of the array until
     * all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing
     * class, actions are performed in the order of iteration (if an
     * iteration order is specified).  Exceptions thrown by the action
     * are relayed to the caller.
     *
     * @param action The action to be performed for each element
     */
    public void forEach(Consumer<? super E> action) {
        // TODO -- you fill in here.
        Iterator iterator = this.iterator();
        try{
            while(iterator.hasNext()){
                action.accept((E) iterator.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}