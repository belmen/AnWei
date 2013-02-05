package com.belmen.anwei.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.belmen.anwei.util.MD5;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;

public class ImageLoader {
	
	private File userImageDir;
	
	public ImageLoader(Context context){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            userImageDir = new File(Environment.getExternalStorageDirectory(), "AnWei/userimage");
        else userImageDir = context.getCacheDir();
        
        if(!userImageDir.exists()) userImageDir.mkdirs();
    }

	public Drawable getProfileImage(String url) throws MalformedURLException, IOException {
		
		Drawable pic;
		String filename = MD5.getMD5String(url.getBytes());
		File file = new File(userImageDir, filename);
		if(file.exists()) {
			InputStream is = new FileInputStream(file);
			pic = Drawable.createFromStream(is, filename);
			is.close();
		} else {
			InputStream is = (InputStream) new URL(url).getContent();
			OutputStream os = new FileOutputStream(file);
			Utils.CopyStream(is, os);
			os.close();
			pic = Drawable.createFromStream(is, filename);
		}
		return pic;
	}
}

class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
