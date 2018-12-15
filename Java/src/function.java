public class function
{
    private String formula;

    public function(String input)
    {
        formula = removeSpaces(input);
    }

    public double evaluate(double x)
    {
        String temp = formula;
        temp.replaceAll("x", Double.toString(x));

        //P

        while(hasChar(temp,"("))
        {
            int begin = formula.indexOf("(");
            int end = formula.indexOf(")");
            double fin = 
        }
    }

    private static boolean hasChar(String st, String ch)
    {
        for(int i = 0; i < st.length(); i++)
        {
            if(ch(st, i).equals(ch))
            {
                return true;
            }
        }
        return false;
    }

    private static String ch(String st, int index)
    {
        return st.substring(index, index + 1);
    }

    private static String removeSpaces(String st)
    {
        st.replaceAll(" ", "");
    }
}
