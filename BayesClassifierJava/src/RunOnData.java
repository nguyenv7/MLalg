import java.util.ArrayList;
import java.util.Scanner;


public class RunOnData {	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BayesianMethods BObj;
		Scanner scan = new Scanner(System.in);
		// Select Data
		System.out.print("Data file name: ");		
		String filename = scan.next();
		System.out.print("Data type: 1.heartDisease.csv , 2.iris.csv , 3.wine.csv: ");
		int data_type = scan.nextInt();
		// Select Algorithm		
		System.out.print("Algorithm type: 1.OptimalBayesian; 2.NavieBayesian; 3.LinearBayesian: ");
		int algo_type = scan.nextInt();
		if (algo_type == 1)
			BObj = new OptimalBayesian();
		else if (algo_type == 2)
			BObj = new NavieBayesian();
		else 
			BObj = new LinearBayesian();
		
		// Readfile
		BObj.ReadFile(filename);
		// Pos processing data // depend on data structure
		if ( data_type == 1){
			BObj.PosProcessingHeartData();
		}
		else if ( data_type == 2){
			BObj.PosProcessingIrisData();
		}
		else if (data_type == 3){
			BObj.PosProcessingWineData();
		}
		
		// Run
//		BObj.Test(BObj.Data, BObj.Label);
		double acc = BObj.CrossValidation(BObj.Data, BObj.Label);
		System.out.println("Accuracy: " + acc);
		
	}

}
