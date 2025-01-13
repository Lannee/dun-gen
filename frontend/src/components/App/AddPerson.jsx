import axios from "axios";

import { useState, useRef, useEffect } from 'react';
import { Dropdown } from 'primereact/dropdown';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Messages } from 'primereact/messages';

import AddPersonFields from "./AddPersonFields";

export default function AddPerson({ getToken }) {
    const msgs = useRef(null);

    const [coordinatesOptions, setCoordinatesOptions] = useState([]);
    const [locationOptions, setLocationOptions] = useState([]);
    
    function performCoordinatesOptions(coords)  {
        return coords.map(coord => {
            return {
                label: `id: ${coord.id}, X: ${coord.x}, Y: ${coord.y}`, // Constructing the label
                value: `${coord.id}` // Constructing the value
            };
        });
    }
    
    function performLocationOptions(locations)  {
        return locations.map(loc => {
            return {
                label: `id: ${loc.id}, X: ${loc.x}, Y: ${loc.y}, Z: ${loc.z}`, // Constructing the label
                value: `${loc.id}` // Constructing the value
            };
        });
    }
    
    useEffect(() => {
        msgs.current.clear();
        axios.post(`http://localhost:8080/api/v1/coordinates/all`, { token: getToken() })
            .then(res => {
                console.log(res.data);
                setCoordinatesOptions(performCoordinatesOptions(res.data));
            })
            .catch(function (error) {
                let myError = "";
                if (error.response) {
                    myError = error.response.data.message
                } else {
                    myError = "An error during request setting up has happened"
                }
                msgs.current.replace([
                    { severity: 'error', life: 5000, summary: 'Error', detail: myError, sticky: false, closable: false }
                ]);
            })
    
        axios.post(`http://localhost:8080/api/v1/location/all`, { token: getToken() })
            .then(res => {
                console.log(res.data);
                setLocationOptions(performLocationOptions(res.data));
            })
            .catch(function (error) {
                let myError = "";
                if (error.response) {					
                    myError = error.response.data.message
                } else {
                    myError = "An error during request setting up has happened"
                }
                msgs.current.replace([
                    { severity: 'error', life: 5000, summary: 'Error', detail: myError, sticky: false, closable: false }
                ]);
            })
    }, [])

    return (
        <div className="form-container">
            <h1>Create Person</h1>
            <div className="card flex flex-column">
                <Messages ref={msgs} />
            </div>
            <AddPersonFields coordinatesOptions={coordinatesOptions} locationOptions={locationOptions} msgs={msgs} getToken={getToken}/>
        </div>
    );
}
