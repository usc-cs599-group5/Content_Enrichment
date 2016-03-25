package csci599;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.tika.Tika;

public class TTRParser
{
    ArrayList<Double> ttr,smoothedTTR;
    Vector<Double> clusters1[];
    Vector<Integer> clusters2[];
    private static Tika tika;
    private static double min,max;
    
    
    TTRParser()
    {
        ttr=new ArrayList<Double>();
        smoothedTTR=new ArrayList<Double>();
        tika = new Tika();
        min=0.0;
        max=0.0;
    }
    
    void parse() throws IOException
    {
        forallfiles();
    }
    
    public void forallfiles() throws IOException
    {
        File f = new File("E:\\Sem 2\\CSCI 599\\Test\\62821ECFBF1ED615D51149EF92A6D738C745D8C68E07713839F638B16FAB84CC");
        String mime = tika.detect(f);
        System.out.println(mime);
        StringBuffer contents=readFile(f);
        contents=generateTTR(contents);
        ArrayList<String> text;
        text=new ArrayList<String>(generateClusters(contents));
        System.out.println("**********************************************OUTPUT OF PART A*******************************");
        for(int i=0;i<text.size();i++)
            System.out.println(text.get(i));
    }
    
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
        //System.out.println(sb);
        return sb;
    }
    
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
        //smoothening2();
        System.out.println("***********************************TTRs********************************************");
        for(int i=0;i<ttr.size();i++)
            System.out.println("New TTR: "+smoothedTTR.get(i)+" TTR: "+ttr.get(i));
        return contents;
    }
    
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
        //System.out.println(html2);
        return html2;
    }
    
    public void smoothening2()
    {
        double[] ttr1=new double[ttr.size()];
        for(int i=0;i<ttr.size();i++)
            ttr1[i]=ttr.get(i);
        double sigma=Math.ceil(calculateStandardDeviation(ttr1));
        double[] newTTR1=new double[ttr1.length];
        double[] newTTR2=new double[newTTR1.length];
        double[] newTTR3=new double[newTTR2.length];
        for(int i=0;i<ttr1.length;i++)
        {
            newTTR1[i]=ttr1[i];
            newTTR2[i]=ttr1[i];
            newTTR3[i]=ttr1[i];
        }
        //equation1
        for(int i=0;i<=(2*sigma);i++)
        {
            newTTR1[i]=0.0;
            for(int j=(int) (-1*sigma);j<=sigma;j++)
                newTTR1[i]=newTTR1[i]+Math.pow(Math.E, ((-1*j*j)/(2*sigma*sigma)));
            newTTR2[i]=newTTR1[i];
            newTTR3[i]=newTTR2[i];
        }
        //equation2
        for(int i=(int)sigma;i<=(2*sigma);i++)
        {
            double sum=0.0;
            for(int j=0;j<=sigma;j++)
                sum=sum+newTTR1[j];
            newTTR2[i]=newTTR2[i]/sum;
            newTTR3[i]=newTTR2[i];
        }
        //equation3
        for(int i=(int)sigma;i<=(ttr1.length-(int)sigma);i++)
        {
            newTTR3[i]=0.0;
            for(int j=(int)(-1*sigma);j<=sigma;j++)
            {
                if((j+(int)sigma)<newTTR2.length&&(i-j)<ttr1.length)
                    newTTR3[i]=newTTR3[i]+(newTTR2[j+(int)sigma]*ttr1[i-j]);
            }
        }
        min=0.0;
        max=0.0;
        for(int i=0;i<newTTR3.length;i++)
        {
            smoothedTTR.add(newTTR3[i]);
            if(min>newTTR3[i])
                min=newTTR3[i];
            if(max<newTTR3[i])
                max=newTTR3[i];
        }
        System.out.println("min="+min);
        System.out.println("max="+max);
    }
    
    public void smoothening1()
    {
        //smoothening formula used from paper https://www3.nd.edu/~tweninge/pubs/WH_TIR08.pdf
        int r=1;
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
    
    public ArrayList<String> generateClusters(StringBuffer contents) throws IOException
    {
        boolean endClustering=false;
        int k=3;
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
        System.out.println("Standard Deviation: "+standardDeviation);
        boolean considerCluster[]=new boolean[k];
        for(int i=0;i<k;i++)
        {
            if(means[i]>=standardDeviation)
                considerCluster[i]=true;
        }
        //output the lines from the trucated file which are present in the clusters2 vector for which considerCluster is true
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
   
}
