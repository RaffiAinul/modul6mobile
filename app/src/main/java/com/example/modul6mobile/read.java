package com.example.modul6mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class read extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
    }

    private void showMahasiswa(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Konfigurasi.TAG_ID);
                String nama = jo.getString(Konfigurasi.TAG_NAMA);
                String jurusan = jo.getString(Konfigurasi.TAG_JURUSAN);
                String email = jo.getString(Konfigurasi.TAG_EMAIL);
                String alamat = jo.getString(Konfigurasi.TAG_ALAMAT);
                String telepon = jo.getString(Konfigurasi.TAG_TELEPON);

                HashMap<String,String> mahasiswa = new HashMap<>();
                mahasiswa.put(Konfigurasi.TAG_ID,id);
                mahasiswa.put(Konfigurasi.TAG_NAMA,nama);
                mahasiswa.put(Konfigurasi.TAG_JURUSAN,jurusan);
                mahasiswa.put(Konfigurasi.TAG_EMAIL,email);
                mahasiswa.put(Konfigurasi.TAG_ALAMAT,alamat);
                mahasiswa.put(Konfigurasi.TAG_TELEPON,telepon);

                list.add(mahasiswa);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                read.this, list, R.layout.list_item,
                new String[]{Konfigurasi.TAG_ID,Konfigurasi.TAG_NAMA,Konfigurasi.TAG_JURUSAN,Konfigurasi.TAG_EMAIL,Konfigurasi.TAG_ALAMAT,Konfigurasi.TAG_TELEPON},
                new int[]{R.id.id, R.id.name, R.id.jurusan, R.id.email, R.id.alamat, R.id.telepon});
        listView.setAdapter(adapter);
    }
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(read.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showMahasiswa();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, select.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String mhsId = map.get(Konfigurasi.TAG_ID).toString();
        intent.putExtra(Konfigurasi.MHS_ID,mhsId);
        startActivity(intent);
    }
}


