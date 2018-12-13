package japscan.gtheurillat.util.picasso;


import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Transformation;

public class ResizeTransformation implements Transformation {

    //la largeur voulue
    private int targetWidth;
    private String targetPosition;

    public ResizeTransformation(int width, String position) {
        this.targetWidth = width;
        this.targetPosition = position;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);
        Bitmap resultScale = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);

        Bitmap result;

        Log.e("Target position", targetPosition);



        if (targetPosition == "left") {
            result = Bitmap.createBitmap(resultScale, 0,0,targetWidth/2, targetHeight);
        }
        else if (targetPosition == "right") {
            result = Bitmap.createBitmap(resultScale, targetWidth/2,0,targetWidth/2, targetHeight);
        }
        else {
            result = resultScale;
        }


        if (result != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "ResizeTransformation"+targetWidth;
    }
}