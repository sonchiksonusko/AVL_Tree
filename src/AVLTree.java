import java.util.Comparator;
public class AVLTree<T extends Comparable<T>> {
    public TreeNode<T> root; // The root node of the AVL tree
    private Comparator<T> comparator; // Comparator (that we have already created to compare movies)

   public AVLTree(Comparator<T> comparator) {
        this.root = null; // Initialize the root to null
        this.comparator = comparator; // Set the comparator for the tree
    }


    public int getBalanceFactor(TreeNode<T> root){
        if(root == null){
            return 0;
        }
        return getHeight(root.leftChild) - getHeight(root.rightChild); // Calculate the balance factor
    }

    public void inOrderTraversal(TreeNode<T> root) {
    if (root != null) {
        inOrderTraversal(root.leftChild);
        System.out.println(root.data); // toString() in Movie
        inOrderTraversal(root.rightChild);
    }
}




public int getHeight(TreeNode<T> root){
        if(root == null){
            return -1;
    }
        return root.height; // Return the depth of the node
    }

public boolean insert( T data){
    TreeNode<T> current = this.root;
    boolean status = true;
    if(root == null){
        this.root = new TreeNode<>(data);
        return status;
    }
   TreeNode<T> parent = null;

   while(current!=null){// Traverse the tree to find the correct position for the new node
    parent = current;
    if(comparator.compare(data,current.data)<0){// If the new data is less than the current node's data, go left
        current = current.leftChild;
      }
    else { //
        current = current.rightChild;
        }
   }
    TreeNode<T> newNode= new TreeNode<T>(data);
    newNode.parent = parent;// Set the parent of the new node
    if(comparator.compare(data,parent.data)<0){// If the new data is less than the parent node's data, insert it as the left child
        parent.leftChild = newNode;
    }
    else{
        parent.rightChild = newNode;
    }
    current =newNode;
    while(current !=null){
        updateHeight(current); // Update the depth of the new node
        int balanceFactor = getBalanceFactor(current); // Get the balance factor of the current node
         if (balanceFactor > 1) { // Left-heavy
            if (getBalanceFactor(current.leftChild) >= 0) {
                LL_Rotation(current); // Left-Left Case
            } else {
                LR_Rotation(current); // Left-Right Case
            }
        } else if (balanceFactor < -1) { // Right-heavy
            if (getBalanceFactor(current.rightChild) <= 0) {
                RR_Rotation(current); // Right-Right Case
            } else {
                RL_Rotation(current); // Right-Left Case
            }
        }
        current = current.parent; // Move up the tree

    }
   
    return status;
   
}

public void updateHeight(TreeNode<T> node){
    int leftHeight = getHeight(node.leftChild);
    int rightHeight = getHeight(node.rightChild);
    node.height = Math.max(leftHeight, rightHeight) + 1; // Update the depth of the node
}

public void LL_Rotation(TreeNode<T> A) {
    TreeNode<T> B = A.leftChild;

    B.parent = A.parent; // Update parent of B
    A.parent = B; // Update parent of A
    A.leftChild = B.rightChild; // Set A's left child to B's right child
    if (B.rightChild != null) // If B's right child is not null, set its parent to A
        B.rightChild.parent = A;
    B.rightChild = A; // Set B's right child to A
    if(B.parent == null) {
        this.root = B;
    } else if(B.parent.leftChild == A) {
        B.parent.leftChild = B;
    } else {
        B.parent.rightChild = B;
    }



    // Update heights
    updateHeight(A);
    updateHeight(B);
}

public void RR_Rotation(TreeNode<T> A) {
    TreeNode<T> B = A.rightChild;

    B.parent = A.parent; // Update parent of B
    A.parent = B; // Update parent of A
    A.rightChild = B.leftChild; // Set A's right child to B's left child
    if (B.leftChild != null) { // If B's left child is not null, set its parent to A
        B.leftChild.parent = A;
    }
    B.leftChild = A; // Set B's left child to A
    if(B.parent == null) {
        this.root = B;
    }else if(B.parent.leftChild == A) {
        B.parent.leftChild = B;
    } else {
        B.parent.rightChild = B;
    }

    // Update heights
    updateHeight(A);
    updateHeight(B);

    
}
public void LR_Rotation(TreeNode<T> A) {
    RR_Rotation(A.leftChild); // Fix the subtree first
    LL_Rotation(A);                  // Return new root after LL rotation
}

public void RL_Rotation(TreeNode<T> A) {
    LL_Rotation(A.rightChild); // Fix the subtree first
    RR_Rotation(A);                    // Return new root after RR rotation
}

public TreeNode<T> findMin(TreeNode<T> root) {
   if (root == null) {
        return null;
    }
   while (root.leftChild != null) {
        root = root.leftChild;
    }
    return root;
}

public TreeNode<T> findMax(TreeNode<T> root) {
    if (root == null) return null;
    while (root.rightChild != null) {
        root = root.rightChild;
    }
    return root;
}






}











