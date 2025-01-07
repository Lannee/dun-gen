import { useState, useRef, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";

import { Button } from 'primereact/button';
import { Messages } from 'primereact/messages';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';


export default function Approve({ getToken }) {
    const msgs = useRef(null);
    const navigate = useNavigate();
    const [results, setResults] = useState([]);
    
    const deleteRow = (id) => {
        setResults((prevResults) => prevResults.filter((result) => result.id !== id));
    };

    const handleApproveClick = id => {
        const data = {
            id: id,
            token: getToken()
        };

        msgs.current.clear();
        axios.post(`http://localhost:8080/api/v1/admin/approve`, data)
            .then(res => {
                console.log(res.data);
                msgs.current.show([
                    { sticky: false, life: 2000, severity: 'success', summary: 'Success', detail: 'Successfully approved', closable: false },
                ])
                deleteRow(id)
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
    }

    const handleRejectClick = id => {
        const data = {
            id: id,
            token: getToken()
        };

        msgs.current.clear();
        axios.post(`http://localhost:8080/api/v1/admin/reject`, data)
            .then(res => {
                console.log(res.data);
                msgs.current.show([
                    { sticky: false, life: 2000, severity: 'success', summary: 'Success', detail: 'Successfully reject', closable: false },
                ])
                deleteRow(id)
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
    }

    useEffect(() => {
        let data = {
            token: getToken()
        }
        msgs.current.clear();

        axios.post(`http://localhost:8080/api/v1/admin/requests`, data)
            .then(res => {
                setResults(res.data);
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
    }, [])

    const approveTemplate = (rowData) => {
        return (
            <Button icon="pi pi-check" className="p-button-success" onClick={() => handleApproveClick(rowData.userId)} />
        );
    };
    
    const rejectTemplate = (rowData) => {
        return (
            <Button icon="pi pi-times" className="p-button-danger" onClick={() => handleRejectClick(rowData.userId)} />
        );
    };

    return (
        <>
            <div className="card flex flex-column justify-content-center align-items-center">
				<Messages ref={msgs} />
			</div>
            <div style={{"height": "50px"}}></div>
                <DataTable value={results} editMode="row" dataKey="id" showGridlines paginator rows={5} rowsPerPageOptions={[5, 10, 25, 50]} header="Users to Approve">
                    <Column field="userId" header="User ID" sortable/>
                    <Column field="name" header="Username" sortable/>
                    <Column field="status" header="Status" />
                    <Column body={approveTemplate} header="Approve" />
                    <Column body={rejectTemplate} header="Reject" />
                </DataTable>
            <div style={{"height": "50px"}}></div>
        </>
    );
}