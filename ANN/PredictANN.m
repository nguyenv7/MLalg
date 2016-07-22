function [ I ] = PredictANN( data, LayerSize, Weight )
    % parameter
    nLayer = length(LayerSize);
    w = Weight;
    numX = size(data,1);
    
    % Activation  value
    a = cell(nLayer,1);
    a{1} = [data ones(numX,1)]; % add bias term                         
    for i=2:nLayer-1
        a{i} = ones(numX,LayerSize(i)+1); % bias node 
    end
    a{end} = ones(numX,LayerSize(end));   
    % Net value each layer * weight matrix 
    net = cell(nLayer-1,1); 
    for i=1:nLayer-2;
        net{i} = ones(numX,LayerSize(i+1)+1); % bias node 
    end
    net{end} = ones(numX,LayerSize(end));
    
    % Feed forward
    for i=1:nLayer-1
        net{i} = a{i} * w{i}';
        if i < nLayer - 1 % inner layers
            a{i+1} = [1./(1+exp(-net{i}(:,1:end-1))) ones(numX,1)];
        else             % output layers
            a{i+1} = 1 ./ (1 + exp(-net{i}));
        end
    end
    % Get the label
    [~,I] = max(a{end},[],2);

end

