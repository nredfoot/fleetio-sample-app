# fleetio-sample-app

Sample app that pulls a list of fuel entries down from Fleetio's API and displays them in a tabbed interface. Clicking any list view item or map marker displays a popover details screen that shows date, vehicle name, cost, cost per mile, gallons, fuel type, price per gallon, vendor, and reference number. Project uses Retrofit, Gson, RxJava & Android's native ViewModel component. No heavy weight persistence, just caching network responses via Retrofit.
