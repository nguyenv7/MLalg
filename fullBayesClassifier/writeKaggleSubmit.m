%% Running feature selection + cross validation to choose best feature set 
load('wineData.mat');
%featureList = featureSelection();

%% Training and labeling for test data
model = trainFullBayes(trainDataFeatures,trainDataType,1:11);
label = predictFullBayes(testData,model);


%% write to file
fileID = fopen('FullBayestypePredictionFull.csv','wt');
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
