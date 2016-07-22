function [ info ] = getInfo( data )
info.numX = size(data,1); % number instances
info.numVar = size(data,2); % number variables
info.RangeVar = zeros(1,info.numVar); % ri of each var
info.VarVal = cell(1,info.numVar); % value of ri
info.Data = data;

for i = 1:info.numVar  
    subVar = data(:,i);
    subVar = unique(subVar);
    info.RangeVar(i) = length(subVar);    
    info.VarVal{i} = subVar;
end

end

