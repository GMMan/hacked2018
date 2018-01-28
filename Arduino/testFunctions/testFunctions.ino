#include <Adafruit_NeoPixel.h>
#include "readBLE.h"
#include "typeOfBill.h"

#define NEO_PIXEL_PIN   6

const int encoderPin = 7;
const int timeout = 1000;

int sensorPin[NUM_SENSORS] = {A0, A1, A2, A3};
int colorValue[COLORS] = {0, 100, 200};

int databaseSize = 0;

Adafruit_NeoPixel strip = Adafruit_NeoPixel(60, NEO_PIXEL_PIN, NEO_RGBW + NEO_KHZ800);

void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);
    Serial2.begin(9600);

    strip.begin();
    strip.setBrightness(30);

    pinMode(encoderPin, INPUT);

    byte *database;

    char *buffer;
    readBLE(&buffer);

    Serial.println("\nend test section\n");
}

void loop() {
    // put your main code here, to run repeatedly:
    char *buffer;
    char opCode;
    
    if(Serial2.available()){
        //Serial.println((int)Serial2.read());
        
        opCode = readBLE(&buffer);
        Serial.print("opCode: ");
        Serial.println((int)opCode);
        switch((int)opCode){
            case 1: //update database
                break;
            case 2: //set value
                break;
            case 3: //get values
                break;
        }
    }

    if(digitalRead(encoderPin == HIGH)){
        int startTime = millis();
        byte readingValue[READINGS * NUM_SENSORS][COLORS];
        for(int reading = 0; reading < READINGS; reading++){
            delay(50);
            while(digitalRead(encoderPin == LOW) && millis() - startTime > timeout); // wait
            if(millis() - startTime > timeout) break;
            for(int color = 0; color < COLORS; color++){
                for(uint16_t i=0; i<strip.numPixels(); i++)
                    strip.setPixelColor(i, colorValue[color]);
                strip.show();
                
                for(int sensor = 0; sensor < NUM_SENSORS; sensor++){
                    readingValue[reading * NUM_SENSORS + sensor][color] = analogRead(sensorPin[sensor]);
                }
            }
        }
    }
}
