%% Running feature selection + cross validation to choose best feature set 
load('wineData.mat');
%featureList = featureSelection();
%featureList = [2,1,7,9,11,5,3,4,10];
%% Training and labeling for test data
model = trainLogisticRegression(trainDataFeatures,trainDataType,1:11);
label = predictLogisticRegression(testData,model,1:11);


%% write to file
fileID = fopen('LogisticRegressTypePredictionFull.csv','wt');
fprintf(fileID,'%s,%s\n','id','type');

for i = 1:length(testData)
    if(label(i) == 0)
        str = 'White';
    else
        str = 'Red';
    end
    fprintf(fileID,'%d, %s\n',i,str);
    
end 
fclose(fileID);
