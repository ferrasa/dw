import React, {Component} from "react";
import "bootstrap/dist/css/bootstrap.min.css"
import './App.css';
import { BrowserRouter, Link } from "react-router-dom";

class App extends Component {
  render() {
      return (
              <div>
                <BrowserRouter>
                  <nav className="navbar navbar-expand-lg navbar-dark bg-secondary">
                    <div className="container">
                      <Link to={"/list"} className="navbar-brand">
                        <b><i>Editora</i></b>
                      </Link>
                      <div className="navbar-nav mr-auto">
                        <li className="nav_item">
                          <Link to={"/list"} className="nav-link">
                            Listar
                          </Link>
                        </li>
                        <li className="nav_item">
                          <Link to={"/add"} className="nav-link">
                            Adicionar
                          </Link>
                        </li>
                      </div>

                    </div>
                  
                  </nav>
                </BrowserRouter>
              </div>
             );
  }
}
export default App;
