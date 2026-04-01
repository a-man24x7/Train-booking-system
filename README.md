# 🚆 Train Booking System

## 📌 About Project

This is a **Train Booking System Web Application** developed using **Java (Servlets), JDBC, MySQL, and Apache Maven**.

The system allows users to:

* View train schedules
* Search trains between stations
* Check seat availability
* Book and cancel tickets
* View booking history

It also provides an **Admin Panel** to manage trains and bookings.

---

## 🎯 Features

### 👤 User Features

* Register & Login
* Search Trains
* Check Seat Availability
* Book Tickets
* Cancel Tickets
* View Booking History
* Update Profile
* Change Password
* Logout

---

### 🛠 Admin Features

* Admin Login
* Add Trains
* Update Train Details
* Delete Trains
* View All Trains
* Manage Bookings
* Logout

---

## 🛠️ Technologies Used

### Frontend

* HTML
* CSS

### Backend

* Java (Servlets)
* JDBC

### Database

* MySQL

### Tools

* Eclipse IDE (Enterprise Edition)
* Apache Maven
* Apache Tomcat 9
* MySQL Workbench
* Git

---

# ⚙️ Software Installation & Setup (Step-by-Step)

## 🔹 Step 1: Install Java (JDK 8)

Download and install:

* Java Development Kit

After installation, verify:

```bash
java -version
```

---

## 🔹 Step 2: Install Eclipse IDE

Download:

* Eclipse IDE for Enterprise Java Developers

Install and open Eclipse.

---

## 🔹 Step 3: Install Apache Maven

Download:

* Apache Maven

After installation, verify:

```bash
mvn -version
```

---

## 🔹 Step 4: Install Apache Tomcat

Download:

* Apache Tomcat

Extract and keep folder ready.

---

## 🔹 Step 5: Install MySQL

Download:

* MySQL

Install and remember:

* Username (e.g., root)
* Password

---

## 🔹 Step 6: Install MySQL Workbench

Download:

* MySQL Workbench

Used to manage database easily.

---

## 🔹 Step 7: Install Git

Download:

* Git

Verify:

```bash
git --version
```

---

# 🗄️ Database Setup (MySQL)

## Step 1: Create Database

```sql
CREATE DATABASE trainbook;
USE trainbook;
```

---

## Step 2: Create Tables

```sql
CREATE TABLE admin (
  mailid VARCHAR(40) PRIMARY KEY,
  pword VARCHAR(20),
  fname VARCHAR(20),
  lname VARCHAR(20),
  addr VARCHAR(100),
  phno BIGINT
);

CREATE TABLE customer (
  mailid VARCHAR(40) PRIMARY KEY,
  pword VARCHAR(20),
  fname VARCHAR(20),
  lname VARCHAR(20),
  addr VARCHAR(100),
  phno BIGINT
);

CREATE TABLE train (
  tr_no INT PRIMARY KEY,
  tr_name VARCHAR(70),
  from_stn VARCHAR(20),
  to_stn VARCHAR(20),
  seats INT,
  fare DECIMAL(8,2)
);

CREATE TABLE history (
  transid VARCHAR(36) PRIMARY KEY,
  mailid VARCHAR(40),
  tr_no INT,
  date DATE,
  from_stn VARCHAR(20),
  to_stn VARCHAR(20),
  seats INT,
  amount DECIMAL(8,2)
);
```

---

## Step 3: Insert Sample Data

```sql
INSERT INTO admin VALUES('admin@gmail.com','12345','Admin','User','Demo Address','9999999999');

INSERT INTO customer VALUES('aman@gmail.com','12345','Aman','Singh','India','9999999999');

INSERT INTO train VALUES(10001,'Express A','Delhi','Mumbai',100,500);
INSERT INTO train VALUES(10002,'Express B','Bangalore','Chennai',80,300);
```

---

# 🔑 Default Login Credentials

### 👨‍💼 Admin

* Email: **[admin@gmail.com](mailto:admin@gmail.com)**
* Password: **12345**

### 👤 User

* Email: **[aman@gmail.com](mailto:aman@gmail.com)**
* Password: **12345**

---

# 🚀 How to Run the Project

## Step 1: Clone Repository

```bash
git clone https://github.com/YOUR_USERNAME/train-booking-system.git
```

---

## Step 2: Import Project in Eclipse

* Open Eclipse
* File → Import
* Select **Existing Maven Project**
* Choose project folder

---

## Step 3: Update Maven

* Right Click Project
* Maven → Update Project

---

## Step 4: Configure Database

Open `application.properties`:

```properties
username=your_username
password=your_password
connectionString=jdbc:mysql://localhost:3306/trainbook
```

---

## Step 5: Configure Tomcat Server

* Go to Servers tab in Eclipse
* Add Apache Tomcat 9
* Select Tomcat installation folder

---

## Step 6: Run Project

* Right click project → Run on Server

---

## Step 7: Open in Browser

```
http://localhost:8080/TrainBook/
```

---

# 📁 Project Structure

```
src/
 └── com.aman
     ├── beans
     ├── constant
     ├── service
     ├── service.impl
     ├── servlets
     └── utility

WebContent/
pom.xml
```

---

# ⚠️ Common Issues

### ❌ Database Connection Failed

* Check MySQL is running
* Check username/password

---

### ❌ Port 8080 Already Used

* Change Tomcat port to 8081

---

### ❌ Maven Dependency Error

```bash
mvn clean install
```

---

# 🔮 Future Improvements

* Upgrade to Spring Boot
* Add REST APIs
* Improve UI
* Add Authentication Security

---

## 👨‍💻 Author

Aman Singh
