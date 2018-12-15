import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class runner
{
    public static void main(String[] args) throws IOException
    {
        boolean cancel = false;
        function k = new function("x^2");
        while(!cancel)
        {
            Scanner scan = new Scanner(System.in);
            String command = scan.nextLine();
            if(command.equals("STOP"))
            {
                cancel = true;
            }
            Runtime.getRuntime().exec(command);
        }
    }
}
