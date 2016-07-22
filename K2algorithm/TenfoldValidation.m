function [ accuracy ] = TenfoldValidation( datafile )
    data = csvread(datafile,1,0);
    numX = size(data,1);
    numTrain = round(numX * 0.9);
    numTest = numX - numTrain;
    accuracy = 0;
    
    for i = 1:10
        list = randperm(numX);
        trainData = data(list(1:numTrain),:);
        testData =  data(list(numTrain+1:numX),:);
        accuracy = accuracy + PredictData(trainData,testData);
    end
        
    accuracy = accuracy / 10;

end

