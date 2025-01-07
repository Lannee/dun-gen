import { useState, useEffect, useRef } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

import { Button } from 'primereact/button';
import { Dropdown } from 'primereact/dropdown';
import { Messages } from 'primereact/messages';


export default function SpecialEyeColorCount({ getToken }) {
    const [results, setResults] = useState();
    const [color, setColor] = useState();
    const [count, setCount] = useState(0);
    const msgs = useRef(null);
	const navigate = useNavigate();

    const handleEyeColorCount = _ => {
        if (!color) {
            msgs.current.replace([
                { severity: 'error', life: 5000, summary: 'Error', detail: "Select a color from DropDown", sticky: false, closable: false }
            ]);
            return
        }
        
        axios.post(`http://localhost:8080/api/v1/person/all`, { token: getToken() })
        .then(res => {
            setResults(res.data);
        })
        .catch(function (error) {
            let myError = "";
            if (error.response) {
                // The request was made and the server responded with a status code
                // that falls out of the range of 2xx
                
                myError = error.response.data.message
            } else {
                // Something happened in setting up the request that triggered an Error

                myError = "An error during request setting up has happened"
            }
            msgs.current.replace([
                { severity: 'error', life: 5000, summary: 'Error', detail: myError, sticky: false, closable: false }
            ]);
        })
        
        let _count = 0
        for (let person in results) {
            if (results[person].eyeColor === color) {
                _count++
            }
        }
        
        msgs.current.show([
            { severity: 'success', life: 10000, summary: 'Success', detail: "Count of object with " + color + " eye color is " + _count, sticky: false, closable: true }
        ]);
    }

    const handleSpecialClick = _ => {
		setTimeout(() => {
			navigate('/special', { replace: true })
			navigate(0)
		}, 0)
	}

    const colorOptions = [
        { label: 'RED', value: 'RED' },
        { label: 'GREEN', value: 'GREEN' },
        { label: 'BROWN', value: 'BROWN' }
    ]

    return (
        <div className="flex flex-wrap align-items-baseline flex-column gap-4 m-4">
            <div className="card flex flex-column justify-content-center align-items-center">
                <Messages ref={msgs} />
            </div>
            <div className="">
                <label className='mr-3'>Color</label>
                <Dropdown 
                    options={colorOptions} 
                    value={color}
                    onChange={(e) => setColor(e.value)} 
                    placeholder="Select Color" 
                    required
                />
            </div>
            <Button onClick={handleEyeColorCount} label="Get count of objects with special eye color" icon="pi pi-palette"></Button>
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