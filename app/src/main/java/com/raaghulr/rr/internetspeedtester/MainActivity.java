package com.raaghulr.rr.internetspeedtester;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import android.os.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView wv;
    String mPath;
    FloatingActionButton fab2;
    private AdView mAdView,mAdView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takeScreenshot();
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.this.wv.loadUrl("https://www.fast.com");
                wv.getSettings().setJavaScriptEnabled(true);
            }
        });

        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        wv = (WebView) findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient(){

            //

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadError();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
               /*
                wv.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('footer-container')[0].style.display='none'; })()"); */

                // hide element by class name
                wv.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('logo-container')[0].style.visibility='hidden'     ; })()");

                wv.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('test-on-ookla')[0].style.display='none'; })()");

                // hide element by id
                wv.loadUrl("javascript:(function() { " +
                        "document.getElementById('your_id').style.display='none';})()");

                wv.loadUrl("javascript:document.getElementById('speed-value').style.setProperty(\"color\", \"blue\");");
                wv.loadUrl("javascript:document.getElementById('speed-units').style.setProperty(\"color\", \"red\");");
                wv.loadUrl("javascript:document.getElementById('speed-controls-container centered').setAttribute(\"align\", \"center\");");
            }
            //


        });
        wv.loadUrl("javascript:alert(functionThatReturnsSomething)");
        wv.loadUrl("https://www.fast.com");







        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9940055534807736~8154470200");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView1 = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest);

    }

    final class wv extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("LogTag", message);
            result.confirm();
            return true;
        }
    }

    private void loadError() {
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab2.getLayoutParams();
        p.setAnchorId(View.NO_ID);
        fab2.setLayoutParams(p);
        fab2.setVisibility(View.VISIBLE);
        String html = "<html><body><table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"
                + "<tr>"
                + "<td><div align=\"center\"><font color=\"red\" size=\"20pt\">Your device don't have active internet connection</font></div></td>"
                + "</tr>" + "</table><html><body>";
        System.out.println("html " + html);

        String base64 = android.util.Base64.encodeToString(html.getBytes(),
                android.util.Base64.DEFAULT);
        wv.loadData(base64, "text/html; charset=utf-8", "base64");
        System.out.println("loaded html");
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure want to exit the application?");
        builder.setCancelable(true);
        builder.setNegativeButton("Stay On App!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void takeScreenshot() {

        Date now = new Date();
        String mPath;
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "Internet Speed Tester");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                // Do something on success
                mPath = Environment.getExternalStorageDirectory().toString() + "/Internet Speed Tester/" + now + ".jpg";
            } else {
                // Do something else on failure
                mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            }
            // image naming and path  to include sd card  appending name you choose for file
            //String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_screenshot) {
            //Intent fp=new Intent(getApplicationContext(),MainActivity.class);
            // startActivity(fp);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    takeScreenshot();
                }
            }, 200);

        } else if (id == R.id.nav_share) {

            Intent i=new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject test");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Hey check out this app, Now you can test your Internet Speed within 15 Seconds with this innovative app at:\n\nhttps://play.google.com/store/apps/details?id=com.raaghulr.rr.internetspeedtester");
            startActivity(Intent.createChooser(i,"Share via"));

        } else if (id == R.id.nav_rateus) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.raaghulr.rr.internetspeedtester")));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}