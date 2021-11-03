package com.racesplits.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.racesplits.R;
import com.racesplits.race.Race;
import com.racesplits.racer.Racer;
import com.racesplits.racer.RacerSplitTime;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Model> modelList;
    Adapter adapter;
    EditText editText;
    Race race = new Race();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableImmersiveUI();

        modelList = new ArrayList<>();
        initRecyclerView();

        editText = (EditText) findViewById(R.id.textinput);
        editText.setTextIsSelectable(false);
        editText.setClickable(true);
        editText.setLongClickable(true);
        editText.setShowSoftInputOnFocus(false);
    }

    private void enableImmersiveUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private boolean handleEnter() {

        String bibNum = parseBibNum(editText.getText().toString());
        editText.setText(null);

        if (!bibNum.equals("") && !bibNum.equals("0")) {
            race.addTimeEntryForRacer(bibNum);
            Racer racer = race.getRacer(bibNum);
            String formattedSplitTime = getFormattedSplitForRacer(racer);

            modelList.add(new Model(R.drawable.ic_time, bibNum, "Split #"+racer.getSplitCount(), formattedSplitTime));
            adapter.notifyItemInserted(modelList.size()-1);
        }
        recyclerView.scrollToPosition(modelList.size()-1);
        return true;
    }

    private String getFormattedSplitForRacer(Racer racer) {

        RacerSplitTime latestSplit = racer.getLatestSplit();
        final int splitCount = racer.getSplitCount();
        Duration durationSincePreviousSplit = latestSplit.getDurationSincePreviousSplit();

        long minutes = durationSincePreviousSplit.toMinutes();
        if (minutes==0) {
            return String.format("%02d seconds",durationSincePreviousSplit.getSeconds() % 60);
        } else if (minutes==1) {
            return String.format("%d minute %02d seconds", minutes, durationSincePreviousSplit.getSeconds() % 60);
        } else {
            return String.format("%d minutes %02d seconds", minutes, durationSincePreviousSplit.getSeconds() % 60);
        }
    }

    private String parseBibNum(String bibNum) {
        if (bibNum.equals("")) {
            return "";
        } else {
            return new Integer(bibNum).toString();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_EQUALS:
                return handleEnter();
            case KeyEvent.KEYCODE_NUMPAD_0:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_1:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_2:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_3:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_4:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_5:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_6:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_7:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_8:
                return super.onKeyUp(keyCode, event);
            case KeyEvent.KEYCODE_NUMPAD_9:
                return super.onKeyUp(keyCode, event);
            default:
                return true;
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(modelList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
    }
}