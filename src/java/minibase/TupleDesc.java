package minibase;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * A helper class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;

        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }

        public static List<TDItem> getListFrom(Type[] typeAr, String[] fieldAr) {
            List<TDItem> list = new ArrayList<TDItem>();
            for (int i = 0; i < typeAr.length; i++) {
                list.add(new TDItem(typeAr[i], fieldAr[i]));
            }
            return list;
        }
    }

    /**
     * @return An iterator which iterates over all the field TDItems that are
     *         included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return null;
    }

    private static final long serialVersionUID = 1L;

    private final List<TDItem> fieldList;
    private final Map<String, Integer> nameToIdMap;
    private final int size;
    private final String stringFormat;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr array specifying the number of and types of fields in this
     *          TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names
     *          may be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
	// TODO: some code goes here
	this(TDItem.getListFrom(typeAr, fieldAr));
    }
	
    private TupleDesc(List<TDItem> fieldList) {
        this.fieldList = new ArrayList<TDItem>(fieldList);
        this.nameToIdMap = new HashMap<String, Integer>();
        int sizeCalculation = 0;
        for (TDItem tdItem : fieldList) {
            sizeCalculation += tdItem.fieldType.getLen();
        }
        this.size = sizeCalculation;
       
	String result = "";

        for (TDItem tdItem : fieldList) {
            result += tdItem.toString();
        }

        this.stringFormat = result;
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with fields
     * of the specified types, with anonymous (unnamed) fields.
     *
     * @param typeAr array specifying the number of and types of fields in this
     *          TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
	// TODO: some code goes here
        this(typeAr, new String[typeAr.length]);
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // TODO: some code goes here
	return fieldList.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     *
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // TODO: some code goes here, don't forget to check index range!
	// + return "null" string for null case
	if (i >= 0 && i < numFields()) {
            return fieldList.get(i).fieldName;
        }
        else {
	    return null;
  	}
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     *
     * @param i The index of the field to get the type of. It must be a valid
     *          index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // TODO: some code goes here
	if (i >= 0 && i < numFields()) {
            return fieldList.get(i).fieldType;
        }
        else {
            return null;
        }
    }

    /**
     * Find the index of the field with a given name.
     *
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
	// TODO: some code goes here
	// hint! how to throw exception? refer hashCode() function in this class

        if (nameToIdMap.containsKey(name)) {
            return nameToIdMap.get(name).intValue();
        }
        for (int i = 0; i < numFields(); i++) {
            String field = getFieldName(i);
            if (field != null && field.equals(name)) {
                nameToIdMap.put(name, new Integer(i));
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc. Note
     *         that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
	// TODO: some code goes here
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     *
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
	// TODO: some code goes here
        List<TDItem> newTdItems = new ArrayList<TDItem>(td1.fieldList);
        newTdItems.addAll(td2.fieldList);
        return new TupleDesc(newTdItems);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     *
     * @param o the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
        if (this == o)
                return true;        

	if ( o == null || ! o.getClass().equals(this.getClass()) )
		return false;

	// TODO: some code goes here
        TupleDesc other = (TupleDesc) o;

        if (fieldList == null) {
            if (other.fieldList != null)
                return false;
        } else {
            int nItems = fieldList.size();
            if (other.fieldList.size() != nItems)
                return false;

            for (int i = 0; i < nItems; i++) {
                if (fieldList.get(i).fieldType != other.fieldList.get(i).fieldType)
                    return false;
            }
        }
        return true;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although the
     * exact format does not matter.
     *
     * @return String describing this descriptor.
     */
    public String toString() {
	// some code goes here
        return "";
    }
}
