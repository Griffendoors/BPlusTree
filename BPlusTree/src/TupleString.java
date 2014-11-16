public class TupleString{

	public final String key;
	public final StringNode rightNode;
	public final StringNode leftNode;

	public TupleString(String toPromote, StringNode leftNode, StringNode sibling){ // Constructor that is provided to the partitioned nodes already
		key = toPromote;
		rightNode = sibling;
		this.leftNode = leftNode;
	}

	public TupleString(String key, Object value, StringNode node){	// Constructor that does the splitting
		((LeafString)node).addKey(key, value);			// add the k/v to the leaf node
		LeafString sibling = new LeafString();			// create a sibling leaf
		int mid = (int)Math.floor((node.size + 1) / 2);	// value for leaf values to split
		int change = 0;
		for(int i = mid; i < node.size; i++) {
			sibling.addKey(node.keys[i], ((LeafString)node).values[i]);		// move values from leaf to sibling ( half each )
			change++;
		}

		for(int i = mid; change > 0; i++){
			((LeafString)node).removeKey(i);		// remove the copied elements from the original ( as now in sibling )
			change--;
		}

		sibling.setNextNode(((LeafString)node).getNextNode());		// update pointers for leafs
		((LeafString)node).setNextNode(sibling);
		this.key = sibling.keys[0];
		rightNode = sibling;
		leftNode = (LeafString)node;
	}

}