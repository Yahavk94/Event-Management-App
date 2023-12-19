# Event Management App

[![License- MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/Yahavk94/Event-Management-App/blob/master/LICENSE)

## Overview

**Event Management App** is a Java-based application built with Spring Boot, designed for effective event data management. It provides RESTful endpoints for efficient event creation, updates and retrieval.

## Features

- **Create events** - easily generate new events with essential details such as name and location.
- **Update events** - effortlessly modify existing events to accommodate changes in details or scheduling.
- **Retrieve events** - access event information through various endpoints, offering sorting and filtering options for convenience.
- **Reminders** - receive automatic notifications for upcoming events via WebSocket communication.

## Technologies Used

- Java
- Spring Boot
- WebSocket
- Lombok

## API Endpoints

- **Create event** - `POST /v1/events`
- **Update event** - `PUT /v1/events/{id}`
- **Retrieve event by ID** - `GET /v1/events/{id}`
- **Get all events** - `GET /v1/events`
- **Delete event by ID** - `DELETE /v1/events/{id}`
- **Send reminders** - `GET /v1/events/send`

> Note - use `http://localhost:8080/v1/events` to access the API.

## API Calling Recommendation

I recommend using [Postman](https://www.postman.com/) for convenient interaction with the API endpoints. Import the provided [Postman collection](./postman/event_management_collection.json) to quickly get started with calling each endpoint. The collection includes sample requests and responses for easy reference.

## WebSocket Communication

Subscribe to `/topic/event-reminders` to receive real-time notifications for upcoming events scheduled within the next 30 minutes.

## Configure MySQL Database Properties (Optional)

Open `application.properties` file and provide the necessary properties for your database.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Make sure that all relevant SQL dependencies are included in the pom.xml file.
