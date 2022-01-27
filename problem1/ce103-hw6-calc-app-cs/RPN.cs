/**
 * @file ce103-hw6-calc-app-cs
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
using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.Windows.Forms;

namespace ce103_hw6_calc_app_cs
{
    public class RPN
    {

        /**
        *
        * @name Calculate
        *     
        * @param [in] input [\b String]
        * It is the method by which the calculation is done in the calculator.
        * It is used to calculate transactions.	  
        **/
        public double Calculate(string input)
        {
            try { return double.Parse(GetExpression(input)); }
            catch (Exception) { return Counting(GetExpression(input)); }

        }

        /**
        *
        * @name GetExpression
        *     
        * @param [in] input [\b String]
        * It is the function that checks most methods in our class.
        * It checks with loops if other methods are working correctly.
        **/
        public string GetExpression(string input)
        {
            string output = string.Empty;
            string fun = string.Empty;
            Stack<char> operStack = new Stack<char>();
            char k = ' '; string p = "";
            for (int i = 0; i < input.Length; i++)
            {
                if (IsOperator(input[i]) || Char.IsDigit(input[i]))
                {
                    if (k == ' ')
                        k = input[i];
                    else
                        if (input[i] == '-' && !Char.IsDigit(k))
                        p += " 0 ";
                    k = input[i];
                }
                p += input[i];
            }
            input = p;
            for (int i = 0; i < input.Length; i++)
            {
                if (IsDelimeter(input[i]))
                    continue;
                if (Char.IsDigit(input[i]))
                {
                    while (!IsDelimeter(input[i]) && !IsOperator(input[i]))
                    {
                        output += input[i];
                        i++;
                        if (i == input.Length) break;
                    }
                    output += " ";
                    i--;
                }
                else
                    if (IsOperator(input[i]))
                {
                    if (input[i] == '(')
                        operStack.Push(input[i]);
                    else if (input[i] == ')')
                    {
                        char s = operStack.Pop();
                        while (s != '(')
                        {
                            output += s.ToString() + ' ';
                            s = operStack.Pop();
                        }
                    }
                    else
                    {
                        if (operStack.Count > 0)
                            if (GetPriority(input[i]) <= GetPriority(operStack.Peek()))
                                output += operStack.Pop().ToString() + " ";

                        operStack.Push(char.Parse(input[i].ToString()));

                    }
                }
                else if (input[i] == '\u03C0')
                    output += " \u03C0 ";
                else if (input[i] == 'e')
                    output += " e ";
                else
                {
                    fun = String.Empty;
                    while (input[i] != '(')
                    {
                        fun += input[i];
                        i++;
                        if (i == input.Length) break;
                    }
                    i++;
                    if (IsFunction(fun))
                    {
                        String param = String.Empty;
                        while (input[i] != ')')
                        {
                            param += input[i];
                            i++;
                            if (i == input.Length) break;
                        }
                        double d;
                        try { d = double.Parse(param); }
                        catch (Exception) { d = Counting(GetExpression(param)); }
                        output += doFunc(fun, d);
                    }
                }
            }
            while (operStack.Count > 0)
                output += operStack.Pop() + " ";
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
        public double Counting(string input)
        {
            double result = 0;
            double b = 0;
            Stack<double> temp = new Stack<double>();
            try { return double.Parse(input); }
            catch (Exception)
            {
                for (int i = 0; i < input.Length; i++)
                {
                    if (Char.IsDigit(input[i]))
                    {
                        string a = string.Empty;

                        while (!IsDelimeter(input[i]) && !IsOperator(input[i]))
                        {
                            a += input[i];
                            i++;
                            if (i == input.Length) break;
                        }
                        temp.Push(double.Parse(a));
                        i--;
                    }
                    else if (input[i] == '\u03C0')
                        temp.Push(Math.PI);
                    else if (input[i] == 'e')
                        temp.Push(Math.E);
                    else if (IsOperator(input[i]))
                    {
                        double a = temp.Pop();
                        try
                        { b = temp.Pop(); }
                        catch (Exception) { b = 0; }

                        switch (input[i])
                        {
                            case '!': result = factorial((int)a); break;
                            case 'P': result = factorial((int)b) / factorial((int)(b - a)); break;
                            case 'C': result = factorial((int)b) / (factorial((int)a) * factorial((int)(b - a))); break;
                            case '^': result = Math.Pow(b, a); break;
                            case '%': result = b % a; break;
                            case '+': result = b + a; break;
                            case '-': result = b - a; break;
                            case '*': result = b * a; break;
                            case '/': if (a == 0) throw new DividedByZeroException(); else result = b / a; break;
                        }
                        temp.Push(result);
                    }
                }
                try { return temp.Peek(); }
                catch (Exception) { throw new SyntaxException(); }

            }

        }

        /**
        *
        * @name IsDelimeter
        *     
        * @param [in] a [\b char]
        * In this function, it returns false if we give char a value, and true if we set it equal to an equals symbol if we don't.
        **/
        public bool IsDelimeter(char c)
        {
            if ((" =".IndexOf(c) != -1))
                return true;
            return false;
        }

        /**
        *
        * @name IsOperator
        *     
        * @param [in] a [\b char]
        * This function checks the signs.
        * Returns true for multiplication, division, addition, subtraction signs, and false for anything else you enter.
        * So it checks the signs.
        **/
        public bool IsOperator(char с)
        {
            if (("+-/*^()PC!%".IndexOf(с) != -1))
                return true;
            return false;
        }

        /**
        *
        * @name IsFunction
        *     
        * @param [in] s [\b String]
        * This function is the one that controls the trigonometric and logarithmic functions.
        * For example, if we write "sin" our function will return true, if we write "si" our function will return false.
        **/
        public bool IsFunction(String s)
        {
            String[] func = { "sin", "cos", "tg", "asin", "acos", "atg", "sqrt", "ln", "lg" };
            if (Array.Exists(func, e => e == s))
                return true;
            return false;
        }

        /**
        *
        * @name doFunc
        *     
        * @param [in] fun [\b String]
        * @param [in] param [\b double]
        * This function is the one that calculates the logarithmic and trigonometric functions.
        * Thanks to this function, trigonometric and logarithmic functions work.
        * 
        **/
        public String doFunc(String fun, double param)
        {
            switch (fun)
            {
                case "cos": return Math.Round(Math.Cos(Math.PI * param / 180), 3, MidpointRounding.AwayFromZero).ToString();
                case "sin": return Math.Round(Math.Sin(Math.PI * param / 180), 3, MidpointRounding.AwayFromZero).ToString();
                case "tg": if (Math.Abs(param % (2 * Math.PI)) == (Math.PI / 2)) throw new TgException(param); else return Math.Round(Math.Tan(Math.PI * param / 180), 3, MidpointRounding.AwayFromZero).ToString();
                case "asin": if (param < -1 || param > 1) throw new ArcSinCosException(param); else return Math.Round(180 * Math.Asin(param) / Math.PI, 3, MidpointRounding.AwayFromZero).ToString();
                case "acos": if (param < -1 || param > 1) throw new ArcSinCosException(param); else return Math.Round(180 * Math.Acos(param) / Math.PI, 3, MidpointRounding.AwayFromZero).ToString();
                case "atg": return Math.Round(180 * Math.Atan(param) / Math.PI, 3, MidpointRounding.AwayFromZero).ToString();
                case "sqrt": if (param < 0) throw new SqrtException(param); else return Math.Sqrt(param).ToString();
                case "ln": if (param <= 0) throw new LogException(param); else return Math.Log(param).ToString();
                case "lg": if (param <= 0) throw new LogException(param); else return Math.Log10(param).ToString();
                default: return "";
            }
        }


        /**
        *
        * @name GetPriority
        *     
        * @param [in] s [\b char]
        * It is the function that sets the process priority.   
        **/
        public byte GetPriority(char s)
        {
            switch (s)
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
        public int factorial(int x)
        {
            int i = 1;
            for (int s = 1; s <= x; s++)
                i = i * s;
            if (x < 0) throw new NegativeFactorialException(x);
            return i;
        }
    }
    public class MyException : Exception
    {
        public string type;
    }
    public class NegativeFactorialException : MyException
    {

        /**
        *
        * @name NegativeFactorialException
        *     
        * @param [in] x [\b int]
        **/
        public NegativeFactorialException(int x)
        {
            this.type = "Math error";
            MessageBox.Show("Factorial(" + x + ") does not exsists", type, MessageBoxButtons.OK);
        }
    }
    public class TgException : MyException
    {

        /**
        *
        * @name TgException
        *     
        * @param [in] x [\b double]
        **/
        public TgException(double x)
        {
            this.type = "Math error";
            MessageBox.Show("Tg(" + x + ") does not exsists", type, MessageBoxButtons.OK);
        }
    }
    public class SqrtException : MyException
    {

        /**
        *
        * @name SqrtException
        *     
        * @param [in] x [\b double]
        **/
        public SqrtException(double x)
        {
            this.type = "Math error";
            MessageBox.Show("Sqrt(" + x + ") does not exsists", type, MessageBoxButtons.OK);
        }
    }
    public class DividedByZeroException : MyException
    {

        /**
        * @name DividedByZeroException
        **/
        public DividedByZeroException()
        {
            this.type = "Math error";
            MessageBox.Show("Division by zero is impossible", type, MessageBoxButtons.OK);
        }
    }
    public class LogException : MyException
    {

        /**
        *
        * @name LogException
        *     
        * @param [in] x [\b double]
        **/
        public LogException(double x)
        {
            this.type = "Math error";
            MessageBox.Show("Log(" + x + ") does not exsists", type, MessageBoxButtons.OK);
        }
    }
    public class SyntaxException : MyException
    {

        /**
        * @name SyntaxException
        **/
        public SyntaxException()
        {
            this.type = "Syntax error";
            MessageBox.Show("You made a mistake", type, MessageBoxButtons.OK);
        }
    }
    public class ArcSinCosException : MyException
    {

        /**
        *
        * @name ArcSinCosException
        *     
        * @param [in] x [\b double]
        **/
        public ArcSinCosException(double x)
        {
            this.type = "Math error";
            MessageBox.Show("Acos(or Asin) (" + x + ") does not exsists", type, MessageBoxButtons.OK);
        }
    }
}
