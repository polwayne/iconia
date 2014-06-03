package de.polwayne.iconia.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Paul on 02.05.14.
 */
public class Utils {

    public static int convertToPx(Context ctx, int dp) {
        // Get the screen's density scale
        final float scale = ctx.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    public static Bitmap createScaledBitmapFromStream( InputStream s, int minimumDesiredBitmapWidth, int minimumDesiredBitmapHeight ) {

        final BufferedInputStream is = new BufferedInputStream(s, 32*1024);
        try {
            final BitmapFactory.Options decodeBitmapOptions = new BitmapFactory.Options();
            // For further memory savings, you may want to consider using this option
            // decodeBitmapOptions.inPreferredConfig = Config.RGB_565; // Uses 2-bytes instead of default 4 per pixel

            if( minimumDesiredBitmapWidth >0 && minimumDesiredBitmapHeight >0 ) {
                final BitmapFactory.Options decodeBoundsOptions = new BitmapFactory.Options();
                decodeBoundsOptions.inJustDecodeBounds = true;
                is.mark(32*1024); // 32k is probably overkill, but 8k is insufficient for some jpgs
                BitmapFactory.decodeStream(is,null,decodeBoundsOptions);
                is.reset();

                final int originalWidth = decodeBoundsOptions.outWidth;
                final int originalHeight = decodeBoundsOptions.outHeight;

                // inSampleSize prefers multiples of 2, but we prefer to prioritize memory savings
                decodeBitmapOptions.inSampleSize= Math.max(1,Math.min(originalWidth / minimumDesiredBitmapWidth, originalHeight / minimumDesiredBitmapHeight));

            }

            return BitmapFactory.decodeStream(is,null,decodeBitmapOptions);

        } catch( IOException e ) {
            throw new RuntimeException(e); // this shouldn't happen
        } finally {
            try {
                is.close();
            } catch( IOException ignored ) {}
        }
    }

    //decodes image and scales it to reduce memory consumption
    public static Bitmap decodeWebImage(String url){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream((InputStream)new URL(url).getContent(),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=300;

            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream((InputStream)new URL(url).getContent(), null, o2);
        } catch (FileNotFoundException e) {}
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return null;
    }

}
