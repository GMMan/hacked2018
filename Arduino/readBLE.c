// returns command ID or 0xffffff for invalid
char readBLE(char **buffer){
    Serial2.begin(9600);

    char tempByte = 0x000000;
    char opCode = 0xffffff;
    int length = 0;

    while(Serial2.available() && tempByte != 0xff00ff) tempByte = Serial2.read();

    if(Serial2.available()){
        tempByte = Serial2.read();
        length = (int)tempByte;
        length <= 8;
        if(Serial2.available()){
            tempByte = Serial2.read();
            length += (int)tempByte;
        }
    }

    if(Serial2.available()){
        opCode = Serial2.read();
    }
    
    return opCode;
}