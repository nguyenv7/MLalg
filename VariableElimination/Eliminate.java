import java.io.*;
import java.util.*;


public class Eliminate {
	String[][] csvMatrix;

	int num_data;
	ArrayList<ArrayList<Integer>> data;
	ArrayList<Integer> label;

	ArrayList<ArrayList<Integer>> Sset;
	ArrayList<ArrayList<Integer>> Gset;

	public Eliminate(){
		data = new ArrayList<ArrayList<Integer>>();
		label = new ArrayList<Integer>();

		Sset = new ArrayList<ArrayList<Integer>>();
		Gset = new ArrayList<ArrayList<Integer>>();
	}
	static int num_dimension;
	/**
	 * @param args
	 */
	public int ConvertToInt(int Di, String atrribute) {
		switch(Di) {
		case 0:
			if ( atrribute.equals("Rainy"))
				return 1;
			else if ( atrribute.equals("Sunny"))
				return 2;			
		case 1:
			if ( atrribute.equals("Cold"))
				return 1;
			else if ( atrribute.equals("Warm"))
				return 2;
		case 2:
			if ( atrribute.equals("Normal"))
				return 1;
			else if ( atrribute.equals("High"))
				return 2;
		case 3:
			if ( atrribute.equals("Weak"))
				return 1;
			else if ( atrribute.equals("Strong"))
				return 2;
		case 4:
			if ( atrribute.equals("Cool"))
				return 1;
			else if ( atrribute.equals("Warm"))
				return 2;
		case 5:
			if ( atrribute.equals("Same"))
				return 1;
			else if ( atrribute.equals("Change"))
				return 2;	
		case 6:
			if ( atrribute.equals("Do Not Enjoy"))
				return 0;
			else if ( atrribute.equals("Enjoy Sport"))
				return 1;
		}
		return -1;
	}

	public void ReadFile(String filename){
		// Read CSV file
		BufferedReader CSVFile = null;
		try {
			//CSVFile = new BufferedReader(new FileReader("train.csv"));
			CSVFile = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LinkedList<String[]> rows = new LinkedList<String[]>();
		String dataRow;
		try {
			dataRow = CSVFile.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Read the number of the lines in .csv file 
		// i = row of the .csv file

		try {
			int i = 0;
			while ((dataRow = CSVFile.readLine()) != null){
				i++;
				rows.addLast(dataRow.split(","));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Raw data file
		csvMatrix = rows.toArray(new String[rows.size()][]);
		//
		num_data = rows.size();
		num_dimension = rows.getFirst().length-1;
		for ( int i = 0 ; i < num_data ; i++ ) {
			ArrayList<Integer> record = new ArrayList<Integer>();
			for ( int j = 0 ; j < num_dimension ; j++) {
				//train_data[i][j] = Integer.parseInt(csvMatrix[i][j]);
				record.add(ConvertToInt(j,(csvMatrix[i][j])));
			}
			data.add(record);
			label.add(ConvertToInt(num_dimension,csvMatrix[i][num_dimension]));
		}

	}
	public ArrayList<Integer> InitS() {
		ArrayList<Integer> S = new ArrayList<Integer>();
		for ( int i = 0 ; i < num_dimension; i++)
			S.add(-1);
		return S;
	}
	public ArrayList<Integer> InitG() {
		ArrayList<Integer> G = new ArrayList<Integer>();
		for ( int i = 0 ; i < num_dimension; i++)
			G.add(0);
		return G;
	}
	public boolean ClassifyS(ArrayList<Integer> rule, ArrayList<Integer> record) {
		for (int i = 0 ; i < rule.size() ;i++) {
			if( rule.get(i) == -1)
				continue;
			if (rule.get(i) != record.get(i))
				return false;
		}
		return true;
	}
	public boolean ClassifySset(ArrayList<Integer> record) {
		for ( int i = 0 ; i < Sset.size(); i++){
			if(ClassifyS(Sset.get(i), record) == false)
				return false;
		}
		return true;
	}
	public boolean ClassifyG(ArrayList<Integer> rule, ArrayList<Integer> record) {
		for (int i = 0 ; i < rule.size() ;i++) {
			if( rule.get(i) == 0)
				continue;
			if (rule.get(i) != record.get(i))
				return false;
		}
		return true;
	}
	
	public boolean ClassifyGset(ArrayList<Integer> record) {
		for ( int i = 0 ; i < Gset.size(); i++){
			if(ClassifyG(Gset.get(i), record) == false)
				return false;
		}
		return true;
	}
	
	public int SizeG(ArrayList<Integer> rule) {		
		int sum = 0;
		for ( int i = 0 ; i < rule.size() ; i++) {
			if( rule.get(i) == 0)
				continue;
			sum = sum + 1;
		}
		return sum;
	}
	public int SizeS(ArrayList<Integer> rule) {
		int sum = 0;
		for ( int i = 0 ; i < rule.size() ; i++) {
			if( rule.get(i) == -1)
				continue;
			sum = sum + 1;
		}
		return sum;
	}
	public int MaxSizeS(ArrayList<ArrayList<Integer>> rulelist) {
		int max = -1;
		for ( int i = 0; i < rulelist.size() ; i++) {
			int size = SizeS(rulelist.get(i));
			if( size > max)
				max = size; 
		}
		return max;
	}
	public int MinSizeG(ArrayList<ArrayList<Integer>> rulelist) {
		int min = Integer.MAX_VALUE;
		for ( int i = 0; i < rulelist.size() ; i++) {
			int size = SizeG(rulelist.get(i));
			if( size < min)
				min = size; 
		}
		return min;
	}
	public boolean CheckGenerateS(ArrayList<Integer> ruleS, ArrayList<ArrayList<Integer>> Gset) {
		int sizeS = SizeS(ruleS);
		if (ruleS.size() == 0)
			return false;
		for ( int i = 0 ; i < Gset.size() ;i++) {
			if (sizeS > SizeG(Gset.get(i))) 
				return true;
		}
		return false;
	}
	public boolean CheckGenerateG(ArrayList<Integer> ruleG, ArrayList<ArrayList<Integer>> Sset) {
		int sizeG = SizeG(ruleG);
		if ( ruleG.size() == 0 )
			return false;
		for ( int i = 0 ; i < Sset.size() ;i++) {
			if (sizeG < SizeS(Sset.get(i))) 
				return true;
		}
		return false;
	}
	public int FindDiff(ArrayList<ArrayList<Integer>> data, ArrayList<Integer> label, int index, int dim, int value){
		for ( int i = 0 ; i <= index ; i++){	
			if ( label.get(i) == 1){
				if (data.get(i).get(dim) == value ) {
					return -1;
				}
				else
					return data.get(i).get(dim);
			}			
		}
		return 1;
	}
	public void CandidateEliminationTraining(ArrayList<ArrayList<Integer>> data, ArrayList<Integer> label) {
		Sset.clear();
		Gset.clear();
		if ( data.size() != label.size())
			return;
		// init
		Sset.add(InitS());
		Gset.add(InitG());
		// set positive first
		int addPos = 0;
		if (label.get(0) != 1) {
			for (int i = 1 ; i < label.size() ;i++) {
				if(label.get(i) == 1) {
					label.add(0, label.get(i));
					data.add(0, data.get(i));

					label.remove(i+1);
					data.remove(i+1);
					addPos++;
				}
				if(addPos == 2)
					break;
			}
		}
		Sset.set(0,data.get(0));		
		// loop all record
		for (int i = 1 ; i < data.size() ;i++) {
			if (label.get(i) == 1 ) { //positive samples
				// remove all G inconsistent
				for ( int iG = 0 ; iG < Gset.size() ; iG++)
					if (ClassifyG(Gset.get(iG), data.get(i)) == false){
						Gset.remove(iG);
						iG--;
					}
				// increment S 
				boolean isAdd = false;
				for ( int iS = 0 ; iS < Sset.size() ; iS++)
					if (ClassifyS(Sset.get(iS), data.get(i)) == false){
						ArrayList<Integer> tmpRule = new ArrayList<Integer>();
						
						tmpRule = Sset.get(iS); 
						Sset.remove(iS);
						iS--;
						// generate S
						for ( int k = 0 ; k < tmpRule.size() ; k++) {
							if ( tmpRule.get(k) == -1) 
								continue;
							if (tmpRule.get(k) != data.get(i).get(k)){
								tmpRule.set(k,-1);
							}
						}
						// check generate
						if ( CheckGenerateS(tmpRule, Gset)) {							
							Sset.add(tmpRule);
							isAdd = true;
						}												
					}
				// remove more general S
				if ( isAdd == true ) {
					int maxS = MaxSizeS(Sset);
					for ( int iS = 0 ; iS < Sset.size(); iS++ ){
						if ( SizeS(Sset.get(iS)) < maxS) {
							Sset.remove(iS);
							iS--;
						}						
					}						
				}
			}
			else { // negative samples
				// remove all S inconsistent
				for ( int iS = 0 ; iS < Sset.size() ; iS++)
					if (ClassifyS(Sset.get(iS), data.get(i)) == true){
						Sset.remove(iS);
						iS--;
					}
				// increment G 
				boolean isAdd = false;
				for ( int iG = 0 ; iG < Gset.size() ; iG++)
					if (ClassifyG(Gset.get(iG), data.get(i)) == true){
						ArrayList<Integer> tmpRule = new ArrayList<Integer>();					
						
						tmpRule = Gset.get(iG); 
						Gset.remove(iG);
						iG--;
						// generate G
						for ( int k = 0 ; k < tmpRule.size() ; k++) {
							if ( tmpRule.get(k) == 0){ 
								ArrayList<Integer> tmpRuleSuccess = new ArrayList<Integer>();
								
								int diff = FindDiff(data, label, i, k, data.get(i).get(k));
								if (diff != -1) {									
									tmpRuleSuccess = (ArrayList<Integer>) tmpRule.clone();
									tmpRuleSuccess.set(k,diff);
								}
								// check generate
								if ( CheckGenerateG(tmpRuleSuccess, Sset)) {							
									Gset.add(tmpRuleSuccess);
									isAdd = true;
								}
							}															
						}
																		
					}
				// remove more specific G
				if ( isAdd == true ) {
					int minG = MinSizeG(Gset);
					for ( int iG = 0 ; iG < Gset.size(); iG++ ){
						if ( SizeG(Gset.get(iG)) > minG) {
							Gset.remove(iG);
							iG--;
						}						
					}						
				}					
			}
		}

	}
	ArrayList<Integer> Predict(ArrayList<ArrayList<Integer>> data) {
		ArrayList<Integer> predict = new ArrayList<Integer>();
		for(int i = 0 ; i < data.size() ; i++) {
			if(!ClassifyGset(data.get(i))){
				predict.add(0);
				continue;
			}				
			if(!ClassifySset(data.get(i))){
				predict.add(0);
				continue;
			}
			predict.add(1);	
		}
		return predict;
	}
	static ArrayList<Integer> PermutationArray(int num, int min,int max) {

		Random random = new Random();
		ArrayList<Integer> arr = new ArrayList<Integer>();

		while (arr.size() < num) {
			int randomNumber = random.nextInt(max - min) + min;
			if ( !arr.contains(randomNumber)) {
				arr.add(randomNumber);
			}
		}
		return arr;
	}
	public double CrossValidation(){
		int num_train = (int) (num_data * 0.9);
		int num_test = num_data - num_train;
		double[] accuracy = new double[10];
		ArrayList<ArrayList<Integer>> train_data = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> test_data = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> train_label = new ArrayList<Integer>();
		ArrayList<Integer> test_label = new ArrayList<Integer>();
		ArrayList<Integer> predict_label = new ArrayList<Integer>();
		
		for ( int i = 0 ; i < 10 ; i++ ){
			train_data.clear(); test_data.clear(); train_label.clear(); test_label.clear();predict_label.clear();
			// create permutation
			ArrayList<Integer> per = new ArrayList<Integer>();
			per = PermutationArray(num_data, 0, num_data);
//			for ( int j = 0 ; j < num_data ; j++)
//				per.add(j);
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
			CandidateEliminationTraining(train_data, train_label);
			// Test
			predict_label = Predict(test_data);
			// Compare to get accuracy
			int sum = 0;
			for ( int ia = 0 ; ia < num_test ; ia++ ){
				if ( predict_label.get(ia) == test_label.get(ia)) 
					sum += 1;
			}
			accuracy[i] = (double)sum / (num_test * 1.0);
		}
		double acc = 0.0;
		for ( int i = 0 ; i < 10 ; i++) {
			acc += accuracy[i]/10;
		}
		return acc;
	}
	public static void main(String[] args) {		
		// TODO Auto-generated method stub
		Eliminate e = new Eliminate();
		e.ReadFile(args[0]);
		double acc = e.CrossValidation();
		String out = "Accuracy of the algorithm: " + Double.toString(acc);
		System.out.println(out);
	}
}
