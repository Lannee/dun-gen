import { Outlet } from 'react-router';
import PropTypes from 'prop-types';

import NavigationBar from './Navbar.jsx';
import Introduction from './Introduction.jsx';

import './src/css/header.css';

export default function Header({ getToken, logout, isAdmin }) {
    return (
        <>
            <header>
                <NavigationBar start={<Introduction />} getToken={getToken} logout={logout} isAdmin={isAdmin}/>
            </header>
            
            <div className="content">
                <Outlet />
            </div>
        </>
    );
}

Header.propTypes = {
    getToken: PropTypes.func.isRequired,
    logout: PropTypes.func.isRequired,
};