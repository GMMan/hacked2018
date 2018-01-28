#include <Adafruit_GFX.h>
#include "Adafruit_LEDBackpack.h"

#ifndef NUM2DISPLAY_H
#define NUM2DISPLAY_H

#define DISPLAY_ADDRESS   0x70

//expects an integer number of cents
void num2Disp(int number, Adafruit_7segment disp);

#endif
