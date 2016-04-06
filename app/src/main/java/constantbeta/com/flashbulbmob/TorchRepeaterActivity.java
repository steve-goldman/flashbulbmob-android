package constantbeta.com.flashbulbmob;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TorchRepeaterActivity extends AppCompatActivity
{
    private static final int MinMillis = 100;
    private static final int MaxMillis = 5000;

    private FlashToggler flashToggler;
    private TorchRepeater torchRepeater;

    private EditText onMillisEditText;
    private EditText offMillisEditText;

    private Button canStartButton;
    private Button cannotStartButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch_repeater);

        setupToolbar();
        setupFlashToggler();
        setupTextInputs();
        setupBoundariesLabel();
        setupButtons();
    }

    private void setupToolbar()
    {
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupTextInputs()
    {
        onMillisEditText = (EditText) findViewById(R.id.on_millis);
        onMillisEditText.addTextChangedListener(millisWatcher);

        offMillisEditText = (EditText) findViewById(R.id.off_millis);
        offMillisEditText.addTextChangedListener(millisWatcher);
    }

    private void setupBoundariesLabel()
    {
        TextView boundariesLabel = (TextView) findViewById(R.id.boundaries_label);
        final String templateText = boundariesLabel.getText().toString();
        final String text = templateText.replace("MIN", "" + MinMillis)
                                        .replace("MAX", "" + MaxMillis);
        boundariesLabel.setText(text);
    }

    private void setupButtons()
    {
        canStartButton = (Button)findViewById(R.id.can_start_button);
        cannotStartButton = (Button)findViewById(R.id.cannot_start_button);
        cannotStartButton.setVisibility(View.GONE);
        stopButton = (Button)findViewById(R.id.stop_button);
        disableAndHide(stopButton);
    }

    private void setupFlashToggler()
    {
        try
        {
            flashToggler = new FlashTogglerDeprecated(getApplicationContext());
        }
        catch (final Exception e)
        {
            // no-op
        }
    }

    private Integer editTextInt(final EditText editText)
    {
        try
        {
            return Integer.parseInt(editText.getText().toString().trim());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private Integer onMillis()
    {
        return editTextInt(onMillisEditText);
    }

    private Integer offMillis()
    {
        return editTextInt(offMillisEditText);
    }

    private boolean canStart()
    {
        return validTime(onMillis()) && validTime(offMillis());
    }

    private boolean validTime(final Integer millis)
    {
        return millis != null && millis >= MinMillis && millis <= MaxMillis;
    }

    private void disableAndHide(final Button button)
    {
        button.setVisibility(View.GONE);
        button.setEnabled(false);
    }

    private void enableAndShow(final Button button)
    {
        button.setVisibility(View.VISIBLE);
        button.setEnabled(true);
    }

    private void disableInputs()
    {
        onMillisEditText.setEnabled(false);
        offMillisEditText.setEnabled(false);
    }

    private void enableInputs()
    {
        onMillisEditText.setEnabled(true);
        offMillisEditText.setEnabled(true);
    }

    @Override
    public void onDestroy()
    {
        if (flashToggler != null)
        {
            flashToggler.destroy();
        }
        super.onDestroy();
    }

    public void startPressed(View view)
    {
        dismissKeyboard();
        disableAndHide(canStartButton);
        enableAndShow(stopButton);
        disableInputs();
        torchRepeater = new TorchRepeater(flashToggler, onMillis(), offMillis());
        torchRepeater.start();
    }

    public void stopPressed(View view)
    {
        torchRepeater.stop();
        enableInputs();
        disableAndHide(stopButton);
        enableAndShow(canStartButton);
    }

    private void dismissKeyboard()
    {
        if (getCurrentFocus() != null)
        {
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private final TextWatcher millisWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (canStart())
            {
                cannotStartButton.setVisibility(View.GONE);
                enableAndShow(canStartButton);
            }
            else
            {
                disableAndHide(canStartButton);
                cannotStartButton.setVisibility(View.VISIBLE);
            }
        }
    };
}
