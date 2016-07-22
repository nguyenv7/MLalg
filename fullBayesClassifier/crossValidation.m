function avgAcc = crossValidation(dSet)
% 10 fold cross validation accuracy
load('wineData.mat');
% case using full features
%dSet = 1:11;

% accuracy array
accArray = zeros(10,1);
for i = 1:10
    idTrain = (idxC ~= i);
    idValid = (idxC == i);
    model = trainFullBayes( trainDataFeatures(idTrain,:), trainDataType(idTrain), dSet );
    validLabel = predictFullBayes(trainDataFeatures(idValid,:),model);
    accArray(i) = sum(validLabel==trainDataType(idValid))/sum(idValid);
end

% generalization error

avgAcc = mean(accArray);
end