package com.example.falldetectionsun;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class setting extends Activity{
	
	private Button buttonInsert;
	private Button buttonCancel;
	private EditText contactName;
	private EditText congtactTelephone;
	private String contacna ;
	private String contactel;
	private File file;
	private String sdpath;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		contactName=(EditText)findViewById(R.id.name);
		congtactTelephone=(EditText)findViewById(R.id.telephone);
		buttonInsert=(Button)findViewById(R.id.buttoninsert);
		buttonCancel=(Button)findViewById(R.id.buttoncancel);
		
		String sdpath=Environment.getExternalStorageDirectory().getPath();
		try{
			file=AlgorithmFunction.creatFile("abc.txt",sdpath);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		buttonCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setting.this.finish();
				
			}
			
		});
		
		buttonInsert.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				contacna = contactName.getText().toString().trim();
				contactel= congtactTelephone.getText().toString().trim();
				if(contacna.length()==0){
					Toast.makeText(getApplicationContext(), "联系人不能为空！", Toast.LENGTH_LONG).show();}
				else{
					if(contactel.length()==0){
						Toast.makeText(getApplicationContext(), "联系电话不能为空！", Toast.LENGTH_LONG).show();
					}
					else{
						
						try{
							String contacna1=contacna+"\n";
							byte[] bufferna=contacna1.getBytes();
							byte[] buffertel=contactel.getBytes();
							FileOutputStream fos =new FileOutputStream(file);
							fos.write(bufferna);
							fos.write(buffertel);
							fos.close();
							
							
						}
						catch(Exception e){
							System.out.println(e);
						}
						
						Intent intent=new Intent(setting.this,detection.class);
						startActivity(intent);
						setting.this.finish();
						
						
					}
					
				}
					
			}
			
		});
			
	}

		
	
	

}
