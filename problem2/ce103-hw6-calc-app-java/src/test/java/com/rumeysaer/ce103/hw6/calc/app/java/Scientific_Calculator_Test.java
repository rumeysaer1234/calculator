/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.rumeysaer.ce103.hw6.calc.app.java;
import org.junit.jupiter.api.Test;



/**
 *
 * @author Lenovo
 */
public class Scientific_Calculator_Test {
    
    public Scientific_Calculator_Test() {
    }
      private RPN rpn = new RPN();
      
      @Test
      public void TestMethod1()
      {
        assert rpn.GetExpression("9*8").equals("9 8 * ");
	assert rpn.GetExpression("3*5").equals("3 5 * ");
	assert rpn.GetExpression("8 / 4 * 4").equals("8 4 / 4 * ");
      }
      
      @Test
      public void TestMethod2()
      { 
        assert rpn.IsDelimeter((char)15) == false;
	assert rpn.IsDelimeter((char)34) == false;
	assert rpn.IsDelimeter((char)'=') == true;
      }
      
      @Test
      public void TestMethod3()
      { 
        assert rpn.IsOperator((char)'d') == false;
        assert rpn.IsOperator((char)12) == false;
        assert rpn.IsOperator((char)'/') == true;
      }
      
      @Test
      public void TestMethod4()
      {
        assert rpn.IsFunction("ta") == false;
        assert rpn.IsFunction("si") == false;
        assert rpn.IsFunction("cos") == true;
      }
      
      @Test
      public void TestMethod5()
      {
        assert rpn.factorial(7)== 5040;
        assert rpn.factorial(5)== 120;
        assert rpn.factorial(12)== 479001600;
      }
      
      @Test
      public void TestMethod6()
      {
        assert rpn.doFunc("cos",90).equals("0.0");
        assert rpn.doFunc("tan",10).equals("");
        assert rpn.doFunc("asin",1).equals("90.0");
      }
      
      @Test
      public void TestMethod7()
      {
        assert rpn.Calculate("lg(100)")== 2;
        assert rpn.Calculate("sqrt(2)")== 1.4142135623730951;
        assert rpn.Calculate("sin(30)")== 0.5;
      }
    
    
}
