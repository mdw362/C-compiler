int fn (int y);

int main ()
{
  int x=5;
  int z=x+fn(x);
  return z;
}
int fn (int y)
{
  y=3;
  return y;
}
