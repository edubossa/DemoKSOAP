package com.ews.demoksoap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    private EditText n1;
    private EditText n2;
    private TextView result;

    String METHOD_NAME = "soma";
    String SOAP_ACTION = "";

    String NAMESPACE = "http://calculadora.com.br/fiap";
    String SOAP_URL = "http://172.16.18.106:8080/webservices/calculadora";

    SoapObject request;
    SoapPrimitive calcular;

    ProgressDialog pdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.n1 = (EditText) findViewById(R.id.n1);
        this.n2 = (EditText) findViewById(R.id.n2);
        this.result = (TextView) findViewById(R.id.result);
    }


    public void somar(View view) {
        new CalcularAsync().execute(Integer.valueOf(n1.getText().toString()), Integer.valueOf(n2.getText().toString()));

    }

    //https://github.com/heiderlopes/SomarSOAPP/blob/master/app/src/main/java/br/com/heiderlopes/somarsoapp/MainActivity.java
    private class CalcularAsync extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("n1", params[0]);
            request.addProperty("n2", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
            try {
                httpTransport.call(SOAP_ACTION, envelope);
                calcular = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.getMessage();
                Log.e("CalcularAsync", e.getMessage());
            }

            return  null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pdialog.dismiss();
            //Toast.makeText(getApplicationContext(), "Resultado: " + calcular.toString(), Toast.LENGTH_SHORT).show();
            if (calcular != null)
                result.setText(calcular.toString());
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(MainActivity.this);
            pdialog.setMessage("Converting...");
            pdialog.show();
        }
    }


}
