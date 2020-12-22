# Exercise 3: React Search/Filter application

## Due Date: Tuesday January 21st, 2019 11:59 PM
## Objectives: To become aquainted with React components, state, and JSX, practice array functions (filter, map, reduce, etc..)

## Specifications/Requirements
We have provided you with a complete React application bootstrapped with the react cli tool. To complete this project you need to create 2 components: List and Search. 
The Search component will display a text input box that will allow the user to enter a city or state. As the user enters data the application will display a live list of results that match the current text in the input field. We have provided you with the fetch request inside of the componentDidMount lifecycle method in `Search.js`. Do not change or add any code to this method. You are responsible for taking the results from this request to check against the results from the fetch request.
Hint: you will need to define a handler to respond to an onChange event triggered inside of the input box. Additionally, once you retrieve it's value you will want to check to see if it matches the results from the fetch request using a regular expression. Here is a reference to regular expressions in javascript as well as an online tester:
https://www.w3schools.com/jsref/jsref_obj_regexp.asp  

https://regex101.com/  

Once you have stored the matched data you will then pass them to the List component.

In List.js you will create the component that will create an unordered list and display each matched item inside of it.
Note: React requires entries in an unordered list to contain a "key" property that must be uniques. For more information on lists and keys in React, read over the official documentation:

https://reactjs.org/docs/lists-and-keys.html

Hint: the List component will be receiving the collection of matched data as a prop. Additionally, for each list item the component will display the data in the following format:  ```<city> - <state>```. To know how to retrieve those values from the request object, inspect the JSON object in the console.



__Note: In order to run the application run ```npm install``` and then ```npm start``` in the top level directory of the exercise and open a brownser and navigate to localhost:3000 in order to see your application running. You can copy paste the endpoint to see the JSON objects__ 