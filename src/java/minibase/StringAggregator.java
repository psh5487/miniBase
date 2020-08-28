package minibase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Knows how to compute some aggregate over a set of StringFields.
 */
public class StringAggregator implements Aggregator {

    private static final long serialVersionUID = 1L;

    //Group Field, Type
    private int m_gbfield;
    private Type m_gbfieldtype;
    //Aggregation Field
    private int m_afield;
    //Operator => ONLY COUNT is SUPPORTED FOR STRINGS
    private Op m_op;

    private TupleDesc m_td;
    private ArrayList<Tuple> tupleGroup;

    /**
     * Aggregate constructor
     * @param gbfield the 0-based index of the group-by field in the tuple, or NO_GROUPING if there is no grouping
     * @param gbfieldtype the type of the group by field (e.g., Type.INT_TYPE), or null if there is no grouping
     * @param afield the 0-based index of the aggregate field in the tuple
     * @param what aggregation operator to use -- only supports COUNT
     * @throws IllegalArgumentException if what != COUNT
     */

    public StringAggregator(int gbfield, Type gbfieldtype, int afield, Op what) {
        // some code goes here
	if (what != Op.COUNT) throw new IllegalArgumentException(); //Only COUNT OP is Supported
	   	m_gbfield = gbfield;
		m_gbfieldtype = gbfieldtype;
		m_afield = afield;
		m_op = what;
		
		//If no grouping => tuple is of form ( aggregate Value )
		if (gbfield == Aggregator.NO_GROUPING)
		{
			Type [] temp = new Type[1];
			temp[0] = Type.INT_TYPE;
			m_td = new TupleDesc(temp);
		}
		//If grouping => tuple is of form ( groupValue, aggregate Value ) 
		else
		{
			Type [] temp = new Type[2];
			temp[0] = m_gbfieldtype;
			temp[1] = Type.INT_TYPE;
			m_td = new TupleDesc(temp);
		}
		
		tupleGroup = new ArrayList<Tuple>();
    }

    /**
     * Merge a new tuple into the aggregate, grouping as indicated in the constructor
     * @param tup the Tuple containing an aggregate field and a group-by field
     */
    public void mergeTupleIntoGroup(Tuple tup) {
        // some code goes here
	if (m_gbfield == Aggregator.NO_GROUPING)
		{
			//NO GROUPING
			if (tupleGroup.size() == 0)
			{
				//FIRST TUPLE
				Tuple curTup = new Tuple(m_td);
				//Initializing the count to be 1
				curTup.setField(0, new IntField(1));
				tupleGroup.add(curTup);
			}
			else
			{
				//There are already other tuples inserted.
				Tuple curTup = tupleGroup.get(0);
				IntField temp = (IntField) curTup.getField(1);
				//Incrementing the count by 1
				curTup.setField(0, new IntField(temp.getValue()+1));
			}
		}
		else
		{
			//GROUPING
			Field tupGroup = tup.getField(m_gbfield);
			Tuple current = null;
			//Find tuple to Group
			for (int i = 0; i < tupleGroup.size(); i++)
			{
				Tuple temp = tupleGroup.get(i);
				Field tempField = temp.getField(0);
				if (tempField.equals(tupGroup))
				{
					current = temp;
				}
			}
			//First Tuple to group
			if (current == null)
			{
				current = new Tuple(m_td);
				//Initializing Count to 1
				current.setField(0, tupGroup);
				current.setField(1, new IntField(1));
				tupleGroup.add(current);
			}
			//There are already grouped tuples
			else
			{
				//Incrementing the count
				IntField temp1 = (IntField) current.getField(1);
				current.setField(1, new IntField(temp1.getValue()+1));
			}
		}
    }

    //public static class StringAggregatorIterator extends Operator {

    //    private static final long serialVersionUID = 1L;

    //    private StringAggregator stringAggregator;
    //    private Iterator<Field> iterator;
    //    private DbIterator child;

    //    StringAggregatorIterator(StringAggregator stringAggregator) {
    //        this.stringAggregator = stringAggregator;
    //        this.iterator = stringAggregator.numTuples.keySet().iterator();
    //    }

    //    public TupleDesc getTupleDesc() {
    //        return stringAggregator.tupleDesc;
    //    }

    //    public void open()
    //            throws DbException, NoSuchElementException, TransactionAbortedException {
    //        iterator = stringAggregator.numTuples.keySet().iterator();
    //    }

    //    public void close() {
    //        iterator = null;
    //    }

    //    public void rewind() throws DbException, TransactionAbortedException {
    //        iterator = stringAggregator.numTuples.keySet().iterator();
    //    }

    //    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
    //        if (iterator == null) {
    //            return null;
    //        }
    //        while (iterator.hasNext()) {
    //            Field field = iterator.next();
    //            int result = stringAggregator.numTuples.get(field);
    //            Tuple tuple = new Tuple(getTupleDesc());
    //            if (stringAggregator.gbField == NO_GROUPING) {
    //                tuple.setField(0, new IntField(result));
    //            } else {
    //                tuple.setField(0, field);
    //                tuple.setField(1, new IntField(result));
    //            }
    //            return tuple;
    //        }
    //        return null;
    //    }

//	@Override
//    	public DbIterator[] getChildren() {
//            // some code goes here
//            return new DbIterator[]{this.child};
//    	}
//
//    	@Override
//    	public void setChildren(DbIterator[] children) {
//            // some code goes here
//	    //if (children.length != 1) {
//              //  throw new DbException("Expected only one child!");
//            //}
//            child = children[0];
//    	}
//    }

    /**
     * Create a DbIterator over group aggregate results.
     *
     * @return a DbIterator whose tuples are the pair (groupVal,
     *   aggregateVal) if using group, or a single (aggregateVal) if no
     *   grouping. The aggregateVal is determined by the type of
     *   aggregate specified in the constructor.
     */
    public DbIterator iterator() {
        // some code goes here
        //throw new UnsupportedOperationException("please implement me for proj2");
	return new TupleIterator(m_td, tupleGroup);
    }

}
