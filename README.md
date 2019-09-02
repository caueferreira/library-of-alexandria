# Library of Alexandria
> A showcase application using [scryfall](https://scryfall.com/docs/api "scryfall") API, because I wish to study a few technologies and a new approach, also did not wanted to use a movie API again :P 

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

## Architecture

### Modules

The application is divided in several modules, those are:
- **app** ~ main module, all feature modules depend on it
- **cache** ~ responsible for caching data, it has an interface which any feature can implement 
- **network** ~ responsible for handling all network requests, including network errors
- **core** ~ has all bases, interfaces, extensions that other modules might use
- **cards** ~ the main feature, where you can find all expansions and cards
- **about** ~ a small module where you can check some information about the app 

Library of Alexandria, showcase how to use dynamic features, which means that the feature modules - both **about** and **cards** - implements the **app** module, normally we're not used to it since we're used to have our **app** module implementing our feature modules, the main difference is how we communicate between classes; the _Activities.kt_ holds a reflection that allows you to navigate between activities.

### Stack
- Kotlin
- Koin
- Coroutine
- Retrofit
- Glide
- ACC
- AndroidX
