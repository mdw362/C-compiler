
int main ()
{
  int x=4;
  int y=9;
  if (x>5){
    x=7;
    y=0;
  }
  else if (x<=9){
    x=23;
    y=2;
  }
  else 
    x=3;
  
  x=x+y;
  y=y*x;
  return x+y;
}
