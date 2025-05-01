package com.example.hosykien211604449;

import android.util.Log;

/**
 * Class Function chứa các phương thức tiện ích chung
 */
public class Function {
    private static final String TAG = "Function";

    /**
     * Gửi dữ liệu hoặc thực hiện hành động chung.
     * Hiện tại không có thiết bị ESP32 cụ thể, chỉ log cho mục đích debug.
     * @param payload Dữ liệu cần xử lý
     * @param topic   Tên định danh
     */
    public static void sendData(String payload, String topic) {
        Log.i(TAG, "sendData called with topic=" + topic + ", payload=" + payload);
    }
}
