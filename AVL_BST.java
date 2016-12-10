//===========================================================================================================================
//	Program : To add elements in the AVL balanced tree
//===========================================================================================================================
//	@author: Karthika, Nevhetha, Kritika
// 	Date created: 2016/10/09
//===========================================================================================================================
import java.util.*;

public class AVL_BST<T extends Comparable<? super T>> {
	
	class AVLEntry<T> {
		int height;
		T element;
		AVLEntry<T> left, right, parent;
		
		AVLEntry(T x, AVLEntry<T> l, AVLEntry<T> r, AVLEntry<T> p) {
			height = 0;
			element = x;
			left = l;
			right = r;
			parent = p;
		}
	}
	
	AVLEntry<T> avlRoot;
	//nullNode used for null nodes shared by leaves
	AVLEntry<T> nullNode = new AVLEntry<>(null, null, null, null);
	int size;

	AVL_BST() {
		avlRoot = null;
		size = 0;
	}

	// Find x in subtree rooted at node t. Returns node where search ends.
	AVLEntry<T> find(AVLEntry<T> t, T x) {
		AVLEntry<T> pre = t;
		while (t != nullNode) {
			pre = t;
			int cmp = x.compareTo(t.element);
			if (cmp == 0) {
				return t;
			} else if (cmp < 0) {
				t = t.left;
			} else {
				t = t.right;
			}
		}
		return pre;
	}

	// Is x contained in tree?
	public boolean contains(T x) {
		AVLEntry<T> node = find(avlRoot, x);
		return node == nullNode ? false : x.equals(node.element);
	}

	int height(AVLEntry<T> node) {
		if (node == null) return -1;
		return node == nullNode ? -1 : node.height;
	}
	
	/** Procedure to insert elements in the tree 
	 * Runs in time O(logn) where n is the size of the array
	 * @param x : Integer to add
	 * @return : boolean, If tree contains a node with same key, replace element by
	 * Returns true if x is a new element added to tree. 
	 */
	public boolean avlAdd(T x) {
		boolean flag = false; 
		//Add to the root
		if (size == 0) {
			avlRoot = new AVLEntry<>(x, nullNode, nullNode, null);
		} else {
			AVLEntry<T> node = find(avlRoot, x);
			int cmp = x.compareTo(node.element);
			//Node already exists
			if (cmp == 0) {
				node.element = x;
				return false;
			}
			AVLEntry<T> newNode = new AVLEntry<>(x, nullNode, nullNode, node);
			//add to left or right child
			if (cmp < 0) {
				node.left = newNode;
				flag = false;
			} else {
				node.right = newNode;
				flag = true;
			}
			
			AVLEntry<T> tmpNode = avlRoot;
			do {
				AVLEntry<T> y1 = node.left;
				AVLEntry<T> y2 = node.right;
				//Update height till root after addition
				node.height = Math.max(height(y1), height(y2)) + 1;
				tmpNode = node.parent;
				
				if (height(y1) - height(y2) ==  2) {
					if (!flag) {
						//Left Left
						if (tmpNode == null)
							 avlRoot = singleRotateLeft(node);
						 else
							 tmpNode.left = singleRotateLeft(node);
					} else {
						//Left right
						if (tmpNode == null)
							 avlRoot = doubleRotateLeft(node);
						 else {
							 if (tmpNode.element.compareTo(x) < 0)
								 tmpNode.right = doubleRotateLeft(node); 
							 else
								 tmpNode.left = doubleRotateLeft(node); 
						 }
					}
				} else if (height(y1) - height(y2) ==  -2) {
					if (flag) {
						//Right Right
						if (tmpNode == null)
							 avlRoot = singleRotateRight(node);
						 else
							 tmpNode.right = singleRotateRight(node);
					} else {
						//Right Left
						if (tmpNode == null)
							 avlRoot = doubleRotateRight(node);
						 else {
							 if (tmpNode.element.compareTo(x) < 0)
								 tmpNode.right = doubleRotateRight(node); 
							 else
								 tmpNode.left = doubleRotateRight(node); 
						 }
					}
				}
				node = node.parent;
			}while (node != null);
		}
		size++;
		return true;
	}

	/** Procedure for rotate left
	 * @param node1 : AVLEntry, node which is unbalanced
	 * @return : node2, AVLEntry node to be after rotation
	 */
	public AVLEntry<T> singleRotateLeft(AVLEntry<T> node1) {
		AVLEntry<T> node2 = node1.left;
		node1.left = node2.right;
		node2.right = node1;
		node2.parent = node1.parent;
		node1.parent = node2;
		AVLEntry<T> y1 = node1.left;
		AVLEntry<T> y2 = node1.right;
		node1.height = Math.max(height(y1), height(y2)) + 1;
		y1 = node2.left;
		y2 = node2.right;
		node2.height = Math.max(height(y1), height(y2)) + 1;
		return node2;
	}
	
	/** Procedure for rotate right
	 * @param node1 : AVLEntry, node which is unbalanced
	 * @return : node2, AVLEntry node to be after rotation
	 */
	public AVLEntry<T> singleRotateRight(AVLEntry<T> node1) {
		AVLEntry<T> node2 = node1.right;
		node1.right = node2.left;
		node2.left = node1;
		node2.parent = node1.parent;
		node1.parent = node2;
		AVLEntry<T> y1 = node1.left;
		AVLEntry<T> y2 = node1.right;
		node1.height = Math.max(height(y1), height(y2)) + 1;
		y1 = node2.left;
		y2 = node2.right;
		node2.height = Math.max(height(y1), height(y2)) + 1;
		return node2;
	}
	
	/** Procedure for double rotate right left
	 * @param node1 : AVLEntry, node which is unbalanced
	 * @return : node2, AVLEntry node to be after rotation
	 */
	public AVLEntry<T> doubleRotateLeft(AVLEntry<T> node1) {
		node1.left = singleRotateRight(node1.left);
		return singleRotateLeft(node1);
	}
	
	/** Procedure for double rotate left right
	 * @param node1 : AVLEntry, node which is unbalanced
	 * @return : node2, AVLEntry node to be after rotation
	 */
	public AVLEntry<T> doubleRotateRight(AVLEntry<T> node1) {
		node1.right = singleRotateLeft(node1.right);
		return singleRotateRight(node1);
	}

	public static void main(String[] args) {
		AVL_BST<Integer> t = new AVL_BST<>();
		t.nullNode.height = -1;
		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			int x = in.nextInt();
			if (x > 0) {
				System.out.println("Add " + x + " : ");
				t.avlAdd(x);
				t.printTree();//t.levelOrderOrBST(t.avlRoot);
			} else if (x < 0) {
				System.out.print("Remove " + x + " : ");
				//t.remove(-x);
				t.printTree();
			} else {
				Comparable[] arr = t.toArray();
				System.out.print("Final: ");
				for (int i = 0; i < t.size; i++) {
					System.out.print(arr[i] + " ");
				}
				System.out.println();
				return;
			}
		}
		in.close();
	}
	//
	void levelOrderOrBST(AVLEntry<T> avlRoot) {
		if (avlRoot == nullNode)
			return;
		Queue<AVLEntry<T>> q = new ArrayDeque<AVLEntry<T>>();
		q.add(avlRoot);
		while (!q.isEmpty()) {
			AVLEntry<T> n = q.remove();
			System.out.print(n.element + " ");
			if (n.left != nullNode)
				q.add(n.left);
			if (n.right != nullNode)
				q.add(n.right);
		}
	}
	
	// Create an array with the elements using in-order traversal of tree
	public Comparable[] toArray() {
		Comparable[] arr = new Comparable[size];
		inOrder(avlRoot, arr, 0);
		return arr;
	}

	// Recursive in-order traversal of tree rooted at "node".
	// "index" is next element of array to be written.
	// Returns index of next entry of arr to be written.
	int inOrder(AVLEntry<T> node, Comparable[] arr, int index) {
		if (node != nullNode) {
			index = inOrder(node.left, arr, index);
			arr[index++] = node.element;
			index = inOrder(node.right, arr, index);
		}
		return index;
	}

	public void printTree() {
		System.out.print("[" + size + "]");
		printTree(avlRoot);
		System.out.println();
	}

	// Inorder traversal of tree
	void printTree(AVLEntry<T> node) {
		if (node != nullNode) {
			printTree(node.left);
			System.out.print(" " + node.element);
			printTree(node.right);
		}
	}
}