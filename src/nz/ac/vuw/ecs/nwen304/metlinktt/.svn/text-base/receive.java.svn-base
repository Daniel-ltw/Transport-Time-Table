package nz.ac.vuw.ecs.nwen304.metlinktt;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class receive extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//  get the SMS message passed in
		Bundle bundle = intent.getExtras();        
		SmsMessage[] msgs = null;
		String str = "";            
		if (bundle != null)
		{
			//  retrieve the SMS message received
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];            
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
				if (msgs[i].getOriginatingAddress().equals("287")) {
					//  disable notification
					/*NotificationManager nm = (NotificationManager)context
					.getSystemService(context.NOTIFICATION_SERVICE);*/

					//  display the new SMS message
					str = msgs[i].getMessageBody();
					StringTokenizer tok = new StringTokenizer(str);
					ArrayList<String> details = new ArrayList<String>();

					//  getting details of bus services and timings
					while (tok.hasMoreTokens()) {
						String s = tok.nextToken();
						if (s.matches("[0-9]*@[0-9][0-9]:[0-9][0-9][,||.]")) {
							details.add(s.substring(0, s.length() - 1));
						}
					}

					//  manipulating details of buses to be displayed on toast
					str = "";
					for (String c:details) {
						String[] split = c.split("@");
						if (details.indexOf(c) == (details.size() - 1) ) {
							str += "Bus " + split[0] + " arrives at " + split[1];
						} else {
							str += "Bus " + split[0] + " arrives at " + split[1] + " \n";
						}
					}
					
					Toast.makeText(context, str, Toast.LENGTH_LONG).show();

				}
			}
		} 
	}

}
