package com.example.falldetectionsun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class sendmessage extends Activity{
	
	//private int i;
	private ArrayList<String> ok=new ArrayList<String>();
	private String nam;
	private String tele;
	private String readline;
	private String txtbs;
	private ImageView alarm;
	private TextView autosend;
	private Button buttonOut1;
	public String longi;
	public String lati;
	private Timer timer = new Timer();	
	public String longtitude;
	public String latitude;
	LocationManager locationmanger;
	
	PendingIntent paIntent;
	SmsManager smsManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmessage);
		
		locationmanger =(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		String locationProvider = LocationManager.GPS_PROVIDER;
		Location lastKnownLocation = locationmanger.getLastKnownLocation(locationProvider);
		setNewLocation(lastKnownLocation);
		locationmanger.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
//		LocationManager locationManager=(LocationManager)sendmessage.this.getSystemService(Context.LOCATION_SERVICE);
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,200,200,new TestLocationListener());
		
		String path=Environment.getExternalStorageDirectory().getPath();
		try{
			File file=new File(path+"//"+"abc.txt");
			BufferedReader br=new BufferedReader(new FileReader(file));
			
			while((readline=br.readLine())!=null){
				ok.add(readline);
				
			}
			br.close();
			nam=ok.get(0);
			tele=ok.get(1);
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		paIntent = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
		smsManager = SmsManager.getDefault();
		
		buttonOut1=(Button)findViewById(R.id.buttonOut1);
		
		buttonOut1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendmessage.this.finish();				
			}
			
		});
		
		timer.schedule(task, 10*1000);
	}
	
	TimerTask task=new TimerTask(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			smsManager.sendTextMessage(tele, null, longtitude+latitude, paIntent, null);
			//smsManager.sendTextMessage(tele, null, longi+lati, paIntent, null);
						
		}
		
	};
	
	protected void setNewLocation(Location location) {
	    if(location!=null){
	    System.out.print("111111");
	    longtitude = "" + location.getLongitude();
	    latitude = "" + location.getLatitude();
	    }else{
	    Toast.makeText(getApplicationContext(), "正在努力获取位置。。。", Toast.LENGTH_SHORT).show();
	    }
	}
	
	 private final LocationListener locationListener = new LocationListener() {
		    @Override
		    public void onLocationChanged(Location location) {
		    setNewLocation(location);
		    Toast.makeText(getApplicationContext(), "您正在位移。。。", Toast.LENGTH_SHORT).show();
		}
		        public void onStatusChanged(String provider, int status, Bundle extras) {
		       
		        }

		        public void onProviderEnabled(String provider) {}

		        public void onProviderDisabled(String provider) {}
		    };
	
//	private class TestLocationListener implements LocationListener{
//
//		@Override
//		public void onLocationChanged(Location location) {
//			// TODO Auto-generated method stub
//			
//			double longitude=location.getLongitude();
//			double latitude=location.getLatitude();
//			longi=Double.toString(longitude);
//			lati=Double.toString(latitude);
//			
//		}
//
//		@Override
//		public void onProviderDisabled(String provider) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onProviderEnabled(String provider) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
	

}
