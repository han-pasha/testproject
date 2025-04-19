# Test Project

## Description

This project serves as a solution for **problem 3 and 4**.

## Key Notes

- Because I'm supposed to use java 8, the springboot version and other dependencies
  is adjusted to the latest stable release for the springboot 2.7.18
- Authentication that being used is session based.
- Css and jquery is inbuilt with the xhtml for Jfs.
- For simplicity and time reason, function that isn't listed in the requirement wont be made. but not limited to it.
- Jersey implementation is in ProductResource.class
- Jackson is inbuilt with spring, but i do add a configuration and validation for it.
- Since jackson mainly used to serialize and deserialize, it didnt had any function in here because spring, 
automatically serialize and deserialize incoming request using it. the usage for this is mainly when the object is coming to our project in string format.
- for integration testing, please whitelist the "/api/mobile/**" in SecurityConfiguration because the test for security is in different place. and also this is due too time limit.
- Since this is for testing purpose, there wont be any other active profile or configuration based on profile.
- There might be some dependency issue in the pom file. Typical maven behaviour.

## Checkpoint

### Problem 3
- Login page ✅
- Add product (only name and price are required) ✅
- Update product (only name and price can be edited) ✅
- Delete product ✅
- Logout✅
- Jsf ✅
- Hibernate ✅
- Tomcat ✅
- Intellij ✅
- User password stored using SHA256 ✅
- Product prices encrypted using AES256 ✅
- initial admin data ✅

### Problem 4
- add restfull feature ✅
- Jersey ✅
- Jackson ✅
- returned value accordingly ✅
- securing the api ✅

### Outside feature
- Unit Testing service, util, and etc. ✅
- pagination and order ✅
- Integration Testing ✅
- api documentation ✅
- Exception Handling ✅

## Dependencies

Key dependencies include:

| **Dependency**                 | **Version**                 |
|--------------------------------|-----------------------------|
| Spring Boot Starter Web        | Included in parent (2.7.18) |
| Spring Boot Starter Data JPA   | Included in parent (2.7.18) |
| Spring Boot Starter Security   | Included in parent (2.7.18) |
| Spring Boot Starter Jersey     | Included in parent (2.7.18) |
| Bean Validation API            | 2.0.1.Final                |
| CDI API                        | 2.0                        |
| JoinFaces                      | 4.5.4                      |
| SpringDoc OpenAPI UI           | 1.6.14                     |
| Lombok                         | 1.18.30                    |

## Setup

### Clone the Repository

```bash
git clone https://github.com/your-repo-url.git
cd your-repo-folder

