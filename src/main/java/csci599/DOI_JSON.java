package csci599;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

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
                urls.add(file.getName());
            }
        }
    }
    
    public void generate_JSON()
    {
        Iterator i=urls.iterator();
        while(i.hasNext())
        {
            System.out.println(i.next());
        }
    }
}
