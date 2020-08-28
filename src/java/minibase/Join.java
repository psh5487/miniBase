package minibase;

import java.util.*;

/**
 * The Join operator implements the relational join operation.
 */
public class Join extends Operator {

    private static final long serialVersionUID = 1L;

    private JoinPredicate m_jp; //Predicate for joining children
    private DbIterator m_child1; //Left relation
    private DbIterator m_child2; //Right relation
    private Tuple temp;

    /**
     * Constructor. Accepts to children to join and the predicate to join them
     * on
     * 
     * @param p
     *            The predicate to use to join the children
     * @param child1
     *            Iterator for the left(outer) relation to join
     * @param child2
     *            Iterator for the right(inner) relation to join
     */
    public Join(JoinPredicate p, DbIterator child1, DbIterator child2) {
        // some code goes here
	m_jp = p;
	m_child1 = child1;
	m_child2 = child2;
    }

    public JoinPredicate getJoinPredicate() {
        // some code goes here
        return m_jp;
    }

    private String FieldNameCreator(TupleDesc td,int size)
    {
	String fieldName = "";
	for (int j = 0; j < size; j++)
	{
		fieldName += td.getFieldName(j) + " ";
	}
	return fieldName;	
    }

    /**
     * @return
     *       the field name of join field1. Should be quantified by
     *       alias or table name.
     * */
    public String getJoinField1Name() {
        // some code goes here
        TupleDesc first = m_child1.getTupleDesc();
	int count = first.numFields();
	return FieldNameCreator(first, count);
    }

    /**
     * @return
     *       the field name of join field2. Should be quantified by
     *       alias or table name.
     * */
    public String getJoinField2Name() {
        // some code goes here
        TupleDesc first = m_child2.getTupleDesc();
	int count = first.numFields();
	return FieldNameCreator(first, count);
    }

    /**
     * @see minibase.TupleDesc#merge(TupleDesc, TupleDesc) for possible
     *      implementation logic.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return TupleDesc.merge(m_child1.getTupleDesc(), m_child2.getTupleDesc());
    }

    public void open() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // some code goes here
	m_child1.open();
        m_child2.open();
        super.open();
    }

    public void close() {
        // some code goes here
        m_child1.close();
        m_child2.close();
        super.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        // some code goes here
	m_child1.rewind();
        m_child2.rewind();
    }

    /**
     * Returns the next tuple generated by the join, or null if there are no
     * more tuples. Logically, this is the next tuple in r1 cross r2 that
     * satisfies the join predicate. There are many possible implementations;
     * the simplest is a nested loops join.
     * <p>
     * Note that the tuples returned from this particular implementation of Join
     * are simply the concatenation of joining tuples from the left and right
     * relation. Therefore, if an equality predicate is used there will be two
     * copies of the join attribute in the results. (Removing such duplicate
     * columns can be done with an additional projection operator if needed.)
     * <p>
     * For example, if one tuple is {1,2,3} and the other tuple is {1,5,6},
     * joined on equality of the first column, then this returns {1,2,3,1,5,6}.
     * 
     * @return The next matching tuple.
     * @see JoinPredicate#filter
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // some code goes here
        //Left Tuples
	Tuple left = temp;
	if (left == null && !m_child1.hasNext()) {return null;} //no tuples to combine.
	if (left == null && m_child1.hasNext()) 
	{
		left = m_child1.next();
			//Begin at the first tuple
	}
	while (left != null)
	{
		while (m_child2.hasNext())
		{
			Tuple right = m_child2.next(); 
			//If tuple 1 and tuple 2 satisfy join predicate, join them
			if (m_jp.filter(left, right))
			{
				temp = left;
				//TupleMaker effectively performs the cross-product of left and right 
				return CrossProduct(left, right, getTupleDesc());
			}
		}
		//There are no more right tuples
		if (m_child1.hasNext())
		{
			//Move on to next left tuple and reset right tuple to beginning
			left = m_child1.next();
			m_child2.rewind();
		}
		//There are no more left or right tuples and the join is completed
		else { break; }
	}
	temp = null;
	return null;
    }

    private Tuple CrossProduct(Tuple left,Tuple right,TupleDesc td)
    {
	Tuple joined = new Tuple(td);
	int joinedSize = td.numFields();
	//TupleDesc is combination of left and right
	int leftSize = m_child1.getTupleDesc().numFields();
	//For each field in the merged TupleDesc, combine left and right tuples
	for (int j = 0 ; j < joinedSize; j++)
	{
		//Effectively the cross Product of Tuples
		if (j < leftSize) joined.setField(j,left.getField(j));
		else { joined.setField(j,right.getField(j - leftSize)); }
	}
	//joined is combination of left, right
	return joined;
    }

    @Override
    public DbIterator[] getChildren() {
        // some code goes here
        return new DbIterator[] { m_child1, m_child2 };
    }

    @Override
    public void setChildren(DbIterator[] children) {
        // some code goes here
	children[0] = m_child1;
	children[1] = m_child2;
    }

}
