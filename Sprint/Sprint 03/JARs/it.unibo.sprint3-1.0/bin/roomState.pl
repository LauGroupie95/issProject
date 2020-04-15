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
%% Cibi nel frigo
%% --------------------------------------

iteminfridge(quiches,c000,8).
iteminfridge(wurstel,c001,20).
iteminfridge(cola,c002,15).
iteminfridge(chips,c003,30).
iteminfridge(pizza,c004,50).
iteminfridge(fanta,c005,10).
iteminfridge(beer,c006,20).
iteminfridge(pasta,c007,16).
iteminfridge(nuggets,c008,20).
iteminfridge(snacks,c009,52).
iteminfridge(wine,c010,20).
iteminfridge(colazero,c011,15).

%% --------------------------------------
%% Funzioni di utility
%% --------------------------------------

exposeObjectOnTable(L) :- findall((X), itemontable(X), L).
exposeFoodOnTable(L) :- findall(X/Y/Z, itemontable(X,Y,Z), L).

exposeFridge(L) :- findall((X/Y/Z), iteminfridge(X,Y,Z), L).

exposePantry(L) :- findall((X), iteminpantry(X), L).

exposeDishwasher(L) :- findall((X), itemindishwasher(X), L).

exposeObjectOnRobot(L) :- findall((X), itemonrobot(X), L).
exposeFoodOnRobot(L) :- findall(X/Y/Z, itemonrobot(X,Y,Z), L).

%expose(H, T, R) :- exposeTable(H), exposeFridge(T), exposePantry(R).

expose(OT, FT, F, P, D, OR, FR) :- exposeObjectOnTable(OT), exposeFoodOnTable(FT), exposeFridge(F), exposePantry(P), exposeDishwasher(D), exposeObjectOnRobot(OR), exposeFoodOnRobot(FR). 
