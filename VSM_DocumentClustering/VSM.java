import java.util.*;

/**
 * 
 * Document id class that contains the document id and the term
 *         weight in tf-idf
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

public class VSM {
	String[] myDocs;
	ArrayList<String> termList;
	ArrayList<ArrayList<Doc>> docLists;
	double[] docLength;

	/**
	 * Construct an inverted index
	 * 
	 * @param docs
	 *            List of input strings or file names
	 * 
	 */
	public VSM(String[] docs) {
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

		// compute the tf-idf term weights and the doc lengths
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

	}

	/**
	 * perform rankSearch
	 * 
	 * @param query
	 *            user query in free form text
	 */
	public void rankSearch(String[] query) {
		HashMap<Integer, Double> docs = new HashMap<Integer, Double>();
		ArrayList<Doc> docList;
		for (String term : query) {
			int index = termList.indexOf(term);
			if (index < 0)
				continue;
			docList = docLists.get(index);
			double w_t = Math.log(myDocs.length * 1.0 / docList.size());
			Doc doc;
			for (int j = 0; j < docList.size(); j++) {
				doc = docList.get(j);
				double score = w_t * doc.tw;
				if (!docs.containsKey(doc.docId)) {
					docs.put(doc.docId, score);
				} else {
					score += docs.get(doc.docId);
					docs.put(doc.docId, score);
				}
			}
		}
		System.out.println(docs);
	}

	/**
	 * Return the string representation of a positional index
	 */
	public String toString() {
		String matrixString = new String();
		ArrayList<Doc> docList;
		for (int i = 0; i < termList.size(); i++) {
			matrixString += String.format("%-15s", termList.get(i));
			docList = docLists.get(i);
			for (int j = 0; j < docList.size(); j++) {
				matrixString += docList.get(j) + "\t";
			}
			matrixString += "\n";
		}
		return matrixString;
	}

	public static void main(String[] args) {
//		String[] docs = { "new home sales top forecasts",
//				"home sales rise in july", "increase in home sales in july",
//				"july new home sales rise" };
		String[] docs = { "hot chocolate cocoa beans", "cocoa ghana africa",
				"beans harvest ghana", "cocoa butter", "butter truffles",
				"sweet chocolate can", "brazil sweet sugar can",
				"suger can brazil", "sweet cake icing", "cake black forest" };

		VSM vsm = new VSM(docs);
		System.out.println(vsm);
		String[] query = { "in" }; // single term based on frequency only
		// String[] query = {"in", "top"};
		// String[] query = {"in", "july"};
		// String[] query = {"nothing", "no", "new", "home"};
		vsm.rankSearch(query);
	}

}
