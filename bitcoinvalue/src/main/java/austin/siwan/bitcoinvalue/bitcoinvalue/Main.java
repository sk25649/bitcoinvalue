package austin.siwan.bitcoinvalue.bitcoinvalue;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Main extends Activity {

    public interface Callback{
        void onComplete();
    }

    private BitcoinValueAPIAsyncTask bitcoinValueAsyncTask;
    private TextView bitcoinValueDisplay, bitcoinTimestamp;
    private Bitcoin bitcoin;
    private Button calcualteBitcoinValue;
    private EditText bitcoinConversion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bitcoinValueDisplay = (TextView)findViewById(R.id.bitcoinValueDisplay);
        bitcoinTimestamp = (TextView)findViewById(R.id.bitcoinTimestamp);
        calcualteBitcoinValue = (Button)findViewById(R.id.calculateBitcoinValue);
        bitcoinConversion = (EditText)findViewById(R.id.bitcoinConversion);

        calcualteBitcoinValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetCurrentBitcoinValue();
            }
        });

        bitcoinConversion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    GetCurrentBitcoinValue();
                }
                    return false;
            }
        });

        //action bar support
        ActionBar actionBar = getActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCurrentBitcoinValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.refresh:
                GetCurrentBitcoinValue();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void GetCurrentBitcoinValue(){
        bitcoinValueAsyncTask = new BitcoinValueAPIAsyncTask(new Callback() {
            @Override
            public void onComplete() {
                try {
                    bitcoin = bitcoinValueAsyncTask.get(1000, TimeUnit.MILLISECONDS);
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss zzz");
                    TimeZone timeZone = TimeZone.getDefault();
                    dateFormat.setTimeZone(timeZone);
                    Date date = new Date();
                    String dateInStr = dateFormat.format(date).toString();
                    String desiredAmount = bitcoinConversion.getText().toString();
                    double calculatedUSDEqualivent =
                            calculateBitcoinUSDEqualivent(desiredAmount, bitcoin.getLast());
                    bitcoinValueDisplay.setText(String.format(" Bitcoin = %.2f USD",
                            calculatedUSDEqualivent));
                    bitcoinTimestamp.setText(String.format("Last Updated: %s",
                            dateInStr));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }, this);
        bitcoinValueAsyncTask.execute();
    }

    private double calculateBitcoinUSDEqualivent(String desireBitcoinAmount, String bitcoinInUsd) {
        double nil = 0.00;

        try {
            double desired = Double.parseDouble(desireBitcoinAmount);
            double usd = Double.parseDouble(bitcoinInUsd);

            if(desired == nil) {
                return nil;
            }

            return(desired*usd);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid number", Toast.LENGTH_SHORT).show();
        }

        return nil;
    }
}