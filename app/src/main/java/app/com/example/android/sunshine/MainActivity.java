package app.com.example.android.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import app.com.example.android.sunshine.sync.SunshineSyncAdapter;


public class MainActivity extends ActionBarActivity implements forecastFragment.Callback {

    private boolean mTwoPane;

    private String mLocation;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocation = Utility.getPreferredLocation(this);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailsActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        forecastFragment forecastFragment = ((forecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast));
        forecastFragment.setUseTodayLayout(!mTwoPane);
        if (mTwoPane == false) {
            getSupportActionBar().setElevation(0f);
        }

        SunshineSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        if (location != null && !location.equals(mLocation)) {
            forecastFragment ff = (forecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if (ff != null) {
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {

            Bundle args = new Bundle();
            args.putParcelable(DetailsActivityFragment.DETAIL_URI, contentUri);

            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class).setData(contentUri);
            startActivity(intent);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}