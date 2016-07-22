# [MATLAB]Artificial neural network

The accuracy of my code after 10 times cross validation is 85.11% in the Iris data

- ANN.m : main algorithm for Arfitical Neural Network.
- Input : dataset and label.
- Output : network's structure: layer size and weight matrix.

All config about learning rate, stop condition, network architecture are in the first 15 lines.
Overall the algorithm:
- Convert the raw label to matrix label. Ex: [1 2 3] to [1 0 0, 0 1 0, 0 0 1]. In order to 
feed into network.
- Initialize some matrix to optimize the system's speed.
-  Loop until meet the stop condition: Feed foward -> Calculate the error -> Backpropagation the error: update weights

PredictANN.m: return the prediction label for given data with the learned network structure and its weight matrix.


CrossValidation.m: run 10 times the ANN algorithm to get the true accuracy of this code.
To run: CrossValidation('iris.csv');
Return the accuracy.
