#include <Wire.h>
#include <Adafruit_GFX.h>
#include <RTClib.h>
#include <string.h>
#include "Adafruit_LEDBackpack.h"

void num2Disp(int number, Adafruit_7segment disp){
    char strBuf[10];
    int length = strlen(itoa(number, strBuf, 10));
    
    if(length <= 4){
        disp.writeDigitNum(0, number/1000, false);
        disp.writeDigitNum(1, number/100 % 10, true);
        disp.writeDigitNum(3, number/10 % 10, false);
        disp.writeDigitNum(4, number % 10, false);
        disp.writeDisplay();
    }
    else{
        for(int i = length - 4; i >= 0; i--){
            disp.writeDigitNum(0, number/(int)pow(10,length-i-4) % 10, false);
            disp.writeDigitNum(1, number/(int)pow(10,length-i-3) % 10, i == 0);
            disp.writeDigitNum(3, number/(int)pow(10,length-i-2) % 10, i == 1);
            disp.writeDigitNum(4, number/(int)pow(10,length-i-1) % 10, i == 2);
            disp.writeDisplay();
            delay(2000);
        }
    }
}
