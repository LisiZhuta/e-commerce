# E-Commerce Platform

E-Commerce Platform is a web application that enables users to browse products, place orders, and manage their accounts. This platform provides a seamless shopping experience and efficient order management.

## Table of Contents

- [Project Overview](#project-overview)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Testing](#testing)
- [Database](#database)

## Project Overview

E-Commerce Platform aims to simplify online shopping by offering a user-friendly interface for customers to explore products, place orders, and manage their profiles. It includes features such as user authentication, product listings, order processing, and address management.

## Getting Started

To run this project locally, follow these instructions:

### Prerequisites

- Java JDK 
- Gradle
- MySQL Database

### Installation

1. Clone the repository:

      git clone https://github.com/LisiZhuta/e-commerce.git (My project is private but i can make it public)

### Testing
For testing this project i used mainly insomnia(since there are a bunch of endpoints i will provide the endpoints i worked with in a file inside the project named endpoints just to make it easier.
IMPORTANT! When logging in in the response a jwt token will show. You will need to copy that and in some endpoints make Authorization Bearer token and put that token inside the bearer token.otherwise some endpoints wont work



### Database

For the database you will need to connect to your own mysql,create a database lets say ecommerce and update the password (mine is lisi123) and also the database name.
After that you run the project to create the tables. In the project folder i will provide a mysql query to insert some data to play with
Note! in my mysql Workbench(mac version) i had to input the query line by line it didnt process a whole text of queries.application.properties is very important for encryption and security.