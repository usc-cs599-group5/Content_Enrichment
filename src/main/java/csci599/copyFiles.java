package csci599;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;

public class copyFiles
{
    private static Tika tika = new Tika();
    int[] count;
    int countT;
    
    copyFiles()
    {
         count=new int[15];
         countT=0;//counts total number of files
    }

    public void copy(File folder,File dest) throws IOException
    {
        if(countT>=8000)
            return;
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                copy(file,dest);
            } else {
                if(tika.detect(file).equalsIgnoreCase("application/pdf")&&count[0]<=1000)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[0]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("application/xhtml+xml")&&count[1]<=500)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[1]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("image/gif")&&count[2]<=500)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[2]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("image/png")&&count[3]<=500)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[3]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("application/pdf")&&count[4]<=250)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[4]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("audio/mpeg")&&count[5]<=500)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[5]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("image/jpeg")&&count[6]<=500)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[6]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("application/mp4")&&count[7]<=500)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[7]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("application/rss")&&count[8]<=250)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[8]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("text/html")&&count[9]<3000)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[9]++;
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("application/octet-stream")&&count[10]<=250)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[10]++;            
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("text/plain")&&count[11]<=500)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[11]++;            
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("application/xml")&&count[12]<=1000)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[12]++;               
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("video/mp4")&&count[13]<=250)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[13]++;               
                    countT++;
                }
                if(tika.detect(file).equalsIgnoreCase("application/msword")&&count[14]<=500)
                {   
                    FileUtils.copyFileToDirectory(file,dest);
                    count[14]++;          
                    countT++;
                }
            }
        }
    }
}