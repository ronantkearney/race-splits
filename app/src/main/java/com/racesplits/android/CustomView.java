package com.racesplits.android;

import android.content.Context;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

public class CustomView extends View {

    SpannableStringBuilder  mText;

    public CustomView(Context context) {
        this(context, null, 0);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFocusableInTouchMode(true);
        mText = new SpannableStringBuilder();

        // handle key presses not handled by the InputConnection
        setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (event.getUnicodeChar() == 0) { // control character

                        if (keyCode == KeyEvent.KEYCODE_DEL) {
                            mText.delete(mText.length() - 1, mText.length());
                            Log.i("TAG", "text: " + mText + " (keycode)");
                            return true;
                        }
                        // TODO handle any other control keys here
                    } else { // text character
                        mText.append((char)event.getUnicodeChar());
                        Log.i("TAG", "text: " + mText + " (keycode)");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    // toggle whether the keyboard is showing when the view is clicked
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_TEXT;
        // outAttrs.inputType = InputType.TYPE_CLASS_NUMBER; // alternate (show number pad rather than text)
        return new CustomInputConnection(this, true);
    }
}
