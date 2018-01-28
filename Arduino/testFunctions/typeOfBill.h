#ifndef TYPEOFBILL_H
#define TYPEOFBILL_H

#define READINGS    3
#define COLORS      3

// returns index of matching bill
unsigned int typeOfBill(int databaseSize, int reading[READINGS][COLORS], int database[][READINGS][COLORS]);

#endif
