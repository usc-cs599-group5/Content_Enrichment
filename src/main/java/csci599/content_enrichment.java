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
                ttr.parse(new File("D:\\polar-test"));
                break;
            }
            case "geo":
                Geo.extract(new File(args[1]));
                break;
            case "doi":
            {
                DOI_JSON doi=new DOI_JSON();
                doi.generate_urls(new File("D:\\polar-fulldump"));
                doi.generate_JSON();
                break;
            }
        }
    }
}
