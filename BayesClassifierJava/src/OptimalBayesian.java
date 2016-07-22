import java.util.ArrayList;
import Jama.Matrix;

public class OptimalBayesian extends BayesianMethods {
	ArrayList<Matrix> MeanList = new ArrayList<Matrix>();
	ArrayList<Matrix> CovList = new ArrayList<Matrix>();
	String[] UniqueTrainLabel;
	
	public void Training(ArrayList<ArrayList<Double>> TrainData,
			ArrayList<String> TrainLabel) {
		MeanList.clear();
		CovList.clear();
		// Unique label
		UniqueTrainLabel = UniqueLabel(TrainLabel);
		// Get list instance of each label
		ArrayList<ArrayList<Integer>> listByLabel = GetElementbyLabel(
				TrainLabel, UniqueTrainLabel);
		// Get data for each label and calc mean and covariance
		for (int i = 0; i < listByLabel.size(); i++) {
			ArrayList<Integer> list = listByLabel.get(i);
			ArrayList<ArrayList<Double>> MatrixData = new ArrayList<ArrayList<Double>>();

			for (int k = 0; k < list.size(); k++) {
				MatrixData.add(TrainData.get(list.get(k)));
			}
			Matrix mean = MeanMatrix(MatrixData);
			Matrix cov = FullCovMatrix(MatrixData, mean);
			MeanList.add(mean);
			CovList.add(cov);
		}
	}

	public String Testing(ArrayList<Double> TestData) {
		// Voting label
		int[] labelCount = new int[UniqueTrainLabel.length];
		for (int i = 0 ; i < UniqueTrainLabel.length; i++)
			labelCount[i] = 0;
		// Voting pair
		for (int m = 0 ; m < UniqueTrainLabel.length ; m++){
			for ( int k = m + 1; k < UniqueTrainLabel.length ; k++) {
				double scoreM = MahalanobisDistance(TestData,CovList.get(m),MeanList.get(m));
				double scoreK = MahalanobisDistance(TestData,CovList.get(k),MeanList.get(k));
				if ( scoreM < scoreK)
					labelCount[m]++;
				else
					labelCount[k]++;
			}
		}
		// Find max count
		String testlabel = new String() ;
		double maxCount = Integer.MIN_VALUE;
		for ( int i = 0 ; i < labelCount.length; i++){
			if (labelCount[i] > maxCount){
				maxCount = labelCount[i];
				testlabel = UniqueTrainLabel[i];
			}
				
		}
		return testlabel;
	}
	
	public double CrossValidation(	ArrayList<ArrayList<Double>> data, ArrayList<String> label){
		
		int num_data = data.size();
		
		int num_train = (int) (num_data * 0.9);
		int num_test = num_data - num_train;
		double[] accuracy = new double[10];
		ArrayList<ArrayList<Double>> train_data = new ArrayList<ArrayList<Double>>();
		ArrayList<ArrayList<Double>> test_data = new ArrayList<ArrayList<Double>>();
		ArrayList<String> train_label = new ArrayList<String>();
		ArrayList<String> test_label = new ArrayList<String>();
		ArrayList<String> predict_label = new ArrayList<String>();
		
		for ( int i = 0 ; i < 10 ; i++ ){
			System.out.println(i);
			train_data.clear(); test_data.clear(); train_label.clear(); test_label.clear();predict_label.clear();
			// create permutation			 
			 ArrayList<Integer> per = PermutationArray(num_data, 0, num_data);
			// Get train_data and label
			for ( int k = 0 ; k < num_train ; k++){
				train_data.add(data.get(per.get(k)));
				train_label.add(label.get(per.get(k)));
			}
			// Get test data and label
			for ( int k = num_train ; k < num_data ; k++){
				test_data.add(data.get(per.get(k)));
				test_label.add(label.get(per.get(k)));
			}
			// Train 
			Training(train_data,train_label);
			// Test
			for ( int m = 0 ; m < test_data.size(); m++){
				String TestLabel = Testing(test_data.get(m));
				predict_label.add(TestLabel);
			}
			// Compare to get accuracy
			int sum = 0;
			for ( int ia = 0 ; ia < num_test ; ia++ ){
				if ( predict_label.get(ia).equals(test_label.get(ia))) 
					sum += 1;
			}
			accuracy[i] = (double)sum / (num_test * 1.0);
			System.out.println(accuracy[i]);
		}
		double acc = 0.0;
		for ( int i = 0 ; i < 10 ; i++) {
			acc += accuracy[i]/10;			
		}
		return acc;
	}
	public void Test(ArrayList<ArrayList<Double>> data, ArrayList<String> label){
		ArrayList<String> predict_label = new ArrayList<String>();
		// Train 
		Training(data,label);
		// Test
		for ( int m = 0 ; m < data.size(); m++){
			String TestLabel = Testing(data.get(m));			
			predict_label.add(TestLabel);
		}
		int sum = 0;
		for (int i = 0 ; i < label.size(); i++)
			if (label.get(i).equals(predict_label.get(i)))
				sum++;
		double acc = (sum * 1.0) / label.size();
		System.out.println("Accuracy: " + acc);
	}
}
