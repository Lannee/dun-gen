import axios from "axios";

import { useState, useRef, useEffect } from 'react';
import { Dropdown } from 'primereact/dropdown';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';

export default function EditPersonFields({ id, coordinatesOptions, locationOptions, msgs, getToken }) {
    const [name, setName] = useState('');
    const [coordinates, setCoordinates] = useState(null);
    const [eyeColor, setEyeColor] = useState('');
    const [hairColor, setHairColor] = useState('');
    const [location, setLocation] = useState(null);
    const [height, setHeight] = useState('');
    const [nationality, setNationality] = useState('');

    useEffect(() => {
        axios.post(`http://localhost:8080/api/v1/person/${id}`, { token: getToken() })
        .then(res => {
            setName(res.data.name);
            setCoordinates(String(res.data.coordinates_id))
            setEyeColor(res.data.eyeColor)
            setHairColor(res.data.hairColor)
            setLocation(String(res.data.location_id))
            setHeight(res.data.height)
            setNationality(res.data.nationality)
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

    const getObjectById = (array, id) => {
        return array.find(item => item.value == id);
    };

    const colorOptions = [
        { label: 'RED', value: 'RED' },
        { label: 'GREEN', value: 'GREEN' },
        { label: 'BROWN', value: 'BROWN' }
    ]

    const countryOptions = [
        {label: "RUSSIA", value: "RUSSIA"},
        {label: "VATICAN", value: "VATICAN"},
        {label: "SOUTH_KOREA", value: "SOUTH_KOREA"},
        {label: "JAPAN", value: "JAPAN"}
    ]

    const handleSubmit = (e) => {
        e.preventDefault();
        const personData = {
            id: id,
            name: name,
            coordinates_id: coordinates,
            eye_color: eyeColor,
            hair_color: hairColor,
            location_id: location,
            height: height,
            nationality: nationality,
            token: getToken()
        };

        msgs.current.clear();
        axios.post(`http://localhost:8080/api/v1/person/edit`, personData)
            .then(res => {
                console.log(res.data);
                msgs.current.show([
                    { sticky: false, life: 2000, severity: 'success', summary: 'Success', detail: 'Successfully edited', closable: false },
                ])
            })
            .catch(function (error) {
                let myError = error.code;
                if (error.response) {
                    myError += " " + error.response.code;
                } else {
                    myError = "An error during request setting up has happened"
                }
                msgs.current.show([
                    { severity: 'error', life: 5000, summary: 'Error', detail: myError, sticky: false, closable: false }
                ]);
            });
    };

    return (
        <>
            <form onSubmit={handleSubmit}>
                <div className="fields flex flex-column gap-1 m-4">
                    <div className="field flex justify-content-between align-items-center ">
                        <label className='m-0'>Name</label>
                        <InputText className="w-7" value={name} onChange={(e) => setName(e.target.value)} placeholder="Write Name" required />
                    </div>
                    <div className="field flex justify-content-between align-items-center">
                        <label className='m-0'>Coordinates</label>
                        <Dropdown
                            options={coordinatesOptions} 
                            value={coordinates} 
                            onChange={(e) => setCoordinates(e.value)} 
                            placeholder="Select Coordinates" 
                            required 
                        />
                    </div>
                    <div className="field flex justify-content-between align-items-center">
                        <label className='m-0'>Eye Color</label>
                        <Dropdown 
                            options={colorOptions}
                            value={eyeColor}
                            onChange={(e) => setEyeColor(e.value)} 
                            placeholder="Select Color" 
                            required 
                        />
                    </div>
                    <div className="field flex justify-content-between align-items-center">
                        <label className='m-0'>Hair Color</label>
                        <Dropdown 
                            options={colorOptions} 
                            value={hairColor}
                            onChange={(e) => setHairColor(e.value)} 
                            placeholder="Select Color" 
                            required
                        />
                    </div>
                    <div className="field flex justify-content-between align-items-center">
                        <label className='m-0'>Location</label>
                        <Dropdown 
                            options={locationOptions} 
                            value={location} 
                            onChange={(e) => setLocation(e.value)} 
                            placeholder="Select Location" 
                            required 
                        />
                    </div>
                    <div className="field flex justify-content-between align-items-center">
                        <label className='m-0'>Height (cm)</label>
                        <InputText className="w-4" type="number" value={height} onChange={(e) => setHeight(e.target.value)} placeholder="Write Height in cm" required />
                    </div>
                    <div className="field flex justify-content-between align-items-center">
                        <label className='m-0'>Nationality</label>
                        <Dropdown 
                            options={countryOptions} 
                            value={nationality}
                            onChange={(e) => setNationality(e.value)} 
                            placeholder="Select nationality" 
                            required
                        />
                    </div>
                </div>
                
                <Button type="submit" label="Edit Person" icon="pi pi-check" />
            </form>
        </>
    );
}
