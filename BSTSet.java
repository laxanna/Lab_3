package lab3;

public class BSTSet {

	private TNode root;
 
	// Empty tree
	public BSTSet() {
		root = null;
	}

	// n*log(n)
	// log(n) since we check half a tree
	// n since there's n total nodes and we go through each
	public BSTSet(int[] input) {
		if (input.length == 0) {
			root = null;
		} else {
			int[] sortedInput = insertionSort(input);
			root = recursiveMaker(sortedInput, 0, sortedInput.length - 1);
		}
	}

	public TNode recursiveMaker(int[] input, int begin, int ending) {

		// Base case
		if (begin > ending) {
			return null;
		}

		// Get middle and make it a root
		int mid = (begin + ending) / 2;
		TNode nodes = new TNode(input[mid], null, null);

		// Make left and right side and make them left child and right child of root.
		nodes.left = recursiveMaker(input, begin, mid - 1);
		nodes.right = recursiveMaker(input, mid + 1, ending);
		return nodes;
	}

	// log(n)
	// log(n) when we split the tree
	public boolean isIn(int v) {
		if (root == null) {
			return false;
		}

		return (recursiveIsIn(root, v));
	}

	private boolean recursiveIsIn(TNode start, int v) {
		if (start == null) {
			return false;
		}

		if (start.element == v) {
			return true;
		}

		boolean leftCheck = recursiveIsIn(start.left, v);

		if (leftCheck == true) {
			return true;
		}

		boolean rightCheck = recursiveIsIn(start.right, v);

		return rightCheck;
	}

	// log(n)
	public void add(int v) {
		if (root == null) {
			root = new TNode(v, null, null);
		} else {
			recursionAdd(root, v);
		}
	}

	public TNode recursionAdd(TNode start, int v) {

		// If empty tree
		if (start == null) {
			start = new TNode(v, null, null);
		}

		// Check each half of the tree and add on the appropriate side
		if (v < start.element)
			start.left = recursionAdd(start.left, v);
		else if (v > start.element)
			start.right = recursionAdd(start.right, v);

		// If no change, return the node
		return start;
	}

	// log(n)
	public boolean remove(int v) {
		if (isIn(v) == false) {
			return false;
		} else {
			recursionRemove(root, v);
			return true;
		}
	}

	public TNode recursionRemove(TNode start, int v) {

		// if empty
		if (start == null) {
			return start;
		}

		// Go down the tree sides
		if (v < start.element) {
			start.left = recursionRemove(start.left, v);
		} else if (v > start.element) {
			start.right = recursionRemove(start.right, v);
		}

		// If this node is the one to be deleted
		else {

			// Single node, no more going down
			if (start.left == null) {
				return start.right;
			} else if (start.right == null) {
				return start.left;
			}

			// Get the smallest value in the right side of the tree
			start.element = minValue(start.right);

			// Delete
			start.right = recursionRemove(start.right, start.element);
		}

		return start;
	}

	int minValue(TNode start) {
		int minimum = start.element;
		while (start.left != null) {
			minimum = start.left.element;
			root = start.left;
		}
		return minimum;
	}

	// nlog(n) + klog(k)
	public BSTSet union(BSTSet s) {
		// New set to preserve old set contents
		BSTSet merger = new BSTSet();

		// Unionise this set and s
		//Have to do both since the sets are different
		recursionUnion(merger, root);
		recursionUnion(merger, s.root);
		return merger;
	}

	private void recursionUnion(BSTSet s, TNode start) {

		// If root not null
		if (start != null) {

			// Add the elements of the set together, then move left and right and continue
			// adding
			s.add(start.element);
			recursionUnion(s, start.left);
			recursionUnion(s, start.right);
		}
	}

	// nlog(n) + klog(k)
	public BSTSet intersection(BSTSet s) {
		BSTSet intersect = new BSTSet();
		recursionIntersection(s.root, intersect);
		return intersect;

	}

	public void recursionIntersection(TNode start, BSTSet s) {

		// If not empty
		if (start != null) {

			// If the element exists
			if (isIn(start.element)) {

				// Add the element to the new set, holding intersects
				s.add(start.element);
			}

			// Check left and right
			recursionIntersection(start.left, s);
			recursionIntersection(start.right, s);
		}
	}

	// nlog(n) + klog(k)
	public BSTSet difference(BSTSet s) {
		BSTSet different = new BSTSet();
		recursionDifference(root, s, different);
		return different;
	}

	public void recursionDifference(TNode start, BSTSet s, BSTSet total) {
		if (start != null) {
			if (s.isIn(start.element)) {
				// Cool, do nothing
			} else {

				// Add the element if it doesn't exist to total to hold the difference
				total.add(start.element);
			}
			recursionDifference(start.left, s, total);
			recursionDifference(start.right, s, total);

		}
	}

	// n, have to go through each node
	public int size() {

		// Base case
		if (root == null) {
			return 0;
		} else {
			return (recursionSize(root, 0));
		}
	}

	public int recursionSize(TNode start, int counting) {

		// Count the total size
		if (!(start == null)) {
			counting++;
			counting = recursionSize(start.left, counting);
			counting = recursionSize(start.right, counting);
		}
		return counting;
	}

	// n, have to go through all
	public int height() {
		return (recursionHeight(root));
	}

	public int recursionHeight(TNode start) {

		// If root node is null, then height no exist
		if (start == null) {
			return -1;
		} else {

			// Depth of the trees
			int leftHeight = recursionHeight(start.left);
			int rightHeight = recursionHeight(start.right);

			// Height = deepest depth + 1
			if (leftHeight > rightHeight) {
				return (leftHeight + 1);
			} else {
				return (rightHeight + 1);
			}
		}
	}

	public void printBSTSet() {
		if (root == null) {
			System.out.println("The set is empty");
		} else {
			System.out.print("The set elements are: ");
			printBSTSet(root);
			System.out.print("\n");
		}
	}

	private void printBSTSet(TNode t) {
		if (t != null) {
			printBSTSet(t.left);
			System.out.print(" " + t.element + ", ");
			printBSTSet(t.right);
		}
	}

	public TNode getRoot() {
		return root;
	}

	// Typical insertion sort
	private int[] insertionSort(int array[]) {
		int length = array.length;

		for (int i = 1; i < length; ++i) {
			int current = array[i];
			int j = i - 1;

			while (j >= 0 && array[j] > current) {
				array[j + 1] = array[j];
				j = j - 1;
			}
			array[j + 1] = current;
		}
		int hold = array[array.length - 1];
		int counter = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != hold) {
				counter++;
			}
			hold = array[i];
		}
		int[] complete = new int[counter];
		hold = array[array.length - 1];
		counter = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != hold) {
				complete[counter] = array[i];
				counter++;
			}
			hold = array[i];
		}
		return complete;
	}

	// n
	public void printNonRec() {

		System.out.print("Elements are: ");
		// Make stack
		MyStack stacked = new MyStack();

		// Make root
		TNode now = root;

		// First loop makes inner loop go until now == null
		while (true) {

			// Print left most node
			while (now != null) {
				stacked.push(now);
				now = now.left;
			}

			// If empty stack, break
			if (stacked.IsEmpty()) {
				break;
			}

			// Then parent
			now = stacked.pop();

			// Then right node
			System.out.print(now.element + ", ");
			now = now.right;
		}
	}

	// n
	public void printLevelOrder() {
		System.out.print("Elements are: ");
		// root node
		TNode now = root;

		// Has a value
		if (now != null) {

			// Make my queue
			MyQueue queued = new MyQueue();

			// Enqueue the root
			queued.enqueue(now);

			// As long as not empty
			while (!queued.IsEmpty()) {

				// Dequeue the node
				TNode node = queued.dequeue();
				System.out.print(node.element + ", ");

				// Check left
				if (node.left != null) {
					queued.enqueue(node.left);
				}

				// Check right
				if (node.right != null) {
					queued.enqueue(node.right);
				}
			}

			// If empty tree
		} else {
			System.out.print("Binary tree is empty");
		}
	}
}
