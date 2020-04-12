iteminpantry(dish).
iteminpantry(forks).
iteminpantry(glasses).

iteminfridge(raspberry, c000, 5).
iteminfridge(meet, c001, 5).
iteminfridge(cola, c002, 5).
iteminfridge(salad, c003, 5).
iteminfridge(pizza, c004, 10).
iteminfridge(fanta, c005, 10).
iteminfridge(apple, c006, 10).

exposeObjectOnTable(L) :- findall((X), itemontable(X), L).
exposeFoodOnTable(L) :- findall(X/Y/Z, itemontable(X,Y,Z), L).

exposeFridge(L) :- findall((X/Y/Z), iteminfridge(X,Y,Z), L).

exposePantry(L) :- findall((X), iteminpantry(X), L).

exposeDishwasher(L) :- findall((X), itemindishwasher(X), L).

exposeObjectOnRobot(L) :- findall((X), itemonrobot(X), L).
exposeFoodOnRobot(L) :- findall(X/Y/Z, itemonrobot(X,Y,Z), L).

%expose(H, T, R) :- exposeTable(H), exposeFridge(T), exposePantry(R).

expose(OT, FT, F, P, D, OR, FR) :- exposeObjectOnTable(OT), exposeFoodOnTable(FT), exposeFridge(F), exposePantry(P), exposeDishwasher(D), exposeObjectOnRobot(OR), exposeFoodOnRobot(FR). 
