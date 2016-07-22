%%
load('wineData.mat');
numHidden = 200;
%% Convert numeric labels to matrix label
numTrain = size(trainDataQual,1);
% qual values: 1 to 7
qualMatrix = zeros(numTrain,7);
for i = 1:numTrain
    iLabel = trainDataQual(i);
    qualMatrix(i,iLabel) = 1;
end
%% Training and labeling for test data
model = trainENN(trainDataFeatures_norm,qualMatrix,numHidden);

labelMatrix = predictENN(testData_norm,model);

%% Convert matrix label to numeric labels
[~,label] = max(labelMatrix,[],2);
%% write to file
strOpen = strcat('ENNQualPredictionH',num2str(numHidden),'.csv');
fileID = fopen(strOpen,'wt');
fprintf(fileID,'%s,%s\n','id','quality');

for i = 1:length(testData)
    fprintf(fileID,'%d, %d\n',i,label(i));
    
end 
fclose(fileID);
