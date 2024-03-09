# Weather Forecast Telegram Bot
> This project is a Telegram weather forecast bot that uses OpenWeatherMap API to provide weather forecast information to users on Telegram.
> Live demo [_here_](https://t.me/weather_2491_bot). 

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Setup](#setup)
* [Project Status](#project-status)
* [Contact](#contact)


## General Information
- This is Java-based application that uses OpenWeatherMap API to provide weather forecast information to users on Telegram.
- The problem that the project intends to solve is to make it easier for Telegram users 
to obtain accurate and up-to-date weather information in their location without having to search for it on other websites or applications.
- The purpose of the project is to create a Telegram bot that can provide users with real-time weather forecast information based on their location. 
It aims to make the process of obtaining weather information quick and easy for users.


## Technologies Used
- Java 17 
- SpringBoot
- Spring Data JPA
- TelegramBots API
- OpenWeatherMap API
- PostgreSQL
- Docker
- Flyway
- Lombok


## Features
- Get weather forecast by city name and date
- Set notifications for tomorrow's forecast by given city and time
- Set notifications for today's forecast in morning (7:00 AM) and afternoon (15:00) by given city
- Delete all subscribed notifications
- Change language and unit system


## Setup
At first, you need to specify enviroment variables, presented in application.properties file on your local machine.

Then, if you want to run latest version of this bot:  
- `docker compose -f docker-compose.pull.yml up -d` to start  
- `docker compose -f docker-compose.pull.yml down` to stop  

If you are making changes to the code:  
- `docker compose -f docker-compose.build.yml up -d` to start  
- `docker compose -f docker-compose.build.yml down` to stop  

## Project Status
Project is: _complete_


