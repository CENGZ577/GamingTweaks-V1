package com.example.gamingtweaks;

import android.webkit.JavascriptInterface;
import rikka.shizuku.Shizuku;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShizukuBridge {

    @JavascriptInterface
    public String execShell(String command) {
        if (!Shizuku.pingBinder()) {
            return "Error: Shizuku service is not running!";
        }

        try {
            // Menggunakan metode perizinan & eksekusi proses resmi Shizuku
            Process process = Shizuku.newProcess(new String[]{"sh", "-c", command}, null, null);
            
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
