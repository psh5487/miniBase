package minibase;

import java.io.IOException;

/**
 * The delete operator. Delete reads tuples from its child operator and removes
 * them from the table they belong to.
 */
public class Delete extends Operator {

    private static final long serialVersionUID = 1L;

    private TransactionId tid;
    private DbIterator dbit;
    private TupleDesc tupdesc;
    private boolean  flag;

    // hint: implementation of Delete.java is not that much different from implementing Insert.java.
    /**
     * Constructor specifying the transaction that this delete belongs to as
     * well as the child to read from.
     * 
     * @param t
     *            The transaction this delete runs in
     * @param child
     *            The child operator from which to read tuples for deletion
     */
    public Delete(TransactionId t, DbIterator child) {
        // TODO: some code goes here
	tid = t;
    	dbit = child;
    	flag = false;

	Type[] type = new Type[]{Type.INT_TYPE};
    	String[] name = new String[]{"Del"};
   	tupdesc = new TupleDesc(type, name);
    }

    public TupleDesc getTupleDesc() {
        // TODO: some code goes here
        return tupdesc;
    }

    public void open() throws DbException, TransactionAbortedException {
        // TODO: some code goes here
	dbit.open();
    	super.open();
    }

    public void close() {
        // TODO: some code goes here
	dbit.close();
    	super.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // TODO: some code goes here
	dbit.close();
    }

    /**
     * Deletes tuples as they are read from the child operator. Deletes are
     * processed via the buffer pool (which can be accessed via the
     * Database.getBufferPool() method.
     * 
     * @return A 1-field tuple containing the number of deleted records.
     * @see Database#getBufferPool
     * @see BufferPool#deleteTuple
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // TODO: some code goes here
        if(flag)
            return null;
        
        int ctr = 0;
        while(dbit.hasNext())
        {
            Tuple tup = dbit.next();
            Database.getBufferPool().deleteTuple(tid, tup);
	    ctr++;
        }
            
        flag = true;
            
        Tuple t = new Tuple(tupdesc);
        t.setField(0, new IntField(ctr));
        return t;
    }

    @Override
    public DbIterator[] getChildren() {
        // TODO: some code goes here
        return null;
    }

    @Override
    public void setChildren(DbIterator[] children) {
        // TODO: some code goes here
    }

}
