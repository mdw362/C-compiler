
class Token (var value : String, var dtype: String){
  def getValue() : String=value
  def getDtype (): String=dtype
  def printToken (){
    println("TOKEN: " + dtype + "\tVALUE: " + value)
  }
}
object Token {

}
