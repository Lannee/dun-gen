import axios from "axios";

import { useState, useRef } from 'react';
import { Dropdown } from 'primereact/dropdown';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Messages } from 'primereact/messages';
import { Checkbox } from 'primereact/checkbox';


export default function AddLocation ({ getToken }) {
    const msgs = useRef(null);
    const [x, setX] = useState('');
    const [y, setY] = useState('');
    const [z, setZ] = useState('');
    const [isEditableByAdmin, setIsEditableByAdmin] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();
        const locationData = {
            x: x,
            y: y,
            z: z,
            editableByAdmin: isEditableByAdmin,
            token: getToken()
        };

        msgs.current.clear();
        axios.post(`http://localhost:8080/api/v1/location/add`, locationData)
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
        <div className="form-container">
            <h1>Create Location</h1>
            <div className="card flex flex-column">
                <Messages ref={msgs} />
            </div>
            <form onSubmit={handleSubmit}>
                <div className="fields flex flex-column gap-1 m-4">
                    <div className="field flex justify-content-around align-items-center">
                        <label className='m-0'>X</label>
                        <InputText className="w-6" type="number" value={x} onChange={(e) => setX(e.target.value)} required />
                    </div>
                    <div className="field flex justify-content-around align-items-center">
                        <label className='m-0'>Y</label>
                        <InputText className="w-6" type="number" value={y} onChange={(e) => setY(e.target.value)} required />
                    </div>
                    <div className="field flex justify-content-around align-items-center">
                        <label className='m-0'>Z</label>
                        <InputText className="w-6" type="number" value={z} onChange={(e) => setZ(e.target.value)} required />
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
                
                <Button type="submit" label="Create Location" icon="pi pi-check" />
            </form>
        </div>
    );
}
