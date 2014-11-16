import java.util.Arrays;


public abstract class StringNode {

	public int maxNumKeys;
	int size;
	String keys[];

	public StringNode() {
		size = 0;
		maxNumKeys = 5;
		keys = new String[maxNumKeys + 1];
	}

	protected abstract int addToWhere(String string);
}