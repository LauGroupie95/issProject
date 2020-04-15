cd JARs/it.unibo.robotmind-1.0/bin/
start cmd /k it.unibo.robotmind.bat


cd ../../../

cd JARs/itUniboRoomButler-1.0/bin/

start cmd /k itUniboClear.bat
timeout /t 5 /nobreak
start cmd /k itUniboScanningRoom.bat
timeout /t 5 /nobreak
start cmd /k itUniboFridge.bat
timeout /t 5 /nobreak
start cmd /k itUniboKb.bat
timeout /t 5 /nobreak
start cmd /k itUniboParty.bat
timeout /t 5 /nobreak
start cmd /k itUniboNavigator.bat
timeout /t 5 /nobreak
start cmd /k itUniboPrepare.bat
timeout /t 5 /nobreak
start cmd /k itUniboRoomButler.bat
timeout /t 5 /nobreak
start cmd /k itUniboSprint6