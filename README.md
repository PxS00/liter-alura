# 📚 LiterAlura

**LiterAlura** is a Java Spring Boot console application that integrates with an external books API to search, store, and analyze literary data. The project focuses on clean architecture, relational database modeling, and API consumption.

---

## 🚀 Features

* 🔍 Search books by title using an external API
* 💾 Persist books and authors in a PostgreSQL database
* 👨‍💻 Avoid duplicate authors using relational mapping
* 🌐 Filter books by language
* 📖 List stored books
* ✍️ List stored authors
* 📅 Find authors alive in a specific year
* 🔗 Many-to-Many relationship between Books and Authors

---

## 🛠 Technologies

* Java 17+
* Spring Boot
* Spring Data JPA / Hibernate
* PostgreSQL
* Jackson (JSON Parsing)
* Maven
* Java HTTP Client

---

## 🧱 Architecture

The project follows a layered architecture to ensure maintainability and scalability:

```
Main (CLI Layer)
   ↓
Service Layer
   ↓
Repository Layer
   ↓
Model Layer (Entities & DTOs)
   ↓
Integration Layer (External API)
```

---

## 📡 External API

This project consumes book data from the Gutendex API, which provides access to Project Gutenberg’s digital library.

---

## 🗄 Database Modeling

The application uses relational mapping with JPA/Hibernate:

* A Book can have multiple Authors
* An Author can write multiple Books
* Implemented using a Many-to-Many relationship

---

## 📂 Project Structure

```
literalura
 ┣ main
 ┃ ┗ CLI interaction
 ┣ service
 ┃ ┗ Business logic
 ┣ repository
 ┃ ┗ Database access
 ┣ model
 ┃ ┗ Entities and DTOs
 ┗ integration
   ┗ External API communication
```

---

## ⚙️ Setup & Installation

### 1️⃣ Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/literalura.git
```

### 2️⃣ Configure Database

Create a PostgreSQL database:

```
liter_alura
```

### 3️⃣ Configure Environment Variables

```env
LITERALURA_DB_URL=jdbc:postgresql://localhost/liter_alura
POSTGRES_DB_USER=your_user
POSTGRES_DB_PASS=your_password
```

### 4️⃣ Run the Project

```bash
./mvnw spring-boot:run
```

---

## 🧪 Example Menu

```
1 - Search book
2 - Search author
3 - Stored books
4 - Stored authors
5 - Authors alive in a specific year
6 - Books by language
0 - Exit
```

---

## 🎯 Learning Goals

This project was developed to practice:

* REST API consumption in Java
* Database persistence using JPA/Hibernate
* Entity relationship modeling
* Clean code and layered architecture
* Exception handling and data validation

---

## 📌 Future Improvements

* REST API version of the application
* Unit and integration testing
* Pagination and advanced search filters
* Docker containerization
* User interface

---

## 👨‍💻 Author

Developed by **Lucas Rossoni Dieder**

---

## 📜 License

This project is for educational purposes.
