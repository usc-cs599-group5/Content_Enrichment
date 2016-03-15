package csci599;
import org.apache.tika.Tika;
import java.io.File;
import java.io.IOException;

public class content_enrichment {
   
   private static Tika tika = new Tika();
   public static void main(String arg[]) throws IOException{
   File f = new File("E:\\Sem 2\\CSCI 599\\Dataset\\1358A41AF2B7F5872E9BFF6CBC9665923836D451544F5DFDCB20B62375FB8956");
   String text = tika.detect(f);
   System.out.println(text);
   
   }
   
   
}
