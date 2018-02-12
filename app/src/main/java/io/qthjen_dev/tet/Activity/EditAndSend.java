package io.qthjen_dev.tet.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;

import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;

import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.r0adkll.slidr.Slidr;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.qthjen_dev.tet.R;

public class EditAndSend extends AppCompatActivity {

    private TextInputLayout mText;
    private Button mSendSms, mBt_share, bt_sendMessage;
    private LoginButton mLogin;
    private Toolbar mTbar_editAndSend;
    private AdView mAdView;

    private String mDataSended;

    private List<String> nameContact = new ArrayList<>();
    private List<String> numberContact = new ArrayList<>();
    private List<String> mNumberContactIsSelected = new LinkedList<>();

    private ShareDialog mShareDialog;
    private CallbackManager mCallbackManager;

    private String data;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_edit_and_send);

        /** TODO: LUU Y PACKAGE NAME
         try {
            PackageInfo info = null;
            try {
                info = getPackageManager().getPackageInfo(
                        "io.qthjen_dev.tet",
                        PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
         } catch (NoSuchAlgorithmException e) {}
         **/

        InitView();
        Slidr.attach(this);

        mShareDialog = new ShareDialog(this);
        mLogin.setReadPermissions(Arrays.asList("public_profile", "email"));

        data = getIntent().getExtras().getString("mydata");

        mText.getEditText().setText(data);

        getContactList();

        mSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNumberContactIsSelected.clear();
                /*
                SEND SMS:

                String[] number = new String[] {"01694051396", "0968511597", "01658247153", "0984612636"};

                for (int i = 0; i < number.length; i++) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage( number[i], null, mDataSended, null, null);
                }
                */

                /** ---------------- TODO: DIALOG CONTACT --------------------------------- **/
                final Dialog dialog = new Dialog(EditAndSend.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.dialog_mycontact);

                RecyclerView recyclerContact = dialog.findViewById(R.id.recyclerContact);
                Button send = dialog.findViewById(R.id.sendWithContact);
                ImageView bt_quit = dialog.findViewById(R.id.bt_quit);

                recyclerContact.setHasFixedSize(true);
                recyclerContact.setLayoutManager(new LinearLayoutManager(EditAndSend.this));

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerContact.getContext(), DividerItemDecoration.VERTICAL);
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider_item);
                dividerItemDecoration.setDrawable(drawable);
                recyclerContact.addItemDecoration(dividerItemDecoration);

                ContactAdapter contactAdapter = new ContactAdapter(nameContact, numberContact);

                recyclerContact.setAdapter(contactAdapter);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDataSended = mText.getEditText().getText().toString();

                        sendSMS(mNumberContactIsSelected, mDataSended);

                        dialog.dismiss();

                    }
                });

                bt_quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                /** -------------------------------------------------------------------------- **/

            }
        });

        mLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                TastyToast.makeText(EditAndSend.this, "Đăng nhập thành công", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                TastyToast.makeText(EditAndSend.this, "Lỗi mạng", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }
        });

        mBt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( CheckConnectInternet(EditAndSend.this) ) {

                    boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

                    if (!loggedIn) {

                        mDataSended = mText.getEditText().getText().toString();

                        setClipboard(EditAndSend.this, mDataSended);

                        Bitmap imageShare = BitmapFactory.decodeResource(getResources(), R.drawable.tet31);

                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(imageShare)
                                .build();

                        SharePhotoContent content = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                .build();

                        mShareDialog.show(content);

                        TastyToast.makeText(EditAndSend.this, "Đã copy vào clipboard bạn có thể paste lời chúc!", Toast.LENGTH_LONG, TastyToast.INFO);

                    } else {

                        TastyToast.makeText(EditAndSend.this, "Bạn chưa đăng nhập Facebook!", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    }

                } else {

                    TastyToast.makeText(EditAndSend.this, "Lỗi mạng!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }

            }
        });

        bt_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckConnectInternet(EditAndSend.this)) {

                    boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

                    if (isAppInstalled("com.facebook.orca")) {

                        if (!loggedIn) {

                            MessageDialog messageDialog = new MessageDialog(EditAndSend.this);
                            messageDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
                                @Override
                                public void onSuccess(Sharer.Result result) {
                                    //TastyToast.makeText(EditAndSend.this, "Đăng nhập thành công", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }

                                @Override
                                public void onCancel() {
                                }

                                @Override
                                public void onError(FacebookException e) {
                                }

                            });

                            if (MessageDialog.canShow(ShareLinkContent.class)) {

                                mDataSended = mText.getEditText().getText().toString();

                                setClipboard(EditAndSend.this, mDataSended);

                                Bitmap imageShare = BitmapFactory.decodeResource(getResources(), R.drawable.tet31);

                                SharePhoto photo = new SharePhoto.Builder()
                                        .setBitmap(imageShare)
                                        .build();

                                SharePhotoContent content = new SharePhotoContent.Builder()
                                        .addPhoto(photo)
                                        .build();

                                messageDialog.show(content);

                                TastyToast.makeText(EditAndSend.this, "Đã copy vào clipboard bạn có thể paste lời chúc!", Toast.LENGTH_LONG, TastyToast.INFO);

                            }

                        } else {

                            TastyToast.makeText(EditAndSend.this, "Bạn chưa đăng nhập Facebook!", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        }

                    } else {

                        TastyToast.makeText(EditAndSend.this, "Bạn chưa cài đặt Facebook Messager!", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    }

                } else {

                    TastyToast.makeText(EditAndSend.this, "Lỗi mạng!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }

            }
        });

    }

    /** --------------------------------- END onCreate ----------------------------------------**/

    /** TODO: SEND SMS WITH SmsManager (multiple part text) **/

    private void sendSMS(List<String> phoneNumber, String message) {

        SmsManager sms = SmsManager.getDefault();
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        ArrayList<String> parts = sms.divideMessage(message);

        ArrayList<PendingIntent> sendList = new ArrayList<>();
        sendList.add(sentPI);

        ArrayList<PendingIntent> deliverList = new ArrayList<>();
        deliverList.add(deliveredPI);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        TastyToast.makeText(EditAndSend.this, "Gửi thành công!",
                                TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        TastyToast.makeText(EditAndSend.this, "Lỗi!",
                                TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        TastyToast.makeText(EditAndSend.this, "Lỗi! Bạn chưa cấp quyền gửi tin nhắn!",
                                TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        TastyToast.makeText(EditAndSend.this, "Lỗi! Không có PDU!",
                                TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        TastyToast.makeText(EditAndSend.this, "Lỗi! Radio tắt!",
                                TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        break;

                }

            }

        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        TastyToast.makeText(EditAndSend.this, "SMS đã được gửi!",
                                TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        break;

                    case Activity.RESULT_CANCELED:
                        TastyToast.makeText(EditAndSend.this, "Lỗi! SMS chưa được gửi!",
                                TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                        break;

                }

            }

        }, new IntentFilter(DELIVERED));

        for ( int i = 0; i < mNumberContactIsSelected.size(); i++) {
            sms.sendMultipartTextMessage(phoneNumber.get(i), null, parts, sendList, deliverList);
        }

    }

    /** -------------------------------------------------------------------------------------- **/


    /** TODO: check connection internet **/
    private boolean CheckConnectInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /** -------------------------------------------------------------------------------------- **/

    /** TODO: copy to clipboard **/
    private void setClipboard(Context context, String text) {

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {

            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);

        } else {

            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);

        }

    }

    /** -------------------------------------------------------------------------------------- **/

    /** TODO: check APP installed **/
    private boolean isAppInstalled ( String packageName ) {

        PackageManager pm = getPackageManager();
        boolean installed = false;

        try {

            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;

        } catch (PackageManager.NameNotFoundException e) {

            installed = false;
        }

        return installed;
    }

    /** -------------------------------------------------------------------------------------- **/

    /** TODO: GET CONTACT **/
    private void getContactList() {

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.i("name", "Name: " + name);
                        nameContact.add(name);
                        //Log.i("number", "Phone Number: " + phoneNo);
                        numberContact.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        }

        if (cur != null) {
            cur.close();
        }

    }

    /** ------------------------------------------------------------------------------------ **/

    private void InitView() {

        mText = findViewById(R.id.etSend);
        mSendSms = findViewById(R.id.bt_sendSms);
        mBt_share = findViewById(R.id.bt_share);
        mLogin = findViewById(R.id.login);
        bt_sendMessage = findViewById(R.id.bt_sendMessage);
        mTbar_editAndSend = findViewById(R.id.tbar_editAndSend);
        mAdView = findViewById(R.id.adView2);

        setSupportActionBar(mTbar_editAndSend);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gửi lời chúc");
        mTbar_editAndSend.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /** TODO: setup Adapter contact **/

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

        List<String> nameList;
        List<String> phoneNumberList;

        public ContactAdapter(List<String> nameList, List<String> phoneNumberList) {
            this.nameList = nameList;
            this.phoneNumberList = phoneNumberList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.mycontact_layout, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder,final int position) {

            holder.nameContact.setText(nameList.get(position));
            holder.checkContact.setText(phoneNumberList.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ( holder.checkContact.isChecked() ) {

                        holder.checkContact.setChecked(false);
                    } else {
                        holder.checkContact.setChecked(true);
                    }

                    if ( holder.checkContact.isChecked() ) {
                        mNumberContactIsSelected.add(holder.checkContact.getText().toString());
                    } else {
                        mNumberContactIsSelected.remove(holder.checkContact.getText().toString());
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return phoneNumberList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CheckBox checkContact;
            TextView nameContact;

            View mView;

            public ViewHolder(View itemView) {
                super(itemView);
                //this.setIsRecyclable(false);
                checkContact = itemView.findViewById(R.id.checkContact);
                nameContact = itemView.findViewById(R.id.nameContact);
                mView = itemView;

            }

        }

    }

}
