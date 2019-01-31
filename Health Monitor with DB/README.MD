# Health Monitor with Database

Tasks:

1. Creating a database for a patient. On entering the patient name, ID, age, and sex, app should instantiate a database with table name Name_ID_Age_Sex. The table will have four columns: a) time stamp, b) x values, c) y values, and d) z values. The app will then initiate a service that will connect to an accelerometer and collect data at a sampling frequency of 1 Hz (receiving one data per one second). The data will be stored in the table with the time stamp of a sample and the raw three axes values of the accelerometer. 

2. Uploading the database that created in part 1 to a web server. The link to the folder is - http://impact.asu.edu/CSE535Spring18Folder/
Add another button to the UI named Upload Database. When anyone hit the button, database should be uploaded to the web server.

3. Have another button in the UI named Download Database. When anyone hit that button the app should download data (if any) from the web server and plot the last 10 seconds of data in the graph (Do not copy it from saved database file). Download path should be “Android/Data/CSE535_ASSIGNMENT2_DOWN” on SDCARD.
