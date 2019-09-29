

Softwares
=========
	Grizzly, Jersey, HK2, Jackson, Jose4j
	Gradle script will take care of downloading required softwares
	
To Run
======
	This application requires Google and Zomato account
	*Google - Identity Provider
	*Zomato - Business Partner
	
	1. Create an oauth application in https://console.cloud.google.com/apis/credentials
	2. Add http://<YOUR_HOST_NAME>/loginRedirect to redirect URL
	3. Add your domain to authorized domain in oauth consent page.
		Eg: your hostname is xyx.company.com, then add company.com in authorized domain list
	4. Generate API key from Zomato developer website
	5. Get the Credentials and API key from Google, add for openId.apiKey and openId.credentials in config file
	6. Add the Zomato api key to zomatoConfig.apiKey
	
	After adding all required details.
	Run ./gradlew runner ==> It will build and start the server
	
	
How to Use
==========
	Application runs in http://<YOUR_HOST_NAME>
	
	Note: I didn't build full-fledged UI application, since I don't have much experience with UI development
	
	1. Access http://<YOUR_HOST_NAME>/login, it will open a page and you will find button to 'Login with Google'
	2. Click the button and provide your google credentials
	3. In the next page, you will be presented with your profile details and a 1 hour VALID access token
	
	After this all activities has to be performed using REST API.
	All the REST endpoints are protected and it requires valid access token[you got it from UI]
	
	1. Get the cities list
	curl -X GET http://<YOUR_HOST_NAME>/cities  -H 'Authorization: Bearer <ACCESS_TOKEN>'
	
	2. Get/Search the Restaurants
	curl -X GET http://<YOUR_HOST_NAME>/resturants/search?type=city&value=Bangalore&page=8&sort=cost&order=desc  -H 'Authorization: Bearer <ACCESS_TOKEN>'
	
	3. Get your Favourite Restaurants
	curl -X GET http://<YOUR_HOST_NAME>/favourite/  -H 'Authorization: Bearer <ACCESS_TOKEN>'
	
	4. Mark restaurant as favourite
	curl -X POST http://<YOUR_HOST_NAME>/favourite/<RESTAURANT_ID>  -H 'Authorization: Bearer <ACCESS_TOKEN>'
		*Will ignore duplicate resturant ID
		*Persisted in file, hence you can retrieve restaurants even after server restart
	
	5. Remove restaurant as from favourite list
	curl -X DELETE http://<YOUR_HOST_NAME>/favourite/<RESTAURANT_ID>  -H 'Authorization: Bearer <ACCESS_TOKEN>'
		*Error thrown no restaurants available in your list
		*Error thrown if requested restaurant not available
	
	6. Get your bookings
	curl -X GET http://<YOUR_HOST_NAME>/booking/  -H 'Authorization: Bearer <ACCESS_TOKEN>'
	
	7. Make bookings
	curl -X POST http://<YOUR_HOST_NAME>/booking  -H 'Authorization: Bearer <ACCESS_TOKEN>' -d '{
		"restaurantId": "24",
		"time": "2012-04-23T21:14:43.511Z",
		"status": "ACTIVE",
		"guests": 2
	}'
		* Booking not allowed, if guests are more than 20
		* Booking not allowed, if time between last booking and current booking is less than 1 hour
		* Known issue: response you will duplicate attributes(not entries)
	
	
	8. Cancel bookings
	curl -X DELETE http://<YOUR_HOST_NAME>/booking/<RESTAURANT_ID>  -H 'Authorization: Bearer <ACCESS_TOKEN>'
		*Error thrown no bookings available in your list
		*Error thrown if requested booking not available
	
	ERROR Messages
	--------------
	1. All the above endpoints throw errors when
		* Token expired
		* Token not paassed
		* Token signature verification
		* Endpoint not available
		* HTTP method mismatch
	