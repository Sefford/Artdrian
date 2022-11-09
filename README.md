# Artdrian
A small app for downloading adrianmato.art wallpapers

# Technical details of the app
- Target SDK: 33

```
./gradlew test #Execute Unit testing
./gradlew executeScreenshotTest #Execute screenshot testing. Note that the baseline is a Pixel XL API 33 or they may mismatch
```

# Architecture

The app is built follow the principles of Clean Architecture, and it is also a small test bed for the newest trends of Android 
development.

There are several differentiated layers on the code, separated by packaging. A nice to have would have done to separate those 
into modules; but being a small app, it beats the purpose. I'll enumerate the packages and some information of what contains.

**Datasources**

It holds both local and api classes for retrieving and storing data.

`WallpaperApi` is implemented through Retrofit and connects to the server serving the wallpapers. I used GSon for 
deserialization. I know there are more modern alternatives like Moshi or Kotlin Serialization, but this is the one I am more 
familliar with, and while it has certain risks with Kotlin, I am experienced enough to handle them and the app is very limited 
and controlled, so I will not run any problems with it.

`WallpaperMemoryDataSource` is an easy, memory cache for storing the information retrieved from the API, however, it is 
abstracted behind WallpaperDataSource, as I may choose to use another kind of persistence in the future (Room or File).

All of the Datasources class API are protected suspend functions so I can indicate the compiler when there is an effect that may 
take some time and therefore, should be protected behind a Coroutine.

The `WallpaperRepository` class decorates the same api as WallpaperDataSource, however adding different `CachePolicies` that could
help me tweaking the behavior of the app if I wanted to in case of low connectivity situations, or use it as a failsafe. I 
have done more generic repositories in the past (namely my old Kor repos). However, by default, I simply use the classic 
priorize the local information and fallback to the network.

For the manipulation of the data, I used (Arrow)[https://arrow-kt.io/]. Normally I favor some FP when doing that, as the code, 
while a little strange to unfamilliar devs, it provides quite concise code and easy data structures to work with errors.

For testing this layer, I used some ad-hoc fakes and white box testing together with snapshot testing. I used the 
kotlin-snapshot library for that.

**Use cases**

For the use cases I used a normal approach, all of them encapsulate the business logic, again, using suspend functions to 
convey that there are side effects going on and Arrow to return information.

The tests for this layer have been done through white, large scoped tests which include using MockWebServer to simulate the 
API calls, snapshot testing and ad-hoc fakes to provide a more realistic test conditions.

**UI - ViewModels**

While I have used extensively MVP in the past I have wanted to give a chance to use Android ViewModels. 

AFAIK, the main advantage of the Android ViewModels is that they hide away the complexity of saving the state on configuration 
changes, and while I haven't done an extensive test of them, I have tried to rotate the device, and they seemed to fulfill my 
expectations.

A ViewModel instance holds the appropiate UseCases and exposes the calls using Kotlin Flows. As I do not have the need to go 
further and use a StateFlow or SharedFlow, they behaved quite nice. I am not so experienced using them, so probably there are 
some nuances that I may be missing or I may have implemented myself instead of relying of the more native to Android flows.

Those flows emit ViewStates that the Activity and the Composables subscribe to and modifies the UI state when received. Those 
flows are executed via lifecycleScopes on the IO dispatcher.

The ViewModels are tested almost E2E using a combination of Robolectric to supply the Android parts, custom DI to inject 
certain test doubles and MockWebServer.

**UI - Compose**

I have used Compose to write the whole UI. Nothing much to say on that regard, I set some state hoisting to handle certain 
situations and used a hand from (Accompanist)[https://google.github.io/accompanist/permissions/] for permission handling in Compose.

Each Activity has an Screen under their *View file (eg. WallpaperListViews). This is a Composable that takes a ViewState and 
renders appropiately depending on the state.

The UI has been tested using dummy Compose Testing with ComposeRules and Screenshot testing using (Shot)(https://github.com/pedrovgs/Shot)

**UI - Activities**

I did not complicate myself and made a classic and easy two-activities list/detail. I could have used Fragments or choose some 
Compose Navigation/NavGraph solutions, but due to the small scope of the app and the investment on the latter ones, which I 
have not used in the past, I decided to go for something classical and easy, with Intents.

I did certain testing of the flows using ComposeAndroidRule and Espresso. Those tests are the most complex the application 
have and required extensive refactoring to allow me to create an alternative Dependency Injection on Test time.

In order to do that, I needed to create my own alternative Application instance that loads a Test Graph with doubles, and 

The API was mocked, although the retrieval of the images are not at the moment of this commit. However, this allows me to run 
easy and relatively fast UI tests, and can navigate through the app and perform actions in a manipulable way.

For asserting certain situations like "Saving the wallpaper" or "Setting it up", I required to create some IdlingResources for 
the test, one for listening to file changes, and another, a little more crude to check for signas from a BroadcastReceiver.

# Work to do

I have run out of time to work on the Info button in the Detail Screen and will not work at the time of this tag. I will keep 
working on it.

Also I would like to give a better support of other form factors using Compose.

The part of the Themeing at the moment, works, but it may require reworking to have better dynamic Material 3 theme and Day/Night.

Being a fan of Offline First discipline, I would love to have it work as offline as possible.

Also I would like to create a service, maybe with Workmanager to be able to rotate through the dowloaded wallpapers automatically.
