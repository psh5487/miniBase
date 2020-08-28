package minibase;

import java.util.*;

/**
 * SeqScan is an implementation of a sequential scan access method that reads
 * each tuple of a table in no particular order (e.g., as they are laid out on
 * disk).
 */
public class SeqScan implements DbIterator {

    private static final long serialVersionUID = 1L;

    private final TransactionId transactionId;
    private TupleDesc tupleDesc;
    private DbFileIterator iterator;
    private int tableId;
    private String tableAlias;

    /**
     * Creates a sequential scan over the specified table as a part of the
     * specified transaction.
     * 
     * @param tid
     *            The transaction this scan is running as a part of.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public SeqScan(TransactionId tid, int tableid, String tableAlias) {
        // TODO: some code goes here
	this.transactionId = tid;
        reset(tableid, tableAlias);
    }

    /**
     * @return
     *       return the table name of the table the operator scans. This should
     *       be the actual name of the table in the catalog of the database
     * */
    public String getTableName() {
	// TODO 
        return Database.getCatalog().getTableName(tableId);
    }
    
    /**
     * @return Return the alias of the table this operator scans. 
     * */
    public String getAlias()
    {
        // TODO: some code goes here
        return tableAlias;
    }

    private static String representPossiblyNullString(String string) {
        return (string == null) ? "null" : string;
    }

    /**
     * Reset the tableid, and tableAlias of this operator.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public void reset(int tableid, String tableAlias) {
        // TODO: some code goes here
	this.tableId = tableid;
        this.tableAlias = tableAlias;
        
	String tableAliasRepresentation = representPossiblyNullString(tableAlias);
        TupleDesc underlyingTupleDesc = Database.getCatalog().getTupleDesc(tableid);
        int tupleDescSize = underlyingTupleDesc.numFields();
        Type[] newTypes = new Type[tupleDescSize];
        String[] newFieldNames = new String[tupleDescSize];
        for (int i = 0; i < tupleDescSize; i++) {
            Type type = underlyingTupleDesc.getFieldType(i);
            String fieldName = underlyingTupleDesc.getFieldName(i);
            newTypes[i] = type;
            newFieldNames[i] = tableAliasRepresentation + "." + representPossiblyNullString(fieldName);
        }
        this.tupleDesc = new TupleDesc(newTypes, newFieldNames);
	
        this.iterator = Database.getCatalog().getDbFile(tableid).iterator(transactionId);
    }

    public SeqScan(TransactionId tid, int tableid) {
        this(tid, tableid, Database.getCatalog().getTableName(tableid));
    }

    public void open() throws DbException, TransactionAbortedException {
        // TODO: some code goes here
	// hint! to implement sequential scan you need to access the Database Fil
	this.iterator.open();
    }

    /**
     * Returns the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor. This prefix
     * becomes useful when joining tables containing a field(s) with the same
     * name.
     * 
     * @return the TupleDesc with field names from the underlying HeapFile,
     *         prefixed with the tableAlias string from the constructor.
     */
    public TupleDesc getTupleDesc() {
        // TODO: some code goes here
        return tupleDesc;
    }

    public boolean hasNext() throws TransactionAbortedException, DbException {
        // TODO: some code goes here
        return this.iterator.hasNext();
    }

    public Tuple next() throws NoSuchElementException,
            TransactionAbortedException, DbException {
        // TODO: some code goes here
        return this.iterator.next();
    }

    public void close() {
        // TODO: some code goes here
	this.iterator.close();
    }

    public void rewind() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // TODO: some code goes here
	this.iterator.rewind();
    }
}
