

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
            int begin = temp.indexOf("(");
            int end = temp.indexOf(")");
            function tempF = new function(temp.substring(begin + 1, end));
            double replace = tempF.evaluate(x);
            temp = replaceExpression(temp, begin, end + 1, replace);
        }

        //E

        while(hasChar(temp, "^"))
        {
            int carrot = temp.indexOf("^");
            double before = getNumberBefore(temp, carrot);
            double after = getNumberAfter(temp, carrot);
            double eval = Math.pow(before, after);
            int beginIndex = getIndexNumberBefore(temp, carrot);
            int endIndex = getIndexNumberAfter(temp, carrot);
            temp = replaceExpression(temp, beginIndex, endIndex + 1, eval);
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
        return st;
    }

    private static String replaceExpression(String st, int index1, int index2, double n)
    {
        String temp = st.substring(index1, index2);
        st.replace(temp, Double.toString(n));
        return st;
    }

    private static double getNumberBefore(String st, int index)
    {
        int i = index - 1;
        while(isNumber(st, i) && i >= 0)
        {
            i--;
        }
        String numberText = st.substring(i, index);
        return Double.parseDouble(numberText);
    }

    private static int getIndexNumberBefore(String st, int index)
    {
        int i = index - 1;
        while(isNumber(st, i) && i >= 0)
        {
            i--;
        }
        return i;
    }

    private static double getNumberAfter(String st, int index)
    {
        int i = index + 1;
        while(isNumber(st, i) && i < st.length())
        {
            i++;
        }
        String numberText = st.substring(index + 1, i + 1);
        return Double.parseDouble(numberText);
    }

    private static int getIndexNumberAfter(String st, int index)
    {
        int i = index + 1;
        while(isNumber(st, i) && i < st.length())
        {
            i++;
        }
        return i;
    }

    private static boolean isNumber(String st, int i)
    {
        String num = ch(st, i);
        return num.equals("1") || num.equals("2") || num.equals("3") || num.equals("4") || num.equals("5") || num.equals("6") || num.equals("7") || num.equals("8") || num.equals("9") || num.equals("0") || num.equals(".");
    }
}
