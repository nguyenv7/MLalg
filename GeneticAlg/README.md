# Genetic Algorithm Implementation for Knap Sack problem

To run program: GAKnapSack('items.cvs');
From line 8 to 14 is
 % System parameter
    maxWeight = 200;  	% the weight limit
    ElitismPortion = 0.1; 	% the elitism fraction
    PopulationSize = 100; 	% population size 
    TourneySize = 2;	% tourney size
    MutationRate = 1/50;	% Mutation rate
    maxIter = 1000;		% the iteration limit or loop limit

I get the result with all parameter: 
Solution fitness value: 1068 
Solution fitness weight:192 

I wrote one point and two point crossover.
Penalty adjustment for weight limit is set the fitness value = 0.
Stop when 90% fitness value are equal.
Use Roulette wheel technology for Survivor phase.

## The approach:
-Initialize the first population
- Loop when not meet stop condition.
- Check stop condition to break the loop.
- Select parent by tournament.
- Elitism some parents.
- Create new population by crossover and mutation.
- Calculate new fitness value.
- Return the best solution
