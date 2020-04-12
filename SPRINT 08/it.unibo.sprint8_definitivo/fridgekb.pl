%% --------------------------------------
%% Cibi nel frigo
%% --------------------------------------

iteminfridge(quiches,c000,2).
iteminfridge(wurstel,c001,20).
iteminfridge(cola,c002,15).
iteminfridge(chips,c003,30).
iteminfridge(pizza,c004,50).
iteminfridge(fanta,c005,10).
iteminfridge(beer,c006,20).
iteminfridge(pasta,c007,16).
iteminfridge(nuggets,c008,1).
iteminfridge(snacks,c009,52).
iteminfridge(wine,c010,20).
iteminfridge(colazero,c011,15).
iteminfridge(omelettes,c012,1).
iteminfridge(brioches,c013,52).
iteminfridge(crescentine,c014,20).
iteminfridge(tigelle,c015,15).

%% --------------------------------------
%% Funzioni di utility
%% --------------------------------------

exposeFridge(L) :- findall((X/Y/Z), iteminfridge(X,Y,Z), L).

