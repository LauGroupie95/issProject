%% cell(X,Y,STATE).	%%	STATE 0 means obstacle, 1 means visited

%%direction( D ). 			%% D= sud, east, west, north
%%cell( X,Y, STATE )		%%CELL= 1 => done

initMap(DIRECTION) :-
	output("initMap  "),
	assign(x,0),
	assign(y,0),
	assert( direction(DIRECTION) ),
	retractall( cell(X,Y,STATE) ), 
	addRule( cell(0,0,1) ).		

changeDirectionA:- direction( D ), changeDirectionA( D ).
changeDirectionA( sud   ):- replaceRule( direction( sud ), direction( east ) ).
changeDirectionA( east  ):- replaceRule( direction( east ), direction( north ) ).
changeDirectionA( north ):- replaceRule( direction( north ), direction( west ) ).
changeDirectionA( west  ):- replaceRule( direction( west ), direction( sud ) ).

changeDirectionD:- direction( D ), changeDirectionD( D ).
changeDirectionD( sud   ):- replaceRule( direction( sud ), direction( west ) ).
changeDirectionD( east  ):- replaceRule( direction( east ), direction( sud ) ).
changeDirectionD( north ):- replaceRule( direction( north ), direction( east ) ).
changeDirectionD( west  ):- replaceRule( direction( west ), direction( north ) ).
	
updateMapAfterStep :-
	direction( D ),
	updateMap(D,X,Y),
	addRule( cell(X,Y,1) ),
	showMap.
	
updateMap(sud,X,Y):-
	getVal(x,X),
	getVal(y,LASTY),
	Y is LASTY+1,
	assign(y,Y). 
updateMap(east,X,Y):-
	getVal(y,Y),
	getVal(x,LASTX),
	X is LASTX + 1,
	assign(x,X).
updateMap(north,X,Y):-
	getVal(x,X),
	getVal(y,LASTY),
	Y is LASTY-1,
	assign(y,Y). 
updateMap(west,X,Y):-
	getVal(y,Y),
	getVal(x,LASTX),
	X is LASTX - 1,
	assign(x,X).
   
showMap :- 
	output("%%%%%%%%%%%%%%%%%%%%%% "),
	showCells,
	output("%%%%%%%%%%%%%%%%%%%%%% ").


showCells :-
 	cell( X,Y,STATE ),
 	output( cell( X,Y, STATE ) ),
	fail.
showCells.		

output( M ) :- stdout <- println( M ).


 
:- initialization(initMap).
 