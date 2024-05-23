import React, {Component} from "react";
import "bootstrap/dist/css/bootstrap.min.css"
import './App.css';
import { Routes, BrowserRouter, Route, Link } from "react-router-dom";

import ListArtigo from "./components/listArtigo";
import AddArtigo from "./components/addArtigo";
import Artigo from "./components/artigo";


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
                  <div className="container mt-3">
                    <Routes>
                      <Route element={<ListArtigo />} path="/" />
                      <Route element={<ListArtigo />} path="/list" />
                      <Route element={<AddArtigo />} path="/add" />
                      <Route element={<Artigo />} path="/list/:id" />
                    </Routes>
                  </div>
                </BrowserRouter>
              </div>
             );
  }
}
export default App;
