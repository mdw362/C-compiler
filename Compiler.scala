import scala.io.Source
import java.io._
import scala.util.matching.Regex
import Array._

object Compiler {
  def main (args: Array[String]){
    var cFile=args(0)
    var tokens: List[Token]=List ()
    var ast : AST = new AST (new ASTNode (null, "PROGRAM") )
    
    Source.fromFile(cFile).foreach{
      var str=""
      (s)=>{
        
        if (s=='(' || 
            s==')' || 
            s=='{' || 
            s=='}' || 
            s=='-' || 
            s=='~' || 
            s=='!') 
          tokenizer(s+"")
        else if (s=='\n') str=""
        else if (s==';'){
          tokenizer(str)
          tokenizer(";")
          str=""
        }
        else if (s==' '){
          tokenizer(str)
          str=""
        }
        else str=str+s
      }
    }
//    printTokens()
    parseStatement()
//    printAST()
    generateCode()

    // Receives string input and converts it to a token
    def tokenizer (word : String){
      var dtype : String="";
      if (word=="{")                         dtype="OPEN_BRACE" 
      else if (word=="}")                    dtype="CLOSE_BRACE" 
      else if (word=="(")                    dtype="OPEN_PAREN"
      else if (word==")")                    dtype="CLOSE_PAREN"
      else if (word==";")                    dtype="SEMI_COLON"
      else if (word=="-")                    dtype="NEGATION" 
      else if (word=="~")                    dtype="BIT_COMPL"
      else if (word=="!")                    dtype="LOG_NEGATION"
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
          var expNode=parseExpression ()
          retNode.addChild(expNode)
          currentNode.addChild (retNode)
          if (tokens(current).getDtype!="SEMI_COLON"){
            println("SEMI COLON MISSING")
          }
        }
        current+=1
      }
      def parseExpression() : ASTNode = {
        var token=tokens(current)
        if (token.getDtype=="INT_LITERAL"){
          var node=new ASTNode (token.getValue, "EXPRESSION")
          current+=1
          node
        }
        // if unary operator
        else if (token.getDtype=="NEGATION" || token.getDtype=="LOG_NEGATION" || token.getDtype=="BIT_COMPL"){
            current+=1
            var op_token=parseExpression ()
            var node=new ASTNode (op_token.getValue+token.getValue, "EXPRESSION")
            node
          }   
        else null
      }
    }
    // Iterates through AST and translates the C code into x86 Assembly
    def generateCode(){
      var fileName=cFile.substring(0, cFile.length()-2)
      var codeGenerator =new PrintWriter (new File(fileName+".s"))
      var node : ASTNode = ast.getRoot()
      var line=""
      var indent=""
      while (node!=null){
        line=""
        if (node.getDtype=="FUNCTION"){
          line=".globl _"+node.getValue()+"\n_"+node.getValue+":\n"
          indent=indent+"    "
          codeGenerator.write(line)
        }
        if (node.getDtype=="RETURN"){
          node=node.getChildren()(0)
          // Perhaps verify that node is of type EXPRESSION
          var num=""
          // Parses expression of format number[unary_op]
          for (ch <- node.getValue()){
            var c=ch+""
            if ( c.matches("[0-9]")) num=num+c
            else{
              var ins=""
              if (num.length()>0){
                ins=indent+"movl\t$"+num+", %eax\n"
                num=""
              }
              if (c=="-")      ins=indent+"neg \t%eax\n"
              else if (c=="~") ins=indent+"not \t%eax\n"
              else if (c=="!") ins=indent+"cmpl\t$0, %eax\n"+indent+"movl\t$0, $eax\n"+indent+"sete\t%al\n"
              line=line+ins
            }
            if (num.length()>0) line=line+indent+"movl\t$" + num+", %eax\n"
            
          }
          
          line=line+indent+"ret\n"
          codeGenerator.write(line)
        }
        if (!node.isLeaf()) node=node.getChildren()(0)
        else node=null 
      }
      codeGenerator.close()
    
    }
    def printTokens(){
      for (i<-0 to tokens.length-1) 
        println("Token: " + tokens(i).getValue() + " Type: " + tokens(i).getDtype())
    }

    def printAST (){
      var node : ASTNode=ast.getRoot()
      // Create a queue to do depth first traversal
      println ("ISLEAF: " +node.isLeaf())
      while (!(node.isLeaf())){
        println ("TYPE: " + node.getDtype + "\tVALUE: " + node.getValue())
        node=node.getChildren()(0)
      }
      println ("TYPE: " + node.getDtype + "\tVALUE: " + node.getValue())

    }
  }

}
