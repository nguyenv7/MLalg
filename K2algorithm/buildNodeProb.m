function [ Node ] = buildNodeProb( info, DAG )    
    numVar = info.numVar;        

    Node = cell(1,numVar);
    
    for i = 1:numVar
        ParentI = find(DAG(:,i) == 1);
        [Node{i}.label Node{i}.prob] = CalcProb(info,i,ParentI);
    end

end

