%% --------------------------------------
%% Cibi nel frigo
%% --------------------------------------

iteminfridge(quiches,c000,2).
iteminfridge(wurstel,c001,20).
iteminfridge(cocacola,c002,15).
iteminfridge(chips,c003,30).
iteminfridge(pizza,c004,50).
iteminfridge(fanta,c005,10).
iteminfridge(beers,c006,20).
iteminfridge(pasta,c007,16).
iteminfridge(nuggets,c008,1).
iteminfridge(snacks,c009,52).
iteminfridge(redwine,c010,20).
iteminfridge(sprite,c011,15).
iteminfridge(eggs,c012,1).
iteminfridge(coffee,c013,52).
iteminfridge(nutella,c014,20).
iteminfridge(tigelle,c015,15).

%% --------------------------------------
%% Funzioni di utility
%% --------------------------------------

exposeFridge(L) :- \+ iteminfridge(F,C,Q) -> L = [] ; setof(item(F,C,Q), iteminfridge(F,C,Q), L).

