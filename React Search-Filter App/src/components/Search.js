import React, { Component } from "react";
import List from "./List";

class Search extends Component {
  
  
  constructor(props) {
    super(props);
    this.state = {
      name: '',
      places: [],
      myArr: []
    };
  }

  handleNameChange = event => {
    this.setState({ name: event.target.value }, () => {
      this.validateName();
    });
  };


  validateName = () => {
    const { name } = this.state;
    
    const reg = new RegExp (name , 'i')

    this.setState({
      myArr: this.state.places.filter(i =>
        i.city.match(reg) || i.state.match(reg)).map(
          i => i.city + " - " + i.state)  
    });

  }


  render() {
        return (
          <div>
          <form onSubmit={this.handleSubmit}>
            <div className='form-group'>
              <label htmlFor='name'>Name</label>
              <input
               
                className={`form-control`}
                placeholder='Enter city or state'
                value={this.state.name}
                onChange={this.handleNameChange}
              />
             
            </div>
            
          </form>
          <List value = {this.state.myArr}/>

          </div>
        );
      }

  

  /* DO NOT MODIFY */
  componentDidMount() {
    const allPlaces = [];
    const endpoint =
      "https://gist.githubusercontent.com/Miserlou/c5cd8364bf9b2420bb29/raw/2bf258763cdddd704f8ffd3ea9a3e81d25e2c6f6/cities.json";
    fetch(endpoint)
      .then(data => data.json())
      .then(results => {
        allPlaces.push(...results)
        this.setState({ places: allPlaces })
      })
      .catch(error => console.log(error));
  }

}

export default Search;