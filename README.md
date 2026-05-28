# LinkSnip

LinkSnip is a full-stack URL shortener with QR code generation and click analytics. Users can register, log in, shorten URLs, set custom aliases, configure link expiry, and track how many times each link has been clicked.


## Features

- User Registration and Login
- JWT-based Authentication and Authorization
- Shorten long URLs into compact, shareable links
- Custom aliases for short links
- Link expiry (TTL support)
- QR code generation for any short link
- Click analytics per shortened URL
- Delete your own links
- Protected routes for authenticated users
- RESTful APIs using Spring Boot
- Responsive and clean React UI


## Tech Stack

**Frontend:**
- React.js
- Axios
- React Router

**Backend:**
- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Maven

**Database:**
- PostgreSQL


## Screenshots

### Register Page
![Register](https://github.com/user-attachments/assets/c7a541dd-cf95-4a3d-bbd2-b37edc0df728)

### Login Page
![Login](https://github.com/user-attachments/assets/a9635766-94c2-47b8-a41d-4e0d177093f5)

### Dashboard
![Dashboard](https://github.com/user-attachments/assets/62eb4c2f-9b3e-47e4-a37b-449b5d103386)


## How to Run the Project

### 1. Clone the repository

```bash
git clone https://github.com/Karthikeya-chodisetti/LinkSnip.git
cd LinkSnip
```

### 2. Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend runs at: `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at: `http://localhost:5173`

---

## Environment Variables

Create `application.properties` in `backend/src/main/resources/`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/linksnip
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080

app.jwt-secret=YOUR_JWT_SECRET_KEY
app.jwt-expiration-ms=86400000

app.base-url=http://localhost:8080
```


## API Endpoints

### Auth

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT token |

### Links

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/api/links` | Yes | Shorten a URL (with optional alias and TTL) |
| GET | `/api/links/{code}` | Yes | Redirect to the original URL |
| GET | `/api/links/my` | Yes | Get all links belonging to the logged-in user |
| DELETE | `/api/links/{code}` | Yes | Delete a link you own |

### Analytics

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| GET | `/api/links/{code}/analytics` | Yes | Get click analytics for a short link |

### QR Code

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| GET | `/api/links/qr/{code}` | Yes | Get a QR code image (PNG) for a short link |


### Example: Shorten a URL

**Request**
```json
POST /api/links
Content-Type: application/json

{
  "url": "https://www.example.com/some/very/long/path",
  "customAlias": "mylink",
  "ttlValue": 7,
  "ttlUnit": "DAYS"
}
```

**Response**
```json
{
  "shortUrl": "http://localhost:8080/api/links/mylink"
}
```

---

## Future Improvements

- Google / GitHub OAuth login
- Link click graphs and geo-analytics
- Categories and tags for links
- Dark mode UI
- Mobile app version



## Author

Karthikeya Chodisetti  
GitHub: https://github.com/Karthikeya-chodisetti

