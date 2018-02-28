package com.example.falldetectionsun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.os.Environment;


public class AlgorithmFunction {
	
	
	 //最小值
	   public  static double  Min(double d1, double d2, double d3) {
			double min=88888;
			if(d1>=d2){
				min=d2;
			}
			else{
				min=d1;
			}
			if(min>=d3){
				min=d3;
			}
			return min;
		}	  
	
	// dtw
    public static double DTW(ArrayList<Double> t,ArrayList<Double> r){
    	
        double dist=0;
        int n=t.size();
        int m=r.size();
        double d[][]=new double [n][m];
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                d[i][j]=(Math.sqrt((t.get(i)-r.get(j))*(t.get(i)-r.get(j))));
                }
            }
        double D[][]=new double[n][m];
        double D1;double D2;double D3;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                D[i][j]=88888;
                }
            }
        D[0][0]=d[0][0];
        int k=1;
        for(int i=1;i<n;i++){
            for(int j=0;j<m;j++){
                D1=D[i-1][j];
                if(j>0){
                    D2=D[i-1][j-1];
                    }
                else{
                    D2=88888;
                    }
                if(j>1){
                    D3=D[i-1][j-2];
                    }
                else{
                    D3=88888;
                    }
                D[i][j]=d[i][j]+Min(D1,D2,D3);

                }
            }
        int i=n-1;
        int j=m-1;
        while(i!=0&&j!=1){
            if (i==0)
                {
                j=j-1;
                } 
            else if(j==0)
                {i=i-1;}
            else
                {if(D[i-1][j]<=D[i-1][j-1]&&D[i-1][j]<=D[i-1][j-2])
                    {
                    i=i-1;
                    }
                else if(D[i-1][j-1]<=D[i-1][j]&&D[i-1][j-1]<=D[i-1][j-2])
                   {
                    i=i-1;j=j-1;
                    }
                else {
                    i=i-1;j=j-2;
                    }             
                } 
            k=k+1;
            } 
        dist=D[n-1][m-1]/k;
        return dist;
        }



    //Smooth
    
    public static ArrayList<Double> smooth(ArrayList<Double> a){
        int n=a.size();
        ArrayList<Double> b=new ArrayList<Double>();
        b.add(a.get(0));
        b.add((a.get(0)+a.get(1)+a.get(2))/3);
        for(int i=2;i<n-2;i++){
            b.add((a.get(i-2)+a.get(i-1)+a.get(i)+a.get(i+1)+a.get(i+2))/5);
            }
        b.add((a.get(n-3)+a.get(n-2)+a.get(n-1))/3);
        b.add(a.get(n-1));
        return b;
        }
  //运动幅度特征概率分配函数
    public static double Sds (double a){
    	
    	if (a>0&a<1.0){
    		a=0.968;
    	}
    	else if(a>=1.0&a<1.2){
    		a=0.826;
    	}
    	else if(a>=1.2&a<1.4){
    		a=0.667;
    	}
    	else if(a>=1.4&a<1.6){
    		a=0.455;
    	}
    	else if(a>=1.6&a<1.8){
    		a=0.333;
    	}
    	else if(a>=1.8&a<2.0){
    		a=0.047;
    	}
    	else if(a>=2.0){
    		a=0.024;
    	}
    	return a;
    	
    }
    //倾斜程度特征概率分配函数
    public static double Jds (double j){
    	
    	if (j>0&j<1.3){
    		j=0.964;
    	}
    	else if(j>=1.3&j<1.5){
    		j=0.735;
    	}
    	else if(j>=1.5&j<1.7){
    		j=0.5;
    	}
    	else if(j>=1.7&j<1.9){
    		j=0.323;
    	}
    	else if(j>=1.9&j<2.1){
    		j=0.105;
    	}
    	else if(j>=2.1){
    		j=0.036;
    	}
    	return j;
    	
    }
    //旋转程度特征概率分配函数
    public static double Gds (double g){
    	
    	if (g>0&g<0.8){
    		g=0.925;
    	}
    	else if(g>=0.8&g<1.0){
    		g=0.818;
    	}
    	else if(g>=1.0&g<1.2){
    		g=0.615;
    	}
    	else if(g>=1.2&g<1.4){
    		g=0.5;
    	}
    	else if(g>=1.4&g<1.6){
    		g=0.208;
    	}
    	else if(g>=1.6){
    		g=0.033;
    	}
    	return g;
    	
    }
    //D-S检测跌倒，flag为1是跌倒，flag为0是日常生活活动
    public static int ds(double s1,double s2,double s3){
    	int flag;
    	double mfall;
    	double mactivity;
        double m1=s1*s2*s3;
        double m2=(1-s1)*(1-s2)*(1-s3);
    	
        mfall=m1/(m1+m2);
    	mactivity=1-mfall;
    
    	if (mfall>=mactivity){
    		flag=1;
    	}
    	else {
    		flag=0;
    	}
    	
    	return flag;
    }
    //在手机内存卡上创建文件夹
	public static File creatFile(String fileName,String sdpath1)throws IOException{
		File file1;	
		file1=new File(sdpath1+"//"+fileName);
		if(!file1.exists()){
			file1.createNewFile();
		}
		
		return file1;
	}
	
	//读取文件的内容插入到Double类型的ArrayList里
	 public static ArrayList<Double> read(String dirname)
		{
			ArrayList<Double> ok=new ArrayList<Double>();
			String sdpath=Environment.getExternalStorageDirectory().getPath();
			String readline;
			
			try{
				File file=new File(sdpath+"//"+dirname);
				BufferedReader br=new BufferedReader(new FileReader(file));
				
				//StringBuffer bs=new StringBuffer();
				
				while((readline=br.readLine())!=null){
					//bs.append(readline+"\n");
					ok.add(Double.parseDouble(readline));
					
				}
				br.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}	
			return ok;
		}

}
   