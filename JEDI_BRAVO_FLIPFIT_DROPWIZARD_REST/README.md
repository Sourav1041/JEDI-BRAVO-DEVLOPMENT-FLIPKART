# JEDI-BRAVO FlipFit Dropwizard REST API

A RESTful web service implementation of the FlipFit gym booking application using Dropwizard framework.

## Features Implemented

### Java 17+ Features

1. **Date and Time API**
   - Login timestamp display using `LocalDateTime` and `DateTimeFormatter`
   - Welcome message with formatted date/time on successful login

2. **Lambda Expressions with Stream API**
   - Filter approved/not approved gym owners using Stream API
   - Filter approved/not approved gym centers using Stream API
   - Endpoints: `/admin/owners/approved`, `/admin/owners/pending`, `/admin/centers/approved`, `/admin/centers/pending`

3. **For-Each Loops**
   - All iteration logic uses enhanced for-each loops
   - Functional programming style with Stream API

4. **Comprehensive Javadoc Documentation**
   - All classes and methods documented
   - Generate HTML documentation: `mvn javadoc:javadoc`

## Project Structure

```
src/main/java/com/flipfit/
├── bean/                    # Entity/Bean classes
├── dao/                     # Data Access Objects
│   └── impl/               # DAO implementations
├── business/               # Service layer interfaces
│   └── impl/              # Service implementations
├── rest/                   # REST Controllers
│   ├── AuthController.java
│   ├── GymCustomerController.java
│   ├── GymOwnerController.java
│   └── AdminController.java
├── utils/                  # Utility classes
├── constant/               # Constants
├── exception/              # Custom exceptions
├── validation/             # Validation utilities
├── enums/                  # Enumerations
├── FlipFitApplication.java # Main application class
└── FlipFitConfiguration.java # Configuration class
```

## Build and Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Database Setup
1. Create database and run schema:
```bash
mysql -u root -p < ../JEDI_BRAVO_FLIPFIT_PROJECT_DEVELOPMENT_POS_DAO/flipfit_schema.sql
```

2. Load sample data:
```bash
mysql -u root -p flipfit < ../JEDI_BRAVO_FLIPFIT_PROJECT_DEVELOPMENT_POS_DAO/complete_dummy_data.sql
```

### Build Project
```bash
mvn clean install
```

### Package Application
```bash
mvn clean package
```

### Run Application
```bash
java -jar target/JEDI_BRAVO_FLIPFIT_DROPWIZARD_REST-1.0-SNAPSHOT.jar server config.yml
```

### Access API
- **API Base URL**: http://localhost:8080
- **Admin Console**: http://localhost:8081

## API Endpoints

### Authentication (`/auth`)
- `POST /auth/login` - User login (returns welcome message with timestamp)
- `POST /auth/register/customer` - Register as gym customer
- `POST /auth/register/owner` - Register as gym owner
- `PUT /auth/password/change` - Change password

### Customer Operations (`/customer`)
- `GET /customer/slots/available` - View all available slots
- `GET /customer/slots/city/{city}` - View slots by city
- `GET /customer/centers/city/{city}` - View gym centers by city
- `POST /customer/booking/create` - Book a slot
- `GET /customer/bookings/{customerId}` - View my bookings
- `DELETE /customer/booking/{bookingId}` - Cancel booking
- `POST /customer/waitlist/join` - Join waitlist
- `GET /customer/slots/nearest/{gymId}/{preferredTime}` - Find nearest available slot
- `GET /customer/notifications/{customerId}` - View notifications

### Gym Owner Operations (`/owner`)
- `POST /owner/center/add` - Add new gym center
- `GET /owner/centers/{ownerId}` - View my gym centers
- `POST /owner/slot/add` - Add gym slot
- `GET /owner/slots/{gymId}` - View gym slots
- `GET /owner/profile/{ownerId}` - View owner profile
- `PUT /owner/center/{gymId}` - Update gym center

### Admin Operations (`/admin`)
- `GET /admin/owners/pending` - Get pending gym owners (Stream API filter)
- `GET /admin/owners/approved` - Get approved gym owners (Stream API filter)
- `GET /admin/centers/pending` - Get pending gym centers (Stream API filter)
- `GET /admin/centers/approved` - Get approved gym centers (Stream API filter)
- `PUT /admin/owner/approve/{ownerId}` - Approve gym owner
- `PUT /admin/center/approve/{gymId}` - Approve gym center
- `DELETE /admin/owner/reject/{ownerId}` - Reject gym owner
- `DELETE /admin/center/reject/{gymId}` - Reject gym center
- `GET /admin/users` - View all users
- `GET /admin/owners` - View all gym owners
- `GET /admin/centers` - View all gym centers

## Testing with cURL

### Login Example
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john.customer@example.com","password":"password123"}'
```

Response includes welcome message and login timestamp:
```json
{
  "message": "Login successful",
  "userId": "USR001",
  "username": "John Customer",
  "email": "john.customer@example.com",
  "role": "CUSTOMER",
  "loginTime": "Tuesday, January 28, 2026 at 02:30:45 PM",
  "welcomeMessage": "Welcome John Customer!"
}
```

### Filter Approved Owners (Stream API)
```bash
curl http://localhost:8080/admin/owners/approved
```

### Filter Pending Centers (Stream API)
```bash
curl http://localhost:8080/admin/centers/pending
```

## Generate Documentation
```bash
mvn javadoc:javadoc
```
Generated documentation will be in `target/site/apidocs/`

## Technologies Used
- **Dropwizard 2.0.33** - RESTful web services framework
- **JAX-RS** - Java API for RESTful Web Services
- **Apache Jersey** - JAX-RS implementation
- **Jackson** - JSON processing
- **Apache Jetty** - Embedded web server
- **MySQL 8.0** - Database
- **Java 17** - Programming language
- **Maven** - Build tool

## Assignment Completion

✅ **Assignment 1**: Date and Time API - Login displays current timestamp with formatted date/time  
✅ **Assignment 2**: Lambda & Stream API - Admin filters for approved/pending owners and centers  
✅ **Assignment 3**: For-Each Loops - All iterations use enhanced for-each loops  
✅ **Assignment 4**: Javadoc Documentation - Complete documentation with `mvn javadoc:javadoc`

## Authors
**JEDI-BRAVO Team**

## Version
1.0-SNAPSHOT

## Date
January 28, 2026
