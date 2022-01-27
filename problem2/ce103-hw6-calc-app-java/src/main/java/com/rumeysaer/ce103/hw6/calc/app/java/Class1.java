/**
 * @file ce103-hw6-calc-app-java
 * @author Rumeysa ER
 * @date 26 January 2022
 *
 * @brief <b> HW-6 Functions </b>
 *
 * HW-6 Sample Lib Functions
 *
 * @see http://bilgisayar.mmf.erdogan.edu.tr/en/
 *
 */
package com.rumeysaer.ce103.hw6.calc.app.java;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

class RPN
{
    /**
    *
    * @name Calculate
    *     
    * @param [in] input [\b String]
    * It is the method by which the calculation is done in the calculator.
    * It is used to calculate transactions.	  
    **/
    public double Calculate(String input)
    {
	try
	{
	  return Double.parseDouble(GetExpression(input));
	}
	catch (RuntimeException e)
	{
	  return Counting(GetExpression(input));
	}
    }
    
    /**
    *
    * @name roundAvoid
    *     
    * @param [in] value [\b double]
    * Converts trigonometric functions calculated in radians to degrees.  	  
    **/
    public  double roundAvoid(double value){
        int places=3;
        double scale = Math.pow(10,places);
        return Math.round(value * scale) / scale;
    }
    
    /**
    *
    * @name GetExpression
    *     
    * @param [in] input [\b String]
    * It is the function that checks most methods in our class.
    * It checks with loops if other methods are working correctly.
    **/
    public String GetExpression(String input)
    {
	String output = "";
	String fun = "";
	Stack<Character> operStack = new Stack<Character>();
	char k = ' ';
	String p = "";
	for (int z = 0; z < input.length(); z++)
	{
	    if (IsOperator(input.charAt(z)) || Character.isDigit(input.charAt(z)))
	    {
	        if (k == ' ')
		k = input.charAt(z);
		else
		    if (input.charAt(z) == '-' && !Character.isDigit(k))
			p += " 0 ";
		k = input.charAt(z);
	    }
	    p += input.charAt(z);
	}
	input = p;
	for (int z = 0; z < input.length(); z++)
	{
	    if (IsDelimeter(input.charAt(z)))
		continue;
	    if (Character.isDigit(input.charAt(z)))
	    {
		while (!IsDelimeter(input.charAt(z)) && !IsOperator(input.charAt(z)))
		{
		    output += input.charAt(z);
		    z++;
		    if (z == input.length()) break;
		}
		output += " ";
		z--;
	    }
	    else
		if (IsOperator(input.charAt(z)))
		{
		    if (input.charAt(z) == '(')
			operStack.push(input.charAt(z));
		    else if (input.charAt(z) == ')')
		    {
			char s = operStack.pop();
			while (s != '(')
			{
			    output += String.valueOf(s) + ' ';
			    s = operStack.pop();
			}
		    }
		    else
		    {
			if (operStack.size() > 0)
			    if (GetPriority(input.charAt(z)) <= GetPriority(operStack.peek()))
				output += operStack.pop().toString() + " ";
			    operStack.push(input.charAt(z));
                    }
		}
		else if (input.charAt(z) == '\u03C0')
		    output += " \u03C0 ";
		else if (input.charAt(z) == 'e')
		    output += " e ";
		else
		{
		    fun = "";
		    while (input.charAt(z) != '(')
		    {
			fun += input.charAt(z);
			z++;
			if (z == input.length()) break;
		    }
		    z++;
		    if (IsFunction(fun))
		    {
			String param = "";
			while (input.charAt(z) != ')')
			{
			    param += input.charAt(z);
			    z++;
			    if (z == input.length()) break;
			}
			double d;
			try
			{
			    d = Double.parseDouble(param);
			}
			catch (RuntimeException e)
			{
			    d = Counting(GetExpression(param));
			}
			output += doFunc(fun, d);
			}
                }    
        }
	while (operStack.size() > 0)
	    output += operStack.pop() + " ";
		
            return output;
                    
    }
    
    /**
    *
    * @name Counting
    *     
    * @param [in] input [\b String]
    * In this function, multiplication division addition subtraction functions are checked.
    * The return value of the factorial, nPr, etc. functions is checked.
    **/
    public double Counting(String input)
    {
	double result = 0;
	double d = 0;
	Stack<Double> temp = new Stack<Double>();
	try
	{
	  return Double.parseDouble(input);
	}
	catch (RuntimeException e)
	{
	    for (int i = 0; i < input.length(); i++)
	    {
		if (Character.isDigit(input.charAt(i)))
		{
		    String a = "";

		    while (!IsDelimeter(input.charAt(i)) && !IsOperator(input.charAt(i)))
		    {
			a += input.charAt(i);
			i++;
			if (i == input.length()) break;
		    }
		    temp.push(Double.parseDouble(a));
		    i--;
		}
		else if (input.charAt(i) == '\u03C0')
		    temp.push(Math.PI);
		else if (input.charAt(i) == 'e')
		    temp.push(Math.E);
		else if (IsOperator(input.charAt(i)))
		{
		    double a = temp.pop();
		    try
		    {
		      d = temp.pop();
		    }
		    catch (RuntimeException e2)
		    {
		      d = 0;
		    }

		    switch (input.charAt(i))
		    {
			case '!':
			result = factorial((int)a); break;
			case 'P':
			result = factorial((int)d) / factorial((int)(d - a)); break;
			case 'C':
			result = factorial((int)d) / (factorial((int)a) * factorial((int)(d - a))); break;
			case '^':
			result = Math.pow(d,a); break;
			case '%':
			result = d % a; break;
			case '+':
			result = d + a; break;
			case '-':
			result = d - a; break;
			case '*':
			result = d * a; break;
			case '/':
			if (a == 0) throw new DividedByZeroException(); else result = d / a; break;						
		    }
		    temp.push(result);
		}
	    }
	    try
	    {
	      return temp.peek();
	    }
	    catch (RuntimeException e3)
	    {
	      throw new SyntaxException();
            }
	}
    }
    
    /**
    *
    * @name IsDelimeter
    *     
    * @param [in] i [\b char]
    * In this function, it returns false if we give char a value, and true if we set it equal to an equals symbol if we don't.
    **/
    public  boolean IsDelimeter(char i)
    {
	if ((" =".indexOf(i) != -1))
	{
	   return true;
	}
	return false;
    }
    
    /**
    *
    * @name IsOperator
    *     
    * @param [in] b [\b char]
    * This function checks the signs.
    * Returns true for multiplication, division, addition, subtraction signs, and false for anything else you enter.
    * So it checks the signs.
    **/
    public  boolean IsOperator(char b)
    {
	if (("+-/*^()PC!%".indexOf(b) != -1))
	{
	   return true;
	}
	return false;
    }
    
    /**
    *
    * @name IsFunction
    *     
    * @param [in] y [\b String]
    * This function is the one that controls the trigonometric and logarithmic functions.
    * For example, if we write "sin" our function will return true, if we write "si" our function will return false.
    **/
    public boolean IsFunction(String y)
    {
	String[] func = {"sin", "cos", "tg", "asin", "acos", "atg", "sqrt", "ln", "lg"};
	if (Arrays.stream(func).anyMatch(y::equals))
        {
	    return true;
        }		
	return false;
    }
    
    /**
    *
    * @name doFunc
    *     
    * @param [in] func [\b String]
    * @param [in] param [\b double]
    * This function is the one that calculates the logarithmic and trigonometric functions.
    * Thanks to this function, trigonometric and logarithmic functions work.
    * 
    **/
    public String doFunc(String func, double param)
    {     
	switch (func)
	{
	    case "cos":              
		return Double.toString(roundAvoid(Math.cos(Math.PI*param/180)));
            case "sin":
		return Double.toString(roundAvoid(Math.sin(Math.PI*param/180)));
	    case "tg":
		if (Math.abs(param % (2 * Math.PI)) == (Math.PI / 2))
		{
		    throw new TgException(param);
		}
		else
		{
		    return Double.toString(roundAvoid(Math.tan(Math.PI*param/180)));
		}
	    case "asin":
		if (param < -1 || param > 1)
		{
		    throw new ArcSinCosException(param);
		}
		else
		{
		    return Double.toString(roundAvoid(180*Math.asin(param)/Math.PI));
		}
	    case "acos":
		if (param < -1 || param > 1)
		{
		    throw new ArcSinCosException(param);
		}
		else
		{
		    return Double.toString(roundAvoid(180*Math.acos(param)/Math.PI));
		}
	    case "atg":
		return Double.toString(roundAvoid(180*Math.atan(param)/Math.PI));
	    case "sqrt":
		if (param < 0)
		{
		    throw new SqrtException(param);
		}
		else
		{
		    return Double.toString(Math.sqrt(param));
		}
	    case "ln":
		if (param <= 0)
		{
		    throw new LogException(param);
		}
		else
		{
		    return Double.toString(Math.log(param));
		}
	    case "lg":
		if (param <= 0)
		{
		    throw new LogException(param);
		}
		else
		{
		    return Double.toString(Math.log10(param));
		}
	    default:
		return "";
	}
    }
    
    /**
    *
    * @name GetPriority
    *     
    * @param [in] r [\b char]
    * It is the function that sets the process priority.   
    **/
    public byte GetPriority(char r)
    {
            switch (r)
            {
                case '(': return 0;
                case ')': return 1;
                case '+': return 2;
                case '-': return 3;
                case '!': return 4;
                case '%': return 4;
                case '*': return 4;
                case '/': return 4;
                case '^': return 5;
                default: return 4;
            }
    }
    
    /**
    *
    * @name factorial
    *     
    * @param [in] x [\b int]
    * This function shows how the factorial is calculated.
    * It is a function of the factorial.
    * Without this function, factorial operations will not work.
    **/
    public  int factorial(int x)
    {
       int b = 1;
       for (int a = 1; a <= x; a++)
       {
	 b = b * a;
       }
       if (x < 0)
       {
	 throw new NegativeFactorialException(x);
       }
	 return b;
    }
    
    public class MyException extends RuntimeException
    {
	public String type;
    }
    
    public  class NegativeFactorialException extends MyException
    {
        /**
        *
        * @name NegativeFactorialException
        *     
        * @param [in] z [\b int]
        * @param [in] y [\b int]
        **/
	public NegativeFactorialException(int z)
        {
	    this.type = "Math error";
                Object[] options = {"OK"};
            int y = JOptionPane.showOptionDialog(new JFrame(),
                   "Factorial(" + z + ") does not exsists ","Math error",
                   JOptionPane.PLAIN_MESSAGE,
                   JOptionPane.QUESTION_MESSAGE,
                   null,
                   options,
                   options[0]);
	}
    }
    
    public  class TgException extends MyException
    {
        /**
        *
        * @name TgException
        *     
        * @param [in] z [\b double]
        * @param [in] y [\b int]
        **/
	public TgException(double z)
	{
            this.type = "Math error";
                Object[] options = {"OK"};
            int y = JOptionPane.showOptionDialog(new JFrame(),
                    "Tg(" + z + ") does not exsists","Math Error",
                     JOptionPane.PLAIN_MESSAGE,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     options,
                     options[0]);			
	}
    }
    
    public class SqrtException extends MyException
    {
        /**
        *
        * @name SqrtException
        *     
        * @param [in] z [\b double]
        * @param [in] y [\b int]
        **/
	public SqrtException(double z)
	{
            this.type = "Math error";
                Object[] options = {"OK"};
            int y = JOptionPane.showOptionDialog(new JFrame(),
                    "Sqrt(" + z + ") does not exsists","Math Error",
                     JOptionPane.PLAIN_MESSAGE,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     options,
                     options[0]);			
	}
    }
    
    public class DividedByZeroException extends MyException
    {
        /**
        *
        * @name DividedByZeroException
        *     
        * @param [in] y [\b int]
        **/
        public DividedByZeroException()
	{
            this.type = "Math error";
                Object[] options = {"OK"};
            int y = JOptionPane.showOptionDialog(new JFrame(),
                    "Division by zero is impossible","Math Error",
                     JOptionPane.PLAIN_MESSAGE,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     options,
                     options[0]);			
	}
    }
    
    public class LogException extends MyException
    {
        /**
        *
        * @name LogException
        *     
        * @param [in] z [\b double]
        * @param [in] y [\b int]
        **/
	public LogException(double z)
	{
            
            this.type = "Math error";
                Object[] options = {"OK"};
            int y = JOptionPane.showOptionDialog(new JFrame(),
                    "Log(" + z + ") does not exsists","Math Error",
                     JOptionPane.PLAIN_MESSAGE,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     options,
                     options[0]);			
	}
    }
    
    public class SyntaxException extends MyException
    {
        /**
        *
        * @name SyntaxException
        *     
        * @param [in] y [\b int]
        **/
        public SyntaxException()
	{
            this.type = "Math error";
                Object[] options = {"OK"};
            int y = JOptionPane.showOptionDialog(new JFrame(),
                    "You made a mistake","Math Error",
                     JOptionPane.PLAIN_MESSAGE,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     options,
                     options[0]);			
	}
    }
    
    public class ArcSinCosException extends MyException
    {
        /**
        *
        * @name ArcSinCosException
        *     
        * @param [in] z [\b double]
        * @param [in] y [\b int]
        **/
	public ArcSinCosException(double z)
	{
            this.type = "Math error";
                Object[] options = {"OK"};
            int y = JOptionPane.showOptionDialog(new JFrame(),
                    "Acos(or Asin) (" + z + ") does not exsists","Math Error",
                     JOptionPane.PLAIN_MESSAGE,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     options,
                     options[0]);			
	}
    
    }    
}

