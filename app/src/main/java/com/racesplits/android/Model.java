package com.racesplits.android;

public class Model {

    private int imageView1;
    private String textView1;
    private String textView2;
    private String textView3;

    public Model(int imageView1, String textView1, String textView2, String textView3) {
        this.imageView1 = imageView1;
        this.textView1 = textView1;
        this.textView2 = textView2;
        this.textView3 = textView3;
    }

    public int getImageView1() {
        return imageView1;
    }

    public void setImageView1(int imageView1) {
        this.imageView1 = imageView1;
    }

    public String getTextView1() {
        return textView1;
    }

    public void setTextView1(String textView1) {
        this.textView1 = textView1;
    }

    public String getTextView2() {
        return textView2;
    }

    public void setTextView2(String textView2) {
        this.textView2 = textView2;
    }

    public String getTextView3() {
        return textView3;
    }

    public void setTextView3(String textView3) {
        this.textView3 = textView3;
    }

}
