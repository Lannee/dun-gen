import { useState, useRef, useEffect } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

import { Button } from 'primereact/button';
import { Messages } from 'primereact/messages';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

export default function ImportHistory({ getToken }) {
    const msgs = useRef(null);
    const [imports, setImports] = useState([])

    useEffect(() => {
        msgs.current.clear();
        axios.post('http://localhost:8080/api/v1/import/history', { token: getToken() })
        .then(res => {
            setImports(res.data);
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
	}, [])


    // const handleDowloadClick = async (objectName) => {
    //     let data = {
    //         token: getToken()
    //     }
    //     msgs.current.clear();
    //     axios.post(`http://localhost:8080/api/v1/import/download/` + objectName, data)
    //         .then(res => {
    //             console.log(res)
    //             msgs.current.show([
    //                 { sticky: false, life: 2000, severity: 'success', summary: 'Success', detail: res.data.message, closable: false },
    //             ])
    //         })
    //         .catch(function (error) {
    //             let myError = "";
    //             if (error.response) {
    //                 // The request was made and the server responded with a status code
    //                 // that falls out of the range of 2xx
                    
    //                 myError = error.response.data.message
    //             } else {
    //                 // Something happened in setting up the request that triggered an Error
    //                 console.log('Error', error.message);
    //                 myError = "An error during request setting up has happened"
    //             }
    //             msgs.current.replace([
    //                 { severity: 'error', life: 5000, summary: 'Error', detail: myError, sticky: false, closable: false }
    //             ]);
    //         });
    // }

    // const dowloadTemplate = (rowData) => {
    //     return (
    //         <Button icon="pi pi-download" className="p-button-success" onClick={() => handleDowloadClick(rowData.objectName)} />
    //     );
    // }; 

    return (
        <div className="m-4">
            <h1>History of imports </h1>
            <div className="card flex flex-column justify-content-center align-items-center">
                <Messages ref={msgs} />
            </div>
            <DataTable value={imports} removableSort showGridlines paginator rows={5} rowsPerPageOptions={[5, 10, 25, 50]} >
                <Column field="status" header="Status" />
                <Column field="userName" header="Username" />
                <Column field="count" header="Count of imported objects" sortable />
                <Column field="time" header="Time" sortable />
                <Column field="objectName" header="fileHash" />
                {/* <Column body={dowloadTemplate} header="Download log" /> */}
            </DataTable>
        </div>
    );
}