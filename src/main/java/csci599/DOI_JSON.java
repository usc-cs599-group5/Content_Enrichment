package csci599;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class DOI_JSON
{
    HashSet urls;
    
    DOI_JSON()
    {
        urls=new HashSet<String>();
    }
    
    public void generate_urls(File folder)
    {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                generate_urls(file);
            } else {
                System.out.println(file.getAbsolutePath().substring(3));
                urls.add(file.getAbsolutePath().substring(3));
            }
        }
    }
    
    public void generate_JSON()
    {
        /*Iterator i=urls.iterator();
        while(i.hasNext())
        {
            System.out.println(i.next());
        }*/
        try {
            new ObjectMapper().writeValue(new File("DOI_URLs.json"), urls);
            System.out.println("DOI_URLs.json created");
        } catch (IOException ex) {
            System.err.println("Error writing DOI_URLs.json");
        }
    }
}
