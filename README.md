# FIT3077 - Smile - Assignment 2A

## Requirements
- [Android Studio](https://developer.android.com/studio/install)
- Android SDK 
- Internet features requires [Monash VPN](https://www.monash.edu/esolutions/network/vpn)

## Building Smile
1. Open project in Android Studio
2. Run project in an Android emulator

## Design principles and patterns
The **abstract pattern factory pattern**, as well as the **adapter pattern**, adds a layer of abstract to avoid depending on a single service. Given that the REST API of the app will change its form, we will rely on the factory to create the correct implementation the app requires. The benefit of this approach is that the rest of the app does not rely on a concrete service, which leads to improved extensibility when services are added in the future.

The **model-view-controller (MVC) architectural pattern** is used throughout the app to aid in separation of concerns. The benefit of our approach is that the business logic of the app (such as persistence) can be changed independently of the UI, which reduces bugs caused by tightly coupled code (i.e. code that combines both the Android API and business logic in a single class). A related benefit is that the view layer can be easily changed, promoting extensibility of our solution.

The **observer pattern** ensures that the UI is updated when the underlying data changes. We have used the observer pattern as the UI needs to react to multiple events, such as loading the asynchronously from the network and refreshing the data every hour).
