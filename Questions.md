### Questions to answer as part of the final exercise for CMMSC388A  
(just add your answers here after each questions)  

1. What are the React component lifecycle methods?  

   They are special methods on the component class that are run when a component mounts and unmounts
   
   Some examples are render(), constructor(), componentDidMount(), componentDidUpdate(), componentWillUnmount()


2.  In what direction does data flow between React components?  

    parent to child


3.  What is the name of the technique for a child component communicating a state change to the parent?  

    callbacks


4.  Describe the process/mechanism that accomplishes the task in question 3.  
    We can make a callback function that takes in a parameter which will have access from the child in the parent class. Then we send the
    defined callback function as a props to the child class, where we 
    send the data to the parent class using the callback function.



5.  what does this line of code do:  
```javascript
  let data = {
              foo: "Some data",
              bar: "Some more data"
            }
  let [foo, bar] = data
  console.log(`${foo} and then ${bar}`)
```
    it prints out "Some data and then Some more data"


6.  True or False. Javascript is object oriented  
    true

7. How do you create state for a component?  
   initialize it in the constructor (such as this.state)


8.  True or false. Props can be Read and Write 
    false


9.  True or false. Every component can have state.  
    true


10. What did you enjoy most about the course? What did you enjoy the least?    I enjoyed the interesting exercises that helped introduce me to
    React.js. I enjoyed the late release of the exercises the least. 

