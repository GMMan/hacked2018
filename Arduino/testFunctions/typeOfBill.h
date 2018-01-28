#ifndef TYPEOFBILL_H
#define TYPEOFBILL_H

// returns index of matching bill
unsigned int typeOfBill(int numColors, int numReadings, int databaseSize,
        int reading[numReadings][numColors], int database[databaseSize][numReadings][numColors]);

#endif
