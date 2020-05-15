package com.example.modul6mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class select extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextId;
    private EditText editTextNama;
    private EditText editTextJurusan;
    private EditText editTextEmail;
    private EditText editTextAlamat;
    private EditText editTextTelepon;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Intent intent = getIntent();

        id = intent.getStringExtra(Konfigurasi.MHS_ID);
        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextJurusan = (EditText) findViewById(R.id.editTextJurusan);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextAlamat = (EditText) findViewById(R.id.editTextAlamat);
        editTextTelepon = (EditText) findViewById(R.id.editTextTelepon);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        editTextId.setText(id);

        getMahasiswa();
    }
    private void getMahasiswa(){
        class GetMahasiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(select.this,"Fetching...","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showMahasiswa(s);
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_GET_MHS,id);
                return s;
            }
        } GetMahasiswa ge = new GetMahasiswa();
        ge.execute();
    }
    private void showMahasiswa(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String name = c.getString(Konfigurasi.TAG_NAMA);
            String jurusan = c.getString(Konfigurasi.TAG_JURUSAN);
            String email = c.getString(Konfigurasi.TAG_EMAIL);
            String alamat = c.getString(Konfigurasi.TAG_ALAMAT);
            String telepon = c.getString(Konfigurasi.TAG_TELEPON);

            editTextNama.setText(name);
            editTextJurusan.setText(jurusan);
            editTextEmail.setText(email);
            editTextAlamat.setText(alamat);
            editTextTelepon.setText(telepon);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateMahasiswa() {
        final String nama = editTextNama.getText().toString().trim();
        final String jurusan = editTextJurusan.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String alamat = editTextAlamat.getText().toString().trim();
        final String telepon = editTextTelepon.getText().toString().trim();

        class UpdateMahasiswa extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(select.this, "Mengupdate...", "Silahkan Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                loading.dismiss();
                Toast.makeText(select.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_MHS_ID, id);
                hashMap.put(Konfigurasi.KEY_MHS_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_MHS_JURUSAN, jurusan);
                hashMap.put(Konfigurasi.KEY_MHS_EMAIL, email);
                hashMap.put(Konfigurasi.KEY_MHS_ALAMAT, alamat);
                hashMap.put(Konfigurasi.KEY_MHS_TELEPON, telepon);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_UPDATE_MHS, hashMap);

                return s;
            }
        }
        UpdateMahasiswa ue = new UpdateMahasiswa();
        ue.execute();
    }
    private void deleteMahasiswa(){
        class DeleteMahasiswa extends AsyncTask<Void,Void,String>
        {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(select.this, "Mengupdate...", "Silahkan Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(select.this, s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_DELETE_MHS, id);
                return s;
            }
        }
        DeleteMahasiswa de = new DeleteMahasiswa();
        de.execute();
    }
    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda Yakin Ingin Menghapus Mahasiswa ini?");

        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                deleteMahasiswa();
                startActivity(new Intent(select.this,read.class));
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateMahasiswa();
        }

        if(v == buttonDelete){
            confirmDeleteEmployee();
        }
    }
}

