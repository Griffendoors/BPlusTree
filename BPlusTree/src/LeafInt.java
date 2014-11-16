import java.util.Arrays;


public class LeafInt extends IntNode{

	public String values[];
	public LeafInt nextNode;

	public LeafInt(){
		values = new String[maxNumKeys + 1];
		nextNode = null;
	}

	protected int addToWhere(int key){
		if(size == 0)return 0;
		for(int i = 0; i < maxNumKeys; i++){
			if(keys[i] == null)return i;
			if(keys[i].compareTo(key) >= 0) return i;
		}
		return maxNumKeys;
	}

	public void addKey(int key, Object value){
		int index = addToWhere(key);
		/*
		 * This is an extremely useful tool, it moves all elements in the source array from given index (param 1 & 2),
		 * To the destination array starting at given index  (param 3 & 4)
		 * The number of elements transfered is given by the fifth parameter
		 *
		 * This is call is moving all the leaf key elements up one place(number of keys - index = position to insert), to make room for the added key
		 * The same is done for the values
		 */
		System.arraycopy(keys, index, keys, index + 1, size - index);
		System.arraycopy(((Object) (values)), index, ((Object) (values)), index + 1, size - index);
		keys[index] = key;					// index will be empty after the arraycopy call, so the new key and value can be inserted in
		values[index] = value.toString();
		size++;
	}

	public void removeKey(int index){

		keys[index] = null;
		values[index] = null;
		size--;
	}

	public LeafInt getNextNode(){
		return nextNode;
	}

	public void setNextNode(LeafInt nextNode){
		this.nextNode = nextNode;	// the pointer to the next element in the keys chain at the base level of the Bplus tree, used for iterating through all Key-value pairs
	}

    public String toString(){
    	return "Leaf Node - " + super.keys[0] + " \t with keys : " + Arrays.toString(values) + "\n";
    }
}
