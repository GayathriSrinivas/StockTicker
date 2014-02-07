package com.cmpe.assignment1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private Button btn_stock;
	private Button btn_sjsu;
	private EditText et_stock;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Setting Listener for the buttons 
        btn_stock = (Button)findViewById(R.id.btn_stock);
        btn_sjsu = (Button)findViewById(R.id.btn_sjsu);
        btn_stock.setOnClickListener(this);    
        btn_sjsu.setOnClickListener(this); 
        et_stock = (EditText)findViewById(R.id.et_symbol);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_stock:
			String stockSymbol = et_stock.getText().toString().trim();
			if(stockSymbol.equals("")){
				Toast.makeText(this,"Please Enter Stock Symbol",Toast.LENGTH_SHORT).show();
				break;
			}
			Toast.makeText(this,"Fetching Stock Price",Toast.LENGTH_SHORT).show();
			new HttpGetRequest((TextView)findViewById(R.id.tv_result),stockSymbol).execute();
			break;
			
		case R.id.btn_sjsu:
			Toast.makeText(this,"Fetching SJSU website !! ",Toast.LENGTH_SHORT).show();
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sjsu.edu"));
			startActivity(browserIntent);
			break;

		default:
			break;
		}
	}
}

 class HttpGetRequest extends AsyncTask<Void, Void, String>{

	private TextView result;
	private String stockSymbol; 
	
	public HttpGetRequest(TextView result,String stockSymbol){
		this.result = result;
		this.stockSymbol = stockSymbol;
	}
	 
	 
	@Override
	protected String doInBackground(Void... params) {
		
		String url = "http://finance.yahoo.com/d/quotes.csv?s="+stockSymbol+"&f=l1";
		String stockPrice = null;
		  
		  try {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response = httpclient.execute(new HttpGet(url));
		    InputStream in = response.getEntity().getContent();
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    stockPrice = br.readLine();
		  } catch (Exception e) {
		    Log.i("[GET REQUEST]", "Network exception", e);
		  }
		    return stockPrice;
	}
	
    protected void onPostExecute(String stockPrice) {
      result.setText("StockPrice is $"+stockPrice );
    }
	
}
