package com.example.gitupload;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
//import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button takePhotto,login1,login2,upload;
	TextView textul;
	//------------------------------------------------------
	 private static final int CAMERA_REQUEST = 1888;
	    private String selectedImagePath;   
	  //  WebView webview;
	    String fileName = "capturedImage.jpg";
	    private static Uri mCapturedImageURI; 

	
	//-------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhotto = (Button) findViewById(R.id.takePhotto);
        textul=(TextView) findViewById(R.id.textul);
        //webview=(WebView)findViewById(R.id.webView1);
        login1=(Button) findViewById(R.id.login1);
        login2=(Button) findViewById(R.id.login2);
        upload=(Button) findViewById(R.id.upload);
        //TAKE PHOTTO-----------------------------------------------------------
        takePhotto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TakePhoto();
			}
		});
        //----------------------------------------------------------------------
        upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    textul.setText("Uploading");	
			}
		});
        
        login1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setContentView(R.layout.activity_login);
			}
					
		});
        login2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setContentView(R.layout.activity_login_activity2);
			}
		});
        
        //----------------------------------------------------------------------
        
        
        
        
        
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    

   

    //-------------------------------------------------------------------------------
  
   public void TakePhoto()
   {   
           ContentValues values = new ContentValues();  
           values.put(MediaStore.Images.Media.TITLE, fileName);  
           mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
           Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI); 
           startActivityForResult(cameraIntent, CAMERA_REQUEST);
   }       
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data)
   {
       if (resultCode == RESULT_OK)
           {
             if (requestCode == CAMERA_REQUEST) 
             { 
               selectedImagePath = getPath(mCapturedImageURI);
               textul.setText(selectedImagePath);
        //   webview.loadUrl("javascript:ReceivePhoto(\""+selectedImagePath+"\")");
             }
           }
   }

   public String getPath(Uri uri) {
       String[] projection = { MediaStore.Images.Media.DATA };
       Cursor cursor = managedQuery(uri, projection, null, null, null);
       int column_index = cursor
               .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
       cursor.moveToFirst();
       return cursor.getString(column_index);
   }
    
    //----------------------------------------------------
}
