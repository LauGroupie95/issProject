%% --------------------------------------
%% Oggetti in dispensa 
%% --------------------------------------

iteminpantry(dishes).
iteminpantry(forks).
iteminpantry(glasses).
iteminpantry(napkins).
iteminpantry(spoons).
iteminpantry(knives).


%% --------------------------------------
%% Funzioni di utility
%% --------------------------------------

exposeObjectOnTable(L) :- findall((X), itemontable(X), L).
exposeFoodOnTable(L) :- findall(X/Y/Z, itemontable(X,Y,Z), L).

exposePantry(L) :- findall((X), iteminpantry(X), L).

exposeDishwasher(L) :- findall((X), itemindishwasher(X), L).

exposeObjectOnRobot(L) :- findall((X), itemonrobot(X), L).
exposeFoodOnRobot(L) :- findall(X/Y/Z, itemonrobot(X,Y,Z), L).

%expose(H, T, R) :- exposeTable(H), exposeFridge(T), exposePantry(R).

expose(OT, FT, P, D, OR, FR) :- exposeObjectOnTable(OT), exposeFoodOnTable(FT), exposePantry(P), exposeDishwasher(D), exposeObjectOnRobot(OR), exposeFoodOnRobot(FR). 
