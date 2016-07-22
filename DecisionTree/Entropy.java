import java.util.ArrayList;

public class Entropy {	
	static double calcEntropy(ArrayList<Integer> data_label) {
		if ( data_label == null)
			return 0.0;
		if (data_label.size() == 0)
			return 0.0;
		
		int[] count = new int[DecisionT.NUM_LABEL];		
		for ( int k = 0 ; k < DecisionT.NUM_LABEL ; k++) {
			count[k] = 0;
		}
		// Count number of each label in data set 
		for (int i = 0 ; i < data_label.size(); i++) {
			for ( int j = 0 ; j < DecisionT.NUM_LABEL ; j++) {
				if ( data_label.get(i) == DecisionT.label_value[j]){
					count[j]++;	
				}
			}
		}
		// Calc entropy value
		double entropy = 0.0;
		for ( int i = 0 ; i < DecisionT.NUM_LABEL; i++) {
			double probability = count[i] / (double)data_label.size();
			if (probability == 0.0)
				continue;
			entropy += -probability * (Math.log(probability) / Math.log(2));
		}
		return entropy;
	}
	// Calc value for data set with its entropy, label and which dimension to calculate
	static double calcGain(double rootEntropy, ArrayList<ArrayList<Integer>> data , ArrayList<Integer> data_label, int di) {
		int num_data = data.size();
		ArrayList<Integer> subSize = new ArrayList<Integer>();
		ArrayList<Double> subEntropy = new ArrayList<Double>();
		// Count number each value that emerge in dataset on dimension di. 
		for ( int k = 0 ; k < DecisionT.NUM_VALUE[di] ; k++) {
			int value = k;
			ArrayList<Integer> subLabel = new ArrayList<Integer>();
			
			for ( int i = 0 ; i < num_data ; i++) {				
					if ( data.get(i).get(di) == value ) {
						//data.add(data.get(i));
						subLabel.add(data_label.get(i));
					}
			}
			subSize.add(subLabel.size());
			subEntropy.add(calcEntropy(subLabel));
		}
		double GainValue = rootEntropy;
		for ( int k = 0 ; k < DecisionT.NUM_VALUE[di] ; k++) { 
			GainValue += -(subSize.get(k) / ((double)num_data)) * subEntropy.get(k);
		}
		
		return GainValue;
	}
}	
