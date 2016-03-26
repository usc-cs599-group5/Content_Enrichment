package csci599;
import org.apache.tika.Tika;
import java.io.File;
import java.io.IOException;

public class content_enrichment
{
   
   public static void main(String args[]) throws IOException
   {
        switch(args[0])
        {
            case "ttr":
            {
                TTRParser ttr=new TTRParser();
                ttr.parse();
                break;
            }
            case "geo":
                Geo.extract(new File(args[1]));
                break;
        }
    }
}
