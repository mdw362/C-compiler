import scala.io.Source
import java.io._
import scala.util.matching.Regex
import Array._

object Compiler {
  def main (args: Array[String]){
    var cFile=args(0)
    // TODO: Change this to list
    var tokens=Array(new Token("", ""))
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
    parseStatement()
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

      var tokenArray=Array (new Token (word.replaceAll("\n", ""), dtype))
      tokens=concat (tokens, tokenArray)
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
        if (tokens(current).getDtype=="RETURN" && tokens(current+1).getDtype=="INT_LITERAL" && tokens(current+2).getDtype=="SEMI_COLON"){
          var retNode=new ASTNode (null,"RETURN")
          var litNode=new ASTNode (tokens(current+1).getValue, "INT_LITERAL")
          retNode.addChild(litNode)
          currentNode.addChild (retNode)
          while (current < tokens.length && tokens(current).getDtype!="CLOSE_BRACE") current+=1
          
          currentNode=ast.getRoot()
          if (tokens(current).getDtype=="CLOSE_BRACE"){
            // pop from stack
          }
          else {
            println ("ERROR: Missing closing curly brace")
          }
        }
        current+=1
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
          line=indent+"movl\t$"
          node=node.getChildren()(0)
          line=line+node.getValue()+", %eax\n"+indent+"ret\n"
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
