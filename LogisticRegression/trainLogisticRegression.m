function [ W, Li ] = trainLogisticRegression( features, label, dSet )
%TRAINLOGISTICREGRESSION Return estimate W of label = sigmoid(W' * features)
% GDLD Gradient descent for logistic regression
% Input: features: n x d
%        label   : n x 1
% return:
% weight vector W = [w, w0]: d+1 x 1
% Li the local maximum likelihood: scalar

features = features(:,dSet);
%% stop condition
numIterMax = 50;
deltaW = 10^-4;
%% data statistic
n = length(label);
d = size(features,2);
% add bias to data
X = [features, ones(n,1)];

%% declare variables
%W = rand(d+1,1);
W = rand(d+1,1);
R = label;
L = likelihoodFunc(X, R, W);
eta = 0.01;
%% run schotachatis grandient desecent
numIter = 1;
while (1)
    Wlast = W;
    if(numIter > numIterMax)
        %fprintf('Enough iteration \n');
        break;
    end
    % shuffle data
    idx = randperm(n);
    X = X(idx,:);
    R = R(idx,:);
    
    % update W
    for i = 1:n
        W = W + eta*(R(i)-sigmoid(X(i,:)*W))*X(i,:)';
        %L = [L , likelihoodFunc(X, R, W)];
    end
    numIter = numIter + 1;
    if(norm(Wlast-W,2) < deltaW)
        fprintf('Enough convergence \n');
        break;
    end
    
    %clf;
    %plot(1:length(L),L,'.');
end
p = sigmoid(X*W);
pred = (p>=0.5);
acc = sum(pred == R)/n;
Li = likelihoodFunc(X, R, W);
%clf;
%plot(1:length(L),L,'.');
end

function L = likelihoodFunc(X, R, W)
L = R' * log(sigmoid(X*W)) + (1-R') * log(1-sigmoid(X*W));
%L = sum((log(sigmoid(X*W)).^ R).*(log(1-sigmoid(X*W)).^(1-R)));
end

function p = sigmoid(x)
% Sigmoid function: s(x) = 1 / (1+exp(-x))
p = 1./(1+exp(-x));
end

