%% Implement Reinforcement learning by Policy Gradient
clear;
%% Init map
% reward for each nonterminal action (constant in this case)
R = -0.04;
% discount factor gamma
gamma = 1.0;
% which locations are walls
wall = [1 1 1 1 1 1; ...
    1 0 0 0 0 1; ...
    1 0 1 0 0 1; ...
    1 0 0 0 0 1; ...
    1 1 1 1 1 1];
[size_y, size_x] = size(wall);
% nonterminal states
nonterminal = [0 0 0 0 0 0; ...
    0 1 1 1 0 0; ...
    0 1 0 1 0 0; ...
    0 1 1 1 1 0; ...
    0 0 0 0 0 0];
% terminal rewards
terminal = [0 0 0 0  0 0; ...
    0 0 0 0 +1 0; ...
    0 0 0 0 -1 0; ...
    0 0 0 0  0 0; ...
    0 0 0 0  0 0];
% number the grid nodes from 1 to 30
coord = reshape(1:30, 5, 6);
up = coord + (-1)*nonterminal;        % move up
right = coord + (+size_y)*nonterminal;% move right
down = coord + (+1)*nonterminal;      % move down
left = coord + (-size_y)*nonterminal; % move left
% bump to wall does not move
up(find(wall(up))) = coord(find(wall(up)));
right(find(wall(right))) = coord(find(wall(right)));
down(find(wall(down))) = coord(find(wall(down)));
left(find(wall(left))) = coord(find(wall(left)));

% find resulting coordinate when moving into a direction (d)
s_prime(:,:,1) = left;
s_prime(:,:,2) = up;
s_prime(:,:,3) = right;
s_prime(:,:,4) = down;
% 1: left, 2: up, 3: right, 4: down

% start position: nonterminal(start.y,start.x)
start.y = 4;
start.x = 2;

%% Simulate M episode
M = 1000;
%alpha = 1;
alphaArray = [0.01,0.1,1];
alphaArrayColor ={'g','r','k'};
for a=1:3
    alpha = alphaArray(a);
    cRewardArray = zeros(M,1);
    %% Init theta and pi map
    theta = zeros(5,6,4);
    pi = zeros(5,6,4);
    for y = 1:size_y
        for x = 1:size_x
            sumTheta = sum(exp(theta(y,x,:)));
            pi(y,x,:) = exp(theta(y,x,:))/sumTheta;
        end
    end
    for m = 1:M
        %% In each episode, random walk K times or stop when go to terminal
        K = 100;
        % Go forward to reach the target
        nextState = 9;
        goArray = [];
        for k = 1:K
            goArray(k).at = nextState;
            [yTmp,xTmp] = find(coord == nextState);
            if(terminal(yTmp,xTmp)==1 || terminal(yTmp,xTmp)==-1)
                goArray(k).reward = terminal(yTmp,xTmp);
                break;
            end
            goArray(k).dir = RouletteWheelSelection(pi(yTmp,xTmp,:));
            goArray(k).reward = R;
            %goArray(k).next = s_prime(yTmp,xTmp,goArray(k).dir);
            %nextState = goArray(k).next;
            nextState = s_prime(yTmp,xTmp,goArray(k).dir);
        end
        % Go backward to update theta
        cReward = 0; % cumulative reward
        for kb = length(goArray):-1:1
            cReward = cReward + goArray(kb).reward;
            if isempty(goArray(kb).dir)
                continue;
            end
            % update theta
            [yTmp,xTmp] = find(coord == goArray(kb).at);
            for i=1:4
                if i==goArray(kb).dir
                    theta(yTmp,xTmp,i) = theta(yTmp,xTmp,i) + ...
                        alpha*(1-pi(yTmp,xTmp,i))*cReward;
                else
                    theta(yTmp,xTmp,i) = theta(yTmp,xTmp,i) + ...
                        alpha*-pi(yTmp,xTmp,i)*cReward;
                end
            end
            % update pi
            sumTheta = sum(exp(theta(yTmp,xTmp,:)));
            pi(yTmp,xTmp,:) = exp(theta(yTmp,xTmp,:))/sumTheta;
        end
        cRewardArray(m) = cReward;
    end
    % calculate the mean of accumulate reward 
    cRewardArrayMean = zeros(M,1);
    for i=1:M
        cRewardArrayMean(i) = mean(cRewardArray(1:i));
    end
    cRewardArrayMeanAlpha{a} = cRewardArrayMean;
end
plot( cRewardArrayMeanAlpha{1},'g');
hold;
plot( cRewardArrayMeanAlpha{2},'r');
plot( cRewardArrayMeanAlpha{3},'k');
line(xlim,[0.705 0.705]);
legend('0.01','0.1','1','goal','Location','southeast');
xlabel('N episodes');
ylabel('Average accumulate reward ');