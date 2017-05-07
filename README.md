---
Developer: Supannikar Nontarak
Stack: Java
Framework: SpringBoot
---

Todo API
=========

RESTFul API Create/Update and Delete for todo. It's including:

 - id : Todo id
 - subject : Subject of todo
 - detail : The detail of todo
 - status : Status of todo. We prefer 2 statuses (Done and Pending)

Database setup
--------------------------------
This API we are using MySQL as database and also liquibase are helping for creating a table.

Run this command for creating database:

```create database todo character set utf8;```

```grant all privileges on todo.* to todo identified by 'password';```

```grant all privileges on todo.* to todo@'localhost' identified by 'password';```

```FLUSH PRIVILEGES;```

----------
Architecture Setup
--------------------------------
**Prerequisite**
- java (require JDK version 8)
- maven (require version 3)
- git

**Step for running project**

1. Clone project from repository: git clone git@github.com:supannikar/nitest.git

2. Build project: mvn clean install

3. Run project: mvn spring-boot:run

**Step for running with jar file**

1. java -jar target/nitest-0.0.1-SNAPSHOT.jar

The API will be run on port 8090: http://localhost:8090/api/nitest/v1/todos
And also we've implement RestFul API documentation. It will be run on this link: http://localhost:8090/api/nitest/v1/docs

Description about API documentation
--------------------------------
- Create new todo

```POST: http://localhost:8090/api/nitest/v1/todos```

Request Body will provide seem like this:
```json
{
    "id":null,
    "subject":"subject of todo",
    "detail":"detail of todo",
    "status":"Pending"
}
```

- Get all list

```GET: http://localhost:8090/api/nitest/v1/todos```

- Get todo by id

```GET: http://localhost:8090/api/nitest/v1/todos/{id}```

- Update existing todo by id

```PUT: http://localhost:8090/api/nitest/v1/todos/{id}```

JSON Request Body will provide same as create nw todo:
```json
{
    "id":1,
    "subject":"subject of todo",
    "detail":"detail of todo",
    "status":"Pending"
}
```

- Delete todo by id

```DELETE: http://localhost:8090/api/nitest/v1/todos/{id}```
