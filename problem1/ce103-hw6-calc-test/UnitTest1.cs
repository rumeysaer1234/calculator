using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using ce103_hw6_calc_app_cs;

namespace ce103_hw6_calc_test
{
    [TestClass]
    public class UnitTest1
    {
        private RPN rpn = new RPN();
        [TestMethod]
        public void TestMethod1()
        {
            Assert.AreEqual(rpn.GetExpression("9 / 7 * 7"), "9 7 / 7 * ");
            Assert.AreEqual(rpn.GetExpression("5*6"), "5 6 * ");
        }
        [TestMethod]
        public void TestMethod2()
        {
            Assert.AreEqual(rpn.IsDelimeter(Convert.ToChar("=")), true);
            Assert.AreEqual(rpn.IsDelimeter(Convert.ToChar(1)), false);

        }
        [TestMethod]
        public void TestMethod3()
        {
            Assert.AreEqual(rpn.IsOperator(Convert.ToChar("j")), false);
            Assert.AreEqual(rpn.IsOperator(Convert.ToChar("/")), true);
            Assert.AreEqual(rpn.IsOperator(Convert.ToChar(8)), false);
        }
        [TestMethod]
        public void TestMethod4()
        {
            Assert.AreEqual(rpn.IsFunction("sinx"), false);
            Assert.AreEqual(rpn.IsFunction("lg"), true);
            Assert.AreEqual(rpn.IsFunction("cosy"), false);


        }
        [TestMethod]
        public void TestMethod5()
        {
            Assert.AreEqual(rpn.factorial(2), 2);
            Assert.AreEqual(rpn.factorial(7), 5040);
            Assert.AreEqual(rpn.factorial(3), 6);


        }
        [TestMethod]
        public void TestMethod6()
        {
            Assert.AreEqual(rpn.doFunc("sin", 0), "0");
            Assert.AreEqual(rpn.doFunc("cot", 10), "");
            Assert.AreEqual(rpn.doFunc("acos", 1), "0");


        }
        [TestMethod]
        public void TestMethod7()
        {
            Assert.AreEqual(rpn.Calculate("tg(40)"), 0.839);
            Assert.AreEqual(rpn.Calculate("lg(100)"), 2);

        }
    }
}
