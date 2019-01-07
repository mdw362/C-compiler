import scala.io.Source
import java.io._
import scala.util.matching.Regex
import Array._
import scala.collection.mutable.HashMap
import scala.collection.mutable.Stack
object Compiler {
  def main (args: Array[String]){
    var cFile=args(0)
    var tokens: List[Token]=List ()
    var ast : AST = new AST (new ASTNode (null, "PROGRAM") )
    // Iterate through every char in file and construct tokens
    Source.fromFile(cFile).foreach{
      analyzeChar()
    }
    // Primary execution
    printTokens
    parseStatement
    ast.printAST
    
  //  System.exit(0)
    generateCode

    def analyzeChar () = {
      var str=""
      var s2='\u0000'
      var ignore=false
      (s : Char) => {
        if (s=='\n') ignore=false
        else if (s=='#') ignore=true;
        // Checks for comparison operators
        if (!ignore){
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
              tokenizer(s2+"")         
              s2='\u0000'
              str=s+""
            }
          }
          else if (s=='>' ||
                   s=='<' ||
                   s=='!' ||
                   s=='=' ||
                   s=='&' ||
                   s=='|'){
            if ( !(str.contains(" ")) ) tokenizer(str)
            s2=s
          }
          // Checks for additional special characters
          else if (s=='(' || 
                   s==')' || 
                   s=='{' || 
                   s=='}' || 
                   s=='-' || 
                   s=='~' || 
                   s=='+' ||
                   s=='/' ||
                   s=='*' ||
                   s==':' ||
                   s=='?' || 
                   s=='!' ||
                   s==',') {
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
    }
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
      else if (word==",")                    dtype="COMMA"
      else if (word=="=")                    dtype="ASSIGNMENT"
      else if (word=="?")                    dtype="QUESTION"
      else if (word==":")                    dtype="COLON"
      else if (word=="int")                  dtype="INT"
      else if (word=="&&")                   dtype="AND"
      else if (word=="||")                   dtype="OR"
      else if (word=="==")                   dtype="EQUAL"
      else if (word=="!=")                   dtype="NOT_EQUAL"
      else if (word=="<")                    dtype="LESS_THAN"
      else if (word==">")                    dtype="GREATER_THAN"
      else if (word=="<=")                   dtype="LESS_EQUAL_THAN"
      else if (word==">=")                   dtype="GREATER_EQUAL_THAN"
      else if (word=="for")                  dtype="FOR"
      else if (word=="break")                dtype="BREAK"
      else if (word=="while")                dtype="WHILE"
      else if (word=="do")                   dtype="DO"
      else if (word=="continue")             dtype="CONTINUE"
      else if (word=="if")                   dtype="IF"
      else if (word=="else")                 dtype="ELSE"
      else if (word=="return")               dtype="RETURN"
      else if (word.matches("[0-9]+"))       dtype="INT_LITERAL"
      else if (word.matches("[a-zA-Z0-9]*"))    dtype="IDENTIFIER"
      else                                   dtype=null
      if (dtype==null) return
      tokens=tokens ::: List(new Token (word.replaceAll("\n",""),dtype))
    }
    
    // Takes existing tokens and creates an AST
    def parseStatement () {
      var currentNode=ast.getRoot()
      var current=0
      val functions : HashMap [String, Int]=HashMap()
      while (current < tokens.length){
        if (tokens(current).getDtype==null) current+=1
        handleStatement
        current+=1
      }
      // Converts individual statements into nodes
      def handleStatement (){ 
        // Parses function
        if (tokens(current).getDtype=="INT" && tokens(current+1).getDtype=="IDENTIFIER" && tokens(current+2).getDtype=="OPEN_PAREN"){
          val name=tokens(current+1).getValue
          val prog=currentNode 
          val funcNode=new ASTNode (tokens(current+1).getValue(), "FUNCTION")
          currentNode.addChild(funcNode)
          currentNode=funcNode
          val paramNode=new ASTNode (null, "PARAMETERS")
          currentNode.addChild(paramNode)
          val temp=currentNode
          currentNode=paramNode
          current+=2 // jump to open paren
          val numParams=parseParams
          current+=1 // jump to open brace
          currentNode=temp
          // Function validation
          if (functions.contains(name) &&
              numParams!=functions.get(name).get){
              error ("FUNCTION")
          }
          else functions += (name -> numParams)
          println("PARSING FUNC")
          tokens(current).printToken
          if (tokens(current).getDtype=="OPEN_BRACE"){
            while (tokens(current).getDtype!="CLOSE_BRACE"){
              handleStatement
              current+=1
              if (current>=tokens.length) error ("CURLY_BRACE")
            }
          }
          else if (tokens(current).getDtype!="SEMI_COLON")error ("SEMI_COLON")
          println("FINIHSED PARSING")
          currentNode=prog
        }
        // Parses variable assignments and declarations
        else if (tokens(current).getDtype=="INT" && tokens(current+1).getDtype=="IDENTIFIER"){
          // Declaration with no assignment
          if (tokens(current+2).getDtype=="SEMI_COLON"){
            val declNode=new ASTNode (tokens(current+1).getValue, "DECLARATION")
            currentNode.addChild (declNode)
            current+=2

          }
          // Declaration with assignment
          else if (tokens(current+2).getDtype=="ASSIGNMENT"){
            val declNode=new ASTNode (tokens(current+1).getValue, "DECLARATION")
            val asgNode=new ASTNode (tokens(current+1).getValue, "ASSIGNMENT")
            current+=3
            val exprNode=parseExpression
            if (exprNode==null) error ("ASSIGNMENT")
            asgNode.addChild(exprNode)
            declNode.addChild(asgNode)
            currentNode.addChild(declNode)
          }

          if (tokens(current).getDtype!="SEMI_COLON") error ("SEMI_COLON") 
        }
        // Parses only assignments
        else if (tokens(current).getDtype=="IDENTIFIER" && tokens(current+1).getDtype=="ASSIGNMENT"){
          val asgNode=new ASTNode (tokens(current).getValue, "ASSIGNMENT")
          current+=2
          val exprNode=parseExpression
          if (exprNode==null) error ("ASSIGNMENT")
          asgNode.addChild(exprNode)
          currentNode.addChild(asgNode)
          if (tokens(current).getDtype!="SEMI_COLON" && currentNode.getDtype!="FOR") 
            error ("SEMI_COLON")      
        }
        // Parses return statements
        else if (tokens(current).getDtype=="RETURN" ) {
          current+=1
          var retNode=new ASTNode (null,"RETURN")
          // Recursively evaluate expression after return
          var exprNode=parseExpression
          if (exprNode!=null)retNode.addChild(exprNode)
          currentNode.addChild (retNode)
          if (tokens(current).getDtype!="SEMI_COLON")error ("SEMI_COLON")
        }
        // Parses if statements
        else if (tokens(current).getDtype=="IF"){
          current+=1
          val exprNode=parseExpression
          val ifNode=new ASTNode (null, "IF")
          val condNode=new ASTNode (null, "CONDITIONAL")
          condNode.addChild(exprNode)
          condNode.addChild(ifNode)
          currentNode.addChild(condNode)
          val temp=currentNode
          currentNode=ifNode
          if (tokens(current).getDtype=="OPEN_BRACE"){
            while (tokens(current).getDtype!="CLOSE_BRACE"){
              if (tokens(current).getDtype=="INT" &&
                  tokens(current+1).getDtype=="IDENTIFIER") error ("DECLARATION") 
              else handleStatement
              current+=1
              if (current>=tokens.length) error ("CURLY_BRACE") 
            }
          }
          else handleStatement
          current+=1
          if (tokens(current).getDtype=="ELSE"){
            val elseNode=new ASTNode (null, "ELSE")
            condNode.addChild(elseNode)
            currentNode=elseNode
            current+=1
            if (tokens(current).getDtype=="IF") handleStatement
            else{
              if (tokens(current).getDtype=="OPEN_BRACE") current+=1
              while (tokens(current).getDtype!="CLOSE_BRACE"){
                if (tokens(current).getDtype=="INT" &&
                  tokens(current+1).getDtype=="IDENTIFIER") error ("DECLARATION")
                handleStatement
                current+=1
              }
            }
          }
          else current-=1
          currentNode=temp
        }
        else if (tokens(current).getDtype=="DO"){
          current+=1
          val doNode=new ASTNode (null, "DO")
          currentNode.addChild(doNode)
          val temp=currentNode
          currentNode=doNode
          if (tokens(current).getDtype=="OPEN_BRACE"){
            while (tokens(current).getDtype!="CLOSE_BRACE"){
              handleStatement
              current+=1
              if (current>=tokens.length) error ("CURLY_BRACE")
            }
          }
          else handleStatement
          current+=1
          if (tokens(current).getDtype!="WHILE") error _
          current+=1
          currentNode.addChild(parseExpression)
          if (tokens(current).getDtype!="SEMI_COLON") error ("SEMI_COLON")
          currentNode=temp

        }
        else if (tokens(current).getDtype=="WHILE"){
          current+=1
          val whileNode=new ASTNode (null, "WHILE")
          currentNode.addChild(whileNode)
          val temp=currentNode
          val exprNode=parseExpression
          whileNode.addChild(exprNode)
          currentNode=whileNode
          if (tokens(current).getDtype=="OPEN_BRACE"){
            while (tokens(current).getDtype!="CLOSE_BRACE"){
              handleStatement
              current+=1
              if (current>=tokens.length) error ("CURLY_BRACE")
            }
          }
          else handleStatement
          currentNode=temp
        }
        else if (tokens(current).getDtype=="FOR"){
          val temp=currentNode

          val forNode=new ASTNode (null, "FOR")
          currentNode.addChild(forNode)
          currentNode=forNode
          if (tokens(current+1).getDtype!="OPEN_PAREN") error ("PAREN")
          var i=0
          current+=2 // skip open paren
          // Evaluate for loop parameters
          
          for (i <- 0 to 2){
            if (!(i==2 && tokens(current).getDtype=="CLOSE_PAREN")){
              handleStatement
              current+=1
            }
          }
          if (tokens(current).getDtype=="CLOSE_PAREN") 
            current+=1
          if (tokens(current).getDtype=="OPEN_BRACE"){
            while (tokens(current).getDtype!="CLOSE_BRACE"){
              handleStatement
              current+=1
              if (current>=tokens.length) error ("CURLY_BRACE")
            }
          }
          else handleStatement
          currentNode=temp
        }
        else if (tokens(current).getDtype=="BREAK"){
          if (tokens(current+1).getDtype!="SEMI_COLON") error ("SEMI_COLON")
          currentNode.addChild (new ASTNode (null, "BREAK"))
       //   current+=1
        }
        else if (tokens(current).getDtype=="CONTINUE"){
          if (tokens(current+1).getDtype!="SEMI_COLON") error("SEMI_COLON")
          currentNode.addChild(new ASTNode (null, "CONTINUE"))
        //  current+=1
        }
        // Parses expressions
        else {
          val exprNode=parseExpression
          if (exprNode!=null) currentNode.addChild(exprNode)
          else if (currentNode.getDtype=="FOR") 
            currentNode.addChild(new ASTNode (null, "EMPTY"))
        }
      }
      // Starts parsing at open paren
      // Finishes at close paren
      def parseParams ()= {
        current+=1
        var numParams=0
        while (tokens(current+2).getDtype=="COMMA" || tokens(current+2).getDtype=="CLOSE_PAREN"){
          val declNode=new ASTNode (tokens(current+1).getValue, "DECLARATION")
          currentNode.addChild(declNode)
          numParams+=1
          if (tokens(current+2).getDtype=="COMMA") current+=3
          else current+=2
        }
        numParams
      }
      def parseExpression () : ASTNode ={
        var andOne=parseAndExpression
        var andTwo=new ASTNode(null, null)
        var token=tokens(current)
        var exprNode=new ASTNode(null, null)
        while (token.getDtype()=="OR"){
          current+=1
          exprNode=new ASTNode(token.getValue, "EXPRESSION")
          andTwo=parseAndExpression
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
        while (token.getDtype=="AND"){
          current+=1
          exprNode=new ASTNode (token.getValue, "EXPRESSION")
          eqTwo=parseEqualityExpression
          exprNode.addChild(eqOne)
          exprNode.addChild(eqTwo)
          eqOne=exprNode
          token=tokens(current)
        }
        if (exprNode.getValue==null && exprNode.getDtype==null) eqOne
        else exprNode
      }
      def parseEqualityExpression () : ASTNode = {
        var relOne=parseRelationalExpression
        var relTwo=new ASTNode (null, null)
        var token=tokens(current)
        var exprNode=new ASTNode (null, null)
        while (token.getDtype=="EQUAL" || token.getDtype=="NOT_EQUAL"){
          current+=1
          exprNode=new ASTNode (token.getValue, "EXPRESSION")
          relTwo=parseRelationalExpression
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
        val token=tokens(current)
        if (token.getDtype=="OPEN_PAREN"){
          current+=1
          val node=parseExpression 
          if (!(tokens(current).getDtype=="CLOSE_PAREN")) error ("PAREN")
          current+=1
          node
        }
        else if (token.getDtype=="IDENTIFIER" && tokens(current+1).getDtype=="OPEN_PAREN"){
          if (!(functions.contains(token.getValue))) error ("FUNCTION")
          val funcNode=new ASTNode (token.getValue, "FUNCTION_CALL")
          current+=2
          var numParams=0
          while (tokens(current).getDtype!="CLOSE_PAREN"){
            funcNode.addChild(parseExpression)
            if (tokens(current).getDtype=="COMMA") current+=1
            numParams+=1
          }
          if (numParams!=functions.get(token.getValue).get) error ("FUNCTION")
          current+=1
          funcNode
        }
        else if (token.getDtype=="INT_LITERAL" || token.getDtype=="IDENTIFIER"){
          var node=new ASTNode (token.getValue, "EXPRESSION")
          current+=1
          node
        }
        // if unary operator
        else if (token.getDtype=="NEGATION" || token.getDtype=="LOG_NEGATION" || token.getDtype=="BIT_COMPL"){
            var unaryNode=new ASTNode (token.getValue, "EXPRESSION")
            current+=1
            unaryNode.addChild(parseFactor)
            unaryNode
          }   
        else {
          //println("RETURNING NULL FOR : " + token.getDtype + " WITH VAL: " + token.getValue)
          null
        }
      }
    }
    // Iterates through AST and translates the C code into x86 Assembly
    def generateCode(){
      val fileName=cFile.substring(0, cFile.length()-2)
      val codeGenerator =new PrintWriter (new File(fileName+".s"))
      var n : ASTNode = ast.getRoot
      var lines=""
      var env : HashMap [String, Int]=null
      var params : HashMap [String, Int] = null
      var stackIndex=0
      var paramOffset=8
      var branchCount=0
      var condCount=0
      var loopTest= 0
      var loops = Stack [String]()
      println("ROOT CHILDREN======")
      for (c <- n.getChildren){
        c.printNode
      }
      println("END ROOT ======")
      search(n)
      // Performs DFS through AST and generates assembly for each respective node
      def search (node : ASTNode){
        // rsp is top of frame, rbp is bottom of frame; rbp tells 
        // where stack ends
        if (node.getDtype=="FUNCTION"){
          if (node.getChildren.length==1) return
          paramOffset=8
          params= HashMap()
          lines=".globl _"+node.getValue+"\n_"+node.getValue+":\n"
          lines=lines+"    pushq       %rbp\n"
          lines=lines+"    movq        %rsp, %rbp\n"
          codeGenerator.write(lines)
          env=HashMap()
          lines=""
          stackIndex=0
          val doesRet : (ASTNode) => Boolean= (node) => node.getDtype=="RETURN"

          println("EVALUATING FUNC " + node.getValue+ " ==============")
          for (c <- node.getChildren){
            if (c.getDtype=="PARAMETERS"){
              for (gc <- c.getChildren){
                params += (gc.getValue -> paramOffset)
                paramOffset+=4
              }
            }
            else{
              search(c)
            }
          }
          for (c <- node.getChildren) c.printNode
          println("FINISHED EVALUATING FUNC=============")
//          TODO: This is printing when it shouldn't
          if (!(node.getChildren.exists(doesRet))){
            lines=lines+"    movq        $0, %rax\n"
            lines=lines+"    movq        %rbp, %rsp\n"
            lines=lines+"    popq        %rbp\n"
            lines=lines+"    ret\n"
          }
          /*
          if (!doesRet) {
            println("FAULTY RETURN")
            codeGenerator.write("    movq        $0, %rax\n")
            lines=lines+"    movq        %rbp, %rsp\n"
            lines=lines+"    popq        %rbp\n"
            lines=lines+"    ret\n"
          }*/
          codeGenerator.write(lines)
          lines=""        
        }
        /*
        if (node.getDtype=="FUNCTION_CALL"){
          codeGenerator.write("    subq\t$"+4*(node.getChildren.length+1)+", %rax\n")
          codeGenerator.write("    xorq\t%rdx, rdx\n    movq\t0x20, %rcx\n")
          codeGenerator.write("    idivq\t%rcx\n    subq\t%rdx, %rsp\n    pushq\t%rdx\n")   
          for (i <- node.getChildren.length-1 to 0){
            search (node.getChildren()(i))
            codeGenerator.write ("    pushq       %rax\n")
          }
          codeGenerator.write("    callq       _"+node.getValue+"\n")
          codeGenerator.write("    addq        $0x"+(node.getChildren.length*4).toHexString+", %rsp\n")
          codeGenerator.write("    popq\t%rdx\n    addq\t%rdx, %rsp\n")

        }*/
        // If there is no return statement, return 0
        if (node.getDtype=="RETURN"){
          lines=""
          lines=evalExpression(node.getChildren()(0), env, params)
          codeGenerator.write(lines)
          lines=""
          node.printNode
          lines=lines+"    movq        %rbp, %rsp\n"
          lines=lines+"    popq        %rbp\n"
          codeGenerator.write(lines+"    ret\n")
          lines=""
        }
        if (node.getDtype=="PROGRAM"){
          node.getChildren.foreach { (c) => search (c) }
        }
        // For declarations, populates env and sets stack offset appropriately
        if (node.getDtype=="DECLARATION"){
          if ((env.contains(node.getValue))) error ("MULTI_DECLARATION")
          env+= (node.getValue -> stackIndex)
          stackIndex-=4
          // If value is declared and not initialized, initialize it to 0
          if (node.getChildren.length==0){
            lines=lines+"    movq        $0, %rax\n"
            lines=lines+"    pushq       %rax\n"
            codeGenerator.write(lines)
            lines=""
          }
          else{
            lines=evalExpression (node.getChildren()(0).getChildren()(0), env, params)
            lines=lines+"    pushq        %rax\n"
            codeGenerator.write(lines)
            lines=""
          }
        }
        // For if/else statements, generate jumps appropriately
        if (node.getDtype=="CONDITIONAL"){
          codeGenerator.write(evalExpression (node.getChildren()(0), env, params))
          lines="    cmpq         $0, %rax\n"
          // if there is no else statement, we want to jump to statements
          // under if. If there is an else statement, we jump to 
          // statements under if
          if (node.getChildren.length==2)
            lines=lines+(s"    je           _post_conditional${condCount}\n")
          else if (node.getChildren.length==3)
            lines=lines+"    je           _branch"+branchCount+"\n"
//          else
 //           lines=lines+"    jne          _post_conditional"+condCount+"\n"
          codeGenerator.write(lines)
          lines=""
          val checkCond : (ASTNode) => Boolean = (node) => node.getDtype=="CONDITIONAL"
          val checkRet : (ASTNode) => Boolean = (node) => node.getDtype=="RETURN"
          val currCond=condCount
          def condFunc (c : ASTNode) = {
            if (!(c.getDtype=="EXPRESSION")){
              if (c.getDtype=="ELSE"){
                codeGenerator.write(s"_branch${branchCount}\n")
                branchCount+=1
              }

              c.getChildren.foreach{
                (gc) => search(gc)
              }
              if (c.getDtype=="IF" && !(c.getChildren.exists (checkRet))){
                if (node.getChildren.length==3)
                  codeGenerator.write("    jmp        _post_conditional"+condCount+"\n")

              }
              if ((c.getDtype=="ELSE" && !(c.getChildren.exists(checkCond)) &&
                c.getChildren.length==1) || 
                (c.getDtype=="IF" && node.getChildren.length==2)){
                codeGenerator.write(s"_post_conditional_${condCount}\n")
                condCount+=1
              }
              /*
              if (node.getChildren.length>2){
                codeGenerator.write("    jmp          _post_conditional"+currCond+"\n")

                if (branchCount<node.getChildren.length-2){
                  codeGenerator.write("_branch"+branchCount+":\n")
                  branchCount+=1
                }
              }*/
            }
          }
          node.getChildren().foreach{
            condFunc 
          }
 //         codeGenerator.write("_post_conditional"+currCond+":\n")
        }
        // Evaluates expression, puts it in %rax, then moves that value to the
        // correct place in memory
        if (node.getDtype=="ASSIGNMENT"){
          if (!(env.contains(node.getValue)) && !(params.contains(node.getValue)))
            error ("ERROR: "+ node.getValue+ " IS UNDEFINED. NOW EXITTING")
          lines=evalExpression (node.getChildren()(0), env, params)
          if (env.contains(node.getValue) && env.get(node.getValue).get!=0) 
            lines=lines+"    movq        %rax, "+env.get(node.getValue).get+"(%rbp)\n"
          else if (env.contains(node.getValue))
            lines=lines+"    movq       %rax, (%rbp)\n"
          else if (params.contains(node.getValue) && params.get(node.getValue).get!=0)
            lines=lines+"    movq      %rax, " + params.get(node.getValue).get+"%(rbp)\n"
          else
            lines=lines+"    movq       %rax, (%rbp)\n"
          codeGenerator.write (lines)
        }
        if (node.getDtype=="EXPRESSION"){
          lines=evalExpression (node, env, params)
          codeGenerator.write(lines)
        }
        if (node.getDtype=="WHILE"){
          val currWhile=loopTest
          loops.push("WHILE")
          loopTest+=1
          lines=lines+evalExpression (node.getChildren()(0), env, params)
          lines=lines+"    cmpq          $0, %rax\n"
          lines=lines+"    jne           _post_loop" + currWhile+"\n"
 //         lines=lines+"    jmp           _post_loop"+currWhile+"\n"
          lines=lines+"_loop" + currWhile+":\n"
          codeGenerator.write(lines)
          var i=0
          for (i <- 0 to node.getChildren.length-1){
            if (i>0) search(node.getChildren()(i))
          }
          lines=""
          codeGenerator.write("_post_loop"+currWhile+":\n")
          loops.pop
        }
        if (node.getDtype=="DO"){
          val currDo=loopTest
          loopTest+=1
          loops.push("WHILE")
          codeGenerator.write("_loop"+currDo+":\n")
          var i=0
          for (i <- 0 to node.getChildren.length-1){
            search (node.getChildren()(i))
          }
          lines="    cmpq          $0, %rax\n"
          lines=lines+"    jne          _loop"+currDo+"\n"
          codeGenerator.write(lines+"_post_loop"+currDo+"\n")
          lines=""
          loops.pop
        }
        if (node.getDtype=="FOR"){
          val currFor=loopTest
          loops.push("FOR")
          loopTest+=1
          search (node.getChildren()(0))
          if (node.getChildren()(1).getDtype!="EMPTY"){
            search (node.getChildren()(1))
            codeGenerator.write("    cmpq         $0, %rax\n    je            _post_loop"+currFor+"\n")
          }
          codeGenerator.write("_loop"+currFor+":\n")
          var i=0
          for (i <- 3 to node.getChildren.length-1)
            search (node.getChildren()(i))
          codeGenerator.write("_for_exp"+currFor+":\n")
          lines=""
          if (node.getChildren()(2).getDtype!="EMPTY")
            search (node.getChildren()(2))
          
          if (node.getChildren()(1).getDtype!="EMPTY"){
            search (node.getChildren()(1))
            lines="    cmpq          $0, %rax\n    jne          _loop"+currFor +"\n"
          }
          else {
            lines="    jmp           _loop"+currFor+"\n"
          }
          codeGenerator.write(lines+"_post_loop"+currFor +":\n")
          lines=""
          loops.pop
        }
        if (node.getDtype=="BREAK"){
          if (loops.isEmpty) error ("ERROR: BREAK STATEMENT USED OUTSIDE OF LOOP. NOW EXITTING")
          codeGenerator.write("    jmp           _post_loop"+(loopTest-1) +"\n")
        }
        if (node.getDtype=="CONTINUE"){
          if (loops.isEmpty) error ("ERROR: CONTINUE STATEMENT USED OUTSIDE OF LOOP. NOW EXITTING")
          if (loops.top=="FOR")
            codeGenerator.write("    jmp           _for_exp"+(loopTest-1)+"\n")
          else if (loops.top=="WHILE")
            codeGenerator.write("    jmp           _loop"+(loopTest-1)+"\n")
        }
      }
      codeGenerator.close()
    }
    def evalExpression (n : ASTNode,  env : HashMap [String, Int], params : HashMap [String, Int]) : String ={
      var node=n
      val spc="      "
      var asm=""
      if (node.isLeaf){
        if (!(node.getValue.matches("[0-9]+")) && !(node.getValue.matches("[a-zA-Z]+"))){
          println("ERROR. NON-NUMBER FOUND IN LEAF")
          System.exit(1)
        }
        // If the value is an int literal
        if (node.getValue.matches("[0-9]+"))asm="    movq\t$"+node.getValue+", %rax\n"
        // If the value is a variable
        else {
          if (env.contains(node.getValue) && env.get(node.getValue).get==0)
            asm="    movq\t(%rbp), %rax\n"
          else if (env.contains(node.getValue) && env.get(node.getValue).get!=0)
            asm="    movq\t"+env.get(node.getValue).get+"(%rbp), %rax\n"
          else if (params.contains (node.getValue))
            asm="    movq\t"+params.get(node.getValue).get+"(%rbp), %rax\n"
          else
            error ("ERROR: IDENTIFIER " + node.getValue + " NOT FOUND. NOW EXITTING")
        } 
      }
      else if (node.getDtype=="FUNCTION_CALL"){
          asm=asm+"    subq\t$"+4*(node.getChildren.length+1)+", %rax\n"
          asm=asm+"    xorq\t%rdx, rdx\n    movq\t0x20, %rcx\n"
          asm=asm+"    idivq\t%rcx\n    subq\t%rdx, %rsp\n    pushq\t%rdx\n"   
          for (i <- node.getChildren.length-1 to 0){
            asm=asm+evalExpression (node.getChildren()(i), env, params)+"    pushq       %rax\n"
          }
          asm=asm+"    callq       _"+node.getValue+"\n"
          asm=asm+"    addq        $0x"+(node.getChildren.length*4).toHexString+", %rsp\n"
          asm=asm+"    popq\t%rdx\n    addq\t%rdx, %rsp\n"
          asm

        }

      else{
        if (node.getValue=="+") asm=evalOperation(node, "+")
        else if (node.getValue=="-"){
          if (node.getChildren.length==1) 
            asm=evalExpression(node.getChildren()(0), env, params)+"    neg "+spc+"%rax\n"
          else if (node.getChildren.length==2) asm=evalOperation(node,"-")
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
          asm=evalExpression(node.getChildren()(0), env, params)+"    not   "+spc+"%rax\n"
        else if (node.getValue=="!")
          asm=evalExpression(node.getChildren()(0), env, params)+"    cmpq  "+spc+"$0, %rax\n    movq  "+spc+"$0, %rax\n    sete  "+spc+"%al\n"
      }
    // Generate assembly for the  major operations
      def evalOperation (node : ASTNode, op : String) : String ={
        var asm=""
        var spc="      "
        var t1=evalExpression (node.getChildren()(0), env, params)
        asm=t1+"    pushq "+spc+"%rax\n"
        var t2=evalExpression(node.getChildren()(1), env, params)
        asm=asm+t2+"    popq  " + spc+ "%rcx\n"
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
      asm
    }
    def error (err : String = null){
      if (err=="SEMI_COLON") println("ERROR: NO SEMI COLON DETECTED. NOW EXITTING")
      else if (err=="ASSIGNMENT") println("ERROR: INVALID ASSIGNMENT. NOW EXITTING")
      else if (err=="PAREN") println("ERROR: PARENTHESIS INCORRECTLY MATCHED OR WRITTEN. NOW EXITTING")
      else if (err=="CURLY_BRACE") println ("ERROR: CURLY BRACES MISMATCHED. NOW EXITTING")
      else if (err=="DECLARATION") println("ERROR: DECLARATION WITHIN IF/ELSE STATEMENT. NOW EXITTING")
      else if (err=="MULTI_DECLARATION") println("ERROR: VARIABLE DECLARED MULTIPLE TIMES. NOW EXITTING")
      else if (err=="UNDEFINED") println("ERROR: VARIABLE UNDEFINED. NOW EXITTING")
      else if (err=="FUNCTION") println("ERROR: FUNCTION PARAMETERS OR ARGUMENTS DO NOT MATCH")
      else if (err==null) println("UNKNOWN ERROR. NOW EXITTING")
      else println(err)
      System.exit(1)
      
    }
    def printTokens(){
      for (i<-0 to tokens.length-1) 
        println("Token "+i+": " + tokens(i).getValue() + " Type: " + tokens(i).getDtype())
    }

  }
}
