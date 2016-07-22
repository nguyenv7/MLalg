function [ labels ] = predictLogisticRegression( testData, model, dSet )
%PREDICTLOGISTICREGRESSION Prediction for trainined Logistic regression
%model
m = size(testData,1);

testData= [testData(:,dSet), ones(m,1)];

p = sigmoid(testData*model);
labels = (p>=0.5);
end

