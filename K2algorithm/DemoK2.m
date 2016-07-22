function k2score = DemoK2( datafile )
    data = csvread(datafile,1,0);    
    info = getInfo(data);
    limit = 3;
    %K2 algorithm
    order = [1,2,3,4,5,6];
    label = {'storms','bustourgroup','lightning','campfire','thunder','forestFire'};
    [ dag, k2score ] = myk2( info, order, limit );
    view(biograph(dag,label(order)));
end

