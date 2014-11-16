/**
  Implements a B+ tree in which the keys are integers and the
  values are Strings (with maximum length 60 characters)
 */

public class BPlusTreeIntToString60 {


	public IntNode root;


	public String find(int key){
		if(root.size<1) return null;
		return find(key,root);
	}
	public String find(int key, IntNode node){
		if(node instanceof LeafInt){									//if root is a leaf -> depth = 1 - > find k/v - > return
			for(int i = 0; i< node.size; i++){
				if(node.keys[i].compareTo(Integer.valueOf(key))==0){
					return (String)((LeafInt)node).values[i];
				}
			}
			return null;
		}

		if(node instanceof InternalInt){								// else depth>1 - > recursive tree search
			for(int i = 1; i<=node.size; i++){
				if(node.keys[i].compareTo(Integer.valueOf(key))> 0){
					return find(key,((InternalInt)node).Children[i-1]);
				}
			}
			return find(key,((InternalInt)node).Children[node.size]);
		}
		else{
			return null;												// cant be found
		}
	}


	public boolean put(int key, String value){							// given a k/v to add to tree
		if(this.root == null) root = new LeafInt();						// case: tree is empty, create new root
		TupleInt split = add(key, value, root);							// create the tuple, decides whether split is neccesary
		if(split != null){												// simple : k/b was added into place, add() returned null
			InternalInt temp = new InternalInt();
			temp.addKey(split.key);
			temp.Children[0] = split.leftNode;
			temp.numChildren++;;										//change pointers appropriatley
			temp.Children[1] =  split.rightNode;
			temp.numChildren++;
			root = temp;
		}
		return true;
	}

	public TupleInt add(int key, String value, IntNode node){
		if(node instanceof LeafInt){									// if depth = 1,
			if(node.size < node.maxNumKeys){							// and there is space in the node
				((LeafInt)node).addKey(Integer.valueOf(key), value);	// add the k/v to the root
				return null;											// return null - no changes made to tree
			}else{
				return new TupleInt(Integer.valueOf(key), value, node);	// else node too full,  perform tuple split, return it
			}
		}
		// else depth > 1
		for(int i = 1; i <= node.size; i++){
			if(node.keys[i].compareTo(Integer.valueOf(key)) >= 0){		// check for room in node, use position i as index to add k/v to
				TupleInt split = add(key, value, ((InternalInt)node).Children[i - 1]);
				if(split == null)return null;							// if internal node has room ( ie split returns null) just put into place
				else return dealWithPromote(split.key, split.rightNode, node);		// else we have internal node that is too full, so something needs to be promoted
			}
		}
		TupleInt split = add(key, value, ((InternalInt)node).Children[node.size]);	// k/v needs to be added as a duplicate, with values still stored in leaf
		if(split == null) return null;												// there is room in the appropriate leaf position
		else{
			((InternalInt)node).Children[node.size] =  split.leftNode;
			TupleInt promoted = dealWithPromote(split.key, split.rightNode, node);
			return promoted;
		}
	}



	public TupleInt dealWithPromote(Integer newKey, IntNode rightChild, IntNode node){
		if(newKey.compareTo(node.keys[node.size]) > 0){
			((InternalInt)node).addKey(newKey);
			((InternalInt)node).Children[node.size] = rightChild;
			((InternalInt)node).numChildren++;							// put the rightchild into the node in the appropriate position, spilling over the end if neccesary
		} else{															// else find the right place in to add the element
			for(int i = 1; i <= node.size; i++){
				if(newKey.compareTo(node.keys[i]) >= 0) continue;		// wrong place, next iter
				for(int j = node.size; j >= i; j--){
					((InternalInt)node).Children[j + 1] = ((InternalInt)node).Children[j];
				}
				((InternalInt)node).Children[i] = rightChild;
				((InternalInt)node).numChildren++;
				break;
			}
			((InternalInt)node).addKey(newKey);
		}


		if(node.size >= node.maxNumKeys){								// once added to relative pos, if too many elements contained
			InternalInt sibling = new InternalInt();
			int mid = (int)(Math.floor(node.size / 2) + 1.0D);
			int change = 0;
			for(int i = mid + 1; i <= node.size; i++){
				sibling.addKey(node.keys[i]);
				change++;
			}
			for(int i = mid + 1; change > 0; i++){
				((InternalInt)node).removeKey(i);
				change--;
			}
			int count = 0;
			for(int i = mid; i < ((InternalInt)node).numChildren; i++){
				sibling.Children[count] = ((InternalInt)node).Children[i];
				sibling.numChildren++;
				count++;
			}
			for(int i = mid; count > 0; i++){						// perform appropriate changes to nodes where elements are added / changed / removed
				((InternalInt)node).Children[i] = null;
				((InternalInt)node).numChildren--;
				count--;
			}
			int promoteKey = node.keys[mid];
			((InternalInt)node).removeKey(mid);
			return new TupleInt(promoteKey, node, sibling);
		} else return null;											// no change is made to the tree - enough space in node that got added to
	}
}




























