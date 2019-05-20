package com.example.a8ball;

import android.content.Context;

public class Ball {
    public String[] options;

    public Ball(Context ctx) {
        this.options = ctx.getResources().getStringArray(R.array.options);
    }
}
