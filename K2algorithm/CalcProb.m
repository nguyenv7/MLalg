function [ label , prob ] = CalcProb( info, X, PX )
PX = sort(PX);
label = [];
prob = [];
N = info.numX;
RangeX = info.RangeVar(X);
ValX = info.VarVal{X};
data = info.Data;

UsedX = zeros(1,N);
i = 1;

while  i <= N
    Freq = zeros(1,RangeX);
    
    while  i <= N && UsedX(i) == 1
        i = i + 1;
    end
    
    if ( i > N )
        break;
    end
    
    for d = 1:RangeX
        if ValX(d) == data(i,X)
            break;
        end
    end
    
    Freq(d) = 1;
    UsedX(i) = 1;
    ParrentPattern = data(i,PX);
    i = i + 1;
    if (i > N )
        break;
    end
    
    for k = i : N
        if UsedX(k) == 1
            continue;
        end        
        if isequal(ParrentPattern,data(k,PX))                
            for d = 1:RangeX
                if ValX(d) == data(k,X)
                    break;
                end
            end
            Freq(d) = Freq(d) + 1;
            UsedX(k) = 1;
        end                        
    end
    Sum = sum(Freq);
    for t = 1:RangeX
        tlabel = [ParrentPattern ValX(t)]; 
        label = [label ; tlabel];
        prob = [prob ; Freq(t)/Sum];
    end
end


end

