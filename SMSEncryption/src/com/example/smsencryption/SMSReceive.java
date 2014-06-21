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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.widget.Toast;

public class SMSReceive extends BroadcastReceiver {
	
	Cipher decipher;
	//KeyGenerator objSymmKeyGen;
	static SecretKey objSecKey;
	byte[] decrypted ;
	static Boolean keyFlag = true;
	 byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	  String doc2;
	 IvParameterSpec ivspec = new IvParameterSpec(iv);
	@Override
	public void onReceive(Context context, Intent intent) {
		// if
		// (intent.getAction().equals(android.provider.Telephony.SMS_RECEIVED))
		// {
		//abortBroadcast();// this is prevent message to deliver to user**
	 //android.os.Debug.waitForDebugger();
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			/*	str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";*/
				str+=msgs[i].getMessageBody().toString();
			}
			 
			/*objSecKey =new SecretKeySpec("AAAAAAAAAAAAAAAA".getBytes(), "AES");
			try {
				decipher =  Cipher.getInstance("AES/CBC/PKCS5Padding");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				decipher.init(Cipher.DECRYPT_MODE, objSecKey,ivspec);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] data =  Base64.decode(str, Base64.DEFAULT);
			try {
				decrypted = decipher.doFinal(data);
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				str = new String(decrypted, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			if(keyFlag == true)
			{
				//objSecKey = new SecretKeySpec(str.getBytes(), "AES");
				  byte[] temp = Base64.decode(str, Base64.DEFAULT);
				  objSecKey = new SecretKeySpec(temp, "AES");
				keyFlag=false;
			}
			else
			{
				try {
					
					decipher =  Cipher.getInstance("AES/CBC/PKCS5Padding");
					decipher.init(Cipher.DECRYPT_MODE, objSecKey,ivspec);
					
					byte[] data =  Base64.decode(str, Base64.DEFAULT);//message.getBytes("UTF-8");
					//data = Base64.encode(data, Base64.DEFAULT);
					//data = cipher.doFinal(data);
					
					
					//encmsg = Base64.encodeToString(encrypted, Base64.DEFAULT)
					
					decrypted = decipher.doFinal(data);
					 str = new String(decrypted, "UTF-8");
					 
					 
					 
				} catch (NoSuchAlgorithmException e) {
					Toast.makeText(context, "NoSuchAlgorithmException", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					Toast.makeText(context, "NoSuchPaddingException", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					Toast.makeText(context, "IllegalBlockSizeException", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					Toast.makeText(context, "BadPaddingException", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					Toast.makeText(context, "UnsupportedEncodingException", Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			// ---display the new SMS message---
			Toast.makeText(context, str, Toast.LENGTH_LONG).show();
			  /*String sender = msgs[0].getOriginatingAddress();
			  SmsManager sms = SmsManager.getDefault();
              sms.sendTextMessage("5556", sender, str, null, null)*/;//phone number
		}
	}
}
// }