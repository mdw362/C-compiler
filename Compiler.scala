import scala.io.Source
import java.io._
import scala.util.matching.Regex
import Array._

object Compiler {
  def main (args: Array[String]){
    var cFile=args(0)
    var tokens: List[Token]=List ()
    var ast : AST = new AST (new ASTNode (null, "PROGRAM") )
    var envs : Map [String, String] = Map()
    // Iterate through every char in file and construct tokens
    Source.fromFile(cFile).foreach{
      var str=""
      var s2='\u0000'
      (s)=>{
        if (s2!='\u0000'){
          if (s=='=' ||
             (s2=='&' && s=='&') ||
             (s2=='|' && s=='|')) {
            tokenizer( (s2+"")+(s+""))
            str=""
            s2='\u0000'
          }
          else if (s2=='='){
            tokenizer(s2+"")
            s2='\u0000'
            if (s!=' ') str=s+""
          }
          else{
            if ((!(s+"").matches("[a-zA-Z]") ) && s!='\n')tokenizer(s2+"")
            s2='\u0000'
            str=""
          }
        }
        else if (s=='>' ||
                 s=='<' ||
                 s=='!' ||
                 s=='=' ||
                 s=='&' ||
                 s=='|'){
          if ( !(str.contains(" ")) || str.matches("[0-9]+")) tokenizer(str)
          s2=s
        }
        else if (s=='(' || 
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
//    System.exit(0)
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
      else if (word=="=")                    dtype="ASSIGNMENT"
      else if (word=="int")                  dtype="INT"
      else if (word=="&&")                   dtype="AND"
      else if (word=="||")                   dtype="OR"
      else if (word=="==")                   dtype="EQUAL"
      else if (word=="!=")                   dtype="NOT_EQUAL"
      else if (word=="<")                    dtype="LESS_THAN"
      else if (word==">")                    dtype="GREATER_THAN"
      else if (word=="<=")                   dtype="LESS_EQUAL_THAN"
      else if (word==">=")                   dtype="GREATER_EQUAL_THAN"
      else if (word=="return")               dtype="RETURN"
      else if (word.matches("[0-9]+"))       dtype="INT_LITERAL"
      else if (word.matches("[a-zA-Z]*"))    dtype="IDENTIFIER"
      else                                   dtype=null
      if (dtype==null) return
      tokens=tokens ::: List(new Token (word.replaceAll("\n",""),dtype))
    }
    
    // Takes existing tokens and creates an AST
    def parseStatement () {
      var currentNode=ast.getRoot()
      var current=0
      while (current < tokens.length){
        if (tokens(current).getDtype==null) current+=1
        // check if stack is empty
        // Adds function to AST
        if (tokens(current).getDtype=="INT" && tokens(current+1).getDtype=="IDENTIFIER" && tokens(current+2).getDtype=="OPEN_PAREN"){
          var funcNode=new ASTNode (tokens(current+1).getValue(), "FUNCTION")
          currentNode.addChild(funcNode)
          currentNode=funcNode
        }
        else if (tokens(current).getDtype=="INT" && tokens(current+1).getDtype=="IDENTIFIER"){
          if (tokens(current+2).getDtype=="SEMI_COLON"){

          }
          else if (tokens(current+2).getDtype=="ASSIGNMENT"){
            if (tokens(current+3).getDtype!="INT_LITERAL"){
              println("INVALID ASSIGNMENT. NOW EXITTING")
              System.exit(1)
            }
          }
        }
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
        else {
       //   var exprNode=parseExpression()
          
       //   if (exprNode!=null) currentNode.addChild(exprNode)
        }
        current+=1
      }
      def parseExpression () : ASTNode ={
        var andOne=parseAndExpression()
        var andTwo=new ASTNode(null, null)
        var token=tokens(current)
        var exprNode=new ASTNode(null, null)
        while (token.getDtype()=="OR"){
          current+=1
          exprNode=new ASTNode(token.getValue, "EXPRESSION")
          andTwo=parseAndExpression()
          exprNode.addChild(andOne)
          exprNode.addChild(andTwo)
          andOne=exprNode
          token=tokens(current)
        }
        if (exprNode.getValue==null && exprNode.getDtype==null) andOne
        else exprNode
      }
      def parseAndExpression () : ASTNode ={
        var eqOne=parseEqualityExpression()
        var eqTwo=new ASTNode(null, null)
        var token=tokens(current)
        var exprNode=new ASTNode (null, null)
        while (token.getDtype()=="AND"){
          current+=1
          exprNode=new ASTNode (token.getValue, "EXPRESSION")
          eqTwo=parseEqualityExpression()
          exprNode.addChild(eqOne)
          exprNode.addChild(eqTwo)
          eqOne=exprNode
          token=tokens(current)
        }
        if (exprNode.getValue==null && exprNode.getDtype==null) eqOne
        else exprNode
      }
      def parseEqualityExpression () : ASTNode = {
        var relOne=parseRelationalExpression()
        var relTwo=new ASTNode (null, null)
        var token=tokens(current)
        var exprNode=new ASTNode (null, null)
        while (token.getDtype()=="EQUAL" || token.getDtype()=="NOT_EQUAL"){
          current+=1
          exprNode=new ASTNode (token.getValue, "EXPRESSION")
          relTwo=parseRelationalExpression()
          exprNode.addChild(relOne)
          exprNode.addChild(relTwo)
          relOne=exprNode
          token=tokens(current)
        }
        if (exprNode.getValue==null && exprNode.getDtype==null)relOne
        else exprNode
      }
      def parseRelationalExpression () : ASTNode ={
        var addOne=parseAdditiveExpression()
        var addTwo=new ASTNode (null, null)
        var token=tokens(current)
        var exprNode=new ASTNode (null, null)
        while (token.getDtype()=="LESS_THAN" || 
               token.getDtype()=="GREATER_THAN" ||
               token.getDtype()=="LESS_EQUAL_THAN" ||
               token.getDtype()=="GREATER_EQUAL_THAN"){
          current+=1
          exprNode=new ASTNode (token.getValue, "EXPRESSION")
          addTwo=parseAdditiveExpression()
          exprNode.addChild(addOne)
          exprNode.addChild(addTwo)
          addOne=exprNode
          token=tokens(current)
        }
        if (exprNode.getValue==null && exprNode.getDtype==null) addOne
        else exprNode
      }
      def parseAdditiveExpression () : ASTNode ={
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
          if (!(tokens(current).getDtype=="CLOSE_PAREN")){
            println("ERROR: PARENTHESIS MISSING. NOW EXITING")
            println("CURRENT: " + tokens(current).getValue + " VAL: " + tokens(current).getDtype)
            System.exit(1)
          }
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
        else {
          println("RETURNING NULL FOR : " + token.getDtype + " WITH VAL: " + token.getValue)
          null
        }
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
      var spc="      "
      var asm=""
      if (node.isLeaf()){
        if (!(node.getValue.matches("[0-9]+"))){
          println("ERROR. NON-NUMBER FOUND IN LEAF")
          System.exit(1)
        }
        asm="    movq\t$"+node.getValue()+", %rax\n"
      }
      else{
        if (node.getValue()=="+") asm=evalOperation(node, "+")
        else if (node.getValue()=="-"){
          if (node.getChildren().length==1) 
            asm=evalExpression(node.getChildren()(0))+"    neg "+spc+"%rax\n"
          else if (node.getChildren().length==2) asm=evalOperation(node,"-")
        }
        else if (node.getValue=="*") asm=evalOperation(node, "*")
        else if (node.getValue=="/") asm=evalOperation(node, "/")
        else if (node.getValue=="==") asm=evalOperation(node, "==")
        else if (node.getValue=="!=")asm=evalOperation(node, "!=")
        else if (node.getValue==">=") asm=evalOperation(node,">=")
        else if (node.getValue=="<=") asm=evalOperation(node, "<=")
        else if (node.getValue=="<") asm=evalOperation(node, "<")
        else if (node.getValue==">") asm=evalOperation(node, ">")
        else if (node.getValue=="||") asm=evalOperation(node, "||")
        else if (node.getValue=="&&") asm=evalOperation(node, "&&")
        else if (node.getValue=="~")
          asm=evalExpression(node.getChildren()(0))+"    not   "+spc+"%rax\n"
        else if (node.getValue=="!")
          asm=evalExpression(node.getChildren()(0))+"    cmpq  "+spc+"$0, %rax\n    movq  "+spc+"$0, %rax\n    sete  "+spc+"%al\n"
      }
      asm
    }
    // Generate assembly for the  major operations
    def evalOperation (node : ASTNode, op : String) : String ={
      var asm=""
      var spc="      "
      var t1=evalExpression (node.getChildren()(0))
      asm=t1+"    push  "+spc+"%rax\n"
      var t2=evalExpression(node.getChildren()(1))
      asm=asm+t2+"    pop   " + spc+ "%rcx\n"
      if (op=="+") asm=asm +"    addq  "+spc+"%rcx, %rax\n"
      else if (op=="-") asm=asm+"    subq  "+spc+"%rcx, %rax\n"
      else if (op=="*") asm=asm+"    imulq  "+spc+"%rcx, %rax\n"
      else if (op=="/") asm=asm+"    idivq  "+spc+"%rcx, %rax\n"
      else if (op=="==") 
        asm=asm+"    cmpq  "+spc+"%rax, %rcx\n    movq  "+spc+"$0, %rax\n    sete  "+spc+"%al\n"
      else if (op=="!=")
        asm=asm+"    cmpq  "+spc+"%rax, %rcx\n    movq  "+spc+"$0, %rax\n    setne "+spc+"%al\n"
      else if (op==">=") 
        asm=asm+"    cmpq  "+spc+"%rax, %rcx\n    movq  "+spc+"$0, %rax\n    setge "+spc+"%al\n"
      else if (op=="<=") 
        asm=asm+"    cmpq  "+spc+"%rax, %rcx\n    movq  "+spc+"$0, %rax\n    setle "+spc+"%al\n"
      else if (op==">") 
        asm=asm+"    cmpq  "+spc+"%rax, %rcx\n    movq  "+spc+"$0, %rax\n    setg  "+spc+"%al\n"
      else if (op=="<") 
        asm=asm+"    cmpq  "+spc+"%rax, %rcx\n    movq  "+spc+"$0, %rax\n    setl  "+spc+"%al\n"
      else if (op=="||") 
        asm=asm+"    orq   "+spc+"%rcx, %rax\n    movq  "+spc+"$0, %rax\n    setne "+spc+"%al\n"
      else if (op=="&&")
        asm=asm+"    cmpq  "+spc+"$0, %rcx\n    setne "+spc+"%cl\n    cmpq  "+spc+"$0, %rax\n    movq  "+spc+"$0, %rax\n    setne "+spc+"$al\n    andb  "+spc+"%cl, %al\n"
      asm
    }
    def printTokens(){
      for (i<-0 to tokens.length-1) 
        println("Token: " + tokens(i).getValue() + " Type: " + tokens(i).getDtype())
    }

  }
}
