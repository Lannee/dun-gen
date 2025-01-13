import axios from "axios";

import { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Messages } from 'primereact/messages';

import EditPersonFields from "./EditPersonFields";


export default function EditPerson({ getToken }) {
    const msgs = useRef(null);

    const { id } = useParams(); // Get the ID from the URL
    const [person, setPerson] = useState({});
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
            <div className="flex flex-wrap align-items-center gap-4 m-4">
                <div className="form-container">
                    <h1>Editing Person with id {id}</h1>
                    
                    <div className="card flex flex-column">
                        <Messages ref={msgs} />
                    </div>
                    <EditPersonFields id={id} coordinatesOptions={coordinatesOptions} locationOptions={locationOptions} msgs={msgs} getToken={getToken}/>
                </div>
            </div>
        </div>
    )
}
