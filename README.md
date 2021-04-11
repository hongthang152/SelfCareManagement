# Selfcare Management Documentation

## Directory Structure

Our project consists of two parts:
- Android Application (`app` folder)
- Web/API Server (`selfcaremanagement-backend` folder)

The tree directory structure below contains the primary folders that are crucial to this project. Any folders that are not listed are not important.

```
├───app
│   └───src (Main source code directory for Android Application)
│       ├───main
│       │   ├───java
│       │   │   └───com
│       │   │       └───neurondigital
│       │   │           ├───helpers (Containing helper Java utility methods such as generating readable duration, etc)
│       │   │           └───selfcare
│       │   │               ├───graph
│       │   │               │   ├───eventlist (Containing the Java code for the event list activity)
│       │   │               │   └───model (Containing the necessary Java classes represent each item in the event list)
│       │   │               ├───service (AsyncTask classes that handles HTTP requests to the backend server)
│       │   │               └───treatment (Main menu treatment screen)
│       │   │                   ├───compressiontherapy (Containing Java code for the Compression Therapy view)
│       │   │                   ├───exercise (Containing Java code for the Exercise view)
│       │   │                   ├───manuallymphdrainagemassage (Containing Java code for the Manual Lymph Drainage Massage view)
│       │   │                   ├───pneumatic (Containing Java code for the Pneumatic Compression Pump view)
│       │   │                   └───skincare (Containing Java code for the Skin Care view)
│       │   └───res (Containing all static assets such fonts, icons, colors, layout, etc.)
│       │       ├───color
│       │       ├───drawable
│       │       ├───layout
│       │       ├───lib
│       │       ├───menu
│       │       ├───mipmap-hdpi
│       │       ├───mipmap-mdpi
│       │       ├───mipmap-xhdpi
│       │       ├───mipmap-xxhdpi
│       │       ├───mipmap-xxxhdpi
│       │       ├───values
│       │       ├───values-w820dp
│       │       └───xml
│       └───test (Containing Unit Tests for all treatment modules)
│           └───java
│               └───com
│                   └───neurondigital
│                       └───selfcare
│                           └───treatment
│                               ├───compressiontherapy
│                               ├───exercise
│                               ├───manualymphdrainagemassage
│                               ├───pneumatic
│                               └───skincare
└───selfcaremanagement-backend (Containing NodeJS backend server code)
    ├───bin (Starting folder of NodeJS application)
    ├───models (Containing all NodeJS classes that mimics MongoDB schema)
    ├───node_modules (Containing NodeJS dependencies)
    ├───public (Public assets such as CSS templates)
    │   ├───files
    │   ├───javascripts
    │   └───stylesheets
    ├───routes (All NodeJS code that handles API logic)
    └───views (EJS templates that get compiled into HTML)
```

### Implementation of Product

Selfcare Management Android Application was implemented with Android Studio and Java with dedicated support for API >= 29. We made use of the following external libraries to make our app more robust: 

- florent37 SingleDateTimePicker (Date Time Picker when users tap on date/time)
- alamkanak WeekView (Drawing graph in graph view)

Our Backend Server was written with NodeJS version 12.14.0 and MongoDB v4.2.2. We used Mongoose as the Object Data Modeling (ODM) library for connecting our server with database. We also use EJS templates to render dynamic HTML pages with the help of ExpressJS for controlling the HTTP request routing.


### Transition to a production environment

#### Android Application


As this is like a prototype application, our Android Application is exported into an apk file that can be installed on any Android device. If client decides to bring this application live and host it on Google Play, please make sure everyone aware that hosting application on Google Play will require us to pay a registration fee of $25.

#### Database

We use Mongo Atlas (mLab) as our hosting provider for MongoDB. Mongo Atlas allows a free usage of 512MB cluster for every account so you do not have to pay any money upfront.

From Mongo Atlas admin dashboard, you can try to create a database cluster first with all default options. After the database cluster is created, click on `Connect` and `Connect your application` button to retrieve the database URL that is needed to connect to the database. After you got the database URL, substitute the URL on line 13 of `selfcaremanagement-backend\bin\www` file with your database URL. No extra work is needed after that

#### Web/API Server

Node.js allows our application to be deployed on any machines with NodeJS installed. For our application, we rented a DigitalOcean Ubuntu server to serve our application. Instructions on how to deploy a NodeJS Express server can be found [here](https://www.digitalocean.com/community/tutorials/how-to-set-up-a-node-js-application-for-production-on-ubuntu-16-04). Remember to do `npm install` before you try to run the application. 

For our Android application, make sure that the `app\src\main\java\com\neurondigital\selfcare\Configurations.java` have the correct backend server URL. 


### Testing the installation locally

#### Android Application

##### Requirements

- Android Studio installed
- Java installed
- Android emulator installed with API version >= 29

##### Steps

1. Import the project folder into Android Studio by selecting the .gradle file (New -> Import Project)
2. In Android Studio, create and set up an Android emulator with API 29 support.
3. Open `local.properties` file (using Ctrl + Shift + N to search for file name in Android Studio), make sure `sdk.dir` points to your correct Android SDK folder
4. Click on the "Run selected configuration" or "Debug selected configuration" icon and you should be able to run the project on the Android emulator

#### Web/API Server

##### Requirements

- Node.JS installed (v12.14.0)
- MongoDB installed (v4.2.2)

##### Steps

1. In `selfcaremanagement-backend` folder, open cmd terminal and enter `npm install`
2. After `npm install` is completed, run `npm start`
3. Visits [http://localhost:3000](http://localhost:3000) from your browser, if you can see the "Selfcare Management backend server" text, it means that your backend server has started successfully 


