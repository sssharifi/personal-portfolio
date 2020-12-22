import React, { Component } from "react";

class List extends Component {

    render() {
        // const temp = this.props.text;
        
        return (
            
            <ul>
                {this.props.value.map((item, index) =>
                <li key={index}>{item}</li>)}
            </ul>
        );  
    }

}


export default List;