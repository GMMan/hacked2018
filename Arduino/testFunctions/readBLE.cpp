#include <Arduino.h>
#include <stdlib.h>
#include "readBLE.h"

#define INVALID_OP_CODE     0xff
#define TIMEOUT             100

// returns command ID or 0xff for invalid
char readBLE(char **buffer){

    char tempByte = 0x00;
    char opCode = INVALID_OP_CODE;
    char parityByte = 0x00;
    int length = 0;

    if(Serial2.read() != 0xff) return INVALID_OP_CODE;
    Serial.print("1 ");
    delay(TIMEOUT);
    if((int)Serial2.read() != 0) return INVALID_OP_CODE;
    Serial.print("2 ");
    delay(TIMEOUT);
    if(Serial2.read() != 0xff) return INVALID_OP_CODE;
    Serial.print("3 ");
    delay(TIMEOUT);

    if(Serial2.available()){
        opCode = Serial2.read();
        delay(TIMEOUT);
        Serial.print("4 ");
    }

    // big-endian
    if(Serial2.available()){
        tempByte = Serial2.read();
        delay(TIMEOUT);
        Serial.print("5 ");
        length = (int)tempByte;
        length <<= 8;
        if(Serial2.available()){
            tempByte = Serial2.read();
            delay(TIMEOUT);
            Serial.print("6 ");
            length |= (int)tempByte;
        }
        else{
            return INVALID_OP_CODE;
        }
    }
    else{
        return INVALID_OP_CODE;
    }

    *buffer = malloc(length);
    
    if(Serial2.readBytes(*buffer, length) != length){
        goto cleanup;
    }
    delay(TIMEOUT);
    Serial.print("7 ");

    if(Serial2.available()){
        for(int index = 0; index < length; index++){
            parityByte ^= (*buffer)[index];
        }

        tempByte = Serial2.read();
        Serial.print("8 ");
        if(tempByte != parityByte){
            goto cleanup;
        }
    }
    else {
        goto cleanup;
    }
    
    return opCode;

    cleanup:
    free(*buffer);
    *buffer = NULL;
    return INVALID_OP_CODE;
}
