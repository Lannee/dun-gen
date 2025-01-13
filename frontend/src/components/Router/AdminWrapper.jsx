import axios from "axios";

import { useState, useEffect } from "react";
import { getToken } from "../Authorization/token.js";

import Router from './Router.jsx'

export default function AdminWrapper() {
    let token = getToken()

    const [isAdmin, setIsAdmin] = useState(false);

    let data = {
        token: token
    }
    useEffect(() => {
        axios.post(`http://localhost:8080/api/v1/admin/check`, data)
        .then(res => {
            setIsAdmin(res.data.status)
        }).catch(function (error) {});
    }, [])
    return (
        <Router isAdmin={isAdmin}/>
    )
}