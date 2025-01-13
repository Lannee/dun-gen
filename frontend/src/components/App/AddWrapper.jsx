import { useNavigate } from 'react-router-dom';
import { Button } from 'primereact/button';


export default function AddWrapper({ Component, getToken }) {
    const navigate = useNavigate();

    const handleClick = _ => {        
		setTimeout(() => {
			navigate('/add', { replace: true })
			navigate(0)
		}, 0)
	}

    return (
        <>
            <Button 
                    label="Back to Create objects" 
                    icon="pi pi-chevron-left" 
                    onClick={handleClick} 
                    className="top-right-button" 
                    style={{ position: "absolute", top: "85px", right: "20px" }}
                />
            <div className="flex flex-wrap align-items-center gap-4 m-4">
                <Component getToken={getToken} />
            </div>
        </>
    )
}