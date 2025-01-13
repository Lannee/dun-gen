import { useState, useEffect, useRef } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

import { Button } from 'primereact/button';
import { Messages } from 'primereact/messages';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { FilterMatchMode, FilterService } from 'primereact/api';
import { InputNumber } from 'primereact/inputnumber';


export default function SpecialHeightFilter({ getToken }) {
    const [results, setResults] = useState();
    const msgs = useRef(null);
	const navigate = useNavigate();
    const [filters, setFilters] = useState({
        height: { value: null, matchMode: FilterMatchMode.CUSTOM },
    });
    
    const heightFilterTemplate = (options) => {
        const [from, to] = options.value ?? [null, null];

        return (
            <div className="flex gap-1">
                <InputNumber value={from} onChange={(e) => options.filterApplyCallback([e.value, to])} className="w-full" placeholder="from" />
                <InputNumber value={to} onChange={(e) => options.filterApplyCallback([from, e.value])} className="w-full" placeholder="to" />
            </div>
        );
    };

    useEffect(() => {
        msgs.current.clear();
		axios.post(`http://localhost:8080/api/v1/person/all`, { token: getToken() })
			.then(res => {
				console.log(res.status);
				console.log(res.data);
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
	}, [])

    const handleSpecialClick = _ => {
		setTimeout(() => {
			navigate('/special', { replace: true })
			navigate(0)
		}, 0)
	}

    FilterService.register('custom_height', (value, filters) => {
        const [from, to] = filters ?? [null, null];
        if (from === null && to === null) return true;
        if (from !== null && to === null) return from <= value;
        if (from === null && to !== null) return value <= to;
        return from <= value && value <= to;
    });

    return (
        <>
            <div className="m-4">
                <div className="mt-7 card flex flex-column justify-content-center align-items-center">
                    <Messages ref={msgs} />
                </div>
                <DataTable className="mt-4"
                    value={results}
                    dataKey="id"
                    showGridlines 
                    header="Person List"
                    filters={filters} filterDisplay="row" >
                        <Column field="id" header="ID" />
                        <Column style={{ maxWidth: '10em' }} field="name" header="Name" />
                        <Column field="coordinates_id" header="Coordinates ID" />
                        <Column field="eyeColor" header="Eye Color" />
                        <Column field="hairColor" header="Hair Color" />
                        <Column field="location_id" header="Location ID" />
                        <Column filter filterPlaceholder="Search by height" showFilterMenu={false} filterElement={heightFilterTemplate} field="height" header="Height (cm)" style={{ maxWidth: '10em' }} />
                        <Column field="nationality" header="Nationality" />
                </DataTable>
            </div>
            <Button 
                label="Back to Special Operations" 
                icon="pi pi-arrow-left" 
                onClick={handleSpecialClick} 
                className="top-right-back" 
				style={{ position: "absolute", top: "85px", right: "20px" }}
            />
        </>
    );
}
