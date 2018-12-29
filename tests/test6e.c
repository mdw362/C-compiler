#include <stdio.h>
#include <stdlib.h>
int main ()
{
  int x=4;
  if (x>5)
    x=7;
  else if (x<=9)
    x=23;
  else{ 
    if (x>20)
      x=18;
    x=x+1;
    x=3;
  }
  else 
    x=3;
  
  return x;
}
