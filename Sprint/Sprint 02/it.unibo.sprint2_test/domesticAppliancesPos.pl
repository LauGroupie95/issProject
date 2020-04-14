posTable(L) :- findall((X,Y), table(X,Y), L).
lastpostableinsert(X, [X]).
lastpostableinsert(X, [_|L]) :- lastpostableinsert(X, L).
lastpostableinsert(X) :- posTable(L), lastpostableinsert(X,L).