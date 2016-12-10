//===========================================================================================================================
//	Program : Program to rewrite remove method so that it toggles between removing the minimum element in the right sub tree  and maximum element in the left subtree.
//===========================================================================================================================
//	@author: Karthika, Nevhetha, Kritika
// 	Date created: 2016/10/09
//===========================================================================================================================
 /*
  * Structure provided by Dr. Balaji
  */
import java.util.*;

class BST<T extends Comparable<? super T>> {
	class Entry<T> {
		T element;
		Entry<T> left,right,parent;
		Entry(T x,Entry<T> l,Entry<T> r,Entry<T> p) {
			element = x;
			left = l;
			right = r;
			parent = p;
		}
	}
	
	Entry<T> root;
	int size;
	int state = 0;
	
	BST() {
		root = null;
		size = 0;
	}
	
	Entry<T> find(Entry<T> node ,T x) {
		Entry<T> prev = node;
		while(node!=null) {
			prev = node;
			int cmp = x.compareTo(node.element);
			if(cmp == 0)
				return node;
			else if (cmp < 0)
				node = node.left;
			else
				node = node.right;
		}
		return prev;
	}
	
	public boolean contain(T x) {
		Entry<T> t = find(root,x);
        return t == null?false : x.equals(t.element);
	}
	
	public boolean add(T x) {
		if(size == 0)
			root = new Entry<T>(x,null,null,null);
		else {
			Entry<T> t = find(root,x);
			int cmp = x.compareTo(t.element);
			if(cmp == 0) {
				t.element = x;
				return false;
			}
			Entry<T> newNode = new Entry<T>(x,null,null,t);
			if(cmp < 0)
				t.left = newNode;
			else
				t.right = newNode;
		}
		size++;
		return true;
	}
	
	public T remove(T x) {
		if(root == null){
			System.out.println("No element in the tree");
			return null;
		}
		T rv = null;
		Entry<T> t = find(root,x);
		if(x.equals(t.element)) {
			rv = t.element;
			remove(t);
			size--;
		}
		return rv;
	}
	
	void remove(Entry<T> node) {

		if(node.left != null && node.right != null)
			removeTwo(node);
		else
			removeOne(node);
	}
	
	void removeOne(Entry<T> node) {
		if(node == root) {
			Entry<T> nc = oneChild(node);
			root = nc;
			if(nc != null)
			 nc.parent = null;
		}
		else {
			Entry<T> p = node.parent;
			Entry<T> nc = oneChild(node);
			if(p.left == node)
				p.left = nc;
			else
				p.right = nc;
			if(nc != null)
				nc.parent = p;
		}
	}
	
	Entry<T> oneChild(Entry<T> node) {
		return node.left == null ? node.right : node.left;
	}
	/*
	 * @ Function to remove the node which has two children. The function automatically toggles between choosing either
	the maximum of left subtree or the minimum of right subtree
	 * @param : Entry<T> node : Contains the root node of the tree
	 */

	void removeTwo(Entry<T> node) {
		Entry<T> rv;
		if(state == 0){
			rv = node.right;
			while(rv.left != null)
				rv = rv.left;
			state = 1;
		}
		else {
			rv = node.left;
			while(rv.right != null)
				rv = rv.right;
			state = 0;	
		}
		node.element = rv.element;
		removeOne(rv);
	}
	
	public void printTree() {
		System.out.print("[" + size + "]");
		printTree(root);
		System.out.println();
	    }

	    // Inorder traversal of tree
	    void printTree(Entry<T> node) {
		if(node != null) {
		    printTree(node.left);
		    System.out.print(" " + node.element);
		    printTree(node.right);
		}
	    }
	    
	    public Comparable[] toArray() {
	    	Comparable[] arr = new Comparable[size];
	    	inOrder(root, arr, 0);
	    	return arr;
	        }
	    int inOrder(Entry<T> node, Comparable[] arr, int index) {
	    	if(node != null) {
	    	    index = inOrder(node.left, arr, index);
	    	    arr[index++] = node.element;
	    	    index = inOrder(node.right, arr, index);
	    	}
	    	return index;
	        }


}
public class BSTDriver {
	public static void main(String[] args) {
		BST<Integer> t = new BST<>();
		Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
		    int x = in.nextInt();
		    if(x > 0) {
			System.out.print("Add " + x + " : ");
			t.add(x);
			t.printTree();
		    } else if(x < 0) {
			System.out.print("Remove " + x + " : ");
			t.remove(-x);
			t.printTree();
		    } else {
			Comparable[] arr = t.toArray();
			System.out.print("Final: ");
			for(int i=0; i<t.size; i++) {
			    System.out.print(arr[i] + " ");
			}
			System.out.println();
			return;
		    }		
		}
		in.close();
	}
}
