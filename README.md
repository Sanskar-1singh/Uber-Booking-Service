🚖 Uber Booking Service

A scalable microservice responsible for handling ride bookings, driver assignment, real-time ride requests, and inter-service communication in an Uber-like distributed system.

This service interacts with:

Location Service (fetch nearby drivers)

Socket Server (send ride requests to drivers)

Entity Service (shared DB models like Passenger, Driver, Booking)

Built using Spring Boot, Retrofit (async HTTP), Eureka Service Discovery, JPA, and Lombok.

📁 Project Structure
src/main/java/com/example/uberbookingservice
│
├── apis
│   ├── LocationServiceApis     → Retrofit API for Location Service
│   └── UberSocketApi           → Retrofit API for WebSocket Server
│
├── Controller
│   ├── BookingController       → Request endpoints (future)
│   └── RetrofitConfig          → Retrofit bean configuration
│
├── Dtos                       → Request/Response DTOs
│   ├── CreateBookingDto
│   ├── DriverLocationDto
│   ├── NearByDriverRequestDto
│   ├── ResponseBookingDto
│   ├── RideRequestDto
│   ├── UpdateBookingRequestDto
│   └── UpdateBookingResponseDto
│
├── Repository
│   ├── BookingRepository
│   ├── DriverRepository
│   └── PassengerRepository
│
├── Service
│   ├── BookingServiceImpl      → Core business logic
│   └── IBookingService
│
└── UberBookingServiceApplication  → Spring Boot main class

✨ Features
🟦 1. Create a Booking

Saves booking details

Fetches nearby drivers asynchronously using Location Service

Calls WebSocket Server to send ride request to drivers

🟩 2. Update Booking

Update booking status

Assign driver

Return updated booking details

🟧 3. Async APIs using Retrofit

Both:

LocationServiceApis

UberSocketApi

are built using Retrofit’s async enqueue()

🟨 4. Eureka Service Discovery

Services are auto-discovered using:

eurekaClient.getNextServerFromEureka(serviceName, false)


So no hardcoded URLs are used.

🔗 Microservice Communication Flow
📍 Step 1: User books a ride

Booking Service receives:

passengerId
startLocation { lat, lng }
endLocation   { lat, lng }

📍 Step 2: Fetch nearby drivers

BookingService → LocationService

POST /api/location/nearby/drivers


Returns list of nearby drivers:

[
  { "driverId": "12", "latitude": 28.65, "longitude": 77.23 }
]

📍 Step 3: Raise ride request to Socket Server

BookingService → WebSocket Server

POST /api/socket/newride


This notifies drivers via WebSocket.

🧠 Core Logic (Summary)
🔹 createBooking()

Save booking

Query nearby drivers using Retofit async

Send ride request via Socket Server

🔹 updateBooking()

Update status

Assign driver

Return updated booking

🔹 Async Retrofit Call Pattern
call.enqueue(new Callback<DriverLocationDto[]>() {
   @Override
   public void onResponse(...) { ... }

   @Override
   public void onFailure(...) { ... }
});

🛠️ Technologies Used
Tech	Purpose
Spring Boot	Base framework
Java 17	Language
Eureka Client	Service discovery
Retrofit2	Async inter-service API calls
OkHttp	HTTP client
JPA + Hibernate	DB interaction
Lombok	Boilerplate removal
MySQL	Database
WebSockets (external)	Real-time driver notification
⚙️ Configuration (Retrofit)
return new Retrofit.Builder()
    .baseUrl(getServerName("UBER-LOCATION-SERVICES"))
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()
    .create(LocationServiceApis.class);

💾 Database Entities (from Entity Service)

Passenger

Driver

Bookings

ExactLocations

BookingStatus

These models come from uber-entity-services module.

🚀 Future Improvements

BookingController endpoints

Retry strategies for failed async calls

Chat/Driver acceptance flow

Driver availability logic

Dedicated Event Queue (Kafka)

Distributed transactions

📦 Run the Service
Prerequisites

Java 17

Maven / Gradle

MySQL running

Eureka Discovery Service running

Entity Service, Location Service, and Socket Server running

Start the service
./gradlew bootRun
