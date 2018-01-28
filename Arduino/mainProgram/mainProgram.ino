#include <Adafruit_NeoPixel.h>
#include <Adafruit_GFX.h>
#include <stdlib.h>
#include "readBLE.h"
#include "typeOfBill.h"
#include "num2Display.h"
#include "Adafruit_LEDBackpack.h"

#define NEO_PIXEL_PIN   6

#define READINGS    5
#define NUM_SENSORS 4
#define COLORS      3

const int encoderPin = 11;
const int buttonPin = 8;
const int timeout = 10000;

int sensorPin[NUM_SENSORS] = {A0, A1, A2, A3};
int colorValue[COLORS] = {0x000000ff, 0xff000000, 0x00ff0000};

int databaseSize = 0;
byte *database;
byte *values;

char *buffer;

int cachedValue = 150;

int screenOnTime = millis();
int screenTimeout = 10000;
int currentSum = 0;

byte *readingValue = NULL;

Adafruit_NeoPixel strip = Adafruit_NeoPixel(8, NEO_PIXEL_PIN, NEO_RGBW + NEO_KHZ800);
Adafruit_7segment disp = Adafruit_7segment();

void setup() {
    // put your setup code here, to run once:
    Serial.begin(9600);
    Serial2.begin(9600);

    strip.begin();
    //strip.setBrightness(30);
    disp.begin(DISPLAY_ADDRESS);

    pinMode(encoderPin, INPUT);
    pinMode(buttonPin, INPUT);

    Serial.println("\n end test section \n");

    readingValue = malloc(READINGS * NUM_SENSORS * COLORS);
}

void loop() {
    // put your main code here, to run repeatedly:
    num2Disp(cachedValue, disp);

    if(millis() - screenOnTime < screenTimeout || digitalRead(buttonPin == HIGH)){
        disp.clear();
        disp.writeDisplay();
        currentSum = 0;
    }
    
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
                updateValue();
                break;
            case 3: //get values
                sendValue();
                break;
        }
        if (opCode != INVALID_OP_CODE) {
            free(buffer);
            buffer = NULL;
        }
    }

    Serial.println(cachedValue);   

    if(digitalRead(encoderPin == HIGH)){
        int startTime = millis();
        //byte readingValue[READINGS * NUM_SENSORS][COLORS];
        for(int reading = 0; reading < READINGS; reading++){
            delay(500);
            while(digitalRead(encoderPin == LOW) && millis() - startTime > timeout); // wait for rotary encoder pulse
            if(millis() - startTime > timeout) break;
            for(int color = 0; color < COLORS; color++){
                for(uint16_t i=0; i<strip.numPixels(); i++)
                    strip.setPixelColor(i, colorValue[color]);
                strip.show();
                
                for(int sensor = 0; sensor < NUM_SENSORS; sensor++){
                    readingValue[reading * NUM_SENSORS + color * COLORS + sensor] = analogRead(sensorPin[sensor]);
                }
            }
            for(uint16_t i=0; i<strip.numPixels(); i++)
                strip.setPixelColor(i, 0);
            strip.show();
        }

        int matchingIndex = typeOfBill(databaseSize, readingValue, database);
        currentSum += values[matchingIndex];
        num2Disp(currentSum, disp);
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

char calcParity(char *buf, int len) {
    char parity = 0;
    int i;
    for (i = 0; i < len; ++i) {
        parity ^= buf[i];
    }
    return parity;
}

void sendMessage(char opCode, char *params, int paramsLength) {
    int bufLength = 3 + 1 + 2 + paramsLength + 1;
    char *buffer = malloc(bufLength);
    buffer[0] = 0xff;
    buffer[1] = 0;
    buffer[2] = 0xff;
    buffer[3] = opCode;
    buffer[4] = (paramsLength >> 8) & 0xff;
    buffer[5] = paramsLength & 0xff;
    memcpy(&buffer[6], params, paramsLength);
    buffer[6 + paramsLength] = calcParity(params, paramsLength);
    Serial2.write(buffer, bufLength);
    free(buffer);
}

void updateValue() {
    char *readPtr = buffer;
    cachedValue = read7BitEncodedInt(&readPtr);
}

void sendValue() {
    char *buffer = malloc(20);
    char *writePtr = buffer;
    write7BitEncodedInt(&writePtr, cachedValue);
    sendMessage(3, buffer, writePtr - buffer);
    free(buffer);
}

int read7BitEncodedInt(char **buffer) {
    int i = 0;
    int shift = 0;
    char ch;
    do {
        ch = **buffer;
        i |= (ch & 0x7f) << shift;
        shift += 7;
        (*buffer)++;
    } while (ch & 0x80);
    return i;
}

void write7BitEncodedInt(char **buffer, int value) {
    while (value) {
        **buffer = value & 0x7f;
        value >>= 7;
        if (value) **buffer |= 0x80;
        (*buffer)++;
    }
}

