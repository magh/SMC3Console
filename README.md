# SMC3Console

Contains:
- utils to communicate with an Arduino running SMC3 via the serial interface.
- Assetto Corsa integration app to send accGFrontal data to SMC3.

# Other

sudo pacman -Sy java-rxtx

ln -s /dev/ttyACM0 /dev/ttyS33

Update IP address



# Run

`./gradlew run`

# Serial permission

`usermod -aG dialout USERNAME`

# Assetto Corsa

https://github.com/iimetra/assetto-corsa-telemetry-4j

Java 1.8

mvn clean install
