package com.mg.outad.v4;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wuqiyan on 17/7/28.
 */

public class FileProvider extends ContentProvider {
    private static final String[] COLUMNS = new String[]{"_display_name", "_size"};
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_ROOT_PATH = "root-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final File DEVICE_ROOT = new File("/");
    private static HashMap<String, PathStrategy> sCache = new HashMap();
    private FileProvider.PathStrategy mStrategy;

    public FileProvider() {
    }

    public boolean onCreate() {
        return true;
    }

    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        if(info.exported) {
            throw new SecurityException("Provider must not be exported");
        } else if(!info.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        } else {
            this.mStrategy = getPathStrategy(context, info.authority);
        }
    }

    public static Uri getUriForFile(Context context, String authority, File file) {
        FileProvider.PathStrategy strategy = getPathStrategy(context, authority);
        return strategy.getUriForFile(file);
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        File file = this.mStrategy.getFileForUri(uri);
        if(projection == null) {
            projection = COLUMNS;
        }

        String[] cols = new String[projection.length];
        Object[] values = new Object[projection.length];
        int i = 0;
        String[] cursor = projection;
        int len$ = projection.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String col = cursor[i$];
            if("_display_name".equals(col)) {
                cols[i] = "_display_name";
                values[i++] = file.getName();
            } else if("_size".equals(col)) {
                cols[i] = "_size";
                values[i++] = Long.valueOf(file.length());
            }
        }

        cols = copyOf(cols, i);
        values = copyOf(values, i);
        MatrixCursor var14 = new MatrixCursor(cols, 1);
        var14.addRow(values);
        return var14;
    }

    public String getType(Uri uri) {
        File file = this.mStrategy.getFileForUri(uri);
        int lastDot = file.getName().lastIndexOf(46);
        if(lastDot >= 0) {
            String extension = file.getName().substring(lastDot + 1);
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if(mime != null) {
                return mime;
            }
        }

        return "application/octet-stream";
    }

    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("No external inserts");
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("No external updates");
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        File file = this.mStrategy.getFileForUri(uri);
        return file.delete()?1:0;
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        File file = this.mStrategy.getFileForUri(uri);
        int fileMode = modeToMode(mode);
        return ParcelFileDescriptor.open(file, fileMode);
    }

    private static FileProvider.PathStrategy getPathStrategy(Context context, String authority) {
        HashMap var3 = sCache;
        synchronized(sCache) {
            FileProvider.PathStrategy strat = (FileProvider.PathStrategy)sCache.get(authority);
            if(strat == null) {
                try {
                    strat = parsePathStrategy(context, authority);
                } catch (IOException var6) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", var6);
                } catch (XmlPullParserException var7) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", var7);
                }

                sCache.put(authority, strat);
            }

            return strat;
        }
    }

    private static FileProvider.PathStrategy parsePathStrategy(Context context, String authority) throws IOException, XmlPullParserException {
        FileProvider.SimplePathStrategy strat = new FileProvider.SimplePathStrategy(authority);
        ProviderInfo info = context.getPackageManager().resolveContentProvider(authority, 128);
        XmlResourceParser in = info.loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if(in == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        } else {
            int type;
            while((type = in.next()) != 1) {
                if(type == 2) {
                    String tag = in.getName();
                    String name = in.getAttributeValue((String)null, "name");
                    String path = in.getAttributeValue((String)null, "path");
                    File target = null;
                    if("root-path".equals(tag)) {
                        target = buildPath(DEVICE_ROOT, new String[]{path});
                    } else if("files-path".equals(tag)) {
                        target = buildPath(context.getFilesDir(), new String[]{path});
                    } else if("cache-path".equals(tag)) {
                        target = buildPath(context.getCacheDir(), new String[]{path});
                    } else if("external-path".equals(tag)) {
                        target = buildPath(Environment.getExternalStorageDirectory(), new String[]{path});
                    }

                    if(target != null) {
                        strat.addRoot(name, target);
                    }
                }
            }

            return strat;
        }
    }

    private static int modeToMode(String mode) {
        int modeBits;
        if("r".equals(mode)) {
            modeBits = 268435456;
        } else if(!"w".equals(mode) && !"wt".equals(mode)) {
            if("wa".equals(mode)) {
                modeBits = 704643072;
            } else if("rw".equals(mode)) {
                modeBits = 939524096;
            } else {
                if(!"rwt".equals(mode)) {
                    throw new IllegalArgumentException("Invalid mode: " + mode);
                }

                modeBits = 1006632960;
            }
        } else {
            modeBits = 738197504;
        }

        return modeBits;
    }

    private static File buildPath(File base, String... segments) {
        File cur = base;
        String[] arr$ = segments;
        int len$ = segments.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String segment = arr$[i$];
            if(segment != null) {
                cur = new File(cur, segment);
            }
        }

        return cur;
    }

    private static String[] copyOf(String[] original, int newLength) {
        String[] result = new String[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    private static Object[] copyOf(Object[] original, int newLength) {
        Object[] result = new Object[newLength];
        System.arraycopy(original, 0, result, 0, newLength);
        return result;
    }

    static class SimplePathStrategy implements FileProvider.PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        public SimplePathStrategy(String authority) {
            this.mAuthority = authority;
        }

        public void addRoot(String name, File root) {
            if(TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name must not be empty");
            } else {
                try {
                    root = root.getCanonicalFile();
                } catch (IOException var4) {
                    throw new IllegalArgumentException("Failed to resolve canonical path for " + root, var4);
                }

                this.mRoots.put(name, root);
            }
        }

        public Uri getUriForFile(File file) {
            String path;
            try {
                path = file.getCanonicalPath();
            } catch (IOException var7) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }

            Map.Entry mostSpecific = null;
            Iterator rootPath = this.mRoots.entrySet().iterator();

            while(true) {
                Map.Entry root;
                String rootPath1;
                do {
                    do {
                        if(!rootPath.hasNext()) {
                            if(mostSpecific == null) {
                                throw new IllegalArgumentException("Failed to find configured root that contains " + path);
                            }

                            String rootPath2 = ((File)mostSpecific.getValue()).getPath();
                            if(rootPath2.endsWith("/")) {
                                path = path.substring(rootPath2.length());
                            } else {
                                path = path.substring(rootPath2.length() + 1);
                            }

                            path = Uri.encode((String)mostSpecific.getKey()) + '/' + Uri.encode(path, "/");
                            return (new Uri.Builder()).scheme("content").authority(this.mAuthority).encodedPath(path).build();
                        }

                        root = (Map.Entry)rootPath.next();
                        rootPath1 = ((File)root.getValue()).getPath();
                    } while(!path.startsWith(rootPath1));
                } while(mostSpecific != null && rootPath1.length() <= ((File)mostSpecific.getValue()).getPath().length());

                mostSpecific = root;
            }
        }

        public File getFileForUri(Uri uri) {
            String path = uri.getEncodedPath();
            int splitIndex = path.indexOf(47, 1);
            String tag = Uri.decode(path.substring(1, splitIndex));
            path = Uri.decode(path.substring(splitIndex + 1));
            File root = (File)this.mRoots.get(tag);
            if(root == null) {
                throw new IllegalArgumentException("Unable to find configured root for " + uri);
            } else {
                File file = new File(root, path);

                try {
                    file = file.getCanonicalFile();
                } catch (IOException var8) {
                    throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
                }

                if(!file.getPath().startsWith(root.getPath())) {
                    throw new SecurityException("Resolved path jumped beyond configured root");
                } else {
                    return file;
                }
            }
        }
    }

    interface PathStrategy {
        Uri getUriForFile(File var1);

        File getFileForUri(Uri var1);
    }
}
