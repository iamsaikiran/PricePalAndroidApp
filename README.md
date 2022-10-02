# PricePalAndroidApp
## Application Name: PricePal

### Team Members
[Sai Kiran Reddy Gaddam](https://github.com/iamsaikiran)<br>
[Tejaswini Challa](https://github.com/tejaswinichalla1812)<br>
[Rohan Reddy Kondaveeti](https://github.com/rohan2453)<br>
[Sai Nikhil Vaidyapu](https://github.com/vaidyapusainikhil)


## Problem Statement
People nowadays only spend their money on necessities. As a result, they can only buy anything that is reasonably priced. However, they will need to devote a significant amount of time to visiting all of the stores in order to get product information and conduct a comparison in order to determine which item in which store is more valuable to them. Even if they acquire all of the item details, they will still need to spend a significant amount of time analyzing the items in the stores. Spending their time attempting to figure out all of the product details in multiple stores is a major issue.


## Introduction
The main goal of our application is to build an app for the users which should be very interactive and it’s ease of use, to help a consumer in finding Stores that provide a specific Product in his area. It should also provide a Compare Products feature to compare the prices of a product among different stores.


## Activities
#### Login Page
Every consumer should register here to create an account before he/she begin using the application.

#### Stores
It is used to find what are all the stores available nearby.

#### Products 
To find a specific product, use the search product in Products section.

#### Scan Barcode 
Used to scan the barcode of a particular product.

#### Price Comparison 
To compare the prices of a product among different stores.


## Application Usage Guidelines
Every consumer has to create an account first before using the application features. Then Login with credentials.

#### Stores:
- Stores will be displayed after the consumer has successfully logged in.
- It displays all of the local stores. Following the selection of a specific store, the list of popular products from that store is displayed.
- It also features a product search option where you may look for something specific.

#### Products:
- If you go to the products area, you'll see a list of items along with their prices.
- It also offers a search product feature that allows you to look for a certain item.
- When you search for or click on a certain item, it displays product details such as Quantity, Price, Store, and Store Id.

#### Scan Barcode:
- A section for scanning a specific barcode can be found in the Scan Barcode section.
- By selecting it, we can scan a product's bar code and obtain product information.
- Additionally there's also a section where you may manually type in the barcode.

#### Price Comparison:
- If you proceed to the last section of the page and click on the last Price Comparison section, it will show you the product as well as the stores where you can buy   it.
- If you select the Price Analyzer option, you will be able to compare the price of a product on a specified date.

#### Admin:
- The PricePal Admin page is used to add a product's price from a particular store on a specific date.


## Test Credentials

#### User Login
User Name : pricepal.s22@gmail.com<br>
Password : Pricepal@123

#### Admin login
User Name : pricepalapp@gmail.com<br>
Password : Pricepal@123


## Supported Devices
minSdk 19<br>
targetSdk 32<br>
versionName "1.001"


## Advantages
- User-Friendly.
- Cost and time-efficient.
- Compare the price of the product
- By using Barcode, the app makes it easy to identify the product details.


## Persistent Data
We used Firebase Database to store and retrieve the data.

## API/Libraries used

### Libraries
- squareup picasso
- gms google services
- google firebase


## Technical Stack
- Android : xml, java 
- Server : Firebase cloud messaging      
- Backend : Firebase cloud messaging backend provided by google


## Problems Faced
- It only enables you to compare prices for available products in the stores.
- Login or account related issues.
- Sometimes, barcode scanner not working properly.
- It's also possible that this application will cause problems with loading.


## Future Improvements
- Implement pop up notifications for the customer whenever price of the product is decreased or increased.
- Provide online orders, payments and store pick-up.
- Offer item brand or size-specific comparisons




