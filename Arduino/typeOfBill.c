#include "typeOfBill.h"
#include <stdlib.h>

// returns index of matching bill
unsigned int typeOfBill(int numColors, int numReadings, int databaseSize,
        int reading[numReadings][numColors], int database[databaseSize][numReadings][numColors]){
    
    unsigned int bestMatchIndex = 0;
    unsigned long bestMatchError = 0;
    
    for(unsigned int databaseEntry = 0; databaseEntry < databaseSize; databaseEntry++){
        unsigned long error = 0;
        for(unsigned int readingNumber = 0; readingNumber < numReadings; readingNumber++){
            for(unsigned int colorNumber = 0; colorNumber < numColors; colorNumber++){
                error += abs( reading[readingNumber, colorNumber]
                        - database[databaseEntry][readingNumber][colorNumber] );
            }
        }
        if(error < bestMatchError || databaseEntry == 0){
            bestMatchIndex = databaseEntry;
            bestMatchError = error;
        }
    }

    return bestMatchIndex;
}