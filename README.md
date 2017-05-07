---
Developer: Supannikar Nontarak
Stack: Java
Framework: SpringBoot
---

B2N Order Service API
=========

RESTFul API for simple shopping basket for transactions between companies. It's including:

**Basket API**
 - id: Basket id,
 - basket_name: Basket name,
 - basket_desc: Basket description,
 - member_id: Member id,
 - basket_product: 
   - id: Basket product id,
   - basket_id: Basket id,
   - product_id: Product id,
   - product_qnty: Product quantity

**Basket Order API**
 - id: Order id,
 - basket_id: Basket id,
 - vat_no: Valid vat number
  

Database setup
--------------------------------
This API we are using MySQL as database and also liquibase are helping for creating a table.

Run this command for creating database:

```create database b2b character set utf8;```

```grant all privileges on b2b.* to b2b identified by 'password';```

```grant all privileges on b2b.* to b2b@'localhost' identified by 'password';```

```FLUSH PRIVILEGES;```

----------
Architecture Setup
--------------------------------
**Prerequisite**
- java (require JDK version 8)
- maven (require version 3)
- git

**Step for running project**

1. Clone project from repository: https://github.com/supannikar/b2b-springboot.git

2. Build project: mvn clean install

3. Run project: mvn spring-boot:run

**Step for running with jar file**

1. java -jar target/b2b-0.0.1-SNAPSHOT.jar

The API will be run on port 8092: 

 **Basket API**
  - http://localhost:8092/api/b2b/v1/baskets
 
 **Basket Order API**
  - http://localhost:8092/api/b2b/v1/orders
 
And also we've implement RestFul API documentation. It will be run on this link: http://localhost:8092/api/b2b/v1/docs

Description about API documentation
--------------------------------
**Basket API**
- Create new basket

```POST: http://localhost:8090/api/b2b/v1/baskets```

Request Body will provide seem like this:
```json
{
  "id": null,
  "basket_name": "basket name",
  "basket_desc": "basket desc",
  "member_id": 1,
  "basket_product": [
    {
      "id": null,
      "basket_id": null,
      "product_id": 1,
      "product_qnty": 10
    }
  ]
}
```

- Get all list

```GET: http://localhost:8090/api/b2b/v1/baskets```

- Get basket by id

```GET: http://localhost:8090/api/b2b/v1/baskets/{id}```

- Update existing basket by id

```PUT: http://localhost:8090/api/b2b/v1/todos/{id}```

JSON Request Body will provide same as create nw todo:
```json
{
  "id": 1,
  "basket_name": "basket name",
  "basket_desc": "basket desc",
  "member_id": 1,
  "basket_product": [
    {
      "id": 1,
      "basket_id": 1,
      "product_id": 1,
      "product_qnty": 10
    }
  ]
}
```

- Delete todo by id

```DELETE: http://localhost:8090/api/b2b/v1/baskets/{id}```

**Basket Order API**
- Create new order

```POST: http://localhost:8090/api/b2b/v1/orders```

Request Body will provide seem like this:
```json
{
  "id": null,
  "basket_id": 1,
  "vat_no": "LU26375245"
}
```

- Get all list

```GET: http://localhost:8090/api/b2b/v1/orders```

- Get order by id

```GET: http://localhost:8090/api/b2b/v1/orders/{id}```

- Update existing order by id

```PUT: http://localhost:8090/api/b2b/v1/orders/{id}```

JSON Request Body will provide same as create nw todo:
```json
{
  "id": 2,
  "basket_id": 1,
  "vat_no": "LU26375245"
}
```

- Delete todo by id

```DELETE: http://localhost:8090/api/b2b/v1/orders/{id}```
