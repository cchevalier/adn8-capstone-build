# PhotoSplash
**adn8-capstone-build**

AD nanodegree - Project 8: Capstone, Stage 2 - Build

by CChevalier, Feb. 2016



### General Useful References  
- [CodePath Android Cliffnotes](https://guides.codepath.com/android)  
- [Future Studio Blog](https://futurestud.io/blog) (Interesting series on Retrofit and Picasso)  


### References specifics to Common Project Requirements

#### 3rd party libraries used
- [Retrofit 2 by Square](http://square.github.io/retrofit/)  
- [Picasso by Square](http://square.github.io/picasso/)
- [PhotoView by Chris Banes](https://github.com/chrisbanes/PhotoView)

#### Other part of codes used
- [DynamicHeightImageView](https://github.com/etsy/AndroidStaggeredGrid/blob/master/library/src/main/java/com/etsy/android/grid/util/DynamicHeightImageView.java)  
- See additional references in the code  
 
#### Widget
- [App Widgets](https://developer.android.com/guide/topics/appwidgets/index.html)  

### Google Play Services

#### AdMob
- [Adding AdMob into an Existing App](https://firebase.google.com/docs/admob/android/existing-app)  

#### Analytics
- [Add Analytics to Your Android App](https://developers.google.com/analytics/devguides/collection/android/v4/)
- [GitHub: googlesamples/google-services/android/analytics/](https://github.com/googlesamples/google-services/tree/master/android/analytics)

### Building
- [This on Udacity Discussion Forum - AND P7 & P8](https://discussions.udacity.com/t/confused-by-capstone-rubric-build-requirements/42427)  
- [Sign Your App - Release Mode](https://developer.android.com/studio/publish/app-signing.html#release-mode)

### Data Persistence
- [Local Databases with SQLiteOpenHelper](https://guides.codepath.com/android/Local-Databases-with-SQLiteOpenHelper)  

#### Content Provider
- [Creating Content Providers](https://guides.codepath.com/android/Creating-Content-Providers)  

##### SyncAdapter / IntentService / AsyncTask   
- [AsyncTaskLoader](https://developer.android.com/reference/android/content/AsyncTaskLoader.html): Abstract loader that provides an AsyncTask to do the work.  

#### Loaders / AsyncTaskLoader
- [Loaders](https://developer.android.com/guide/components/loaders.html)  

- Android Design Patterns
    - [Part 1 - Life before Loaders](http://www.androiddesignpatterns.com/2012/07/loaders-and-loadermanager-background.html)
    - [Part 2 - Understanding the LoaderManager](http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html)  
    - [Part 3 - Implementing Loaders](http://www.androiddesignpatterns.com/2012/08/implementing-loaders.html)  
    - [GitHub: AppListLoader](https://github.com/alexjlockwood/AppListLoader)  

But an important issue remains on AOSP side:  
- [Issue 183783:	Support Fragments do not retain loaders on rotation](https://code.google.com/p/android/issues/detail?id=183783) 

