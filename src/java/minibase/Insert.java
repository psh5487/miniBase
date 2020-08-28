package minibase;

import java.io.IOException;

/**
 * Inserts tuples read from the child operator into the tableid specified in the
 * constructor
 */
public class Insert extends Operator {

    private static final long serialVersionUID = 1L;

    private final int tableId;
    private final TransactionId transactionId;
    private DbIterator child;
    private boolean open;
    private boolean inserted;

    /**
     * Constructor.
     * 
     * @param t
     *            The transaction running the insert.
     * @param child
     *            The child operator from which to read tuples to be inserted.
     * @param tableid
     *            The table in which to insert tuples.
     * @throws DbException
     *             if TupleDesc of child differs from table into which we are to
     *             insert.
     */
    public Insert(TransactionId t,DbIterator child, int tableid)
            throws DbException {
        // TODO: some code goes here
	this.transactionId = t;
        this.child = child;
        this.tableId = tableid;
        this.open = false;
    }

    public TupleDesc getTupleDesc() {
        // TODO: some code goes here
        return new TupleDesc(new Type[]{Type.INT_TYPE}, new String[]{"NUMBER INSERTED"});
    }

    public void open() throws DbException, TransactionAbortedException {
        // TODO some code goes here
	// hint: you have to consider parent class as well
	child.open();//
	super.open();//
	this.open = true;
        this.inserted = false;
    }

    public void close() {
        // TODO: some code goes here
	child.close();//	
	super.close();
        this.open = false;
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // TODO: some code goes here
	//close();
        //open();
	child.rewind();
    }

    /**
     * Inserts tuples read from child into the tableid specified by the
     * constructor. It returns a one field tuple containing the number of
     * inserted records. Inserts should be passed through BufferPool. An
     * instances of BufferPool is available via Database.getBufferPool(). Note
     * that insert DOES NOT need check to see if a particular tuple is a
     * duplicate before inserting it.
     * 
     * @return A 1-field tuple containing the number of inserted records, or
     *         null if called more than once.
     * @see Database#getBufferPool
     * @see BufferPool#insertTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // TODO: some code goes here
	// hint: insert Tuples passed by iterator (child) to Buffer.
        if (inserted) {
            return null;
        }
        child.open();
        int count = 0;
        while (child.hasNext()) {
            Tuple toBeInsertedTuple = child.next();
            try {
                Database.getBufferPool().insertTuple(transactionId, tableId, toBeInsertedTuple);
            } catch (IOException e) {
                e.printStackTrace();
                throw new DbException("IOException caught while trying to insert tuple.");
            }
            count++;
        }
        child.close();
        Tuple resultTuple = new Tuple(getTupleDesc());
        resultTuple.setField(0, new IntField(count));
        this.inserted = true;
        return resultTuple;
    }

    @Override
    public DbIterator[] getChildren() {
        // TODO: some code goes here
	// hint! there is only one element you can pass through DbIterator[]
        return new DbIterator[]{child};
    }

    @Override
    public void setChildren(DbIterator[] children) {
        // TODO: some code goes here
	if (open) {
            throw new IllegalStateException("Child cannot be set while open.");
        }
        this.child = children[0];
    }
}
