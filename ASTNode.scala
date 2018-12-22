import Array._

class ASTNode (var value : String, var dtype : String){

  // TODO: CHange to linked list or something
  var children=new Array[ASTNode](0)
  def getValue () : String=value
  def getDtype () : String=dtype
  def getChildren () : Array[ASTNode]=children
  def addChild (node : ASTNode){
    var temp =Array(node)
    children = concat (children, temp)
  }
  def isLeaf () : Boolean=children.length==0
}
