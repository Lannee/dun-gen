import { useNavigate } from 'react-router-dom';

import { Button } from 'primereact/button';


export default function Special() {
	const navigate = useNavigate();

    const handleMaxID = _ => {
        setTimeout(() => {
            navigate('max_id')
            navigate(0)
        }, 0)
    }
    
    const handleNameFilter = _ => {
        setTimeout(() => {
            navigate('name_filter')
            navigate(0)
        }, 0)
    }
    
    const handleHeightFilter = _ => {
        setTimeout(() => {
            navigate('height_filter')
            navigate(0)
        }, 0)
    }
    
    const handleHairColorCount = _ => {
        setTimeout(() => {
            navigate('hair_color_count')
            navigate(0)
        }, 0)
    }
    
    const handleEyeColorCount = _ => {
        setTimeout(() => {
            navigate('eye_color_count')
            navigate(0)
        }, 0)
    }
    
    return (
        <div className="flex flex-wrap align-items-baseline flex-column gap-4 m-4">
            <Button onClick={handleMaxID} label="Get object with max ID" icon="pi pi-sort-amount-up"></Button>
            <Button onClick={handleNameFilter} label="Get objects array filtered by name" icon="pi pi-filter"></Button>
            <Button onClick={handleHeightFilter} label="Get objects array filtered by height" icon="pi pi-filter"></Button>
            <Button onClick={handleHairColorCount} label="Get count of objects with special hair color" icon="pi pi-palette"></Button>
            <Button onClick={handleEyeColorCount} label="Get count of objects with special eye color" icon="pi pi-palette"></Button>
        </div>
    );
}
