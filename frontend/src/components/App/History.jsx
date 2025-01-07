import axios from "axios";

import { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Messages } from 'primereact/messages';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

export default function History({ getToken, object }) {
    const msgs = useRef(null);

    const { id } = useParams(); // Get the ID from the URL
    const [history, setHistory] = useState([])

    useEffect(() => {
        msgs.current.clear();
		axios.post('http://localhost:8080/api/v1/' + object + '/history', { id: id, token: getToken() })
			.then(res => {
				setHistory(res.data);
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

    return (
        <div className="m-4">
            <h1>History of object {object} with ID {id}</h1>
            <div className="card flex flex-column justify-content-center align-items-center">
                <Messages ref={msgs} />
            </div>
            <DataTable value={history} showGridlines paginator rows={5} rowsPerPageOptions={[5, 10, 25, 50]} >
                <Column field="userName" header="Username" />
                <Column field="time" header="Time" sortable />
            </DataTable>
        </div>
    );
}
