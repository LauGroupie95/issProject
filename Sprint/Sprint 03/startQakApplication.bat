cd JARs/itUniboRobotmind-1.0/bin/

start cmd /k itUniboRobotmind.bat

cd ../../../
cd JARs/it.unibo.sprint3-1.0/bin/

start cmd /k itUniboNavigator.bat
timeout /t 5 /nobreak
start cmd /k itUniboScanningRoom.bat
timeout /t 5 /nobreak
start cmd /k itUniboKb.bat
timeout /t 5 /nobreak
start cmd /k itUniboPrepare.bat
timeout /t 5 /nobreak
start cmd /k it.unibo.sprint3.bat



