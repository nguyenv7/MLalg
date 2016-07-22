import java.util.*;

/**
 * Document clustering
 * 
 * 
 */
class Doc {
	int docId;
	double tw;

	public Doc(int did, double weight) {
		docId = did;
		tw = weight;
	}

	public String toString() {
		String docIdString = docId + ":" + tw;
		return docIdString;
	}
}
public class Clustering {
	// vector represent of documents
	int numCenter;	
	ArrayList<double[]> centroidsVector = new ArrayList<double[]>();
	ArrayList<ArrayList<Integer>> centroidsMember = new ArrayList<ArrayList<Integer>>(); 
	ArrayList<double[]> vecDocs;
	boolean isHand = true; // if set centroids by hand
	/**
	 * Constructor for attribute initialization
	 * 
	 * @param numC
	 *            number of clusters
	 */
	public Clustering(int numC) {
		numCenter = numC;
	}

	/**
	 * Load the documents to build the vector representations
	 * 
	 * @param docs
	 */
	public void preprocess(String[] docs) {
		String[] myDocs;
		ArrayList<String> termList;
		ArrayList<ArrayList<Doc>> docLists;
		double[] docLength;
		
		myDocs = docs;
		termList = new ArrayList<String>();
		docLists = new ArrayList<ArrayList<Doc>>();
		ArrayList<Doc> docList;
		// parse the documents to construct the index and collect the raw
		// frequencies.
		for (int i = 0; i < myDocs.length; i++) {
			String[] tokens = myDocs[i].split(" ");
			String token;
			for (int j = 0; j < tokens.length; j++) {
				token = tokens[j];
				if (!termList.contains(token)) {
					termList.add(token);
					docList = new ArrayList<Doc>();
					Doc doc = new Doc(i, 1); // initial raw frequency is 1
					docList.add(doc);
					docLists.add(docList);
				} else {
					int index = termList.indexOf(token);
					docList = docLists.get(index);
					int k = 0;
					boolean match = false;

					// search the postings list for a document id, if match,
					// insert a new position number to the document id
					for (Doc doc : docList) {
						if (doc.docId == i) {
							doc.tw++; // increase word count
							match = true;
							break;
						}
						k++;
					}
					// if no match, add a new document id along with the
					// position number
					if (!match) {
						Doc doc = new Doc(i, 1);
						docList.add(doc);
					}
				}
			}//
		}// end with parsing

		// LBE07: compute the tf-idf term weights and the doc lengths
		int N = myDocs.length;
		docLength = new double[N];
		for (int i = 0; i < termList.size(); i++) {
			docList = docLists.get(i);
			int df = docList.size();
			Doc doc;
			for (int j = 0; j < docList.size(); j++) {
				doc = docList.get(j);
				double tfidf = (1 + Math.log(doc.tw))
						* Math.log(N / (df * 1.0));
				docLength[doc.docId] += Math.pow(tfidf, 2);
				doc.tw = tfidf;
				docList.set(j, doc);
			}
		}
		// update the length
		for (int i = 0; i < N; i++) {
			docLength[i] = Math.sqrt(docLength[i]);
		}
	
		int numDoc = docs.length;
		int vecLength = termList.size();
		vecDocs = new ArrayList<double[]>();
		
		// build vector tf-idf for each document
		double[][] vector = new double[numDoc][vecLength];

		for (int i = 0; i < docLists.size(); i++) {
			docList = new ArrayList<Doc>();
			docList = docLists.get(i);
			for (int j = 0; j < docList.size(); j++) {
				int id = docList.get(j).docId;
				double tw = docList.get(j).tw;
				vector[id][i] = tw;
			}
		}

		for (int i = 0; i < numDoc; i++) {			
			vecDocs.add(vector[i]);
		}
		
	}

	/**
	 * Cluster the documents For kmeans clustering, use the first and the ninth
	 * documents as the initial centroids
	 */
	public void cluster() {
		// Init first centroids member
		if ( !isHand){
			Random rnd = new Random();
			for ( int i = 0 ; i < numCenter ; i++){
				int rndC = rnd.nextInt(vecDocs.size());				
				centroidsVector.add(vecDocs.get(rndC));				
			}		
		}
		// by Hand
		numCenter = 2;
		centroidsVector.add(vecDocs.get(1));
		centroidsVector.add(vecDocs.get(8));

		// parameter
		int numLoop = 100;
		double epsilon = 0.1;
		int loop = 0;
		
		for( int i = 0 ; i < numCenter ; i++){
			ArrayList<Integer> tmp = new ArrayList<Integer>();
			centroidsMember.add(tmp);
		}
		double lastSumDistance = 0;
		while(loop < numLoop){
			System.out.println("Loop: " + loop + ".............");
			for( int i = 0 ; i < numCenter ; i++){		
				ArrayList<Integer> tmp  = centroidsMember.get(i);
				tmp.clear();
			}
			
			//Reassign clusters
			for(int i = 0 ; i < vecDocs.size() ; i++){
				double[] currentDoc = vecDocs.get(i);
				int bestCenter = -1;
				double bestDistance = -1;
				for(int k = 0 ; k < numCenter; k++){
					double dis = cosineSimilarity(currentDoc, centroidsVector.get(k));
					System.out.println("Point " + i + " to center " + k + " : " + dis);
					if ( dis >= bestDistance){
						bestDistance = dis;
						bestCenter = k;						
					}
				}
				if(bestDistance == 0 )
					continue;
				ArrayList<Integer> list = centroidsMember.get(bestCenter);
				list.add(i);
			}
			//Compute centroids
			for(int i = 0 ; i < numCenter; i++){
				ArrayList<Integer> list = centroidsMember.get(i);
				ArrayList<double[]> listVector = new ArrayList<double[]>();
				
				for(int k = 0 ; k < list.size() ; k++){
					listVector.add(vecDocs.get(list.get(k)));
				}
				
				centroidsVector.set(i, meanVector(listVector));
			}
			//Check stop condition
			double sumDistance = 0;
			for(int i = 0 ; i < numCenter; i++){
				ArrayList<Integer> list = centroidsMember.get(i);						
				for(int k = 0 ; k < list.size() ; k++){
					double dis = cosineSimilarity(centroidsVector.get(i),vecDocs.get(list.get(k)));
					sumDistance += dis;
				}				
			}
			// print all cluster
			for(int i = 0 ; i < numCenter; i++){
				String s = new String();
				s += "Cluster " + i + ":";
				ArrayList<Integer> list = centroidsMember.get(i);						
				for(int k = 0 ; k < list.size() ; k++){
					s += " " + list.get(k); 
				}
				System.out.println(s);
			}
			
			if (Math.abs(sumDistance - lastSumDistance) < epsilon)
				break;
			lastSumDistance = sumDistance;
			loop++;
			
			
		}
		
		
		System.out.println("Finish!");
		
	}
	double[] meanVector(ArrayList<double[]> list){
		double[] mean = new double[list.get(0).length];
		for( int i = 0 ; i < list.size();i++){
			for( int j = 0 ; j < mean.length;j++){
				mean[j] += list.get(i)[j]/list.size();
			}
		}
		return mean;
	}	
	
	double Magnitude(double[] a){
		double mag = 0;
		for(int i = 0 ; i < a.length ; i++){
			mag += Math.pow(a[i], 2);
		}
		return Math.sqrt(mag);
	}
	double dotProduct(double[] a, double[] b){
		double sum = 0;
		for(int i = 0 ; i < a.length ; i++){
			sum += a[i] * b[i];
		}
		return sum;
	}
	double cosineSimilarity(double[] a, double[] b){
		double MagA = Magnitude(a);
		double MagB = Magnitude(b);
		double denominator = MagA * MagB;
		if (denominator == 0)
			return 0;
		else
			return (dotProduct(a, b) / denominator); 
	}
	public static void main(String[] args) {
		String[] docs = { "hot chocolate cocoa beans", "cocoa ghana africa",
				"beans harvest ghana", "cocoa butter", "butter truffles",
				"sweet chocolate can", "brazil sweet sugar can",
				"suger can brazil", "sweet cake icing", "cake black forest" };
		Clustering c = new Clustering(2);

		c.preprocess(docs);

		c.cluster();
		/*
		 * Expected result: Cluster: 0 0 1 2 3 4 Cluster: 1 5 6 7 8 9
		 */
	}
}

