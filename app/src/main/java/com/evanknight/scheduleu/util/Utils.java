package com.evanknight.scheduleu.util;

import android.content.Context;

public class Utils {
    // public static int convertPixels(int dp){ return dp * (440 / 160); }
    public static int convertPixels(Context  context, int dp_needed){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp_needed * scale + 0.5f);
    }

    public static String formattedTrace(StackTraceElement[] traceArray){
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement ste : traceArray){
            builder.append(ste.toString()).append("\n");
        }
        return builder.toString();
    }

}

