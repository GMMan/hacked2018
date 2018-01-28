#include <Wire.h>
#include <Adafruit_GFX.h>
#include <RTClib.h>
#include "Adafruit_LEDBackpack.h"

#define DISPLAY_ADDRESS   0x70

Adafruit_7segment Disp = Adafruit_7segment();

void setup() {
  // put your setup code here, to run once:

  Disp.begin(DISPLAY_ADDRESS);
  

}

void loop() {
  // put your main code here, to run repeatedly:
int i = 100;
  Disp.writeDigitNum(0, i/1000, false);
  Disp.writeDigitNum(1, i/100 % 10, false);
  Disp.writeDigitNum(3, i/10 % 10, false);
  Disp.writeDigitNum(4, i % 10, false);
  Disp.writeDisplay();
  delay(100);

}
