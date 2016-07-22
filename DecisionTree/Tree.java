import java.util.ArrayList;

public class Tree {
	public Node buildTree(ArrayList<ArrayList<Integer>> data, ArrayList<Integer> data_label, Node root) {
//		String s = null;
//		for ( int i = 0 ; i < DecisionT.num_dimension ; i++) {
//			s = s + " "+ Boolean.toString(DecisionT.AtrributeUsed[i]); 
//		} 
//		System.out.println(s);
		// Calculate entropy
		int bestDi = -1;
		double bestGain = 0;
		root.entropy = Entropy.calcEntropy(root.data_label);
		// If entropy = 0 or all attributes are used => leaf node 
		if (root.entropy == 0 || DecisionT.CheckAllUsed()) {
			int[] count = new int[DecisionT.NUM_LABEL];		
			for ( int k = 0 ; k < DecisionT.NUM_LABEL ; k++) {
				count[k] = 0;
			}
			// find the most label value in data set
			for (int i = 0 ; i < data_label.size(); i++) {
				for ( int j = 0 ; j < DecisionT.NUM_LABEL ; j++) {
					if ( data_label.get(i) == DecisionT.label_value[j]){
						count[j]++;	
					}
				}
			}
			int bestLabelid = -1;
			int max = Integer.MIN_VALUE; 
			for (int i = 0 ; i < DecisionT.NUM_LABEL; i++) {
				if (count[i] > max ) {
					max = count[i];
					bestLabelid = i;
				}
			}
			
			root.label = DecisionT.label_value[bestLabelid];
			return root;
		}
		// Calc Gain information
		for ( int di = 0 ; di < DecisionT.num_dimension ; di++) {
			if(DecisionT.AtrributeUsed[di] == false) {
				double gain = Entropy.calcGain(root.entropy , data, data_label, di);
				// Find a dimension with best Gain value
				if ( gain > bestGain) {
					bestDi = di;
					bestGain = gain;
				}
			}
		}
		// Build sub-tree
		if ( bestDi != -1) {
			root.Di = bestDi;
			root.children = new Node[DecisionT.NUM_VALUE[bestDi]];
			DecisionT.AtrributeUsed[bestDi] = true;
			
			for ( int i = 0; i < DecisionT.NUM_VALUE[bestDi]; i++) {
				root.children[i] = new Node();
				root.children[i].parent = root;
				root.children[i].setData(data, data_label, bestDi, i);		
				
			}
			
			for ( int i = 0; i < DecisionT.NUM_VALUE[bestDi]; i++) {
				buildTree(root.children[i].data, root.children[i].data_label, root.children[i]);
			}
		}
		else {
			return root;
		}
		return root;
	}
	
	// Predict label for new instance
	public int TreePredict(ArrayList<Integer> record, Node root) {
		int deep = 0;
		while(root.children != null) {
			deep++;
			int nodeValue = record.get(root.Di);
			for ( int i = 0 ; i < DecisionT.NUM_VALUE[root.Di] ; i++) {
				if ( nodeValue == i) {
					root = root.children[i];
					break;
				}
			}
			if (deep > DecisionT.num_dimension)
				return -1;
		}		
		return root.label;
	}		 
}
