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

exposePantry(L) :- \+ iteminpantry(OBJ) -> L = [] ; setof(item(OBJ), iteminpantry(OBJ), L).

exposeObjectOnTable(L) :- \+ itemontable(OBJ) -> L = [] ; setof(item(OBJ), itemontable(OBJ), L).
exposeFoodOnTable(L) :- \+ itemontable(F,C,Q) -> L = [] ; setof(item(F,C,Q), itemontable(F,C,Q), L).

exposeDishwasher(L) :- \+ itemindishwasher(OBJ) -> L = [] ; setof(item(OBJ), itemindishwasher(OBJ), L).

exposeObjectOnRobot(L) :- \+ itemonrobot(OBJ) -> L = [] ; setof(item(OBJ), itemonrobot(OBJ), L).
exposeFoodOnRobot(L) :- \+ itemonrobot(F,C,Q) -> L = [] ; setof(item(F,C,Q), itemonrobot(F,C,Q), L).

expose(OT, FT, P, D, OR, FR) :- exposeObjectOnTable(OT), exposeFoodOnTable(FT), exposePantry(P), exposeDishwasher(D), exposeObjectOnRobot(OR), exposeFoodOnRobot(FR). 

%% setof(+Template, +Goal, -Set) 
%% setof will fail (and so not bind Set) if the there are no instances of Template for which Goal succeeds - i.e. effectively it fails if Set would
%% be empty. bagof also fails in these circumstances, while findall does not (it binds the variable that is its third argument to the empty list, or,
%% if the third argument is instantiated, it succeeds if the third argument is the empty list).
%% setof(+Template, +Goal, -Set) 
%% \+ Goal -> Set= [] ; setof(Template, Goal, Set)
%% if ‘Goal' cannot be proven, then Set is empty, otherwise return the result of setof

output(M) :- stdout <- println(M).