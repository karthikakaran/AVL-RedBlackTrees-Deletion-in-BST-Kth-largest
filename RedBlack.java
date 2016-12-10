import java.util.Scanner;
public class RedBlack<T extends Comparable<? super T>> {
	/*----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 *  @dateCreated:		-October-04-2016
	 *  @dateLastModified:	-October-04-2016
	 *  @author: 			-Nevhetha
	 *----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 *  
	 *  @comment:			-
	 *
	 *  @memberVariable: 	-variableName_dataType:					accessSpecifier:		description:	
	 *  					-root_Entry<T>:							private					root of the tree
	 *  					-size_int:								private					size(# of nodes) of the tree
	 *  
	 *  @constructor: 		-constructorSignature:											description:
	 *  					-AVL():															Non-Parameterized Constructor
	 *  
	 *  @memberFunction: 	-methodSignature:												description:
	 *  					-public boolean add(T x):										adds x to the tree
	 *  					-private void backtrack(Entry<T> node,T x):						checks for any violation of height and fixes them
	 *  					-private Entry<T>  balance(Entry<T> node,T x):					fixes the tree if the tree is not balanced
	 *  					-private void rotateLeft(Entry<T> node):						performs rotation about the right child of the node
	 *  					-private void rotateRight(Entry<T> node):						performs rotation about the left child of the node
	 *					
	 *  --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
	 */
    class Entry<T> {
    	/*----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    	 *  @dateCreated:		-October-04-2016
    	 *  @dateLastModified:	-October-04-2016
    	 *  @author: 			-Nevhetha
    	 *----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    	 *  
    	 *  @comment:			-
    	 *
    	 *  @memberVariable: 	-variableName_dataType:					accessSpecifier:		description:	
    	 *  					-element_T:								private					value of the node
    	 *  					-left_Entry<T>:							private					left child
    	 *  					-right_Entry<T>:						private					right child
    	 *  					-parent_Entry<T>:						private					parent of the node
    	 *  					-color_boolean:							private					color of the node
    	 *  																					false- red , true-black
    	 *  
    	 *  @constructor: 		-constructorSignature:											description:
    	 *  					-Entry(T x,boolean h, Entry<T> l, Entry<T> r, Entry<T> p) 			Parameterized Constructor
    	 *  
    	 *  @memberFunction: 	-methodSignature:												description:
    	 *					
    	 *  --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------		
    	 */
        T element;
        boolean color;
        Entry<T> left, right, parent;

        Entry(T x,boolean h, Entry<T> l, Entry<T> r, Entry<T> p) {
            element = x;
            color=false;
	    left = l;
	    right = r;
	    parent = p;
        }
    }
    
    Entry<T> root;
    int size;

    RedBlack() {
	root = null;
	size = 0;
    }
    
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------
	 *  @dateLastModified:	-October-04-2016
	 *  @author: 			-Nevhetha
	 *-------------------------------------------------------------------------------------------------------------------------------------------------------
	 *  
	 *  @comment:			-add(..) is a function that adds the element into the tree just like a regular BST
	 *
	 *  @param: 			-variableName_dataType:							description:	
	 *  					-x_T:											value to be added to the tree
	 *  
	 *  @localVariables: 	-variableName_dataType:							description:
	 *  					-node_Entry<T>:									node whose value is equal to x
	 *  					-cmp_int:										result of comparing node and x
	 *  					-newNode_Entry<>T:								node to be added to the tree
	 *  
	 *  @return:			-variableName_dataType:							description:
	 *  					-X_boolean										true if the insertion was successful, false if there is an element already of the
	 *  																	same value
	 *  
	 *  -----------------------------------------------------------------------------------------------------------------------------------------------------	
	 *  
	 *  	
	 */
    public boolean add(T x) {
    	if(size == 0) {
    	    root = new Entry<>(x,false, null, null, null);
    	} else {
    	    Entry<T> node = find(root, x);
    	    int cmp = x.compareTo(node.element);
    	    if(cmp == 0) {
    		node.element = x;
    		return false;
    	    }
    	    Entry<T> newNode = new Entry<>(x, false,null, null, node);
    	    if(cmp < 0) {
    	    	node.left = newNode;
    	    } else {
    	    	node.right = newNode;
    	    }
    	    if(!node.color){
    	    	repair(node,newNode);
    	    }
    	}
    	size++;
    	if(!root.color){
    		root.color=true;
    		if(root.left!=null&&root.left.color==false)
    			root.left.color=true;
    		if(root.right!=null&&root.right.color==false)
    			root.right.color=true;
    	}
    	return true;
     }
    
    
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------
	 *  @dateLastModified:	-October-04-2016
	 *  @author: 			-Nevhetha
	 *-------------------------------------------------------------------------------------------------------------------------------------------------------
	 *  
	 *  @comment:			-repair(..) is a function that checks if the constraints of the Red-Black tree are met and fixes it if not
	 *
	 *  @param: 			-variableName_dataType:							description:	
	 *  					-node_Entry<T>:									node where the color is to be checked is compatible with the child's color						
	 *  					-newNode_Entry<T>:								child of the node 'node'
	 *  
	 *  @localVariables: 	-variableName_dataType:							description:
	 *  				
	 *  @return:			-variableName_dataType:							description:
	 *  
	 *  -----------------------------------------------------------------------------------------------------------------------------------------------------	
	 *  
	 *  	
	 */
    private void repair(Entry<T> node,Entry<T> newNode) {
		if(node.parent.left==node||node.parent.right==node){
			if(node.parent.left==node){
				if(node.parent.right!=null&&node.parent.right.color==false){ // parent's sibling is red
					node.parent.left.color=true;
					node.parent.color=false;
					node.parent.right.color=true;
					if(node.parent.parent!=null){
						if(node.parent.color==false &&node.parent.parent.color==false)
							repair(node.parent.parent,node.parent);
					}
				}
				else{ //parent's sibling is black
					
					if(node.left==newNode){ //Left-Left case
						leftLeft(node);
					}
					else { //Left - Right case
						Entry<T> g=node.parent;
						g.left=newNode;
						newNode.parent=g;
						newNode.left=node;
						node.parent=newNode;
						node.right=null;
						leftLeft(node.parent);
					}
				}
			}
			else if(node.parent.right==node){//parent's sibling is red
				if(node.parent.left!=null&&node.parent.left.color==false){
					node.parent.left.color=true;
					node.parent.color=false;
					node.parent.right.color=true;
					if(node.parent.parent!=null){
						if(node.parent.color==false &&node.parent.parent.color==false)
							repair(node.parent.parent,node.parent);
					}
				}
				else
				{ // parent's sibling is black
					if(node.right==newNode){//Right -Right case
						rightRight(node);
					}
					else{//Right-left case
						Entry<T> g=node.parent;
						g.right=newNode;
						newNode.parent=g;
						newNode.right=node;
						node.parent=newNode;
						node.left=null;
						rightRight(node.parent);
					}
				}
			}		
		}
	}

    
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  @dateLastModified:	-October-04-2016
   	 *  @author: 			-Nevhetha
   	 *-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  
   	 *  @comment:			-rightRight(..) is a function handles the case where the inbalance in the color is due to the right - right nodes
   	 *
   	 *  @param: 			-variableName_dataType:							description:	
   	 *  					-node_Entry<T>:									node whose color and that of its right child's is red						
   	 *  
   	 *  @localVariables: 	-variableName_dataType:							description:
   	 *  				
   	 *  @return:			-variableName_dataType:							description:
   	 *  
   	 *  -----------------------------------------------------------------------------------------------------------------------------------------------------	
   	 *  
   	 *  	
   	 */
	private void rightRight(Entry<T> node) {
			rotateLeft(node);
			if(node==root){
				node.left.color=false;
				node.right.color=false;
				node.color=true;
			}
			else
				swapColors(node,node.left);
	}

	  
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  @dateLastModified:	-October-04-2016
   	 *  @author: 			-Nevhetha
   	 *-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  
   	 *  @comment:			-leftLeft(..) is a function handles the case where the inbalance in the color is due to the left-left nodes
   	 *
   	 *  @param: 			-variableName_dataType:							description:	
   	 *  					-node_Entry<T>:									node whose color and that of its left child's is red						
   	 *  
   	 *  @localVariables: 	-variableName_dataType:							description:
   	 *  				
   	 *  @return:			-variableName_dataType:							description:
   	 *  
   	 *  -----------------------------------------------------------------------------------------------------------------------------------------------------	
   	 *  
   	 *  	
   	 */
	private void leftLeft(Entry<T> node) {
			rotateRight(node);
			if(node==root){
				node.left.color=false;
				node.right.color=false;
				node.color=true;
			}
			else
				swapColors(node.right,node);
	}

	private void rotateLeft(RedBlack<T>.Entry<T> node) {
		Entry<T> g=node.parent;
		Entry<T> p=node;
		if(g.parent!=null){
			if(g.parent.left==g)
				g.parent.left=p;
			else
				g.parent.right=p;
		}
		if(p.left!=null){
			g.right=p.left;
			g.right.parent=g;
		}
		else
			g.right=null;
		p.left=g;
		p.parent=g.parent;
		g.parent=g;
		if(g==root)
			root=p;
	}

	private void swapColors(Entry<T> node, Entry<T> right) {
		boolean c=node.color;
		node.color=right.color;
		right.color=c;
		
	}

	private void rotateRight(Entry<T> node) {
		Entry<T> g=node.parent;
		Entry<T> p=node;
		if(g.parent!=null){
			if(g.parent.left==g)
				g.parent.left=p;
			else
				g.parent.right=p;
		}
		if(p.right!=null){
			g.left=p.right;
			g.left.parent=g;
		}
		else
			g.left=null;
		p.right=g;
		p.parent=g.parent;
		g.parent=p;	
		if(g==root)
			root=p;
	}

	/*-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  @dateLastModified:	-October-04-2016
   	 *  @author: 			-Nevhetha
   	 *-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  
   	 *  @comment:			-find(...) is a function that Finds x in subtree rooted at node t
   	 *
   	 *  @param: 			-variableName_dataType:							description:	
   	 *  					-t_Entry<T>:									root of the tree in which x is to be searched
   	 *  					-x_T:											value to be searched
   	 *  
   	 *  @localVariables: 	-variableName_dataType:							description:
   	 *  					-cmp_int:										result of comparing x with current element
   	 *  
   	 *  @return:			-variableName_dataType:							description:
   	 *  					-pre_Entry<T>:									node where search ends.
   	 *  -----------------------------------------------------------------------------------------------------------------------------------------------------	
   	 *  
   	 *  	
   	 */
    Entry<T> find(Entry<T> t, T x) {
	Entry<T> pre = t;
	while(t != null) {
	    pre = t;
	    int cmp = x.compareTo(t.element);
	    if(cmp == 0) {
		return t;
	    } else if(cmp < 0) {
		t = t.left;
	    } else {
		t = t.right;
	    }
	}
	return pre;
    }
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  @dateLastModified:	-October-04-2016
   	 *  @author: 			-Nevhetha
   	 *-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  
   	 *  @comment:			-printTree(...) is a function that prints the tree by calling upon printTree(root)\
   	 *  					-It also prints the size of the tree.
   	 *
   	 *  @param: 			-variableName_dataType:							description:	
   	 *  
   	 *  @localVariables: 	-variableName_dataType:							description:
   	 *  
   	 *  @return:			-variableName_dataType:							description:
   	 *  -----------------------------------------------------------------------------------------------------------------------------------------------------	
   	 *  
   	 *  	
   	 */
    public void printTree() {
    	System.out.print("[" + size + "]");
    		printTree(root);
    		System.out.println();
     }
    
    /*-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  @dateLastModified:	-October-04-2016
   	 *  @author: 			-Nevhetha
   	 *-------------------------------------------------------------------------------------------------------------------------------------------------------
   	 *  
   	 *  @comment:			-printTree(...) is a function that prints the tree rooted a node according to its inorder traversal.
   	 *
   	 *  @param: 			-variableName_dataType:							description:	
   	 *  					-node_Entry<T>:									root of the tree to be printed
   	 *  
   	 *  @localVariables: 	-variableName_dataType:							description:
   	 *  
   	 *  @return:			-variableName_dataType:							description:
   	 *  -----------------------------------------------------------------------------------------------------------------------------------------------------	
   	 *  
   	 *  	
   	 */
        void printTree(Entry<T> node) {
        	if(node != null) {
        		printTree(node.left);
        		System.out.print(" " + node.element+"color "+node.color);
        		printTree(node.right);
        	}
        }
        
    public static void main(String[] args){
    	RedBlack<Integer> t = new RedBlack<>();
    	Scanner in = new Scanner(System.in);
    	while(in.hasNext()) {
    	    int x = in.nextInt();
    	    if(x > 0) {
    	    	System.out.print("Add " + x + " : ");
    	    	t.add(x);
    	    	t.printTree();
    	    } 
    	    if(x==0)
    	    	break;
    	}
    }
}
    