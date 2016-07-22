import java.util.ArrayList;

public class Node {
	Node parent;
	Node[] children;
	ArrayList<ArrayList<Integer>> data; // data for each node
	ArrayList<Integer> data_label;     // label for each node
	int label;   // label for leaf node
	double entropy; // entropy value
	int Di;      // which dimension that Node hold
	
	public Node() {
		this.data = new ArrayList<ArrayList<Integer>>();
		this.data_label = new ArrayList<Integer>();
		entropy = 0;
		parent = null;
		children = null;
		label = -1;
	}
	// Get sub data that has certain value in dimension di from whole data set. 
	public void setData(ArrayList<ArrayList<Integer>> rootdata, ArrayList<Integer> rootlabel, int di, int value) {
		int num_data = rootdata.size();
		for ( int i = 0 ; i < num_data ; i++) {
			//for (int j = 0 ; j < DecisionT.num_dimension ; j++) {
				if ( rootdata.get(i).get(di) == value ) {
					data.add(rootdata.get(i));
					data_label.add(rootlabel.get(i));
				}
			//}
		}
	}
}
