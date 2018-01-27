WalletThing

Goal: a device and app that works together to count money in a wallet and display
the amount in a foreign currency

Hardware:
- Arduino
- Photoresistors - detects color of bills
- BLE module - comms
- Motor stick - position/presence of bills
- neopixel stick - lights up different colors
- 7-seg display - indicates amount of money inside
- reset button

Software: Android 5.0
- Connects to hardware through BLE
- Displays - current amount in wallet, history
- Sends - photoresistor comparison values for various bills, reset, conversion rate for supported bills
- Recieves - current value, conversion rate from internet, notification when money inserted/removed
