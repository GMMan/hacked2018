#include <Adafruit_NeoPixel.h>
#ifdef _AVR_
  #include <avr/power.h>
#endif
#include <Button.h>
Button button1(7);

#define PIN 6

Adafruit_NeoPixel strip = Adafruit_NeoPixel(60, PIN, NEO_RGBW + NEO_KHZ800);

int photocellPin = 0;     // the cell and 10K pulldown are connected to a0
int photocellReading;     // the analog reading from the analog resistor divider

void setup() {
  strip.begin();
  strip.setBrightness(30);
  strip.show(); // Initialize all pixels to 'off'
  button1.begin();
  Serial.begin(9600);

}

void loop() {
  if (button1.pressed()){
    for(uint16_t i=0; i<strip.numPixels(); i++)
    strip.setPixelColor(i, 100, 100, 100, 100);
    strip.show();
    delay(5000);
    for(uint16_t i=0; i<strip.numPixels(); i++)
    strip.setPixelColor(i, 0, 0, 0, 0);
    strip.show();
  }

    photocellReading = analogRead(photocellPin);  
 
  Serial.print("Analog reading = ");
  Serial.print(photocellReading);     // the raw analog reading
 
  // We'll have a few threshholds, qualitatively determined
  if (photocellReading < 796) {
    Serial.println(" - No Value");
  } else if (photocellReading < 740) {
    Serial.println(" - Purple");
  } else if (photocellReading < 815) {
    Serial.println(" - Blue");
  } else if (photocellReading < 855) {
    Serial.println(" - Brown");
  } else if (photocellReading < 869) {
    Serial.println(" - Green");
  } else if (photocellReading < 892) {
    Serial.println(" - Red");
  } else {
    Serial.println(" - No Value");
  }
  delay(2000);
}
