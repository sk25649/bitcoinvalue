package austin.siwan.bitcoinvalue.bitcoinvalue;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by Jojo on 7/2/14.
 */
public class BitcoinValueAPIAsyncTask extends AsyncTask<Void, Void, Bitcoin> {

    private Main.Callback mCallback;
    private Context context;
    private String currencyCode;

    public BitcoinValueAPIAsyncTask(Main.Callback callback, Context context, String currencyCode) {
        mCallback = callback;
        this.context = context;
        this.currencyCode = currencyCode;
    }

    @Override
    protected Bitcoin doInBackground(Void... voids) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String search_usd =
                String.format("https://api.bitcoinaverage.com/ticker/global/%s", currencyCode);
        String result = RestClient.connect(search_usd);
        JsonElement json = new JsonParser().parse(result);
        Bitcoin bitcoin = gson.fromJson(json, Bitcoin.class);
        return bitcoin;
    }

    @Override
    protected void onPostExecute(Bitcoin bitcoin) {
        mCallback.onComplete();
    }
}