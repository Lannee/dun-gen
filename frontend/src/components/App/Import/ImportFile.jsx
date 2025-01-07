import { useState, useRef } from 'react';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import { Messages } from 'primereact/messages';
import { ProgressSpinner } from 'primereact/progressspinner';

import axios from 'axios';

export default function ImportFile({ getToken }) {
    const msgs = useRef(null);
    const [file, setFile] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        let myError = "";
        if (!file) {
            myError = 'Please select a file to upload.'
            msgs.current.replace([
                { severity: 'error', life: 5000, summary: 'Error', detail: myError, sticky: false, closable: false }
            ]);
            return;
        }           

        setLoading(true);
        const formData = new FormData();
        formData.append('file', file);
        formData.append("token", getToken());
        
        axios.post(`http://localhost:8080/api/v1/import/file`, formData, {             
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            })
        .then(res => {
            setLoading(false);
            console.log(res)
            if (res.data.status.valueOf() == "FAILED" || res.data.status.valueOf() == "NOT_FOUND") {
                let myError = "";
                let errors = res.data.errors
                for (const [key, value] of Object.entries(errors)) {
                    myError = myError + `${key}: ${value} `
                }
                msgs.current.replace([
                    { severity: 'error', life: 10000, summary: 'Error', detail: myError, sticky: false, closable: false }
                ]);
        }    else if (res.data.status.valueOf() == "SUCCESSFUL") {
                msgs.current.show([
                    { severity: 'success', life: 10000, summary: 'Success', detail: "Successfully imported", sticky: false, closable: false }
                ]);
            }
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
        .finally(() => {
            setLoading(false);
        })

    };

    return (
        <div className="flex flex-wrap align-items-baseline flex-column gap-4 m-4">
            <div className="card flex flex-column justify-content-center align-items-center">
                <Messages ref={msgs} />
            </div>

            <InputText 
                    id="fileInput" 
                    type="file" 
                    onChange={handleFileChange} 
                    accept=".yaml,.yml" 
                />
            <Button label="Upload" icon="pi pi-upload" onClick={handleUpload} />
            {loading && (
                <div className="spinner-container">
                    <ProgressSpinner 
                        style={{ width: '50px', height: '50px' }} 
                        strokeWidth="8" 
                        animationDuration=".5s" 
                    />
                </div>
            )}
        </div>
    );
}   
