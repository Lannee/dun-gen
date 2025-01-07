import { useState } from 'react';
import { Dropdown } from 'primereact/dropdown';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';


export default function Add({ getToken }) {
    return (
        <>
            <div className="flex flex-wrap align-items-center gap-4 m-4">
                <a href="add/person">
                    <Button label="Add Person" icon="pi pi-plus"></Button>
                </a>
                <a href="add/coordinates">
                    <Button label="Add Coordinates" icon="pi pi-plus"></Button>
                </a>
                <a href="add/location">
                    <Button label="Add Location" icon="pi pi-plus"></Button>
                </a>
            </div>
        </>
    );
}
