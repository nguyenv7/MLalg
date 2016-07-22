function [ label ] = predictFullBayes( dataFeatures, model )
%using NaiveBayes classificaion algorithm to predict label for binary label
%data

dSet = model.dSet;
class0Dist = model.class0Dist;
class1Dist = model.class1Dist;
p0 = model.p0;
p1 = model.p1;
%% Likelihood on newData
probLi = ones(size(dataFeatures,1),2);
for i = 1:2 % for each dimension
    probLi(:,1) = probLi(:,1) .* mvnpdf(dataFeatures(:,dSet),class0Dist{1,i},class0Dist{2,i});
    probLi(:,2) = probLi(:,2) .* mvnpdf(dataFeatures(:,dSet),class1Dist{1,i},class1Dist{2,i});
end

%% Posterior
probPos(:,1) = probLi(:,1)*p0;
probPos(:,2) = probLi(:,2)*p1;

%% Make prediction
[~, maxID] = max(probPos,[],2);
label = maxID - 1;
end

