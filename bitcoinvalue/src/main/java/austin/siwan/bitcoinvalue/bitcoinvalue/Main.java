package austin.siwan.bitcoinvalue.bitcoinvalue;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bitcoinValueDisplay = (TextView)findViewById(R.id.bitcoinValueDisplay);
        bitcoinTimestamp = (TextView)findViewById(R.id.bitcoinTimestamp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCurrentBitcoinValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void GetCurrentBitcoinValue(){
        bitcoinValueAsyncTask = new BitcoinValueAPIAsyncTask(new Callback() {
            @Override
            public void onComplete() {
                try {
                    bitcoin = bitcoinValueAsyncTask.get(1000, TimeUnit.MILLISECONDS);
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss zzz");
//                    TimeZone central = TimeZone.getTimeZone("America/Chicago");
                    TimeZone timeZone = TimeZone.getDefault();
                    dateFormat.setTimeZone(timeZone);
                    Date date = new Date();
                    String dateInStr = dateFormat.format(date).toString();
                    bitcoinValueDisplay.setText(String.format("1 Bitcoin = %s USD",
                            bitcoin.getLast()));
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
}
