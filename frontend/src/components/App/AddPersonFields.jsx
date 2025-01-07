import axios from "axios";

import { useState, useRef, useEffect } from 'react';
import { Dropdown } from 'primereact/dropdown';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Checkbox } from 'primereact/checkbox';


export default function AddPersonFields({ coordinatesOptions, locationOptions, msgs, getToken }) {
    const [name, setName] = useState('');
    const [coordinates, setCoordinates] = useState(null);
    const [eyeColor, setEyeColor] = useState('');
    const [hairColor, setHairColor] = useState('');
    const [location, setLocation] = useState(null);
    const [height, setHeight] = useState('');
    const [nationality, setNationality] = useState('');
    const [isEditableByAdmin, setIsEditableByAdmin] = useState(false);

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
            name: name,
            coordinates_id: coordinates,
            eye_color: eyeColor,
            hair_color: hairColor,
            location_id: location,
            height: height,
            nationality: nationality,
            editableByAdmin: isEditableByAdmin,
            token: getToken()
        };
        
        console.log(personData)

        msgs.current.clear();
        axios.post(`http://localhost:8080/api/v1/person/add`, personData)
            .then(res => {
                console.log(res.data);
                msgs.current.show([
                    { sticky: false, life: 2000, severity: 'success', summary: 'Success', detail: 'Successfully created', closable: false },
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
                            placeholder="Select color" 
                            required 
                        />
                    </div>
                    <div className="field flex justify-content-between align-items-center">
                        <label className='m-0'>Hair Color</label>
                        <Dropdown 
                            options={colorOptions} 
                            value={hairColor}
                            onChange={(e) => setHairColor(e.value)} 
                            placeholder="Select color" 
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
                        <InputText className="w-4" type="number" value={height} onChange={(e) => setHeight(e.target.value)} required />
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
                    <div className="field flex justify-content-around align-items-center">
                        <label className='m-0'>Editable by Admin?</label>
                        <Checkbox 
                            inputId="isEditableByAdmin" 
                            checked={isEditableByAdmin} 
                            onChange={(e) => {setIsEditableByAdmin(e.checked)}} 
                        />
                    </div>
                </div>
                
                <Button type="submit" label="Create Person" icon="pi pi-check" />
            </form>
        </>
    );
}
