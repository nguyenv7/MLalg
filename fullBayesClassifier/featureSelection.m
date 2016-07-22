function [featureList ]= featureSelection()
%% Forward feature selection for NaiveBayes classifer
%%
clear;
featuresName = {'fixedAcidity','volatileAcidity','citricAcid','residualSugar',...
    'chlorides','freeSulfurDioxide','totalSulfurDioxide','density','pH','sulphates','alcohol'};

featureF = [1:length(featuresName)];
featureS = [];

accArray = [];
while(true)
    if(isempty(featureF))
        break;
    end
    
    maxAcc = -1;
    maxId = -1;
    
    for iF = 1:length(featureF)
        tmpS = [featureS , featureF(iF)];
        
        tmpAcc = crossValidation(tmpS);
        
        if tmpAcc > maxAcc
            maxAcc = tmpAcc;
            maxId = iF;
        end
        
    end
    
    %fprintf('Add more d %d, acc: %.3f \n',featureF(maxId),maxAcc);
    accArray = [accArray, maxAcc];
    featureS = [featureS featureF(maxId)];
    featureS
    fprintf('\t%.3f\n',maxAcc);
    %featureS
    featureF(maxId) = [];
    
end

%for i = 1:length(featureS)
%    fprintf('%s ',featuresName{featureS(i)});
%end

[~,idMax] = max(accArray);
featureList = featureS(1:idMax);
end