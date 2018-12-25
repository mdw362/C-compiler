import scala.io.Source
import java.io._
import scala.util.matching.Regex
import Array._

object Compiler {
  def main (args: Array[String]){
    var cFile=args(0)
    var tokens: List[Token]=List ()
    var ast : AST = new AST (new ASTNode (null, "PROGRAM") )
    // Iterate through every char in file and construct tokens
    Source.fromFile(cFile).foreach{
      var str=""
      (s)=>{

        if (s=='(' || 
            s==')' || 
            s=='{' || 
            s=='}' || 
            s=='-' || 
            s=='~' || 
            s=='+' ||
            s=='/' ||
            s=='*' ||
            s=='!') {
              if (!(str.contains(" ")) || (str.matches("[0-9]+"))) tokenizer(str)
              tokenizer(s+"")
              str=""
            }
        else if (s==';'){
          if (!(str.contains(" ")) || str.matches("[0-9]+"))tokenizer(str)
          tokenizer(";")
          str=""
        }
        else if (s==' ' || s=='\n' || s=='\t'){
          if (!(str.contains(" ")))tokenizer(str)
          str=""
        }
        else str=str+s
      }
    }
    // Primary execution
//    printTokens()
    parseStatement()
//    ast.printAST()
 //   System.exit(0)
    generateCode()

    // Receives string input and converts it to a token
    def tokenizer (word : String){
      if (word=="") return
      var dtype : String="";
      if (word=="{")                         dtype="OPEN_BRACE" 
      else if (word=="}")                    dtype="CLOSE_BRACE" 
      else if (word=="(")                    dtype="OPEN_PAREN"
      else if (word==")")                    dtype="CLOSE_PAREN"
      else if (word==";")                    dtype="SEMI_COLON"
      else if (word=="-")                    dtype="NEGATION" 
      else if (word=="~")                    dtype="BIT_COMPL"
      else if (word=="!")                    dtype="LOG_NEGATION"
      else if (word=="+")                    dtype="ADD"
      else if (word=="*")                    dtype="MULTIPLY"
      else if (word=="/")                    dtype="DIVIDE"
      else if (word=="int")                  dtype="INT"
      else if (word=="return")               dtype="RETURN"
      else if (word.matches("[0-9]+"))       dtype="INT_LITERAL"
      else if (word.matches("[a-zA-Z]*"))    dtype="IDENTIFIER"
      else                                   dtype=null
      tokens=tokens ::: List(new Token (word.replaceAll("\n",""),dtype))
    }
    
    // Takes existing tokens and creates an AST
    def parseStatement () {
      var currentNode=ast.getRoot()
      var current=0
      while (current < tokens.length){
        if (tokens(current).getDtype==null) current+=1
        //TODO: use a stack to hold all parens and curly braces
        if (tokens(current).getDtype=="OPEN_PAREN") { 
          // push to stack 
        }
        if (tokens(current).getDtype=="CLOSE_PAREN"){
          // pop from stack
        }
        if (tokens(current).getDtype=="OPEN_BRACE"){
          // push to stack
        }
        if (tokens(current).getDtype=="CLOSE_BRACE"){
          // pop from stack
        }
        // check if stack is empty
        // Adds function to AST
        if (tokens(current).getDtype=="INT" && tokens(current+1).getDtype=="IDENTIFIER" && tokens(current+2).getDtype=="OPEN_PAREN"){
          var funcNode=new ASTNode (tokens(current+1).getValue(), "FUNCTION")
          currentNode.addChild(funcNode)
          currentNode=funcNode
        }
        // Possibly check if there is a curly brace in the stack
        if (tokens(current).getDtype=="RETURN" ) {
          current+=1
          var retNode=new ASTNode (null,"RETURN")
          // Recursively evaluate expression after return
          var exprNode=parseExpression ()

          if (exprNode!=null)retNode.addChild(exprNode)
          currentNode.addChild (retNode)
          if (tokens(current).getDtype!="SEMI_COLON"){
            println("ERROR: SEMI COLON MISSING. NOW EXITTING")
            System.exit(1)
          }
        }
        current+=1
      }
      def parseExpression () : ASTNode ={
        var termOne=parseTerm()
        var termTwo=new ASTNode(null,null)
        var token=tokens(current)
        var exprNode=new ASTNode(null, null)
        while (token.getDtype()=="ADD" || token.getDtype()=="NEGATION"){
          current+=1
          exprNode=new ASTNode (token.getValue , "EXPRESSION")
          termTwo=parseTerm()
          exprNode.addChild(termOne)
          exprNode.addChild(termTwo)
          termOne=exprNode
          token=tokens(current)
          
        }

        if (exprNode.getValue==null && exprNode.getDtype==null) termOne 
        else exprNode
      }
      def parseTerm (): ASTNode ={
        var factorOne=parseFactor()
        var factorTwo=new ASTNode(null, null)
        var token=tokens(current)
        var exprNode=new ASTNode (null, null)
        while (token.getDtype()=="MULTIPLY" || token.getDtype=="DIVIDE"){
          current+=1
          exprNode=new ASTNode (token.getValue, "EXPRESSION")
          factorTwo=parseFactor()

          token=tokens(current)
          exprNode.addChild(factorOne)
          exprNode.addChild(factorTwo)
          factorOne=exprNode

        }
        if (exprNode.getValue==null && exprNode.getDtype==null) factorOne 
        else exprNode
      }
      def parseFactor() : ASTNode = {
        var token=tokens(current)
        if (token.getDtype=="OPEN_PAREN"){
          current+=1
          var node=parseExpression ()
          if (!(tokens(current).getDtype=="CLOSE_PAREN"))
            println("ERROR: PARENTHESIS MISSING. NOW EXITING")
          current+=1
          node
        }
        else if (token.getDtype=="INT_LITERAL"){
          var node=new ASTNode (token.getValue, "EXPRESSION")
          current+=1
          node
        }
        // if unary operator
        else if (token.getDtype=="NEGATION" || token.getDtype=="LOG_NEGATION" || token.getDtype=="BIT_COMPL"){
            var unaryNode=new ASTNode (token.getValue, "EXPRESSION")
            current+=1
            unaryNode.addChild(parseFactor())
            unaryNode
          }   
        else null
      }
    }
    // Iterates through AST and translates the C code into x86 Assembly
    def generateCode(){
      var fileName=cFile.substring(0, cFile.length()-2)
      var codeGenerator =new PrintWriter (new File(fileName+".s"))
      var node : ASTNode = ast.getRoot()
      var lines=""
      search(node)
      // Performs DFS through AST and generates assembly for each respective node
      def search (n : ASTNode){
        var node=n
        if (node.getDtype=="FUNCTION"){
          lines=".globl _"+node.getValue+"\n_"+node.getValue+":\n"
          codeGenerator.write(lines)
          lines=""
          search(node.getChildren()(0))
        }
        if (node.getDtype=="RETURN"){
          lines=evalExpression(node.getChildren()(0))
          codeGenerator.write(lines)
          lines=""
        }
        if (node.getDtype=="PROGRAM"){
          for (c <- node.getChildren()){
            search(c)
          }
        }
        if (node.getDtype=="FUNCTION"){
          lines="ret\n"
          codeGenerator.write(lines)
          lines=""
        }
      }
      codeGenerator.close()
    }
    def evalExpression (n : ASTNode) : String ={
      var node=n
      var current=""
      if (node.isLeaf()){
        if (!(node.getValue.matches("[0-9]+"))){
          println("ERROR. NON-NUMBER FOUND IN LEAF")
          System.exit(1)
        }
        current="    movq\t$"+node.getValue()+", %rax\n"
      }
      else{
        if (node.getValue()=="+") current=evalOperation(node, "+")
        else if (node.getValue()=="-"){
          if (node.getChildren().length==1) 
            current=evalExpression(node.getChildren()(0))+"    neg \t%rax\n"
          else if (node.getChildren().length==2) current=evalOperation(node,"-")
        }
        else if (node.getValue=="*") current=evalOperation(node, "*")
        else if (node.getValue=="/") current=evalOperation(node, "/")
        else if (node.getValue=="~")
          current=evalExpression(node.getChildren()(0))+"    not \t%rax\n"
        else if (node.getValue=="!")
          current=evalExpression(node.getChildren()(0))+"    cmpl\t$0, %rax\n    movl\t$0, %rax\n    sete\t%al\n"
      }
      current
    }
    // Generate assembly for the four major operations
    def evalOperation (node : ASTNode, op : String) : String ={
      var current=""
      if (op=="+" || op=="-" || op=="*" || op=="/"){
        var t1=evalExpression (node.getChildren()(0))
        current=t1+"    push \t%rax\n"
        var t2=evalExpression(node.getChildren()(1))
      
        if (op=="+") current=current+t2+"    pop \t%rcx\n    addq\t%rcx, %rax\n"
        else if (op=="-") current=current+t2+"    pop \t%rcx\n    subq\t%rcx, %rax\n"
        else if (op=="*") current=current+t2+"    pop \t%rcx\n    imulq\t%rcx, %rax\n"
        else if (op=="/") current=current+t2+"    pop \t%rcx\n    idivq\t%rcx, %rax\n"
      }
      current
    }
    def printTokens(){
      for (i<-0 to tokens.length-1) 
        println("Token: " + tokens(i).getValue() + " Type: " + tokens(i).getDtype())
    }

  }
}
