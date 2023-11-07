# Diagnosis

This is a dignosis app checks and testing the device hardwares and sensors are properlly working or not and at the end sending the diagnose data to firebase. Because of minimum time I just woked on or focused on Auto diagnosis feature. Other features can be developed further. 

The App has:

i. Splash Screen : A minimal Splash screen showing the name and description of the app.
ii. Home Screen : Home screen showing just a app rule. Can add more feature of app in which this diagnosis feature is going to integrate.
iii. Diagnosis Activity : This is a Main Activity of the app. In this activity you can see the testings list and at the bottom end a button for sending data to database (Firebase).
iv. Auto Diagnosis : Auto Diagnosis is a quick way to test the device by just one click. Its automatically testing the hardware and sensors you have to just wait for few seconds.
v. Manual Test : Manual test are other testing options which we have to click one by one to test the given sensor. We can't start manual test without Auto Diagnosis. If we want to use Manual testing then we have to skip or continue with Auto Diagnosis.
vi. Sending Data : At the bottom of the Diagnosis activity we see the Send data button which send all the testing result to the Firebase realtime database.

UIs : 
* This app has minimal design strategy.
* CardViews are giving a more attractive view to the activity.
* After testing the colour changes of CardView.
* Progress Bars, Icons Changes and Recycler View is giving it a real time experience.
