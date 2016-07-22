import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class RunOnData extends DecisionT {

	ArrayList<ArrayList<Double>> Data = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Integer>> DiscreteData = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> Label = new ArrayList<Integer>();

	// Return a sorted array of Unique value of an input array
	static public Double[] Unique(Double[] array) {
		Set<Double> set = new LinkedHashSet<Double>(Arrays.asList(array));
		Double[] uniqueArray = set.toArray(new Double[] {});
		// sort
		double[] tmp = new double[uniqueArray.length];
		for (int i = 0; i < tmp.length; i++)
			tmp[i] = uniqueArray[i];
		Arrays.sort(tmp);
		for (int i = 0; i < tmp.length; i++)
			uniqueArray[i] = tmp[i];

		return uniqueArray;
	}

	static public Integer[] Unique(Integer[] array) {
		Set<Integer> set = new LinkedHashSet<Integer>(Arrays.asList(array));
		Integer[] uniqueArray = set.toArray(new Integer[] {});
		// sort
		int[] tmp = new int[uniqueArray.length];
		for (int i = 0; i < tmp.length; i++)
			tmp[i] = uniqueArray[i];
		Arrays.sort(tmp);
		for (int i = 0; i < tmp.length; i++)
			uniqueArray[i] = tmp[i];

		return uniqueArray;
	}

	// get maximum number of an array
	public Double getMax(Double[] array) {
		Double max = -Double.MAX_VALUE;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max)
				max = array[i];
		}
		return max;
	}

	// get minimum number of an array
	public Double getMin(Double[] array) {
		Double min = Double.MAX_VALUE;
		for (int i = 0; i < array.length; i++) {
			if (array[i] < min)
				min = array[i];
		}
		return min;
	}
	// Discrete data in one dimension
	public ArrayList<Integer> DiscreteArray(ArrayList<Double> array) {
		int num_value = 5;
		// Array to return
		ArrayList<Integer> DiscreteData = new ArrayList<Integer>();
		// get min, max value of an array
		Double max = getMax(array.toArray(new Double[] {}));
		Double min = getMin(array.toArray(new Double[] {}));
		// get average distance of num_value discrete value
		Double DeltaValue = (max - min) / (double) num_value;
		// find center value of each discrete value
		double[] centerValue = new double[5];
		for (int i = 0; i < num_value; i++)
			centerValue[i] = min + i * DeltaValue + DeltaValue / 2.0;
		// discrete data to value from 0 -> 5 that according to its cluster
		for (int k = 0; k < array.size(); k++) {
			int min_i = -1;
			double min_distance = Double.MAX_VALUE;
			for (int i = 0; i < num_value; i++) {
				double distance = Math.abs(array.get(k) - centerValue[i]);
				if (distance < min_distance) {
					min_i = i;
					min_distance = distance;
				}
			}
			DiscreteData.add(min_i);
		}
		return DiscreteData;
	}
	// Discrete data in whole dataset
	public void DiscreteDataSet() {
		int[][] tmp = new int[num_data][num_dimension];
		// Discrete data
		// Get column
		for (int k = 0; k < num_dimension; k++) {
			ArrayList<Double> column = new ArrayList<Double>();
			for (int i = 0; i < num_data; i++) {
				column.add(Data.get(i).get(k));
			}
			Double[] ColumnValue = Unique(column.toArray(new Double[] {}));
			// if less than 5 unique value in one dimension
			if (ColumnValue.length <= 5) {
				NUM_VALUE[k] = ColumnValue.length;
				for (int i = 0; i < num_data; i++) {
					for (int m = 0; m < ColumnValue.length; m++)
						if (column.get(i).equals(ColumnValue[m])) {
							tmp[i][k] = m;
							break;
						}
					// tmp[i][k] = column.get(i).intValue();
				}
			} else {
				NUM_VALUE[k] = 5;
				ArrayList<Integer> DiscreteColumn = DiscreteArray(column);
				for (int i = 0; i < num_data; i++) {
					tmp[i][k] = DiscreteColumn.get(i);
				}
			}
		}
		// add to DiscreteData
		for (int i = 0; i < num_data; i++) {
			ArrayList<Integer> instance = new ArrayList<Integer>();
			for (int k = 0; k < num_dimension; k++) {
				instance.add(tmp[i][k]);
			}
			DiscreteData.add(instance);
		}
		// get label information
		Integer[] UniqueLabel = Unique(Label.toArray(new Integer[] {}));
		NUM_LABEL = UniqueLabel.length;
		label_value = new int[NUM_LABEL];
		for (int i = 0; i < NUM_LABEL; i++)
			label_value[i] = UniqueLabel[i];
	}

	public void PosProcessingHeartData() {
		// Load data
		for (int i = 0; i < num_data; i++) {
			ArrayList<Double> instance = new ArrayList<Double>();
			for (int k = 0; k < num_dimension; k++) {
				instance.add(Double.parseDouble(csvMatrix[i][k]));
			}
			Data.add(instance);
			Label.add(Integer.parseInt(csvMatrix[i][num_dimension]));
		}
	}

	public void PosProcessingIrisData() {
		for (int i = 0; i < num_data; i++) {
			ArrayList<Double> instance = new ArrayList<Double>();
			for (int k = 0; k < num_dimension; k++) {
				instance.add(Double.parseDouble(csvMatrix[i][k]));
			}
			Data.add(instance);
			Label.add(Label4Iris(csvMatrix[i][num_dimension]));
		}
	}

	public int Label4Iris(String label) {
		if (label.equals("Iris-setosa"))
			return 0;
		else if (label.equals("Iris-versicolor"))
			return 1;
		else if (label.equals("Iris-virginica"))
			return 2;
		else
			return -1;
	}

	public void PosProcessingWineData() {
		for (int i = 0; i < num_data; i++) {
			ArrayList<Double> instance = new ArrayList<Double>();
			for (int k = 1; k <= num_dimension; k++) {
				instance.add(Double.parseDouble(csvMatrix[i][k]));
			}
			Data.add(instance);
			Label.add(Integer.parseInt(csvMatrix[i][0]));
		}
	}
	// Create a permutation array that is unique and in the range between min and max
	static ArrayList<Integer> PermutationArray(int num, int min, int max) {

		Random random = new Random();
		ArrayList<Integer> arr = new ArrayList<Integer>();

		while (arr.size() < num) {
			int randomNumber = random.nextInt(max - min) + min;
			if (!arr.contains(randomNumber)) {
				arr.add(randomNumber);
			}
		}
		return arr;
	}
	// Cross Validation for Decision Tree
	public double CrossValidation(ArrayList<ArrayList<Integer>> data,
			ArrayList<Integer> label) {

		int num_data = data.size();

		int num_train = (int) (num_data * 0.9); // 90% data for train
		int num_test = num_data - num_train;
		double[] accuracy = new double[10];
		ArrayList<ArrayList<Integer>> train_data = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> test_data = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> train_label = new ArrayList<Integer>();
		ArrayList<Integer> test_label = new ArrayList<Integer>();
		ArrayList<Integer> predict_label = new ArrayList<Integer>();

		for (int i = 0; i < 10; i++) {
			train_data.clear();
			test_data.clear();
			train_label.clear();
			test_label.clear();
			predict_label.clear();
			// create permutation
			ArrayList<Integer> per = PermutationArray(num_data, 0, num_data);
			// Get train_data and label
			for (int k = 0; k < num_train; k++) {
				train_data.add(data.get(per.get(k)));
				train_label.add(label.get(per.get(k)));
			}
			// Get test data and label
			for (int k = num_train; k < num_data; k++) {
				test_data.add(data.get(per.get(k)));
				test_label.add(label.get(per.get(k)));
			}
			// Train
			
			Tree t = new Tree();
			Node root = new Node();
			root.data = DiscreteData;
			root.data_label = Label;
			// Reset attribute marked array 
			for ( int iat = 0 ; iat < num_dimension ; iat++) {
				AtrributeUsed[iat] = false;
			}
			
			t.buildTree(DiscreteData, Label, root);
			
			// Test
			for (int m = 0; m < test_data.size(); m++) { 
				int pLabel = t.TreePredict(test_data.get(m), root);
				predict_label.add(pLabel);
			}
			// Compare to get accuracy
			int sum = 0;
			for (int ia = 0; ia < num_test; ia++) {
				if (predict_label.get(ia) == (test_label.get(ia)))
					sum += 1;
			}
			accuracy[i] = (double) sum / (num_test * 1.0);
		}
		double acc = 0.0;
		for (int i = 0; i < 10; i++) {
			acc += accuracy[i] / 10;
		}
		return acc;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		// Select Data
		System.out.print("Data file name: ");
		String filename = scan.next();
		System.out.println("Data type: 1.heartDisease.csv , 2.iris.csv , 3.wine.csv: ");
		int data_type = scan.nextInt();
		RunOnData R = new RunOnData();
		R.ReadFile(filename);
		if (data_type == 1)
			R.PosProcessingHeartData();
		else if (data_type == 2)
			R.PosProcessingIrisData();
		else if (data_type == 3)
			R.PosProcessingWineData();
		// Discrete data to a range from 0 to n.
		R.DiscreteDataSet();
		double acc = R.CrossValidation(R.DiscreteData, R.Label);
		System.out.println("Accuracy: "+acc);	
	}

}
