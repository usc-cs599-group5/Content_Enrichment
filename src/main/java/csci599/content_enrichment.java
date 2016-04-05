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
            case "copy":
            {
                copyFiles copyf = new copyFiles();
                copyf.copy(new File(args[1]),new File(args[2]));
                break;
            }
            case "ttr":
            {
                TTRParser ttr=new TTRParser();
                ttr.parse(new File(args[1]));
                break;
            }
            case "geo":
                Geo.extract(new File(args[1]));
                break;
            case "doi":
            {
                DOI_JSON doi=new DOI_JSON();
                doi.generate_urls(new File(args[1]));
                doi.generate_JSON();
                break;
            }
            case "tJParser":
            {
                textToJSONParser tr = new textToJSONParser();
                tr.generateJSON7(new File(args[1]));
                break;
            }
        }
    }
}
