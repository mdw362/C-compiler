int fnOne (int num, int num2);
int main ()
{
  int x=3;
  int y=5;
  int z=fnOne(x,y);
  return z;

}

int fnOne (int num, int num2)
{
  int new=1;
  if (num>55){
    new=num*12;
    return new;
  }
  else if (num <4)
    new=5;
  int n=2;
  do {
    n=n+1;
  } while (n<12);
  n=n+4;
  new=10;
  while (n<new){
    n=n+3;
  }
  return num+n;
}

