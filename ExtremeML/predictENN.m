function [ labelMatrix ] = predictENN( testFeatures,model )
%PREDICTENN prediction function of Extreme Machine Learning
%  

numTest = size(testFeatures,1);
H = sigmoid(testFeatures*model.IN + repmat(model.B,numTest,1));
labelMatrix = H * model.W;

end

