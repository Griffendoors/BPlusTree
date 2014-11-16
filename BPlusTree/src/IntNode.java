import java.util.Arrays;


public abstract class IntNode {

    public int maxNumKeys;
    int size;
    Integer keys[];

    public IntNode(){
        size = 0;
        maxNumKeys = 5;
        keys = new Integer[maxNumKeys + 1];
    }

    protected abstract int addToWhere(int i);

}