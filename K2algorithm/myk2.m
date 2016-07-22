function [ DAG, K2Score ] = myk2( info, Order, limit )
    numVar = info.numVar;
    DAG = zeros(numVar);
    K2Score = zeros(1,numVar);

    for i = 2:numVar
        Parent = zeros(1,numVar);
        Pold = CalcScore(info,Order(i),[]);
        isOK = 1;
        
        while isOK == 1 && sum(Parent) < limit
            maxScore = -Inf;
            maxNode = 0;
            for p = 1:i-1
                if Parent(Order(p)) == 1
                    continue;
                end                
                tParent = Parent;
                tParent(Order(p)) = 1;
                score = CalcScore(info,Order(i),find(tParent == 1));
                if score > maxScore
                    maxScore = score;
                    maxNode = Order(p);
                end
                
            end
            Pnew = maxScore;
            if (Pnew > Pold) 
                Pold = Pnew;
                Parent(maxNode) = 1;
            else
                isOK = 0;
            end
            
        end
        K2Score(Order(i)) = Pold;
        DAG(:,Order(i)) = Parent;
    end
end

