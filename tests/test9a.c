 
int func (int t);
int main (int x, int y)
{
  int z=x+y;
  return z+func(x);

}
int func (int t)
{
  t=t+1;
  return t;
}
