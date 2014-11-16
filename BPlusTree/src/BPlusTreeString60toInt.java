
public class BPlusTreeString60toInt{

	public StringNode root;


	public Integer find(String key){
		if(root.size < 1)return null;
		return find(key, root);
	}


	public Integer find(String key, StringNode node){
		if(node instanceof LeafString){
			for(int i = 0; i < node.size; i++){
				if(node.keys[i].compareTo(key) == 0){
					return Integer.valueOf(Integer.parseInt((String)((LeafString)node).values[i]));
				}
			}
			return null;
		}

		if(node instanceof InternalString){
			for(int i = 1; i <= node.size; i++){
				if(node.keys[i].compareTo(key) > 0){
					return find(key, ((InternalString)node).Children[i - 1]);
				}
			}
			return find(key, ((InternalString)node).Children[node.size]);
		}
		return null;

	}

	public boolean put(String key, int value){
		if(root == null) root = new LeafString();
		TupleString split = add(key, value, root);
		if(split != null){
			InternalString temp = new InternalString();
			temp.addKey(split.key);
			temp.Children[0] = split.leftNode;
			temp.numChildren++;
			temp.Children[1] =  split.rightNode;
			temp.numChildren++;
			root = temp;
		}
		return true;
	}

	public TupleString add(String key, int value, StringNode node){
		if(node instanceof LeafString){
			if(node.size < node.maxNumKeys){
				((LeafString)node).addKey(key, Integer.valueOf(value));
				return null;
			}
			else{
				return new TupleString(key, Integer.valueOf(value), node);
			}
		}
		for(int i = 1; i <= node.size; i++){
			if(node.keys[i].compareTo(key) >= 0){
				TupleString split = add(key, value, ((InternalString)node).Children[i - 1]);
				if(split == null)return null;
				else return dealWithPromote(split.key, split.rightNode, node);
			}
		}

		TupleString split = add(key, value, ((InternalString)node).Children[node.size]);
		if(split == null)return null;
		else {
			((InternalString)node).Children[node.size] = split.leftNode;
			TupleString promoted = dealWithPromote(split.key, split.rightNode, node);
			return promoted;
		}
	}

	public TupleString dealWithPromote(String newKey, StringNode rightChild, StringNode node){
		if(newKey.compareTo(node.keys[node.size]) > 0){
			((InternalString)node).addKey(newKey);
			((InternalString)node).Children[node.size] = rightChild;
			((InternalString)node).numChildren++;
		} else{
			for(int i = 1; i <= node.size; i++){
				if(newKey.compareTo(node.keys[i]) >= 0) continue;
				for(int j = node.size; j >= i; j--){
					((InternalString)node).Children[j + 1] = ((InternalString)node).Children[j];
				}
				((InternalString)node).Children[i] = rightChild;
				((InternalString)node).numChildren++;
				break;
			}

			((InternalString)node).addKey(newKey);
		}
		if(node.size >= node.maxNumKeys){
			InternalString sibling = new InternalString();
			int mid = (int)(Math.floor(node.size / 2) + 1.0D);
			int change = 0;
			for(int i = mid + 1; i <= node.size; i++){
				sibling.addKey(node.keys[i]);
				change++;
			}
			for(int i = mid + 1; change > 0; i++){
				((InternalString)node).removeKey(i);
				change--;
			}
			int count = 0;
			for(int i = mid; i < ((InternalString)node).numChildren; i++){
				sibling.Children[count] = ((InternalString)node).Children[i];
				sibling.numChildren++;
				count++;
			}
			for(int i = mid; count > 0; i++){
				((InternalString)node).Children[i] = null;
				((InternalString)node).numChildren--;
				count--;
			}
			String promoteKey = node.keys[mid];
			((InternalString)node).removeKey(mid);
			return new TupleString(promoteKey, node, sibling);
		}

		return null;

	}
}