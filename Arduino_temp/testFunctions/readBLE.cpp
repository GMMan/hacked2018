#include <Arduino.h>
#include <stdlib.h>
#include "readBLE.h"

#define INVALID_OP_CODE     0xff

// returns command ID or 0xff for invalid
char readBLE(char **buffer){
    Serial2.begin(9600);

    char tempByte = 0x00;
    char opCode = INVALID_OP_CODE;
    char parityByte = 0x00;
    int length = 0;

    while(Serial2.available() && tempByte != 0xff00ff) tempByte = Serial2.read();

    if(Serial2.available()){
        opCode = Serial2.read();
    }

    // big-endian
    if(Serial2.available()){
        tempByte = Serial2.read();
        length = (int)tempByte;
        length <<= 8;
        if(Serial2.available()){
            tempByte = Serial2.read();
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

    if(Serial2.available()){
        for(int index = 0; index < length; index++){
            parityByte ^= (*buffer)[index];
        }

        tempByte = Serial2.read();
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
