import java.util.ArrayList;

public class function
{
    private String formula;
    private ArrayList<Double> xV;
    private ArrayList<Double> yV;

    public function(String input)
    {
        formula = removeSpaces(input);
    }

    private function(function other)
    {
        formula = removeSpaces(other.toString());
    }

    public function(ArrayList<Double> xVals, ArrayList<Double> yVals, int p)
    {
        xV = new ArrayList<>();
        yV = new ArrayList<>();
        for(int i = 0; i < xVals.size(); i++)
        {
            xV.add(xVals.get(i));
            yV.add(yVals.get(i));
        }
        formula = estimateFunctionFromList(p);

    }

    public double derivative(double x, double precision)
    {
        double k = .0001/precision;
        return (evaluate(x + k) - evaluate(x))/k;

    }

    public function derivative(double x1, double x2, int intervals, int precision, int p)
    {
        double len = (x2 - x1)/intervals;

        ArrayList<Double> yVals = new ArrayList<>();
        ArrayList<Double> xVals = new ArrayList<>();
        for(int i = 0; i < intervals; i++)
        {
            xVals.add(x1 + len * i);
            yVals.add(derivative(xVals.get(i),precision));
        }

        return new function(xVals, yVals, p);
    }

    public double evaluate(double x)
    {

        String temp = formula;
        temp = temp.replaceAll("x", Double.toString(x));
        temp = fixSub(temp);


        //P

        while(hasChar(temp,"("))
        {
            int begin = temp.indexOf("(");
            int end = temp.indexOf(")");
            function tempF = new function(temp.substring(begin + 1, end));
            double replace = tempF.evaluate(x);
            temp = replaceExpression(temp, begin, end + 1, replace);
        }

        while(hasChar(temp,"L"))
        {
            int begin = temp.indexOf("n");
            int end = getIndexNumberAfter(temp,begin);

            double replace = Math.log(getNumberAfter(temp,begin));
            temp = replaceExpression(temp, begin - 1, end + 1, replace);
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

        //M

        while(hasChar(temp, "*"))
        {
            int star = temp.indexOf("*");
            double before = getNumberBefore(temp, star);
            double after = getNumberAfter(temp, star);
            double eval = before * after;
            int beginIndex = getIndexNumberBefore(temp, star);
            int endIndex = getIndexNumberAfter(temp, star);
            temp = replaceExpression(temp, beginIndex, endIndex + 1, eval);
        }

        //D
        while(hasChar(temp, "/"))
        {
            int slash = temp.indexOf("/");
            double before = getNumberBefore(temp, slash);
            double after = getNumberAfter(temp, slash);
            double eval = before / after;
            int beginIndex = getIndexNumberBefore(temp, slash);
            int endIndex = getIndexNumberAfter(temp, slash);
            temp = replaceExpression(temp, beginIndex, endIndex + 1, eval);
        }

        //A

        while(hasChar(temp, "+"))
        {
            int plus = temp.indexOf("+");
            double before = getNumberBefore(temp, plus);
            double after = getNumberAfter(temp, plus);
            double eval = before + after;
            int beginIndex = getIndexNumberBefore(temp, plus);
            int endIndex = getIndexNumberAfter(temp, plus);
            temp = replaceExpression(temp, beginIndex, endIndex + 1, eval);
        }

        //S
        while(hasChar(temp, "s"))
        {
            int dash = temp.indexOf("s");
            double before = getNumberBefore(temp, dash);
            double after = getNumberAfter(temp, dash);
            double eval = before - after;
            int beginIndex = getIndexNumberBefore(temp, dash);
            int endIndex = getIndexNumberAfter(temp, dash);
            temp = replaceExpression(temp, beginIndex, endIndex + 1, eval);
        }

        return Double.parseDouble(temp);
    }

    public String toString()
    {
        return formula;
    }

    public function invert(double x1, double x2, int intervals)
    {
        double len = (x2 - x1)/intervals;
        ArrayList<Double> xVals = new ArrayList<>();
        ArrayList<Double> yVals = new ArrayList<>();
        for(int i = 0; i < intervals; i++)
        {
            yVals.add(x1 + len * i);
            xVals.add(evaluate(yVals.get(i)));
        }
        function temp = new function(xVals, yVals, 6);
        return temp;
    }

    public function add(function other)
    {
        String temp = "(" + formula + ")" + "+" + "(" + other.toString() + ")";
        return new function(temp);
    }

    public function subtract(function other)
    {
        String temp = "(" + formula + ")" + "-" + "(" + other.toString() + ")";
        return new function(temp);
    }

    public function multiply(function other)
    {
        String temp = "(" + formula + ")" + "*" + "(" + other.toString() + ")";
        return new function(temp);
    }

    public function divide(function other)
    {
        String temp = "(" + formula + ")" + "/" + "(" + other.toString() + ")";
        return new function(temp);
    }

    public function estimate(double x1, double x2, int intervals)
    {
        double len = (x2 - x1)/intervals;
        double[] yVals = new double[intervals];
        double[] xVals = new double[intervals];
        for(int i = 0; i < intervals; i++)
        {
            xVals[i] = x1 + len * (double)i;
            yVals[i] = evaluate(xVals[i]);
        }

        double[] rs = new double[10];
        double[] yValsTemp = new double[yVals.length];
        double[] xValsTemp = new double[xVals.length];
        rs[0] = getR(xVals,yVals);
        for(int i = 0; i < yVals.length; i++)
        {
            yValsTemp[i] = Math.exp(yVals[i]);
        }
        rs[1] = getR(xVals,yValsTemp);
        for(int i = 0; i < yVals.length; i++)
        {
            yValsTemp[i] = Math.log(yVals[i]);
        }
        rs[2] = getR(xVals,yValsTemp);
        for(int i = 0; i < yVals.length; i++)
        {
            yValsTemp[i] = Math.log(yVals[i]);
            xValsTemp[i] = Math.log(xVals[i]);
        }
        rs[3] = getR(xValsTemp,yValsTemp);
        //log
        for(int i = 0; i < yVals.length; i++)
        {
            xValsTemp[i] = Math.exp(xVals[i]);
        }
        rs[4] = getR(xValsTemp,yVals);
        //exp
        for(int i = 0; i < yVals.length; i++)
        {
            xValsTemp[i] = Math.log(xVals[i]);
        }
        rs[5] = getR(xValsTemp, yVals);


        boolean[] user = generateRegArray(rs);



        //Linear

        if(user[0])
        {

            double slope = getR(xVals,yVals) * STDEV(yVals) / STDEV(xVals);
            double b = MEAN(yVals) - slope * MEAN(xVals);
            return new function(round(slope,6) + "*x+" + round(b,6));
        }

        //Logarithmic

        if(user[1])
        {

            double slope = getR(xVals,yValsTemp) * STDEV(yValsTemp) / STDEV(xVals);
            double b = MEAN(yValsTemp) - slope * MEAN(xVals);
            return new function("Ln(" + round(slope,6) + "*x+" + round(b,6) + ")");
        }

        //Exponential
        if(user[2])
        {

            double slope = getR(xVals,yValsTemp) * STDEV(yValsTemp) / STDEV(xVals);
            double b = MEAN(yValsTemp) - slope * MEAN(xVals);
            return new function(round(Math.exp(slope),6) + "^x*" + round(Math.exp(b),6));
        }

        //Power


        if(user[3])
        {

            double slope = getR(xValsTemp,yValsTemp) * STDEV(yValsTemp) / STDEV(xValsTemp);
            double b = MEAN(yValsTemp) - slope * MEAN(xValsTemp);
            return new function("x^" + round(slope,6) + "*" + round(Math.exp(b),6));
        }

        if(user[4])
        {
            double slope = getR(xValsTemp,yValsTemp) * STDEV(yValsTemp) / STDEV(xValsTemp);
            double b = MEAN(yValsTemp) - slope * MEAN(xValsTemp);
            return new function(slope + "*e^x+" + b);
        }

        if(user[5])
        {
            double slope = getR(xValsTemp,yValsTemp) * STDEV(yValsTemp) / STDEV(xValsTemp);
            double b = MEAN(yValsTemp) - slope * MEAN(xValsTemp);
            return new function(slope + "*Ln(x)+" + b);
        }
        return null;
    }

    public function powerSeries(int degree, int x1, int x2, int intervals, int precision, int p, double center)
    {
        ArrayList<Double> coefficients = new ArrayList<>();
        String series = "";
        function temp = new function(this);
        for(int i = 0; i < degree; i++)
        {
            series += temp.evaluate(center) + "/" + i + "!*(x-" + center + ")^" + i + "+";
            temp = temp.derivative(x1, x2, intervals, precision, p);
        }
        series += temp.evaluate(center) + "/" + degree + "!*(x-" + center + ")^" + degree;
        return new function(series);
    }

    public String estimateFunctionFromList(int p)
    {
        int size = xV.size();
        double[] xVals = new double[size];
        double[] yVals = new double[size];

        for(int i = 0; i < size; i++)
        {
            xVals[i] = xV.get(i);
            yVals[i] = yV.get(i);
        }
        double[] rs = new double[10];
        double[] yValsTemp = new double[yVals.length];
        double[] xValsTemp = new double[xVals.length];
        //linear
        rs[0] = getR(xVals,yVals);
        //exp
        for(int i = 0; i < yVals.length; i++)
        {
            yValsTemp[i] = Math.exp(yVals[i]);
        }
        rs[1] = getR(xVals,yValsTemp);
        //log
        for(int i = 0; i < yVals.length; i++)
        {
            yValsTemp[i] = Math.log(yVals[i]);
        }
        rs[2] = getR(xVals,yValsTemp);
        //power
        for(int i = 0; i < yVals.length; i++)
        {
            yValsTemp[i] = Math.log(yVals[i]);
            xValsTemp[i] = Math.log(xVals[i]);
        }
        rs[3] = getR(xValsTemp,yValsTemp);
        //log
        for(int i = 0; i < yVals.length; i++)
        {
            xValsTemp[i] = Math.exp(xVals[i]);
        }
        rs[4] = getR(xValsTemp,yVals);
        //exp
        for(int i = 0; i < yVals.length; i++)
        {
            xValsTemp[i] = Math.log(xVals[i]);
        }
        rs[5] = getR(xValsTemp, yVals);
        boolean[] user = generateRegArray(rs);


        //Linear

        if(user[0])
        {

            double slope = getR(xVals,yVals) * STDEV(yVals) / STDEV(xVals);
            double b = MEAN(yVals) - slope * MEAN(xVals);
            return round(slope,p) + "*x+" + round(b,p);
        }

        //Logarithmic

        if(user[1])
        {

            double slope = getR(xVals,yValsTemp) * STDEV(yValsTemp) / STDEV(xVals);
            double b = MEAN(yValsTemp) - slope * MEAN(xVals);
            return "Ln(" + round(slope,p) + "*x+" + round(b,p) + ")";
        }

        //Exponential
        if(user[2])
        {

            double slope = getR(xVals,yValsTemp) * STDEV(yValsTemp) / STDEV(xVals);
            double b = MEAN(yValsTemp) - slope * MEAN(xVals);
            return round(Math.exp(slope),p) + "^x*" + round(Math.exp(b),p);
        }

        //Power


        if(user[3])
        {

            double slope = getR(xValsTemp,yValsTemp) * STDEV(yValsTemp) / STDEV(xValsTemp);
            double b = MEAN(yValsTemp) - slope * MEAN(xValsTemp);
            return "x^" + round(slope,p) + "*" + round(Math.exp(b),p);
        }

        if(user[4])
        {
            double slope = getR(xValsTemp,yValsTemp) * STDEV(yValsTemp) / STDEV(xValsTemp);
            double b = MEAN(yValsTemp) - slope * MEAN(xValsTemp);
            return slope + "*e^x+" + b;
        }

        if(user[5])
        {
            double slope = getR(xValsTemp,yValsTemp) * STDEV(yValsTemp) / STDEV(xValsTemp);
            double b = MEAN(yValsTemp) - slope * MEAN(xValsTemp);
            return slope + "*Ln(x)+" + b;
        }
        return null;
    }

    private static boolean[] generateRegArray(double[] rs)
    {
        double max = 0;
        int maxNum = 0;
        for(int i = 0; i < rs.length; i++)
        {
            if(rs[i] > max)
            {
                maxNum = i;
                max = round(rs[i],9);
            }
        }
        boolean[] toReturn = new boolean[rs.length];
        toReturn[maxNum] = true;
        return toReturn;

    }

    private static String fixSub(String temp)
    {
        while(hasChar(temp, "-"))
        {
            int index = temp.indexOf("-");
            if(index != 0 && (isNumber(temp, index - 1) || ch(temp, index - 1).equals(")")))
            {
                temp = temp.substring(0, index) + "s" + temp.substring(index + 1,temp.length());
            }
            else
            {
                temp = temp.substring(0, index) + "a" + temp.substring(index + 1,temp.length());
            }

        }
        temp = temp.replaceAll("a", "-");
        return temp;
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
        st = st.replaceAll(" ", "");
        return st;
    }

    private static String replaceExpression(String st, int index1, int index2, double n)
    {
        String temp = st.substring(index1, index2);
        st = st.replace(temp, Double.toString(n));
        return st;
    }

    private static double getNumberBefore(String st, int index)
    {
        int i;
        for(i = index - 1; i >= 0 && isNumber(st,i); i--)
        {

        }
        String numberText = st.substring(i + 1, index);

        return Double.parseDouble(numberText);
    }

    private static int getIndexNumberBefore(String st, int index)
    {
        int i;
        for(i = index - 1; i >= 0 && isNumber(st,i); i--)
        {

        }
        return i + 1;
    }

    private static double getNumberAfter(String st, int index)
    {
        int i;

        for(i = index + 1; i < st.length() && isNumber(st,i); i++)
        {

        }
        String numberText = st.substring(index + 1, i);
        return Double.parseDouble(numberText);
    }

    private static int getIndexNumberAfter(String st, int index)
    {
        int i;
        for(i = index + 1; i < st.length() && isNumber(st,i); i++)
        {

        }
        return i - 1;
    }

    private static boolean isNumber(String st, int i)
    {
        String num = ch(st, i);
        return num.equals("1") || num.equals("2") || num.equals("3") || num.equals("4") || num.equals("5") || num.equals("6") || num.equals("7") || num.equals("8") || num.equals("9") || num.equals("0") || num.equals(".") || num.equals("-") || num.equals("E");
    }

    private static double MEAN(double[] vals)
    {
        double sum = 0;
        for(int i = 0; i < vals.length; i++)
        {
            sum += vals[i];
        }
        return sum/vals.length;
    }

    private static double STDEV(double[] vals)
    {
        double sum = 0;
        double mean = MEAN(vals);
        for(int i = 0; i < vals.length; i++)
        {
            sum += (vals[i] - mean) * (vals[i] - mean);
        }
        return Math.sqrt(sum/(vals.length - 1));
    }

    private static double getR(double[] xVals, double[] yVals)
    {
        double sum = 0;

        double Sx = STDEV(xVals);
        double Sy = STDEV(yVals);
        double xMean = MEAN(xVals);
        double yMean = MEAN(yVals);
        for(int i = 0; i < xVals.length; i++)
        {
            sum += (xVals[i] - xMean)*(yVals[i] - yMean)/(Sx * Sy);
        }
        return sum/(xVals.length-1);
    }

    public static String arrayToString(boolean[] array)
    {
        String toReturn = "";
        for (int i = 0; i < array.length; i++)
        {
            toReturn += array[i] + " ";
        }
        return toReturn;
    }

    public static String arrayToString(double[] array)
    {
        String toReturn = "";
        for (int i = 0; i < array.length; i++)
        {
            toReturn += array[i] + " ";
        }
        return toReturn;
    }

    public static double round(double num, int place)
    {
        num = num * Math.pow(10, place);
        num = Math.round(num);
        num = num / Math.pow(10, place);
        return num;
    }
}
