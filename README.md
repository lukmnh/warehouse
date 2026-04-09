# warehouse
Warehouse management system

- Items Management -> Full CRUD operations for products
- Variants Support -> Products can have variants (size, flavor, color)
- Categories & Brands -> Master data management
- Stock Tracking -> Real-time inventory levels
- OpenAPI Documentation -> Interactive Swagger UI

Prerequisites
- Java 21
- Postgresql
- Springboot 3.5.13

How to run the application :
first -> clone this repository https://github.com/lukmnh/warehouse.git in your git bash
second -> create or using existing database, create new schema called "geli"
after that -> configure database setup in .yaml to matching with your current database, username and password
then -> copy sql in db-migration, and then paste in your database then run the query.
fnally -> run the application on your IDE

design decision, use uuid for security (non guessing id). implement soft delete for data recovery if any accindetal deletions and keep history data. using base model so no need to duplicate for column that have same type data and same rule.

assumptions : 
single warehouse - system assumes one physical warehouse location, no backorders - reject order if stock insufficient, manual stock updates.

if want to check API endpot, run the application first then open this link on website : 
http://localhost:8080/swagger-ui.html
