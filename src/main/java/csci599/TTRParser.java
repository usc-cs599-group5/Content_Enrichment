package csci599;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;

public class TTRParser
{
    private static Tika tika;
    ArrayList<Double> ttr,smoothedTTR;
    private static double min,max;
        
    TTRParser()
    {
        ttr=new ArrayList<Double>(); //initialize TTR array
        smoothedTTR=new ArrayList<Double>(); //smoothed TTR of the file
        tika = new Tika();
        min=0.0;
        max=0.0;
    }
    //for each file
    void parse(File folder) throws IOException
    {
         for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                parse(file);
            } else {
                System.out.println(file.getAbsolutePath().substring(3));
                forallfiles(file);
            }
        }
    }
    
    public void forallfiles(File f) throws IOException
    {
        StringBuffer contents = parseToHTML(f);//get the XHTML format of the file
        contents=generateTTR(contents);//calculate the TTR of the file
        ArrayList<String> text;
        text=new ArrayList<String>(generateClusters(contents));//get the clusters using K-Means clustering
        //System.out.println("**********************************************OUTPUT OF PART A*******************************");
        PrintWriter writer = new PrintWriter("F:\\DOI\\TTR_PDFs\\" + f.getName(),"UTF-8");//get only text part of the file
            try{           
                for(int i=0;i<text.size();i++){
                    writer.println(text.get(i));
                    System.out.println(text.get(i));
                }
                ttr.clear();
                smoothedTTR.clear();
                min=0;
                max=0;
            }
            finally{
                writer.close();
            }
    }
    //Reads the file
    public static StringBuffer readFile(File f) throws FileNotFoundException, IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(f));
        String str;
        int i = 0;
        StringBuffer sb = new StringBuffer();
        while ((str = in.readLine()) != null)
        {

            if (!str.trim().isEmpty()) 
            {    
                 if (i > 0)
                    sb.append("\n");
                 sb.append(str.trim());
                 i++;
                 
            }
        }
        return sb;
    }
    //generates TTR of the file
    public StringBuffer generateTTR(StringBuffer contents) throws FileNotFoundException, IOException
    {
        
        contents=removeUnwantedTags(contents);
        String content=contents.toString();
        BufferedReader br = new BufferedReader(new StringReader(content));
        String line=null;
        int j=0;
        while( (line=br.readLine()) != null )
        {
            line=line.trim();
            Double tags=0.0;
            Double chars=0.0;
            for(int i=0;i<line.length();i++)
            {
                if(line.charAt(i)=='<')
                {
                    i++;
                    while(i<line.length()&&line.charAt(i)!='>')
                        i++;
                    tags++;
                }
                else
                    chars++;
            }
            if(tags==0)
                ttr.add(chars);
            else
                ttr.add(chars/tags);
            System.out.println("Line Number : "+j+"\n"+line+" : text = "+chars+" tags = "+tags+" ttr = "+ttr.get(j));
            j++;
        }
        smoothening1();
        return contents;
    }
    //removes script,meta,link,style and comments from the file
    public StringBuffer removeUnwantedTags(StringBuffer html)
    {
        int start;
        while ((start = html.indexOf("<!--")) != -1)
        {
            int end = html.indexOf("-->", start);
            html.delete(start, end+3);
        }

        while ((start = html.indexOf("<script")) != -1)
        {
            int end = html.indexOf("</script>", start);
            html.delete(start, end+9);
        }

        while ((start = html.indexOf("<style")) != -1)
        {
            int end = html.indexOf("</style>", start);
            html.delete(start, end+8);
        }
        
        while ((start = html.indexOf("<link")) != -1)
        {
            //System.out.println(html.indexOf("<link"));
            int end = html.indexOf(">", start);
            //System.out.println(end);
            html.delete(start, end+1);
        }
        
        while ((start = html.indexOf("<meta")) != -1)
        {
            int end = html.indexOf(">", start);
            html.delete(start, end+1);
        }
        
        StringBuffer html2 = new StringBuffer();
        for(int i = 0 ; i < html.length();i++)
        {
            if(html.charAt(i) == '\n' && html.charAt(i+1) == '\n')
                continue;
            html2.append(html.charAt(i));
        }
        return html2;
    }
    //performs smoothening
    public void smoothening1()
    {
        //smoothening formula used from paper https://www3.nd.edu/~tweninge/pubs/WH_TIR08.pdf
        int r=10;
        min=0.0;
        max=0.0;
        for(int i=0;i<ttr.size();i++)
        {
            int start=i-r;
            if(start<0)
                start=0;
            Double ek=0.0;
            for(int j=start;j<ttr.size()&&j<=(i+r);j++)
            {
                ek=ek+ttr.get(j);
            }
            ek=ek/(2*r+1);
            smoothedTTR.add(ek);
            if(min>ek)
                min=ek;
            if(max<ek)
                max=ek;
        }
    }
    //performs K-means clustering
    public ArrayList<String> generateClusters(StringBuffer contents) throws IOException
    {
        boolean endClustering=false;
        int k=3;
        Vector<Double> clusters1[];
        Vector<Integer> clusters2[];
        clusters1=new Vector[k];
        clusters2=new Vector[k];
        for(int i=0;i<k;i++)
        {
            clusters1[i]=new Vector();
            clusters2[i]=new Vector();
        }
        double[] means=new double[k];
        means=findMeans(means,k);
        double[] oldmeans=new double[k];
        for(int i=0;i<means.length;i++)
            oldmeans[i]=means[i];
        while(!endClustering)
        {
            for(int i=0;i<smoothedTTR.size();i++)
            {
                double element=smoothedTTR.get(i);
                double min=-1.0;
                int index=-1;
                for(int j=0;j<k;j++)
                {
                    double difference=means[j]-element;
                    if(difference<0)
                    {
                        difference*=-1;
                    }
                    if(min>difference || min==-1)
                    {
                        min=difference;
                        index=j;
                    }
                }
                clusters1[index].addElement(element);
                clusters2[index].addElement(i);
            }
            for(int i=0;i<k;i++)
            {
                oldmeans[i]=means[i];
            }
            for(int i=0;i<k;i++)
            {
                double newmean=0;
                for(int j=0;j<clusters1[i].size();j++)
                {
                    newmean+=clusters1[i].get(j);
                }
                if(clusters1[i].size()==0)
                    means[i]=0.0;
                else
                    means[i]=newmean/clusters1[i].size();
            }
            for(int i=0;i<k;)
            {
                if(oldmeans[i]==means[i])
                {
                    i++;
                    endClustering=true;
                }
                else
                {
                    for(int j=0;j<k;j++)
                    {
                        clusters1[j].removeAllElements();
                        clusters2[j].removeAllElements();
                    }
                    endClustering=false;
                    break;
                }
            }    
        }
        double standardDeviation=calculateStandardDeviation(means);
        boolean considerCluster[]=new boolean[k];
        for(int i=0;i<k;i++)
        {
            if(means[i]>=standardDeviation)
                considerCluster[i]=true;
        }
        String content=contents.toString();
        BufferedReader br = new BufferedReader(new StringReader(content));
        String line=null;
        ArrayList<String> lines=new ArrayList<String>();
        int j=0;
        while( (line=br.readLine()) != null )
        {
            for(int i=0;i<k;i++)
            {
                if(considerCluster[i]==true)
                {
                    for(int l=0;l<clusters2[i].size();l++)
                    {
                        if(clusters2[i].get(l)==j)
                            lines.add(line);
                    }
                }
            }
            j++;
        }
        return lines;
    }
    //calculate means of the clusters
    public double[] findMeans(double[] means,int k)
    {
        double val=(max-min)/(k+1);
        double start=min;
        for(int i=0;i<means.length;i++)
        {
            means[i]=start+val;
            start=start+val;
        }
        return means;
    }
    //calculate standard deviation
    public double calculateStandardDeviation(double[] means)
    {
        double mean=0.0;
        int k=means.length;
        for(int i=0;i<k;i++)
            mean=mean+means[i];
        mean=mean/k;
        double[] squaredMeans=new double[k];
        for(int i=0;i<k;i++)
            squaredMeans[i]=(means[i]-mean)*(means[i]-mean);
        double varience=0.0;
        for(int i=0;i<k;i++)
            varience=varience+squaredMeans[i];
        varience=varience/k;
        return Math.sqrt(varience);
    }
    //get the XHTML form of the file
    public static StringBuffer parseToHTML(File f)
    {
        ContentHandler handler = new ToXMLContentHandler();
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        try (InputStream stream = new FileInputStream(f)) 
        {
            parser.parse(stream, handler, metadata);
            System.out.println(handler.toString());
            StringBuffer br = new StringBuffer(handler.toString());
            br=trimXHTMLData(br);
            return br;
        }
        catch(Exception e)
        {
            System.out.println("Exception occurred " + e); 
        }
        return new StringBuffer("");
    }
    //truncates the lines of the file
    public static StringBuffer trimXHTMLData(StringBuffer contents) throws IOException
    {
        BufferedReader  br= new BufferedReader(new StringReader(contents.toString()));
        String line="";
        StringBuffer xhtml=new StringBuffer();
        int i = 0;
        while ((line = br.readLine()) != null)
        {

            if (!line.trim().isEmpty()) 
            {    
                 if (i > 0)
                    xhtml.append("\n");
                 xhtml.append(line.trim());
                 i++;
                 
            }
        }
        return xhtml;
    }
}
