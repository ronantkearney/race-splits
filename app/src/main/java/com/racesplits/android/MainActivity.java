package com.racesplits.android;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.racesplits.R;
import com.racesplits.race.Race;
import com.racesplits.racer.Racer;
import com.racesplits.racer.RacerSplitTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Model> modelList;
    Adapter adapter;
    TextInputLayout editTextLayout;
    EditText editText;
    SeekBar seekbar;
    TextView sliderText;
    Race race = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableImmersiveUI();

        modelList = new ArrayList<>();
        initRecyclerView();

        editTextLayout = findViewById(R.id.textinputlayout);
        editText = findViewById(R.id.textinput);
        editText.setTextIsSelectable(false);
        editText.setClickable(true);
        editText.setLongClickable(true);
        editText.setShowSoftInputOnFocus(false);

        sliderText = findViewById(R.id.slidertext);

        seekbar = findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((progress > 95) && (race==null)) {
                    sliderText.setText("Race started. Slide back left to end");
                    editText.setEnabled(true);
                    editText.setFocusedByDefault(true);
                    editText.setFocusableInTouchMode(false);
                    editTextLayout.setVisibility(View.VISIBLE);
                    race = new Race();
                    readRacerFile();
                } else if ((progress < 5) && (race!=null)) {
                    sliderText.setText("The race is over");
                    editText.setEnabled(false);
                    editTextLayout.setVisibility(View.INVISIBLE);
                    seekBar.setEnabled(false);

                    LocalDateTime filenameDateTime = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd_HHmm");
                    final String RESULTS_LIST_FILE_NAME = dtf.format(filenameDateTime)+"_Results_Log_RAW.csv";
                    final String RESULTS_LIST_FILE_DIR = "RaceResults";

                    FileOutputStream fos = null;
                    File externalFile = new File(getExternalFilesDir(RESULTS_LIST_FILE_DIR), RESULTS_LIST_FILE_NAME);
                    try {
                        fos = new FileOutputStream(externalFile);
                        byte[] raceLog = race.getRaceLog().getBytes(StandardCharsets.UTF_8);
                        fos.write(raceLog);
//                        sliderText.setText(raceLog.length+" bytes  written to dir: "+getFilesDir()+"/"+RESULTS_LIST_FILE_NAME);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos!=null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    final String RESULTS_LAPS_FILE_NAME = dtf.format(filenameDateTime)+"_Results_Laps_RAW.csv";
                    final String RESULTS_LAPS_FILE_DIR = "RaceResults";

                    FileOutputStream fos2 = null;
                    File externalFile2 = new File(getExternalFilesDir(RESULTS_LAPS_FILE_DIR), RESULTS_LAPS_FILE_NAME);
                    try {
                        fos2 = new FileOutputStream(externalFile2);
                        byte[] sortedResults = race.getSortedResults().getBytes(StandardCharsets.UTF_8);
                        fos2.write(sortedResults);
//                        sliderText.setText(raceLog.length+" bytes  written to dir: "+getFilesDir()+"/"+RESULTS_LIST_FILE_NAME);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos2!=null) {
                            try {
                                fos2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void readRacerFile() {

        final String RACER_FILENAME = "RacerMaster.csv";
        final String RACER_FILENAME_DIR = "RaceResults";

        FileInputStream fis = null;
        File racerFile = new File(getExternalFilesDir(RACER_FILENAME_DIR), RACER_FILENAME);

        try {
            fis = new FileInputStream(racerFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String bib = tokens[2].trim();
                race.putRacerName(bib, tokens[0]+" "+tokens[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis!=null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

            modelList.add(new Model(R.drawable.ic_time, bibNum, racer.getName(), "Split #"+racer.getSplitCount(), formattedSplitTime));
            adapter.notifyItemInserted(modelList.size()-1);
        }
        recyclerView.scrollToPosition(modelList.size()-1);
        return true;
    }

    private String getFormattedSplitForRacer(Racer racer) {

        RacerSplitTime latestSplit = racer.getLatestSplit();
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
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
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