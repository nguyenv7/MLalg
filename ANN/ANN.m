function [LayerSize, Weight] = ANN(data, data_label)    
    
    numDim = size(data,2);   
    numOutput = length(unique(data_label));        
    numX = size(data,1);
    
    %% e: tolarance value for stop. epochs: number loop
    mse = Inf;
    epochs = 0;
    stopE = 10^-6;
    training_rate = 0.2;
    %% Config number node of each layer
    LayerSize = [4,3,3]; % or [4,4,3] or [3,3,3,3]
    nLayer = length(LayerSize);
    %% Check config is suitable for data
    if ( numDim ~= LayerSize(1)|| numOutput ~= LayerSize(end))
        fprintf('Wrong config size \n');
        return;
    end
    %% Init label for ANN training
    data_label_train = zeros(numX,LayerSize(end));   
    for i = 1:numX
        data_label_train(i,data_label(i)) = 1;
    end
    %% Init variable for speed
    % Weight matrix
    w = cell(nLayer-1,1); 
    for i=1:nLayer-2        
        w{i} = [1 - 2.*rand(LayerSize(i+1),LayerSize(i)+1) ; zeros(1,LayerSize(i)+1)];
    end
    w{end} = 1 - 2.*rand(LayerSize(end),LayerSize(end-1)+1);
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
        net{i} = ones(numX,LayerSize(i+1)+1); %add bias node 
    end
    net{end} = ones(numX,LayerSize(end));
    
    % Sum delta with weight each layer
    sum_delta = cell(nLayer-1,1);
    for i=1:nLayer-1
        sum_delta{i} = zeros(size(w{i}));
    end    
    
    % Loop for training
    while mse > stopE && epochs < 5000
        % Feed forward
         for i=1:nLayer-1
            net{i} = a{i} * w{i}'; 
            if i < nLayer - 1 % inner layer
                a{i+1} = [1./(1+exp(-net{i}(:,1:end-1))) ones(numX,1)];
            else             % output layer
                a{i+1} = 1 ./ (1 + exp(-net{i}));
            end
         end
        % Calc sum squarred error
        %[~,I] = max(a{end},[],2);
        err = (data_label_train - a{end});
        sse = sum(err.^2);
		fprintf('Sum square error: %d \n', sum(sse));
        % Backpropagation phase
        delta = err .* a{end} .* (1 - a{end});
        for i=nLayer-1:-1:1
            sum_delta{i} = training_rate * delta' * a{i};
            if i > 1            
                delta = a{i} .* (1-a{i}) .* (delta*w{i});
            end
        end

        % Update weight
        for i=1:nLayer-1        
            w{i} = w{i} + (sum_delta{i} ./ numX);
        end   
        epochs = epochs + 1;
        mse = sum(sse)/(numX*numOutput); % mse = 1/P * 1/M * summed squared error    
    end
    Weight = w;
end