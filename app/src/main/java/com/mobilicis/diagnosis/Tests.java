package com.mobilicis.diagnosis;

public class Tests {
    String backCameraWorking,frontCameraWorking,primaryMicrophoneWorking,secondaryMicrophoneWorking,bluetoothWorking,rootedStatus,accelerometerWorking,gpsWorking,fingerprintWorking;

    public Tests(){}

    public Tests(String backCameraWorking, String frontCameraWorking, String primaryMicrophoneWorking, String secondaryMicrophoneWorking, String bluetoothWorking, String rootedStatus, String accelerometerWorking, String gpsWorking, String fingerprintWorking) {
        this.backCameraWorking = backCameraWorking;
        this.frontCameraWorking = frontCameraWorking;
        this.primaryMicrophoneWorking = primaryMicrophoneWorking;
        this.secondaryMicrophoneWorking = secondaryMicrophoneWorking;
        this.bluetoothWorking = bluetoothWorking;
        this.rootedStatus = rootedStatus;
        this.accelerometerWorking = accelerometerWorking;
        this.gpsWorking = gpsWorking;
        this.fingerprintWorking = fingerprintWorking;
    }

    public String getBackCameraWorking() {
        return backCameraWorking;
    }

    public void setBackCameraWorking(String backCameraWorking) {
        this.backCameraWorking = backCameraWorking;
    }

    public String getFrontCameraWorking() {
        return frontCameraWorking;
    }

    public void setFrontCameraWorking(String frontCameraWorking) {
        this.frontCameraWorking = frontCameraWorking;
    }

    public String getPrimaryMicrophoneWorking() {
        return primaryMicrophoneWorking;
    }

    public void setPrimaryMicrophoneWorking(String primaryMicrophoneWorking) {
        this.primaryMicrophoneWorking = primaryMicrophoneWorking;
    }

    public String getSecondaryMicrophoneWorking() {
        return secondaryMicrophoneWorking;
    }

    public void setSecondaryMicrophoneWorking(String secondaryMicrophoneWorking) {
        this.secondaryMicrophoneWorking = secondaryMicrophoneWorking;
    }

    public String getBluetoothWorking() {
        return bluetoothWorking;
    }

    public void setBluetoothWorking(String bluetoothWorking) {
        this.bluetoothWorking = bluetoothWorking;
    }

    public String getRootedStatus() {
        return rootedStatus;
    }

    public void setRootedStatus(String rootedStatus) {
        this.rootedStatus = rootedStatus;
    }

    public String getAccelerometerWorking() {
        return accelerometerWorking;
    }

    public void setAccelerometerWorking(String accelerometerWorking) {
        this.accelerometerWorking = accelerometerWorking;
    }

    public String getGpsWorking() {
        return gpsWorking;
    }

    public void setGpsWorking(String gpsWorking) {
        this.gpsWorking = gpsWorking;
    }

    public String getFingerprintWorking() {
        return fingerprintWorking;
    }

    public void setFingerprintWorking(String fingerprintWorking) {
        this.fingerprintWorking = fingerprintWorking;
    }
}
