class AST (var root : ASTNode) {
  def getRoot () : ASTNode=root
 
  def printAST() {
    val current=root
    printNode(current)
    
    def printNode(node : ASTNode){
      if (node==null) println ("NODE IS NULL")
      else {
        println ("NODE VAL: " + node.getValue + "\t NODE TYPE: " + node.getDtype + "\tCHILDREN: " + node.getChildren().length)
        for (n <- node.getChildren()){
          printNode(n)
        }
      }
    
    }
  }

}
