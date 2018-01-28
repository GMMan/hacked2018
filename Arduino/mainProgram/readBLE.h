#ifndef READBLE_H
#define READBLE_H

#define INVALID_OP_CODE     0xff
#define TIMEOUT             10

// returns command ID or 0xffffff for invalid
char readBLE(char **buffer);

// Other I/O functions
char calcParity(char *buf, int len);
void updateDatabase();
char calcParity(char *buf, int len);
void sendMessage(char opCode, char *params, int paramsLength);
void updateValue();
void sendValue();
int read7BitEncodedInt(char **buffer);
void write7BitEncodedInt(char **buffer, int value);

#endif
