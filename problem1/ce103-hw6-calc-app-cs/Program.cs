using System;
using System.Windows.Forms;

namespace ce103_hw6_calc_app_cs
{
    static class Program
    {
        [STAThread]
        static void Main() // Entry point
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new CalcForm());
        }
    }
}
