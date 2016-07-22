import java.io.*;
import java.util.*;

public class DecisionT {
		static boolean[] AtrributeUsed; // marked array for examinate what dimension is used in tree
		
		String[][] csvMatrix;  // raw data
		int num_data;		// number of row data
		ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> label = new ArrayList<Integer>();
		
		static int num_dimension;  // number dimension/ feature of database
		static int[] NUM_VALUE; // number distinct value in one dimension
		
		static int NUM_LABEL; // number label
		static int[] label_value; // label's values
		//int[][] train_data;
		//int[] train_label;
		
	public DecisionT() {
		num_dimension = 0;
		NUM_VALUE = null;
		
		NUM_LABEL = 0;
		label_value = null;
	}	
	
	static public boolean CheckAllUsed(){
		for ( int i = 0 ; i < num_dimension ; i++) {
			if(DecisionT.AtrributeUsed[i] == false) 
				return false;
		} 
		return true;
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
		num_data = rows.size();
		num_dimension = rows.getFirst().length-1;
		NUM_VALUE = new int[num_dimension];
		// Covert to integer, divide to train and label array.
		//train_data = new int[num_train][num_dimension];
		//train_label = new int[num_train];
		
/*		for ( int i = 0 ; i < num_data ; i++ ) {
			ArrayList<Integer> record = new ArrayList<Integer>();
			for ( int j = 0 ; j < num_dimension ; j++) {
				//train_data[i][j] = Integer.parseInt(csvMatrix[i][j]);
				record.add(Integer.parseInt(csvMatrix[i][j]));
			}
			data.add(record);
			label.add(Integer.parseInt(csvMatrix[i][num_dimension]));
		}*/
		
		AtrributeUsed = new boolean[num_dimension];
		for ( int i = 0 ; i < num_dimension ; i++) {
			AtrributeUsed[i] = false;
		}

	} 
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DecisionT train  = new DecisionT();
		train.ReadFile(args[0]);
		Tree t = new Tree();
		Node root = new Node();
		root.data = train.data;
		root.data_label = train.label;
		t.buildTree(train.data, train.label, root);
		
		DecisionT test = new DecisionT();
		test.ReadFile(args[1]);
		
		String outputfile = "result.txt";
		try {
			PrintWriter out = new PrintWriter(new FileWriter(outputfile));
			for ( int i = 0 ; i < test.num_data ; i++ ) {
				System.out.println(i);
				int label = t.TreePredict(test.data.get(i), root);
				out.println(label);
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	*/
}
