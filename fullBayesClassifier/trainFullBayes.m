function [ model ] = trainFullBayes( trainFeatures, trainLabels, dSet )
%TRAINNAIVEBAYES Training using NaiveBayes classificaion algorithm
% dSet: list of dimension used.
model.dSet = dSet;
model.numLabel = length(unique(trainLabels));
%% Class prior
class0idx = (trainLabels == 0);
class1idx = (trainLabels == 1);

model.p0 = sum(class0idx) / length(class0idx);
model.p1 = sum(class1idx) / length(class1idx);

%% Estimate normal dist parameters
class0Dist = cell(2,model.numLabel); % for mu, sigma in each dimension
class1Dist = cell(2,model.numLabel); % for mu, sigma in each dimension

for i = 1:model.numLabel % for each label
    class0Dist{1,i} = mean(trainFeatures(class0idx,dSet));% for mu
    class0Dist{2,i} = cov(trainFeatures(class0idx,dSet));% for sigma
    
    class1Dist{1,i} = mean(trainFeatures(class1idx,dSet));% for mu
    class1Dist{2,i} = cov(trainFeatures(class1idx,dSet));% for sigma
end

model.class0Dist = class0Dist;
model.class1Dist = class1Dist;
end

