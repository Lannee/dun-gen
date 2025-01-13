import { useState, useEffect } from "react";
import {
    BrowserRouter,
    Route,
    Routes,
} from "react-router-dom";

import App from "../../Pages/App";
import Home from "../../Pages/Home";
import About from "../../Pages/About";
import NotFound from "./NotFound";
import Header from '../App/Header.jsx';
import Register from "../Authorization/Register.jsx";
import Login from "../Authorization/Login.jsx";
import Request from "../Authorization/Request.jsx";
import Approve from "../App/Approve.jsx";

import { getToken, setTokenLS, logout } from "../Authorization/token.js";
import Add from "../App/Add.jsx";
import AddPerson from "../App/AddPerson.jsx";
import AddLocation from "../App/AddLocation.jsx";
import AddCoordinates from "../App/AddCoordinates.jsx";
import AddWrapper from "../App/AddWrapper.jsx";
import EditPerson from "../App/EditPerson.jsx";
import Special from "../App/Special/Special.jsx"
import SpecialMaxId from "../App/Special/SpecialMaxId.jsx"
import SpecialNameFilter from "../App/Special/SpecialNameFilter.jsx"
import SpecialHeightFilter from "../App/Special/SpecialHeightFilter.jsx"
import SpecialHairColorCount from "../App/Special/SpecialHairColorCount.jsx"
import SpecialEyeColorCount from "../App/Special/SpecialEyeColorCount.jsx"
import History from "../App/History.jsx"
import Import from "../App/Import/Import.jsx"
import ImportFile from "../App/Import/ImportFile.jsx"
import ImportHistory from "../App/Import/ImportHistory.jsx"


export default function Router({ isAdmin }) {
    let token = getToken()

    if (!token) {
        return (
            <BrowserRouter>
                <Routes>
                    <Route path='*' element={<NotFound />} />
                    <Route path="/" element={<Header getToken={getToken} logout={logout} isAdmin={false}/>}>
                        <Route path="/home" element={<Home />} />
                        <Route path="/about" element={<About />} />
                    </Route>
                    <Route path="/register" element={<Register />} />
                    <Route path="/login" element={<Login setToken={setTokenLS} />} />
                </Routes>
            </BrowserRouter>
        );
    }

    return (
        <BrowserRouter>
            <Routes>
                <Route path='*' element={<NotFound />} />
                <Route path="/" element={<Header getToken={getToken} logout={logout} isAdmin={isAdmin} />}>
                    <Route path="/" element={<App getToken={getToken} isAdmin={isAdmin}/>} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/about" element={<About />} />

                    <Route path="/request" element={<Request getToken={getToken} />} />
                    {isAdmin && 
                        <Route path="/approve" element={<Approve getToken={getToken} />} />
                    }

                    <Route path="/add" element={<Add getToken={getToken} />} />
                    <Route path="/add/coordinates" element={<AddWrapper Component={AddCoordinates} getToken={getToken} />} />
                    <Route path="/add/location" element={<AddWrapper Component={AddLocation} getToken={getToken} />} />
                    <Route path="/add/person" element={<AddWrapper Component={AddPerson} getToken={getToken} />} />

                    <Route path="/special" element={<Special/>} />
                    <Route path="/special/max_id" element={<SpecialMaxId getToken={getToken} />} />
                    <Route path="/special/name_filter" element={<SpecialNameFilter getToken={getToken} />} />
                    <Route path="/special/height_filter" element={<SpecialHeightFilter getToken={getToken} />} />
                    <Route path="/special/hair_color_count" element={<SpecialHairColorCount getToken={getToken} />} />
                    <Route path="/special/eye_color_count" element={<SpecialEyeColorCount getToken={getToken} />} />

                    <Route path="/history/person/:id" element={<History getToken={getToken} object="person" />} />
                    <Route path="/history/coordinates/:id" element={<History getToken={getToken} object="coordinates" />} />
                    <Route path="/history/location/:id" element={<History getToken={getToken} object="location" />} />

                    <Route path="/import" element={<Import />} />
                    <Route path="/import/file" element={<ImportFile getToken={getToken} />} />
                    <Route path="/import/history" element={<ImportHistory getToken={getToken} />} />
                </Route>
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login setToken={setTokenLS} />} />
            </Routes>
        </BrowserRouter>
    );
}
