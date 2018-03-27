# FoodMap
## About
This app let's you search for nearby restaurants. You can view restaurant details and save your favorites.

## Project requirements
* Use Activities
* Responsive layout
* Use RecyclerView and RecyclerAdapter
* Implement Localization
* Good Architecture (separation of concerns)
* Use Local Storage
* Use an external RESTful API
* Optional: Google Maps API.

### Activities
The following activities are used:
* MapsActivity (Main screen of the app, plots nearby restaurant on a map)
* SearchActivity (Search for nearby restaurants)
* SearchDetailActivity (Shows detailed information about a selected restaurant)
* FavoritesActivity (Favorite restaurants of the user)

### RESTful API
The app makes use of the Yelp API. The purpose of Yelp is to connect people with great local businesses. You can find more information on [their website](https://www.yelp.com/).

### Localization
The current languages are supported: English, Dutch.
Localization is implementend by supporting different language xml string files. On top of that, the system's language is added to each JSON request so that these results are also localized. This depends on the information provided by the API.

### Architecture
Classes are devided into the following packages: 
* activities - Contains all the activities. Classes are all named with Activity at the end. That way, you can immediately know what it is when reading Java code that doesn't have its full package name.
* adapters - Contains all the adapters.
* data - Contains all classes related to data management such as ContentProvider and SQLiteHelper.
* helpers - Contains helper classes. A helper class is a place to put code that is used in more than one place. I have a UniversalImageLoader helper class for instance. Most of the methods are static.
* interfaces - Contains all interfaces.
* models - Contains all local models. When syncing from an HTTP API I parse the JSON into these Java objects. I also pull Cursor rows into these models as well.
* sync - Contains all classes related to calling an HTTP API.

### Local Storage
The app uses SQLite to enable Local Storage functionality. Favorite restaurants are stored within an local database on the user's device. 

## Screenshots
![Screenshot 1](/screenshots/Screenshot_1_nexus5x-portrait.png  "Screenshot 1")
![Screenshot 2](/screenshots/Screenshot_2_nexus5x-portrait.png  "Screenshot 2")
![Screenshot 3](/screenshots/Screenshot_3_nexus5x-portrait.png  "Screenshot 3")
![Screenshot 4](/screenshots/Screenshot_4_nexus5x-portrait.png  "Screenshot 4")

## Build configuration
Note: to build the app you need the a 'secrets.xml' containing the API-keys (inside the res/values/ folder).