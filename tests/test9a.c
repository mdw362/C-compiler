 
int func (int t);
int main ()
{
  int x=3;
  return func(x);

}
int func (int t)
{
  t=t+1;
  return t;
}
