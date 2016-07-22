%% Ensemble the prediction with various number of hidden neural.
load('wineData.mat');
numHidden = 10;
numEnsemble = 20;
%% Convert numeric labels to matrix label
numTrain = size(trainDataQual,1);
% qual values: 1 to 7
qualMatrix = zeros(numTrain,7);
for i = 1:numTrain
    iLabel = trainDataQual(i);
    qualMatrix(i,iLabel) = 1;
end
%% Ensemble result matrix
ensLabels = zeros(size(testData,1),numEnsemble);
for i = 1:numEnsemble
    
    %% Training and labeling for test data
    model = trainENN(trainDataFeatures_norm,qualMatrix,int16(numHidden));
    
    labelMatrix = predictENN(testData_norm,model);
    [~,label] = max(labelMatrix,[],2);
    % add to ensemble matrix
    ensLabels(:,i) = label;
    numHidden = numHidden*1.2;
end
%% Get final ensemble label
label = mode(ensLabels,2);
%% write to file
strOpen = strcat('ENNQualPredictionH',num2str(numHidden),'.csv');
fileID = fopen(strOpen,'wt');
fprintf(fileID,'%s,%s\n','id','quality');

for i = 1:length(testData)
    fprintf(fileID,'%d, %d\n',i,label(i));
    
end
fclose(fileID);
