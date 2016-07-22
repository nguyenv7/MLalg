function [ accuracy ] = PredictData( data , test)
    % traning
    info = getInfo(data);
    limit = 3;
    % list all possible order of variables
    PermutationList = perms(1:info.numVar-1);
    PermutationList = [PermutationList ones(size(PermutationList,1),1)*info.numVar];
    BestList = [];
    BestScore = -Inf;
    BestDAG = [];
    % find best order of Bayes NetWork
    for i = 1:size(PermutationList,1) 
        Order = PermutationList(i,:);
        [ DAG, K2Score ] = myk2( info, Order, limit );
        scoremodel = (sum(K2Score));
        
        if scoremodel > BestScore
            BestScore = scoremodel;
            BestList = Order;
            BestDAG = DAG;
        end    
    end
    % build probability table for each node
    NodeProb = buildNodeProb(info,BestDAG);
    label = zeros(size(test,1),1);
    for i = 1:size(test,1)
        label(i) = PredictSample(info,test(i,:),BestDAG,NodeProb);        
    end
    actualLabel = test(:,info.numVar);
    accuracy = sum(label==actualLabel)/size(test,1);    
end

