# Marclay
<img src="https://s3.amazonaws.com/marclay/static/hero.png" width="400" align="right" hspace="20">

Marclay is an Android TV app that turns your TV into a backdrop of stunning video landscapes from around the world adapting to your time of day. When paired to your Android phone Marclay synchronizes your notifications, so you never miss important information. 

## Configuration

Create a `gradle.properties` file in the root of the repository to provide configuration parameters. Create free accounts for both [Firebase](https://www.firebase.com/) and [OpenWeather](http://openweathermap.org/api) to obtain a Firebase App name and an OpenWeather API key. 

```
open_weather_api_key="API_KEY"
firebase_app_name="APP_NAME"
```

Import this example schema into your Firebase app to load sample videos. They are downloaded to your Android TV on the first launch. 

```
{
  "videos" : [ {
    "hour" : 13,
    "location" : "Santa Cruz, California",
    "url" : "https://s3.amazonaws.com/marclay/1_santa_cruz.mov"
  }, {
    "hour" : 16,
    "location" : "Quepos, Costa Rica",
    "url" : "https://s3.amazonaws.com/marclay/0_costa_rica.mov"
  } ]
}
```

## Install 

Run `./gradlew assembleDebug` to generate APKs for mobile and tv. The project consists of three modules. 

- core
	- Common code shared between both apps 
- mobile
	- Runs on the device that will forward notifications to the TV
- tv
	- Produces the APK that will run on your Android TV
