function [ model ] = trainENN( dataFeatures, dataOutput, numHidden)
%TRAINENN Training Extreme Neural Network model
% Model include:  
% IN: weight matrix between input layer and a hidden layer
% B : bias array of hidden layer
% W : weight matrix between a hidden layer and the output layer 
% using sigmoid active function

numTrain = size(dataFeatures,1);
numTrainNeural = size(dataFeatures,2);
%numOutNeural = size(dataOutput,2);

model.IN = rand(numTrainNeural,numHidden)*2-1;
model.B = rand(1,numHidden)*2-1;
%W = rand(numHidden,numOutNeural)*2-1;

H = sigmoid(dataFeatures*model.IN + repmat(model.B,numTrain,1));

model.W = pinv(H' * H)*H'*dataOutput;
end

