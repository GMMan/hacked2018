#include <Adafruit_NeoPixel.h>
#include <Adafruit_GFX.h>
#include <stdlib.h>
#include "readBLE.h"
#include "typeOfBill.h"
#include "num2Display.h"
#include "Adafruit_LEDBackpack.h"

#define NEO_PIXEL_PIN   6

const int encoderPin = 7;
const int timeout = 1000;

int sensorPin[NUM_SENSORS] = {A0, A1, A2, A3};
int colorValue[COLORS] = {0, 100, 200};

int databaseSize = 0;
byte *database;
byte *values;

char *buffer;

Adafruit_NeoPixel strip = Adafruit_NeoPixel(60, NEO_PIXEL_PIN, NEO_RGBW + NEO_KHZ800);
Adafruit_7segment disp = Adafruit_7segment();

void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);
    Serial2.begin(9600);

    strip.begin();
    strip.setBrightness(30);
    disp.begin(DISPLAY_ADDRESS);

    pinMode(encoderPin, INPUT);

    Serial.println("\nend test section\n");

    num2Disp(1234567, disp);
}

void loop() {
    // put your main code here, to run repeatedly:
    
    char opCode;
    
    if(Serial2.available()){
        //Serial.println((int)Serial2.read());
        
        opCode = readBLE(&buffer);
        Serial.print("opCode: ");
        Serial.println((int)opCode);
        switch((int)opCode){
            case 1: //update database
                updateDatabase();
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
            while(digitalRead(encoderPin == LOW) && millis() - startTime > timeout); // wait for rotary encoder pulse
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

void updateDatabase(){
    int dimension1 = buffer[0]; //num Entries
    int dimension2 = buffer[1]; //num Samples/Entry
    int dimension3 = buffer[2]; //num Colors
    int i;

    database = malloc(dimension1 * dimension2 * dimension3);
    values = malloc(sizeof(int) * dimension1);

    char *readPtr = &buffer[3];
    for(i = 0; i < dimension1; ++i){
        values[i] = read7BitEncodedInt(&readPtr);
    }

    memcpy(database, readPtr, dimension1 * dimension2 * dimension3);
}

int read7BitEncodedInt(char **buffer) {
    int i = 0;
    char ch;
    do {
        ch = *(*buffer++);
        i << 7;
        i |= ch & 0x7f;
    } while (ch & 0x80);
    return i;
}

