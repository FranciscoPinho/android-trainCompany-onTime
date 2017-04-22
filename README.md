# android-trainCompany-onTime
A collection of android applications to mimick ticket purchase/validation. This project was developed completely on my own as a university assignment-


![download](https://cloud.githubusercontent.com/assets/9083330/25300786/8c6b1754-270f-11e7-96cf-94e44fa2f4d7.png)

## Included features 
 
### OnTime Travelers 
 
* Persistent authentication - Proceeding the first manual authentication action by the user, his login email and a unique device identifier will be recorded on the application's shared preferences, this will allow the application to automatically send a login request upon startup. 
* Session data for multiple devices – The user can be logged in at multiple devices at once. 
* Account creation - A user can create na account and add a credit card to his name. 
* Check schedules - A user may check the train schedules through the application 
* Purchase Tickets – A user may purchase tickets by choosing the beginning and end stations of his journey, relevant data such as schedules, prices and trip distance is provided in response to user interactions with the Spinners that allow him to choose the stations. Since credit card info is added on startup, the purchase is "automatic" and simulated through button clicks. 
* Ticket Viewer - After a successful purchase request, a QR code image for the corresponding ticket will be generated through a bound service(QRCodeGenerator class) and saved on the application's cache directory. This fragment will present a Spinner with the list of all the images corresponding to each bought ticket by the user. 
* Gift Tickets – By taking a screenshot of the QR code of one's bought ticket, one can easily gift a train ticket. 
 
### OnTime Inspector 
 
* Download Tickets by Train Selection . 
* Syncing functionality with the Server, with this the Server will always be updated on which tickets have already been used. 
* Ticket Listing – List all the tickets in the local database. 
* Local database cleanup – After sending updates to the server and after the trip is over, the inspector may want to clear the local datase. 
* Ticket Validation – Read ticket signature through the QR Code and validate the ticket by generating a local hash based on downloaded ticket data and by comparing the hashes

## Interface(Decrease zoom to see more clearly)

### OnTime Traveler
![18015843_1383532288352161_1470635047_o](https://cloud.githubusercontent.com/assets/9083330/25300843/7948d020-2710-11e7-9462-80284a19ee3c.png)
![18049596_1383532128352177_2124530038_o](https://cloud.githubusercontent.com/assets/9083330/25300844/794988f8-2710-11e7-9acf-5599ccad7971.png)
![18083918_1383532198352170_445309536_o](https://cloud.githubusercontent.com/assets/9083330/25300838/7922d424-2710-11e7-9519-988f42043f4d.png)
![18083923_1383532071685516_1006175767_o](https://cloud.githubusercontent.com/assets/9083330/25300839/79271688-2710-11e7-8447-7dbb49865132.png)
![18111133_1383532038352186_1745097274_o](https://cloud.githubusercontent.com/assets/9083330/25300841/792d3d4c-2710-11e7-949f-fb6dce7fd525.png)

### OnTime Inspector
![18111134_1383564505015606_1406525160_o](https://cloud.githubusercontent.com/assets/9083330/25300842/7932903a-2710-11e7-9acd-18cd0c93c6b9.png)
![18109971_1383564501682273_1956434856_o](https://cloud.githubusercontent.com/assets/9083330/25300840/792bde52-2710-11e7-8901-725fd65c0d8e.png)

