package io.qthjen_dev.tet.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.sdsmdg.tastytoast.TastyToast;
import java.util.ArrayList;
import java.util.List;
import io.qthjen_dev.tet.R;

public class ListDataActivity extends AppCompatActivity {

    private AdapterListData mAdapter;
    private List<String> mArrayData = new ArrayList<>();
    private Toolbar mTbarListData;
    private ImageView mClearSearch;

    private RecyclerView mRecycler;

    private AutoCompleteTextView mSearch;

    private int mPosition;

//    private int PERMISSION_REQUEST = 1;
//    private static int PERMISSION_REQUEST2 = 2;
//
//    private static int POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        FindView();
        Slidr.attach(this);

        mPosition = getIntent().getExtras().getInt("position");

        switch (mPosition) {

            case 0:GetDataChame();break;
            case 1:GetDataThayco();break;
            case 2:GetDataNy();break;
            case 3:GetDataTho();break;
            case 4:GetDataHai();break;
            case 5:GetDataHay();break;
            case 6:GetDataEnligh();break;
            case 7:GetDataJapan();break;

        }

        LinearLayoutManager mLayout_manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayout_manager);
        mRecycler.setHasFixedSize(true);

        mAdapter = new AdapterListData(this,mArrayData);
        mRecycler.setAdapter(mAdapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mArrayData);

        //mSearch.setText("");
        mSearch.setThreshold(1);
        mSearch.setAdapter(arrayAdapter);

        mSearch.setMaxLines(1);
        mSearch.setEllipsize(TextUtils.TruncateAt.END);

        mSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //ActivityCompat.requestPermissions(ListDataActivity.this,new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST);

//                Intent intent = new Intent(ListDataActivity.this, EditAndSend.class);
//                intent.putExtra("mydata", mSearch.getText().toString());
//                startActivity(intent);

                if ( CheckPermission(Manifest.permission.SEND_SMS) &&
                        CheckPermission(Manifest.permission.CALL_PHONE)
                        && CheckPermission(Manifest.permission.READ_CONTACTS) ) {

                    Intent intent = new Intent(ListDataActivity.this, EditAndSend.class);
                    intent.putExtra("mydata", mSearch.getText().toString());
                    startActivity(intent);

                } else {

                    TastyToast.makeText(ListDataActivity.this, "Bạn chưa cho quyền gửi tin nhắn và đọc danh bạ! Làm ơn bật lên trong mục quyền(permission)", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }

            }
        });

        mClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSearch.setText("");

            }
        });

    }

    private void GetDataEnligh() {

        mArrayData.clear();
        String[] listdata = getResources().getStringArray(R.array.english);
        for ( int i = 0; i < listdata.length; i++) {
            mArrayData.add(listdata[i]);
        }

    }

    private void GetDataChame() {

        mArrayData.clear();
        String[] listdata = getResources().getStringArray(R.array.chame);
        for ( int i = 0; i < listdata.length; i++) {
            mArrayData.add(listdata[i]);
        }

    }

    private void GetDataNy() {

        mArrayData.clear();
        String[] listdata = getResources().getStringArray(R.array.ny);
        for ( int i = 0; i < listdata.length; i++) {
            mArrayData.add(listdata[i]);
        }

    }

    private void GetDataJapan() {

        mArrayData.clear();
        String[] listdata = getResources().getStringArray(R.array.jopen);
        for ( int i = 0; i < listdata.length; i++) {
            mArrayData.add(listdata[i]);
        }

    }

    private void GetDataHay() {

        mArrayData.clear();
        String[] listdata = getResources().getStringArray(R.array.hay);
        for ( int i = 0; i < listdata.length; i++) {
            mArrayData.add(listdata[i]);
        }

    }

    private void GetDataHai() {

        mArrayData.clear();
        String[] listdata = getResources().getStringArray(R.array.haihuoc);
        for ( int i = 0; i < listdata.length; i++) {
            mArrayData.add(listdata[i]);
        }

    }

    private void GetDataTho() {

        mArrayData.clear();
        String[] listdata = getResources().getStringArray(R.array.tho);
        for ( int i = 0; i < listdata.length; i++) {
            mArrayData.add(listdata[i]);
        }

    }

    private void GetDataThayco() {

        mArrayData.clear();
        String[] listdata = getResources().getStringArray(R.array.thayco);
        for ( int i = 0; i < listdata.length; i++) {
            mArrayData.add(listdata[i]);
        }

    }

    private void FindView() {

        mRecycler = findViewById(R.id.recyclerDataList);
        mSearch = findViewById(R.id.mySearch_view);
        mTbarListData = findViewById(R.id.tbar_listData);
        mClearSearch = findViewById(R.id.clearSearch);

        setSupportActionBar(mTbarListData);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lời chúc");
        mTbarListData.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean CheckPermission(String permission) {

        int res = checkCallingOrSelfPermission(permission);

        return (res == PackageManager.PERMISSION_GRANTED);
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if ( requestCode == PERMISSION_REQUEST ) {

            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED ) {

                Intent intent = new Intent(ListDataActivity.this, EditAndSend.class);
                intent.putExtra("mydata", mSearch.getText().toString());
                startActivity(intent);

            } else {

                TastyToast.makeText(ListDataActivity.this, "Bạn chưa cấp các quyền cần thiết để ứng dụng có thể hoạt động!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

        } else if ( requestCode == PERMISSION_REQUEST2 ) {

            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED ) {

                Intent intent = new Intent(ListDataActivity.this, EditAndSend.class);
                intent.putExtra("mydata", mArrayData.get(POSITION));
                startActivity(intent);

            } else {

                TastyToast.makeText(ListDataActivity.this, "Bạn chưa cấp các quyền cần thiết để ứng dụng có thể hoạt động!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
*/
class AdapterListData extends RecyclerView.Adapter<AdapterListData.ViewHolder> {

        private Context context;
        private List<String> arrayData;

        public AdapterListData(Context context, List<String> arrayData ) {
            this.context = context;
            this.arrayData = arrayData;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.datalist_layout, parent, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.tv_mau.setMaxLines(2);
            holder.tv_mau.setEllipsize(TextUtils.TruncateAt.END);
            holder.tv_mau.setText(arrayData.get(position));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ( CheckPermission(Manifest.permission.SEND_SMS) &&
                            CheckPermission(Manifest.permission.CALL_PHONE)
                            && CheckPermission(Manifest.permission.READ_CONTACTS)) {

                        Intent intent = new Intent(context, EditAndSend.class);
                        intent.putExtra("mydata", holder.tv_mau.getText());
                        context.startActivity(intent);

                    } else {

                        TastyToast.makeText(ListDataActivity.this, "Bạn chưa cho quyền gửi tin nhắn và đọc danh bạ! Làm ơn bật lên trong mục quyền(permission)", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_mau;
            View view;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_mau = itemView.findViewById(R.id.tv_mau);
                view = itemView;
            }

        }

    }

}
