# Selfcare Management Backend

Selfcare Management Backend is a REST API backend server that stores and processes treatment data from our Android application

## Requirements

- [NodeJS](https://nodejs.org/dist/v14.16.0/node-v14.16.0-x64.msi) installed
- [MongoDB](https://docs.mongodb.com/manual/installation/) installed

## Installation

1. Make sure MongoDB is running

2. From our backend server project folder, execute the following commands:

```bash
npm install
npm start
```

3. Visit your browser on [http://localhost:3000](http://localhost:3000) and you should see the line "Selfcare Management backend server". That proves that the server is running

## Technical Overview

For this project, we will be using [ExpressJS](https://expressjs.com/) as the library for handling REST API and [Mongoose](https://mongoosejs.com/docs/) as the library for handling MongoDB database connection. We will also be using [EJS](https://ejs.co/) as the main rendering engine for our HTML templates.

To familiarize yourself with what each library does, you can take a quick glance of their introduction page to know how things work with our project.

Below is the short description for the main project folders. Folders that are not listed inside this list are usually not important to the main logic of our app.

    ├── models         # Contains JavaScript codes that mimics the schema of a collection (table) inside MongoDB
    ├── routes         # Contains JavaScript logic code that dictates how the server should behave when an API endpoint is visited
    ├── views          # Contains EJS files that renders the HTML page


## License
[MIT](https://choosealicense.com/licenses/mit/)