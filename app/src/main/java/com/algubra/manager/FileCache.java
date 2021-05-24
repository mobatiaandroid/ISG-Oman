package com.algubra.manager;

import android.content.Context;

import java.io.File;

/**
 * Created by gayatri on 18/5/17.
 */
public class FileCache {

    /** The cache dir. */
    private File cacheDir;

    /**
     * Instantiates a new file cache.
     *
     * @param context the context
     * @param dir the dir
     */
    public FileCache(Context context, String dir) {
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(), dir);
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }
    /**
     * Instantiates a new file directory in external storage.
     *
     * @param context the context
     * @param dir the dir
     */
    public FileCache(Context context, String dir,Boolean notNeedPrivateDirecory) {
        // Find the dir to save cached images
        if(notNeedPrivateDirecory){
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED))
                cacheDir = new File(
                        android.os.Environment.getExternalStorageDirectory(), dir);
        }
        else{


            cacheDir = new File( context.getFilesDir(), dir);

        }

        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }
    /**
     * Gets the file.
     *
     * @param url the url
     * @return the file
     */
    public File getFile(String url) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.
        String filename = String.valueOf(url.hashCode());
        // Another possible solution (thanks to grantland)
        // String filename = URLEncoder.encode(url);

        File f = new File(cacheDir, filename);
        return f;

    }
	/*public File getFile(String url,String type) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);

		File f = new File(cacheDir, filename+type);
		return f;

	}*/

    public File getFileWithName(String fileName) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.
        String filename = String.valueOf(fileName);
        // Another possible solution (thanks to grantland)
        // String filename = URLEncoder.encode(url);

        File f = new File(cacheDir, filename);
        return f;

    }

    /**
     * Clear.
     */
    public void clear() {
        File[] files = cacheDir.listFiles();
        for (File f : files) {
            deleteRecursive(f);
        }
    }

    /**
     * Delete recursive.
     *
     * @param fileOrDirectory the file or directory
     */
    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        fileOrDirectory.delete();
    }

}