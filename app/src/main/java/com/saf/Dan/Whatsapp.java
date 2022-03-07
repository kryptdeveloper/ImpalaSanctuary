package com.saf.Dan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.planner.R;
import com.saf.clientFeedback.ChatWithAdmin;

import java.net.URLEncoder;

public class Whatsapp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);
        Button login = (Button) findViewById(R.id.send);
        Button inapp = (Button) findViewById(R.id.inapp);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
                if (isWhatsappInstalled) {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_VIEW);
                    sendIntent.setPackage("com.whatsapp");
                    String url = "https://api.whatsapp.com/send?phone=" + "+254705883870" + "&text=" + "Hello, Can you Help me with an issue regarding Impala...";
                    sendIntent.setData(Uri.parse(url));
                    if (sendIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        startActivity(sendIntent);
                    }
                }
                else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(Whatsapp.this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
            }
        });

        inapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainIntent = new Intent(Whatsapp.this, ChatWithAdmin.class);
                startActivity(MainIntent);
            }
        });



    }
//    private void openWhatsApp() {
//        String smsNumber = "0759867556";
//        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
//        if (isWhatsappInstalled) {
//
//            Intent sendIntent = new Intent("android.intent.action.MAIN");
//            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
//            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
//
//            startActivity(sendIntent);
//        } else {
//            Uri uri = Uri.parse("market://details?id=com.whatsapp");
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            Toast.makeText(this, "WhatsApp not Installed",
//                    Toast.LENGTH_SHORT).show();
//            startActivity(goToMarket);
//        }
//    }
//
    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}