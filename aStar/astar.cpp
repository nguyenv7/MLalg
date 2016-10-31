#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

#include "math.h"
using namespace std;
#define mSIZE 12
class Node
{
public:
	double fCost;
	//vector<int> path;
	double gCost;

	int x;
	int y;
	Node* prv;
	Node() {fCost = 0; gCost = 0; x=0;y=0;prv=NULL;}
	Node(double f, double g, int xi, int yi, Node* p) : fCost(f), gCost(g), x(xi), y(yi) , prv(p) {}
};
// Adapt an implementation for priority queue in C++ that can be iteratived through that
// http://www.linuxtopia.org/online_books/programming_books/c++_practical_programming/c++_practical_programming_189.html
template<class T, class Compare> class PQV {
	vector<T> v;
	Compare comp;
public:
	// Don't need to call make_heap(); it's empty:
	PQV(Compare cmp = Compare()) : comp(cmp) {}
	void push(const T& x) {
		v.push_back(x); // Put it at the end
		// Re-adjust the heap:
		push_heap(v.begin(), v.end(), comp);
	}
	void pop() {
		// Move the top element to the last position:
		pop_heap(v.begin(), v.end(), comp);
		v.pop_back(); // Remove that element
	}
	const T& top() { return v.front(); }
	bool empty() const { return v.empty(); }
	int size() const { return v.size(); }
	typedef vector<T> TVec;
	TVec getVector() {
		TVec r(v.begin(), v.end());
		// It s already a heap
		sort_heap(r.begin(), r.end(), comp);
		// Put it into priority-queue order:
		reverse(r.begin(), r.end());
		return r;
	}
};

struct CompareNode : public std::binary_function<Node*, Node*, bool>
{
	bool operator()( Node* lhs,  Node* rhs) const
	{
		return lhs->fCost > rhs->fCost;
	}
};
Node* findNode(Node* n, vector<Node*> nodeArray){
	for(unsigned int i = 0; i < nodeArray.size(); i++){
		if(nodeArray.at(i)->x == n->x && nodeArray.at(i)->y == n->y)
			return nodeArray.at(i);
	}
	return NULL;
}
void printPath(Node* nIn){
	vector<Node*> path;
	Node* n = nIn;
	cout << "finding path!" << endl;
	while(n != NULL){
		path.push_back(n);
		n = n->prv;
	}

	Node* first = path.at(path.size()-1);
	for(int i = path.size()-2; i >= 0 ;i--){
		Node* next = path.at(i);
		if(first->x == next->x){
			if(first->y < next->y)
				cout << "S";
			else
				cout << "N";
		}
		else if (first->y == next->y){
			if(first->x < next->x)
				cout << "E";
			else
				cout << "W";
		}
		first = next;
	}
	cout << endl;
}
int main()
{

	int maze[mSIZE][mSIZE];
	// read data to matrix maze
	string line;
	ifstream myfile ("data.txt");
	int numLine = 0;
	if (myfile.is_open())
	{
		while ( getline (myfile,line) )
		{
			//cout << line << '\n';
			for(int k=0; k < mSIZE ;k++){
				if(line.at(k)=='0')
					maze[numLine][k] = 0;
				else
					maze[numLine][k] = 1;
			}
			numLine++;
		}
		myfile.close();
	}

	else cout << "Unable to open file";

	// calculate h the heuristic cost by Euclid distance
	double hCost[mSIZE][mSIZE];
	// isVisited
	int isVisited[mSIZE][mSIZE];

	// target node
	int targetX = mSIZE-1;
	int targetY = mSIZE-1;

	for(int i = 0; i < mSIZE ; i++){
		for(int j = 0; j < mSIZE ; j++){
			hCost[i][j] = sqrt((i-targetY)*(i-targetY)+(j-targetX)*(j-targetX));
			isVisited[i][j] = 0;
		}
	}

	// start node
	int startX = 0;
	int startY = 0;
	isVisited[startY][startX] = 1;

	Node* startNode = new Node();
	startNode->x = startX;
	startNode->y = startY;
	startNode->gCost = 0;
	startNode->fCost = startNode->gCost + hCost[startNode->y][startNode->x];
	startNode->prv = NULL;

	/// A Star alg
	///////////////////////////////// CASE 1 ////////////////////////////////
	PQV<Node*,CompareNode> OpenList;
	OpenList.push(startNode);
	int isFind = 0;
	int numNodeExpandCase1 = 0;
	while(!OpenList.empty()){

		//// pick lowerest score
		Node* current = OpenList.top();
		numNodeExpandCase1++;
		//cout << current->x << " " << current->y << " " << current->fCost << endl;

		if((current->x == targetX) && (current->y == targetY) ){ // test stop
			isFind = 1;
			printPath(current);
			break;
		}
		OpenList.pop();

		//// create neighbor list
		vector<Node*> neighborList;
		// jump N x,y-1
		if((current->y-1) >= 0)
			if(maze[(current->y-1)][(current->x)]==0){
				Node* goN = new Node();
				goN->prv = current;
				goN->x = current->x;
				goN->y = current->y-1;
				goN->gCost = current->gCost+1;
				goN->fCost = goN->gCost + hCost[goN->y][goN->x];
				neighborList.push_back(goN);
			}
		// jump W x-1,y
		if((current->x-1) >= 0)
			if(maze[(current->y)][(current->x-1)]==0){
				Node* goW= new Node();
				goW->prv = current;
				goW->x = current->x-1;
				goW->y = current->y;
				goW->gCost = current->gCost+1;
				goW->fCost = goW->gCost + hCost[goW->y][goW->x];
				neighborList.push_back(goW);

			}
		// jump E x+1,y
		if((current->x+1) < mSIZE)
			if(maze[(current->y)][(current->x+1)]==0){
				Node* goE= new Node();
				goE->prv = current;
				goE->x = current->x+1;
				goE->y = current->y;
				goE->gCost = current->gCost+1;
				goE->fCost = goE->gCost + hCost[goE->y][goE->x];
				neighborList.push_back(goE);

			}
		// jump S x,y+1
		if((current->y+1) < mSIZE)
			if(maze[(current->y+1)][(current->x)]==0){
				Node* goS= new Node();
				goS->prv = current;
				goS->x = current->x;
				goS->y = current->y+1;
				goS->gCost = current->gCost+1;
				goS->fCost = goS->gCost + hCost[goS->y][goS->x];
				neighborList.push_back(goS);
			}

		//// process the neighbor list
		vector<Node*> vOpenList = OpenList.getVector();
		for(unsigned int i = 0; i < neighborList.size() ; i++){
			Node* nInOpenList = findNode(neighborList.at(i),vOpenList);
			if(nInOpenList == NULL)//new Node
				OpenList.push(neighborList.at(i));
			// exist Node
			else if (neighborList.at(i)->gCost >=  nInOpenList->gCost)
				continue;
			else //better path
				OpenList.push(neighborList.at(i));
		}
	}
	if(isFind == 0)
		cout << "Fail to find the path" << endl;

	/////////////////////////////////////// CASE 2 ///////////////////////////////////////
	// with using isVisited to check a node is visited or not
	cout << endl;
	cout << "Case 2" << endl;
	//priority_queue<Node*,vector<Node*>, CompareNode > OpenList;
	PQV<Node*,CompareNode> OpenList2;
	OpenList2.push(startNode);
	isFind = 0;
	int numNodeExpandCase2 = 0;
	while(!OpenList2.empty()){

		//// pick lowerest score
		Node* current = OpenList2.top();
		numNodeExpandCase2++;
		//cout << current->x << " " << current->y << " " << current->fCost << endl;

		if((current->x == targetX) && (current->y == targetY) ){ // test stop
			isFind = 1;
			printPath(current);
			break;
		}

		isVisited[current->y][current->x] = 1;
		OpenList2.pop();

		//// create neighbor list
		vector<Node*> neighborList;
		// jump N x,y-1
		if((current->y-1) >= 0)
			if(maze[(current->y-1)][(current->x)]==0 && isVisited[current->y-1][current->x] == 0){
				Node* goN = new Node();
				goN->prv = current;
				goN->x = current->x;
				goN->y = current->y-1;
				goN->gCost = current->gCost+1;
				goN->fCost = goN->gCost + hCost[goN->y][goN->x];
				neighborList.push_back(goN);
			}
		// jump W x-1,y
		if((current->x-1) >= 0)
			if(maze[(current->y)][(current->x-1)]==0 && isVisited[current->y][current->x-1] == 0){
				Node* goW= new Node();
				goW->prv = current;
				goW->x = current->x-1;
				goW->y = current->y;
				goW->gCost = current->gCost+1;
				goW->fCost = goW->gCost + hCost[goW->y][goW->x];
				neighborList.push_back(goW);

			}
		// jump E x+1,y
		if((current->x+1) < mSIZE)
			if(maze[(current->y)][(current->x+1)]==0 && isVisited[current->y][current->x+1] == 0){
				Node* goE= new Node();
				goE->prv = current;
				goE->x = current->x+1;
				goE->y = current->y;
				goE->gCost = current->gCost+1;
				goE->fCost = goE->gCost + hCost[goE->y][goE->x];
				neighborList.push_back(goE);

			}
		// jump S x,y+1
		if((current->y+1) < mSIZE)
			if(maze[(current->y+1)][(current->x)]==0 && isVisited[current->y+1][current->x] == 0){
				Node* goS= new Node();
				goS->prv = current;
				goS->x = current->x;
				goS->y = current->y+1;
				goS->gCost = current->gCost+1;
				goS->fCost = goS->gCost + hCost[goS->y][goS->x];
				neighborList.push_back(goS);
			}

		//// process the neighbor list
		vector<Node*> vOpenList = OpenList2.getVector();
		for(unsigned int i = 0; i < neighborList.size() ; i++){
			Node* nInOpenList = findNode(neighborList.at(i),vOpenList);
			if(nInOpenList == NULL)//new Node
				OpenList2.push(neighborList.at(i));
			// exist Node
			else if (neighborList.at(i)->gCost >=  nInOpenList->gCost)
				continue;
			//else //better path
				//OpenList2.push(neighborList.at(i));
		}
	}
	if(isFind == 0)
		cout << "Fail to find the path" << endl;

	cout << "Number of node that expanded in Case 1: " << numNodeExpandCase1 << endl;
	cout << "Number of node that expanded in Case 2: " << numNodeExpandCase2 << endl;
	return 1;
}
