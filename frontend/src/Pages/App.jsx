import axios from "axios";
import PropTypes from 'prop-types';

import { Messages } from 'primereact/messages';
import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from 'primereact/button';


import ResultTable from '../components/App/ResultTable';

import './src/css/App.css';
import './src/js/canvas';
import React from 'react';
import { drawIsHitPoint, drawPoint } from './src/js/canvas_points'

export default function App({ getToken, isAdmin }) {
	const msgs = useRef(null);
	const navigate = useNavigate();

	const handleAddClick = _ => {
		setTimeout(() => {
			navigate('/add', { replace: true })
			navigate(0)
		}, 0)
	}
	
	const handleSpecialClick = _ => {
		setTimeout(() => {
			navigate('/special', { replace: true })
			navigate(0)
		}, 0)
	}

	const handleImportClick = _ => {
		setTimeout(() => {
			navigate('/import', { replace: true })
			navigate(0)
		}, 0)
	}
	
	const handleThrowClick = async e => {
		e.preventDefault();

		// if (yValue < -3 || yValue > 5) {
		// 	setIsCorrectY(false)
		// 	setYValue(-3)
		// }

		// if (!(isCorrectX && isCorrectY && isCorrectR)) {
		// 	msgs.current.show([
		// 		{ sticky: false, life: 5000, severity: 'error', summary: 'Error', detail: 'You chose invalid values in selectors. Fix all error messages', closable: false },
		// 	])
		// 	return;
		// }

		let data = {
			// x: parseFloat(xValue.name),
			// y: parseFloat(yValue),
			// r: parseFloat(rValue.name),
			token: getToken()
		}

		axios.post(`http://localhost:8080/api/v1/person/add`, data)
			.then(res => {
				console.log(res.status);
				console.log(res.data);

				let point = res.data

				let isHitBool = true
				if (point.isHit == "MISS") {
					isHitBool = false
				}
				drawIsHitPoint(
					point.x,
					point.y,
					point.r,
					isHitBool,
					"canvas", "canvas1")

				// setResults(results => (
				// 	[...results, res.data]
				// ))

				msgs.current.show([
					{ sticky: false, life: 2000, severity: 'success', summary: 'Success', detail: 'Successfully Thrown', closable: false },
				])
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
	}

	return (
		<div className="App">
			<Button 
                label="Create objects" 
                icon="pi pi-plus" 
                onClick={handleAddClick} 
                className="top-right-button" 
				style={{ position: "absolute", top: "85px", right: "20px" }}
            />
			<Button 
                label="Special Operations" 
                icon="pi pi-star" 
                onClick={handleSpecialClick} 
                className="top-right-button" 
				style={{ position: "absolute", top: "85px", right: "200px" }}
            />
			<Button 
                label="Import Data" 
                icon="pi pi-file-import" 
                onClick={handleImportClick} 
                className="top-right-button" 
				style={{ position: "absolute", top: "85px", right: "415px" }}
            />
			<div style={{"height": "30px"}}></div>
			<div className="card flex flex-column justify-content-center align-items-center">
				<Messages ref={msgs} />
			</div>
			<div style={{"height": "50px"}}></div>
			<div className="wrapper align-middle">
				<ResultTable getToken={getToken} isAdmin={isAdmin} />
			</div>
			<div style={{"height": "50px"}}></div>
		</div>
	);
}

App.propTypes = {
	getToken: PropTypes.func.isRequired
};
