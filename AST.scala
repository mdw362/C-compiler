class AST (var root : ASTNode) {
  def getRoot () : ASTNode=root
 
  def printAST() {
    var current=root
    printNode(current)
    
    def printNode(node : ASTNode){
      println ("NODE VAL: " + node.getValue + "\t NODE TYPE: " + node.getDtype + "\tCHILDREN: " + node.getChildren().length)
      for (n <- node.getChildren()){
        printNode(n)
      }
    
    }
  }

}
