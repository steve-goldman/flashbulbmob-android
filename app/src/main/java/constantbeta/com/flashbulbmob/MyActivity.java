package constantbeta.com.flashbulbmob;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.constantbeta.flashbulbmob.MESSAGE";
    private FlashToggler flashToggler;
    private Timer repeatingTimer;
    private final List<Long> onDeltas = new ArrayList<Long>();
    private final List<Long> offDeltas = new ArrayList<Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            flashToggler = new FlashTogglerDeprecated(getApplicationContext());
        }
        catch (final Exception e) {
            AlertDialog alert = new AlertDialog.Builder(MyActivity.this)
                    .setTitle("Error")
                    .setMessage("Device does not support flash")
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            alert.show();
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatingTimer = new Timer();
                repeatingTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (repeatingTimer == null) {
                            return;
                        }

                        final long startTime = getCurrentTimeMicros();
                        flashToggler.toggle();
                        final long delta = getCurrentTimeMicros() - startTime;
                        if (flashToggler.isFlashOn()) {
                            onDeltas.add(delta);
                        } else {
                            offDeltas.add(delta);
                        }
                    }
                }, 100, 100);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        repeatingTimer.cancel();
                        repeatingTimer = null;
                        flashToggler.toggleOff();
                        System.out.println("Deltas:");
                        for (int i = 0; i < Math.min(onDeltas.size(), offDeltas.size()); i++)
                            System.out.println(onDeltas.get(i) + "," + offDeltas.get(i));
                    }
                }, 100000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (flashToggler != null) {
            flashToggler.destroy();
        }
        super.onDestroy();
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText)findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private long getCurrentTimeMicros() {
        return System.nanoTime() / 1000;
    }
}
