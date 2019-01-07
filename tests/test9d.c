int fnOne (int num);
int main ()
{
  int x=3;
  int y=fnOne(x);
  return y;

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
  else{
    num=7;
  }
  int n=2;
  return num+n;
}

