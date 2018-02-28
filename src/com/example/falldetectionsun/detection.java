package com.example.falldetectionsun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class detection extends Activity{
	
	private int flaglast;
	private int time;
	private int fallflag;
	private ImageView fall;
	private TextView indetection;
	private Button buttonOut;
	private Button buttonStart;
	private Timer timer = new Timer();
	private SensorManager sensorManager;
	private MySensorEventListener sensorEventListener;
	private ArrayList<Double>sv=new ArrayList<Double>();
	private ArrayList<Double>jd=new ArrayList<Double>();
	private ArrayList<Double>gv=new ArrayList<Double>();
	private ArrayList<Double>sv1=new ArrayList<Double>();
	private ArrayList<Double>sv2=new ArrayList<Double>();
	private ArrayList<Double>sv3=new ArrayList<Double>();
	private ArrayList<Double>sv4=new ArrayList<Double>();
	private ArrayList<Double>jd1=new ArrayList<Double>();
	private ArrayList<Double>jd2=new ArrayList<Double>();
	private ArrayList<Double>jd3=new ArrayList<Double>();
	private ArrayList<Double>jd4=new ArrayList<Double>();
	private ArrayList<Double>gv1=new ArrayList<Double>();
	private ArrayList<Double>gv2=new ArrayList<Double>();
	private ArrayList<Double>gv3=new ArrayList<Double>();
	private ArrayList<Double>gv4=new ArrayList<Double>();
	private ArrayList<Double>testsv=new ArrayList<Double>();
	private ArrayList<Double>testjd=new ArrayList<Double>();
	private ArrayList<Double>testgv=new ArrayList<Double>();
	private double meansv,meanjd,meangv;
	public MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detection);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorEventListener=new MySensorEventListener();
		
		fall=(ImageView)findViewById(R.id.fall);
		indetection=(TextView)findViewById(R.id.indetection);
		buttonOut=(Button)findViewById(R.id.buttonOut);
		buttonStart=(Button)findViewById(R.id.buttonStart);
		
		mp=MediaPlayer.create(getBaseContext(), R.raw.alarm);
		
		sv1=AlgorithmFunction.read("sv1.txt");
		sv2=AlgorithmFunction.read("sv2.txt");
		sv3=AlgorithmFunction.read("sv3.txt");
		sv4=AlgorithmFunction.read("sv4.txt");
		jd1=AlgorithmFunction.read("jd1.txt");
		jd2=AlgorithmFunction.read("jd2.txt");
		jd3=AlgorithmFunction.read("jd3.txt");
		jd4=AlgorithmFunction.read("jd4.txt");
		gv1=AlgorithmFunction.read("gv1.txt");
		gv2=AlgorithmFunction.read("gv2.txt");
		gv3=AlgorithmFunction.read("gv3.txt");
		gv4=AlgorithmFunction.read("gv4.txt");
		testsv=AlgorithmFunction.read("testsv.txt");
		testjd=AlgorithmFunction.read("testjd.txt");
		testgv=AlgorithmFunction.read("testgv.txt");
		
		meansv=(AlgorithmFunction.DTW(sv1,sv2)+AlgorithmFunction.DTW(sv1,sv3)+AlgorithmFunction.DTW(sv1,sv4)+AlgorithmFunction.DTW(sv2,sv3)+
				AlgorithmFunction.DTW(sv2,sv4)+AlgorithmFunction.DTW(sv3,sv4))/6;
		meanjd=(AlgorithmFunction.DTW(jd1,jd2)+AlgorithmFunction.DTW(jd1,jd3)+AlgorithmFunction.DTW(jd1,jd4)+AlgorithmFunction.DTW(jd2,jd3)+
				AlgorithmFunction.DTW(jd2,jd4)+AlgorithmFunction.DTW(jd3,jd4))/6;
		meangv=(AlgorithmFunction.DTW(gv1,gv2)+AlgorithmFunction.DTW(gv1,gv3)+AlgorithmFunction.DTW(gv1,gv4)+AlgorithmFunction.DTW(gv2,gv3)+
				AlgorithmFunction.DTW(gv2,gv4)+AlgorithmFunction.DTW(gv3,gv4))/6;

		buttonOut.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				detection.this.finish();	
			}	
			
		});
		
		buttonStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				timer.schedule(task, 3000);
				time=0;
			}	
			
		});
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Sensor gyroscopeSensor=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		Sensor accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(sensorEventListener,gyroscopeSensor,SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(sensorEventListener,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
		
		super.onResume();
		
	}
	
	class MySensorEventListener implements SensorEventListener{	

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			
			time++;
			
				switch(event.sensor.getType()){
				case Sensor.TYPE_GYROSCOPE:
					double ax=event.values[0];
					double ay=event.values[1];
					double az=event.values[2];
					double a=ax*ax+ay*ay+az*az;
					double sv1=Math.sqrt(a);
					double jd1=Math.cos(ay/sv1);
					
					if(time<150){
						sv.add(sv1);
						jd.add(jd1);
					}
				
					break;
				case  Sensor.TYPE_ACCELEROMETER:
					double gx=event.values[0];
					double gy=event.values[1];
					double gz=event.values[2];
					double g=gx*gx+gy*gy+gz*gz;
					double gv1=Math.sqrt(g);
					
					if(time<150){
						gv.add(gv1);
					}

					break;					
			}
				
		}

	}
	private int falldet(){
		
		int s,j,g;
		s=sv.size();
		j=jd.size();
		g=gv.size();
			
		double dtwsv=(AlgorithmFunction.DTW(sv, sv1)+AlgorithmFunction.DTW(sv, sv2)+AlgorithmFunction.DTW(sv, sv3)+AlgorithmFunction.DTW(sv, sv4))*meansv/4;
		double dtwjd=(AlgorithmFunction.DTW(jd, jd)+AlgorithmFunction.DTW(jd, jd2)+AlgorithmFunction.DTW(jd, jd3)+AlgorithmFunction.DTW(jd, jd4))*meanjd/4;
		double dtwgv=(AlgorithmFunction.DTW(gv, gv1)+AlgorithmFunction.DTW(gv, gv2)+AlgorithmFunction.DTW(gv, gv3)+AlgorithmFunction.DTW(gv, gv4))*meangv/4;
		
		double dssv=AlgorithmFunction.Sds(dtwsv);
		double dsjd=AlgorithmFunction.Jds(dtwjd);
		double dsgv=AlgorithmFunction.Gds(dtwgv);
		
		fallflag=AlgorithmFunction.ds(dssv,dsjd,dsgv);
		System.out.println(fallflag);
		return fallflag;		
	}
	
	TimerTask task=new TimerTask(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			flaglast=falldet();
			if (flaglast==1){
				mp.start();
				Intent intent=new Intent(detection.this,sendmessage.class);
				startActivity(intent);
				detection.this.finish();				
			}	
			
		}	
		
	};	
	
}

