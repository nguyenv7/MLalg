function [acc ] = Test( datafile )
    data = csvread(datafile,1,0);    
    info = getInfo(data);
    limit = 3;
    %K2 algorithm
%     order = [1,2,3,4,5,6];
%     label = {'storms','bustourgroup','lightning','campfire','thunder','class'};
%     [ dag, k2score ] = myk2( info, order, limit );
%     view(biograph(dag,label(order)));
    % Test predict data
%     numX = size(data,1);
%     numTrain = round(numX * 0.9);
%     numTest = numX - numTrain;
%     trainData = data(1:numTrain,:);
%     testData =  data(numTrain+1:numX,:);
%     acc = PredictData(trainData,testData);
end

