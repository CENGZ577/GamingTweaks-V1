package com.example.gamingtweaks;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import rikka.shizuku.Shizuku;

public class ShizukuBridge {
    private final Context context;
    public ShizukuBridge(Context context) { this.context = context; }

    @JavascriptInterface
    public void runTweak(String command) {
        if (!Shizuku.pingBinder()) { showToast("Shizuku belum berjalan!"); return; }
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(0);
            showToast("Berikan izin Shizuku.");
            return;
        }
        new Thread(() -> {
            try {
                Process p = Shizuku.newProcess(new String[]{"sh", "-c", command}, null, null);
                if (p.waitFor() == 0) showToast("Tweak Berhasil!");
            } catch (Exception e) { showToast("Gagal: " + e.getMessage()); }
        }).start();
    }
    private void showToast(String m) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, m, Toast.LENGTH_SHORT).show());
    }
}
