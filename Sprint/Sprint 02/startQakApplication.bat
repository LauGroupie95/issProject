cd JARs/itUniboRobotmind-1.0/bin/

start cmd /k itUniboRobotmind.bat

cd ../../../
cd JARs/it.unibo.sprint2-1.0/bin/

start cmd /k itUniboNavigator.bat
timeout /t 5 /nobreak
start cmd /k itUniboScanningRoom.bat
timeout /t 5 /nobreak
start cmd /k it.unibo.sprint2.bat



