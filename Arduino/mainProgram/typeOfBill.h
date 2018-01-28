#ifndef TYPEOFBILL_H
#define TYPEOFBILL_H

#define READINGS    5
#define NUM_SENSORS 4
#define COLORS      3

// returns index of matching bill
unsigned int typeOfBill(int databaseSize, byte *reading, byte *database);

#endif
