
public class InternalString extends StringNode {
	StringNode Children[];
	int numChildren;

	public InternalString(){
		numChildren = 0;
		Children = new StringNode[maxNumKeys + 1];
	}

	protected int addToWhere(String key){
		if(size == 0)return 1;
		for(int i = 1; i < maxNumKeys; i++) {
			if(keys[i] == null)return i;
			if(keys[i].compareTo(key) >= 0)return i;
		}
		return maxNumKeys;
	}

	public void removeKey(int index){
		keys[index] = null;
		size--;
	}

	public void addKey(String key){
		int index = addToWhere(key);
		System.arraycopy(keys, index, keys, index + 1, size - (index - 1));
		keys[index] = key;
		size++;
	}
}