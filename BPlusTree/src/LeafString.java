import java.util.Arrays;


public class LeafString extends StringNode {

    public Object values[];
    public LeafString nextNode;
    public LeafString() {
        values = new String[maxNumKeys + 1];
        nextNode = null;
    }

    protected int addToWhere(String key){
        if(size == 0)return 0;
        for(int i = 0; i < maxNumKeys; i++) {
            if(keys[i] == null) return i;
            if(keys[i].compareTo(key) >= 0)return i;
        }
        return maxNumKeys;
    }

    public void addKey(String key, Object value){
        int index = addToWhere(key);
        System.arraycopy(keys, index, keys, index + 1, size - index);
        System.arraycopy(((Object) (values)), index, ((Object) (values)), index + 1, size - index);
        keys[index] = key;
        values[index] = value.toString();
        size++;
    }

    public void removeKey(int index) {
        keys[index] = null;
        values[index] = null;
        size--;
    }

    public LeafString getNextNode(){
        return nextNode;
    }

    public void setNextNode(LeafString nextNode){
        this.nextNode = nextNode;
    }

    public String toString(){
    	return "Leaf Node - " + super.keys[0] + " \t with keys : " + Arrays.toString(values) + "\n";
    }

}
