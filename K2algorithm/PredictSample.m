function [ label ] = PredictSample( info, sample,DAG, NodeProb )
% Predict the label for each given sample
% Assume the label variable is the last dimmension.

    numVar = size(DAG,1);
    maxProb = -Inf;
    predictVar = info.VarVal{numVar};
    
    % Test each possible label value
    for d = 1:length(predictVar)
        if length(sample) == numVar -1
            Sample = [sample predictVar(d)];
        elseif length(sample) == numVar
                Sample(numVar) = predictVar(d);
        else
            fprintf('Error: \n');
        end
            
        prob = 1;
        for i = 1:numVar
            parent = find(DAG(:,i) == 1);
            parent = sort(parent);

%             if isempty(parent)
%                 continue;
%             end

            currentlabel = [Sample(parent) Sample(i)];
            problabel = NodeProb{i}.label;
            for k = 1:size(problabel,1)
                if currentlabel == problabel(k,:)
                    prob = prob * NodeProb{i}.prob(k);
                    break;
                end
            end 
        end 
        % Select the label which maximizes the join probability
        if prob > maxProb
            maxProb = prob;
            label = predictVar(d);
        end
    end

end

