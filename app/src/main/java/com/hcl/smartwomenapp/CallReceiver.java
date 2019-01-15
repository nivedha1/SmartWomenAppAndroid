package com.hcl.smartwomenapp;


import java.lang.reflect.Method;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.os.Binder;
import android.os.IBinder;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                    try {
                        String serviceManagerName = "android.os.ServiceManager";
                        String serviceManagerNativeName = "android.os.ServiceManagerNative";
                        String telephonyName = "com.android.internal.telephony.ITelephony";
                        Class<?> telephonyClass;
                        Class<?> telephonyStubClass;
                        Class<?> serviceManagerClass;
                        Class<?> serviceManagerNativeClass;
                        Method telephonyEndCall;
                        Object telephonyObject;
                        Object serviceManagerObject;
                        telephonyClass = Class.forName(telephonyName);
                        telephonyStubClass = telephonyClass.getClasses()[0];
                        serviceManagerClass = Class.forName(serviceManagerName);
                        serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
                        Method getService =
                                serviceManagerClass.getMethod("getService", String.class);
                        Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
                        Binder tmpBinder = new Binder();
                        tmpBinder.attachInterface(null, "fake");
                        serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
                        IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
                        Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
                        telephonyObject = serviceMethod.invoke(null, retbinder);
                        telephonyEndCall = telephonyClass.getMethod("endCall");
                        telephonyEndCall.invoke(telephonyObject);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if(incomingNumber.equals("+91"+context.getApplicationContext().getSharedPreferences("SessionPerson", 0).getString("phoneno", "")))
                {
                    String urlcode = "tel:" + "**21*" +
                            "+91"+context.getApplicationContext().getSharedPreferences("SessionPerson", 0).getString("forwardno", "")
                            + Uri.encode("#");
                    Intent intentcall = (new Intent(Intent.ACTION_CALL, Uri.parse(urlcode)));
                    intentcall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentcall);
                    urlcode = "tel:"
                             + Uri.encode("##002#");
                    intentcall = (new Intent(Intent.ACTION_CALL, Uri.parse(urlcode)));
                    intentcall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentcall);
                }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }

}




