int fnOne (int num);
int fnTwo (int numOne, int numTwo);
int main ()
{
  int x=3;
  int y=fnOne(x);
  int z=fnTwo(x,y);
  return z;

}

int fnOne (int num)
{
  int new=1;
  if (num>55){
    new=num*12;
    return new;
  }
  else if (num <4){
    new=5;
    num=new+1;
  }
  else if (num>30){
    new=2;
    num=new+9;
  }
  else{
    num=7;
  }
  int n=2;
  return num+n;
}
int fnTwo(int numOne, int numTwo)
{
  int total=0;
  int i=0;
  for (; ;){
    total=total+numOne;
    total=total+numTwo;
    if (total>10) break;
  }
  return total;
}
