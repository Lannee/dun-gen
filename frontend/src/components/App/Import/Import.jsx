import { useNavigate } from 'react-router-dom';

import { Button } from 'primereact/button';


export default function Import() {
	const navigate = useNavigate();

    const handleImportFile = _ => {
        setTimeout(() => {
            navigate('file')
            navigate(0)
        }, 0)
    }
    
    const handleHistory = _ => {
        setTimeout(() => {
            navigate('history')
            navigate(0)
        }, 0)
    }
    
    return (
        <div className="flex flex-wrap align-items-baseline flex-column gap-4 m-4">
            <Button onClick={handleImportFile} label="Import data via file" icon="pi pi-file-import"></Button>
            <Button onClick={handleHistory} label="Get history of data imports" icon="pi pi-history"></Button>
        </div>
    );
}
