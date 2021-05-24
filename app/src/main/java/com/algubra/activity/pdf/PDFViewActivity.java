package com.algubra.activity.pdf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, download, print, share;
//    PDFView pdf;

    PDFView Pdf;
    Integer pageNumber = 0;
    String pdfFileName;
    ProgressDialog dialog;

    WebView webView;

    String url, title, name;
    Bundle extras;
    Context mContext;
    ProgressDialog mProgressDialog;
    private PrintManager printManager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activty_pdfview);
        mContext = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //  LocaleHelper.setLocale(getApplicationContext(), PrefUtils.getLanguageString(mContext));

        extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("pdf_url");
            title = extras.getString("title");
            name = extras.getString("filename");

        }
        System.out.println("PDF: " + url);
        resetDisconnectTimer();
        printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        relativeHeader = findViewById(R.id.relativeHeader);
        print = findViewById(R.id.print);
        download = findViewById(R.id.download);
        share = findViewById(R.id.shared);

        webView = findViewById(R.id.pdfView);

//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//
//        PdfWebViewClient pdfWebViewClient = new PdfWebViewClient(this, webView);
//        pdfWebViewClient.loadPdfUrl(url);

        dialog = new ProgressDialog(PDFViewActivity.this);
        dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
        dialog.show();

        webView.clearCache(true);
        webView.clearHistory();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
//        webView.loadUrl("https://view.officeapps.live.com/op/embed.aspx?src="+url);
        WebSettings webSettings = webView.getSettings();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
                webView.loadUrl("http://docs.google.com/viewer?url=" + url + "&embedded=true");
                //  webView.loadUrl(url);
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        }, 5000);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
//                if (url.startsWith("intent://")) {
//                    System.out.println("workingcondition1");
//                    try {
//                        Context context = webView.getContext();
//                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//                        if (intent != null) {
//                            PackageManager packageManager = context.getPackageManager();
//                            ResolveInfo info = packageManager.resolveActivity(intent,
//                                    PackageManager.MATCH_DEFAULT_ONLY);
//                            String fallbackUrl = intent.getStringExtra(
//                                    "browser_fallback_url");
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(fallbackUrl));
//                            context.startActivity(browserIntent);
//                            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
//
//                        }
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//
//                }
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {

                    System.out.println(" url to click" + url);
                    webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
//                    view.getContext().startActivity(
//                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
//                if (url.startsWith("intent://")) {
//                    System.out.println("workingcondition2");
//                    try {
//                        Context context = webView.getContext();
//                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//                        if (intent != null) {
//                            PackageManager packageManager = context.getPackageManager();
//                            ResolveInfo info = packageManager.resolveActivity(intent,
//                                    PackageManager.MATCH_DEFAULT_ONLY);
//                            String fallbackUrl = intent.getStringExtra(
//                                    "browser_fallback_url");
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(fallbackUrl));
//                            context.startActivity(browserIntent);
//                            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
//
//                        }
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//
//                }

                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    System.out.println(" url to click" + url);
//                    view.getContext().startActivity(
//                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                System.out.println("LOADINGURL:" + url);
                if (url != null && (url.contains("intent://"))) {
                    Log.d("URLDATA1:", url);
                    System.out.println("url to clickupdated" + url);
                    try {
                        Context context = webView.getContext();
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
//                            PackageManager packageManager = context.getPackageManager();
//                            ResolveInfo info = packageManager.resolveActivity(intent,
//                                    PackageManager.MATCH_DEFAULT_ONLY);
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(fallbackUrl));
//                            context.startActivity(browserIntent);
                            webView.loadUrl(fallbackUrl);


                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                } else if (url != null && (url.contains("https://isgoman.meitsystems.com"))) {
                    Log.d("URLDATA2:", url);
                    webView.loadUrl(url);
                } else {
                    // webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                    try {

                        String urltest = "https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(url, "ISO-8859-1");
                        //webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + urltest);
                        webView.loadUrl(url);
                        Log.d("URLDATA3:", url);


                        System.out.println("urltestdata:" + urltest);
                        System.out.println("urltestdata:" + url);


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
                Log.d("PDF Error: ", view.getUrl() + " error code " + errorCode + " description " + description);
                Log.d("LAODING URL DATA:", failingUrl);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

       /* webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
               Log.d("LOADING URL",url);
//                if (url.startsWith("intent://")) {
//                    System.out.println("workingcondition3");
//                    try {
//                        Context context = webView.getContext();
//                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//                        if (intent != null) {
//                            PackageManager packageManager = context.getPackageManager();
//                            ResolveInfo info = packageManager.resolveActivity(intent,
//                                    PackageManager.MATCH_DEFAULT_ONLY);
//                            String fallbackUrl = intent.getStringExtra(
//                                    "browser_fallback_url");
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(fallbackUrl));
//                            context.startActivity(browserIntent);
//                            webView.loadUrl(url);
//
//                        }
//                        return true;
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//
//                }
                if (url != null && (url.startsWith("https://docs.google.com/gview?embedded=true&url=") || url.startsWith("https://docs.google.com/gview?embedded=true&url="))) {
                    System.out.println(" url to click" + url);
                    try {
                        Context context = webView.getContext();
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            PackageManager packageManager = context.getPackageManager();
                            ResolveInfo info = packageManager.resolveActivity(intent,
                                    PackageManager.MATCH_DEFAULT_ONLY);
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(fallbackUrl));
                            context.startActivity(browserIntent);
                            webView.loadUrl(url);


                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    return true;
                } else {
                    webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });*/


//        webView.setWebViewClient(new WebViewClient() {
//
//            public void onPageFinished(WebView view, String url) {
//                // do your stuff here
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//
//                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
//                    view.getContext().startActivity(
//                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
//                }
//            }
//        });

        // Pdf = findViewById(R.id.pdfView);
        // Pdf.setBackgroundColor(Color.LTGRAY);

        headermanager = new HeaderManager(PDFViewActivity.this, title);
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                finish();
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    String jobName = getString(R.string.app_name) + " Document";
                    PrintJob printJob = printManager.print(jobName, new PDFPrintDocumentAdapter(PDFViewActivity.this, "document", getFilepath("document.pdf")), null);

                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                // Toast.makeText(mContext, "File Downloaded to download/"+name+".pdf", Toast.LENGTH_SHORT).show();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    mProgressDialog = new ProgressDialog(PDFViewActivity.this);
                    mProgressDialog.setMessage("Downloading..");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCancelable(true);
                    new DownloadPDF().execute();
                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            new DownloadPDF().cancel(true); //cancel the task
                        }
                    });
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                if (AppUtilityMethods.isNetworkConnected(mContext)) {
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File fileWithinMyDir = new File(getFilepath("document.pdf"));
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getFilepath("document.pdf")));
                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "ISG Oman");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });


        PermissionListener permissionListenerGallery = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (AppUtilityMethods.isNetworkConnected(mContext)) {

                    new loadPDF().execute();
                } else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(mContext, "Permission Denied\n", Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(mContext)
                .setPermissionListener(permissionListenerGallery)
                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        //
    }

    private void LoadingPdf(File uri) {

        dialog = new ProgressDialog(PDFViewActivity.this);
        dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
        dialog.show();

        Pdf.fromUri(Uri.fromFile(uri))
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }


        PdfDocument.Meta meta = Pdf.getDocumentMeta();
        Log.e("PDFVIEW", "title = " + meta.getTitle());
        Log.e("PDFVIEW", "author = " + meta.getAuthor());
        Log.e("PDFVIEW", "subject = " + meta.getSubject());
        Log.e("PDFVIEW", "keywords = " + meta.getKeywords());
        Log.e("PDFVIEW", "creator = " + meta.getCreator());
        Log.e("PDFVIEW", "producer = " + meta.getProducer());
        Log.e("PDFVIEW", "creationDate = " + meta.getCreationDate());
        Log.e("PDFVIEW", "modDate = " + meta.getModDate());

        printBookmarksTree(Pdf.getTableOfContents(), "-");
    }

    private void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("PDFVIEW", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageError(int page, Throwable t) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Log.e("PDFVIEW", "Cannot load page " + page + "Error: " + t.getMessage());
    }

    public class loadPDF extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new ProgressDialog(PDFViewActivity.this);
//            dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
//            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            try {
                String fileName = "document.pdf";
                u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                // Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "document.pdf");
            // System.out.println("file.exists() = " + file.exists());
            // pdf.fromUri(uri);

//            LoadingPdf(file);

            //pdf.fromFile(file).defaultPage(1).enableSwipe(true).load();


            //web.loadUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + "test.pdf");
            // Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_LONG).show();
        }

    }

    public class DownloadPDF extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private ProgressDialog dialog;
        String filename = name.replace(" ", "_");
        String fileName = filename + ".pdf";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PDFViewActivity.this);
            dialog.setMessage(getResources().getString(R.string.pleasewait));//Please wait...
            dialog.show();
        }

        protected Void doInBackground(String... urls) {
            URL u = null;
            try {

                u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                // c.setDoOutput(true);


                String auth = "SGHCXFTPUser" + ":" + "cXFTPu$3r";
                String encodedAuth = new String(Base64.encodeToString(auth.getBytes(), Base64.DEFAULT));
                encodedAuth = encodedAuth.replace("\n", "");

                c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                c.addRequestProperty("Authorization", "Basic " + encodedAuth);
                //c.setRequestProperty("Accept", "application/json");
                // c.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                c.connect();

                int response = c.getResponseCode();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/download/";
                // Log.d("Abhan", "PATH: " + PATH);
                File file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + fileName);
            System.out.println("file.exists() = " + file.exists());
            if (file.exists()) {
                Toast.makeText(mContext, "File Downloaded to download/" + fileName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Something Went Wrong. Download failed", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void resetDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity.disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();

    }

    @Override
    public void onStop() {
        super.onStop();
//        stopDisconnectTimer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopDisconnectTimer();

    }

    private String getFilepath(String filename) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Download/" + filename).getPath();

    }

    /**
     * PDF CLASS
     */

    private class PdfWebViewClient extends WebViewClient {
        private static final String TAG = "PdfWebViewClient";
        private static final String PDF_EXTENSION = ".pdf";
        private static final String PDF_VIEWER_URL = "http://docs.google.com/gview?embedded=true&url=";

        private Context mContext;
        private WebView mWebView;
        private ProgressDialog mProgressDialog;
        private boolean isLoadingPdfUrl;

        public PdfWebViewClient(Context context, WebView webView) {
            mContext = context;
            mWebView = webView;
            mWebView.setWebViewClient(this);
        }

        public void loadPdfUrl(String url) {
            mWebView.stopLoading();

            if (!TextUtils.isEmpty(url)) {
                isLoadingPdfUrl = isPdfUrl(url);
                if (isLoadingPdfUrl) {
                    mWebView.clearHistory();
                }

                showProgressDialog();
            }

            mWebView.loadUrl(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return shouldOverrideUrlLoading(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            handleError(errorCode, description.toString(), failingUrl);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            return shouldOverrideUrlLoading(webView, uri.toString());
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onReceivedError(final WebView webView, final WebResourceRequest request, final WebResourceError error) {
            final Uri uri = request.getUrl();
            handleError(error.getErrorCode(), error.getDescription().toString(), uri.toString());
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            Log.i(TAG, "Finished loading. URL : " + url);
            dismissProgressDialog();
        }

        private boolean shouldOverrideUrlLoading(final String url) {
            Log.i(TAG, "shouldOverrideUrlLoading() URL : " + url);

            if (!isLoadingPdfUrl && isPdfUrl(url)) {
                mWebView.stopLoading();

                final String pdfUrl = PDF_VIEWER_URL + url;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadPdfUrl(pdfUrl);
                    }
                }, 300);

                return true;
            }

            return false; // Load url in the webView itself
        }

        private void handleError(final int errorCode, final String description, final String failingUrl) {
            Log.e(TAG, "Error : " + errorCode + ", " + description + " URL : " + failingUrl);
        }

        private void showProgressDialog() {
            dismissProgressDialog();
            mProgressDialog = ProgressDialog.show(mContext, "", "Loading...");
        }

        private void dismissProgressDialog() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }

        private boolean isPdfUrl(String url) {
            if (!TextUtils.isEmpty(url)) {
                url = url.trim();
                int lastIndex = url.toLowerCase().lastIndexOf(PDF_EXTENSION);
                if (lastIndex != -1) {
                    return url.substring(lastIndex).equalsIgnoreCase(PDF_EXTENSION);
                }
            }
            return false;
        }
    }


}
