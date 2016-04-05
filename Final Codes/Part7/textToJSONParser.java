package csci599;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.tika.Tika;

public class textToJSONParser {
    HashMap<String,HashSet<String>> hr = new HashMap<>();
    public void generateJSON7(File folder) throws IOException{

        for (File file : folder.listFiles()) {

            if (file.isDirectory()) {
                generateJSON7(file);
            } 
            else {
                String cur;
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                System.out.println(file.getAbsolutePath());
                BufferedReader br = new BufferedReader(isr);
                while((cur=br.readLine()) != null){
                    int breakpoint = cur.indexOf(":");
                    String s = cur.substring(0,breakpoint);
                    String h = cur.substring(breakpoint+2);
                    if(hr.containsKey(s)){
                        HashSet temp = hr.get(s);
                        temp.add(h);     
                    }
                    else{
                        HashSet<String> r = new HashSet<String>();
                        r.add(h);
                        hr.put(s,r);
                    }
                }
  
            }
        
        }
        try {
            new ObjectMapper().writeValue(new FileWriter("Sweet_Output",true), hr);
            System.out.println("Sweet_Output created");
        } 
        catch (IOException ex) {
            System.err.println("Error writing sweet_output.json");
        }
    
    }
    public void addFileContentType(File folder) throws IOException{
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("D:\\polar_temp.txt", true)));
        Tika tika = new Tika();
        for (File file : folder.listFiles()){
            if (file.isDirectory()) {
                addFileContentType(file);
            }
            else{
                String mimetype  = tika.detect(file);
                String output = "{\"Content-Type\": \""+mimetype+"\", \"id\": \""+file.getName()+"\"}";
                out.println(output);
            }

        }
        out.close();
        
    }

}

