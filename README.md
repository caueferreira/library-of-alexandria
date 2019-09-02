# [ 🚧 Work in progress 🚧 ] Library of Alexandria
> A showcase application using [scryfall](https://scryfall.com/docs/api "scryfall") API, because I wish to study a few technologies and a new approach, also did not wanted to use a movie API again :P 

This is a working in [progress application](https://github.com/caueferreira/library-of-alexandria/projects), its missing several pieces of the application and many of them are yet to be refactored. None the less, its already following the concept of its final architecture. 
Issues are always welcome, however since the first part of the app is not finished, no PR is currently accepted.

| Module | Build Status |
| ------------ | ------------ |
| App | [![Build Status](https://app.bitrise.io/app/d1418167e662a0df/status.svg?token=6D40OFYo1iwaxP27t8lDrg)](https://app.bitrise.io/app/d1418167e662a0df) |
| Cache | [![Build Status](https://app.bitrise.io/app/83e13043814ffc5d/status.svg?token=v1dlW3zsj2i7JrQYfv4iXA)](https://app.bitrise.io/app/83e13043814ffc5d) |
| Network | [![Build Status](https://app.bitrise.io/app/eb0a4fc95c71256f/status.svg?token=yJBd3KuSrRQSKDwO5XZnvg)](https://app.bitrise.io/app/eb0a4fc95c71256f) |
| Core | [![Build Status](https://app.bitrise.io/app/4f681c4a36c03169/status.svg?token=05FEsBtyvwD7pql5DacWeA)](https://app.bitrise.io/app/4f681c4a36c03169) | 
| Cards | [![Build Status](https://app.bitrise.io/app/14208e84a2b63b15/status.svg?token=F-9_R71nXW76iCZem5S3IA)](https://app.bitrise.io/app/14208e84a2b63b15) |
| About | [![Build Status](https://app.bitrise.io/app/74a4c36961caf0c1/status.svg?token=fgj8VqorwEQjvRYgTR69BQ)](https://app.bitrise.io/app/74a4c36961caf0c1) |

## About
This application is a showcase of some of the most interesting technologies I have gathered during this last couple of months. Here we are using the concepts of unidirectional data flow, dynamic feature modules, coroutine, flow and many more. Check the stack section for more information.

This app was developed with off-line first concept in mind, meaning that all of its data is cached and can be retrieved when the device has no internet. It is using the concepts of unidirectional data flow, so our view will receive states that represents what should be presente, reducing some unecessary logic prior added to Activities/Fragments.
The architecture studied in this app allows a huge test cover, which is showcased here as well; you can check more about its architecture in the architecture section.

It has some big inspirations in [Plaid](https://github.com/android/plaid "Plaid"), a open source project you should star.

### Goals
 * Modularize the app using [dynamic feature modules](https://developer.android.com/guide/app-bundle/ "dynamic feature modules").
 * Showcase many of Android technologies and best practices.
 * Showcase a resilient and simple style practice for the application.
 * Create a big test coverage, showcasing some of the frameworks available.
 * Implement a powerful architecture helping other developers to learn it.


## Architecture

Library of Alexandria take a unidirectional data flow approach, while developing this app; meaning that it follows the approach of the MVI pattern. The application follows the clean architecture, all features are divided between 3 layers: **data**, **domain** and **view**. 
                      
* The **data** layer is responsible for getting all data from the network or the cache. The **data** layer is responsible for mapping it to the **domain** object before providing it.
* The **domain** possess all the business rules, as for now its responsible for fetching the cached data first and after that it retrieves the network data. As for now its moslty responsible for providing the cached data before the network data.   
* The **view** layer is responsible for all the data and function the users can see or interact.

<p align="center">
  <img src="https://github.com/caueferreira/library-of-alexandria/blob/master/.github/application-architecture.png" width="680">
</p?>

### Modules

The application is divided in several modules, those are:
- **app** ~ main module, all feature modules depend on it
- **cache** ~ responsible for caching data, it has an interface which any feature can implement 
- **network** ~ responsible for handling all network requests, including network errors
- **core** ~ has all bases, interfaces, extensions that other modules might use
- **cards** ~ the main feature, where you can find all expansions and cards
- **about** ~ a small module where you can check some information about the app

Library of Alexandria, showcase how to use dynamic features, which means that the feature modules - both **about** and **cards** - implements the **app** module, normally we're not used to it since we're used to have our **app** module implementing our feature modules, the main difference is how we communicate between classes; the _Activities.kt_ holds a reflection that allows you to navigate between activities.

<p align="center">
  <img src="https://github.com/caueferreira/library-of-alexandria/blob/master/.github/dynamic-feature-modules.png" width="360">
</p>

### Stack
- Kotlin
- Koin
- Coroutine
- Retrofit
- Glide
- ACC
- AndroidX

## Running

**Runing tests**

To run the unit tests simple execute the following command:

`./gradlew clean test`

**Installing application**

To install the app you can either install it using the Android Studio or run:

`./gradlew installDebug`
