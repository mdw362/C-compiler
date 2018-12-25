class AST (var root : ASTNode) {
  def getRoot () : ASTNode=root
 
  def printAST() {
    var current=root
    printNode(current)
  }  
  def printNode(node : ASTNode){
  
    println ("NODE VAL: " + node.getValue + "\t NODE TYPE: " + node.getDtype)
    if (node.getChildren().length>0)printNode (node.getChildren()(0))
    if (node.getChildren().length>1)printNode(node.getChildren()(1))
    
  }

}
