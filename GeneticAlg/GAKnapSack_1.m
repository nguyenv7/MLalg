function [ list, profit ] = GAKnapSack( datafile )
    best = [];
    average = [];
    % Read data
    data = csvread(datafile,1,0);
    % Get basic information
    numItem = size(data,1);
    weight = data(:,1);
    price = data(:,2);
    % System parameter
    maxWeight = 200;
    ElitismPortion = 0.1;
    PopulationSize = 100;
    TourneySize = 2;
    MutationRate = 1/50;
    maxIter = 1000;
    % Initialize the first population
    %Population = cell(1,PopulationSize);
    Population =[];
    for i = 1:PopulationSize
        record = GenerateFirst(numItem,weight,price,maxWeight);
        %Population(i) = record;
        Population = [Population record];
    end
    % Loop
    numIter = 1;
    while(1)
        if(numIter > maxIter)
            break;
        end
        % Check stop condition
        isStop = checkStop(Population);
        if ( isStop == 1)
            break;
        end        
        % Select parent
        ParentSet = [];
        % Sort fitness value
        fitnessP = [Population(:).fval];
        [~,Idx] = sort(fitnessP,'descend');
        fprintf('Best fitness value: %d \n',fitnessP(1));
        Population = Population(Idx);       
        best = [best ; Population(1).fval];
        average = [average ; mean(fitnessP)];
        
        % Elitism
        ElitSet = [];
        for i = 1:round(ElitismPortion*size(Population,2))
            ElitSet = [ElitSet Population(i)];            
            Population(i) = [];mean(fitnessP)
        end
%         for i = 1:round(ElitismPortion*size(Population,2))
%             Population(i) = [];
%         end                
        % Create new Population Generation
        newPopulation = {};
        newPopulation = [newPopulation ElitSet]; % add elitism 
        
        % Tournament selection
        Parent = TournamentSelect(Population,TourneySize);
        OffSpringSet = [];
        % Create offsprings
        for i = 1:round(PopulationSize/2) 
            i1 = randi([1 size(Parent,2)]);
            i2 = randi([1 size(Parent,2)]);
            while i2 == i1
                i2 = randi([1 size(Parent,2)]);
            end
            % CrossOver
            [c1, c2] = CrossOver(Parent(i1),Parent(i2));
            OffSpringSet = [OffSpringSet c1 c2];
        end
        
        % Mutation offsprings        
        for i = 1:length(OffSpringSet)
            OffSpringSet(i) = Mutation(OffSpringSet(i),MutationRate);
        end
        
        % Survivor selection offspring vs parent.
        % Calculating fitness value
        for i = 1:length(OffSpringSet)
            OffSpringSet(i) = FitnessValue(OffSpringSet(i), maxWeight);
        end
        % Selection set
        SelectSet = [Parent OffSpringSet];
        fitnessS = [SelectSet(:).fval];
        numSelect = PopulationSize - size(ElitSet,2);
        selectSet = [];
%         save();
        while(1)
            if (length(selectSet) >= numSelect)
                break;
            end
            chooseIdx = RouletteWheel(fitnessS);
%            fprintf('chooseIdx: %d \n',chooseIdx);
            if ( isempty(find(selectSet == chooseIdx)))                
                selectSet = [selectSet chooseIdx];
            end
            if (length(selectSet) >= length(unique(fitnessS)))
                selectSet = [selectSet chooseIdx];
            end
        end
%         for i = 1:numSelect
%             chooseIdx = RouletteWheel(fitnessS);            
%             selectSet = [selectSet chooseIdx];
%         end
        SurvivorSet = [SelectSet(selectSet)];
        % New population
        Population = [];
        Population = [Population ElitSet SurvivorSet];
        %
        numIter = numIter + 1;
    end
    % Print solution
    % Sort fitness value
    fitnessP = [Population(:).fval];
    [~,Idx] = sort(fitnessP,'descend'); 
    %
    solution = Population(Idx(1));    
    fprintf('Solution fitness value: %d \n',solution.fval);        
    save();
    fprintf('Solution fitness weight:%d \n',solution.Sw);
    profit = solution.fval;
	plot(1:numIter,best,1:numIter,average,'.-',legend('average','best'));
end

function chooseIdx = RouletteWheel(weight)
    accum = cumsum(weight);
    p = rand() * accum(end);
    chooseIdx = -1;
    for index = 1 : length(accum)
        if (accum(index) > p)
            chooseIdx = index;
            break;
        end
    end
    
end
function c = Mutation(record, rate )
    numMutation = round(rate * length(record.bol));
    r = rand();
    if r > 0.5
        preK = [];
        for i = 1:numMutation
            k = randi([1 size(record.bol,2)]);        
            if isempty(find(preK == k))
               record.bol(k) =  1 - record.bol(k);
               preK = [preK ; k];
            end
        end
    end
    c = record;
end

function [c1, c2] = CrossOver(record1 , record2)
r = rand();
if r > 0.5
    i1 = randi([2 size(record1.bol,2)-1]);
    c1 = record1;
    c2 = record2;
    r = rand();
    if ( r < 0.5)
        % One point cross over
        tmp = c2.bol(i1+1:end);
        c2.bol(i1+1:end) = c1.bol(i1+1:end);
        c1.bol(i1+1:end) = tmp;
    else
        % Two point cross over
        i2 = randi([1 size(record1.bol,2)]);
        while i2 <= i1 
            i2 = randi([1 size(record1.bol,2)]);
        end
        
        tmp = c2.bol(i1+1:i2);
        c2.bol(i1+1:i2) = c1.bol(i1+1:i2);
        c1.bol(i1+1:i2) = tmp;        
    end
else
    c1 = record1;
    c2 = record2;
end
end

function Parent = TournamentSelect(Population, TourSize)
    Parent = [];
    while size(Population,2) >= TourSize        
        % select TourSize tournament
        Pset = [];
        for k = 1:TourSize
            IdxPk = randi([1 size(Population,2)]);
            Pk = Population(IdxPk);                    
            Pset = [Pset Pk];            
            Population(IdxPk) = [];            
        end
        % sort fitness value
        fitnessP = [Pset(:).fval];
        [~,Idx] = sort(fitnessP,'descend');
        Parent = [Parent Pset(Idx(1))];
    end
    if size(Population,2) ~= 0
    fitnessP = [Population(:).fval];
    [~,Idx] = sort(fitnessP,'descend');
    Parent = [Parent Population(Idx(1))];
    end
end

function isStop = checkStop(Population) 
    fitnessP = [Population(:).fval];
    fitnessValue = unique(fitnessP);
    isStop = 0;
    for i = 1:length(fitnessValue)
        val = fitnessValue(i);
        idxVal = (fitnessP == val);
        portionVal = sum(idxVal) / size(Population,2);
        if ( portionVal >= 0.9 )
            isStop = 1;
            break;
        end
    end
end

function record = GenerateFirst(recordSize, weight, price, max)
% record.bol : string [0 1 0 1 1 ...] 
% record.fval: fitness value 
% record.w : weight array
% record.p : price array
% record.Sp : sum of price
% record.Sw : sum of weight
    record.bol = zeros(1,recordSize);
    record.w = weight;
    record.p = price;
    lastRecord = record;
    while(1)
        i = randi([1 recordSize]);
        if ( record.bol(i) == 1)
            continue;
        else
            record.bol(i) = 1;
        end
        record = FitnessValue(record,max);
        
        if ( record.fval == 0 )
            break;
        end
        lastRecord = record;
    end
    record = lastRecord;
end
function [record] = FitnessValue(record, max)
    SumWeight = 0;
    SumPrice = 0;
    for i = 1:length(record.bol)
        if record.bol(i) == 1
            SumWeight = SumWeight + record.w(i);
            SumPrice = SumPrice + record.p(i);
        end
    end
    
    if (SumWeight > max)
        record.fval = 0;
    else
        record.fval = SumPrice;
    end    
    record.Sw = SumWeight;
end