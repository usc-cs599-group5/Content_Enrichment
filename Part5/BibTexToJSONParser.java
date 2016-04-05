import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class BibTexToJSONParser {
    public static void main(String arg[]) throws IOException{
        BibTexToJSONParser f = new BibTexToJSONParser();
        f.formatdata(new File(arg[0]));
    }
    public void formatdata(File f) throws FileNotFoundException, IOException{
        String str;
        BufferedReader in = new BufferedReader(new FileReader(f));
        PrintWriter writer = new PrintWriter("D:\\XMLFiles\\" + f.getName(),"UTF-8");
        while ((str = in.readLine()) != null){
            if(str.contains("@")){
                System.out.println("{");
            }
            else if(str.contains("={")){
                str = str.trim();
                String first_part = "\""+ str.substring(0,str.indexOf("={"));
                first_part +=  "\" : \"" + str.substring(str.indexOf("={") + 2);
                first_part = first_part.replace('}', '\"');
                System.out.println(first_part);
            }
            else if(str.contains("}")){
                System.out.println(str + ",");
            }
            else
                System.out.println(str);
                
        }
    }
}
