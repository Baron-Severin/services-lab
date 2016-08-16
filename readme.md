---
title: Services
type: lab
duration: "2:30"
creator: Brad Zimmerman (SEA)
---

# ![](https://ga-dash.s3.amazonaws.com/production/assets/logo-9f88ae6c9c3871690e33280fcf557f33.png) Services

## Exercise

#### Introduction

* Attached with this lab you will see the solution code for the previous instructor's lab. It describes a music player which boots up on load and allows the user to pause or play the music whenever they desire. While that's all well and good, we need to use this service in a slightly different way. Taking a page from the lesson yesterday, you will be building an app with a service that connects to the google play services. Use the hints you can get about the Android Service functionality from the solution code as well as the WWWs (World Wide Webs) to make an app with the following functions:

* Allows the user to see the weather conditions and temperature when a button is pushed
* Allows the user to see their current level of activity (Walking, Running, Biking, etc) as well as the likelyness of it being correct when a button is pushed
* Allows the user to see the top 2 nearest places (restaurants, museums, etc) when a button is pushed

#### Requirements

> ***Note:*** _This should be done in pairs._

* Your app must implement the 3 google play services described above
* Your app must implement those google play services inside an Android Service
* Your app must update a textview(s) with the new information when a button is clicked

**Bonus:**

* Add additional calls to google play services
* Put you service in it's own seperate process
* Have your service update your Activity no matter if a button is selected or not

#### Deliverable

An Android Studio project, with no particular format. It just has to meet the requirements above. Peak at the [solution code](solution-code) if you're stuck!

#### Hint

* Remember each button click returns a result that can be stored in two textviews.
* You might need to connect the Service to the Activity in some way to get this project working.

#### Resources

- [Services | Android Developers](http://developer.android.com/reference/android/app/Service.html)
- Make a thing that does something
- Have it do something else
- This is what we expect of you
