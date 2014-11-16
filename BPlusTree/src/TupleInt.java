public class TupleInt{

	public final int key;
	public final IntNode rightNode;
	public final IntNode leftNode;

	public TupleInt(int toPromote, IntNode leftNode, IntNode sibling){	// Constructor that is provided to the partitioned nodes already
		key = toPromote;
		rightNode = sibling;
		this.leftNode = leftNode;
	}

	public TupleInt(int key, Object value, IntNode node){				// Constructor that does the splitting
		((LeafInt)node).addKey(key, value);
		LeafInt sibling = new LeafInt();
		int mid = (int)Math.floor((node.size + 1) / 2);
		int change = 0;
		for(int i = mid; i < node.size; i++) {
			sibling.addKey(node.keys[i], ((LeafInt)node).values[i]);
			change++;
		}

		for(int i = mid; change > 0; i++){
			((LeafInt)node).removeKey(i);
			change--;
		}

		sibling.setNextNode(((LeafInt)node).getNextNode());
		((LeafInt)node).setNextNode(sibling);
		this.key = sibling.keys[0];
		rightNode = sibling;
		leftNode = (LeafInt)node;
	}

}