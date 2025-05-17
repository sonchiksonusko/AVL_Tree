import java.util.Comparator;

public class TreeNode <T extends Comparable> {
    public T data; // The data stored in the node
    public TreeNode<T> leftChild; // Reference to the left child node
    public TreeNode<T> rightChild; // Reference to the right child node
    public TreeNode<T> parent; // Reference to the parent node
    public int height; // The depth of the node in the tree
    
    public TreeNode(T data) {
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
        this.height = 1;
    }

    public int compare(T a , T b) {
        return a.compareTo(b); //ensures that the objects of type T can be compared using .compareTo().
        //when create a new AVLTree  must pass a Comparator<T> that defines how to compare
    }


}


