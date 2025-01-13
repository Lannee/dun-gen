import { useState, useRef } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";

import { Button } from 'primereact/button';
import { Messages } from 'primereact/messages';

export default function Request({ getToken }) {
    const msgs = useRef(null);
    const navigate = useNavigate();
    
    const handleSubmit = async e => {
        e.preventDefault();
    
        let data = {
            token: getToken()
        }
        msgs.current.clear();
        axios.post(`http://localhost:8080/api/v1/admin/add`, data)
            .then(res => {
                msgs.current.show([
                    { sticky: false, life: 2000, severity: 'success', summary: 'Success', detail: res.data.status, closable: false },
                ])
                setTimeout(() => {
                    navigate('/home', { replace: true })
                    navigate(0)
                }, 2000)
            })
            .catch(function (error) {
                let myError = "";
                if (error.response) {
                    // The request was made and the server responded with a status code
                    // that falls out of the range of 2xx
                    console.log(error.response.data);
                    myError = error.response.data.message
                } else {
                    // Something happened in setting up the request that triggered an Error
                    console.log('Error', error.message);
                    myError = "An error during request setting up has happened"
                }
                msgs.current.show([
                    { severity: 'error', life: 5000, summary: 'Error', detail: myError, sticky: false, closable: false }
                ]);
            });
    }

    return (
        <>
            <div className="about mx-5 my-5">
                <form onSubmit={handleSubmit}>
                    <Button type="submit" label="Request admin priviliges" icon="pi pi-user-plus" className="w-12rem mx-auto p-4"></Button>
                    <Messages ref={msgs} />
                </form>
            </div>
        </>
    );
}