package com.example.smsencryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Base64;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	 Button btnSendSMS;
	    EditText txtPhoneNo;
	    EditText txtMessage;
	    Boolean KeySent=false;
	    KeyGenerator objSymmKeyGen;
		SecretKey objSecKey;
		Cipher cipher;
		
		byte[] encrypted ;
		
		String message,encmsg;
		  byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		  String doc2;
		 IvParameterSpec ivspec = new IvParameterSpec(iv);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
        
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                String phoneNo = txtPhoneNo.getText().toString();
                message = txtMessage.getText().toString();                 
                if (phoneNo.length()>0 && message.length()>0)
					try {
						sendSMS(phoneNo, message);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
                    Toast.makeText(getBaseContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        });        
    }    
        
    protected void sendSMS(String phoneNumber, String message) throws NoSuchAlgorithmException, NoSuchPaddingException {
		// TODO Auto-generated method stub
    	//android.os.Debug.waitForDebugger();
    	  /*if (KeySent ==false)
          {
          	generateSecretKey();
          	PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(), 0);             
            SmsManager sms = SmsManager.getDefault();
            byte[] enc = objSecKey.getEncoded();
            //byte[] data = text.getBytes("UTF-8");
            String base64 = Base64.encodeToString(enc, Base64.DEFAULT);
            sms.sendTextMessage(phoneNumber, null, base64, pi, null);     
            KeySent=true;
          }
    	  else 
    	  {
    		  encrypt();
    		  PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(), 0);             
              SmsManager sms = SmsManager.getDefault();
              sms.sendTextMessage(phoneNumber, null, encmsg, pi, null);   
    	  }*/
    	
    	PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(), 0);             
        SmsManager sms = SmsManager.getDefault();
    	cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    	objSecKey =new SecretKeySpec("AAAAAAAAAAAAAAAA".getBytes(), "AES");
          encrypt();
          sms.sendTextMessage(phoneNumber, null, encmsg, pi, null);   
		
	}

	
    public void generateSecretKey() {
		try {
			objSymmKeyGen = KeyGenerator.getInstance("AES");
			 cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objSymmKeyGen.init(256);
		objSecKey = objSymmKeyGen.generateKey();
	}

    public void encrypt()
	{
		try {
			cipher.init(cipher.ENCRYPT_MODE, objSecKey,ivspec);
			
			byte[] data = message.getBytes("UTF-8");
			//data = Base64.encode(data, Base64.DEFAULT);
			encrypted = cipher.doFinal(data);
			encmsg = Base64.encodeToString(encrypted, Base64.DEFAULT);
			//encmsg = encrypted.toString();
							
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
