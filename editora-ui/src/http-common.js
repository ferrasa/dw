import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:8000/editora",
    headers: {
        "Content-type": "application/json"
    }

});

