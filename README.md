# Location Reminder

**Location Reminder** is a mobile application which acts as a to-do list which reminds you of what you set up to do when reaching a specific location. The app requires the user to create an account and login to set and access reminders.

I developed this application as part of the Udacity's [Android Kotlin Developer Nanodegree](https://www.udacity.com/course/android-kotlin-developer-nanodegree--nd940) Program.

I built the app using the **MVVM (Model-View-ViewModel) architecture pattern** - to allow fast reaction to design changes; the **Repository Pattern** - to facilitate separation of concerns; and **Dependency Injection** - to provide ease of testing.

Location Reminder uses [FirebaseUI Authentication](https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md) (for personalized use) and **Google Maps** with **Geofences** (for each reminder, a geofencing request is created in the background which triggers a notification when you enter the geofencing area).

### The app leverages the following components from the Jetpack library:

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/)
* [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/)
* [Room](https://developer.android.com/training/data-storage/room)
* [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview) - to display the asteroids in a list.

### Testing

For testing, I used [Espresso](https://developer.android.com/training/testing/espresso), [Mockito](https://github.com/mockito/mockito), and [Koin](https://github.com/InsertKoinIO/koin) to build a large test suite consisting of local and instrumented tests.

The test suite includes:
- Automation Testing using ViewMatchers and ViewInteractions to simulate user interactions with the app
- Testing for Snackbar and Toast messages
- End-to-End testing for Fragments Navigation
- Testing for ViewModels, Coroutines, and LiveData
- Testing for the DAO (Data Access Object) and Repository classes
- Testing for the error messages

### Break Down Tests

```
1.androidTest
        //TODO: explain the tests here.
2. test
        //TODO: explain the tests here.
```

## Setting up the Repository

```
1. To enable Firebase Authentication:
        a. Go to the authentication tab at the Firebase console and enable Email/Password and Google Sign-in methods.
        b. download `google-services.json` and add it to the app.
2. To enable Google Maps:
    a. Go to APIs & Services at the Google console.
    b. Select your project and go to APIs & Credentials.
    c. Create a new api key and restrict it for android apps.
    d. Add your package name and SHA-1 signing-certificate fingerprint.
    c. Enable Maps SDK for Android from API restrictions and Save.
    d. Copy the api key to the `google_maps_api.xml`
3. Run the app on your mobile phone or emulator with Google Play Services in it.
```

## Application flow
Take a look at the things you can do with this app:

<img src="screenshots/screen_1" width="400px"/>
<img src="screenshots/screen_2" width="400px"/>
<img src="screenshots/screen_3" width="400px"/>
<img src="screenshots/screen_4" width="400px"/>
<img src="screenshots/screen_5" width="400px"/>
<img src="screenshots/screen_6" width="400px"/>
<img src="screenshots/screen_7" width="400px"/>
<img src="screenshots/screen_8" width="400px"/>
<img src="screenshots/screen_9" width="400px"/>


## Get in touch
Reach out at [diana.sica29@gmail.com](mailto:diana.sica29@gmail.com)