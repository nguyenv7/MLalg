# [MATLAB] K2 algorithm in learning Bayesian network

DemoK2.m: demo the structure of Bayesian Network by picture.  Noted that: you should specific by hand the order of nodes.
Run by command: DemoK2('forestFireData.m').
getInfo.m : get some basic information of the given data
myk2.m: main function of k2 algorithm
CalcScore.m: return the probability score of each tried parrent node and used for K2 algorithm
buildNodeProb.m: build the condition probability table of each node with these parents for whole Bayesian NetWork.
CalcProb.m: calculation the condition probability of each node with these parents.
PredictData.m: predict label for whole dataset with probabity table of each node.
PredictSample.m: predict label for one sample.
TenfoldValidation.m: run 10 times cross validation with 90% sample for training and 10% sample for testing. 
