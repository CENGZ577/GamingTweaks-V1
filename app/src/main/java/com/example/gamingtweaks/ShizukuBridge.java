package com.example.gamingtweaks;

import android.content.Context;
import android.webkit.JavascriptInterface;
import rikka.shizuku.Shizuku;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class ShizukuBridge {

    private Context context;

    // 1. Tambahkan konstruktor yang menerima Context/MainActivity
    public ShizukuBridge(Context context) {
        this.context = context;
    }

    public ShizukuBridge() {
    }

    @JavascriptInterface
    public String execShell(String command) {
        if (!Shizuku.pingBinder()) {
            return "Error: Shizuku service is not running!";
        }

        try {
            // 2. Tembus pembatasan akses newProcess menggunakan Java Reflection
            Method newProcessMethod = Shizuku.class.getDeclaredMethod("newProcess", String[].class, String[].class, String.class);
            newProcessMethod.setAccessible(true);
            Process process = (Process) newProcessMethod.invoke(null, new String[]{"sh", "-c", command}, null, null);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            return output.toString().trim();
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }
}
