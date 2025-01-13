import { useState, useRef } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

import { Button } from 'primereact/button';
import { Messages } from 'primereact/messages';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

export default function SpecialMaxId({ getToken }) {
    const [results, setResults] = useState([]);
    const msgs = useRef(null);
	const navigate = useNavigate();


    
    const handleMaxID = (e) => {
        e.preventDefault();
        const data = {
            token: getToken()
        };

        msgs.current.clear();
        axios.post(`http://localhost:8080/api/v1/person/max_id`, data)
            .then(res => {
                msgs.current.show([
                    { sticky: false, life: 2000, severity: 'success', summary: 'Success', detail: 'Successfully got object', closable: false },
                ])
                setResults([res.data])
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
            });
    };

    const handleSpecialClick = _ => {
		setTimeout(() => {
			navigate('/special', { replace: true })
			navigate(0)
		}, 0)
	}

    return (
        <div className="m-4">
            <Button onClick={handleMaxID} label="Get object Person with max ID" icon="pi pi-sort-amount-up"></Button>
            <div className="card flex flex-column justify-content-center align-items-center">
                <Messages ref={msgs} />
            </div>
            <DataTable className="mt-5" value={results} dataKey="id" showGridlines header="Person List">
                <Column field="id" header="ID" />
                <Column field="name" header="Name" />
                <Column field="coordinates_id" header="Coordinates ID" />
                <Column field="eyeColor" header="Eye Color" />
                <Column field="hairColor" header="Hair Color" />
                <Column field="location_id" header="Location ID" />
                <Column field="height" header="Height (cm)" />
                <Column field="nationality" header="Nationality" />
            </DataTable>
            <Button 
                label="Back to Special Operations" 
                icon="pi pi-arrow-left" 
                onClick={handleSpecialClick} 
                className="top-right-back" 
				style={{ position: "absolute", top: "85px", right: "20px" }}
            />
        </div>
    );
}