package com.racesplits.android;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;

public class CustomInputConnection extends BaseInputConnection {

    private SpannableStringBuilder mEditable;

    CustomInputConnection(View targetView, boolean fullEditor) {
        super(targetView, fullEditor);
        CustomView customView = (CustomView) targetView;
        mEditable = customView.mText;
    }

    @Override
    public Editable getEditable() {
        return mEditable;
    }

    // just adding this to show that text is being committed.
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        boolean returnValue = super.commitText(text, newCursorPosition);
        Log.i("TAG", "text: " + mEditable);
        return returnValue;
    }
}
