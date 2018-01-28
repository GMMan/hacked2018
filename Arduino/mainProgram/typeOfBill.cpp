#include <stdlib.h>
#include <Arduino.h>
#include "typeOfBill.h"

#define READINGS    5
#define NUM_SENSORS 4
#define COLORS      3

// returns index of matching bill
unsigned int typeOfBill(int databaseSize, byte *reading, byte *database){
    
    unsigned int bestMatchIndex = 0;
    unsigned long bestMatchError = 0;
    
    for(unsigned int databaseEntry = 0; databaseEntry < databaseSize; databaseEntry++){
        unsigned long error = 0;
        for(unsigned int readingNumber = 0; readingNumber < READINGS; readingNumber++){
            for(unsigned int colorNumber = 0; colorNumber < COLORS; colorNumber++){
                error += abs( (int)reading[readingNumber * READINGS + colorNumber] - (int)database[databaseEntry * databaseSize + readingNumber * READINGS + colorNumber] );
            }
        }
        if(error < bestMatchError || databaseEntry == 0){
            bestMatchIndex = databaseEntry;
            bestMatchError = error;
        }
    }

    return bestMatchIndex;
}
