
# Mortgage API

A Spring Boot application for managing mortgage rates and mortgage feasibility checks.


I understand that the mortgage calculation is more complex, but since it is a 
small project I assumed the calculation per year and not compounding interest or 
fixed rates per 10 years like here in the netherlands.

## Features

- **GET /api/interest-rates**: Retrieve a list of available mortgage interest rates.
- **POST /api/mortgage-check**: Check if a mortgage is feasible and get the monthly payment.

## Features to add in the future

- Security check
- Save the mortgage check in the database
- Add a database (Remove in memory H2)

## Requirements

- **Java 21**
- **Docker** (optional, for containerized deployment)
- **Maven** (for local build, if not using Docker)

## Technologies Used

- **Spring Boot**
- **Java 21**
- **Swagger**
- **H2 Database**
- **Docker**

## Running the Application Locally

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-repo/mortgage-api.git
cd mortgage-api
```

### Step 2: Build the Project with Maven

If you don’t have Docker installed and prefer to run the application locally:

```bash
mvn clean install
mvn spring-boot:run
```

By default, the application will run on **`http://localhost:8080`**.

---

## Dockerizing the Application

### Step 1: Build the Docker Image



```bash
docker build -t mortgage-api .
```

### Step 2: Run the Docker Container

```bash
docker run -p 8080:8080 mortgage-api
```

This will start the application and map port `8080` on the host machine to the container’s port `8080`.

---

## Accessing Swagger UI

Once the application is running, you can access the **Swagger UI** for exploring and testing the API:

- Open your browser and navigate to `http://localhost:8080/swagger-ui.html`
