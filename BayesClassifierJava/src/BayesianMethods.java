import java.io.*;
import java.util.*;

import Jama.*;

public abstract class BayesianMethods {
	
	String[][] csvMatrix; // raw data 

	
	int num_data; // number of row data
	int num_dimension;  // number of dimension
	
	ArrayList<ArrayList<Double>> Data; // dataset
	ArrayList<String> Label;  // label for dataset
	
	public BayesianMethods() {
		Data = new ArrayList<ArrayList<Double>>();
		Label = new ArrayList<String>();
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
	}
	// Return an double array that all values in it are unique 
	static public Double[] Unique(Double[] array){
		Set<Double> set = new LinkedHashSet<Double>(Arrays.asList(array));
		Double[] uniqueArray = set.toArray(new Double[]{});
		return uniqueArray;		
	}
	//Return an String array that all values in it are unique
	static public String[] Unique(String[] array){
		Set<String> set = new LinkedHashSet<String>(Arrays.asList(array));
		String[] uniqueArray = set.toArray(new String[]{});
		return uniqueArray;		
	}
	// Unique for label-set
	public String[] UniqueLabel(ArrayList<String> labelList){
		String[] tmp = labelList.toArray(new String[]{});
		String[] UniqueLabel = Unique(tmp);
		return UniqueLabel;
	}
	// Get all element in labelList that is corresponding with label's values in UniqueLabel
	// Return list of element for each label
	public ArrayList<ArrayList<Integer>> GetElementbyLabel(ArrayList<String> labelList, String[] UniqueLabel){
		
		ArrayList<ArrayList<Integer>> listByLabel = new ArrayList<ArrayList<Integer>>();
		
		for ( int i = 0 ; i < UniqueLabel.length ; i++) {
			ArrayList<Integer> listI = new ArrayList<Integer>();
			for ( int k  = 0 ; k < labelList.size(); k++) {
				 if (labelList.get(k).equals(UniqueLabel[i])) {
					 listI.add(k);
				 }
			}
			listByLabel.add(listI);
		}
		return listByLabel;	
	}
	// Return matrix n-1 that store mean value of all dimension in Matrix Data 
	public Matrix MeanMatrix(ArrayList<ArrayList<Double>> MatrixData) {
		int dim = MatrixData.get(0).size();
		double[][] meanArray = new double[dim][1];
		
		for ( int i = 0 ; i < dim ; i++){
			double sum = 0;
			for ( int k = 0 ; k < MatrixData.size() ; k++) {
				sum += MatrixData.get(k).get(i);
			}
			meanArray[i][0] = sum / MatrixData.size();
		}
		
		return new Matrix(meanArray);
	}
	//Return matrix n-n that store covariance value of MatrixData, Diag mean return a diagonal matrix for NavieBayes 
	public Matrix DiagCovMatrix(ArrayList<ArrayList<Double>> MatrixData, Matrix meanArray ) {
		int dim = MatrixData.get(0).size();
		int numdata = MatrixData.size();
		
		double[][] normalizeData = new double[numdata][dim];
		
		for ( int i = 0 ; i < dim ; i++){		
			for ( int k = 0 ; k < MatrixData.size() ; k++) {
				normalizeData[k][i] = MatrixData.get(k).get(i) - meanArray.get(i, 0);
			}			
		}
		Matrix normalizeMatrix = new Matrix(normalizeData);
		Matrix tmp = normalizeMatrix.transpose();
		Matrix cov = tmp.times(normalizeMatrix);
		cov = cov.times(1.0/(numdata-1));
		
		Matrix diagCov = new Matrix(dim, dim);
		for (int i = 0 ; i < dim ; i++ ) {
			diagCov.set(i, i, cov.get(i, i));
		}
		
		return diagCov;
	}
	//Return matrix n-n that store covariance value of MatrixData, Full mean return a full value matrix for OptimalBayes
	public Matrix FullCovMatrix(ArrayList<ArrayList<Double>> MatrixData, Matrix meanArray ) {
		int dim = MatrixData.get(0).size();
		int numdata = MatrixData.size();
		
		double[][] normalizeData = new double[numdata][dim];
		
		for ( int i = 0 ; i < dim ; i++){		
			for ( int k = 0 ; k < MatrixData.size() ; k++) {
				normalizeData[k][i] = MatrixData.get(k).get(i) - meanArray.get(i, 0);
			}			
		}
		Matrix normalizeMatrix = new Matrix(normalizeData);
		Matrix tmp = normalizeMatrix.transpose();	
		Matrix cov = null;
		try
		{
		cov = tmp.times(normalizeMatrix);}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
		cov = cov.times(1.0/(numdata-1));
		
		return cov;
	}
	// Calc Mahalanobis distance between a data point and a distribution that has cov and mean value.
	public double MahalanobisDistance(ArrayList<Double> data, Matrix cov, Matrix mean){
		int dim = data.size();
		double[][] normalizeData = new double[dim][1];
		
		for ( int i = 0 ; i < dim ; i++){
			normalizeData[i][0] = data.get(i) - mean.get(i, 0);
		}
		
		Matrix deltaX = new Matrix(normalizeData);
		Matrix tmp = deltaX.transpose();
		if (cov.det() == 0)
			return Double.MAX_VALUE;
		Matrix inv = cov.inverse();
		Matrix score = tmp.times(inv).times(deltaX);
		
		return score.get(0, 0);
	}
	//Calc Mean distance between a data point and a distribution that has only mean value.
	public double MeanDistance(ArrayList<Double> data, Matrix mean){
		int dim = data.size();
		double[][] normalizeData = new double[dim][1];
		
		for ( int i = 0 ; i < dim ; i++){
			normalizeData[i][0] = data.get(i) - mean.get(i, 0);
		}
		
		Matrix deltaX = new Matrix(normalizeData);		
		double score = deltaX.norm2();
		return score;
	}
	// like function: http://www.mathworks.com/help/matlab/ref/perms.html
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
	// Because all dataset is not same structure so we need pos-processing to read each data set
	public void PosProcessingHeartData(){
		for ( int i = 0 ; i < num_data; i++){
			ArrayList<Double> instance = new ArrayList<Double>();
			for ( int k = 0 ; k < num_dimension; k++){
				instance.add(Double.parseDouble(csvMatrix[i][k]));
			}
			Data.add(instance);
			Label.add(csvMatrix[i][num_dimension]);
		}
	}
	
	public void PosProcessingIrisData(){
		for ( int i = 0 ; i < num_data; i++){
			ArrayList<Double> instance = new ArrayList<Double>();
			for ( int k = 0 ; k < num_dimension; k++){
				instance.add(Double.parseDouble(csvMatrix[i][k]));
			}
			Data.add(instance);
			Label.add(csvMatrix[i][num_dimension]);
		}
	}
	
	
	public void PosProcessingWineData(){
		for ( int i = 0 ; i < num_data; i++){
			ArrayList<Double> instance = new ArrayList<Double>();
			for ( int k = 1 ; k <= num_dimension; k++){
				instance.add(Double.parseDouble(csvMatrix[i][k]));
			}
			Data.add(instance);
			Label.add(csvMatrix[i][0]);
		}
	}
	public abstract void Training(ArrayList<ArrayList<Double>> TrainData,
			ArrayList<String> TrainLabel);
	public abstract String Testing(ArrayList<Double> TestData);
	public abstract double CrossValidation(	ArrayList<ArrayList<Double>> data, ArrayList<String> label);
	public abstract void Test(ArrayList<ArrayList<Double>> data, ArrayList<String> label);
	/**
	 * @param args
	 */
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}	*/
}