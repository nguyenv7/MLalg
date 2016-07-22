function avgAcc = crossValidation(dSet)
%% 10 fold cross validation accuracy
% remember parpool('local',4) before running
%%
data = load('wineData.mat');
% case using full features
%dSet = 1:11;

% accuracy array
accArray = zeros(10,1);
parfor i = 1:10
    idTrain = (data.idxC ~= i);
    idValid = (data.idxC == i);
    model = trainLogisticRegression( data.trainDataFeatures(idTrain,:), data.trainDataType(idTrain), dSet );
    validLabel = predictLogisticRegression(data.trainDataFeatures(idValid,:),model,dSet);
    accArray(i) = sum(validLabel==data.trainDataType(idValid))/sum(idValid);
    %accArray(i) = sum(validLabel==trainDataType)/length(trainDataType);
end

% generalization error

avgAcc = mean(accArray);
end