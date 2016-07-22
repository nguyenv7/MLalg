function [ accuracy ] = CrossValidation( datafile )
    % Load data
    rawdata = csvread(datafile,1,0);
    numX = size(rawdata,1);
    
    data = rawdata(:,1:size(rawdata,2)-1);
    data_label = rawdata(:,size(rawdata,2));
    
    % Loop 10 times    
    numTrain = round(numX / 10);
    numTest = numX - numTrain;
    accuracy = 0;
    
    for i = 1:10
        list = randperm(numX);
        %list = 1:numX;
        % Train data
        trainData = data(list(1:numTrain),:);
        trainLabel = data_label(list(1:numTrain),:);
        % Test data
        testData =  data(list(numTrain+1:numX),:);
        testLabel = data_label(list(numTrain+1:numX));
        % Training
        [LayerSize,Weight] = ANN(trainData,trainLabel);
        labelI = PredictANN(testData,LayerSize,Weight);
        accuracy = accuracy + sum(testLabel == labelI) / length(testLabel);
    end        
    accuracy = accuracy / 10;
    fprintf('Accuracy: %d \n',accuracy);    
end

