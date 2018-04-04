package com.example.usuario.yyy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends Activity {

    private EditText editText;
    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.tabla);
        editText = (EditText) findViewById(R.id.numero);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = editText.getText().toString();
                ComunicationTask com = new ComunicationTask();
                com.execute("10.225.92.134","9800",num);
            }
        });

    }

    private class ComunicationTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            BufferedReader bf=null;
            Socket sc=null;

            String res="";

            try {

                sc=new Socket(params[0],Integer.parseInt(params[1]));
                PrintStream ps=new PrintStream(sc.getOutputStream());
                ps.println(params[2]);

                ps.flush();
                InputStream is=sc.getInputStream();
                bf=new BufferedReader(new InputStreamReader(is));
                res=bf.readLine();


            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(bf!=null){
                    try {
                        bf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(sc!=null){
                    try {
                        sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return res;
        }

        @Override
        protected void onPostExecute(String s) {
         //   super.onPostExecute(s);5
            Integer[] tabla=null;
            try {
                JSONArray jsonArray=new JSONArray(s);
                tabla=new Integer[jsonArray.length()];
                for(int i=0;i<jsonArray.length();i++){
                    tabla[i]=jsonArray.getInt(i);
                }

                cargarLista(tabla);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private void cargarLista(Integer[] tabla) {
            ArrayAdapter<Integer> adapter=new ArrayAdapter<Integer>(MainActivity.this,android.R.layout.simple_list_item_1,tabla);
        listView.setAdapter(adapter);

        }


    }
}
